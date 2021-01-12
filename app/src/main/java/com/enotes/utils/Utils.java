package com.enotes.utils;

import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.widget.Toast;

import com.enotes.ui.register.RegisterActivity;

import java.io.ByteArrayOutputStream;

public class Utils {
    public static byte[] bitmapToByteArray(Bitmap bitmap) {
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.PNG, 0, outputStream);
        return outputStream.toByteArray();
    }

    public static void toast(Context context, String title) {
        Toast.makeText(context, title, Toast.LENGTH_SHORT).show();
    }

    public static void toastUi(Context context, String title) {
        ((Activity) context).runOnUiThread(new Runnable() {
            public void run() {
                Utils.toast(context, title);
            }
        });
    }
}
