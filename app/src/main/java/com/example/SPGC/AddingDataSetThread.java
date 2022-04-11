package com.example.SPGC;

import android.os.AsyncTask;

import com.github.mikephil.charting.data.LineData;

import java.util.ArrayList;

public class AddingDataSetThread extends AsyncTask<Integer, Void, LineData> {
    public static boolean isRunning ;
    @Override
    protected synchronized LineData doInBackground(Integer... integers) {
        int direction = integers[0];
        LineData lindData = new LineData();
        float scale = MainActivity.getVisibleXRange();
        float lowestX = MainActivity.getLineData().getXMin();
        float highestX = MainActivity.getLineData().getXMax();
        //if the scale attribute is > 51 which can be the case because
        // the getLowestX and highestX do not work properly sometimes
        // we cancel the task by returning null;
        if ( scale <= 0 || scale > 51 || MyGestureListener.distance(lowestX,highestX) > 200)
            return null;
        isRunning = true;

        // start compute values for function depending on direction of scrolling
            ArrayList<MFunction> functionsArrayList = MainActivity.getFunctionsList();
            if (functionsArrayList.size() <= 0)
                return null;
            MFunction MFunction;
            for (int i = 0; i < functionsArrayList.size(); i++) {
                MFunction = functionsArrayList.get(i);
                if (direction == 1)
                lindData.addDataSet(MFunction.computeValues
                        (lowestX, highestX, scale, 1));
                else if (direction == -1)
                    lindData.addDataSet(MFunction.computeValues
                            (lowestX, highestX, scale, -1));
                else if (direction == 0 )
                    lindData.addDataSet(MFunction.computeValues
                            (lowestX, highestX, scale, 0));
            }
        return lindData;
    }

    @Override
    protected void onPostExecute(LineData lineData) {
        // setting a new LineData to the LineChart and refresh it;
        if (lineData != null) {
            MainActivity.getLineChart().setData(lineData);
            MainActivity.getLineChart().notifyDataSetChanged();
            MainActivity.getLineChart().invalidate();
            isRunning = false;
        }
    }

}

