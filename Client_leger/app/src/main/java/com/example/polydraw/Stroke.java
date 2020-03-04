package com.example.polydraw;

import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.Point;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.List;

public class Stroke {
    private Path _path;
    private Paint _paint;
    public List<Point> pointArray;

    public Stroke (Paint paint) {
        pointArray = new ArrayList<Point>();
        _paint = paint;
    }

    public Path getPath() {
        return _path;
    }

    public Paint getPaint() {
        return _paint;
    }

    public void addPoint(Point pt) {
        if(_path == null) {
            _path = new Path();
            _path.moveTo(pt.x, pt.y);
        } else {
         _path.lineTo(pt.x,pt.y);
        }
        pointArray.add(pt);
    }
}

