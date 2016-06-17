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

package net.ednovak.workout;

import android.app.ActionBar;
import android.app.Dialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.animation.DecelerateInterpolator;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.NumberPicker;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;

public class WeightChart extends AppCompatActivity {
    private final static String TAG = WeightChart.class.getName();

    private SharedPreferences sharedPrefs;
    private Context ctx;

    private float[] platesAvail;
    private int bar;
    private int max;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_weight_chart);

        ctx = this;
    }

    protected void onResume(){
        super.onResume();

        sharedPrefs = getSharedPreferences(Splash.name, MODE_PRIVATE);
        max = sharedPrefs.getInt("max", 200);

        String unit = sharedPrefs.getString("unit", "lbs");
        if(unit.equals("lbs")){
            bar = 45;
            platesAvail = new float[] {45, 35, 25, 10, 5, 2.5f};
        } else if(unit.equals("kg")){
            bar = 20;
            platesAvail = new float[] {25, 20, 15, 10, 5, 2.5f, 1.25f};
        }



        drawTable();

    }

    /**
     *
     */
    // This finds what plates should go on each side of the bar for the desired weight
    private Float[] findPlates(int weight){

        ArrayList<Float> answer = new ArrayList<Float>();

        float eachSideWeight = (weight - bar) / 2.0f;
        float weightRemaining = eachSideWeight;

        int count = 0;
        while(weightRemaining > 0 && count < 10){
            count++;
            //Log.d(TAG, "Weight Remaining: " + weightRemaining);
            for(int i = 0; i < platesAvail.length; i++){
                float cur = platesAvail[i];
                //Log.d(TAG, "trying: " + cur);
                if(cur <= weightRemaining){
                    weightRemaining -= cur;
                    answer.add(new Float(cur));
                    //Log.d(TAG, "Selected " + cur);
                    break;
                }
            }
        }
        return answer.toArray(new Float[answer.size()]);
    }

    private String platesToString(Float[] plates){
        StringBuilder sb = new StringBuilder();
        for(int i = 0; i < plates.length; i++){
            if(plates[i] % 1 == 0){
                sb.append(String.format("%d", (long)(double)plates[i]));
            }

            else{
                sb.append(plates[i]);
            }

            if( i < plates.length - 1){
                sb.append(" + ");
            }
        }
        return sb.toString();
    }

    private void drawTable(){
        TableLayout tl = (TableLayout)findViewById(R.id.table);
        tl.removeAllViews(); // Destory all the rows

        // Draw all the rows
        for(int total = bar; total <= max; total+=5){
            int aboveBar = total - bar;
            Float[] plates = findPlates(total);

            TableRow tr = new TableRow(this);
            TableRow.LayoutParams lpr = new TableRow.LayoutParams(TableRow.LayoutParams.MATCH_PARENT, TableRow.LayoutParams.WRAP_CONTENT);
            tr.setLayoutParams(lpr);
            tl.addView(tr);

            tr.setOnClickListener(new View.OnClickListener() {

                private boolean highlighted;

                @Override
                public void onClick(View v) {

                    int color = 0;
                    if(highlighted){
                        color = getResources().getColor(R.color.textGrey);
                        highlighted = false;
                    } else {
                        color = getResources().getColor(R.color.highlightRed);
                        highlighted = true;
                    }

                    TableRow curRow = (TableRow)v;
                    for(int i = 0; i < curRow.getChildCount(); i++) {
                        TextView tmp = (TextView) (curRow.getChildAt(i));
                        tmp.setTextColor(color);
                    }
                }
            });

            if(total == max){
                tr.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        int startingWeight = Math.min(max / 5, 200);
                        final WeightChooserDialog d = new WeightChooserDialog(ctx, startingWeight);

                        d.yesButt.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                setMax(d.getValue() * 5);
                                drawTable();
                                d.dismiss();
                            }
                        });
                        d.show();
                    }
                });
            }

            if(total % 10 == 0){
                tr.setBackgroundColor(getResources().getColor(R.color.weightBGGreyLight));
            }

            // Total weight
            TextView tvTotal = new TextView(this);
            TableRow.LayoutParams lpTV1 = new TableRow.LayoutParams(0, TableRow.LayoutParams.WRAP_CONTENT, 1f);
            lpTV1.setMargins(0, 0, 0, 10);
            tvTotal.setGravity(Gravity.CENTER);
            tvTotal.setTextColor(getResources().getColor(R.color.textGrey));
            tvTotal.setTextSize(32f);
            tvTotal.setText(String.valueOf(total));
            tvTotal.setLayoutParams(lpTV1);
            tr.addView(tvTotal);

            // Above Bar
            TextView tvAbove = new TextView(this);
            TableRow.LayoutParams lpTV2 = new TableRow.LayoutParams(0, TableRow.LayoutParams.WRAP_CONTENT, 1f);
            tvAbove.setGravity(Gravity.CENTER_VERTICAL|Gravity.CENTER_HORIZONTAL);
            tvAbove.setTextColor(getResources().getColor(R.color.textGrey));
            tvAbove.setTextSize(16f);
            tvAbove.setText(String.valueOf(aboveBar));
            tvAbove.setLayoutParams(lpTV2);
            tr.addView(tvAbove);

            // On Each Side
            TextView tvSide = new TextView(this);
            TableRow.LayoutParams lpTV3 = new TableRow.LayoutParams(0, TableRow.LayoutParams.WRAP_CONTENT, 1.0f);
            tvSide.setGravity(Gravity.CENTER);
            tvSide.setTextColor(getResources().getColor(R.color.textGrey));
            tvSide.setTextSize(16f);
            tvSide.setText(platesToString(plates));
            tvSide.setLayoutParams(lpTV3);
            tr.addView(tvSide);

        }
    }

    private void setMax(int newMax){
        if(newMax < bar){
            newMax = bar;
            Toast.makeText(ctx, "Minimum weight is 45 lbs", Toast.LENGTH_LONG).show();
        }
        max = newMax;
        SharedPreferences.Editor editor = sharedPrefs.edit();
        editor.putInt("max", max);
        editor.commit();
    }
}
