package com.vku.lethanhan.utcshop.data_local;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.util.Log;

public class AccessTokenManager {
    public static SharedPreferences sharedPreferences;
    public static SharedPreferences.Editor editor;
    public static Context context;
    private static final String SHARE_PREFERENCES = "SHARE_PREFERENCES";

    public AccessTokenManager(Context context) {
        this.context = context.getApplicationContext();
        sharedPreferences = context.getSharedPreferences(SHARE_PREFERENCES, Context.MODE_PRIVATE);
        editor = sharedPreferences.edit();
    }

    public static void setAccess_token(String access_token) {
        editor.putString("access_token", "Bearer " + access_token);
        editor.commit();
    }

    public static String getAccess_token() {
        return sharedPreferences.getString("access_token", "");
    }

    public static void delAccess_token() {
        editor.remove("access_token").commit();
    }
}
