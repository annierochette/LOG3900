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
    private int strokeID = 0;
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
        socket.getSocket().on("SegmentErasing", onEraserToggle);
        socket.getSocket().on("CouleurSelectionnee", onColorChange);
        socket.getSocket().on("TailleTrait", onWidthChange);
        socket.getSocket().on("PointeSelectionnee", onTipChange);
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

    public void setErase(boolean isErase){
        eraser = isErase;

    }

    private Emitter.Listener onWidthChange = new Emitter.Listener() {
        @Override
        public void call(Object... args) {
            capWidth = (Integer) args[0];
        }
    };

    private Emitter.Listener onColorChange = new Emitter.Listener() {
        @Override
        public void call(Object... args) {
            paintColor = (Integer) args[0];
        }
    };

    private Emitter.Listener onTipChange = new Emitter.Listener() {
        @Override
        public void call(Object... args) {
            String data = (String) args[0];

            if(data.equals("Ronde")){
                capOption = Paint.Cap.ROUND;
            }
            else{
                capOption = Paint.Cap.SQUARE;
            }
        }
    };

    private Emitter.Listener onDrawing = new Emitter.Listener() {
        @Override
        public void call(Object... args) {
            System.out.println("Stroke received");
            strokeID++;
            String data = args[0].toString();
            Gson gson = new Gson();
            Point[] _receivedPoints = gson.fromJson(data, Point[].class);
            pointDown(_receivedPoints[0].x, _receivedPoints[0].y, strokeID);
            for(int i = 1; i < _receivedPoints.length; i++){
                pointMove((int) _receivedPoints[i].x, (int) _receivedPoints[i].y, strokeID);

            }

            postInvalidate();
        }
    };

    private Emitter.Listener onEraserToggle = new Emitter.Listener() {
        @Override
        public void call(Object... args) {
            System.out.println("Erasing stroke received");
            setErase(true);

//            JSONObject data = (JSONObject) args[0];
            String data = args[0].toString();
            Gson gson = new Gson();
            Point[] _receivedPoints = gson.fromJson(data, Point[].class);
            eraseStroke(_receivedPoints[0].x, _receivedPoints[0].y, 0);
            for(int i = 1; i < _receivedPoints.length; i++){
                pointMove((int) _receivedPoints[i].x, (int) _receivedPoints[i].y, 0);

            }

            postInvalidate();
        }
    };

}

