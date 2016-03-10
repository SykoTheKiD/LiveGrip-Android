package com.jaysyko.wrestlechat.activities;

import android.app.Fragment;
import android.os.Bundle;

import com.jaysyko.wrestlechat.R;
import com.jaysyko.wrestlechat.fragments.UserProfileFragment;

public class UserProfileActivity extends BaseActivity {

    @Override
    protected Fragment createFragment() {
        return new UserProfileFragment();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_profile);

    }

}
