package com.example.SPGC;

        import android.graphics.Color;


        import com.github.mikephil.charting.data.Entry;
        import com.github.mikephil.charting.data.LineDataSet;

        import org.mariuszgromada.math.mxparser.Expression;
        import org.mariuszgromada.math.mxparser.Function;

        import java.util.LinkedList;
        import java.util.List;


public class MFunction {
    private List<Entry> entries;
    private float integralUpperBound = 1;
    private float integralLowerBound = -1;
    private float numericValue = 10000;
    private boolean hasNumericValue = false;
    // two expressions one for displaying and the other is for parsing;
    private String expression;
    private String symbolicExpression;


    public MFunction() {
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
        this.integralLowerBound = upperBound;
    }

    public void setLowerBound(float lowerBound) {
        this.integralLowerBound = lowerBound;
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
                if (direction == 0){
                    // adding data to the middle of the lineChart
                    entries = new LinkedList<>();
                    for (float x = from - 2; x <= to + 2 ; x+=xGrowthFactor) {
                        expression = new Expression("f(" + x + ")", function);
                        currentOutput =(float)(expression.calculate());
                        if (!String.valueOf(currentOutput).equals("NaN"))
                            entries.add(new Entry(x, currentOutput));
                    }
                }else if (direction == 1){
                    // adding data to the right
                    from = entries.get(entries.size()-1).getX();
                    to = from + scale;
                    for (float x = from ; x <= to ; x+=xGrowthFactor) {
                        expression = new Expression("f(" + x + ")", function);
                        currentOutput =(float)(expression.calculate());
                        if (!String.valueOf(currentOutput).equals("NaN")) {
                            entries.add(new Entry(x, currentOutput));
                            if (!(entries.get(0).getX() > lowestPoint - scale/2))
                                entries.remove(0);
                        }
                    }
                }else if (direction == -1){
                    // adding data to the left
                    to = entries.get(0).getX() - scale;
                    from = entries.get(0).getX() ;
                    for (float x = from ; x >= to ; x-=xGrowthFactor) {
                        expression = new Expression("f(" + x + ")", function);
                        currentOutput =(float)(expression.calculate());
                        if (!String.valueOf(currentOutput).equals("NaN")) {
                            entries.add(0,new Entry(x, currentOutput));
                            if (entries.get(entries.size() - 1).getX() > highestPoint + scale/2)
                                entries.remove(entries.size() - 1);
                        }
                    }
                }
            }catch (Exception exception){
                //
            }

        // customising the data set
        LineDataSet dataSet = new LineDataSet(entries, this.symbolicExpression);
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
                        stringBuilder.insert(j, ",x," + integralLowerBound + "," + integralLowerBound);
                        completeEquation = stringBuilder.toString();
                        integralBuilder.insert(integralBuilder.length() - 1, ",x," + integralLowerBound + "," + integralUpperBound);
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
