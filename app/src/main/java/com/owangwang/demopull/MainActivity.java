package com.owangwang.demopull;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.TimePicker;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {
TimePicker timePicker;
static Toast toast;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        timePicker=findViewById(R.id.timePicker);

        timePicker.setOnTimeChangedListener(new TimePicker.OnTimeChangedListener() {
            @Override
            public void onTimeChanged(TimePicker view, int hourOfDay, int minute) {
                Log.d("当前所选时间：","----"+hourOfDay+"----"+minute);
                showToast("当前所选时间：----"+hourOfDay+"----"+minute);
            }
        });
    }
    public void showToast(String s){
        if (toast==null){
            toast=Toast.makeText(this,s,Toast.LENGTH_SHORT);

        }else {
            toast.setText(s);
            toast.setDuration(Toast.LENGTH_SHORT);

        }
        toast.show();

    }
}
