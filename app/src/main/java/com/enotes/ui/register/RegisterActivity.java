package com.enotes.ui.register;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;

import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Patterns;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.enotes.R;
import com.enotes.remote.Bridge;
import com.enotes.utils.Utils;
import com.google.android.gms.safetynet.SafetyNet;
import com.google.android.gms.safetynet.SafetyNetApi;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;

import org.jetbrains.annotations.NotNull;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.FormBody;
import okhttp3.RequestBody;
import okhttp3.Response;

public class RegisterActivity extends AppCompatActivity {
    private String reCaptchaToken = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        final EditText registerUsername = findViewById(R.id.registerUsername);
        final EditText registerEmail = findViewById(R.id.registerEmail);
        final EditText registerPassword = findViewById(R.id.registerPassword);
        final EditText registerRepeatPassword = findViewById(R.id.registerRepeatPassword);
        final Button confirmRegister = findViewById(R.id.confirmRegister);
        final Button back = findViewById(R.id.back);

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
                if (registerUsername.length() < 3 || registerUsername.length() > 20) {
                    registerUsername.setError("Username length can only be between 3 and 20!");
                    errors = true;
                }
                if (registerEmail.length() < 5 || registerEmail.length() > 256) {
                    registerEmail.setError("Email length can only be between 5 and 256!");
                    errors = true;
                }
                if (!Patterns.EMAIL_ADDRESS.matcher(registerEmail.getText()).matches()) {
                    registerEmail.setError("E-mail must be valid!");
                    errors = true;
                }
                if (!registerPassword.getText().toString().equals(registerRepeatPassword.getText().toString())) {
                    registerPassword.setError("Passwords must match!");
                    registerRepeatPassword.setError("Passwords must match!");
                    errors = true;
                } else {
                    registerPassword.setError(null);
                    registerRepeatPassword.setError(null);
                }
                if (registerPassword.length() < 8 || registerPassword.length() > 125) {
                    registerPassword.setError("Password length can only be between 8 and 125!");
                    errors = true;
                }

                confirmRegister.setEnabled(!errors);
            }
        };
        registerUsername.addTextChangedListener(afterTextChangedListener);
        registerEmail.addTextChangedListener(afterTextChangedListener);
        registerPassword.addTextChangedListener(afterTextChangedListener);
        registerRepeatPassword.addTextChangedListener(afterTextChangedListener);
        /*passwordEditText.setOnEditorActionListener(new TextView.OnEditorActionListener() {

            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if (actionId == EditorInfo.IME_ACTION_DONE) {
                    loginViewModel.login(usernameEditText.getText().toString(),
                            passwordEditText.getText().toString());
                }
                return false;
            }
        });*/
        confirmRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SafetyNet.getClient(RegisterActivity.this).verifyWithRecaptcha("6LcNvSkaAAAAAEmCtmnCcy8AghmvguCM-V0F9nC3")
                        .addOnSuccessListener(RegisterActivity.this, new OnSuccessListener<SafetyNetApi.RecaptchaTokenResponse>() {
                            @Override
                            public void onSuccess(SafetyNetApi.RecaptchaTokenResponse recaptchaTokenResponse) {
                                if (!recaptchaTokenResponse.getTokenResult().isEmpty()) {
                                    reCaptchaToken = recaptchaTokenResponse.getTokenResult();
                                    System.out.println(reCaptchaToken);
                                    RequestBody requestBody = new FormBody.Builder()
                                            .addEncoded("username", registerUsername.getText().toString())
                                            .addEncoded("password", registerPassword.getText().toString())
                                            .addEncoded("email", registerEmail.getText().toString())
                                            .addEncoded("mobile", "true")
                                            .addEncoded("captcha", reCaptchaToken)
                                            .build();
                                    Bridge.register(requestBody, new Callback() {
                                        @Override
                                        public void onFailure(@NotNull Call call, @NotNull IOException e) {
                                            Utils.toastUi(RegisterActivity.this, "Oops, something went wrong!");
                                        }

                                        @Override
                                        public void onResponse(@NotNull Call call, @NotNull Response response) throws IOException {
                                            if (response.isSuccessful()) {
                                                try {
                                                    JSONObject result = new JSONObject(response.body().string());
                                                    System.out.println(result.getBoolean("success"));
                                                    System.out.println(result.getString("message"));
                                                    if (result.getBoolean("success")) {
                                                        Utils.toastUi(RegisterActivity.this, result.getString("message"));
                                                        finish();
                                                    } else {
                                                        Utils.toastUi(RegisterActivity.this, result.getString("message"));
                                                    }
                                                } catch (JSONException e) {
                                                    Utils.toastUi(RegisterActivity.this, "Something went wrong, please try again!");
                                                }
                                            } else {
                                                Utils.toastUi(RegisterActivity.this, "Something went wrong, please try again!");
                                            }
                                        }
                                    });
                                } else {
                                    Utils.toastUi(RegisterActivity.this, "Captcha verification failed!");
                                }
                            }
                        })
                        .addOnFailureListener(RegisterActivity.this, new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception e) {
                                Utils.toastUi(RegisterActivity.this, "Oops, something went wrong with reCAPTCHA!");
                            }
                        });
            }
        });
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }
}