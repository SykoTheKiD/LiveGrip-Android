package com.jaysyko.wrestlechat.fragments;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.jaysyko.wrestlechat.R;
import com.jaysyko.wrestlechat.adapters.TabAdapter;

public class TabFragment extends Fragment {

    public static final int MAX_TABS = 3;
    public static TabLayout tabLayout;
    public static ViewPager viewPager;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        /**
         * Inflate tab_layout and setup Views.
         */
        View view = inflater.inflate(R.layout.tab_layout, null);
        tabLayout = (TabLayout) view.findViewById(R.id.tabs);
        viewPager = (ViewPager) view.findViewById(R.id.event_info_viewpager);

        /**
         * Set an Adapter for the View Pager
         */
        viewPager.setAdapter(new TabAdapter(getChildFragmentManager(), getContext()));

//        if (CurrentEvents.getInstance(getContext()).getEvents().size() > 0){
//            view.findViewById(R.id.event_info_viewpager).setVisibility(View.VISIBLE);
//            view.findViewById(R.id.empty_layout).setVisibility(View.GONE);
//        }else{
//            view.findViewById(R.id.event_info_viewpager).setVisibility(View.GONE);
//            view.findViewById(R.id.empty_layout).setVisibility(View.VISIBLE);
//        }

        /**
         * Now , this is a workaround ,
         * The setupWithViewPager doesn't works without the runnable .
         * Maybe a Support Library Bug .
         */

        tabLayout.post(new Runnable() {
            @Override
            public void run() {
                tabLayout.setupWithViewPager(viewPager);
            }
        });

        return view;

    }

}