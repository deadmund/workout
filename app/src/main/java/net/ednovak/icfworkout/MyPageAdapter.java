package net.ednovak.icfworkout;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.view.PagerAdapter;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.ViewGroup;

/**
 * Created by ejnovak on 7/2/16.
 */
public class MyPageAdapter extends FragmentStatePagerAdapter {
    private final static String TAG = MyPageAdapter.class.getName();
    private Context ctx;

    public MyPageAdapter(FragmentManager fm){
        super(fm);
    }

    @Override
    public Fragment getItem(int pos){
        Fragment f = new CustomFragment();
        Bundle args = new Bundle();
        args.putInt("INT", pos + 1);
        f.setArguments(args);
        return f;
    }

    @Override
    public int getCount(){
        return 100;
    }

    @Override
    public CharSequence getPageTitle(int position){
        return "Object " + (position + 1);
    }

    public static class CustomFragment extends Fragment {

    }

}

