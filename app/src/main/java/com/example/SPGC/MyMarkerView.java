package com.example.SPGC;

import android.annotation.SuppressLint;
import android.content.Context;
import android.widget.TextView;

import com.github.mikephil.charting.components.MarkerView;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.highlight.Highlight;

public class MyMarkerView extends MarkerView {
    private final TextView highlightedView;

    @SuppressLint("ResourceType")

    public MyMarkerView(Context context, int layoutResource) {
        super(context, layoutResource);
        highlightedView = findViewById(R.id.highlightedEntry);
    }

    @Override
    public void refreshContent(Entry e, Highlight highlight) {
        //we determine the precision of the entry
        int i;
        float length = MainActivity.getVisibleXRange();
        if (length < 10) {
            i = 4;
        } else if (length < 25) {
            i = 3;
        } else if (length < 35) {
            i = 2;
        } else {
            i = 1;
        }
        String x = String.format("%." + i + "f", e.getX());
        String y = String.format("%." + i + "f", e.getY());
        highlightedView.setText(x + "," + y);
    }
}
