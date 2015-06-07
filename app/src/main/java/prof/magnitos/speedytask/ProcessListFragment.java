package prof.magnitos.speedytask;

import android.app.Activity;
import android.app.ActivityManager;
import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.gc.materialdesign.views.ButtonRectangle;

import java.util.ArrayList;


/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link ProcessListFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link ProcessListFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class ProcessListFragment extends Fragment {

    View v;

    private RecyclerView ChatMessagesViewRecycle;
    private ProcessListAdapter mAdapter;
    private RecyclerView.LayoutManager mLayoutManager;

    SwipeRefreshLayout mSwipeRefreshLayout;


    public static ArrayList<ProcessDetailInfo> mDetailList;
    ActivityManager mActivityManager = null;
    //private TaskListAdapters.ProcessListAdapter mpAdapter;
    private Handler mHandler;

    private OnFragmentInteractionListener mListener;

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @return A new instance of fragment ProcessListFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static ProcessListFragment newInstance() {
        ProcessListFragment fragment = new ProcessListFragment();
        Bundle args = new Bundle();
        fragment.setArguments(args);
        return fragment;
    }

    public ProcessListFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setRetainInstance(true);
        //ProcessDetailInfo.IGNORE_PREFS_NAME = getActivity().getPackageName();
        if (getArguments() != null) {
            //mParam1 = getArguments().getString(ARG_PARAM1);
            //mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    @Override
    public void onResume(){
        super.onResume();
        refresh();//main method
        mAdapter.notifyDataSetChanged();
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        v = inflater.inflate(R.layout.fragment_process_list, container, false);

        mSwipeRefreshLayout = (SwipeRefreshLayout) v.findViewById(R.id.refresh);

        mSwipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                refresh();
            }
        });

        ChatMessagesViewRecycle = (RecyclerView) v.findViewById(R.id.process_list);

        ChatMessagesViewRecycle.setHasFixedSize(true);

        // use a linear layout manager
        mLayoutManager = new LinearLayoutManager(getActivity());
        ChatMessagesViewRecycle.setLayoutManager(mLayoutManager);

        mActivityManager = ((ActivityManager) getActivity().getSystemService(Context.ACTIVITY_SERVICE));


        ButtonRectangle button = (ButtonRectangle) v.findViewById(R.id.button);

        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                killAllTasks();
            }
        });

        // specify an adapter (see also next example)


        getRunningProcess();

        return v;
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


    public void refresh(){
        getRunningProcess();//getRunningProcess
        //getListView().setAdapter(this.mAdapter);//ListView add content
        //bindEvent();//click on button
        //mAdapter = new ProcessListAdapter(mDetailList);
        //ChatMessagesViewRecycle.setAdapter(mAdapter);
        refreshMem();
        // AutoStartReceiver.RefreshNotification(this);
    }

    private void refreshMem(){
        if (mHandler == null)
            mHandler = new Handler(){
                public void handleMessage(Message paramMessage){
                    try{
                        String str = "Available Memory: " + CommonLibrary.MemoryToString(
                                CommonLibrary.getAvaliableMemory(mActivityManager));
                        showToast(str);
                        // this.setTitle(str + "  " + Setting.getAutoKillInfo());
                        return;
                    }
                    catch (Exception localException){
                        localException.printStackTrace();
                    }
                }
            };
        mHandler.sendEmptyMessageDelayed(0, 700L);
    }

    public void showToast(String mtext) {
        //������� � ���������� ��������� �����������
        Toast toast = Toast.makeText(getActivity(),
                mtext,
                Toast.LENGTH_SHORT);
        toast.setGravity(Gravity.CENTER, 0, 0);
        toast.show();
    }
    private void killAllTasks(){
        CommonLibrary.KillProcess(getActivity(), mDetailList, mActivityManager, true);
        getRunningProcess();
        //mAdapter = new ProcessListAdapter(mDetailList);
        //ChatMessagesViewRecycle.setAdapter(mAdapter);
        refreshMem();
        android.util.Log.e("ATK", "Manually kill ends");
    }

    public void getRunningProcess(){
        mDetailList = CommonLibrary.GetRunningProcess(getActivity(), mActivityManager);
        Log.i("Task_man", " data " + mDetailList);

        //mAdapter = new TaskListAdapters.ProcessListAdapter(getActivity(), mDetailList);
        mAdapter = new ProcessListAdapter(mDetailList);
        mAdapter.SetOnItemClickListener(new ProcessListAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(View view, int position, ProcessDetailInfo localProcessDetailInfo) {
                if (localProcessDetailInfo.getSelected()) {
                    localProcessDetailInfo.setSelected(false);
                } else {
                    localProcessDetailInfo.setSelected(true);
                }
                //killAllTasks();
            }
        });
        ChatMessagesViewRecycle.setAdapter(mAdapter);
        mAdapter.notifyDataSetChanged();
        Log.i("Task_man", " adapter " + mAdapter);
        new MainAsync(new AsyncResponse() {
            @Override
            public void processFinish(Object output) {

                ArrayList<Integer> inter = new ArrayList<Integer>();

                inter.add(0);

                BusProvider.getInstance().post(inter);

            }
        }).execute(new Object[]{});
        mSwipeRefreshLayout.setRefreshing(false);
    }


}
