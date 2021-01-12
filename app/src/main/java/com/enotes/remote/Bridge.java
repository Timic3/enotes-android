package com.enotes.remote;

import java.io.IOException;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.FormBody;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

class HttpError extends RuntimeException {
    public HttpError(String errorMessage) {
        super(errorMessage);
    }
}

public class Bridge {
    static final MediaType JSON = MediaType.get("application/json; charset=utf-8");
    static OkHttpClient client = new OkHttpClient();

    public static String get(String url) {
        Request request = new Request.Builder()
                .url(url)
                .build();
        try (Response response = client.newCall(request).execute()) {
            return response.body().string();
        } catch (IOException e) {
            e.printStackTrace();
        }

        return null;
    }

    /*public static String post(String url, String json) {
        RequestBody body = RequestBody.create(json, JSON);
        Request request = new Request.Builder()
                .url(url)
                .post(body)
                .build();
        try (Response response = client.newCall(request).execute()) {
            return response.body().string();
        } catch (IOException e) {
            e.printStackTrace();
        }

        return null;
    }*/

    public static void register(RequestBody requestBody, Callback callback) {
        Request request = new Request.Builder()
                .url("https://api-enotes.westeurope.cloudapp.azure.com/authentication/register")
                .post(requestBody)
                .addHeader("Content-Type", "application/x-www-form-url-encoded")
                .build();
        Call call = client.newCall(request);
        call.enqueue(callback);
    }

    public static void login(RequestBody requestBody, Callback callback) {
        Request request = new Request.Builder()
                .url("https://api-enotes.westeurope.cloudapp.azure.com/authentication/login")
                .post(requestBody)
                .addHeader("Content-Type", "application/x-www-form-url-encoded")
                .build();
        Call call = client.newCall(request);
        call.enqueue(callback);
    }

    public static void notes(RequestBody requestBody, String token, Callback callback) {
        Request request = new Request.Builder()
                .url("https://api-enotes.westeurope.cloudapp.azure.com/notes/get")
                .post(requestBody)
                .addHeader("Content-Type", "application/x-www-form-url-encoded")
                .addHeader("Authorization", "Bearer " + token)
                .build();
        Call call = client.newCall(request);
        call.enqueue(callback);
    }

    public static void items(RequestBody requestBody, String token, Callback callback) {
        Request request = new Request.Builder()
                .url("https://api-enotes.westeurope.cloudapp.azure.com/notes/gettodo")
                .post(requestBody)
                .addHeader("Content-Type", "application/x-www-form-url-encoded")
                .addHeader("Authorization", "Bearer " + token)
                .build();
        Call call = client.newCall(request);
        call.enqueue(callback);
    }

    public static void addNote(RequestBody requestBody, String token, Callback callback){
        Request request = new Request.Builder()
                .url("https://api-enotes.westeurope.cloudapp.azure.com/notes/create")
                .post(requestBody)
                .addHeader("Content-Type", "application/x-www-form-url-encoded")
                .addHeader("Authorization", "Bearer " + token)
                .build();
        Call call = client.newCall(request);
        call.enqueue(callback);
    }
}
