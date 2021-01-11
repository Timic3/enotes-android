package com.enotes.ui.notes;

import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.CalendarView;
import android.widget.SeekBar;
import android.widget.TextView;

import com.enotes.R;

public class ReminderSelector extends AppCompatActivity {

    String date;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_reminder_selector);
        CalendarView  cv = findViewById(R.id.calendarView);
        TextView tvh = findViewById(R.id.textViewHour);
        TextView tvm = findViewById(R.id.textViewMinute);
        cv.setOnDateChangeListener(new CalendarView.OnDateChangeListener() {
            @Override
            public void onSelectedDayChange(CalendarView view, int year, int month, int dayOfMonth) {
                date = String.format("%d-%s-%s", year, format(month+1), format(dayOfMonth));
            }
        });
        SeekBar sbh = findViewById(R.id.seekBarHour);
        sbh.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                tvh.setText(String.format("Hour: %s",format(progress)));
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) { }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) { }
        });
        SeekBar sbm = findViewById(R.id.seekBarMinute);
        sbm.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                tvm.setText(String.format("Minute: %s",format(progress)));
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) { }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) { }
        });
    }

    public String format(int number){
        return number<10 ? "0"+number : number+"";
    }

    public void addReminder(View view){
        Intent data = new Intent();
        SeekBar sbh = findViewById(R.id.seekBarHour);
        SeekBar sbm = findViewById(R.id.seekBarMinute);
        data.setData(Uri.parse(date+" "+format(sbh.getProgress())+":"+format(sbm.getProgress())));
        setResult(Activity.RESULT_OK, data);
        finish();
    }

    public void cancel(View view){
        finish();
    }
}