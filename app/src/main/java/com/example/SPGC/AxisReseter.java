package com.example.SPGC;


import com.github.mikephil.charting.components.YAxis;


public class AxisReseter extends Thread {
    int resetXAxis;
    boolean resetPositiveDirectionOfAxis;
    float yValue;

    public AxisReseter(boolean resetPositiveDirectionOfAxis, int resetXAxis, float yValue) {
        this.resetPositiveDirectionOfAxis = resetPositiveDirectionOfAxis;
        this.resetXAxis = resetXAxis;
        this.yValue = yValue;
    }

    @Override
    public void run() {
        // resetting the lineChart max and min axis values
        try {
            MainActivity.getLineChart().setDragDecelerationEnabled(false);
            float xCoordinate = MainActivity.getLineChart().getLowestVisibleX();
            if (resetXAxis == 0 && resetPositiveDirectionOfAxis) {
                MainActivity.getLineChart().getXAxis().setAxisMinimum(MainActivity.getLineChart().getXAxis().getAxisMinimum() + 250);
                MainActivity.getLineChart().getXAxis().setAxisMaximum(MainActivity.getLineChart().getXAxis().getAxisMaximum() + 250);
                MainActivity.getLineChart().setVisibleXRangeMaximum(50);
            } else if (resetXAxis == 0 && !resetPositiveDirectionOfAxis) {
                MainActivity.getLineChart().getXAxis().setAxisMinimum(MainActivity.getLineChart().getXAxis().getAxisMinimum() - 250);
                MainActivity.getLineChart().getXAxis().setAxisMaximum(MainActivity.getLineChart().getXAxis().getAxisMaximum() - 250);
            } else if (resetXAxis == 1 && resetPositiveDirectionOfAxis) {
                MainActivity.getLineChart().getAxisLeft().setAxisMinimum(MainActivity.getLineChart().getAxisLeft().getAxisMinimum() + 250);
                MainActivity.getLineChart().getAxisLeft().setAxisMaximum(MainActivity.getLineChart().getAxisLeft().getAxisMaximum() + 250);
            } else if (resetXAxis == 1 && !resetPositiveDirectionOfAxis) {
                MainActivity.getLineChart().getAxisLeft().setAxisMinimum(MainActivity.getLineChart().getAxisLeft().getAxisMinimum() - 250);
                MainActivity.getLineChart().getAxisLeft().setAxisMaximum(MainActivity.getLineChart().getAxisLeft().getAxisMaximum() - 250);
            }
            MainActivity.getLineChart().setVisibleYRangeMaximum(50, YAxis.AxisDependency.LEFT);
            MainActivity.getLineChart().setVisibleYRangeMinimum((float) 0.1, YAxis.AxisDependency.LEFT);
            MainActivity.getLineChart().setVisibleXRangeMaximum(50);
            MainActivity.getLineChart().setVisibleXRangeMinimum((float) 0.1);
            MainActivity.getLineChart().moveViewTo(xCoordinate, yValue, YAxis.AxisDependency.LEFT);

            MainActivity.notifyLineChart();
        } catch (Exception e) {
            e.getMessage();
        }
    }
}
