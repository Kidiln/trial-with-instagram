package com.jcb.instaapp;

import android.content.Context;
import android.os.Handler;
import android.os.Message;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.jcb.instaapp.network.InstagramCache;
import com.jcb.instaapp.network.InstagramDeserializer;
import com.jcb.instaapp.model.Datum;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by jacobkoikkara on 9/20/15.
 */
public class InstagramFetch {


    private static final String FETCH_URL = "https://api.instagram.com/v1/users/self/media/recent?access_token=";
//    private static final String FETCH_VIDEO_URL = "https://api.instagram.com/v1/users/self/media/recent?type=video&access_token=";
//    private static final String FETCH_PICTURE_URL = "https://api.instagram.com/v1/users/self/media/recent?type=image&access_token=";
    public static final String CACHE_I_KEY = "InstagramCacheImage";
    public static final String CACHE_V_KEY = "InstagramCacheVideo";

    private static final String TAG_VIDEO = "video";
    private static int WHAT_ERROR = 1;
    private static int WHAT_FETCH_INFO = 2;


    private Context mContext;
    private InstagramSession mSession;
    private InstagramFetchListener mListener;

    private Handler mHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            if (msg.what == WHAT_ERROR) {

                mListener.onFail("Failed to fetch Instagram details");
            } else {

                mListener.onSuccess();
            }
        }
    };

    public InstagramFetch(Context context) {

        mContext = context;

        mSession = new InstagramSession(mContext);
    }

    public void setListener(InstagramFetchListener listener) {

        mListener = listener;
    }


    public void fetchFromInstagram() {

        new Thread(new Runnable() {
            @Override
            public void run() {

                try {

                    URL example = new URL(FETCH_URL
                            + mSession.getAccessToken());

                    URLConnection tc = example.openConnection();
                    BufferedReader in = new BufferedReader(new InputStreamReader(
                            tc.getInputStream()));

                    String line;
                    while ((line = in.readLine()) != null) {
                        JSONObject ob = new JSONObject(line);

                        GsonBuilder gsonBuilder = new GsonBuilder();
                        gsonBuilder.registerTypeAdapter(Datum[].class, new InstagramDeserializer());
                        Gson gson = gsonBuilder.create();
                        Datum[] users = gson.fromJson(String.valueOf(ob), Datum[].class);

                        List<Datum> picItem = new ArrayList<Datum>();
                        List<Datum> vidItem = new ArrayList<Datum>();

                        for(Datum temp : users){
                            if(temp.getType().equalsIgnoreCase(TAG_VIDEO)) {

                                vidItem.add(temp);
                            } else {

                                picItem.add(temp);
                            }
                        }
                        InstagramCache.writeObject(mContext, CACHE_I_KEY, picItem);
                        InstagramCache.writeObject(mContext, CACHE_V_KEY, vidItem);

                        mHandler.sendMessage(mHandler.obtainMessage(WHAT_FETCH_INFO, 1, 0));


                    }
                } catch (MalformedURLException e) {
                    mHandler.sendMessage(mHandler.obtainMessage(WHAT_ERROR, 1, 0));

                    e.printStackTrace();
                } catch (IOException e) {
                    mHandler.sendMessage(mHandler.obtainMessage(WHAT_ERROR, 2, 0));

                    e.printStackTrace();
                } catch (JSONException e) {
                    mHandler.sendMessage(mHandler.obtainMessage(WHAT_ERROR, 3, 0));

                    e.printStackTrace();
                }

            }
        }).start();

    }

    public interface InstagramFetchListener {
        public abstract void onSuccess();

        public abstract void onFail(String error);
    }
}
