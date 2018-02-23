package com.jetlightstudio.jettunes;

/**
 * Created by oussama on 24/11/2017.
 */

public class Song {
    private long mSongID;
    private String mSongTitle;
    private String mSongArtist;

    public Song(long id, String currentTitle, String artist){
        mSongID = id;
        mSongTitle = currentTitle;
        mSongArtist = artist;
    }

    public long getSongID(){
        return mSongID;
    }

    public String getSongTitle(){
        return mSongTitle;
    }
}
