package com.jcb.instalist;

import android.util.Log;

public class ApplicationData {
//	public static final String CLIENT_ID = "379d744556c743c090c8a2014779f59f";
//	public static final String CLIENT_SECRET = "fd6ec75e44054da1a5088ad2d72f2253";
//	public static final String CALLBACK_URL = "instagram://connect";

    public static final String CLIENT_ID = "f46508f19ba943ae99a4f5415121ae7e";
    public static final String CLIENT_SECRET = "d2ab42875cfb4c4a8b793115d48ea813";
    public static final String CALLBACK_URL = "listingram://connect";
//    curl -F 'client_id=[506447d12e154f0b8ba372d18166efc3]' -F 'client_secret=[89af6132d60c4a8587ee204756514fec]' -F 'grant_type=authorization_code' -F 'redirect_uri=[https://instagram.com]' -F 'code=[ec29a3b289924dc28e93a04379acf41d]' https://api.instagram.com/oauth/access_token


    public static final String TAG = "InstaList";


    public static void printLog(String message) {
        Log.d(TAG, message);
    }

//    CLIENT ID	f46508f19ba943ae99a4f5415121ae7e
//    CLIENT SECRET	d2ab42875cfb4c4a8b793115d48ea813
//    WEBSITE URL	https://trylistingram.com
//    REDIRECT URI	listingram://connect
//    SUPPORT EMAIL	jacob.koikkara@gmail.com


}
