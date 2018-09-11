package com.example.stephen.bigtoptricks;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.webkit.WebView;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;

import java.util.ArrayList;

public class TrickDiscovery extends AppCompatActivity {

    private String mTrickName;
    private String mTrickDescription;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_trick_discovery);

        ArrayList<String> trick_details = getIntent().getStringArrayListExtra("details");
        Log.d("LOG", "asdf made it    " + trick_details.toString());

        TextView title_text_view = (TextView) findViewById(R.id.discovery_title_text_view);
        title_text_view.setText(trick_details.get(0));

        TextView details_text_view = (TextView) findViewById(R.id.discover_description_text_view);
        details_text_view.setText(trick_details.get(7));

        WebView wv = (WebView) findViewById(R.id.discovery_animation);
        wv.loadUrl(trick_details.get(3));

        mTrickName = trick_details.get(0);
        mTrickDescription = trick_details.get(7);


    }

    public void addTrick(View view){
        Intent toAddTrick = new Intent(this, AddTrick.class);
        toAddTrick.putExtra("trickName", mTrickName);
        toAddTrick.putExtra("trickDesc", mTrickDescription.substring(0,123));
        startActivity(toAddTrick);
    }
}
