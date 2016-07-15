package com.jaysyko.wrestlechat.activities;

import android.app.Fragment;
import android.app.FragmentManager;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import com.jaysyko.wrestlechat.R;

public abstract class BaseActivity extends AppCompatActivity {

    protected abstract Fragment createFragment();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_generic);
        getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        FragmentManager fm = getFragmentManager();
        Fragment fragment = fm.findFragmentById(R.id.container_view);

        if (fragment == null) {
            fragment = createFragment();
            fm.beginTransaction()
                    .add(R.id.container_view, fragment)
                    .commit();
        }
    }
}
