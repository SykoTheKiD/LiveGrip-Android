package com.jaysyko.wrestlechat.activities;

import android.app.Fragment;
import android.os.Bundle;
import android.text.Html;
import android.view.MenuItem;

import com.jaysyko.wrestlechat.R;
import com.jaysyko.wrestlechat.fragments.WebViewLoaderFragment;

public class WebViewActivity extends BaseActivity {

    @Override
    protected Fragment createFragment() {
        return new WebViewLoaderFragment();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_web_view);
        android.support.v7.app.ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);
            actionBar.setSubtitle(Html.fromHtml("<font color='#FFBF00' size='1'>WrestleChat 2.0</font>"));
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                finish();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }
}
