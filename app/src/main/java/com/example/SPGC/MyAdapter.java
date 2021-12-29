package com.example.SPGC;

import android.content.Context;
import android.graphics.Typeface;
import android.os.AsyncTask;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.github.mikephil.charting.data.LineData;

import java.util.ArrayList;


public class MyAdapter extends ArrayAdapter<MFunction> {
    ArrayList<MFunction> functionsList = MainActivity.getFunctionsList();
    private final LayoutInflater inflater;
    private final int layoutRes;
    private int selectedItem = 0;
    private String text = "";
    public static int cursorPosition = 0;

    public MyAdapter(@NonNull Context context, int resource) {
        super(context, resource);
        layoutRes = resource;
        inflater = LayoutInflater.from(context);
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        convertView = inflater.inflate(layoutRes, parent, false);
        MFunction MFunction = functionsList.get(position);
        RelativeLayout relativeLayout = (RelativeLayout) convertView;
        TextView textView = relativeLayout.findViewById(R.id.textView);
        // we set the text of the text view = the numerical value of the MFunction;
        if (MFunction.isHasNumericValue()) {
            textView.setText(String.valueOf(MFunction.getNumericValue()));
        } else {
            textView.setText("");
        }
        Button removeListItemButton = relativeLayout.findViewById(R.id.removeIListItemButton);
        removeListItemButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int index = MainActivity.listView.getPositionForView((RelativeLayout) v.getParent());
                if (functionsList.size() > 1) {
                    // if the listView contains 2 childes or more we remove the one that is clicked by the user ;
                    functionsList.remove(index);
                    TextView textView = MainActivity.listView.getChildAt(0).findViewById(R.id.textView);
                    textView.setText(textView.getText());
                    MainActivity.listView.invalidateViews();

                }
            }
        });
        EditText functionField = relativeLayout.findViewById(R.id.functionField);
        functionField.setTypeface(Typeface.defaultFromStyle(Typeface.BOLD_ITALIC));
        //preventing the user from the user from using the device keyboard;
        functionField.setShowSoftInputOnFocus(false);
        if (!MFunction.getSymbolicExpression().equals("")) {
            functionField.setText(MFunction.getSymbolicExpression());
        }
        functionField.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // modifying the cursor position when the user modifies it by clicking ;
                cursorPosition = ((EditText) v).getSelectionEnd();
            }
        });


        functionField.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            @NonNull
            public void onFocusChange(View v, boolean hasFocus) {
                ListView listView = (ListView) parent;
                EditText view = (EditText) v;
                try {
                    if (!v.hasFocus()) {
                        // when the user changes focus we set the text of the unfocused child = text and set text = "";
                        view.setText(text);
                        text = "";
                    }
                    if (v.hasFocus()) {
                        // we set the text of the focus child equal to the expression of the corresponding MFunction and reset the focused child position
                        int position = listView.getPositionForView(v);
                        selectedItem = position;
                        text = functionsList.get(position).getSymbolicExpression();
                        view.setSelection(cursorPosition);
                    }
                } catch (Exception e) {
                    e.getMessage();
                }
            }
        });

        functionField.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                // when text changes we set the expression of the MFunction that corresponds to the focused child using selectedItem
                text = s.toString();
                MFunction mFunction = functionsList.get(selectedItem);
                mFunction.setSymbolicExpression(text);
                if (text.contains("x") && !mFunction.isHasNumericValue()) {
                    // if it is a function we draw it
                    AddDataSet addDataSet = new AddDataSet();
                    addDataSet.execute();
                    ((TextView)MainActivity.listView.getFocusedChild().findViewById(R.id.textView)).setText("");

                } else {
                    EditText editText = ((EditText) MainActivity.listView.getFocusedChild().findViewById(R.id.functionField));
                    TextView textView = ((TextView) MainActivity.listView.getFocusedChild().findViewById(R.id.textView));
                    if (mFunction.isHasNumericValue() && text.length() > 1) {
                        textView.setTextKeepState(String.valueOf(mFunction.getNumericValue()));
                    } else if (text.length() == 0) {
                        AddDataSet addDataSet = new AddDataSet();
                        addDataSet.execute();
                        textView.setTextKeepState("");
                    }
                }
            }
        });
        return relativeLayout;

    }

    private class AddDataSet extends AsyncTask<Void, Void, LineData> {
        // we use this to draw the function that has been modified
        @Override
        protected LineData doInBackground(Void... voids) {
            LineData lineData = new LineData();
            for (int i = 0; i < functionsList.size(); i++) {
                if (i == selectedItem) {
                    lineData.addDataSet(
                            functionsList.get(selectedItem).computeValues(
                                    MainActivity.getLowestXPoint(),
                                    MainActivity.getHighestXPoint(),
                                    MainActivity.getVisibleXRange(),
                                    0));
                } else {
                    lineData.addDataSet(MainActivity.getLineChart().getData().getDataSetByIndex(i));
                }
            }
            return lineData;
        }

        @Override
        protected void onPostExecute(LineData lineData) {
            MainActivity.getLineChart().setData(lineData);
            MainActivity.notifyLineChart();

        }
    }

    @Override
    public int getCount() {
        return functionsList.size();
    }
}