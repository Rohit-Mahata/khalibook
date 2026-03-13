package com.rohit.khalibook.session;

import android.content.Context;
import android.content.SharedPreferences;

public class SessionManager {

    private static final String PREF = "session";
    private static final String USER_ID = "user_id";

    private final SharedPreferences prefs;

    public SessionManager(Context context) {
        prefs = context.getSharedPreferences(PREF, Context.MODE_PRIVATE);
    }

    public void login(int userId) {
        prefs.edit().putInt(USER_ID, userId).apply();
    }

    public int getUserId() {
        return prefs.getInt(USER_ID, -1);
    }

    public boolean isLoggedIn() {
        return getUserId() != -1;
    }

    public void logout() {
        prefs.edit().clear().apply();
    }
}
