package net.ednovak.icfworkout;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.graphics.Typeface;
import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.text.SpannableString;
import android.text.style.StyleSpan;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.RelativeLayout;
import android.widget.TextView;

/**
 * An example full-screen activity that shows and hides the system UI (i.e.
 * status bar and navigation/system bar) with user interaction.
 */
public class SplashFragment extends Fragment{
    private final static String TAG = SplashFragment.class.getName();
    public final static String name = "ICF_prefs";
    private SharedPreferences prefs;
    private View rootView;
    private Context ctx;

    @Override
    public void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflator, ViewGroup container, Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.d(TAG, "Creating SplashFragment");

        //Intent launcherInt = getActivity().getIntent();
        //String content = launcherInt.getData().toString();

        rootView = inflator.inflate(R.layout.fragment_splash, container, false);

        ctx = getActivity().getApplicationContext();


        // Place version
        try {
            PackageInfo pInfo = ctx.getPackageManager().getPackageInfo(ctx.getPackageName(), 0);
            String v = pInfo.versionName;

            TextView tv = new TextView(ctx);
            SpannableString spanString = new SpannableString("V" + v);
            spanString.setSpan(new StyleSpan(Typeface.ITALIC), 0, spanString.length(), 0);
            tv.setText(spanString);
            tv.setTextColor(getResources().getColor(R.color.white));
            tv.setTextSize(10);

            RelativeLayout.LayoutParams tmpParams = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.WRAP_CONTENT, RelativeLayout.LayoutParams.WRAP_CONTENT);
            tmpParams.addRule(RelativeLayout.ALIGN_BOTTOM, R.id.title_pic);
            tmpParams.addRule(RelativeLayout.ALIGN_RIGHT, R.id.title_pic);
            tmpParams.setMargins(0, 0, 5, 0);
            tv.setLayoutParams(tmpParams);

            RelativeLayout top = (RelativeLayout)rootView.findViewById(R.id.topRL);
            top.addView(tv);

        } catch(PackageManager.NameNotFoundException e){

        }


        Log.d(TAG, "Finished Creating SplashFragment");
        return rootView;

    }



    @Override
    public void onResume(){
        super.onResume();

        View decorView = getActivity().getWindow().getDecorView();
        // Hide the status bar.
        int uiOptions = View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN;
        decorView.setSystemUiVisibility(uiOptions);

        // Set "Last Time" text
        prefs = ctx.getSharedPreferences(SplashFragment.name, ctx.MODE_PRIVATE);
        String day = prefs.getString("day", "none");

        TextView tv = (TextView)rootView.findViewById(R.id.last_time_text);
        if (day.equals("a")) {
            tv.setText("Last time you did workout A");
        } else if (day.equals("b")){
            tv.setText("Last time you did workout B");
        } else {
            tv.setText("Choose a workout!");
        }
    }
}
