package com.jetlightstudio.jettunes;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.TextView;

public class SongActivity extends AppCompatActivity {
    TextView songTitle;
    TextView currentTime;
    TextView songDuartion;
    TextView songIndex;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_song);
    }
}
