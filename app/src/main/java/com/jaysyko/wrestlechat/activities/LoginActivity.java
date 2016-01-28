package com.jaysyko.wrestlechat.activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.jaysyko.wrestlechat.R;
import com.parse.LogInCallback;
import com.parse.ParseUser;
import com.parse.SignUpCallback;

public class LoginActivity extends AppCompatActivity {

    public static final String WELCOME_BACK_MESSAGE = "Welcome Back, ";
    public static final String FAILED_LOGIN_MSG = "Wrong username/password combo";
    public static final String SIGN_UP_ERROR_MSG = "There was an error signing up.";
    private EditText usernameField;
    private EditText passwordField;
    private String username;
    private String password;
    private String createdDate;
    private String userId;
    private Intent intent;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        // Redirect to Events page if logged in;
        intent = new Intent(getApplicationContext(), EventListActivity.class);
        ParseUser currentUser = ParseUser.getCurrentUser();
        if (currentUser != null) {
            startActivity(intent);
        } else {
            Button loginButton = (Button) findViewById(R.id.loginButton);
            Button signUpButton = (Button) findViewById(R.id.signupButton);
            usernameField = (EditText) findViewById(R.id.loginUsernameTV);
            passwordField = (EditText) findViewById(R.id.loginPasswordTV);
            loginButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    username = usernameField.getText().toString();
                    password = passwordField.getText().toString();
                    ParseUser.logInInBackground(username, password, new LogInCallback() {
                        public void done(ParseUser user, com.parse.ParseException e) {
                            if (user != null) {
                                Toast.makeText(getApplicationContext(),
                                        WELCOME_BACK_MESSAGE.concat(username),
                                        Toast.LENGTH_LONG).show();
                                startActivity(intent);
                            } else {
                                Toast.makeText(getApplicationContext(),
                                        FAILED_LOGIN_MSG,
                                        Toast.LENGTH_LONG).show();
                            }
                        }
                    });
                }
            });

            signUpButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {

                    username = usernameField.getText().toString();
                    password = passwordField.getText().toString();

                    ParseUser user = new ParseUser();
                    user.setUsername(username);
                    user.setPassword(password);

                    user.signUpInBackground(new SignUpCallback() {
                        public void done(com.parse.ParseException e) {
                            if (e == null) {
                                startActivity(intent);
                            } else {
                                Toast.makeText(getApplicationContext(),
                                        SIGN_UP_ERROR_MSG
                                        , Toast.LENGTH_LONG).show();
                            }
                        }
                    });
                }
            });
        }
    }
}
