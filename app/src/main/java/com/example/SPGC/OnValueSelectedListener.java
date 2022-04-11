package com.example.SPGC;


import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineDataSet;
import com.github.mikephil.charting.highlight.Highlight;
import com.github.mikephil.charting.listener.OnChartValueSelectedListener;


public class OnValueSelectedListener implements OnChartValueSelectedListener {
    private LineDataSet lineDataSet = null;
    @Override
    public void onValueSelected(Entry e, Highlight h) {
        lineDataSet = (LineDataSet) MainActivity.getLineChart().getData().getDataSetByIndex(h.getDataSetIndex());
        lineDataSet.setLineWidth(4);
    }

    @Override
    public void onNothingSelected() {
        if (lineDataSet != null) {
            lineDataSet.setLineWidth(3);
            lineDataSet = null;
        }
    }
}
