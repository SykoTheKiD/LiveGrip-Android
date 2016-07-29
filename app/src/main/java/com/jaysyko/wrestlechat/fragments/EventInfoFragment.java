package com.jaysyko.wrestlechat.fragments;

import android.app.Activity;
import android.app.Fragment;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.jaysyko.wrestlechat.R;
import com.jaysyko.wrestlechat.adapters.EventDetailsAdapter;
import com.jaysyko.wrestlechat.application.eLog;
import com.jaysyko.wrestlechat.eventManager.CurrentActiveEvent;
import com.jaysyko.wrestlechat.models.Event;
import com.jaysyko.wrestlechat.utils.ImageTools;
import com.squareup.picasso.Picasso;
import com.squareup.picasso.Target;

public class EventInfoFragment extends Fragment {

    private static final String TAG = EventInfoFragment.class.getSimpleName();
    private final Event currentActiveEvent = CurrentActiveEvent.getInstance().getCurrentEvent();
//    private final Handler handler = new Handler();
    private View view;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view = inflater.inflate(R.layout.fragment_event_info, container, false);
        Toolbar toolbar = (Toolbar) view.findViewById(R.id.toolbar);
        final Activity activity = getActivity();
        ((AppCompatActivity) activity).setSupportActionBar(toolbar);
        prepareEventInfoContent(activity);
        ListView eventDetails = (ListView) view.findViewById(R.id.events_list_view);
        eventDetails.setEmptyView(view.findViewById(R.id.empty_layout));
        EventDetailsAdapter adapter = new EventDetailsAdapter(getActivity(), currentActiveEvent.getMatchList());
        eventDetails.setAdapter(adapter);

//        handler.post(new Runnable() {
//            @Override
//            public void run() {
//                new AdBuilder(activity).buildAd();
//            }
//        });
        return view;
    }

    private void prepareEventInfoContent(final Context context) {
        Animation animation = AnimationUtils.loadAnimation(context, R.anim.fade_in);
        TextView eventTitle = (TextView) view.findViewById(R.id.event_title);
        eventTitle.setText(currentActiveEvent.getEventName());
        final ImageView eventImage = (ImageView) view.findViewById(R.id.event_image);
        final Target target = new Target() {
            @Override
            public void onBitmapLoaded(Bitmap bitmap, Picasso.LoadedFrom from) {
                eLog.e(TAG, "Bitmap loaded");
                eventImage.setImageBitmap(bitmap);
                view.setBackgroundColor(ImageTools.getDominantColor(bitmap));
            }

            @Override
            public void onBitmapFailed(Drawable errorDrawable) {
                eLog.e(TAG, "Bitmap failed");
            }

            @Override
            public void onPrepareLoad(Drawable placeHolderDrawable) {
                eLog.e(TAG, "Bitmap load");
            }
        };
        eventImage.setTag(target);
        eventImage.startAnimation(animation);
        Picasso.with(context).load(currentActiveEvent.getEventImage()).into(target);
    }

}
