package com.example.polydraw;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.Point;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffXfermode;
import android.util.AttributeSet;
import android.util.SparseArray;
import android.view.MotionEvent;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;

import com.example.polydraw.Socket.SocketIO;
import com.github.nkzawa.socketio.client.IO;
import com.github.nkzawa.socketio.client.Socket;
import com.google.gson.Gson;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.IOException;
import java.io.ObjectOutputStream;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.List;

public class DrawingCanvas extends View {

    private List<Stroke> _allStroke;
    private SparseArray<Stroke> activeStrokes;
    private List<Point> _allPoints;
    private boolean eraser = false;
    private int paintColor = Color.BLACK;
    private Paint.Cap capOption = Paint.Cap.SQUARE;
    private int capWidth = 5;

    private SocketIO socket;

    public Canvas mCanvas;
    public Bitmap mBitmap;
    private Paint mBitmapPaint;


    public DrawingCanvas (Context context, AttributeSet attrs){
        super(context,attrs);
        _allStroke = new ArrayList<Stroke>();
        activeStrokes = new SparseArray<Stroke>();
        _allPoints = new ArrayList<Point>();
        socket = new SocketIO();
        socket.initInstance("1234");

        setFocusable(true);
        setFocusableInTouchMode(true);
        setBackgroundColor(Color.TRANSPARENT);
        setLayerType(LAYER_TYPE_HARDWARE, null);
        mBitmapPaint = new Paint(Paint.DITHER_FLAG);


    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
        mBitmap = Bitmap.createBitmap(w, h, Bitmap.Config.ARGB_8888);
        mCanvas = new Canvas(mBitmap);
    }

    @Override
    protected void onDraw(Canvas canvas){
        if (_allStroke != null) {
            for (Stroke stroke: _allStroke) {
                if (stroke != null) {
                    Path path = stroke.getPath();
                    Paint painter = stroke.getPaint();
                    if ((path != null) && (painter != null)) {
                        canvas.drawBitmap(mBitmap, 0,0, mBitmapPaint);
                        canvas.drawPath(path, painter);
                    }
                }
            }
        }
    }

    @Override
    public boolean onTouchEvent(MotionEvent event){
        final int action = event.getActionMasked();
        final int pointerCount = event.getPointerCount();

        switch(action) {
            case MotionEvent.ACTION_DOWN:
                if (eraser) {
                    eraseStroke((int) event.getX(), (int) event.getY(),event.getPointerId(0));
                } else {
                    pointDown((int) event.getX(), (int) event.getY(), event.getPointerId(0));
                }
                break;

            case MotionEvent.ACTION_MOVE:
                if (eraser) {
                    for (int pc = 0; pc < pointerCount; pc++) {
                       pointMove((int) event.getX(pc), (int) event.getY(pc),event.getPointerId(pc));
                    }
                } else {
                    for (int pc = 0; pc < pointerCount; pc++) {
                        pointMove((int) event.getX(pc), (int) event.getY(pc), event.getPointerId(pc));
                    }
                }
                break;

            case MotionEvent.ACTION_POINTER_DOWN:
                if (eraser) {
                    for (int pc = 0; pc < pointerCount; pc++) {
                        eraseStroke((int)event.getX(pc), (int)event.getY(pc),event.getPointerId(pc));
                    }
                } else {
                    for (int pc = 0; pc < pointerCount; pc++) {
                        pointDown((int)event.getX(pc), (int)event.getY(pc), event.getPointerId(pc));
                    }
                }
                break;

            case MotionEvent.ACTION_UP:
                for (int pc = 0; pc < pointerCount; pc++) {
                    pointUp((int)event.getX(pc), (int)event.getY(pc), event.getPointerId(pc));
                }
                break;

            case MotionEvent.ACTION_POINTER_UP:
                break;

            default:
                return false;
        }

        invalidate();
        return true;
    }

