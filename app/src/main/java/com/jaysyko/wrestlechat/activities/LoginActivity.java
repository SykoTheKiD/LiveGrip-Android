package com.jaysyko.wrestlechat.activities;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.jaysyko.wrestlechat.R;
import com.jaysyko.wrestlechat.application.Initializer;
import com.jaysyko.wrestlechat.auth.CurrentActiveUser;
import com.jaysyko.wrestlechat.dialogs.Dialog;

import static com.jaysyko.wrestlechat.utils.FormValidation.formIsClean;

public class LoginActivity extends AppCompatActivity {

    public static final String WELCOME_BACK_MESSAGE = "Welcome Back, ";
    public static final String FAILED_LOGIN_MSG = "Wrong username/password combo";
    public static final String SIGN_UP_ERROR_MSG = "There was an error signing up.";
    private static final String ERROR_MSG_BLANK = "Username/Password cannot be blank";
    private EditText usernameField;
    private EditText passwordField;
    private TextView signUpText;
    private String username;
    private String password;
    private Intent intent;
    private boolean signIn = true;
    private Context context;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        // Redirect to Events page if logged in;
        Initializer.setInternetCheck(true);
        context = getApplicationContext();
        intent = new Intent(context, EventListActivity.class);

        if (CurrentActiveUser.getInstance() != null) {
            startActivity(intent);
            finish();
        } else {
            final Button loginButton = (Button) findViewById(R.id.signIn);
            final Button signUpButton = (Button) findViewById(R.id.signUp);
            usernameField = (EditText) findViewById(R.id.usernameEV);
            passwordField = (EditText) findViewById(R.id.loginPasswordEV);
            signUpText = (TextView) findViewById(R.id.signUpText);
            loginButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Initializer.setInternetCheck(true);
                    username = usernameField.getText().toString();
                    password = passwordField.getText().toString();
                    if (formIsClean(username, password)) {
                        CurrentActiveUser currentActiveUser = CurrentActiveUser.getInstance(username, password);
                        if (currentActiveUser.loginUser()) {
                            Dialog.makeToast(context, WELCOME_BACK_MESSAGE.concat(username));
                            startActivity(intent);
                            finish();
                        } else {
                            Dialog.makeToast(context, FAILED_LOGIN_MSG);
                        }
                    } else {
                        Dialog.makeToast(context, ERROR_MSG_BLANK);
                    }
                }
            });

            signUpText.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (signIn) {
                        loginButton.setVisibility(View.GONE);
                        signUpButton.setVisibility(View.VISIBLE);
                        signUpText.setText(R.string.have_account_login);
                    } else {
                        loginButton.setVisibility(View.VISIBLE);
                        signUpButton.setVisibility(View.GONE);
                        signUpText.setText(R.string.no_account_dialog);
                    }
                    signIn ^= true;

                }
            });

            signUpButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Initializer.setInternetCheck(true);
                    username = usernameField.getText().toString();
                    password = passwordField.getText().toString();
                    CurrentActiveUser currentActiveUser = CurrentActiveUser.getInstance(username, password);
                    if (currentActiveUser.signUpUser()) {
                        startActivity(intent);
                    } else {
                        Dialog.makeToast(context, SIGN_UP_ERROR_MSG);
                    }

                }
            });
        }
    }
}
