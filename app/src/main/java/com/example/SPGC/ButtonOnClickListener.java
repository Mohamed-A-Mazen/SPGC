package com.example.SPGC;

import android.text.Html;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.PopupWindow;

import androidx.constraintlayout.widget.ConstraintLayout;


public class ButtonOnClickListener implements View.OnClickListener {
    private final LayoutInflater inflater;
    private PopupWindow functionsPopupWindow;
    private Button integral = null;
    private int editTextPosition = 0;
    public ButtonOnClickListener(LayoutInflater inflater) {
        this.inflater = inflater;
    }

    @Override
    public void onClick(View v) {
        try {
            StringBuilder stringBuilder;
            Button button = (Button) v;
            EditText editText = MainActivity.listView.getFocusedChild().findViewById(R.id.functionField);
            editTextPosition = MainActivity.listView.getPositionForView(editText);
            int cursorPosition = editText.getSelectionStart() - 1;

            if (button.getId() == MainActivity.deleteButton.getId()) {
                // deleting from text if user taps delete
                if (!editText.getText().toString().isEmpty() && cursorPosition >= 0) {
                        MainActivity.getLineChart().setSelected(false);
                        MainActivity.getLineChart().highlightValue(null);
                        stringBuilder = new StringBuilder(editText.getText().toString());
                        stringBuilder.deleteCharAt(cursorPosition);
                        editText.setText(stringBuilder.toString());
                        editText.setSelection(cursorPosition);
                        MyAdapter.cursorPosition = cursorPosition;
                }
            } else if (button.getId() == MainActivity.leftwardArrow.getId()) {
                // move cursor to the left and modify cursorPosition field
                editText.setSelection(cursorPosition);
                MyAdapter.cursorPosition = cursorPosition;
            } else if (button.getId() == MainActivity.rightwardArrow.getId()) {
                // move cursor to the right and modify cursorPosition field
                if (cursorPosition < editText.getText().toString().length()) {
                    editText.setSelection(cursorPosition + 2);
                    MyAdapter.cursorPosition = cursorPosition + 2;
                }

            } else if (button.getId() == MainActivity.functionsButton.getId()) {
                // creating a popup window when user clicks the funs button;
                View functionsPopupView = inflater.inflate(R.layout.popup_window, null, true);
                functionsPopupWindow = new PopupWindow(functionsPopupView,
                        ConstraintLayout.LayoutParams.WRAP_CONTENT,
                        ConstraintLayout.LayoutParams.WRAP_CONTENT,
                        true);
                functionsPopupWindow.showAtLocation(button, Gravity.BOTTOM, 0, 1);
                Button sin = functionsPopupView.findViewById(R.id.sin);
                Button cos = functionsPopupView.findViewById(R.id.cos);
                Button tan = functionsPopupView.findViewById(R.id.tan);
                Button ctan = functionsPopupView.findViewById(R.id.ctan);
                Button sec = functionsPopupView.findViewById(R.id.sec);
                Button cosec = functionsPopupView.findViewById(R.id.cosec);
                Button aSin = functionsPopupView.findViewById(R.id.aSin);
                Button aCos = functionsPopupView.findViewById(R.id.aCos);
                Button aTan = functionsPopupView.findViewById(R.id.aTan);
                Button aCtan = functionsPopupView.findViewById(R.id.aCtan);
                Button aSec = functionsPopupView.findViewById(R.id.aSec);
                Button aCosec = functionsPopupView.findViewById(R.id.aCosec);
                Button sinHyper = functionsPopupView.findViewById(R.id.sinHyper);
                Button cosHyper = functionsPopupView.findViewById(R.id.cosHyper);
                Button tanHyper = functionsPopupView.findViewById(R.id.tanHyper);
                Button cotHyper = functionsPopupView.findViewById(R.id.cotHyper);
                Button secHyper = functionsPopupView.findViewById(R.id.secHyper);
                Button cscHyper = functionsPopupView.findViewById(R.id.cscHyper);
                Button logarithm = functionsPopupView.findViewById(R.id.logarithm);
                Button logBase2 = functionsPopupView.findViewById(R.id.logBase2);
                logBase2.setText(Html.fromHtml("log<sub>2</sub>"));
                Button naturalLog = functionsPopupView.findViewById(R.id.naturalLog);
                Button derivative = functionsPopupView.findViewById(R.id.derivative);
                integral = functionsPopupView.findViewById(R.id.integral);
                sin.setOnClickListener(dialogOnClickListener);
                cos.setOnClickListener(dialogOnClickListener);
                tan.setOnClickListener(dialogOnClickListener);
                ctan.setOnClickListener(dialogOnClickListener);
                sec.setOnClickListener(dialogOnClickListener);
                cosec.setOnClickListener(dialogOnClickListener);
                aSin.setOnClickListener(dialogOnClickListener);
                aCos.setOnClickListener(dialogOnClickListener);
                aTan.setOnClickListener(dialogOnClickListener);
                aCtan.setOnClickListener(dialogOnClickListener);
                aSec.setOnClickListener(dialogOnClickListener);
                aCosec.setOnClickListener(dialogOnClickListener);
                sinHyper.setOnClickListener(dialogOnClickListener);
                cosHyper.setOnClickListener(dialogOnClickListener);
                tanHyper.setOnClickListener(dialogOnClickListener);
                cotHyper.setOnClickListener(dialogOnClickListener);
                secHyper.setOnClickListener(dialogOnClickListener);
                cscHyper.setOnClickListener(dialogOnClickListener);
                logarithm.setOnClickListener(dialogOnClickListener);
                logBase2.setOnClickListener(dialogOnClickListener);
                naturalLog.setOnClickListener(dialogOnClickListener);
                derivative.setOnClickListener(dialogOnClickListener);
                integral.setOnClickListener(dialogOnClickListener);
                functionsPopupView.setOnTouchListener(new View.OnTouchListener() {
                    @Override
                    public boolean onTouch(View v, MotionEvent event) {
                        functionsPopupWindow.dismiss();
                        return true;
                    }
                });
            } else {
                // adding the text of the clicked button depending on the cursor position
                if (cursorPosition == editText.getText().length() - 1) {
                    editText.append(button.getText());
                } else {
                    editText.setText(new StringBuffer(editText.getText()).insert(cursorPosition + 1, button.getText()));
                    editText.setSelection(cursorPosition + button.getText().length()+1);
                }
                // modify cursor position
                MyAdapter.cursorPosition+=button.getText().length();
            }

        } catch (Exception e) {
            e.getMessage();
        }
    }