    private void pointDown(int x, int y, int id) {
        Paint paint = new Paint();
        paint.setStyle(Paint.Style.STROKE);
        paint.setStrokeWidth(capWidth);
        paint.setAntiAlias(true);
        paint.setColor(paintColor);
        paint.setStrokeJoin(Paint.Join.ROUND);
        paint.setStrokeCap(capOption);

        Point pt = new Point(x,y);
        _allPoints.add(pt);

        if(eraser){
            if(_allPoints.size() == 15){
                String json = new Gson().toJson(_allPoints);
                socket.getSocket().emit("SegmentErasing", "General", json);
                _allPoints.clear();
                _allPoints = new ArrayList<Point>();
            }

        }
        else{
            if(_allPoints.size() == 15){
                String json = new Gson().toJson(_allPoints);
                socket.getSocket().emit("StrokeDrawing", "General", json);
                _allPoints.clear();
                _allPoints = new ArrayList<Point>();
            }

        }

        Stroke stroke = new Stroke(paint);
        stroke.addPoint(pt);
        activeStrokes.put(id, stroke);
        _allStroke.add(stroke);
    }

    private void pointMove(int x, int y, int id) {
        //retrieve the stroke and add new point to its path
        Stroke stroke = activeStrokes.get(id);
        if (stroke != null) {
            Point pt = new Point(x, y);
            stroke.addPoint(pt);
            _allPoints.add(pt);

            if(eraser){
                if(_allPoints.size() == 15){
                    String json = new Gson().toJson(_allPoints);
                    socket.getSocket().emit("SegmentErasing", "General", json);
                    _allPoints.clear();
                    _allPoints = new ArrayList<Point>();
                }

            }
            else {
                if (_allPoints.size() == 15) {
                    String json = new Gson().toJson(_allPoints);
                    socket.getSocket().emit("StrokeDrawing", "General", json);
                    _allPoints.clear();
                    _allPoints = new ArrayList<Point>();
                }
            }
        }
    }

    private void eraseStroke(int x, int y, int id){
        Paint paint = new Paint();
        paint.setStrokeWidth(25);
        paint.setAntiAlias(true);
        paint.setStyle(Paint.Style.STROKE);
        paint.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.CLEAR));
        paint.setStrokeJoin(Paint.Join.ROUND);

        Point pt = new Point(x,y);
        Stroke stroke = new Stroke(paint);
        stroke.addPoint(pt);
        activeStrokes.put(id, stroke);
        _allStroke.add(stroke);
    }

    private void pointUp(int x, int y, int id) {
        Paint paint = new Paint();
        paint.setStyle(Paint.Style.STROKE);
        paint.setStrokeWidth(capWidth);
        paint.setAntiAlias(true);
        paint.setColor(paintColor);
        paint.setStrokeJoin(Paint.Join.ROUND);
        paint.setStrokeCap(capOption);

        Point pt = new Point(x,y);
        _allPoints.add(pt);

        if(eraser){
            String json = new Gson().toJson(_allPoints);
            socket.getSocket().emit("SegmentErasing", "General", json);
            _allPoints.clear();
            _allPoints = new ArrayList<Point>();

        }
        else{
            String json = new Gson().toJson(_allPoints);
            socket.getSocket().emit("StrokeDrawing", "General", json);
            _allPoints.clear();
            _allPoints = new ArrayList<Point>();

        }

        Stroke stroke = new Stroke(paint);
        stroke.addPoint(pt);
        activeStrokes.put(id, stroke);
        _allStroke.add(stroke);
    }

    public void setErase(boolean isErase){
        eraser = isErase;

    }

    public void setDrawingColor(int newColor){
        socket.getSocket().emit("CouleurSelectionnee", "General", newColor);
        paintColor = newColor;

    }

    public void setWidth(int width){
        socket.getSocket().emit("TailleTrait", "General", width);
        capWidth = width;

    }

    public void setPencilTip(String cap){
        if(cap.equals("Ronde")){
            socket.getSocket().emit("PointeSelectionnee", "General", "Ronde");
            capOption = Paint.Cap.ROUND;

        }
        else{
            socket.getSocket().emit("PointeSelectionnee", "General", "Carre");
            capOption = Paint.Cap.SQUARE;
        }

    }

    public Bitmap getBitmap(){

        this.setDrawingCacheEnabled(true);
        this.buildDrawingCache();
        Bitmap bmp = Bitmap.createBitmap(this.getDrawingCache());
        this.setDrawingCacheEnabled(false);

        return bmp;
    }

    public int getPaintColor() {
        return paintColor;
    }

    public int getCapWidth() {
        return capWidth;
    }

    public Paint.Cap getCapOption() {
        return capOption;
    }
}
