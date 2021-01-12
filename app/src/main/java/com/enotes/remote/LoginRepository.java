package com.enotes.remote;

import android.content.Context;

import com.enotes.ui.login.LoginActivity;
import com.enotes.ui.main.MainActivity;

import de.adorsys.android.securestoragelibrary.SecurePreferences;
import de.adorsys.android.securestoragelibrary.SecureStorageException;

public class LoginRepository {
    public static void login(Context context, String id, String username, String token) throws SecureStorageException {
        LoginRepository.set(context, "id", id);
        LoginRepository.set(context, "username", username);
        LoginRepository.set(context, "token", token);
    }

    public static void logout(Context context) throws SecureStorageException {
        SecurePreferences.clearAllValues(context);
    }

    public static String get(Context context, String key) {
        return SecurePreferences.getStringValue(context, key, null);
    }

    public static void set(Context context, String key, String value) throws SecureStorageException {
        SecurePreferences.setValue(context, key, value);
    }
}
