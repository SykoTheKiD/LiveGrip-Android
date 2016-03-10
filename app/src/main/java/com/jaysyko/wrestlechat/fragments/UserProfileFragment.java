package com.jaysyko.wrestlechat.fragments;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;

import com.jaysyko.wrestlechat.R;
import com.jaysyko.wrestlechat.activities.EventListActivity;
import com.jaysyko.wrestlechat.activities.LoginActivity;
import com.jaysyko.wrestlechat.ads.AdBuilder;
import com.jaysyko.wrestlechat.auth.CurrentActiveUser;
import com.jaysyko.wrestlechat.dialogs.Dialog;
import com.jaysyko.wrestlechat.forms.Form;
import com.jaysyko.wrestlechat.forms.formValidators.SignUpValidator;
import com.jaysyko.wrestlechat.network.NetworkState;
import com.jaysyko.wrestlechat.utils.ImageTools;
import com.jaysyko.wrestlechat.utils.IntentKeys;
import com.jaysyko.wrestlechat.utils.StringResources;

public class UserProfileFragment extends Fragment {

    private Context applicationContext;
    private Handler handler = new Handler();
    private EditText newPassword, username;
    private CurrentActiveUser currentActiveUser;
    private ImageView profilePicture;
    private Button update;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_user_profile, container, false);
        getActivity().setTitle(getString(R.string.manage_profile));
        username = (EditText) view.findViewById(R.id.usernameChange);
        currentActiveUser = CurrentActiveUser.getInstance();
        username.setText(currentActiveUser.getUsername());
        applicationContext = getActivity();
        newPassword = (EditText) view.findViewById(R.id.newPassword);
        profilePicture = (ImageView) view.findViewById(R.id.profilePicture);
        ImageTools.loadImage(applicationContext, currentActiveUser.getCustomProfileImageURL(), profilePicture);
        update = (Button) view.findViewById(R.id.updateProfile);
        handler.post(new Runnable() {
            @Override
            public void run() {
                new AdBuilder(getActivity()).buildAd();
            }
        });
        handler.post(new Runnable() {
            @Override
            public void run() {
                profilePicture.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
                        builder.setTitle(getString(R.string.custom_image_title));

                        // Set up the input
                        final EditText input = new EditText(getActivity());
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
                        String newPasswordStr = newPassword.getText().toString().trim();
                        String newUsernameStr = username.getText().toString().trim();
                        if (!(newUsernameStr.equals(currentActiveUser.getUsername())) && !newPasswordStr.isEmpty()) {
                            if (changeUsername(newUsernameStr) && changePassword(newPasswordStr)) {
                                startActivity(reLogin());
                            }
                        } else if (!(newUsernameStr.equals(currentActiveUser.getUsername()))) {
                            if (changeUsername(newUsernameStr)) {
                                Dialog.makeToast(applicationContext, getString(R.string.saved_successfully));
                                startActivity(new Intent(applicationContext, EventListActivity.class));
                            }
                        } else if (!newPasswordStr.isEmpty()) {
                            if (changePassword(newPasswordStr)) {
                                startActivity(reLogin());
                            } else {
                                Dialog.makeToast(applicationContext, getString(R.string.error_has_occured));
                            }
                        }
                    }
                });
            }
        });
        return view;
    }

    @NonNull
    private Intent reLogin() {
        Dialog.makeToast(applicationContext, getString(R.string.password_changed_relogin));
        Intent intent = new Intent(applicationContext, LoginActivity.class);
        intent.putExtra(IntentKeys.USERNAME, currentActiveUser.getUsername());
        CurrentActiveUser.getInstance().logout();
        return intent;
    }

    private boolean changeUsername(String newUsernameStr) {
        Form form = new SignUpValidator(newUsernameStr, StringResources.DUMMY_PASSWORD).validate();
        if (form.isValid()) {
            if (currentActiveUser.setUsername(newUsernameStr)) {
                return true;
            } else {
                Dialog.makeToast(applicationContext, getString(R.string.username_taken));
                return false;
            }
        } else {
            Dialog.makeToast(applicationContext, getString(Form.getSimpleMessage(form.getReason())));
            return false;
        }
    }

    private boolean changePassword(String newPasswordStr) {
        Form form = new SignUpValidator(currentActiveUser.getUsername(), newPasswordStr).validate();
        if (form.isValid()) {
            return currentActiveUser.setPassword(newPasswordStr);
        } else {
            Dialog.makeToast(applicationContext, getString(Form.getSimpleMessage(form.getReason())));
            return false;
        }
    }
}
