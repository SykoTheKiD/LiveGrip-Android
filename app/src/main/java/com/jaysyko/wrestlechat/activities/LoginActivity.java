package com.jaysyko.wrestlechat.activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import com.jaysyko.wrestlechat.R;
import com.jaysyko.wrestlechat.application.Initializer;
import com.jaysyko.wrestlechat.auth.CurrentActiveUser;
import com.jaysyko.wrestlechat.dialogs.Dialog;

public class LoginActivity extends AppCompatActivity {

    public static final String WELCOME_BACK_MESSAGE = "Welcome Back, ";
    public static final String FAILED_LOGIN_MSG = "Wrong username/password combo";
    public static final String SIGN_UP_ERROR_MSG = "There was an error signing up.";
    private EditText usernameField;
    private EditText passwordField;
    private String username;
    private String password;
    private Intent intent;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        // Redirect to Events page if logged in;
        Initializer.setInternetCheck(true);
        intent = new Intent(getApplicationContext(), EventListActivity.class);
        if (CurrentActiveUser.getInstance() != null) {
            startActivity(intent);
            finish();
        } else {
            Button loginButton = (Button) findViewById(R.id.signIn);
            Button signUpButton = (Button) findViewById(R.id.signUp);
            usernameField = (EditText) findViewById(R.id.usernameEV);
            passwordField = (EditText) findViewById(R.id.loginPasswordEV);
            loginButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Initializer.setInternetCheck(true);
                    username = usernameField.getText().toString();
                    password = passwordField.getText().toString();
                    CurrentActiveUser currentActiveUser = CurrentActiveUser.getInstance(username, password);
                    if (currentActiveUser.loginUser()) {
                        Dialog.makeToast(getApplicationContext(), WELCOME_BACK_MESSAGE.concat(username));
                        startActivity(intent);
                    } else {
                        Dialog.makeToast(getApplicationContext(), FAILED_LOGIN_MSG);
                    }
                }
            });

            signUpButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Initializer.setInternetCheck(true);
                    username = usernameField.getText().toString();
                    password = passwordField.getText().toString();
                    CurrentActiveUser currentActiveUser = CurrentActiveUser.getInstance(username, password);
                    currentActiveUser.setUsername(username);
                    currentActiveUser.setPassword(password);
                    if (currentActiveUser.signUpUser()) {
                        startActivity(intent);
                    } else {
                        Dialog.makeToast(getApplicationContext(), SIGN_UP_ERROR_MSG);
                    }

                }
            });
        }
    }
}
