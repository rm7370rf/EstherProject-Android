package org.rm7370rf.estherproject;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;

import static org.rm7370rf.estherproject.R.string.topics;

public class TopicListActivity extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_topic_list);
        setTitle(topics);
    }

}
