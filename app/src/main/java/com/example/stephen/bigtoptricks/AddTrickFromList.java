package com.example.stephen.bigtoptricks;

import android.content.ContentValues;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.res.Resources;
import android.net.Uri;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.InputType;
import android.util.Log;
import android.widget.EditText;
import android.widget.Toast;

import com.example.stephen.bigtoptricks.data.Contract;

import java.util.ArrayList;

public class AddTrickFromList extends AppCompatActivity implements mSiteswapListAdapter.mAdapterOnClickHandler{

    //initialize variables for resources from strings.xml
    private ArrayList<String> pattern_list = new ArrayList<>();
    private String[] patterns;
    //initialize an adapter and recyclerView
    private mSiteswapListAdapter mSiteswapListAdapter;
    private RecyclerView mList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_trick_from_list);

        //get resources from strings.xml
        Resources res = getResources();
        patterns = res.getStringArray(R.array.patterns);
        //Log.d("LOG", "asdf patterns length: " + patterns.length);
        for (int i = 0; i < patterns.length; i++){
            //Log.d("LOG", "asdf " + patterns[i]);
            pattern_list.add(patterns[i]);
        }
        //Log.d("LOG", "asdf patterns length: " + pattern_list.size());

        //code for recycler view
        mList = (RecyclerView) findViewById(R.id.recycler_view_siteswap_list);
        LinearLayoutManager layoutManager = new LinearLayoutManager(this,
                LinearLayoutManager.VERTICAL, false);
        mList.setLayoutManager(layoutManager);
        mList.setHasFixedSize(true);
        mSiteswapListAdapter = new mSiteswapListAdapter(this, this, pattern_list);
        mList.setAdapter(mSiteswapListAdapter);
    }

    //@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@ START ONCLICK METHOD @@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@
    @Override
    public void onClick(final int position) {
        Intent toAddTrick = new Intent(this, AddTrick.class);
        toAddTrick.putExtra("trickName", pattern_list.get(position));
        toAddTrick.putExtra("trickDesc", "There is no description for this trick. This trick was part of the siteswap list.");
        startActivity(toAddTrick);
    }
    //@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@ END ONCLICK METHOD @@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@
}
