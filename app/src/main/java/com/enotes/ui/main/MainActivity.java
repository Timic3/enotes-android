package com.enotes.ui.main;

import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.os.Bundle;

import com.enotes.R;
import com.enotes.remote.DatabaseHelper;
import com.enotes.remote.LoginRepository;
import com.enotes.ui.login.LoginActivity;
import com.enotes.ui.notes.AddNoteActivity;
import com.enotes.ui.notes.DrawingCanvas;
import com.enotes.utils.Utils;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.constraintlayout.widget.ConstraintLayout;

import android.speech.RecognizerIntent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.Locale;

import de.adorsys.android.securestoragelibrary.SecurePreferences;
import de.adorsys.android.securestoragelibrary.SecureStorageException;

public class MainActivity extends AppCompatActivity {

    DatabaseHelper databaseHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        System.out.println(LoginRepository.get(MainActivity.this, "id"));
        System.out.println(LoginRepository.get(MainActivity.this, "username"));
        System.out.println(LoginRepository.get(MainActivity.this, "token"));

        try {
            LoginRepository.logout(MainActivity.this);
        } catch (SecureStorageException e) {
            e.printStackTrace();
        }

        setContentView(R.layout.activity_main);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        databaseHelper = new DatabaseHelper(this);
        speak();

        // populate
        Cursor data = databaseHelper.data();
        while (data.moveToNext()) {
            byte[] bitmapArray = data.getBlob(7);
            Bitmap bitmap = null;
            if (bitmapArray != null) {
                bitmap = BitmapFactory.decodeByteArray(bitmapArray, 0, bitmapArray.length);
            }
            int id = data.getInt(0);
            if (data.getInt(6) == 0) {
                generateNote(id, data.getString(1), data.getString(2), data.getString(3), data.getString(4), data.getString(5), bitmap);
            } else {
                generateDrawing(id, bitmap);
            }
        }
        data.close();
    }

    public void generateDrawing(int id, Bitmap image){
        LinearLayout sv = (LinearLayout) findViewById(R.id.scrollViewLayout);
        View view = LayoutInflater.from(this).inflate(R.layout.drawing_sample,null);
        ViewGroup vg = (ViewGroup) view;
        ViewGroup cv = (ViewGroup) vg.getChildAt(0);
        ConstraintLayout cl = (ConstraintLayout) cv.getChildAt(0);

        ImageView iv = (ImageView) cl.getChildAt(0);
        iv.setImageBitmap(image);

        ImageView remove = (ImageView) cl.getChildAt(1);
        remove.setOnClickListener(v -> {
            ((ViewGroup) v.getParent().getParent().getParent()).removeView((View) v.getParent().getParent());
            databaseHelper.remove(id);
            Utils.toast(this, "Drawing has been removed.");
            //remove from file
        });

        sv.addView(view);
    }

    public void generateNote(int id, String title, String description, String items, String reminder, String color, Bitmap image){
        LinearLayout sv = (LinearLayout) findViewById(R.id.scrollViewLayout);

        View view = LayoutInflater.from(this).inflate(R.layout.card_sample,null);
        ViewGroup vg = (ViewGroup) view;
        ViewGroup cv = (ViewGroup) vg.getChildAt(0);
        ConstraintLayout cl = (ConstraintLayout) cv.getChildAt(0);

        TextView d = (TextView) cl.getChildAt(0);
        d.setText(description);

        TextView t = (TextView) cl.getChildAt(1);
        t.setText(title);

        TextView i = (TextView) cl.getChildAt(2);
        i.setText(items);

        ImageView picture = (ImageView) cl.getChildAt(3);
        picture.setImageBitmap(image);

        TextView r = (TextView) cl.getChildAt(4);
        if(reminder != null)
            r.setText(reminder);
        else
            r.setText("No reminder");

        if(color != null) {
            String[] colors = color.split(",");

            int red = Integer.parseInt(colors[0]);
            int green = Integer.parseInt(colors[1]);
            int blue = Integer.parseInt(colors[2]);

            cv.setBackgroundColor(Color.rgb(red, green, blue));
        }

        ImageView remove = (ImageView) cl.getChildAt(5);
        remove.setOnClickListener(v -> {
            ((ViewGroup) v.getParent().getParent().getParent()).removeView((View) v.getParent().getParent());
            databaseHelper.remove(id);
            Utils.toast(this, "Note has been removed.");
            //remove from file
        });

        sv.addView(view);
    }

    public void changeToAddNote(View view){
        startActivityForResult(new Intent(this, AddNoteActivity.class), 200);
    }

    public void changeToDrawing(View view){
        startActivityForResult(new Intent(this, DrawingCanvas.class), 600);
    }

    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == 200) {
            if(resultCode == RESULT_OK && data != null) {
                String returnedResult = data.getData().toString();
                String title = (String) data.getExtras().get("title");
                String description = (String) data.getExtras().get("description");
                String items = (String) data.getExtras().get("items");
                String color = (String) data.getExtras().get("color");
                String reminder = (String) data.getExtras().get("reminder");
                Bitmap image = (Bitmap) data.getExtras().get("image");
                System.out.println(returnedResult);

                byte[] bitmapArray = null;
                if (image != null) {
                    bitmapArray = Utils.bitmapToByteArray(image);
                }
                int result = databaseHelper.insert(title, description, items, reminder, color, false, bitmapArray);
                if (result != -1) {
                    Utils.toast(this, "Note successfully added.");
                } else {
                    Utils.toast(this, "Oops! Something went wrong.");
                }

                generateNote(result, title, description, items, reminder, color, image);
            }
        }
        if(requestCode == 1000){
            if(resultCode == RESULT_OK && data != null){
                ArrayList<String> result = data.getStringArrayListExtra(RecognizerIntent.EXTRA_RESULTS);
                System.out.println(result.get(0));
                switch(result.get(0)){
                    case "add note":
                        startActivityForResult(new Intent(this, AddNoteActivity.class), 200);
                        break;
                    case "add drawing":
                        startActivityForResult(new Intent(this, DrawingCanvas.class), 600);
                    default: break;
                }
            }
        }
        if(requestCode == 600){
            if(resultCode == RESULT_OK && data != null){
                byte[] byteArray = (byte[]) data.getExtras().get("image");
                int result = databaseHelper.insert(null, null, null, null, null, true, byteArray);
                if (result != -1) {
                    Utils.toast(this, "Drawing successfully added.");
                } else {
                    Utils.toast(this, "Oops! Something went wrong.");
                }

                Bitmap bitmap = BitmapFactory.decodeByteArray(byteArray, 0, byteArray.length);
                generateDrawing(result, bitmap);
            }
        }
    }

    public void speak(){
        Intent intent = new Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH);
        intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE_MODEL, RecognizerIntent.LANGUAGE_MODEL_FREE_FORM);
        intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE, Locale.getDefault());
        intent.putExtra(RecognizerIntent.EXTRA_PROMPT, "Say something");

        try{
            startActivityForResult(intent, 1000);
        }
        catch(Exception e){
            System.out.println(e.toString());
        }
    }

    public void speak(View view){
        Intent intent = new Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH);
        intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE_MODEL, RecognizerIntent.LANGUAGE_MODEL_FREE_FORM);
        intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE, Locale.getDefault());
        intent.putExtra(RecognizerIntent.EXTRA_PROMPT, "Say something");

        try{
            startActivityForResult(intent, 1000);
        }
        catch(Exception e){
            System.out.println(e.toString());
        }
    }
}