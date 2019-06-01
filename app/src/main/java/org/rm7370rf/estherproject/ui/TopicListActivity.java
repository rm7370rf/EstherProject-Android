package org.rm7370rf.estherproject.ui;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;

import org.rm7370rf.estherproject.R;

import static org.rm7370rf.estherproject.R.string.topics;

public class TopicListActivity extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_topic_list);
        setTitle(topics);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_topic_list, menu);
        return true;
    }

    @Override
    public boolean onPrepareOptionsMenu(Menu menu) {
        return super.onPrepareOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.accountData:
                Log.d("MENU", "ACCOUNT_DATA");
                return true;
            case R.id.setUsername:
                Log.d("MENU", "SET_USERNAME");
                return true;
            case R.id.logout:
                Log.d("MENU", "LOGOUT");
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

}
