package com.jcb.instalist.ui;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.jcb.instaapp.model.Datum;
import com.jcb.instalist.ApplicationData;
import com.jcb.instalist.R;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

/**
 *
 * Recycleview adapter used to populate the Video recyclerview
 * Created by jacobkoikkara on 9/21/15.
 */
public class VideoRecyclerAdapter extends RecyclerView.Adapter<VideoRecyclerAdapter.ViewHolder> {

    private ArrayList<Datum> mDataset;
    private VideoFragment.VideoOnClickListener mListener;
    private Context mContext;

    // Provide a suitable constructor (depends on the kind of dataset)
    public VideoRecyclerAdapter(Context context, ArrayList<Datum> myDataset) {
        mContext = context;
        mDataset = myDataset;
    }

    public void setClickListener(VideoFragment.VideoOnClickListener listener) {
        mListener = listener;
    }

    // Create new views (invoked by the layout manager)
    @Override
    public VideoRecyclerAdapter.ViewHolder onCreateViewHolder(ViewGroup parent,
                                                              int viewType) {
        // create a new view
        View v = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.list_item, parent, false);
        // set the view's size, margins, paddings and layout parameters
//        ...

        v.setOnClickListener(mListener);
        ViewHolder vh = new ViewHolder(v);
        return vh;
    }

    @Override
    public void onBindViewHolder(VideoRecyclerAdapter.ViewHolder holder, int position) {
        // - get element from your dataset at this position
        // - replace the contents of the view with that element
        holder.mTextView.setText(mDataset.get(position).getCaption().getText());

        Picasso.with(mContext).setIndicatorsEnabled(true);
        Picasso.with(mContext).load(mDataset.get(position).getImages().getLowResolution().getUrl()).resize(ApplicationData.widthScreenInPixels, ApplicationData.widthScreenInPixels).into(holder.mImageView);

    }


    // Return the size of your dataset (invoked by the layout manager)
    @Override
    public int getItemCount() {
        return mDataset.size();
    }

    // Provide a reference to the views for each data item
    // Complex data items may need more than one view per item, and
    // you provide access to all the views for a data item in a view holder
    public static class ViewHolder extends RecyclerView.ViewHolder {
        // each data item is just a string in this case
        public TextView mTextView;
        public ImageView mImageView;

        public ViewHolder(View view) {
            super(view);
            mTextView = (TextView) view.findViewById(R.id.txtTitle);
            mImageView = (ImageView) view.findViewById(R.id.imgPic);
        }

        public static interface IMyViewHolderClicks {
            public void onViewItemClick();
        }
    }
}
