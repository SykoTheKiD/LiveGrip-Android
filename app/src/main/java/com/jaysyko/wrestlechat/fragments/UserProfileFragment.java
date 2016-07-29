package com.jaysyko.wrestlechat.fragments;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Fragment;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.jaysyko.wrestlechat.R;
import com.jaysyko.wrestlechat.dialogs.Dialog;
import com.jaysyko.wrestlechat.exceptions.ImageURLError;
import com.jaysyko.wrestlechat.models.User;
import com.jaysyko.wrestlechat.network.NetworkState;
import com.jaysyko.wrestlechat.sessionManager.SessionManager;
import com.jaysyko.wrestlechat.utils.ImageTools;

public final class UserProfileFragment extends Fragment {

    private static final String TAG = UserProfileFragment.class.getSimpleName();
    private Context mApplicationContext;
    private User currentActiveUser;
    private ImageView profilePicture;
    private TextView username;
    private static final Handler handler = new Handler();

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_user_profile, container, false);
        getActivity().setTitle(getString(R.string.manage_profile));
        currentActiveUser = SessionManager.getCurrentUser();
        mApplicationContext = getActivity();
        profilePicture = (ImageView) view.findViewById(R.id.profile_picture);
        username = (TextView) view.findViewById(R.id.user_profile_fragment_user_name);
        ImageTools.loadImage(mApplicationContext, currentActiveUser.getProfileImage(), profilePicture);
        handler.post(new Runnable() {
            @Override
            public void run() {
                username.setText(currentActiveUser.getUsername());
            }
        });
        handler.post(new Runnable() {
            @Override
            public void run() {
                profilePicture.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        final Activity activity = getActivity();
                        AlertDialog.Builder builder = new AlertDialog.Builder(activity);
                        builder.setTitle(getString(R.string.custom_image_title));

                        // Set up the input
                        final EditText input = new EditText(activity);
                        // Specify the type of input expected; this, for example, sets the input as a password, and will mask the text
                        builder.setView(input);

                        // Set up the buttons
                        builder.setPositiveButton(getString(R.string.ok_dialog), new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                if (NetworkState.isConnected()) {
                                    try {
                                        SessionManager.getCurrentUser().setProfileImage(input.getText().toString(), mApplicationContext);
                                        ImageTools.loadImage(mApplicationContext, currentActiveUser.getProfileImage(), profilePicture);
                                    }catch (ImageURLError e){
                                        Log.e(TAG, e.getMessage());
                                        Dialog.makeToast(mApplicationContext, getString(R.string.bad_image_type));
                                    }
                                } else {
                                    Dialog.makeToast(mApplicationContext, getString(R.string.no_network));
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
        return view;
    }
}
