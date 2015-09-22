package com.jcb.instalist.ui;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.jcb.instaapp.InstagramFetch;
import com.jcb.instaapp.model.Datum;
import com.jcb.instaapp.network.InstagramCache;
import com.jcb.instalist.InstagramUtils;
import com.jcb.instalist.R;
import com.jcb.instalist.VideoPlayerActivity;
import com.jcb.instalist.cache.ImageFetcher;

import java.io.IOException;
import java.util.ArrayList;

/**
 * Created by jacobkoikkara on 9/21/15.
 */
public class VideoFragment extends Fragment {

    private static VideoFragment videoFragment;
    private static ImageFetcher mImageFetcher;
    ArrayList<Datum> myDataset;
    private SwipeRefreshLayout swipeRefreshLayout;
    private RecyclerView recyclerView;
    private LinearLayoutManager mLayoutManager;
    private VideoRecyclerAdapter mAdapter;
    private Context mContext;

    public VideoFragment() {

    }

    public static VideoFragment newInstance(ImageFetcher imgFetcher) {

        if (videoFragment == null) {
            videoFragment = new VideoFragment();
        }

        mImageFetcher = imgFetcher;

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

        swipeRefreshLayout.setEnabled(false);

        // specify an adapter (see also next example)

        try {
            myDataset = (ArrayList<Datum>) InstagramCache.readObject(mContext, InstagramFetch.CACHE_V_KEY);
            // specify an adapter (see also next example)

            mAdapter = new VideoRecyclerAdapter(myDataset, mImageFetcher);
            mAdapter.setClickListener(new VideoOnClickListener());
            recyclerView.setAdapter(mAdapter);


        } catch (IOException e) {
            e.printStackTrace();

        } catch (ClassNotFoundException e) {

            e.printStackTrace();
        }
        return v;
    }

    private void doOnRefresh() {

    }

    private void doOnVideoItemClick(int position) {
        Intent launchIntent = new Intent(mContext, VideoPlayerActivity.class);
        launchIntent.putExtra("url", myDataset.get(position).getVideos().getStandardResolution().getUrl());
        startActivity(launchIntent);
    }

    public class VideoOnClickListener implements View.OnClickListener {
        @Override
        public void onClick(View v) {
            int itemPosition = recyclerView.getChildPosition(v);
            InstagramUtils.showLog("Clicked and Position is ", String.valueOf(itemPosition));

            doOnVideoItemClick(itemPosition);
        }
    }

}
