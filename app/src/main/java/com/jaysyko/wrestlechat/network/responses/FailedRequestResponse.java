package com.jaysyko.wrestlechat.network.responses;

import android.content.Context;
import android.content.Intent;
import android.os.Handler;

import com.jaysyko.wrestlechat.R;
import com.jaysyko.wrestlechat.activities.LoginActivity;
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
    }

    public int getCode(final Context context) {
        switch (this.code){
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
        }
        return this.code;
    }

    public String getMessage() {
        return this.message;
    }
}
