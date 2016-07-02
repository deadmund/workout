//    Copyright 2016 Ed Novak

//    This program is free software: you can redistribute it and/or modify
//    it under the terms of the GNU General Public License as published by
//    the Free Software Foundation, either version 3 of the License, or
//    (at your option) any later version.

//    This program is distributed in the hope that it will be useful,
//    but WITHOUT ANY WARRANTY; without even the implied warranty of
//    MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
//    GNU General Public License for more details.

//    You should have received a copy of the GNU General Public License
//    along with this program.  If not, see <http://www.gnu.org/licenses/>.

package net.ednovak.icfworkout;

import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.LinearLayout;
import android.widget.Toast;

public class Tracker extends AppCompatActivity {
    private final static String TAG = Tracker.class.getName();

    private LinearLayout mainView;
    private SharedPreferences prefs;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        prefs = getSharedPreferences(SplashFragment.name, MODE_PRIVATE);
        String day = prefs.getString("day", "a");
        if(day.equals("a")){
            setContentView(R.layout.activity_tracker_a);
        } else if (day.equals("b")){
            setContentView(R.layout.activity_tracker_b);
        }

        mainView = (LinearLayout)findViewById(R.id.mainView);
        for(int i = 0; i < mainView.getChildCount(); i++) {
            try {
                if (mainView.getChildAt(i).getTag().toString().equals("exercise")) {
                    Exercise cur = (Exercise) mainView.getChildAt(i);
                    cur.setWeight(prefs.getInt(cur.getTitle(), 0), false);
                }
            } catch (NullPointerException e) {
                continue;
            }
        }

        getSupportActionBar().setTitle(getSupportActionBar().getTitle() + " " + day.toUpperCase());
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_tracker, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        Intent intent; // Just in case I need it
        switch(id){
            case R.id.action_clear:
                for(int i = 0; i < mainView.getChildCount(); i++){
                    try {
                        if (mainView.getChildAt(i).getTag().toString().equals("exercise")) {
                            Exercise cur = (Exercise) mainView.getChildAt(i);
                            cur.setNotFinished();
                        }
                    } catch (NullPointerException e){
                        continue;
                    }
                }
                return true;


            case R.id.action_chart:
                intent = new Intent(this, WeightChart.class);
                startActivity(intent);
                return true;

            case R.id.action_help:
                intent = new Intent(this, Help.class);
                startActivity(intent);
                return true;

            case R.id.action_switchDay:
                switchDay();
                finish();
                intent = new Intent(this, Tracker.class);
                startActivity(intent);
                return true;

            case R.id.action_switchUnit:
                switchUnit();
                finish();
                intent = new Intent(this, Tracker.class);
                startActivity(intent);
                return true;

            default:
                // This really should never happen
                Log.d(TAG, "Somehow a menu item that should not exist was selected: " + id);
                Toast.makeText(getApplicationContext(), "Invalid menu option", Toast.LENGTH_SHORT).show();
        }

        return super.onOptionsItemSelected(item);
    }


    private void switchDay(){
        SharedPreferences.Editor editor = prefs.edit();
        String day = prefs.getString("day", "a");
        if(day.equals("a")){
            day = "b";
        } else if (day.equals("b")){
            day = "a";
        }
        editor.putString("day", day);
        editor.commit();
    }

    private void switchUnit(){
        SharedPreferences.Editor editor = prefs.edit();
        String unit = prefs.getString("unit", "lbs");
        if(unit.equals("lbs")){
            unit = "kg";
        } else if (unit.equals("kg")) {
            unit = "lbs";
        }
        editor.putString("unit", unit);
        editor.commit();
    }

    @Override
    public void onPause(){
        super.onPause();

        // Save weights
        SharedPreferences.Editor editor = prefs.edit();
        for(int i = 0; i < mainView.getChildCount(); i++){
            try{
                if(mainView.getChildAt(i).getTag().toString().equals("exercise")){
                    Exercise cur = (Exercise)mainView.getChildAt(i);
                    String title = cur.getTitle();
                    int weight = cur.getWeight();
                    editor.putInt(title, weight);
                }
            } catch (NullPointerException e){
                continue;
            }
            editor.commit();
        }
    }
}
