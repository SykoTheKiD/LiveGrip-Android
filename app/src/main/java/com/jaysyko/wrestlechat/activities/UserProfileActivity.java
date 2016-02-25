package com.jaysyko.wrestlechat.activities;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;

import com.jaysyko.wrestlechat.R;
import com.jaysyko.wrestlechat.ads.AdBuilder;
import com.jaysyko.wrestlechat.auth.CurrentActiveUser;
import com.jaysyko.wrestlechat.dialogs.Dialog;
import com.jaysyko.wrestlechat.forms.Form;
import com.jaysyko.wrestlechat.forms.formValidators.SignUpValidator;
import com.jaysyko.wrestlechat.network.NetworkState;
import com.jaysyko.wrestlechat.utils.ImageTools;
import com.jaysyko.wrestlechat.utils.IntentKeys;

public class UserProfileActivity extends AppCompatActivity {


    private Context applicationContext;
    private Handler handler = new Handler();
    private EditText newPassword, username;
    private CurrentActiveUser currentActiveUser;
    private ImageView profilePicture;
    private Button update;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_profile);
        setTitle("Manage Profile");
        username = (EditText) findViewById(R.id.usernameChange);
        currentActiveUser = CurrentActiveUser.getInstance();
        username.setText(currentActiveUser.getUsername());
        applicationContext = getApplicationContext();
        newPassword = (EditText) findViewById(R.id.newPassword);
        profilePicture = (ImageView) findViewById(R.id.profilePicture);
        ImageTools.loadImage(getApplicationContext(), currentActiveUser.getCustomProfileImageURL(), profilePicture);
        update = (Button) findViewById(R.id.updateProfile);
        handler.post(new Runnable() {
            @Override
            public void run() {
                new AdBuilder(UserProfileActivity.this).buildAd();
            }
        });
        handler.post(new Runnable() {
            @Override
            public void run() {
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
                                    } else {
                                        ImageTools.loadImage(applicationContext, currentActiveUser.getCustomProfileImageURL(), profilePicture);
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
            }
        });
        handler.post(new Runnable() {
            @Override
            public void run() {
                update.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        String newPasswordStr = newPassword.getText().toString();
                        String newUsernameStr = username.getText().toString();
                        if (!(newUsernameStr.equals(currentActiveUser.getUsername())) && !newPasswordStr.isEmpty()) {
                            Form form = new SignUpValidator(newUsernameStr, newPasswordStr).validate();
                            if (form.isValid()) {
                                currentActiveUser.setUsername(newUsernameStr);
                                currentActiveUser.setPassword(newPasswordStr);
                                Dialog.makeToast(applicationContext, getString(R.string.password_changed_relogin));
                                CurrentActiveUser.getInstance().logout();
                                Intent intent = new Intent(applicationContext, LoginActivity.class);
                                intent.putExtra(IntentKeys.USERNAME, currentActiveUser.getUsername());
                                startActivity(intent);
                            } else {
                                Dialog.makeToast(applicationContext, getString(Form.getSimpleMessage(form.getReason())));
                            }
                        } else if (!(newUsernameStr.equals(currentActiveUser.getUsername()))) {
                            Form form = new SignUpValidator(newUsernameStr, "pass").validate();
                            if (form.isValid()) {
                                currentActiveUser.setUsername(newUsernameStr);
                                Dialog.makeToast(applicationContext, getString(R.string.saved_successfully));
                                startActivity(new Intent(applicationContext, EventListActivity.class));
                            } else {
                                Dialog.makeToast(applicationContext, getString(Form.getSimpleMessage(form.getReason())));
                            }
                        } else if (!newPasswordStr.isEmpty()) {
                            Form form = new SignUpValidator(newUsernameStr, newPasswordStr).validate();
                            if (form.isValid()) {
                                currentActiveUser.setPassword(newPasswordStr);
                                Dialog.makeToast(applicationContext, getString(R.string.password_changed_relogin));
                                CurrentActiveUser.getInstance().logout();
                                Intent intent = new Intent(applicationContext, LoginActivity.class);
                                intent.putExtra(IntentKeys.USERNAME, currentActiveUser.getUsername());
                                startActivity(intent);
                            } else {
                                Dialog.makeToast(applicationContext, getString(Form.getSimpleMessage(form.getReason())));
                            }
                        }
                    }
                });
            }
        });
    }
}
