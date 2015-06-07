package prof.magnitos.speedytask.ui;

import android.app.ActivityManager;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.support.v7.app.ActionBarActivity;
import android.util.Log;
import android.widget.TextView;

import com.github.mikephil.charting.animation.Easing;
import com.github.mikephil.charting.charts.BarChart;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.data.BarDataSet;
import com.github.mikephil.charting.data.BarEntry;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Timer;
import java.util.TimerTask;

import prof.magnitos.speedytask.R;
import prof.magnitos.speedytask.components.CommonLibrary;


public class ChartActivity extends ActionBarActivity {


    int[] cpui;

    TextView cpuUsageText, totalMemoryText, usageMemoryText, processStartedText;

    private boolean hasLabels = false;
    private boolean hasLabelsOutside = false;
    private boolean hasCenterCircle = false;
    private boolean hasCenterText1 = false;
    private boolean hasCenterText2 = false;
    private boolean isExploded = false;
    private boolean hasLabelForSelected = false;
    Handler handler = new Handler(Looper.getMainLooper());

    BarChart bchart;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chart);

        /*chart = (PieChartView) findViewById(R.id.chart);
        generateData();
        */
        bchart = (BarChart) findViewById(R.id.chart_bar);


        cpuUsageText = (TextView) findViewById(R.id.cpuUsage);
        totalMemoryText = (TextView) findViewById(R.id.totalMemory);
        usageMemoryText = (TextView) findViewById(R.id.usageMamory);
        processStartedText = (TextView) findViewById(R.id.processStarted);


        Timer timer = new Timer();
        timer.scheduleAtFixedRate(new TimerTask() {

            @Override
            public void run() {

                handler.post(new Runnable() {
                    @Override
                    public void run() {
                        showProcessorChart();
                    }
                });
            }

        }, 0, 5000);

        //chart.setOnValueTouchListener(new ValueTouchListener());
    }


    public void showProcessorChart(){
        ArrayList<BarEntry> entries = new ArrayList<BarEntry>();

        cpui = getCpuUsageStatistic();

        int c=0;

        for(int t= 0; t<cpui.length; t++){
            c+=cpui[t];
        }

        cpuUsageText.setText(c+"%");


        ActivityManager.MemoryInfo mi = new ActivityManager.MemoryInfo();
        ActivityManager activityManager = (ActivityManager) getSystemService(ACTIVITY_SERVICE);
        activityManager.getMemoryInfo(mi);
        long availableMegs = mi.availMem / 1048576L;
        long totalMegs = mi.totalMem / 1048576L;

        totalMemoryText.setText(totalMegs+" Mb");
        usageMemoryText.setText((totalMegs-availableMegs)+" Mb");

        processStartedText.setText(CommonLibrary.GetRunningProcess(this, activityManager).size()+" ");

        entries.add(new BarEntry((int) c, 0));


        BarDataSet d = new BarDataSet(entries, c+" %");
        d.setBarSpacePercent(0f);
        d.setColors(new int[]{Color.rgb(1, 129, 49)});
        d.setBarShadowColor(Color.rgb(1, 129, 49));

        if(c>40){
            d.setBarSpacePercent(5f);
            d.setColors(new int[]{Color.rgb(212, 168, 16)});
            d.setBarShadowColor(Color.rgb(212, 168, 16));
        }

        if(c>70){
            d.setBarSpacePercent(5f);
            d.setColors(new int[]{Color.rgb(212, 104, 16)});
            d.setBarShadowColor(Color.rgb(212, 104, 16));
        }

        if(c>90){
            d.setBarSpacePercent(5f);
            d.setColors(new int[]{Color.rgb(189, 40, 16)});
            d.setBarShadowColor(Color.rgb(189, 40, 16));
        }

        ArrayList<BarDataSet> sets = new ArrayList<BarDataSet>();
        sets.add(d);

        ArrayList<String> m = new ArrayList<String>();
        m.add("CPU %");
        BarData cd = new BarData(m, sets);


        BarData data = cd;

        //data.setValueTypeface(mTf);
        data.setValueTextColor(Color.BLACK);
        bchart.setDescription("");
        bchart.setDrawGridBackground(false);

        XAxis xAxis = bchart.getXAxis();
        xAxis.setPosition(XAxis.XAxisPosition.BOTTOM);
        //xAxis.setTypeface(mTf);
        xAxis.setDrawGridLines(true);

        YAxis leftAxis = bchart.getAxisLeft();
        //leftAxis.setTypeface(mTf);
        leftAxis.setLabelCount(5);
        leftAxis.setSpaceTop(15f);

        YAxis rightAxis = bchart.getAxisRight();
        //rightAxis.setTypeface(mTf);
        rightAxis.setLabelCount(5);
        rightAxis.setSpaceTop(15f);



        // set data
        bchart.setData(data);

        // do not forget to refresh the chart
//            holder.chart.invalidate();
        bchart.animateY(0, Easing.EasingOption.EaseInCubic);
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
