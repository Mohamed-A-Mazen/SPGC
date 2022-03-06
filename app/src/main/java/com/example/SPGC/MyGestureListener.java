package com.example.SPGC;


import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.View;


import com.github.mikephil.charting.data.LineData;


public class MyGestureListener implements View.OnTouchListener, GestureDetector.OnGestureListener {
    private final GestureDetector gestureDetector = new GestureDetector(this);
    private float xScale = 20;


    @Override
    public boolean onTouch(View v, MotionEvent event) {

        MainActivity.listener.onTouch(v, event);

        AddingDataSetThread backgroundTask;
        LineData lineData = MainActivity.getLineData();

        // for reasons i don't know the viewPort of the lineChart sometimes moves
        // spontaneously because of that we want to refresh the chart when that happens

        if (MainActivity.getLowestXPoint() > lineData.getXMax() ||
                MainActivity.getHighestXPoint() < lineData.getXMin() ||
                distance(lineData.getXMax(),lineData.getXMin()) > 200)
        {
            backgroundTask = new AddingDataSetThread();
            backgroundTask.execute(0);
        }
        // if there is a task that is currently running return true;
        if (AddingDataSetThread.isRunning)
            return true;


        gestureDetector.onTouchEvent(event);
        return true;
    }

    @Override
    public boolean onScroll(MotionEvent e1, MotionEvent e2, float distanceX, float distanceY) throws NullPointerException {
        AddingDataSetThread backgroundTask = null;
        LineData lineData = MainActivity.getLineData();

        // if the user rescales the lineChart we refresh the dataSets causing them to be more precise and more curvy;
        if (Math.abs(xScale - (MainActivity.getVisibleXRange())) > 2) {
            backgroundTask = new AddingDataSetThread();
            backgroundTask.execute(0);
            xScale = MainActivity.getVisibleXRange();
        }
        // adding new data depending on scroll direction;
        else if (
        lineData.getXMax() - MainActivity.getVisibleXRange()/3*2 < MainActivity.getHighestXPoint() && e1.getX() > e2.getX()) {
            backgroundTask = new AddingDataSetThread();
            backgroundTask.execute(1);

        }
        else if (
        lineData.getXMin() + MainActivity.getVisibleXRange()/3*2 > MainActivity.getLowestXPoint() && e1.getX() < e2.getX()) {
            backgroundTask = new AddingDataSetThread();
            backgroundTask.execute(-1);
        }
        return true;
    }

    @Override
    public boolean onDown(MotionEvent e) {

        return false;
    }

    @Override
    public void onShowPress(MotionEvent e) {
    }

    @Override
    public boolean onSingleTapUp(MotionEvent e) {
        return false;
    }


    @Override
    public void onLongPress(MotionEvent e) {
    }

    @Override
    public boolean onFling(MotionEvent e1, MotionEvent e2, float velocityX, float velocityY) {

        // modify the axises max and min values

        float minimumVisibleX = MainActivity.getLowestXPoint();
        float maximumVisibleX = MainActivity.getLineChart().getHighestVisibleX();
        float axisMaximum = MainActivity.getLineChart().getXAxis().getAxisMaximum();
        float axisMinimum = MainActivity.getLineChart().getXAxis().getAxisMinimum();
        float maximumVisibleYValue = MainActivity.getHighestVisibleY();
        float minimumVisibleYValue = MainActivity.getLowestVisibleY();
        float maximumYAxis = MainActivity.getLineChart().getAxisLeft().mAxisMaximum;
        float minimumYAxis = MainActivity.getLineChart().getAxisLeft().mAxisMinimum;

        AxisReseter axisReseter = null ;

        if ((distance(maximumVisibleX, axisMaximum) < 100 && e1.getX() > e2.getX())) {
            axisReseter = new AxisReseter(true, 0,
                    (minimumVisibleYValue + maximumVisibleYValue) / 2);
        } else if ((distance(minimumVisibleX, axisMinimum) < 100) && e1.getX() < e2.getX()) {
            axisReseter = new AxisReseter(false, 0,
                    (minimumVisibleYValue + maximumVisibleYValue) / 2);
        }
        if (distance(maximumVisibleYValue, maximumYAxis) < 100) {
            axisReseter = new AxisReseter(true, 1,
                    (minimumVisibleYValue + maximumVisibleYValue) / 2);
        } else if (distance(minimumVisibleYValue, minimumYAxis) < 100) {
            axisReseter = new AxisReseter(false, 1,
                    (minimumVisibleYValue + maximumVisibleYValue) / 2);
        }
        if (axisReseter != null) {
            axisReseter.start();
        }
        return false;
    }


    public static float distance(float x0, float x1) {
        //calculating distance between two given points
        float distance;
        if (x0 < 0 && x1 >= 0) {
            distance = Math.abs(x0) + x1;
        } else if (x0 > 0 && x1 <= 0) {
            distance = x0 + Math.abs(x1);
        } else if (((x0 < 0 && x1 <= 0) && x0 < x1) || ((x0 > 0 && x1 >= 0) && x0 > x1)) {
            distance = Math.abs(x0) - Math.abs(x1);
        } else {
            distance = Math.abs(x1) - Math.abs(x0);
        }
        return distance;
    }
   
}

