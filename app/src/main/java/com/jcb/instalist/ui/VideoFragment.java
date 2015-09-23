package com.jcb.instalist.ui;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.jcb.instaapp.InstagramFetch;
import com.jcb.instaapp.InstagramSession;
import com.jcb.instaapp.model.Datum;
import com.jcb.instaapp.network.InstagramCache;
import com.jcb.instalist.ApplicationData;
import com.jcb.instalist.InstagramUtils;
import com.jcb.instalist.R;
import com.jcb.instalist.VideoPlayerActivity;

import java.io.IOException;
import java.util.ArrayList;

/**
 * Fragment for showing video list
 * Created by jacobkoikkara on 9/21/15.
 */
public class VideoFragment extends Fragment {

    private static VideoFragment videoFragment;
    private ArrayList<Datum> myDataset;
    private SwipeRefreshLayout swipeRefreshLayout;
    private RecyclerView recyclerView;
    private LinearLayoutManager mLayoutManager;
    private VideoRecyclerAdapter mAdapter;
    private Context mContext;

    public VideoFragment() {

    }

    public static VideoFragment newInstance() {

        if (videoFragment == null) {
            videoFragment = new VideoFragment();
        }

        return videoFragment;
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);

        mContext = activity;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.videos_list, container, false);

        swipeRefreshLayout = (SwipeRefreshLayout) v.findViewById(R.id.swipeVidRefreshLayout);
        recyclerView = (RecyclerView) v.findViewById(R.id.itemsVidRecyclerView);

        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                doOnRefresh();
            }
        });

        // use this setting to improve performance if you know that changes
        // in content do not change the layout size of the RecyclerView
        recyclerView.setHasFixedSize(true);

        // use a linear layout manager
        mLayoutManager = new LinearLayoutManager(mContext);
        recyclerView.setLayoutManager(mLayoutManager);

        // specify an adapter (see also next example)

        try {
            myDataset = (ArrayList<Datum>) InstagramCache.readObject(mContext, InstagramFetch.CACHE_V_KEY);
            // specify an adapter (see also next example)

            mAdapter = new VideoRecyclerAdapter(mContext, myDataset);
            mAdapter.setClickListener(new VideoOnClickListener());
            recyclerView.setAdapter(mAdapter);


        } catch (IOException e) {
            e.printStackTrace();

        } catch (ClassNotFoundException e) {

            e.printStackTrace();
        }
        return v;
    }


    /**
     * Do on swipe refresh action. broadcast is sent if network is available.
     */
    private void doOnRefresh() {


        if (InstagramUtils.isNetworkAvailable(mContext)) {

            LocalBroadcastManager.getInstance(mContext).sendBroadcast(new Intent(ApplicationData.INTENT_REFRESH).putExtra(ApplicationData.INTENT_ISVIDEO, true));

        } else if(getTokenValue() == null) {
            InstagramUtils.showToast(mContext, "Token Invalid. Kindly Resync with Instagram");

            swipeRefreshLayout.setRefreshing(false);
        } else {
            try {

                myDataset = (ArrayList<Datum>) InstagramCache.readObject(mContext, InstagramFetch.CACHE_V_KEY);

                mAdapter.notifyDataSetChanged();

                swipeRefreshLayout.setRefreshing(false);

                InstagramUtils.showToast(mContext, "Refresh complete");
            } catch (IOException e) {
                e.printStackTrace();

            } catch (ClassNotFoundException e) {

                e.printStackTrace();
            }
        }


    }

    /**
     * Calling video activity on Video item click
     * @param position
     */
    private void doOnVideoItemClick(int position) {
        Intent launchIntent = new Intent(mContext, VideoPlayerActivity.class);
        launchIntent.putExtra(ApplicationData.INTENT_URL, myDataset.get(position).getVideos().getStandardResolution().getUrl());
        startActivity(launchIntent);
    }

    /**
     * Click listener for Recycler view.
     */
    public class VideoOnClickListener implements View.OnClickListener {
        @Override
        public void onClick(View v) {
            int itemPosition = recyclerView.getChildPosition(v);
            InstagramUtils.showLog("Clicked and Position is ", String.valueOf(itemPosition));

            doOnVideoItemClick(itemPosition);
        }
    }

    /**
     * Retreive token value from Shared Preference
     *
     * @return null if no token is saved.
     */
    private String getTokenValue() {
        SharedPreferences pref = mContext.getSharedPreferences(InstagramSession.SHARED, Context.MODE_PRIVATE);
        return pref.getString(InstagramSession.API_ACCESS_TOKEN, null);
    }

}
