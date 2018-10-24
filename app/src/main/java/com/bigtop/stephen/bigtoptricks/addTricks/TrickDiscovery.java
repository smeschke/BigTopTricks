package com.bigtop.stephen.bigtoptricks.addTricks;

import android.content.Intent;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.text.Html;
import android.util.Log;
import android.view.View;
import android.widget.TextView;
import android.widget.VideoView;

import com.bigtop.stephen.bigtoptricks.R;
import com.bigtop.stephen.bigtoptricks.Trick;

import static com.bigtop.stephen.bigtoptricks.Training.ARG_HIDE_ADD_BUTTON;
import static com.bigtop.stephen.bigtoptricks.Training.ARG_TRICK_OBJECT;

public class TrickDiscovery extends AppCompatActivity {

    private String mTutorial1;
    private String mTutorial2;
    private String mTutorial3;
    private String mTutorial4;
    private String mSource;
    private Trick mTrick;
    private VideoView mAnimationVideoView;
    private String mAnimation;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_discovery);

        // Get the trick that the user clicked on in the Library of Trick
        mTrick = (getIntent().getExtras()).getParcelable(ARG_TRICK_OBJECT);
        String mName = (mTrick).getName();
        String mSiteswap = mTrick.getSiteswap();
        mSource = mTrick.getSource();
        mAnimation = mTrick.getAnimation();
        String tutorials = mTrick.getTutorial();
        String mDifficulty = mTrick.getDifficulty();
        String mCapacity = mTrick.getCapacity();
        String mDescription = mTrick.getDescription();

        // Parse the list of tutorials in to a list
        String[] tutorials_list = tutorials.split(",,,");
        if (tutorials_list.length > 0) {

            mTutorial1 = tutorials_list[0];
            Log.d("LOG", "asdf tutorial: " + mTutorial1);
            if (mTutorial1.length() > 3) {
                TextView tutorial1 = findViewById(R.id.discovery_tutorial_text_view1);
                tutorial1.setVisibility(View.VISIBLE);
            }
        }
        if (tutorials_list.length > 1) {
            mTutorial2 = tutorials_list[1];
            TextView tutorial2 = findViewById(R.id.discovery_tutorial_text_view2);
            tutorial2.setVisibility(View.VISIBLE);
            //tutorial2.setText(mTutorial2);
        }
        if (tutorials_list.length > 2) {
            mTutorial3 = tutorials_list[2];
            TextView tutorial3 = findViewById(R.id.discovery_tutorial_text_view3);
            tutorial3.setVisibility(View.VISIBLE);
        }
        if (tutorials_list.length > 3) {
            mTutorial4 = tutorials_list[3];
            TextView tutorial4 = findViewById(R.id.discovery_tutorial_text_view4);
            tutorial4.setVisibility(View.VISIBLE);
        }

        // Set text views and web view
        TextView title_text_view = findViewById(R.id.discovery_title_text_view);
        title_text_view.setText(mName);
        TextView details_text_view = findViewById(R.id.discover_description_text_view);
        details_text_view.setText(mDescription);
        //#WebView wv = findViewById(R.id.discovery_animation);
        //#wv.loadUrl(mAnimation);
        TextView siteswap = findViewById(R.id.discovery_siteswap_text_view);
        siteswap.setText(getString(R.string.siteswap) + " " + mSiteswap);
        TextView capacity = findViewById(R.id.discovery_capacity_text_view);
        capacity.setText(getString(R.string.capacity) + " " + mCapacity);

        TextView difficulty = findViewById(R.id.discovery_difficulty_text_view);
        difficulty.setText(getString(R.string.difficulty) + " " + mDifficulty);

        // Should the add button be hidden?
        if (getIntent().hasExtra(ARG_HIDE_ADD_BUTTON)) {
            FloatingActionButton floatingActionButton = findViewById(R.id.floatingActionButton);
            floatingActionButton.setVisibility(View.INVISIBLE);
        }

        mAnimationVideoView = (VideoView) findViewById(R.id.discovery_animation);

        // Parse the correct name of the .gif that is stored in /res/raw/
        mAnimation = mAnimation.substring(mAnimation.lastIndexOf("/") + 1);
        mAnimation = mAnimation.substring(0, mAnimation.length() - 4);
        mAnimation = mAnimation.toLowerCase();
        mAnimation = mAnimation.replace("'", "");
        mAnimation = "animation" + mAnimation;

        Log.d("LOG", "asdf resource id: " + mAnimation);

        Uri uri = Uri.parse("android.resource://" + getPackageName() + "/raw/" + mAnimation);
        mAnimationVideoView.setVideoURI(uri);
        mAnimationVideoView.start();
        mAnimationVideoView.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {
            @Override
            public void onPrepared(MediaPlayer mediaPlayer) {
                mediaPlayer.setLooping(true);
            }
        });
    }

    // Restart the video if the user has come back from the 'add trick activivity'
    @Override
    public void onResume() {
        super.onResume();
        Uri uri = Uri.parse("android.resource://" + getPackageName() + "/raw/" + mAnimation);
        mAnimationVideoView.setVideoURI(uri);
        mAnimationVideoView.start();
        mAnimationVideoView.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {
            @Override
            public void onPrepared(MediaPlayer mediaPlayer) {
                mediaPlayer.setLooping(true);
            }
        });
    }

    public void addTrick(View view) {
        Intent toAddTrick = new Intent(this, AddTrick.class);
        toAddTrick.putExtra(ARG_TRICK_OBJECT, mTrick);
        startActivity(toAddTrick);
    }

    public void toUrl1(View view) {
        Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(mTutorial1));
        startActivity(browserIntent);
    }

    public void toUrl2(View view) {
        Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(mTutorial2));
        startActivity(browserIntent);
    }

    public void toUrl3(View view) {
        Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(mTutorial3));
        startActivity(browserIntent);
    }

    public void toUrl4(View view) {
        Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(mTutorial4));
        startActivity(browserIntent);
    }

    public void toSource(View view) {
        Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(mSource));
        startActivity(browserIntent);
    }
}
