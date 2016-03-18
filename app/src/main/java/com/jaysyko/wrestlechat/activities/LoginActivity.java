package com.jaysyko.wrestlechat.activities;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.jaysyko.wrestlechat.R;
import com.jaysyko.wrestlechat.auth.CurrentActiveUser;
import com.jaysyko.wrestlechat.db.BackEnd;
import com.jaysyko.wrestlechat.dialogs.Dialog;
import com.jaysyko.wrestlechat.forms.Form;
import com.jaysyko.wrestlechat.forms.formValidators.LoginValidator;
import com.jaysyko.wrestlechat.forms.formValidators.SignUpValidator;
import com.jaysyko.wrestlechat.network.NetworkState;
import com.jaysyko.wrestlechat.utils.DBConstants;
import com.jaysyko.wrestlechat.utils.IntentKeys;
import com.jaysyko.wrestlechat.utils.StringResources;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class LoginActivity extends AppCompatActivity {

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
                        StringRequest stringRequest = new StringRequest(
                                Request.Method.POST,
                                DBConstants.MYSQL_URL.concat("newuser.php"),
                                new Response.Listener<String>() {
                                    @Override
                                    public void onResponse(String response) {
                                        try {
                                            JSONObject jsonObject = new JSONObject(response);
                                            boolean successful = jsonObject.getBoolean("success");
                                            if (successful) {
                                                Dialog.makeToast(mContext, getString(R.string.user_created));
                                                passwordField.setText(StringResources.NULL_TEXT);
                                                changeMode();
                                            } else {
                                                Dialog.makeToast(mContext, getString(R.string.username_taken));
                                            }
                                        } catch (JSONException e) {
                                            e.printStackTrace();
                                        }
                                    }
                                }, new Response.ErrorListener() {
                            @Override
                            public void onErrorResponse(VolleyError error) {
                                Log.e("ERR", error.getMessage());
                            }
                        }) {
                            @Override
                            protected Map<String, String> getParams() {
                                HashMap<String, String> params = new HashMap<>();
                                params.put("username", username);
                                params.put("password", password);
                                return params;
                            }
                        };
                        new BackEnd(mContext).execute(stringRequest);
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
                        StringRequest stringRequest = new StringRequest(
                                Request.Method.POST,
                                DBConstants.MYSQL_URL.concat("login.php"),
                                new Response.Listener<String>() {
                                    @Override
                                    public void onResponse(String response) {
                                        try {
                                            JSONObject jsonObject = new JSONObject(response);
                                            boolean successful = jsonObject.getBoolean("success");
                                            Log.e("RESP", response);
                                            if (successful) {
                                                CurrentActiveUser.getInstance(username, password);
                                                Dialog.makeToast(mContext, getString(R.string.welcome_back).concat(StringResources.BLANK_SPACE).concat(username));
                                                startActivity(intent);
                                                finish();
                                            } else {
                                                Dialog.makeToast(mContext, getString(R.string.incorrect_login_info));
                                            }
                                        } catch (JSONException e) {
                                            e.printStackTrace();
                                        }
                                    }
                                }, new Response.ErrorListener() {
                            @Override
                            public void onErrorResponse(VolleyError error) {
                                Log.e("ERR", error.getMessage());
                            }
                        }) {
                            @Override
                            protected Map<String, String> getParams() {
                                HashMap<String, String> params = new HashMap<>();
                                params.put("username", username);
                                params.put("password", password);
                                return params;
                            }
                        };
                        new BackEnd(mContext).execute(stringRequest);
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
