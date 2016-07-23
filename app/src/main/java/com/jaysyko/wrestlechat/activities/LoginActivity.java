package com.jaysyko.wrestlechat.activities;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.jaysyko.wrestlechat.R;
import com.jaysyko.wrestlechat.analytics.AuthTracker;
import com.jaysyko.wrestlechat.application.eLog;
import com.jaysyko.wrestlechat.dialogs.Dialog;
import com.jaysyko.wrestlechat.forms.Form;
import com.jaysyko.wrestlechat.forms.formTypes.LoginForm;
import com.jaysyko.wrestlechat.forms.formTypes.SignUpForm;
import com.jaysyko.wrestlechat.models.User;
import com.jaysyko.wrestlechat.network.APIInterface;
import com.jaysyko.wrestlechat.network.ApiManager;
import com.jaysyko.wrestlechat.network.NetworkCallback;
import com.jaysyko.wrestlechat.network.NetworkState;
import com.jaysyko.wrestlechat.network.requestData.AuthData;
import com.jaysyko.wrestlechat.network.responses.FailedRequestResponse;
import com.jaysyko.wrestlechat.network.responses.UserResponse;
import com.jaysyko.wrestlechat.sessionManager.SessionManager;

import retrofit2.Call;

public final class LoginActivity extends AppCompatActivity {

    private static final String TAG = LoginActivity.class.getSimpleName();
    private static final String USERNAME_INTENT_KEY = "username";
    private String username, password;
    private Handler handler = new Handler();
    private Button loginButton, signUpButton;
    private TextView signUpText;
    private EditText usernameField, passwordField;
    private Context mContext;
    private Intent intent;
    private ProgressBar progressBar;
    private boolean signIn = true;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        mContext = getApplicationContext();
        intent = new Intent(mContext, EventListActivity.class);
        if (SessionManager.isLoggedIn(mContext)) {
            startActivity(intent);
            finish();
        }
        progressBar = (ProgressBar) findViewById(R.id.progressBar);
        loginButton = (Button) findViewById(R.id.sign_in_button);
        signUpButton = (Button) findViewById(R.id.sign_up_button);
        signUpText = (TextView) findViewById(R.id.sign_up_text_view);
        usernameField = (EditText) findViewById(R.id.username_text_view);
        usernameField.setText(getIntent().getStringExtra(USERNAME_INTENT_KEY));
        passwordField = (EditText) findViewById(R.id.login_password_et);
        // Redirect to Events page if logged in;
        handler.post(new Runnable() {
            @Override
            public void run() {
                setupLoginListener();
                setupSignUpListener();
                setupSignUpTextListener();
            }
        });
    }

    private void setupSignUpTextListener() {
        signUpText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                changeMode();
            }
        });
    }

    private void changeMode() {
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

    private void setupSignUpListener() {
        signUpButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                username = usernameField.getText().toString();
                password = passwordField.getText().toString();
                if (NetworkState.isConnected(mContext)) {
                    Form form = new SignUpForm(username, password).validate();
                    if (form.isValid()) {
                        handler.postDelayed(new Runnable() {
                            @Override
                            public void run() {
                                progressBar.setVisibility(View.VISIBLE);
                                signUpButton.setVisibility(View.GONE);
                            }
                        }, 500);
                        APIInterface apiManager = ApiManager.getApiService();
                        Call<UserResponse> call = apiManager.createUser(
                                new AuthData(username, password)
                        );
                        ApiManager.request(call, new NetworkCallback<UserResponse>() {
                            @Override
                            public void onSuccess(UserResponse response) {
                                handler.postDelayed(new Runnable() {
                                    @Override
                                    public void run() {
                                        signUpButton.setVisibility(View.VISIBLE);
                                        progressBar.setVisibility(View.GONE);
                                    }
                                }, 500);
                                loginUser(response, true);
                            }

                            @Override
                            public void onFail(FailedRequestResponse t) {
                                eLog.e(TAG, t.getMessage());
                                handler.postDelayed(new Runnable() {
                                    @Override
                                    public void run() {
                                        signUpButton.setVisibility(View.VISIBLE);
                                        progressBar.setVisibility(View.GONE);
                                    }
                                }, 500);
                                handler.post(new Runnable() {
                                    @Override
                                    public void run() {
                                        AuthTracker.trackSignUp(false);
                                    }
                                });
                                final int responseCode = t.getCode(mContext);
                                switch (responseCode) {
                                    case 400:
                                        Dialog.makeToast(mContext, getString(R.string.username_taken));
                                        break;
                                    default:
                                        Dialog.makeToast(mContext, getString(R.string.error_has_occured));
                                        break;
                                }
                            }
                        });
                    } else {
                        Dialog.makeToast(mContext, getString(Form.getSimpleMessage(form.getReason())));
                    }

                } else {
                    Dialog.makeToast(mContext, getString(R.string.no_network));
                }
            }
        });
    }

    private void setupLoginListener() {
        loginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                username = usernameField.getText().toString();
                password = passwordField.getText().toString();
                if (NetworkState.isConnected(mContext)) {
                    Form form = new LoginForm(username, password).validate();
                    if (form.isValid()) {
                        handler.postDelayed(new Runnable() {
                            @Override
                            public void run() {
                                progressBar.setVisibility(View.VISIBLE);
                                loginButton.setVisibility(View.GONE);
                            }
                        }, 500);
                        APIInterface apiManager = ApiManager.getApiService();
                        Call<UserResponse> call = apiManager.getUser(
                                new AuthData(username, password)
                        );
                        ApiManager.request(call, new NetworkCallback<UserResponse>() {
                            @Override
                            public void onSuccess(UserResponse response) {
                                handler.postDelayed(new Runnable() {
                                    @Override
                                    public void run() {
                                        progressBar.setVisibility(View.GONE);
                                        loginButton.setVisibility(View.VISIBLE);
                                    }
                                }, 500);
                                loginUser(response, false);
                            }

                            @Override
                            public void onFail(FailedRequestResponse error) {
                                eLog.e(TAG, error.getMessage());
                                handler.postDelayed(new Runnable() {
                                    @Override
                                    public void run() {
                                        progressBar.setVisibility(View.GONE);
                                        loginButton.setVisibility(View.VISIBLE);
                                    }
                                }, 500);
                                handler.post(new Runnable() {
                                    @Override
                                    public void run() {
                                        AuthTracker.trackLogin(false);
                                    }
                                });
                                final int responseCode = error.getCode(mContext);
                                switch (responseCode) {
                                    case 401:
                                        Dialog.makeToast(mContext, getString(R.string.account_disabled));
                                        break;
                                    case 404:
                                        Dialog.makeToast(mContext, getString(R.string.user_not_found));
                                        break;
                                    default:
                                        Dialog.makeToast(mContext, getString(R.string.error_has_occured));
                                        break;
                                }
                            }
                        });
                    } else {
                        Dialog.makeToast(mContext, getString(Form.getSimpleMessage(form.getReason())));
                    }
                } else {
                    Dialog.makeToast(mContext, getString(R.string.no_network));
                }
            }
        });
    }

    private void loginUser(UserResponse response, final boolean isSignUp) {
        User user = response.getData();
        SessionManager.newSession(mContext, user);
        handler.post(new Runnable() {
            @Override
            public void run() {
                if(isSignUp){
                    AuthTracker.trackSignUp(true);
                }else{
                    AuthTracker.trackLogin(true);
                }
            }
        });
        Dialog.makeToast(mContext, getString(R.string.hello) + " " + response.getData().getUsername() + "!");
        startActivity(intent);
        finish();
    }
}
