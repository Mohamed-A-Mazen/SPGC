package com.example.SPGC;


import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineDataSet;
import com.github.mikephil.charting.highlight.Highlight;
import com.github.mikephil.charting.listener.OnChartValueSelectedListener;


public class OnValueSelectedListener implements OnChartValueSelectedListener {
    private LineDataSet lineDataSet = null;
    private boolean isSelected = false;
    @Override
    public void onValueSelected(Entry e, Highlight h) {
        isSelected = true;
        lineDataSet = (LineDataSet) MainActivity.getLineChart().getData().getDataSetByIndex(h.getDataSetIndex());
        lineDataSet.setLineWidth(4);
    }

    @Override
    public void onNothingSelected() {
        if (lineDataSet != null) {
            isSelected = false;
            lineDataSet.setLineWidth(3);
            lineDataSet = null;
        }
    }
}
