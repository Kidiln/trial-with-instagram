package com.jcb.instalist.ui;

import android.app.Activity;
import android.content.Context;
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
import com.jcb.instalist.R;
import com.jcb.instalist.cache.ImageFetcher;

import java.io.IOException;
import java.util.ArrayList;

/**
 * Created by jacobkoikkara on 9/21/15.
 */
public class PictureFragment extends Fragment {


    private static PictureFragment pictureFragment;
    private static ImageFetcher mImageFetcher;
    private SwipeRefreshLayout swipeRefreshLayout;
    private RecyclerView recyclerView;
    private LinearLayoutManager mLayoutManager;
    private PictureRecyclerAdapter mAdapter;
    private Context mContext;

    public PictureFragment() {
    }


    //    public static PictureFragment newInstance(String text) {
    public static PictureFragment newInstance(ImageFetcher imgFetcher) {

        if (pictureFragment == null) {
            pictureFragment = new PictureFragment();
        }
//        Bundle b = new Bundle();
//        b.putString("msg", text);
//
//        f.setArguments(b);

        mImageFetcher = imgFetcher;

        return pictureFragment;
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);

        mContext = activity;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.pictures_list, container, false);

        swipeRefreshLayout = (SwipeRefreshLayout) v.findViewById(R.id.swipePicRefreshLayout);
        recyclerView = (RecyclerView) v.findViewById(R.id.itemsPicRecyclerView);

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
        ArrayList<Datum> myDataset;
        try {
            myDataset = (ArrayList<Datum>) InstagramCache.readObject(mContext, InstagramFetch.CACHE_I_KEY);
            mAdapter = new PictureRecyclerAdapter(mContext, myDataset, mImageFetcher);
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

}
