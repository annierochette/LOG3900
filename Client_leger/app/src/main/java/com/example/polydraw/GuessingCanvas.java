package com.example.polydraw;

import android.content.Context;
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

import com.example.polydraw.Socket.SocketIO;
import com.github.nkzawa.emitter.Emitter;
import com.github.nkzawa.socketio.client.IO;
import com.github.nkzawa.socketio.client.Socket;
import com.google.gson.Gson;

import org.json.JSONObject;

import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.List;

public class GuessingCanvas extends View {

    private List<Stroke> _allStroke;
    private SparseArray<Stroke> activeStrokes;
    private boolean eraser = false;
    private int paintColor = Color.BLACK;
    private Paint.Cap capOption = Paint.Cap.SQUARE;
    private int capWidth = 5;

    private SocketIO socket;

    public GuessingCanvas (Context context, AttributeSet attrs){
        super(context,attrs);
        _allStroke = new ArrayList<Stroke>();
        activeStrokes = new SparseArray<Stroke>();

        socket = new SocketIO();
        socket.initInstance("1234");

        setFocusable(true);
        setFocusableInTouchMode(true);
        setBackgroundColor(Color.TRANSPARENT);
        setLayerType(LAYER_TYPE_HARDWARE, null);

        socket.getSocket().on("StrokeDrawing", onDrawing);
        socket.getSocket().on("StrokeErasing", onEraserToggle);

    }

    @Override
    protected void onDraw(Canvas canvas){
        if (_allStroke != null) {
            for (Stroke stroke: _allStroke) {
                if (stroke != null) {
                    Path path = stroke.getPath();
                    Paint painter = stroke.getPaint();
                    if ((path != null) && (painter != null)) {
                        canvas.drawPath(path, painter);
                    }
                }
            }
        }
    }

    @Override
    public boolean onTouchEvent(MotionEvent event){
//        final int action = event.getActionMasked();
//        final int pointerCount = event.getPointerCount();
//
//        switch(action) {
//            case MotionEvent.ACTION_DOWN:
//                if (eraser) {
//                    eraseStroke((int) event.getX(), (int) event.getY(),event.getPointerId(0));
//                } else {
//                    pointDown((int) event.getX(), (int) event.getY(), event.getPointerId(0));
//                }
//                break;
//
//            case MotionEvent.ACTION_MOVE:
//                if (eraser) {
//                    for (int pc = 0; pc < pointerCount; pc++) {
//                        pointMove((int) event.getX(pc), (int) event.getY(pc),event.getPointerId(pc));
//                    }
//                } else {
//                    for (int pc = 0; pc < pointerCount; pc++) {
//                        pointMove((int) event.getX(pc), (int) event.getY(pc), event.getPointerId(pc));
//                    }
//                }
//                break;
//
//            case MotionEvent.ACTION_POINTER_DOWN:
//                if (eraser) {
//                    for (int pc = 0; pc < pointerCount; pc++) {
//                        eraseStroke((int)event.getX(pc), (int)event.getY(pc),event.getPointerId(pc));
//                    }
//                } else {
//                    for (int pc = 0; pc < pointerCount; pc++) {
//                        pointDown((int)event.getX(pc), (int)event.getY(pc), event.getPointerId(pc));
//                    }
//                }
//                break;
//
//            case MotionEvent.ACTION_UP:
//                break;
//
//            case MotionEvent.ACTION_POINTER_UP:
//                break;
//
//            default:
//                return false;
//        }
//
//        invalidate();
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

//    private void findStroke(int x, int y, int id) {
//        Paint paint = new Paint();
//        paint.setStyle(Paint.Style.FILL);
//        paint.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.CLEAR));
//
//        Stroke stroke = new Stroke(paint);
//
//        for (Stroke existingStroke: _allStroke) {
//            for (Point pt : existingStroke.pointArray) {
//                if (pt.equals(x, y)) {e
//                    stroke = existingStroke;
//                }
//
//            }
//        }
//
//        Stroke erasedStroke = new Stroke(paint, stroke.pointArray);
//        _allStroke.add(erasedStroke);
//        _allStroke.remove(stroke);
//
//
//        invalidate();
//    }

    public void setErase(boolean isErase){
        eraser = isErase;

    }

    public void setDrawingColor(int newColor){
        paintColor = newColor;

    }

    public void setWidth(int width){
        capWidth = width;

    }

    public void setPencilTip(String cap){
        if(cap.equals("Ronde")){
            capOption = Paint.Cap.ROUND;

        }
        else{
            capOption = Paint.Cap.SQUARE;
        }
    }

    private Emitter.Listener onDrawing = new Emitter.Listener() {
        @Override
        public void call(Object... args) {
            System.out.println("Stroke received");

//            JSONObject data = (JSONObject) args[0];
            String data = args[0].toString();
            Gson gson = new Gson();
            Point[] _receivedPoints = gson.fromJson(data, Point[].class);
            System.out.println(_receivedPoints.length);
            System.out.println(_receivedPoints[0].x);
            System.out.println(_receivedPoints[0].y);
            pointDown(_receivedPoints[0].x, _receivedPoints[0].y, 0);
            for(int i = 1; i < _receivedPoints.length; i++){
                pointMove((int) _receivedPoints[i].x, (int) _receivedPoints[i].y, 0);

            }
            postInvalidate();
        }
    };

    private Emitter.Listener onEraserToggle = new Emitter.Listener() {
        @Override
        public void call(Object... args) {
            Boolean data = (Boolean) args[0];
            setErase(data);
        }
    };

}

