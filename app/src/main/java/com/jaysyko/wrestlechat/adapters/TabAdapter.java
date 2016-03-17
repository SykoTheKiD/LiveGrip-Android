package com.jaysyko.wrestlechat.adapters;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import com.jaysyko.wrestlechat.R;
import com.jaysyko.wrestlechat.date.LiveStatus;
import com.jaysyko.wrestlechat.fragments.TabContentFragment;
import com.jaysyko.wrestlechat.fragments.TabFragment;
import com.jaysyko.wrestlechat.utils.BundleKeys;

/**
 * Created by jarushaan on 2016-03-09.
 */
public class TabAdapter extends FragmentPagerAdapter {

    private Context context;

    public TabAdapter(FragmentManager fm, Context context) {
        super(fm);
        this.context = context;
    }

    /**
     * Return fragment with respect to Position .
     */

    @Override
    public Fragment getItem(int position) {
        TabContentFragment fragment = new TabContentFragment();
        Bundle args = new Bundle();
        if (position == 0) {
            args.putInt(BundleKeys.STATE_KEY, LiveStatus.EVENT_STARTED);
        } else if (position == 1) {
            args.putInt(BundleKeys.STATE_KEY, LiveStatus.EVENT_NOT_STARTED);
        } else if (position == 2) {
            args.putInt(BundleKeys.STATE_KEY, LiveStatus.EVENT_OVER);
        }
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public int getCount() {

        return TabFragment.MAX_TABS;

    }

    /**
     * This method returns the title of the tab according to the position.
     */

    @Override
    public CharSequence getPageTitle(int position) {

        switch (position) {
            case 0:
                return this.context.getString(R.string.tab_title_live);
            case 1:
                return this.context.getString(R.string.tab_title_upcoming);
            case 2:
                return this.context.getString(R.string.tab_title_replay);
        }
        return null;
    }
}
