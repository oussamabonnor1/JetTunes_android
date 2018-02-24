package com.jetlightstudio.jettunes;

/**
 * Created by oussama on 24/11/2017.
 */

public class Song {
    private long mSongID;
    private String mSongTitle;
    private String mSongArtist;

    public Song(long mSongID, String mSongTitle, String mSongArtist, String mSongAlbum) {
        this.mSongID = mSongID;
        this.mSongTitle = mSongTitle;
        this.mSongArtist = mSongArtist;
        this.mSongAlbum = mSongAlbum;
    }

    public long getmSongID() {
        return mSongID;
    }

    public void setmSongID(long mSongID) {
        this.mSongID = mSongID;
    }

    public String getmSongTitle() {
        return mSongTitle;
    }

    public void setmSongTitle(String mSongTitle) {
        this.mSongTitle = mSongTitle;
    }

    public String getmSongArtist() {
        return mSongArtist;
    }

    public void setmSongArtist(String mSongArtist) {
        this.mSongArtist = mSongArtist;
    }

    public String getmSongAlbum() {
        return mSongAlbum;
    }

    public void setmSongAlbum(String mSongAlbum) {
        this.mSongAlbum = mSongAlbum;
    }

    private String mSongAlbum;



    public long getSongID(){
        return mSongID;
    }

    public String getSongTitle(){
        return mSongTitle;
    }
}
