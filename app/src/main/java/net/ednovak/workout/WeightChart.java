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

    private int max;
    private SharedPreferences sharedPrefs;
    private Context ctx;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_weight_chart);

        sharedPrefs = getPreferences(MODE_PRIVATE);
        max = sharedPrefs.getInt("max", 200);

        ctx = this;

        drawTable();
    }

    /**
     *
     */
    // This finds what plates should go on each side of the bar for the desired weight
    private Float[] findPlates(int weight){
        final float[] platesAvail = {45, 35, 25, 10, 5, 2.5f};
        int bar = 45;

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
        for(int total = 45; total <= max; total+=5){
            int aboveBar = total - 45;
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
        if(newMax < 45){
            newMax = 45;
            Toast.makeText(ctx, "Minimum weight is 45 lbs", Toast.LENGTH_LONG).show();
        }
        max = newMax;
        SharedPreferences.Editor editor = sharedPrefs.edit();
        editor.putInt("max", max);
        editor.commit();
    }
}
