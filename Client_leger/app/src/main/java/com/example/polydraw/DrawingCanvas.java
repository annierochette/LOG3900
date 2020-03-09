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

import androidx.appcompat.app.AppCompatActivity;

import java.util.ArrayList;
import java.util.List;

public class DrawingCanvas extends View {

    private List<Stroke> _allStroke;
    private SparseArray<Stroke> activeStrokes;
    private boolean eraser = false;

    public DrawingCanvas (Context context, AttributeSet attrs){
        super(context,attrs);
        _allStroke = new ArrayList<Stroke>();
        activeStrokes = new SparseArray<Stroke>();
        setFocusable(true);
        setFocusableInTouchMode(true);
        setBackgroundColor(Color.TRANSPARENT);
        setLayerType(LAYER_TYPE_HARDWARE, null);

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
        paint.setStrokeWidth(5);
        paint.setAntiAlias(true);
        paint.setColor(Color.BLACK);
        paint.setStrokeJoin(Paint.Join.ROUND);
        paint.setStrokeCap(Paint.Cap.SQUARE);

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

    public void setDrawingColor(Paint paint, int newColor){
        paint.setColor(newColor);

    }

    public void setWidth(Paint paint, int width){
        paint.setStrokeWidth(width);

    }

    public void setPencilTip(Paint paint){
        Paint.Cap cap = paint.getStrokeCap();
        if(paint.getStrokeCap() == Paint.Cap.SQUARE){
            paint.setStrokeCap(Paint.Cap.ROUND);

        }
        else{
            paint.setStrokeCap(Paint.Cap.SQUARE);
        }

    }
}
