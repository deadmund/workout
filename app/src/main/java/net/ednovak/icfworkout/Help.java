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

import android.content.Context;
import android.content.DialogInterface;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

public class Help extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_help);

        // Place version
        try {
            PackageInfo pInfo = getPackageManager().getPackageInfo(getPackageName(), 0);
            String v = pInfo.versionName;

            TextView tv = (TextView)findViewById(R.id.version_string);
            tv.setText("version "  + v);

        } catch(PackageManager.NameNotFoundException e){

        }

    }

    public void eraseLog(View v){
        final Context ctx = Help.this; // grab context ref for all this

        AlertDialog.Builder alDiagBuilder = new AlertDialog.Builder(ctx, R.style.Base_Theme_AppCompat_Light_Dialog);
        alDiagBuilder.setTitle("Delete Log");
        alDiagBuilder.setMessage("Are you sure you want to delete all log entries?");

        alDiagBuilder.setPositiveButton("Yes", new DialogInterface.OnClickListener(){
            public void onClick(DialogInterface dialog, int id){
                // Clear Log
                LoggerFragment.eraseLog(ctx);
            }
        });
        alDiagBuilder.setNegativeButton("Cancel", new DialogInterface.OnClickListener(){
            public void onClick(DialogInterface dialog, int id){
                dialog.cancel();
            }
        });

        alDiagBuilder.create().show();
    }
}
