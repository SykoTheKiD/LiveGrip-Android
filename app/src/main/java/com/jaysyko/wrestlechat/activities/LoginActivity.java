package com.jaysyko.wrestlechat.activities;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.jaysyko.wrestlechat.R;
import com.jaysyko.wrestlechat.auth.CurrentActiveUser;
import com.jaysyko.wrestlechat.dialogs.Dialog;

import static com.jaysyko.wrestlechat.utils.FormValidation.formIsClean;
import static com.jaysyko.wrestlechat.utils.FormValidation.isValidUsername;

public class LoginActivity extends AppCompatActivity {

    private RelativeLayout loadingPanel;
    private String username, password;
    private boolean signIn = true;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        // Redirect to Events page if logged in;
        final Context context = getApplicationContext();
        final Intent intent = new Intent(context, EventListActivity.class);
        if (CurrentActiveUser.getInstance() != null) {
            startActivity(intent);
            finish();
        } else {
            final Button loginButton = (Button) findViewById(R.id.signIn);
            final Button signUpButton = (Button) findViewById(R.id.signUp);
            final TextView signUpText = (TextView) findViewById(R.id.signUpText);
            final TextView signUpPrompt = (TextView) findViewById(R.id.sign_in_prompt);
            final EditText usernameField = (EditText) findViewById(R.id.usernameEV);
            final EditText passwordField = (EditText) findViewById(R.id.loginPasswordEV);
            loadingPanel = (RelativeLayout) findViewById(R.id.loadingPanel);

            loginButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    username = usernameField.getText().toString();
                    password = passwordField.getText().toString();
                    showLoadPanel(true, loginButton);
                    if (formIsClean(username, password)) {
                        CurrentActiveUser currentActiveUser = CurrentActiveUser.getInstance(username, password);
                        if (currentActiveUser.loginUser()) {
                            Dialog.makeToast(context, getString(R.string.welcome_back).concat(" ").concat(username));
                            startActivity(intent);
                            finish();
                        } else {
                            showLoadPanel(false, loginButton);
                            Dialog.makeToast(context, getString(R.string.incorrect_login_info));
                        }
                    } else {
                        Dialog.makeToast(context, getString(R.string.blank_username));
                    }
                    showLoadPanel(false, loginButton);
                }
            });

            signUpButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    username = usernameField.getText().toString();
                    password = passwordField.getText().toString();
                    showLoadPanel(true, loginButton);
                    if (formIsClean(username, password)) {
                        if (isValidUsername(username)) {
                            if (CurrentActiveUser.signUpUser(username, password)) {
                                startActivity(intent);
                            } else {
                                Dialog.makeToast(context, getString(R.string.username_taken));
                            }
                        } else {
                            Dialog.makeToast(context, getString(R.string.invalid_username));
                        }
                    } else {
                        Dialog.makeToast(context, getString(R.string.blank_username));
                    }
                    showLoadPanel(false, loginButton);
                }
            });

            signUpText.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (signIn) {
                        loginButton.setVisibility(View.GONE);
                        signUpButton.setVisibility(View.VISIBLE);
                        signUpText.setText(R.string.have_account_login);
                        signUpPrompt.setText(getString(R.string.sign_up_to_app));
                    } else {
                        loginButton.setVisibility(View.VISIBLE);
                        signUpButton.setVisibility(View.GONE);
                        signUpText.setText(R.string.no_account_dialog);
                        signUpPrompt.setText(getString(R.string.sign_in_to_app));
                    }
                    signIn ^= true;
                }
            });
        }
    }

    private void showLoadPanel(Boolean buttonHide, Button button) {
        if (buttonHide) {
            loadingPanel.setVisibility(View.VISIBLE);
            button.setVisibility(View.GONE);
        } else {
            loadingPanel.setVisibility(View.GONE);
            button.setVisibility(View.VISIBLE);
        }
    }
}
