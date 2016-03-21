package com.jaysyko.wrestlechat.activities;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.android.volley.Request;
import com.jaysyko.wrestlechat.R;
import com.jaysyko.wrestlechat.auth.CurrentActiveUser;
import com.jaysyko.wrestlechat.dialogs.Dialog;
import com.jaysyko.wrestlechat.forms.Form;
import com.jaysyko.wrestlechat.forms.formValidators.LoginValidator;
import com.jaysyko.wrestlechat.forms.formValidators.SignUpValidator;
import com.jaysyko.wrestlechat.network.NetworkCallback;
import com.jaysyko.wrestlechat.auth.UserJSONKeys;
import com.jaysyko.wrestlechat.network.NetworkRequest;
import com.jaysyko.wrestlechat.network.NetworkResponse;
import com.jaysyko.wrestlechat.network.NetworkSingleton;
import com.jaysyko.wrestlechat.network.NetworkState;
import com.jaysyko.wrestlechat.network.RESTEndpoints;
import com.jaysyko.wrestlechat.utils.IntentKeys;
import com.jaysyko.wrestlechat.utils.StringResources;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;

public class LoginActivity extends AppCompatActivity {

    private static final String TAG = LoginActivity.class.getSimpleName();
    private String username, password;
    private boolean signIn = true;
    private Handler handler = new Handler();
    private Button loginButton;
    private Button signUpButton;
    private TextView signUpText;
    private TextView signUpPrompt;
    private EditText usernameField;
    private Context mContext;
    private EditText passwordField;
    private Intent intent;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        // Redirect to Events page if logged in;
        mContext = getApplicationContext();
        intent = new Intent(mContext, EventListActivity.class);
        if (CurrentActiveUser.getInstance() != null) {
            startActivity(intent);
            finish();
        } else {
            loginButton = (Button) findViewById(R.id.sign_in_button);
            signUpButton = (Button) findViewById(R.id.sign_up_button);
            signUpText = (TextView) findViewById(R.id.sign_up_text_view);
            signUpPrompt = (TextView) findViewById(R.id.sign_in_prompt);
            usernameField = (EditText) findViewById(R.id.username_text_view);
            usernameField.setText(getIntent().getStringExtra(IntentKeys.USERNAME));
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
            signUpPrompt.setText(getString(R.string.sign_up_to_app));
        } else {
            loginButton.setVisibility(View.VISIBLE);
            signUpButton.setVisibility(View.GONE);
            signUpText.setText(R.string.no_account_dialog);
            signUpPrompt.setText(getString(R.string.sign_in_to_app));
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
                    Form form = new SignUpValidator(username, password).validate();
                    if (form.isValid()) {
                        HashMap<String, String> params = new HashMap<>();
                        params.put(UserJSONKeys.USERNAME.toString(), username);
                        params.put(UserJSONKeys.PASSWORD.toString(), password);
                        Request request = new NetworkRequest(new NetworkCallback() {
                            @Override
                            public void onSuccess(String response) {
                                    NetworkResponse networkResponse = new NetworkResponse(response);
                                    if (networkResponse.isSuccessful()) {
                                        Dialog.makeToast(mContext, getString(R.string.user_created));
                                        passwordField.setText(StringResources.NULL_TEXT);
                                        changeMode();
                                    } else {
                                        Dialog.makeToast(mContext, getString(R.string.username_taken));
                                    }
                            }
                        }).post(RESTEndpoints.SIGN_UP, params);
                        NetworkSingleton.getInstance(mContext).addToRequestQueue(request);
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
                    Form form = new LoginValidator(username, password).validate();
                    if (form.isValid()) {
                        HashMap<String, String> params = new HashMap<>();
                        params.put(UserJSONKeys.USERNAME.toString(), username);
                        params.put(UserJSONKeys.PASSWORD.toString(), password);
                        Request request = new NetworkRequest(new NetworkCallback() {
                            @Override
                            public void onSuccess(String response) {
                                NetworkResponse networkResponse = new NetworkResponse(response);
                                if (networkResponse.isSuccessful()) {
                                    String id = null, profileImageURL = null;
                                    try {
                                        JSONObject userJSON = (JSONObject) networkResponse.getPayload().get(0);
                                        id = userJSON.getString(UserJSONKeys.ID.toString());
                                        profileImageURL = userJSON.getString(UserJSONKeys.PROFILE_IMAGE.toString());
                                    } catch (JSONException e) {
                                        e.printStackTrace();
                                    }
                                    CurrentActiveUser.getInstance(id, username, password);
                                    CurrentActiveUser.getInstance().setProfileImageURL(profileImageURL);
                                        Dialog.makeToast(mContext, getString(R.string.welcome_back).concat(StringResources.BLANK_SPACE).concat(username));
                                        startActivity(intent);
                                        finish();
                                    } else {
                                        Dialog.makeToast(mContext, getString(R.string.incorrect_login_info));
                                    }
                            }
                        }).post(RESTEndpoints.LOGIN, params);
                        NetworkSingleton.getInstance(mContext).addToRequestQueue(request);
                    } else {
                        Dialog.makeToast(mContext, getString(Form.getSimpleMessage(form.getReason())));
                    }
                } else {
                    Dialog.makeToast(mContext, getString(R.string.no_network));
                }
            }
        });
    }
}
