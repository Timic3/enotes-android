package com.enotes.ui.notes;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.res.ColorStateList;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.speech.RecognizerIntent;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.enotes.R;

import java.util.ArrayList;
import java.util.Locale;

public class AddNoteActivity extends AppCompatActivity {

    String reminder = null;
    String color = null;
    Bitmap bitmap = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_note);

        if(ContextCompat.checkSelfPermission(this, Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED){
            ActivityCompat.requestPermissions(this, new String[] { Manifest.permission.CAMERA }, 100);
        }
        speak();
    }

    public void returnNoteData(View view){
        Intent data = new Intent();
        EditText tet = findViewById(R.id.editTextTitle);
        String title = tet.getText().toString();
        EditText ted = findViewById(R.id.editTextDescription);
        String description = ted.getText().toString();
        EditText tei = findViewById(R.id.editTextItems);
        String items = tei.getText().toString();;

        data.setData(Uri.parse(String.format("{ title: %s, description: %s, items: (%s), color: (%s), reminder: %s }", title, description, items, color, reminder)));
        data.putExtra("title", title);
        data.putExtra("description", description);
        data.putExtra("items", items);
        data.putExtra("color", color);
        data.putExtra("reminder", reminder);
        data.putExtra("image", bitmap);
        setResult(Activity.RESULT_OK, data);
        finish();
    }

    public void returnNoteData(){
        Intent data = new Intent();
        EditText tet = findViewById(R.id.editTextTitle);
        String title = tet.getText().toString();
        EditText ted = findViewById(R.id.editTextDescription);
        String description = ted.getText().toString();
        EditText tei = findViewById(R.id.editTextItems);
        String items = tei.getText().toString();;

        data.setData(Uri.parse(String.format("{ title: %s, description: %s, items: (%s), color: (%s), reminder: %s }", title, description, items, color, reminder)));
        data.putExtra("title", title);
        data.putExtra("description", description);
        data.putExtra("items", items);
        data.putExtra("color", color);
        data.putExtra("reminder", reminder);
        data.putExtra("image", bitmap);
        setResult(Activity.RESULT_OK, data);
        finish();
    }

    public void setReminderVisible(View view){
        startActivityForResult(new Intent(this, ReminderSelector.class), 300);
    }

    public void setColorPickVisible(View view){
        startActivityForResult(new Intent(this, ColorPicker.class), 400);
    }

    public void cancel(View view){
        finish();
    }

    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == 300) {
            if (resultCode == RESULT_OK) {
                String returnedResult = data.getData().toString();
                TextView tv = findViewById(R.id.reminder);
                String[] tmp = returnedResult.split(" ");
                findViewById(R.id.reminder).setVisibility(View.VISIBLE);
                reminder = tmp[0]+" "+tmp[1];
                tv.setText(String.format("Reminder: %s Time: %s", tmp[0],tmp[1]));
            }
        }
        if(requestCode == 400){
            if (resultCode == RESULT_OK) {
                String returnedResult = data.getData().toString();
                System.out.println(returnedResult);
                TextView cv = findViewById(R.id.colorView);
                color = returnedResult;
                String[] colors = returnedResult.split(",");
                cv.setBackgroundTintList(ColorStateList.valueOf(Color.rgb(Integer.parseInt(colors[0]), Integer.parseInt(colors[1]), Integer.parseInt(colors[2]))));
            }
        }

        if(requestCode == 100){
            bitmap = (Bitmap) data.getExtras().get("data");
            ImageView iv = findViewById(R.id.imageView);
            iv.setImageBitmap(bitmap);
        }
        if(requestCode == 1000){
            if(resultCode == RESULT_OK && data != null){
                ArrayList<String> result = data.getStringArrayListExtra(RecognizerIntent.EXTRA_RESULTS);
                if(!result.get(0).equals("cancel")) {
                    String[] returned = result.get(0).split(" ");
                    String command = "";
                    String dataToAdd = "";
                    if (returned.length > 1) {
                        command = returned[0] + " " + returned[1];
                        for (int i = 2; i < returned.length; i++) {
                            dataToAdd = dataToAdd + " " + returned[i];
                        }
                    }
                    System.out.println(command);
                    switch (command) {
                        case "add title":
                            EditText title = findViewById(R.id.editTextTitle);
                            System.out.println(dataToAdd);
                            title.setText(dataToAdd);
                            speak();
                            break;
                        case "add description":
                            EditText description = findViewById(R.id.editTextDescription);
                            description.setText(dataToAdd);
                            speak();
                            break;
                        case "add note":
                            returnNoteData();
                            break;
                        default:
                            speak();
                            break;
                    }
                }
            }
        }
    }

    public void requestImageCaputre(View view){
        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        startActivityForResult(intent, 100);
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
}