package com.jaysyko.wrestlechat.activities;

import android.app.Fragment;
import android.app.FragmentManager;
import android.os.Bundle;

import com.google.android.youtube.player.YouTubeBaseActivity;
import com.jaysyko.wrestlechat.R;
import com.jaysyko.wrestlechat.fragments.MessagingFragment;
import com.jaysyko.wrestlechat.fragments.VideoFeedFragment;
import com.jaysyko.wrestlechat.keys.BundleKeys;

public class VideoFeedActivity extends YouTubeBaseActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_video_feed);
        FragmentManager fm = getFragmentManager();
        Fragment youtubeFragment = fm.findFragmentById(R.id.youtube_fragment);
        Fragment messagingFragment = fm.findFragmentById(R.id.messaging_fragment);

        if (youtubeFragment == null && messagingFragment == null) {
            Bundle videoFeedBundle = new Bundle();
            videoFeedBundle.putBoolean(BundleKeys.SHOW_TOOLBAR, false);
            youtubeFragment = new VideoFeedFragment();
            messagingFragment = new MessagingFragment();
            messagingFragment.setArguments(videoFeedBundle);
            fm.beginTransaction()
                    .add(R.id.youtube_fragment, youtubeFragment)
                    .add(R.id.messaging_fragment, messagingFragment)
                    .commit();
        }
    }
}
