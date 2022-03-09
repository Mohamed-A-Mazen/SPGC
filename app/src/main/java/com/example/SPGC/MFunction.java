package com.example.SPGC;

import android.graphics.Color;


import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineDataSet;

import org.mariuszgromada.math.mxparser.Expression;
import org.mariuszgromada.math.mxparser.Function;

import java.util.LinkedList;
import java.util.List;


public class MFunction {
    private List<Entry> uiList;
    private float upperBound = 1;
    private float lowerBound = -1;
    private float numericValue = 10000;
    private boolean hasNumericValue = false;
    // two expressions one for displaying and the other is for parsing;
    private String expression;
    private String symbolicExpression;


    public MFunction() {
        uiList = new LinkedList<>();
        this.expression = "";
        this.symbolicExpression = "";
    }


    public float getNumericValue() {
        return numericValue;
    }

    public boolean isHasNumericValue() {
        return hasNumericValue;
    }

    public String getSymbolicExpression() {
        return this.symbolicExpression;
    }

    public void setSymbolicExpression(String expression) {
        // setting the symbolic expression and modifying the expression by the symbolToStringMapper method to be ready for parsing;
        this.symbolicExpression = expression;
        this.expression = symbolToStringMapper(expression);
        Expression numericExpression = new Expression(this.expression);
        if (!expression.contains("x") && numericExpression.checkSyntax()) {
            hasNumericValue = true;
            numericValue = Float.parseFloat(String.valueOf(numericExpression.calculate()));
        }
        else if (expression.contains("x") && hasNumericValue){
            hasNumericValue = false;
        }
        // if it contains a derivtive or / and integral we modify it by calculusEquationsFormatter method
        if (this.expression.contains("der") || this.expression.contains("int")) {
            this.expression = calculusEquationsFormatter(this.expression);
        }
    }

    public void setUpperBound(float upperBound) {
        this.upperBound = upperBound;
    }

    public void setLowerBound(float lowerBound) {
        this.lowerBound = lowerBound;
    }

        public LineDataSet computeValues(float from, float to, float scale , int direction) {
        // computing the values of the functions
        if (this.getSymbolicExpression().equals("")) {
            this.hasNumericValue = false;
            return null;
        }
        if (!expression.contains("x")) {
            hasNumericValue = true;
            Expression expression = new Expression(this.expression);
            numericValue = Float.parseFloat(String.valueOf(expression.calculate()));
            return null;
        }

        this.hasNumericValue = false;
        float currentOutput;
        float highestPoint = MainActivity.getHighestXPoint();
        float lowestPoint = MainActivity.getLowestXPoint();
        float xGrowthFactor = scale / 200;
        Function function = new Function("f(x) = " + this.expression);
        Expression expression;
        try {
            /**
            the reason we're having two lists "uiList and modifying list" in each time we are modifying data,
            is because two event handlers might get called at the same time and this might cause an inconsistency in the data
            if they are modifying the same list because the linkedlist is not thread safe , so to prevent that we are having two
            **/

        if (direction == 0){
            // adding data to the middle of the lineChart
            List<Entry> middleList = new LinkedList<>();
                for (float x = from - 2; x <= to + 2 ; x+=xGrowthFactor) {
                    expression = new Expression("f(" + x + ")", function);
                    currentOutput =(float)(expression.calculate());
                    if (!String.valueOf(currentOutput).equals("NaN"))
                        middleList.add(new Entry(x, currentOutput));
                }
                uiList = new LinkedList<>(middleList);
        }else if (direction == 1){
            // adding data to the right
            List<Entry> rightwardList = new LinkedList<>(uiList);
            from = rightwardList.get(uiList.size()-1).getX();
            to = from + scale;
            for (float x = from ; x <= to ; x+=xGrowthFactor) {
                    expression = new Expression("f(" + x + ")", function);
                    currentOutput =(float)(expression.calculate());
                    if (!String.valueOf(currentOutput).equals("NaN")) {
                        rightwardList.add(new Entry(x, currentOutput));
                        if (!(rightwardList.get(0).getX() > lowestPoint - scale/2))
                            rightwardList.remove(0);
                    }
                }
            uiList = new LinkedList<>(rightwardList);
        }else if (direction == -1){
            // adding data to the left
            List<Entry> leftwardList = new LinkedList<>(uiList);
            to = leftwardList.get(0).getX() - scale;
            from = leftwardList.get(0).getX() ;
                for (float x = from ; x >= to ; x-=xGrowthFactor) {
                    expression = new Expression("f(" + x + ")", function);
                    currentOutput =(float)(expression.calculate());
                    if (!String.valueOf(currentOutput).equals("NaN")) {
                        leftwardList.add(0,new Entry(x, currentOutput));
                        if (!(leftwardList.get(leftwardList.size() - 1).getX() < highestPoint + scale/2))
                            leftwardList.remove(leftwardList.size() - 1);
                    }
                }
                uiList = new LinkedList<>(leftwardList);
        }
        }catch (Exception exception){
            exception.getMessage();
        }
        // customising the data set
        LineDataSet dataSet = new LineDataSet(uiList, this.symbolicExpression);
        dataSet.setDrawCircles(false);
        dataSet.setDrawValues(false);
        dataSet.setLineWidth(3);
        dataSet.setColor(chooseColor());
        return dataSet;
    }



