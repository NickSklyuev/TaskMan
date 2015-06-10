package prof.magnitos.speedytask.ui.fragments;

import android.app.Activity;
import android.app.ActivityManager;
import android.content.Context;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.github.mikephil.charting.animation.Easing;
import com.github.mikephil.charting.charts.BarChart;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.data.BarDataSet;
import com.github.mikephil.charting.data.BarEntry;
import com.squareup.otto.Subscribe;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.RandomAccessFile;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import prof.magnitos.speedytask.R;
import prof.magnitos.speedytask.components.AsyncResponse;
import prof.magnitos.speedytask.components.BusProvider;
import prof.magnitos.speedytask.components.CommonLibrary;
import prof.magnitos.speedytask.components.MainAsync;


/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link MainFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link MainFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class MainFragment extends Fragment {

    View v;

    public static int[] cpui;

    TextView cpuUsageText, totalMemoryText, usageMemoryText, processStartedText;

    Handler handler = new Handler(Looper.getMainLooper());

    BarChart bchart;

    private OnFragmentInteractionListener mListener;

    public static BarData datas = null;

    SwipeRefreshLayout mSwipeRefreshLayout;

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @return A new instance of fragment MainFragment.
     */
    public static MainFragment newInstance() {
        MainFragment fragment = new MainFragment();
        Bundle args = new Bundle();
        //args.putString(ARG_PARAM1, param1);
        //args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    public MainFragment() {
        // Required empty public constructor
    }

    @Subscribe
    public void onAvatarChanged(ArrayList<Integer> inter) {
        render();
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setRetainInstance(true);
        BusProvider.getInstance().register(this);
        if (getArguments() != null) {
            //mParam1 = getArguments().getString(ARG_PARAM1);
            //mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        v = inflater.inflate(R.layout.activity_chart, container, false);

        mSwipeRefreshLayout = (SwipeRefreshLayout) v.findViewById(R.id.refresh);

        mSwipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                new MainAsync(new AsyncResponse() {
                    @Override
                    public void processFinish(Object output) {
                        render();

                    }
                }).execute(new Object[]{});
            }
        });


        bchart = (BarChart) v.findViewById(R.id.chart_bar);


        cpuUsageText = (TextView) v.findViewById(R.id.cpuUsage);
        totalMemoryText = (TextView) v.findViewById(R.id.totalMemory);
        usageMemoryText = (TextView) v.findViewById(R.id.usageMamory);
        processStartedText = (TextView) v.findViewById(R.id.processStarted);

        //render();

        return v;
    }


    public void render(){
        try {
            if (datas == null) {
                showProcessorChart();
            }

            bchart.setDescription("");
            bchart.setDrawGridBackground(false);

            XAxis xAxis = bchart.getXAxis();
            xAxis.setPosition(XAxis.XAxisPosition.BOTTOM);
            xAxis.setDrawGridLines(true);

            YAxis leftAxis = bchart.getAxisLeft();
            leftAxis.setLabelCount(5);
            leftAxis.setSpaceTop(15f);

            YAxis rightAxis = bchart.getAxisRight();
            rightAxis.setLabelCount(5);
            rightAxis.setSpaceTop(15f);

            bchart.setData(datas);
            bchart.animateY(0, Easing.EasingOption.EaseInCubic);

            if (cpui != null) {

                int c = 0;

                for (int t = 0; t < cpui.length; t++) {
                    c += cpui[t];
                }

                Runtime runtime = Runtime.getRuntime();

                ActivityManager.MemoryInfo mi = new ActivityManager.MemoryInfo();
                ActivityManager activityManager = (ActivityManager) getActivity().getSystemService(Context.ACTIVITY_SERVICE);
                activityManager.getMemoryInfo(mi);
                long availableMegs = mi.availMem / 1048576L;
                try {
                    long totalMegs = Long.parseLong(getTotalRAM());


                cpuUsageText.setText(c + "%");
                totalMemoryText.setText(totalMegs + " Mb");
                usageMemoryText.setText((totalMegs - availableMegs) + " Mb");
                }catch (Exception e){
                    e.printStackTrace();
                }

                processStartedText.setText(CommonLibrary.GetRunningProcess(getActivity(), activityManager).size() + " ");
            }
            mSwipeRefreshLayout.setRefreshing(false);

            Map<String, String> dimensions = new HashMap<String, String>();
            dimensions.put("type", "update");
            dimensions.put("activity", "system_info");
            //ParseAnalytics.trackEventInBackground("read", dimensions);

        }catch (Exception e){
            e.printStackTrace();
        }
    }


    public String getTotalRAM() {

        RandomAccessFile reader = null;
        String load = null;
        DecimalFormat twoDecimalForm = new DecimalFormat("#.##");
        double totRam = 0;
        String lastValue = "";
        try {
            reader = new RandomAccessFile("/proc/meminfo", "r");
            load = reader.readLine();

            // Get the Number value from the string
            Pattern p = Pattern.compile("(\\d+)");
            Matcher m = p.matcher(load);
            String value = "";
            while (m.find()) {
                value = m.group(1);
                // System.out.println("Ram : " + value);
            }
            reader.close();

            totRam = Double.parseDouble(value);
            // totRam = totRam / 1024;

            double mb = totRam / 1024.0;
            double gb = totRam / 1048576.0;
            double tb = totRam / 1073741824.0;

            if (tb > 1) {
                lastValue = twoDecimalForm.format(tb).concat(" TB");
            } else if (gb > 1) {
                lastValue = twoDecimalForm.format(gb).concat(" GB");
            } else if (mb > 1) {
                lastValue = twoDecimalForm.format(mb).concat(" MB");
            } else {
                lastValue = twoDecimalForm.format(totRam).concat(" KB");
            }



        } catch (IOException ex) {
            ex.printStackTrace();
        } finally {
            // Streams.close(reader);
        }

        return lastValue;
    }

    public long getTotal(ActivityManager.MemoryInfo mi){
        return mi.totalMem;
    }

    public static void showProcessorChart(){
        ArrayList<BarEntry> entries = new ArrayList<BarEntry>();

        cpui = getCpuUsageStatistic();

        int c=0;

        for(int t= 0; t<cpui.length; t++){
            c+=cpui[t];
        }

        //cpuUsageText.setText(c+"%");




        //totalMemoryText.setText(totalMegs+" Mb");
        //usageMemoryText.setText((totalMegs-availableMegs)+" Mb");

        //processStartedText.setText(CommonLibrary.GetRunningProcess(getActivity(), activityManager).size()+" ");

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


        datas = cd;
        datas.setValueTextColor(Color.BLACK);
    }



    public static int[] getCpuUsageStatistic() {

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

    public static String executeTop() {
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

    // TODO: Rename method, update argument and hook method into UI event
    public void onButtonPressed(Uri uri) {
        if (mListener != null) {
            mListener.onFragmentInteraction(uri);
        }
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    /**
     * This interface must be implemented by activities that contain this
     * fragment to allow an interaction in this fragment to be communicated
     * to the activity and potentially other fragments contained in that
     * activity.
     * <p/>
     * See the Android Training lesson <a href=
     * "http://developer.android.com/training/basics/fragments/communicating.html"
     * >Communicating with Other Fragments</a> for more information.
     */
    public interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        public void onFragmentInteraction(Uri uri);
    }

}
