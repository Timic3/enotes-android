package com.enotes.ui.notes;

import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.view.View;

import com.enotes.R;

import java.io.ByteArrayOutputStream;

public class DrawingCanvas extends AppCompatActivity {

    MyCanvas canvas;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_drawing_canvas);
        canvas = new MyCanvas(this, null);
        CardView cv = findViewById(R.id.drawingView);
        cv.addView(canvas);
    }

    public void saveCanvas(View view){
        Intent data = new Intent();
        CardView cv = findViewById(R.id.drawingView);
        Bitmap bm = getBitmapFromView(cv);
        ByteArrayOutputStream stream = new ByteArrayOutputStream();
        bm.compress(Bitmap.CompressFormat.PNG, 100, stream);
        byte[] byteArray = stream.toByteArray();
        // send bitmap to server
        data.putExtra("image", byteArray);
        setResult(Activity.RESULT_OK, data);
        finish();
    }

    public void cancel(View view){
        finish();
    }

    public static Bitmap getBitmapFromView(View view) {
        Bitmap returnedBitmap = Bitmap.createBitmap(view.getWidth(), view.getHeight(),Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(returnedBitmap);
        Drawable bgDrawable =view.getBackground();
        if (bgDrawable!=null)
            bgDrawable.draw(canvas);
        else
            canvas.drawColor(Color.WHITE);
        view.draw(canvas);
        return returnedBitmap;
    }
}