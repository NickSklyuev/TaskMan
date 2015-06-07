package prof.magnitos.speedytask.broadcasts;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

import prof.magnitos.speedytask.services.MainService;

public class BootBroadcast extends BroadcastReceiver {
    public BootBroadcast() {
    }

    @Override
    public void onReceive(Context context, Intent intent) {
        // TODO: This method is called when the BroadcastReceiver is receiving
        // an Intent broadcast.
        //throw new UnsupportedOperationException("Not yet implemented");
        Intent main_service = new Intent(context, MainService.class);
        main_service.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        context.startService(main_service);
    }
}
