package net.ednovak.icfworkout;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;

public class Splash extends FragmentActivity {
    private final static String TAG = Splash.class.getName();
    private CustomPageAdapter pageAdapter;
    private ViewPager vp;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.d(TAG, "Creating splash pageview activity");
        setContentView(R.layout.activity_splash);

        pageAdapter = new CustomPageAdapter(getSupportFragmentManager());
        vp = (ViewPager) findViewById(R.id.pager);
        vp.setAdapter(pageAdapter);

        Log.d(TAG, "Done creating splash pageview activity");
    }


    public class CustomPageAdapter extends FragmentPagerAdapter {
        private final int NUM_ITEMS = 2;

        public CustomPageAdapter(FragmentManager fm){
            super(fm);
        }

        @Override
        public int getCount(){
            return NUM_ITEMS;
        }

        @Override
        public Fragment getItem(int pos){
            Log.d(TAG, "PageAdapter.getItem() pos: " + pos);
            switch(pos){
                case 0:
                    return new SplashFragment();
                case 1:
                    return new LoggerFragment();
                default:
                    Log.d(TAG, "Invalid fragment position: " + pos);
                    return null;
            }
        }
    }


    public void startWorkout(View v) {
        Context ctx = getApplicationContext();
        SharedPreferences prefs = ctx.getSharedPreferences(SplashFragment.name, ctx.MODE_PRIVATE);
        String day = prefs.getString("day", "none");
        Button b = (Button) v;
        SharedPreferences.Editor editor = prefs.edit();

        if (b.getText().toString().equals("Workout A")) {
            editor.putString("day", "a");
        } else if (b.getText().toString().equals("Workout B")) {
            editor.putString("day", "b");
        }
        editor.commit();

        Intent i = new Intent(ctx, Tracker.class);
        startActivity(i);
    }


}
