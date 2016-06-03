package com.jaysyko.wrestlechat.fragments;

import android.app.AlertDialog;
import android.app.Fragment;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;

import com.jaysyko.wrestlechat.R;
import com.jaysyko.wrestlechat.auth.CurrentActiveUser;
import com.jaysyko.wrestlechat.dialogs.Dialog;
import com.jaysyko.wrestlechat.network.NetworkState;
import com.jaysyko.wrestlechat.utils.ImageTools;

public class UserProfileFragment extends Fragment {

    private Context mApplicationContext;
    private Handler handler = new Handler();
    private CurrentActiveUser currentActiveUser;
    private ImageView profilePicture;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_user_profile, container, false);
        getActivity().setTitle(getString(R.string.manage_profile));
        currentActiveUser = CurrentActiveUser.getCurrentUser();
        mApplicationContext = getActivity();
        profilePicture = (ImageView) view.findViewById(R.id.profile_picture);
        ImageTools.loadImage(mApplicationContext, currentActiveUser.getProfileImage(), profilePicture);
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
                                if (NetworkState.isConnected(mApplicationContext)) {
                                    if (!(CurrentActiveUser.getCurrentUser().setProfileImageURL(input.getText().toString()))) {
                                        Dialog.makeToast(mApplicationContext, getString(R.string.bad_image_type));
                                    } else {
                                        ImageTools.loadImage(mApplicationContext, currentActiveUser.getProfileImage(), profilePicture);
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
