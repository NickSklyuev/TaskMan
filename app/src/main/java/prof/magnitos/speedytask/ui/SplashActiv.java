package prof.magnitos.speedytask.ui;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;

import com.gc.materialdesign.views.ProgressBarCircularIndeterminate;

import prof.magnitos.speedytask.R;
import prof.magnitos.speedytask.components.AsyncResponse;
import prof.magnitos.speedytask.components.MainAsync;

/**
 * Created by  on 06.06.2015.
 */
public class SplashActiv extends Activity {


    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.splash_activity);

        ProgressBarCircularIndeterminate dp = (ProgressBarCircularIndeterminate) findViewById(R.id.dialogProgress);
        dp.setBackgroundColor(Color.WHITE);


        new MainAsync(new AsyncResponse() {
            @Override
            public void processFinish(Object output) {
                Intent ta = new Intent(getApplicationContext(),TabActivity.class);
                ta.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(ta);
                finish();
            }
        }).execute(new Object[]{});


       // sleep();
      /* Intent intn = new Intent(getBaseContext(),TabActivity.class);
        startActivity(intn);
        this.finish();*/

        /*Intent intn = new Intent(this,TabActivity.class);
        startActivity(intn);
        this.finish();*/


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
