package com.enotes.ui.login;

import android.app.Activity;

import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.annotation.StringRes;
import androidx.appcompat.app.AppCompatActivity;

import android.text.Editable;
import android.text.TextWatcher;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.enotes.R;
import com.enotes.remote.Bridge;
import com.enotes.remote.LoginRepository;
import com.enotes.ui.main.MainActivity;
import com.enotes.ui.register.RegisterActivity;
import com.enotes.utils.Utils;

import org.jetbrains.annotations.NotNull;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;

import de.adorsys.android.securestoragelibrary.SecurePreferences;
import de.adorsys.android.securestoragelibrary.SecureStorageException;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.FormBody;
import okhttp3.RequestBody;
import okhttp3.Response;

public class LoginActivity extends AppCompatActivity {

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        System.out.println(LoginRepository.get(LoginActivity.this, "id"));
        System.out.println(LoginRepository.get(LoginActivity.this, "username"));
        System.out.println(LoginRepository.get(LoginActivity.this, "token"));

        if (SecurePreferences.contains(LoginActivity.this, "token")) {
            Intent intent = new Intent(LoginActivity.this, MainActivity.class);
            startActivity(intent);
            return;
        }

        setContentView(R.layout.activity_login);

        final EditText usernameEditText = findViewById(R.id.username);
        final EditText passwordEditText = findViewById(R.id.password);
        final Button loginButton = findViewById(R.id.login);
        final ProgressBar loadingProgressBar = findViewById(R.id.loading);
        final Button registerButton = findViewById(R.id.register);

        TextWatcher afterTextChangedListener = new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                // ignore
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                // ignore
            }

            @Override
            public void afterTextChanged(Editable s) {
                boolean errors = false;
                if (usernameEditText.length() < 3 || usernameEditText.length() > 20) {
                    usernameEditText.setError("Username length can only be between 3 and 20!");
                    errors = true;
                }
                if (passwordEditText.length() < 8 || passwordEditText.length() > 125) {
                    passwordEditText.setError("Password length can only be between 8 and 125!");
                    errors = true;
                }

                loginButton.setEnabled(!errors);
            }
        };
        usernameEditText.addTextChangedListener(afterTextChangedListener);
        passwordEditText.addTextChangedListener(afterTextChangedListener);

        loginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // loadingProgressBar.setVisibility(View.VISIBLE);
                RequestBody requestBody = new FormBody.Builder()
                        .addEncoded("username", usernameEditText.getText().toString())
                        .addEncoded("password", passwordEditText.getText().toString())
                        .build();
                Bridge.login(requestBody, new Callback() {
                    @Override
                    public void onFailure(@NotNull Call call, @NotNull IOException e) {
                        Utils.toastUi(LoginActivity.this, "Oops, something went wrong!");
                    }

                    @Override
                    public void onResponse(@NotNull Call call, @NotNull Response response) throws IOException {
                        if (response.isSuccessful()) {
                            try {
                                JSONObject result = new JSONObject(response.body().string());
                                System.out.println(result.getBoolean("success"));
                                System.out.println(result.getString("message"));
                                if (result.getBoolean("success")) {
                                    Utils.toastUi(LoginActivity.this, result.getString("message"));
                                    System.out.println(result.getJSONObject("user"));
                                    LoginRepository.login(LoginActivity.this, result.getJSONObject("user").getString("id"), result.getJSONObject("user").getString("username"), result.getJSONObject("user").getString("token"));
                                    if (SecurePreferences.contains(LoginActivity.this, "token")) {
                                        Intent intent = new Intent(LoginActivity.this, MainActivity.class);
                                        startActivity(intent);
                                    }
                                } else {
                                    Utils.toastUi(LoginActivity.this, result.getString("message"));
                                }
                            } catch (JSONException | SecureStorageException e) {
                                Utils.toastUi(LoginActivity.this, "Something went wrong, please try again!");
                            }
                        } else {
                            Utils.toastUi(LoginActivity.this, "Something went wrong, please try again!");
                        }
                    }
                });
            }
        });

        registerButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent registerIntent = new Intent(LoginActivity.this, RegisterActivity.class);
                LoginActivity.this.startActivity(registerIntent);
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        System.out.println(resultCode);
    }
}