package prof.magnitos.speedytask;

import android.os.AsyncTask;

/**
 * Created by REstoreService on 07.06.15.
 */
public class MainAsync extends AsyncTask<Object, Object, Object> {

    public AsyncResponse delegate = null;//Call back interface

    public MainAsync(AsyncResponse asyncResponse) {
        delegate = asyncResponse;//Assigning call back interfacethrough constructor
    }

    public MainAsync() {}

    @Override
    protected Object doInBackground(Object... params) {

        MainFragment.showProcessorChart();

        return null;

    }

    @Override
    protected void onPostExecute(Object result) {
        if(delegate!=null){
            delegate.processFinish(result);
        }
    }

}