package com.example.taskerapp;

import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.util.Log;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

import lecho.lib.hellocharts.model.PieChartData;
import lecho.lib.hellocharts.model.SliceValue;
import lecho.lib.hellocharts.util.ChartUtils;
import lecho.lib.hellocharts.view.PieChartView;


public class ChartActivity extends ActionBarActivity {

    private PieChartView chart;
    private PieChartData data;

    private boolean hasLabels = false;
    private boolean hasLabelsOutside = false;
    private boolean hasCenterCircle = false;
    private boolean hasCenterText1 = false;
    private boolean hasCenterText2 = false;
    private boolean isExploded = false;
    private boolean hasLabelForSelected = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chart);

        chart = (PieChartView) findViewById(R.id.chart);
        generateData();
        //chart.setOnValueTouchListener(new ValueTouchListener());
    }

    private void generateData() {
        int numValues = 6;

        int[] i = getCpuUsageStatistic();

        List<SliceValue> values = new ArrayList<SliceValue>();
        for(int t = 0; t< i.length; t++) {
            SliceValue sliceValue = new SliceValue((float)i[t],ChartUtils.pickColor());
            if(t==0){
                sliceValue.setLabel("User "+i[t]+"%");
            }else if(t==1){
                sliceValue.setLabel("System "+i[t]+"%");
            }else if(t==2){
                sliceValue.setLabel("IOW "+i[t]+"%");
            }else if(t==3){
                sliceValue.setLabel("IRQ "+i[t]+"%");
            }
            values.add(sliceValue);
        }

        data = new PieChartData(values);
        data.setHasLabels(true);
        data.setHasLabelsOnlyForSelected(false);
        data.setHasLabelsOutside(false);
        data.setHasCenterCircle(true);


            data.setSlicesSpacing(24);



            data.setCenterText1("CPU");

            // Get roboto-italic font.
            //Typeface tf = Typeface.createFromAsset(getAssets(), "Roboto-Italic.ttf");
            //data.setCenterText1Typeface(tf);

            // Get font size from dimens.xml and convert it to sp(library uses sp values).
            data.setCenterText1FontSize(ChartUtils.px2sp(getResources().getDisplayMetrics().scaledDensity,
                    (int) getResources().getDimension(R.dimen.pie_chart_text1_size)));



            data.setCenterText2("Charts usage CPU");

            //Typeface tf = Typeface.createFromAsset(getAssets(), "Roboto-Italic.ttf");

            //data.setCenterText2Typeface(tf);
            data.setCenterText2FontSize(ChartUtils.px2sp(getResources().getDisplayMetrics().scaledDensity,
                    (int) getResources().getDimension(R.dimen.pie_chart_text2_size)));


        chart.setPieChartData(data);
    }

    private int[] getCpuUsageStatistic() {

        String tempString = executeTop();

        System.out.println(tempString);

        tempString = tempString.replaceAll(" ", "");
        tempString = tempString.replaceAll("User", "");
        tempString = tempString.replaceAll("System", "");
        tempString = tempString.replaceAll("IOW", "");
        tempString = tempString.replaceAll("IRQ", "");
        tempString = tempString.replaceAll("%", "");

        String[] myString = tempString.split(",");
        int[] cpuUsageAsInt = new int[myString.length];

        /*for (int i = 0; i < 10; i++) {
            tempString = tempString.replaceAll("  ", " ");
        }
        tempString = tempString.trim();
        String[] myString = tempString.split(" ");
        int[] cpuUsageAsInt = new int[myString.length];
        */
        for (int i = 0; i < myString.length; i++) {
            myString[i] = myString[i].trim();
            cpuUsageAsInt[i] = Integer.parseInt(myString[i]);
        }
        return cpuUsageAsInt;
    }

    private String executeTop() {
        java.lang.Process p = null;
        BufferedReader in = null;
        String returnString = null;
        try {
            p = Runtime.getRuntime().exec("top -n 1");
            in = new BufferedReader(new InputStreamReader(p.getInputStream()));
            while (returnString == null || returnString.contentEquals("")) {
                returnString = in.readLine();
            }
        } catch (IOException e) {
            Log.e("executeTop", "error in getting first line of top");
            e.printStackTrace();
        } finally {
            try {
                in.close();
                p.destroy();
            } catch (IOException e) {
                Log.e("executeTop",
                        "error in closing and destroying top process");
                e.printStackTrace();
            }
        }
        return returnString;
    }


}
