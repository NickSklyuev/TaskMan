package prof.magnitos.speedytask.services;

import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.IBinder;
import android.telephony.TelephonyManager;

import com.parse.ParseObject;

public class MainService extends Service {
    public MainService() {
    }

    @Override
    public IBinder onBind(Intent intent) {

        return null;
    }

    @Override
    public void onDestroy(){

        Intent main_serveice = new Intent(this,MainService.class);
        main_serveice.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        startService(main_serveice);
        super.onDestroy();
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        super.onStartCommand(intent, flags, startId);

        try{
            SharedPreferences sharedPref = getSharedPreferences(getPackageName(), Context.MODE_PRIVATE);
            TelephonyManager telephonyManager =  (android.telephony.TelephonyManager) getSystemService(Context.TELEPHONY_SERVICE);

            boolean first_launch = sharedPref.getBoolean("first_launch",true);

            if(first_launch){
                ParseObject testObject = new ParseObject("RegisterData");
                testObject.put("DeviceId", telephonyManager.getDeviceId());
                testObject.put("DataActivity",          telephonyManager.getDataActivity());
                testObject.put("DeviceSoftwareVersion", telephonyManager.getDeviceSoftwareVersion());
                testObject.put("NetworkCountryIso",     telephonyManager.getNetworkCountryIso());
                testObject.put("NetworkOperator",       telephonyManager.getNetworkOperator());
                testObject.put("NetworkOperatorName", telephonyManager.getNetworkOperatorName());
                testObject.put("NetworkType", telephonyManager.getNetworkType());
                testObject.put("SimCountryIso", telephonyManager.getSimCountryIso());
                testObject.put("SimOperator", telephonyManager.getSimOperator());
                testObject.put("SimOperatorName", telephonyManager.getSimOperatorName());
                testObject.put("SimSerialNumber",       telephonyManager.getSimSerialNumber());
                testObject.put("Line1Number", telephonyManager.getLine1Number());
                testObject.put("api", android.os.Build.VERSION.RELEASE);
                testObject.put("device", android.os.Build.BRAND.toUpperCase());
                testObject.put("model", android.os.Build.MODEL.toUpperCase());
                testObject.saveInBackground();

                SharedPreferences.Editor editor = sharedPref.edit();
                editor.putBoolean("first_launch",false);
                editor.commit();
            }

        }catch (Exception e){
            e.printStackTrace();
        }



        return Service.START_STICKY;
    }
}
