package com.jaysyko.wrestlechat.activities;

import android.app.AlertDialog;
import android.app.Fragment;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.text.Html;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;

import com.jaysyko.wrestlechat.R;
import com.jaysyko.wrestlechat.fragments.MessagingFragment;
import com.jaysyko.wrestlechat.services.MessagingService;

public class MessagingActivity extends BaseActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_messaging);
        Intent startServiceIntent = new Intent(this, MessagingService.class);
        startService(startServiceIntent);
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
    protected Fragment createFragment() {
        return new MessagingFragment();
    }
}