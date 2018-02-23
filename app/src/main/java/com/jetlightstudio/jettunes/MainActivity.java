package com.jetlightstudio.jettunes;

import android.content.ContentResolver;
import android.database.Cursor;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.View;
import android.support.v7.app.AppCompatActivity;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    TextView text;
    Button playButton;
    Button pauseButton;
    MediaPlayer mp;
    ArrayList<Song> songs;
    ListView listView;
    int currentID = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.content_main);

        listView = (ListView) findViewById(R.id.listView);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                currentID = Integer.valueOf(getTitle().toString());
                playMusic(view);
            }
        });
        playButton = (Button) findViewById(R.id.play);
        text = (TextView) findViewById(R.id.text);
        pauseButton = (Button) findViewById(R.id.pause);
        songs = new ArrayList<>();
        fillMusic();
        mp = null;
    }


    public void playMusic(View view) {
        if (mp != null) mp.stop();
        mp = MediaPlayer.create(this, currentID);
        mp.start();
    }

    public void pauseMusic(View view) {
        mp.pause();
    }

    public void fillMusic() {
        ArrayList<Long> titles = new ArrayList<>();
        ContentResolver contentResolver = getContentResolver();
        Uri songUri = MediaStore.Audio.Media.EXTERNAL_CONTENT_URI;
        Cursor songCursor = contentResolver.query(songUri, null, null, null, null);

        if (songCursor != null && songCursor.moveToFirst()) {
            int songId = songCursor.getColumnIndex(MediaStore.Audio.Media._ID);
            int songTitle = songCursor.getColumnIndex(MediaStore.Audio.Media.TITLE);
            int songArtist = songCursor.getColumnIndex(MediaStore.Audio.Media.ARTIST);

            do {
                long currentId = songCursor.getLong(songId);
                String currentTitle = songCursor.getString(songTitle);
                String currentArtist = songCursor.getString(songArtist);
                songs.add(new Song(currentId, currentTitle, currentArtist));
            } while (songCursor.moveToNext());
            for (int i = 0; i < songs.size(); i++) {
                titles.add(songs.get(i).getSongID());
            }
        }
        songCursor.close();
        ArrayAdapter<Long> adapter = new ArrayAdapter<>(this, android.R.layout.simple_expandable_list_item_1, titles);
        listView.setAdapter(adapter);
    }

    @Override
    public void grantUriPermission(String toPackage, Uri uri, int modeFlags) {
        super.grantUriPermission(toPackage, uri, modeFlags);
    }
}