    private int chooseColor() {
        int functionNum = MainActivity.getFunctionsList().indexOf(this) + 1;
        switch (functionNum) {
            case 1:
                return Color.BLUE;
            case 2:
                return Color.RED;
            case 3:
                return Color.GREEN;
            case 4:
                return Color.BLACK;
            case 5:
                return Color.YELLOW;
            default:
                return Color.GRAY;
        }
    }


    private String symbolToStringMapper(String expression) {
        // mapping each symbol to it's corresponding string value
        if (expression.contains("\u221A")) expression = expression.replaceAll("\\u221A", "sqrt");
        if (expression.contains("\u03C0")) expression = expression.replaceAll("\\u03C0", "pi");
        if (expression.contains("÷")) expression = expression.replaceAll("÷", "/");
        if (expression.contains("d/dx")) expression = expression.replaceAll("d/dx", "der");
        if (expression.contains("\u222B")) expression = expression.replaceAll("\\u222B", "int");
        return expression;
    }

    private String calculusEquationsFormatter(String completeEquation) {
        // formatting the calc equations
        for (int i = 0; i < completeEquation.length() - 2; i++) {
            int rightBrackets = 0;
            int leftBrackets = 0;
            String currentString = completeEquation.substring(i, i + 3);
            StringBuilder integralBuilder = new StringBuilder();
            if ((!currentString.equals("int")) && (!currentString.equals("der")))
                continue;
            for (int j = i; j < completeEquation.length(); j++) {
                char myChar = completeEquation.charAt(j);
                integralBuilder.append(myChar);
                if (myChar == '(') rightBrackets++;
                else if (myChar == ')') leftBrackets++;
                if (leftBrackets == rightBrackets && leftBrackets != 0) {
                    StringBuilder stringBuilder = new StringBuilder(completeEquation);
                    if (currentString.equals("int")) {
                        stringBuilder.insert(j, ",x," + lowerBound + "," + upperBound);
                        completeEquation = stringBuilder.toString();
                        integralBuilder.insert(integralBuilder.length() - 1, ",x," + lowerBound + "," + upperBound);
                        Expression expression = new Expression(integralBuilder.toString());
                        // we replace the string of the integral with it's numerical value because we don't want to calculate
                        // it every time we are adding new data because it is constant
                        completeEquation = completeEquation.replace(integralBuilder.toString(),
                                String.valueOf(expression.calculate()));
                        break;
                    }
                    stringBuilder.insert(j, ",x");
                    completeEquation = stringBuilder.toString();
                    break;
                }
            }
        }
        Expression expression = new Expression(completeEquation);
        if (!String.valueOf(expression.calculate()).equals("NaN")) {
            // if the integral part was the whole expression we display it and set it as the numerical value of the MFunction
            this.numericValue = (float) expression.calculate();
            this.hasNumericValue = true;
        }
        return completeEquation;
    }
}
