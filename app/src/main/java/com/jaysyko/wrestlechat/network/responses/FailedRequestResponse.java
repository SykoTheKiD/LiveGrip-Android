package com.jaysyko.wrestlechat.network.responses;

import android.content.Context;
import android.content.Intent;
import android.os.Handler;

import com.jaysyko.wrestlechat.R;
import com.jaysyko.wrestlechat.activities.LoginActivity;
import com.jaysyko.wrestlechat.application.Initializer;
import com.jaysyko.wrestlechat.dialogs.Dialog;
import com.jaysyko.wrestlechat.sessionManager.SessionManager;

/**
 * @author Jay Syko on 2016-07-20.
 */
public class FailedRequestResponse {

    private int code;
    private String message;
    private final Handler handler = new Handler();

    public FailedRequestResponse(int code, String message) {
        this.code = code;
        this.message = message;
        checkSessionValid();
    }

    public int getCode() {
        return this.code;
    }

    public String getMessage() {
        return this.message;
    }

    private void checkSessionValid(){
        final Context context = Initializer.getAppContext();
        final int code = getCode();
        switch (code){
            case 401:
                Dialog.makeToast(context, context.getString(R.string.account_disabled));
                break;
            case 403:
                handler.post(new Runnable() {
                    @Override
                    public void run() {
                        SessionManager.destroySession(context);
                        final Intent intent = new Intent(context, LoginActivity.class);
                        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                        context.startActivity(intent);
                        Dialog.makeToast(context, context.getString(R.string.relogin));
                    }
                });
                break;
            case -1:
                Dialog.makeToast(context, context.getString(R.string.server_down));
        }
        if(code <= 400 && code >= 500){
            Dialog.makeToast(context, context.getString(R.string.error_has_occurred));
        }
    }
}
