package net.ednovak.workout;


import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.res.TypedArray;
import android.text.Html;
import android.util.AttributeSet;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.animation.AccelerateDecelerateInterpolator;
import android.view.animation.Animation;
import android.view.animation.AnticipateInterpolator;
import android.view.animation.DecelerateInterpolator;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.NumberPicker;
import android.widget.TextView;

import static android.R.color.*;

public class Exercise extends LinearLayout {
    private final static String TAG = Exercise.class.getName();

    private TextView title;
    private TextView weight;
    private LinearLayout box;
    private int weightInteger;
    private boolean finished = false;


    public Exercise(Context context, AttributeSet attrs) {
        super(context, attrs);

        this.setOrientation(HORIZONTAL);


        TypedArray a = context.obtainStyledAttributes(attrs,
                R.styleable.Exercise, 0, 0);
        String titleText = a.getString(R.styleable.Exercise_exer_text);
        String weightText = a.getString(R.styleable.Exercise_exer_weight);
        if(weightText == null){
            weightText = "0";
        }
        weightInteger = Integer.valueOf(weightText);
        int valueColor = a.getColor(R.styleable.Exercise_exer_color, getResources().getColor(holo_blue_dark));

        a.recycle();

        // Here we build the child views in code. They could also have
        // been specified in an XML file.

        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        inflater.inflate(R.layout.exercise_layout, this, true);

        // These must happen before the setTitle and setWeight methods are called
        title = (TextView)findViewById(R.id.exertext);
        weight = (TextView)findViewById(R.id.exerweight);
        box = (LinearLayout)findViewById(R.id.exerbox);

        // These must happen after the inflation step
        setTitle(titleText);
        setWeight(weightInteger);
        //setColor(valueColor);

        // Set the tag
        if(this.getTag() == null){
            this.setTag("exercise");
        }


        // setup weight longclick listener
        weight.setOnLongClickListener(new OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                weightInteger += 5;
                setWeight(weightInteger);
                return true;
            }
        });

        weight.setOnTouchListener(new OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                if(event.getAction() == MotionEvent.ACTION_DOWN){
                    v.animate().scaleX(0.85f).setDuration(200).setInterpolator(new DecelerateInterpolator()).withLayer();
                    v.animate().scaleY(0.85f).setDuration(200).setInterpolator(new DecelerateInterpolator()).withLayer();
                } else if (event.getAction() == MotionEvent.ACTION_UP || event.getAction() == MotionEvent.ACTION_CANCEL){
                    v.animate().cancel();
                    v.clearAnimation();
                    v.setScaleX(1.0f);
                    v.setScaleY(1.0f);
                }
                return false;
            }
        });


        title.setOnLongClickListener(new OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                // True, but this isn't actually used in this code block
                //TextView cur = (TextView)v;

                int startingWeight = Math.min(weightInteger/5, 200);
                final WeightChooserDialog d = new WeightChooserDialog(getContext(), weightInteger/5);

                d.yesButt.setOnClickListener(new View.OnClickListener()
                {
                    @Override
                    public void onClick(View v) {
                        setWeight(d.getValue() * 5);
                        d.dismiss();
                    }
                });
                d.show();
                return true;
            }
        });

        title.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                // This tmp could just be "box"
                final LinearLayout tmp = (LinearLayout) v.getParent();
                if(!finished) {
                    finished = true;
                    tmp.setBackgroundColor(getResources().getColor(R.color.finishedGreen));
                    tmp.animate().setStartDelay(1000).alpha(0.15f).setDuration(1000).withLayer();
                } else{
                    setNotFinished();
                }
            }
        });
    }

    /**
     * Convenience method to set the title of a SpeechView
     */
    public void setTitle(String newTitleText) {
        title.setText(newTitleText);
    }

    public void setWeight(int newW){
        weightInteger = newW;
        weight.setText(weightInteger + " lbs");
    }

    public int getWeight(){
        return weightInteger;
    }

    public String getTitle(){
        return title.getText().toString();
    }

    public void setNotFinished(){
        finished = false;
        box.animate().cancel();
        box.clearAnimation();
        box.setBackgroundColor(getResources().getColor(R.color.defaultHoloGrey));
        box.setAlpha(1.0f);
    }
}