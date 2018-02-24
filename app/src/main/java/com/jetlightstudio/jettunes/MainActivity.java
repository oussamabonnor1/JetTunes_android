package com.jetlightstudio.jettunes;

import android.content.ContentResolver;
import android.content.ContentUris;
import android.content.Intent;
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
import java.util.HashMap;

public class MainActivity extends AppCompatActivity {

    TextView text;
    Button playButton;
    MediaPlayer mp;
    ArrayList<Song> songs;
    HashMap<String, Integer> songID;
    ListView listView;
    Uri currentURI;
    int currentIndex = 0;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_player);

        mp = new MediaPlayer();
        listView = (ListView) findViewById(R.id.listView);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                setCurrentURI(i);
                startMusic(view);
            }
        });
        playButton = (Button) findViewById(R.id.play);
        text = (TextView) findViewById(R.id.songTitle);
        songs = new ArrayList<>();
        songID = new HashMap<>();
        fillMusic();
    }

    public void setCurrentURI(int index) {
        String songTitle = listView.getItemAtPosition(index).toString();
        currentURI = ContentUris.withAppendedId(
                android.provider.MediaStore.Audio.Media.EXTERNAL_CONTENT_URI,
                songID.get(songTitle));
        currentIndex = index;
        text.setText(songTitle);
    }

    public void startMusic(View v) {
        mp.stop();
        mp = MediaPlayer.create(this, currentURI);
        playButton.setBackgroundResource(R.drawable.ic_pause_circle_outline_white_24dp);
        if (mp != null) mp.start();
    }

    public void nextSong(View view) {
        setCurrentURI(currentIndex + 1);
        startMusic(view);
    }

    public void prevSong(View view) {
        setCurrentURI(currentIndex - 1);
        startMusic(view);
    }

    public void goToSong(View view) {
        Intent i = new Intent(MainActivity.this, SongActivity.class);
        startActivity(i);
    }

    public void playMusic(View view) {
        if (mp != null) {
            if (mp.isPlaying()) {
                mp.pause();
                playButton.setBackgroundResource(R.drawable.ic_play_circle_outline_white_24dp);
            } else {
                mp.start();
                playButton.setBackgroundResource(R.drawable.ic_pause_circle_outline_white_24dp);
            }
        }
    }

    public void fillMusic() {
        ArrayList<String> titles = new ArrayList<>();
        ContentResolver contentResolver = getContentResolver();
        Uri songUri = MediaStore.Audio.Media.EXTERNAL_CONTENT_URI;
        Cursor songCursor = contentResolver.query(songUri, null, null, null, null);

        if (songCursor != null && songCursor.moveToFirst()) {
            int songId = songCursor.getColumnIndex(MediaStore.Audio.Media._ID);
            int songTitle = songCursor.getColumnIndex(MediaStore.Audio.Media.TITLE);
            int songArtist = songCursor.getColumnIndex(MediaStore.Audio.Media.ARTIST);
            int songAlbum = songCursor.getColumnIndex(MediaStore.Audio.Media.ALBUM);
            do {
                long currentId = songCursor.getLong(songId);
                String currentTitle = songCursor.getString(songTitle);
                String currentArtist = songCursor.getString(songArtist);
                String currentAlbum = songCursor.getString(songAlbum);

                songs.add(new Song(currentId, currentTitle, currentArtist, currentAlbum));
                songID.put(currentTitle, (int) currentId);

            } while (songCursor.moveToNext());
            for (int i = songs.size() - 1; i >= 0; i--) {
                titles.add(songs.get(i).getSongTitle());
            }
        }

        songCursor.close();
        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_expandable_list_item_1, titles);
        listView.setAdapter(adapter);
    }

    @Override
    public void grantUriPermission(String toPackage, Uri uri, int modeFlags) {
        super.grantUriPermission(toPackage, uri, modeFlags);
    }
}
