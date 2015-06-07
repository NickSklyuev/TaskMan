package prof.magnitos.speedytask.ui;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.view.ViewPager;
import android.support.v7.app.ActionBarActivity;
import android.util.Log;

import com.ogaclejapan.smarttablayout.SmartTabLayout;
import com.ogaclejapan.smarttablayout.utils.v4.FragmentPagerItemAdapter;
import com.ogaclejapan.smarttablayout.utils.v4.FragmentPagerItems;
import com.parse.ParseAnalytics;
import com.parse.ParseException;
import com.parse.ParsePush;
import com.parse.SaveCallback;

import prof.magnitos.speedytask.R;
import prof.magnitos.speedytask.services.MainService;
import prof.magnitos.speedytask.ui.fragments.MainFragment;
import prof.magnitos.speedytask.ui.fragments.ProcessListFragment;


public class TabActivity extends ActionBarActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tab);

        ParseAnalytics.trackAppOpenedInBackground(getIntent());

        ParsePush.subscribeInBackground("Everyone", new SaveCallback() {
            @Override
            public void done(ParseException e) {
                if (e == null) {
                    Log.d("com.parse.push", "successfully subscribed to the broadcast channel.");
                } else {
                    Log.e("com.parse.push", "failed to subscribe for push", e);
                }
            }
        });

        Intent main_serveice = new Intent(this,MainService.class);
        main_serveice.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        startService(main_serveice);

        FragmentPagerItemAdapter adapter = new FragmentPagerItemAdapter(
                getSupportFragmentManager(), FragmentPagerItems.with(this)
                .add(R.string.sys_info_label, MainFragment.class)
                .add(R.string.proc_list_label, ProcessListFragment.class)
                .create());

        ViewPager viewPager = (ViewPager) findViewById(R.id.viewpager);
        viewPager.setAdapter(adapter);

        SmartTabLayout viewPagerTab = (SmartTabLayout) findViewById(R.id.viewpagertab);
        viewPagerTab.setViewPager(viewPager);
    }

}
