package com.enotes.ui.notes;

import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.content.Intent;
import android.content.res.ColorStateList;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.SeekBar;
import android.widget.TextView;

import com.enotes.R;

public class ColorPicker extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_color_picker);
        TextView colorPlacer = findViewById(R.id.colorPlacer);
        TextView tvr = findViewById(R.id.textViewRed);
        TextView tvg = findViewById(R.id.textViewGreen);
        TextView tvb = findViewById(R.id.textViewBlue);
        SeekBar sbr = findViewById(R.id.seekBarRed);
        SeekBar sbg = findViewById(R.id.seekBarGreen);
        SeekBar sbb = findViewById(R.id.seekBarBlue);
        sbr.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                tvr.setText(String.format("R: %s",progress));
                colorPlacer.setBackgroundTintList(ColorStateList.valueOf(Color.rgb(sbr.getProgress(), sbg.getProgress(), sbb.getProgress())));
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) { }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) { }
        });
        sbg.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                tvg.setText(String.format("G: %s",progress));
                colorPlacer.setBackgroundTintList(ColorStateList.valueOf(Color.rgb(sbr.getProgress(), sbg.getProgress(), sbb.getProgress())));
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) { }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) { }
        });
        sbb.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                tvb.setText(String.format("B: %s",progress));
                colorPlacer.setBackgroundTintList(ColorStateList.valueOf(Color.rgb(sbr.getProgress(), sbg.getProgress(), sbb.getProgress())));
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) { }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) { }
        });
    }

    public void addColor(View view){
        Intent data = new Intent();
        SeekBar sbr = findViewById(R.id.seekBarRed);
        SeekBar sbg = findViewById(R.id.seekBarGreen);
        SeekBar sbb = findViewById(R.id.seekBarBlue);
        data.setData(Uri.parse(String.format("%s,%s,%s",sbr.getProgress(), sbg.getProgress(), sbb.getProgress())));
        setResult(Activity.RESULT_OK, data);
        finish();
    }
}