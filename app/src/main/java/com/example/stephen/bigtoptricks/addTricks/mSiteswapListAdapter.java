package com.example.stephen.bigtoptricks.addTricks;

import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.stephen.bigtoptricks.R;

import java.util.ArrayList;

public class mSiteswapListAdapter extends RecyclerView.Adapter<mSiteswapListAdapter.mAdapterViewHolder> {

    //get context
    private final Context mContext;
    //set up the click handler
    final private mAdapterOnClickHandler mClickHandler;
    //create string for output list
    public ArrayList<String> output_list;
    public ArrayList<String> capacity_list;
    public ArrayList<String> difficulties_list;
    public ArrayList<String> sources_list;


    //get stuff on list from Main Activity
    public mSiteswapListAdapter(@NonNull Context context,
                                mAdapterOnClickHandler clickHandler,
                                ArrayList<String> ol,
                                ArrayList<String> capacity,
                                ArrayList<String> difficulties,
                                ArrayList<String> sources) {
        mContext = context;
        mClickHandler = clickHandler;
        output_list = ol;
        capacity_list = capacity;
        sources_list = sources;
        difficulties_list = difficulties;

    }

    //when view holder is created, inflate the views
    @Override
    public mSiteswapListAdapter.mAdapterViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        int layoutId = R.layout.siteswap_list_item_custom;;
        if (viewType==1) layoutId = R.layout.siteswap_list_item;
        View view = LayoutInflater.from(mContext).inflate(layoutId, parent, false);
        view.setFocusable(true);
        return new mAdapterViewHolder(view);
    }

    //override getItemViewType (from S11.02)
    @Override
    public int getItemViewType(int position) {
        if (position == 0) {
            return 0;
        } else {
            return 1;
        }
    }

    //bind data to view holder
    @Override
    public void onBindViewHolder(mSiteswapListAdapter.mAdapterViewHolder holder, int position) {
        holder.textView.setText(output_list.get(position));
        String capacityStr = "";
        try{
            for(int i = 0; i < Integer.parseInt(capacity_list.get(position)); i++){
                capacityStr += "* ";
            }
        }catch (Exception e){ capacityStr = "";}
        String difficultyStr = "";
        try{
            for(int i = 0; i < Integer.parseInt(difficulties_list.get(position)); i++){
                difficultyStr += "+ ";
            }
        }catch (Exception e){ difficultyStr = "";}
        holder.capacityTextView.setText(capacityStr);

        holder.difficultyTextView.setText(difficultyStr);
        holder.sourcesTextView.setText(sources_list.get(position));
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
        public final TextView capacityTextView;
        public final TextView difficultyTextView;
        public final TextView sourcesTextView;

        //super the views so that they can be bound - set click listener too
        mAdapterViewHolder(View view) {
            super(view);
            //image  and text views for tricks
            textView = (TextView) view.findViewById(R.id.siteswap_list_item_textview);
            capacityTextView = (TextView) view.findViewById(R.id.siteswap_list_item_capacity);
            difficultyTextView = (TextView) view.findViewById(R.id.siteswap_list_item_difficulty);
            sourcesTextView = (TextView) view.findViewById(R.id.siteswap_list_item_source);
            //set on click listener
            itemView.setOnClickListener(this);
        }

        //when a user taps the list
        @Override
        public void onClick(View view) {
            int adapterPosition = getAdapterPosition();
            // Send the user to the appropriate trick discovery activity
            mClickHandler.onClick(adapterPosition);
        }
    }

    // TODO (6) create swap cursor method to reset the data
    void swapCursor(ArrayList<String> data,ArrayList<String> capacities,ArrayList<String> difficulties,ArrayList<String> sources) {
        // Move through the cursor and extract the movie poster urls.
        output_list = data;
        sources_list = sources;
        capacity_list = capacities;
        difficulties_list = difficulties;
        output_list.add(0, "Add a Custom Tricks");
        sources_list.add(0, "Create trick yourself.");
        capacity_list.add(0, "User Defined");
        difficulties_list.add(0, "Any");

        //Log.d("LOG", "asdf prs: " + mTrickNames.toString());
        notifyDataSetChanged();
    }
}