    private final View.OnClickListener dialogOnClickListener = new View.OnClickListener() {
        // functions dialog onClickListener
        @Override
        public void onClick(View v) {
            Button button = (Button) v;
            EditText editText = MainActivity.listView.getFocusedChild().findViewById(R.id.functionField);

            // adding the button text at the cursor position

            int cursorPosition = editText.getSelectionStart() - 1;
            if (cursorPosition == editText.getText().length() - 1) {
                editText.append(button.getText());
            } else {
                editText.setText(new StringBuffer(editText.getText()).insert(cursorPosition + 1, button.getText()));
                editText.setSelection(cursorPosition + button.getText().length() + 1);

            }
            // modify cursor position
            MyAdapter.cursorPosition+=button.getText().length();
            if (button.getId() == integral.getId()) {
                // creating the popup window for clicking the integral button
                functionsPopupWindow.dismiss();
                View integralPopupView = inflater.inflate(R.layout.integral_pressing_popup_window, null, true);
                final PopupWindow integralBoundsPopupWindow = new PopupWindow(integralPopupView,
                        ConstraintLayout.LayoutParams.WRAP_CONTENT,
                        ConstraintLayout.LayoutParams.WRAP_CONTENT,
                        true);
                integralBoundsPopupWindow.showAtLocation(button, Gravity.CENTER, 0, 0);
                Button okButton = integralPopupView.findViewById(R.id.okButton);
                EditText upperBound = integralPopupView.findViewById(R.id.upperBound);
                EditText lowerBound = integralPopupView.findViewById(R.id.lowerBound);
                okButton.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        try {
                            // modify the MFunction upper and lower bounds fields
                            if (Float.parseFloat(upperBound.getText().toString()) >
                                    Float.parseFloat(lowerBound.getText().toString())) {

                                MainActivity.getFunctionsList().get(editTextPosition).setUpperBound
                                        (Float.parseFloat(upperBound.getText().toString()));

                                MainActivity.getFunctionsList().get(editTextPosition).setLowerBound(
                                        Float.parseFloat(lowerBound.getText().toString()));

                            }
                        } catch (Exception e) {
                        }
                        integralBoundsPopupWindow.dismiss();
                    }
                });
                integralPopupView.setOnTouchListener(new View.OnTouchListener() {
                    @Override
                    public boolean onTouch(View v, MotionEvent event) {
                        integralBoundsPopupWindow.dismiss();
                        return true;
                    }
                });
            }
        }
    };

}

