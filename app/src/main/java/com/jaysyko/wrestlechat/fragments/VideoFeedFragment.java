package com.jaysyko.wrestlechat.fragments;

import android.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.android.youtube.player.YouTubeInitializationResult;
import com.google.android.youtube.player.YouTubePlayer;
import com.google.android.youtube.player.YouTubePlayerView;
import com.jaysyko.wrestlechat.R;
import com.jaysyko.wrestlechat.application.eLog;
import com.jaysyko.wrestlechat.eventManager.CurrentActiveEvent;
import com.jaysyko.wrestlechat.keys.Authorization;

/**
 * A placeholder fragment containing a simple view.
 */
public class VideoFeedFragment extends Fragment {

    private static final String TAG = VideoFeedFragment.class.getSimpleName();

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_video_feed, container, false);
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        View view = getView();
        if (view != null){
            YouTubePlayerView youTubePlayerView = (YouTubePlayerView) view.findViewById(R.id.youtube_video);
            youTubePlayerView.initialize(Authorization.YOUTUBE_API_KEY, new YouTubePlayer.OnInitializedListener() {
                @Override
                public void onInitializationSuccess(YouTubePlayer.Provider provider, YouTubePlayer youTubePlayer, boolean wasRestored) {
                    if (!wasRestored) {
                        youTubePlayer.loadVideo(CurrentActiveEvent.getInstance().getCurrentEvent().getVideo()); // Plays https://www.youtube.com/watch?v=fhWaJi1Hsfo
                    }
                }

                @Override
                public void onInitializationFailure(YouTubePlayer.Provider provider, YouTubeInitializationResult youTubeInitializationResult) {
                    eLog.e(TAG, "Error Video");
                }
            });
        }
    }
}
