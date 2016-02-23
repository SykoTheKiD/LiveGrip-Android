package com.jaysyko.wrestlechat.activities;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;

import com.jaysyko.wrestlechat.R;
import com.jaysyko.wrestlechat.ads.AdBuilder;
import com.jaysyko.wrestlechat.auth.CurrentActiveUser;
import com.jaysyko.wrestlechat.dialogs.Dialog;
import com.jaysyko.wrestlechat.network.NetworkState;
import com.jaysyko.wrestlechat.utils.ImageTools;

public class UserProfileActivity extends AppCompatActivity {


    private Context applicationContext;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_profile);
        EditText username = (EditText) findViewById(R.id.usernameChange);
        CurrentActiveUser currentActiveUser = CurrentActiveUser.getInstance();
        username.setText(currentActiveUser.getUsername());
        applicationContext = getApplicationContext();
        EditText newPassword = (EditText) findViewById(R.id.newPassword);
        ImageView profilePicture = (ImageView) findViewById(R.id.profilePicture);
        ImageTools.loadImage(getApplicationContext(), currentActiveUser.getCustomProfileImageURL(), profilePicture);
        Button update = (Button) findViewById(R.id.updateProfile);
        new AdBuilder(this).buildAd();
        profilePicture.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder builder = new AlertDialog.Builder(UserProfileActivity.this);
                builder.setTitle(getString(R.string.custom_image_title));

                // Set up the input
                final EditText input = new EditText(UserProfileActivity.this);
                // Specify the type of input expected; this, for example, sets the input as a password, and will mask the text
                builder.setView(input);

                // Set up the buttons
                builder.setPositiveButton(getString(R.string.ok_dialog), new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        if (NetworkState.isConnected(applicationContext)) {
                            if (!(CurrentActiveUser.getInstance().setProfileImageURL(input.getText().toString()))) {
                                Dialog.makeToast(applicationContext, getString(R.string.bad_image_type));
                            }
                        } else {
                            Dialog.makeToast(applicationContext, getString(R.string.no_network));
                        }
                    }
                });
                builder.setNegativeButton(getString(R.string.cancel_dialog), new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.cancel();
                    }
                });

                builder.show();
            }
        });
        update.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });
    }
}
