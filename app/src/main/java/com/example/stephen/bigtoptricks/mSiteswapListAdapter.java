package com.example.stephen.bigtoptricks;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.ArrayList;

public class mSiteswapListAdapter extends RecyclerView.Adapter<mSiteswapListAdapter.mAdapterViewHolder> {

    //get context
    private final Context mContext;
    //set up the click handler
    final private mAdapterOnClickHandler mClickHandler;
    //create string for output list
    public ArrayList<String> output_list;


    //get stuff on list from Main Activity
    public mSiteswapListAdapter(@NonNull Context context,
                                mAdapterOnClickHandler clickHandler,
                                ArrayList<String> ol) {
        mContext = context;
        mClickHandler = clickHandler;
        output_list = ol;
    }

    //when view holder is created, inflate the views
    @Override
    public mSiteswapListAdapter.mAdapterViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        int layoutId;
        layoutId = R.layout.siteswap_list_item;
        View view = LayoutInflater.from(mContext).inflate(layoutId, parent, false);
        view.setFocusable(true);
        return new mAdapterViewHolder(view);
    }

    //bind data to view holder
    @Override
    public void onBindViewHolder(mSiteswapListAdapter.mAdapterViewHolder holder, int position) {
        holder.textView.setText(output_list.get(position));
    }

    //How many? The size of the output_list.
    @Override
    public int getItemCount() {
        return output_list.size();
    }

    //set up clicks
    public interface mAdapterOnClickHandler {
        void onClick(int adapterPosition);
    }

    //setting up the recycler view, and clicks
    class mAdapterViewHolder extends RecyclerView.ViewHolder implements
            View.OnClickListener {
        //initialize views
        public final TextView textView;

        //super the views so that they can be bound - set click listener too
        mAdapterViewHolder(View view) {
            super(view);
            //image  and text views for tricks
            textView = (TextView) view.findViewById(R.id.siteswap_list_item_textview);
            //set on click listener
            itemView.setOnClickListener(this);
        }

        //when a user taps the list
        @Override
        public void onClick(View view) {
            int adapterPosition = getAdapterPosition();
            mClickHandler.onClick(adapterPosition);
        }
    }
}
