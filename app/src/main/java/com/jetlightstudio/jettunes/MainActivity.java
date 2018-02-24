package com.jetlightstudio.jettunes;

import android.app.Activity;
import android.content.ContentResolver;
import android.content.ContentUris;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.View;
import android.support.v7.app.AppCompatActivity;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.Random;

public class MainActivity extends AppCompatActivity {

    TextView text;
    Button playButton;
    Button shuffleButton;
    Button repeatButton;
    MediaPlayer mp;
    ArrayList<Song> songs;
    HashMap<Integer, Song> songsMap;
    ListView listView;
    Uri currentURI;
    int currentIndex = 0;
    boolean shuffle = false;
    boolean repeatAll = true;

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
        shuffleButton = (Button) findViewById(R.id.shuffle);
        repeatButton = (Button) findViewById(R.id.repeat);
        text = (TextView) findViewById(R.id.songTitle);
        songs = new ArrayList<>();
        songsMap = new HashMap<>();
        fillMusic();
    }

    public void setCurrentURI(int index) {
        String songTitle = songsMap.get(index).getSongTitle();
        System.out.println(songsMap.get(index).getSongTitle() + "\n" + index);
        currentURI = ContentUris.withAppendedId(
                android.provider.MediaStore.Audio.Media.EXTERNAL_CONTENT_URI,
                songsMap.get(index).getmSongID());
        currentIndex = index;
        text.setText(songTitle);
    }

    public void startMusic(final View v) {
        mp.stop();
        mp = MediaPlayer.create(this, currentURI);
        playButton.setBackgroundResource(R.drawable.ic_pause_circle_outline_white_24dp);
        if (mp != null) {
            mp.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
                @Override
                public void onCompletion(MediaPlayer mediaPlayer) {
                    if (repeatAll) {
                        if (currentIndex >= songs.size() - 1) {
                            currentIndex = 0;
                            startMusic(v);
                        } else {
                            nextSong(v);
                        }
                    } else {
                        startMusic(v);
                    }
                }
            });
            mp.start();
        }
    }

    public void nextSong(View view) {
        setCurrentURI(!shuffle ? currentIndex + 1 : new Random().nextInt(songs.size()));
        startMusic(view);
    }

    public void prevSong(View view) {
        setCurrentURI(!shuffle ? currentIndex - 1 : new Random().nextInt(songs.size()));
        startMusic(view);
    }

    public void shuffleSong(View view) {
        shuffle = !shuffle;
        Toast.makeText(this, shuffle ? "Shuffle On" : "Shuffle Off", Toast.LENGTH_SHORT).show();
        shuffleButton.setBackgroundResource(shuffle ? R.drawable.ic_shuffle_white_24dp : R.drawable.ic_shuffle_black_24dp);
    }

    public void repeatSong(View view) {
        repeatAll = !repeatAll;
        repeatButton.setBackgroundResource(repeatAll ? R.drawable.ic_repeat_white_24dp : R.drawable.ic_repeat_one_white_24dp);
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
            int songDuration = songCursor.getColumnIndex(MediaStore.Audio.Media.DURATION);
            int songAlbum = songCursor.getColumnIndex(MediaStore.Audio.Media.ALBUM);
            do {
                int currentId = songCursor.getInt(songId);
                String currentTitle = songCursor.getString(songTitle);
                String currentArtist = songCursor.getString(songArtist);
                String currentDuration = songCursor.getString(songDuration);
                int currentAlbum = songCursor.getInt(songAlbum);

                songs.add(new Song(currentId, currentTitle, currentArtist, currentDuration, currentAlbum));
                songsMap.put(songs.size() - 1, songs.get(songs.size() - 1));
            } while (songCursor.moveToNext());
            Collections.reverse(songs);
            for (int i = songs.size() - 1; i >= 0; i--) {
                titles.add(songs.get(i).getSongTitle());
            }
        }

        songCursor.close();
        CustumAdapter c = new CustumAdapter(songsMap);
        ArrayAdapter<String> a = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, titles);
        listView.setAdapter(c);
    }

    @Override
    public void grantUriPermission(String toPackage, Uri uri, int modeFlags) {
        super.grantUriPermission(toPackage, uri, modeFlags);
    }


    public class CustumAdapter extends BaseAdapter {

        HashMap<String, Song> songsMap;

        public CustumAdapter(HashMap songsMap) {
            this.songsMap = songsMap;
        }

        @Override
        public int getCount() {
            return songsMap.size();
        }

        @Override
        public Object getItem(int i) {
            return null;
        }

        @Override
        public long getItemId(int i) {
            return i;
        }

        @Override
        public View getView(int i, View view, ViewGroup viewGroup) {
            view = getLayoutInflater().inflate(R.layout.customadapter, null);
            ImageView imageView = (ImageView) view.findViewById(R.id.album);
            TextView title = (TextView) view.findViewById(R.id.title);
            TextView albumTitle = (TextView) view.findViewById(R.id.albumTitle);
            TextView duration = (TextView) view.findViewById(R.id.duration);

            imageView.setImageResource(songsMap.get(i).getIdAlbum());
            System.out.println(title == null);
            title.setText(songsMap.get(i).getSongTitle());
            albumTitle.setText(songsMap.get(i).getmSongAlbum());
            duration.setText(songsMap.get(i).getDuration());
            return view;
        }
    }
}
