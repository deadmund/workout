package net.ednovak.workout;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

/**
 * An example full-screen activity that shows and hides the system UI (i.e.
 * status bar and navigation/system bar) with user interaction.
 */
public class Splash extends AppCompatActivity {
    private final static String TAG = Splash.class.getName();


    public final static String name = "ICF_prefs";

    /**
     * Whether or not the system UI should be auto-hidden after
     * {@link #AUTO_HIDE_DELAY_MILLIS} milliseconds.
     */
    private static final boolean AUTO_HIDE = true;

    /**
     * If {@link #AUTO_HIDE} is set, the number of milliseconds to wait after
     * user interaction before hiding the system UI.
     */
    private static final int AUTO_HIDE_DELAY_MILLIS = 3000;


    private SharedPreferences prefs;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_splash);
        
    }


    @Override
    protected void onResume(){
        super.onResume();

        View decorView = getWindow().getDecorView();
        // Hide the status bar.
        int uiOptions = View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN;
        decorView.setSystemUiVisibility(uiOptions);

        // Set "Last Time" text
        prefs = getSharedPreferences(Splash.name, MODE_PRIVATE);
        String day = prefs.getString("day", "none");

        TextView tv = (TextView)findViewById(R.id.last_time_text);
        if (day.equals("a")) {
            tv.setText("Last time you did workout A");
        } else if (day.equals("b")){
            tv.setText("Last time you did workout B");
        } else {
            tv.setText("Choose a workout!");
        }
    }


    public void startWorkout(View v) {
        Button b = (Button) v;
        SharedPreferences.Editor editor = prefs.edit();

        if (b.getText().toString().equals("Workout A")) {
            editor.putString("day", "a");
        } else if (b.getText().toString().equals("Workout B")) {
            editor.putString("day", "b");
        }
        editor.commit();

        Intent i = new Intent(this, Tracker.class);
        startActivity(i);
    }
}
