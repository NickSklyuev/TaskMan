package com.example.taskerapp;

import android.app.Activity;
import android.app.ActivityManager;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.util.Log;
import android.widget.TextView;

import java.util.concurrent.TimeUnit;

/**
 * Created by  on 06.06.2015.
 */
public class SplashActiv extends Activity {


    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.splash_activity);
        load l=new load();
        l.execute(this);
       // sleep();
      /* Intent intn = new Intent(getBaseContext(),TabActivity.class);
        startActivity(intn);
        this.finish();*/

        /*Intent intn = new Intent(this,TabActivity.class);
        startActivity(intn);
        this.finish();*/


    }
    class load extends AsyncTask<Activity, Object, Object> {

        @Override
        protected Object doInBackground(Activity... a) {
            // TODO Auto-generated method stub


            try { Thread.sleep(3000); } catch (InterruptedException ignore) {}
            Log.i("ss", "splash");
            Intent intent = new Intent(a[0], TabActivity.class);
            startActivity(intent);
            a[0].finish();


            return null;
        }

    }
/*@Override
public void onFinish() {super.onFinish();}*/
/*private void sleep(){
    try{
        TimeUnit.MILLISECONDS.sleep(3000);
    }catch (InterruptedException e){e.printStackTrace();}
}*/

  /*  @Override
    protected void onResume(){
        try {

            TimeUnit.MILLISECONDS.sleep(3000);
            //super.onResume();
           *//* Thread.sleep(3000);*//*
            //tv.setText("");
       *//* TextView tv=(TextView) findViewById(R.id.loadtext);
        tv.setText("hello world");*//*
            Intent intn = new Intent(this, TabActivity.class);
            startActivity(intn);
            this.finish();
        }catch (InterruptedException e){e.printStackTrace();}

    }*/
}
