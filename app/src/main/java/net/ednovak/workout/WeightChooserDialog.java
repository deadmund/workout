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

import android.app.Dialog;
import android.content.Context;
import android.view.View;
import android.widget.Button;
import android.widget.NumberPicker;

/**
 * Created by ejnovak on 3/11/16.
 */
public class WeightChooserDialog extends Dialog {

    private Context ctx;
    public Button yesButt;
    public Button noButt;
    private NumberPicker np;

    WeightChooserDialog(Context newCTX, int val){
        super(newCTX);
        ctx = newCTX;

        if(val < 0 || val > 200){
            throw new IllegalArgumentException("Value (" + val + ") must be 0 < val < 200");
        }

        this.setTitle("Choose Weight");
        this.setContentView(R.layout.weight_dialog);
        yesButt = (Button) this.findViewById(R.id.button1);
        noButt = (Button) this.findViewById(R.id.button2);
        np = (NumberPicker) this.findViewById(R.id.numberPicker1);
        np.setMinValue(0);
        np.setMaxValue(200);
        np.setValue(val);

        String[] values = new String[201];
        for(int i = 0; i < values.length; i++){
            String tmp = Integer.toString(i*5);
            values[i] = tmp;
        }
        np.setDisplayedValues(values);

        /**
         *         np.setMinValue(50);
         np.setMaxValue(500);
         np.setValue(200);
         * np.setOnValueChangedListener(new NumberPicker.OnValueChangeListener() {
            @Override
            public void onValueChange(NumberPicker picker, int oldVal, int newVal) {
                picker.setValue((newVal < oldVal)?oldVal-5:oldVal+5);
            }
        });


         np.setFormatter(new NumberPicker.Formatter() {
            @Override
            public String format(int value) {
                return String.valueOf(value * 5);
            }
        });
         */
        np.setWrapSelectorWheel(false);

        noButt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dismiss();
            }
        });

    }

    public void dismiss(){
        super.dismiss();
    }

    public int getValue(){
        return np.getValue();
    }
}
