package com.jcb.instaapp;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;

/**
 * Manage access token and user name. Uses shared preferences to store access
 * token and user name.
 */
public class InstagramSession {

    public static final String SHARED = "Instagram_Preferences";
    private static final String API_USERNAME = "username";
    private static final String API_ID = "id";
    private static final String API_NAME = "name";
    public static final String API_ACCESS_TOKEN = "access_token";
    private static final String API_COUNT = "count";
    private static final int INITIAL_COUNT = 5;
    private static final int UPDATE_COUNT = 5;
    private SharedPreferences sharedPref;
    private Editor editor;

    public InstagramSession(Context context) {
        sharedPref = context.getSharedPreferences(SHARED, Context.MODE_PRIVATE);
        editor = sharedPref.edit();
    }

    /**
     * @param accessToken
     * @param id
     * @param username
     * @param name
     */
    public void storeAccessToken(String accessToken, String id, String username, String name) {
        editor.putString(API_ID, id);
        editor.putString(API_NAME, name);
        editor.putString(API_ACCESS_TOKEN, accessToken);
        editor.putString(API_USERNAME, username);
        editor.putInt(API_COUNT, INITIAL_COUNT);
        editor.commit();
    }

    public void storeAccessToken(String accessToken) {
        editor.putString(API_ACCESS_TOKEN, accessToken);
        editor.commit();
    }

    /**
     * Reset access token and user name
     */
    public void resetAccessToken() {
        editor.putString(API_ID, null);
        editor.putString(API_NAME, null);
        editor.putString(API_ACCESS_TOKEN, null);
        editor.putString(API_USERNAME, null);
        editor.putInt(API_COUNT, INITIAL_COUNT);
        editor.commit();
    }

    /**
     * Get user name
     *
     * @return User name
     */
    public String getUsername() {
        return sharedPref.getString(API_USERNAME, null);
    }

    /**
     * @return
     */
    public String getId() {
        return sharedPref.getString(API_ID, null);
    }

    /**
     * @return
     */
    public String getName() {
        return sharedPref.getString(API_NAME, null);
    }

    /**
     * Get access token
     *
     * @return Access token
     */
    public String getAccessToken() {
        return sharedPref.getString(API_ACCESS_TOKEN, null);
    }

    /**
     * Get Media count
     *
     * @return int value of count
     */
    public int getMediaCount() {
        return sharedPref.getInt(API_COUNT, INITIAL_COUNT);
    }

    /**
     * Get Updated media count
     *
     * @return int value of count
     */
    public int getUpdateMediaCount() {

        editor.putInt(API_COUNT, getMediaCount() + UPDATE_COUNT);
        editor.commit();

        return sharedPref.getInt(API_COUNT, INITIAL_COUNT);

    }

}