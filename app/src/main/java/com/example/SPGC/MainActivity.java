package com.example.SPGC;

import android.annotation.SuppressLint;
import android.os.Build;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {
    static OnValueSelectedListener onValueSelectedListener;
    @SuppressLint("ClickableViewAccessibility")
    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    private static final String TAG = "MainActivity";
    private static final ArrayList<MFunction> functionsList = new ArrayList<>();
    public static Button rightwardArrow;
    public static Button leftwardArrow;
    public static Button functionsButton;
    public static Button deleteButton;
    public static View.OnTouchListener listener;
    @SuppressLint("StaticFieldLeak")
    public static ListView listView;
    private static LineChart lineChart;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        lineChart = findViewById(R.id.lineChart);
        listView = findViewById(R.id.listView);
        //  setting the buttons
        Button button0 = findViewById(R.id.button0);
        Button button1 = findViewById(R.id.button1);
        Button button2 = findViewById(R.id.button2);
        Button button3 = findViewById(R.id.button3);
        Button button4 = findViewById(R.id.button4);
        Button button5 = findViewById(R.id.button5);
        Button button6 = findViewById(R.id.button6);
        Button button7 = findViewById(R.id.button7);
        Button button8 = findViewById(R.id.button8);
        Button button9 = findViewById(R.id.button9);
        Button buttonX = findViewById(R.id.buttonX);
        Button decimalPoint = findViewById(R.id.decimalPoint);
        Button plus = findViewById(R.id.plus);
        Button minus = findViewById(R.id.minus);
        Button divide = findViewById(R.id.divide);
        Button multiply = findViewById(R.id.multiply);
        Button exponent = findViewById(R.id.exponent);
        Button squaredExp = findViewById(R.id.squaredExp);
        Button openBracket = findViewById(R.id.openBracket);
        Button closeBracket = findViewById(R.id.closeBracket);
        Button squareRoot = findViewById(R.id.squareRoot);
        Button pi = findViewById(R.id.pi);
        Button eulerConstant = findViewById(R.id.eulerConstant);
        rightwardArrow = findViewById(R.id.rightwardArrrow);
        leftwardArrow = findViewById(R.id.leftwardArrow);
        deleteButton = findViewById(R.id.deleteButton);
        functionsButton = findViewById(R.id.functionsButton);
        ButtonOnClickListener buttonsOnClickListener = new ButtonOnClickListener(getLayoutInflater());
        button0.setOnClickListener(buttonsOnClickListener);
        button1.setOnClickListener(buttonsOnClickListener);
        button2.setOnClickListener(buttonsOnClickListener);
        button3.setOnClickListener(buttonsOnClickListener);
        button4.setOnClickListener(buttonsOnClickListener);
        button5.setOnClickListener(buttonsOnClickListener);
        button6.setOnClickListener(buttonsOnClickListener);
        button7.setOnClickListener(buttonsOnClickListener);
        button8.setOnClickListener(buttonsOnClickListener);
        button9.setOnClickListener(buttonsOnClickListener);
        buttonX.setOnClickListener(buttonsOnClickListener);
        plus.setOnClickListener(buttonsOnClickListener);
        minus.setOnClickListener(buttonsOnClickListener);
        multiply.setOnClickListener(buttonsOnClickListener);
        divide.setOnClickListener(buttonsOnClickListener);
        exponent.setOnClickListener(buttonsOnClickListener);
        squaredExp.setOnClickListener(buttonsOnClickListener);
        openBracket.setOnClickListener(buttonsOnClickListener);
        closeBracket.setOnClickListener(buttonsOnClickListener);
        pi.setOnClickListener(buttonsOnClickListener);
        squareRoot.setOnClickListener(buttonsOnClickListener);
        eulerConstant.setOnClickListener(buttonsOnClickListener);
        decimalPoint.setOnClickListener(buttonsOnClickListener);
        functionsButton.setOnClickListener(buttonsOnClickListener);
        deleteButton.setOnClickListener(buttonsOnClickListener);
        rightwardArrow.setOnClickListener(buttonsOnClickListener);
        leftwardArrow.setOnClickListener(buttonsOnClickListener);
        listener = lineChart.getOnTouchListener();
        // assigning a gestureListener to the lineChart
        MyGestureListener myGestureListener = new MyGestureListener();
        lineChart.setOnTouchListener(myGestureListener);
        // adding the first function
        MFunction MFunction = new MFunction();
        functionsList.add(MFunction);
        LineDataSet lineDataSet = null;
        lineDataSet = MFunction.computeValues(-10, 20, 10,0);
        LineData lineData = new LineData();
        lineData.addDataSet(lineDataSet);
        // maker view for displaying values of cartesian point when taped
        MyMarkerView myMarkerView = new MyMarkerView(this, R.layout.highlighted_entry_view);
        myMarkerView.setOffset(-myMarkerView.getMeasuredWidth() / 2, -myMarkerView.getMeasuredHeight());
        lineChart.setMarker(myMarkerView);
        // customising the lineChart
        lineChart.setDragDecelerationEnabled(false);
        lineChart.setTouchEnabled(true);
        lineChart.setDoubleTapToZoomEnabled(false);
        lineChart.setData(lineData);
        lineChart.setMaxHighlightDistance(20);
        lineChart.getAxisLeft().setAxisMinimum(-500);
        lineChart.getAxisLeft().setAxisMaximum(500);
        lineChart.getXAxis().setAxisMinimum(-500);
        lineChart.getXAxis().setAxisMaximum(500);
        lineChart.setVisibleXRangeMinimum((float) 0.1);
        lineChart.setVisibleXRangeMaximum(50);
        lineChart.setVisibleYRangeMaximum(50, YAxis.AxisDependency.LEFT);
        lineChart.setVisibleYRangeMinimum((float) 0.1, YAxis.AxisDependency.LEFT);
        lineChart.zoom((float) 2.5, (float) 2.5, -10, -10);
        lineChart.setVisibleYRangeMaximum(50, lineChart.getAxisLeft().getAxisDependency());
        lineChart.setVisibleYRangeMinimum((float) 0.1, lineChart.getAxisLeft().getAxisDependency());
        lineChart.getAxisRight().setEnabled(false);
        lineChart.moveViewTo(-10, 0, lineChart.getAxisLeft().getAxisDependency());
        lineChart.getXAxis().setPosition(XAxis.XAxisPosition.BOTTOM_INSIDE);
        lineChart.getAxisLeft().setPosition(YAxis.YAxisLabelPosition.INSIDE_CHART);
        lineChart.setViewPortOffsets(0, 0, 0, 0);
        lineChart.setDragDecelerationFrictionCoef(0);
        lineChart.setMaxHighlightDistance(30);
        onValueSelectedListener = new OnValueSelectedListener();
        lineChart.setOnChartValueSelectedListener(onValueSelectedListener);
        // setting the adapter of the listView displayed in the ui of the app;
        MyAdapter myAdapter = new MyAdapter(this, R.layout.list_item);
        listView.setAdapter(myAdapter);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main_menu,menu);
        return super.onCreateOptionsMenu(menu);
    }
    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        // adding a new editText if they are less than 5 ;
        if (functionsList.size() == 5)
            Toast.makeText(getApplicationContext(),"reached maximum functions number",Toast.LENGTH_LONG).show();
        else
        {
            functionsList.add(new MFunction());
            listView.invalidateViews();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    public static LineChart getLineChart() {
        return lineChart;
    }

    public static LineData getLineData() {
        return lineChart.getLineData();
    }

    public static ArrayList<MFunction> getFunctionsList() {
        return functionsList;
    }

    public static void notifyLineChart() {
        lineChart.notifyDataSetChanged();
        lineChart.invalidate();
    }


    public static float getHighestXPoint() {
        return lineChart.getHighestVisibleX();
    }

    public static float getLowestXPoint() {
        return lineChart.getLowestVisibleX();
    }

    public static float getLowestVisibleY() {
        return (float) lineChart.getValuesByTouchPoint(
                lineChart.getViewPortHandler().contentLeft(),
                lineChart.getViewPortHandler().contentBottom(),
                YAxis.AxisDependency.LEFT
        ).y;
    }

    public static float getHighestVisibleY() {
        return (float) lineChart.getValuesByTouchPoint(
                lineChart.getViewPortHandler().contentLeft(),
                lineChart.getViewPortHandler().contentTop(),
                YAxis.AxisDependency.LEFT
        ).y;
    }

    public static float getVisibleXRange() {
        return MyGestureListener.distance(getLowestXPoint(),getHighestXPoint());
    }
}
