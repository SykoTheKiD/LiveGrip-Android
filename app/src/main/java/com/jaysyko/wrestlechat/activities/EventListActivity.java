package com.jaysyko.wrestlechat.activities;

import android.annotation.SuppressLint;
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
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.TextView;

import com.jaysyko.wrestlechat.R;
import com.jaysyko.wrestlechat.analytics.ShareTracker;
import com.jaysyko.wrestlechat.application.eLog;
import com.jaysyko.wrestlechat.customImageView.RoundedImageView;
import com.jaysyko.wrestlechat.dialogs.Dialog;
import com.jaysyko.wrestlechat.fragments.EventListFragment;
import com.jaysyko.wrestlechat.models.User;
import com.jaysyko.wrestlechat.network.NetworkState;
import com.jaysyko.wrestlechat.sessionManager.SessionManager;
import com.jaysyko.wrestlechat.utils.ImageTools;
import com.jaysyko.wrestlechat.utils.StringResources;

public final class EventListActivity extends AppCompatActivity {

    private static final String TAG = EventListActivity.class.getSimpleName();
    DrawerLayout mDrawerLayout;
    NavigationView mNavigationView;
    FragmentManager mFragmentManager;
    FragmentTransaction mFragmentTransaction;
    Context mApplicationContext;
    Handler handler = new Handler();

    @SuppressLint("CommitTransaction")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_event_list);
        mApplicationContext = getApplicationContext();
        /**
         *Setup the DrawerLayout and NavigationView
         */
        mDrawerLayout = (DrawerLayout) findViewById(R.id.drawerLayout);
        mNavigationView = (NavigationView) findViewById(R.id.navDrawerItems);
        final View headerLayout = mNavigationView != null ? mNavigationView.inflateHeaderView(R.layout.nav_header_event_list) : null;
        final TextView headerUsername = (TextView) (headerLayout != null ? headerLayout.findViewById(R.id.drawer_username) : null);
        final RoundedImageView headerProfileImage = (RoundedImageView) (headerLayout != null ? headerLayout.findViewById(R.id.header_profile_image) : null);
        if (headerUsername != null && headerProfileImage != null) {
            final User currentUser = SessionManager.getCurrentUser();
            headerUsername.setText(currentUser.getUsername());
            ImageTools.loadImage(mApplicationContext, currentUser.getProfileImage(), headerProfileImage);
        }
        /**
         * Lets inflate the very first fragment
         * Here we are inflating the TabFragment as the first Fragment
         */

        mFragmentManager = getSupportFragmentManager();
        mFragmentTransaction = mFragmentManager.beginTransaction();
        mFragmentTransaction.replace(R.id.container_view, new EventListFragment()).commit();
        /**
         * Setup click events on the Navigation View Items.
         */

        mNavigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(MenuItem menuItem) {
                mDrawerLayout.closeDrawers();

                int itemId = menuItem.getItemId();
                if (itemId == R.id.nav_my_profile) {
                    Intent intent = new Intent(mApplicationContext, UserProfileActivity.class);
                    startActivity(intent);
                 }

                if (itemId == R.id.nav_logout) {
                    if (NetworkState.isConnected(mApplicationContext)) {
                        handler.post(new Runnable() {
                            @Override
                            public void run() {
                                SessionManager.destroySession(mApplicationContext);
                                eLog.i(TAG, "Session Destroyed");
                                Intent intent = new Intent(mApplicationContext, LoginActivity.class);
                                startActivity(intent);
                                finish();
                            }
                        });
                    } else {
                        Dialog.makeToast(mApplicationContext, getString(R.string.no_network));
                    }
                }

                if (itemId == R.id.nav_share) {
                    handler.post(new Runnable() {
                        @Override
                        public void run() {
                            ShareTracker.trackShare();
                        }
                    });
                    Intent share = new Intent(Intent.ACTION_SEND);
                    share.setType(StringResources.PLAIN_CONTENT_TYPE);
                    share.putExtra(Intent.EXTRA_TEXT, R.string.app_share);
                    startActivity(Intent.createChooser(share, getString(R.string.app_share_title)));
                }
                if (itemId == R.id.nav_about) {
                    Intent aboutIntent = new Intent(mApplicationContext, AboutActivity.class);
                    startActivity(aboutIntent);
                }
                if (itemId == R.id.nav_legal) {
                    Intent legalIntent = new Intent(mApplicationContext, LegalActivity.class);
                    startActivity(legalIntent);
                }
                 return false;
            }

        });

        /**
         * Setup Drawer Toggle of the Toolbar
         */

        android.support.v7.widget.Toolbar toolbar = (android.support.v7.widget.Toolbar) findViewById(R.id.toolbar);
        ActionBarDrawerToggle mDrawerToggle = new ActionBarDrawerToggle(this, mDrawerLayout, toolbar, R.string.app_name, R.string.app_name);
        Animation animation = AnimationUtils.loadAnimation(this, R.anim.slide);
        if (toolbar != null) {
            toolbar.startAnimation(animation);
        } else {
            eLog.e(TAG, "Toolbar is null");
        }
        mDrawerLayout.addDrawerListener(mDrawerToggle);
        mDrawerToggle.syncState();
    }
}
