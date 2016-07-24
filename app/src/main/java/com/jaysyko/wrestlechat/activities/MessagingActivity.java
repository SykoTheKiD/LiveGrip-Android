package com.jaysyko.wrestlechat.activities;

import android.app.AlertDialog;
import android.app.Fragment;
import android.content.ComponentName;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.Bundle;
import android.os.IBinder;
import android.text.Html;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;

import com.jaysyko.wrestlechat.R;
import com.jaysyko.wrestlechat.fragments.MessagingFragment;
import com.jaysyko.wrestlechat.services.MessagingService;
import com.jaysyko.wrestlechat.services.MessagingServiceBinder;
import com.jaysyko.wrestlechat.services.ServiceProvider;

public class MessagingActivity extends BaseActivity implements ServiceProvider {

    private boolean mBound;
    private MessagingService mService;
    private MessagingServiceBinder mBinder;

    private ServiceConnection mConnection = new ServiceConnection() {

        @Override
        public void onServiceConnected(ComponentName className, IBinder service) {
            mBinder = (MessagingServiceBinder) service;
            mService = mBinder.getService();
            mBound = true;
        }

        @Override
        public void onServiceDisconnected(ComponentName arg0) {
            mBound = false;
        }
    };

    @Override
    protected Fragment createFragment() {
        return new MessagingFragment();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_messaging);
        Intent startServiceIntent = new Intent(this, MessagingService.class);
        startService(startServiceIntent);
    }

    @Override
    protected void onStart() {
        super.onStart();
        Intent intent = new Intent(this, MessagingService.class);
        bindService(intent, mConnection, Context.BIND_AUTO_CREATE);
    }

    @Override
    protected void onStop() {
        super.onStop();
        if (mBound) {
            unbindService(mConnection);
            mBound = false;
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.messaging_screen_menu, menu);
        return true;
    }

    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle item selection
        Intent intent;
        switch (item.getItemId()) {
            case R.id.action_event_info:
                intent = new Intent(getApplicationContext(), EventInfoActivity.class);
                startActivity(intent);
                return true;
            case R.id.action_rules:
                AlertDialog.Builder builder = new AlertDialog.Builder(this);
                builder.setMessage(
                        Html.fromHtml(
                                "<h2>Chat Rules</h2>" +
                                "<p>-No Spam</p>" +
                                "<p>-No Offensive Material</p>" +
                                "<p>-No Soliciting</p>"
                        )
                );
                builder.setCancelable(true);

                builder.setPositiveButton(
                        "OK",
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                dialog.cancel();
                            }
                        });
                AlertDialog alertDialog = builder.create();
                alertDialog.show();
                return true;
            case R.id.action_reddit:
                intent = new Intent(getApplicationContext(), WebViewActivity.class);
                startActivity(intent);
                return true;
            case R.id.action_user_profile:
                intent = new Intent(getApplicationContext(), UserProfileActivity.class);
                startActivity(intent);
                return true;
            case R.id.action_settings:
                intent = new Intent(getApplicationContext(), SettingsActivity.class);
                startActivity(intent);
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    @Override
    public MessagingService getMessagingService() {
        return mService;
    }

    @Override
    public MessagingServiceBinder getMessagingServiceBinder() {
        return mBinder;
    }
}