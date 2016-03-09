package com.jaysyko.wrestlechat.activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;

import com.jaysyko.wrestlechat.R;
import com.jaysyko.wrestlechat.fragments.MessagingFragment;

public class MessagingActivity extends AppCompatActivity {


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_messaging);

        FragmentTransaction mFragmentTransaction = getSupportFragmentManager().beginTransaction();
        mFragmentTransaction.replace(R.id.container_view, new MessagingFragment()).commit();

    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.messaging_screen_menu, menu);
        return true;
    }

    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle item selection
        switch (item.getItemId()) {
            case R.id.action_event_info:
                Intent eventInfoIntent = new Intent(getApplicationContext(), EventInfoActivity.class);
                startActivity(eventInfoIntent);
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }
//
//    public void onDestroy() {
//    super.onDestroy();
//    closeAllThreads();
//}
//
//    public void onPause() {
//        super.onPause();
//        closeAllThreads();
//    }
//
//    public void onResume() {
//        super.onResume();
//        handler.post(fetchNewMessagesRunnable);
//    }

//    private void closeAllThreads() {
//        handler.removeCallbacks(fetchNewMessagesRunnable);
//    }
}