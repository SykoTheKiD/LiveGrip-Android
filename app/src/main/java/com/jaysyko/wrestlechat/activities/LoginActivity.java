package com.jaysyko.wrestlechat.activities;

import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GoogleApiAvailability;
import com.google.android.gms.gcm.GoogleCloudMessaging;
import com.google.android.gms.iid.InstanceID;
import com.jaysyko.wrestlechat.R;
import com.jaysyko.wrestlechat.auth.CurrentActiveUser;
import com.jaysyko.wrestlechat.dialogs.Dialog;
import com.jaysyko.wrestlechat.forms.Form;
import com.jaysyko.wrestlechat.forms.formTypes.LoginForm;
import com.jaysyko.wrestlechat.forms.formTypes.SignUpForm;
import com.jaysyko.wrestlechat.network.ApiInterface;
import com.jaysyko.wrestlechat.network.ApiManager;
import com.jaysyko.wrestlechat.network.NetworkCallback;
import com.jaysyko.wrestlechat.network.NetworkState;
import com.jaysyko.wrestlechat.network.UserData;
import com.jaysyko.wrestlechat.network.responses.UserResponse;
import com.jaysyko.wrestlechat.utils.StringResources;

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
    private boolean signIn = true;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        // Redirect to Events page if logged in;
        mContext = getApplicationContext();
        intent = new Intent(mContext, EventListActivity.class);
        loginButton = (Button) findViewById(R.id.sign_in_button);
        signUpButton = (Button) findViewById(R.id.sign_up_button);
        signUpText = (TextView) findViewById(R.id.sign_up_text_view);
        usernameField = (EditText) findViewById(R.id.username_text_view);
        usernameField.setText(getIntent().getStringExtra(USERNAME_INTENT_KEY));
        passwordField = (EditText) findViewById(R.id.login_password_et);
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
                    if (isPlayServicesInstalled()) {
                        Form form = new SignUpForm(username, password).validate();
                        if (form.isValid()) {
                            ApiInterface apiManager = ApiManager.getApiService();
                            Call<UserResponse> call = apiManager.createUser(
                                    new UserData(
                                            username, password
                                    )
                            );
                            ApiManager.request(call, new NetworkCallback<UserResponse>() {
                                @Override
                                public void onSuccess(UserResponse response) {
                                    getDeviceIDForGCM();
                                    loginUser(response);
                                }
                                @Override
                                public void onFail(String t) {
                                    Log.e(TAG, t);
                                    Dialog.makeToast(mContext, t);
                                }
                            });
                        } else {
                            Dialog.makeToast(mContext, getString(Form.getSimpleMessage(form.getReason())));
                        }
                    } else {
                        Dialog.makeToast(mContext, getString(R.string.no_play_services));
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
                        ApiInterface apiManager = ApiManager.getApiService();
                        Call<UserResponse> call = apiManager.getUser(
                                new UserData(
                                    username, password
                                )
                        );
                        ApiManager.request(call, new NetworkCallback<UserResponse>() {
                            @Override
                            public void onSuccess(UserResponse response) {
                                    loginUser(response);
                            }
                            @Override
                            public void onFail(String t) {
                                Log.e(TAG, t);
                                Dialog.makeToast(mContext, t);
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

    private void loginUser(UserResponse response) {
        CurrentActiveUser.newInstance(response.getData());
        Dialog.makeToast(mContext, getString(R.string.welcome_back).concat(StringResources.BLANK_SPACE).concat(username));
        startActivity(intent);
        finish();
    }

    //code to check Google play services availability.
    private boolean isPlayServicesInstalled() {
        GoogleApiAvailability getGoogleAvailability = GoogleApiAvailability.getInstance();
        int availabilityCode = getGoogleAvailability.isGooglePlayServicesAvailable(this);
        if (availabilityCode != ConnectionResult.SUCCESS) {
            if (getGoogleAvailability.isUserResolvableError(availabilityCode)) {
                getGoogleAvailability.getErrorDialog(this, availabilityCode, 9000).show();
            } else {
                Log.i(TAG, "Google Play Services not detected");
                finish();
            }
            return false;
        }
        return true;
    }

    public void getDeviceIDForGCM() {
        new AsyncTask<Void, Void, String>() {
            @Override
            protected String doInBackground(Void... taskParams) {
                final StringBuilder token = new StringBuilder();
                try {
                    InstanceID gcmTokenInstanceID = InstanceID.getInstance(mContext);
                    //we will get gcm_defaultSenderId by applying plugin: 'com.google.gms.google-services'
                    token.append(gcmTokenInstanceID.getToken(getString(R.string.gcm_defaultSenderId), GoogleCloudMessaging.INSTANCE_ID_SCOPE, null));
                } catch (Exception e) {
                    Log.e(TAG, "GCM Registration Token: " + e.getMessage());
                }
                //Keep this token securely, we use this to send message, refer in URL we have added this ID as parameter
                return token.toString();
            }
        }.execute(null, null, null);
    }
}
