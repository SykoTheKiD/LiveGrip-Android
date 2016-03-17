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
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.jaysyko.wrestlechat.R;
import com.jaysyko.wrestlechat.auth.CurrentActiveUser;
import com.jaysyko.wrestlechat.dialogs.Dialog;
import com.jaysyko.wrestlechat.forms.Form;
import com.jaysyko.wrestlechat.forms.formValidators.LoginValidator;
import com.jaysyko.wrestlechat.network.NetworkState;
import com.jaysyko.wrestlechat.utils.DBConstants;
import com.jaysyko.wrestlechat.utils.IntentKeys;
import com.jaysyko.wrestlechat.utils.StringResources;

import java.util.HashMap;
import java.util.Map;

public class LoginActivity extends AppCompatActivity {

    private String username, password;
    private boolean signIn = true;
    private Handler handler = new Handler();

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
            final Button loginButton = (Button) findViewById(R.id.sign_in_button);
            final Button signUpButton = (Button) findViewById(R.id.sign_up_button);
            final TextView signUpText = (TextView) findViewById(R.id.sign_up_text_view);
            final TextView signUpPrompt = (TextView) findViewById(R.id.sign_in_prompt);
            final EditText usernameField = (EditText) findViewById(R.id.username_text_view);
            usernameField.setText(getIntent().getStringExtra(IntentKeys.USERNAME));
            final EditText passwordField = (EditText) findViewById(R.id.login_password_et);
            handler.post(new Runnable() {
                @Override
                public void run() {
                    setupLoginListener(context, intent, loginButton, usernameField, passwordField);
                    setupSignUpListener(context, intent, signUpButton, usernameField, passwordField);
                    setupSignUpTextListener(loginButton, signUpButton, signUpText, signUpPrompt);
                }
            });
        }
    }

    private void setupSignUpTextListener(final Button loginButton, final Button signUpButton, final TextView signUpText, final TextView signUpPrompt) {
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

    private void setupSignUpListener(final Context context, final Intent intent, final Button signUpButton, final EditText usernameField, final EditText passwordField) {
        signUpButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                username = usernameField.getText().toString();
                password = passwordField.getText().toString();
                RequestQueue queue = Volley.newRequestQueue(getApplicationContext());
                StringRequest stringRequest = new StringRequest(Request.Method.POST, DBConstants.MYSQL_URL.concat("/newuser.php"), new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        Log.e("K", response);
                    }
                }, new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Log.e("E", error.getMessage());
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
                queue.add(stringRequest);
                }
//                if (NetworkState.isConnected(context)) {
//                    Form form = new SignUpValidator(username, password).validate();
//                    if (form.isValid()) {
//                        if (CreateNewUser.signUpUser(getApplicationContext(), username, password)) {
//                            startActivity(intent);
//                            finish();
//                        } else {
//                            Dialog.makeToast(context, getString(R.string.username_taken));
//                        }
//                    } else {
//                        Dialog.makeToast(context, getString(Form.getSimpleMessage(form.getReason())));
//                    }
//                } else {
//                    Dialog.makeToast(context, getString(R.string.no_network));
//                }
        });
    }

    private void setupLoginListener(final Context context, final Intent intent, final Button loginButton, final EditText usernameField, final EditText passwordField) {
        loginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                username = usernameField.getText().toString();
                password = passwordField.getText().toString();
                if (NetworkState.isConnected(context)) {
                    Form form = new LoginValidator(username, password).validate();
                    if (form.isValid()) {
                        CurrentActiveUser currentActiveUser = CurrentActiveUser.getInstance(username, password);
                        if (currentActiveUser.loginUser(getParent(), username, password)) {
                            Dialog.makeToast(context, getString(R.string.welcome_back).concat(StringResources.BLANK_SPACE).concat(username));
                            startActivity(intent);
                            finish();
                        } else {
                            Dialog.makeToast(context, getString(R.string.incorrect_login_info));
                        }
                    } else {
                        Dialog.makeToast(context, getString(Form.getSimpleMessage(form.getReason())));
                    }
                } else {
                    Dialog.makeToast(context, getString(R.string.no_network));
                }
            }
        });
    }
}
