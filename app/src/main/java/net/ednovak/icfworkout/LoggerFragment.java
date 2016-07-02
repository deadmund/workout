package net.ednovak.icfworkout;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Created by ejnovak on 7/1/16.
 */
public class LoggerFragment extends Fragment {
    private final static String TAG = LoggerFragment.class.getName();
    private final static String fname = "icf_log.txt";
    private View rootView;
    private Context ctx;

    @Override
    public void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflator, ViewGroup container, Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        //Intent launcherInt = getActivity().getIntent();
        //String content = launcherInt.getData().toString();

        rootView = inflator.inflate(R.layout.fragment_logger, container, false);

        ctx = getActivity().getApplicationContext();

        displayLog();
        return rootView;
    }

    @Override
    public void onResume(){
        super.onResume();
        displayLog();
    }

    private void displayLog() {
        TextView tv = (TextView) rootView.findViewById(R.id.logEntries);

        String logText = readLog(getActivity().getApplicationContext());


        StringBuilder sb = new StringBuilder();
        SimpleDateFormat sdf = new SimpleDateFormat("MMM dd, yyyy");

        String[] logLines = logText.split("\n");
        for (int i = 0; i < logLines.length; i++) {
            if (!logLines[i].equals("")) {
                String[] parts = logLines[i].split(",");

                Date d = new Date(Long.valueOf(parts[0]));
                sb.append(sdf.format(d) + "  " + parts[1] + ": " + parts[2] + "\n");
            }
        }
        Log.d(TAG, "sb: " + sb.toString());
        if (sb.length() == 0) {
            Log.d(TAG, "It's empty, adding placeholder text");
            sb.append("Log entires are created when you increase your weight for the various exercises.");
        }
        tv.setText(sb.toString());
    }




    public static void writeLog(Context ctx, String msg){
        // Test if file exists and append if so (otherwise create


        File f = ctx.getFileStreamPath(fname);
        try {
            FileOutputStream fos;
            if (f.exists()) {
                fos = ctx.openFileOutput(fname, Context.MODE_APPEND); // Append to existing file
            } else {
                fos = ctx.openFileOutput(fname, Context.MODE_PRIVATE); // Create new file
            }
            msg = msg + "\n";
            fos.write(msg.getBytes()); // throws IO exception
            fos.close();
        } catch (FileNotFoundException e1){
            Log.d(TAG, "Something went really wrong and there was a file not found exception");
            e1.printStackTrace();
        } catch (IOException e2){
            Log.d(TAG, "Error writing to the file");
            e2.printStackTrace();
        }
    }

    public static String getFilePath(Context ctx){
        String path = ctx.getFilesDir().getAbsolutePath() + "/" + fname;
        return path;
    }

    public static String readLog(Context ctx){
        File f = ctx.getFileStreamPath(fname);
        try {
            FileInputStream fis;
            fis = ctx.openFileInput(fname); // Read from file (throws exception)

            StringBuilder sb = new StringBuilder();
            int b;
            while((b = fis.read()) != -1){ // fis.read() throws IO exception
                sb.append((char)b);
            }

            fis.close(); // throws IO exception

            return sb.toString();

        } catch (FileNotFoundException e1){
            Log.d(TAG, "File not found trying to read");
            e1.printStackTrace();
        } catch (IOException e2){
            Log.d(TAG, "I/O Error reading file");
            e2.printStackTrace();
        }

        return null;
    }


    public static void eraseLog(Context ctx){
        // Create new file to erase old file
        try {
            FileOutputStream fos = ctx.openFileOutput(fname, Context.MODE_PRIVATE); // Create new file
            fos.close();
        } catch (FileNotFoundException e1) {
            Log.d(TAG, "There was a problem trying to erase the log file.");
            e1.printStackTrace();
        } catch (IOException e2){
            Log.d(TAG, "There was a problem trying to erase the log file.");
            e2.printStackTrace();
        }

    }
}
