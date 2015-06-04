package com.example.taskerapp;

import android.app.ActivityManager;
import android.content.Context;
import android.content.Intent;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.ActionBarActivity;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Iterator;


public class MainActivity extends ActionBarActivity
        {

    private ArrayList<ProcessDetailInfo> mDetailList;
    ActivityManager mActivityManager = null;
    private TaskListAdapters.ProcessListAdapter mAdapter;
    private Handler mHandler;


    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        mActivityManager = ((ActivityManager) getSystemService(Context.ACTIVITY_SERVICE));
    }
   @Override
   protected void onResume(){
       super.onResume();
       refresh();//main method
   }

    public void refresh(){
        getRunningProcess();//getRunningProcess
        getListView().setAdapter(this.mAdapter);//ListView add content
        bindEvent();//click on button
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
                Toast toast = Toast.makeText(getApplicationContext(),
                        mtext,
                        Toast.LENGTH_SHORT);
                toast.setGravity(Gravity.CENTER, 0, 0);
                toast.show();
            }
    private void killAllTasks(){
        CommonLibrary.KillProcess(this, mDetailList, mActivityManager, true);
        getRunningProcess();
        mAdapter = new TaskListAdapters.ProcessListAdapter(this, mDetailList);
        getListView().setAdapter(mAdapter);
        refreshMem();
        android.util.Log.e("ATK", "Manually kill ends");
    }

  /*  public void onItemClick(AdapterView<?> paramAdapterView, View paramView, int paramInt, long paramLong){
        doAction((TaskListAdapters.ListViewItem) paramView.getTag(), 4);
    }*/
    private void doAction(TaskListAdapters.ListViewItem paramListViewItem, int paramInt){
        switch (paramInt){
           /* case Setting.ACTION_KILL:
                kill(paramListViewItem);
                break;
            case Setting.ACTION_SWITCH:
                switchTo(paramListViewItem);
                break;*/
            case 1:
                selectOrUnselect(paramListViewItem);
                break;
           /* case Setting.ACTION_IGNORE:
                ignore(paramListViewItem);
                break;*/
            case 4:
                detail(paramListViewItem);
                break;
           /* case Setting.ACTION_MENU:
                menu(paramListViewItem);
                break;*/
            default:
                return;
        }
    }


    private void detail(TaskListAdapters.ListViewItem paramListViewItem){
        Intent localIntent;
        try{
            localIntent = new Intent("android.intent.action.VIEW");
            if (CommonLibrary.IsGingerbreadOrlater()){
                localIntent.setClassName("com.android.settings", "com.android.settings.applications.InstalledAppDetails");
                localIntent.setData(Uri.fromParts("package", paramListViewItem.detailProcess.getPackageName(), null));
                startActivity(localIntent);
                return;
            }
        }
        catch (Exception localException){
            android.util.Log.e("ATK", localException.toString());
            Toast.makeText(this, paramListViewItem.detailProcess.getPackageName(),Toast.LENGTH_SHORT).show();
        }
        return;
    }

            private void IgnoreSystemApp(){
                Iterator localIterator = getPackageManager().getInstalledPackages(PackageManager.GET_UNINSTALLED_PACKAGES).iterator();
                while (true){
                    if (!localIterator.hasNext())
                        return;
                    PackageInfo localPackageInfo = (PackageInfo)localIterator.next();
                    if (((ApplicationInfo.FLAG_SYSTEM & localPackageInfo.applicationInfo.flags) != 1) ||
                            (!ProcessDetailInfo.IsPersistentApp(localPackageInfo)) || (localPackageInfo.applicationInfo == null) ||
                            (CommonLibrary.IsSystemProcessName(localPackageInfo.applicationInfo.processName)))
                        continue;
                    ProcessDetailInfo.SetIgnored(true, this, localPackageInfo.applicationInfo.processName);
                }
            }

    private void selectOrUnselect(TaskListAdapters.ListViewItem paramListViewItem){
        if (paramListViewItem.detailProcess.getSelected()){
            paramListViewItem.iconCheck.setImageResource(R.drawable.btn_check_off);
            paramListViewItem.detailProcess.setSelected(false);
        }
        else{
            paramListViewItem.iconCheck.setImageResource(R.drawable.btn_check_on);
            paramListViewItem.detailProcess.setSelected(true);
        }
    }


    private void bindEvent(){
        IgnoreSystemApp();

        findViewById(R.id.btn_task).setOnClickListener(new View.OnClickListener() {
            public void onClick(View paramView) {
                android.util.Log.e("ATK", "Start manully kill!");
                killAllTasks();
            }
        });
        ListView localListView = (ListView)findViewById(R.id.listbody);
        localListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                doAction((TaskListAdapters.ListViewItem) view.getTag(), 1);
            }
        });
       /*localListView.setOnClickListener(new View.OnClickListener() {
           @Override
           public void onClick(View v) {
               onItemClick();
           }
       });*/
    }
    private ListView getListView(){
        return (ListView)findViewById(R.id.listbody);
    }
    public void getRunningProcess(){
        mDetailList = CommonLibrary.GetRunningProcess(this, mActivityManager);
        Log.i("Task_man"," data "+mDetailList);

        mAdapter = new TaskListAdapters.ProcessListAdapter(this, mDetailList);
        Log.i("Task_man"," adapter "+mAdapter);
    }

}
