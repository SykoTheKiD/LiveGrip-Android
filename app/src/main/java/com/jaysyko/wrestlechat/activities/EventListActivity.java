package com.jaysyko.wrestlechat.activities;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.design.widget.NavigationView;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

import com.jaysyko.wrestlechat.R;
import com.jaysyko.wrestlechat.auth.CurrentActiveUser;
import com.jaysyko.wrestlechat.dialogs.Dialog;
import com.jaysyko.wrestlechat.fragments.TabFragment;
import com.jaysyko.wrestlechat.network.NetworkState;
import com.jaysyko.wrestlechat.utils.StringResources;

public class EventListActivity extends AppCompatActivity{
    DrawerLayout mDrawerLayout;
    NavigationView mNavigationView;
    FragmentManager mFragmentManager;
    FragmentTransaction mFragmentTransaction;
    Context applicationContext;
    Handler handler = new Handler();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_event_list);
        applicationContext = getApplicationContext();
        /**
         *Setup the DrawerLayout and NavigationView
         */

        mDrawerLayout = (DrawerLayout) findViewById(R.id.drawerLayout);
        mNavigationView = (NavigationView) findViewById(R.id.navDrawerItems);
        final View headerLayout = mNavigationView.inflateHeaderView(R.layout.nav_header_event_list);
        TextView headerUsername = (TextView) headerLayout.findViewById(R.id.drawer_username);
        headerUsername.setText(CurrentActiveUser.getInstance().getUsername());

        /**
         * Lets inflate the very first fragment
         * Here , we are inflating the TabFragment as the first Fragment
         */

        mFragmentManager = getSupportFragmentManager();
        mFragmentTransaction = mFragmentManager.beginTransaction();
        mFragmentTransaction.replace(R.id.containerView, new TabFragment()).commit();
        /**
         * Setup click events on the Navigation View Items.
         */

        mNavigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(MenuItem menuItem) {
                mDrawerLayout.closeDrawers();

                int itemId = menuItem.getItemId();
                if (itemId == R.id.nav_my_profile) {
                    Intent intent = new Intent(applicationContext, UserProfileActivity.class);
                    startActivity(intent);
                 }

                if (itemId == R.id.nav_logout) {
                    if (NetworkState.isConnected(applicationContext)) {
                        handler.post(new Runnable() {
                            @Override
                            public void run() {
                                CurrentActiveUser.getInstance().logout();
                                Intent intent = new Intent(applicationContext, LoginActivity.class);
                                startActivity(intent);
                                finish();
                            }
                        });
                    } else {
                        Dialog.makeToast(applicationContext, getString(R.string.no_network));
                    }
                }

                if (itemId == R.id.nav_share) {
                    Intent share = new Intent(Intent.ACTION_SEND);
                    share.setType(StringResources.PLAIN_CONTENT_TYPE);
                    share.putExtra(Intent.EXTRA_TEXT, R.string.app_share);
                    startActivity(Intent.createChooser(share, getString(R.string.app_share_title)));
                }
                if (itemId == R.id.nav_about) {
                    Intent aboutIntent = new Intent(applicationContext, AboutActivity.class);
                    startActivity(aboutIntent);
                }
                if (itemId == R.id.nav_legal) {
                    Intent legalIntent = new Intent(applicationContext, LegalActivity.class);
                    startActivity(legalIntent);
                }
                 return false;
            }

        });

        /**
         * Setup Drawer Toggle of the Toolbar
         */

                android.support.v7.widget.Toolbar toolbar = (android.support.v7.widget.Toolbar) findViewById(R.id.toolbar);
        ActionBarDrawerToggle mDrawerToggle = new ActionBarDrawerToggle(this, mDrawerLayout, toolbar, R.string.app_name,
                R.string.app_name);
                mDrawerLayout.setDrawerListener(mDrawerToggle);
                mDrawerToggle.syncState();
    }
}