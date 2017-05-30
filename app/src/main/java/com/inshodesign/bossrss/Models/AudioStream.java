package com.inshodesign.bossrss.Models;

/**
 * Created by JClassic on 3/8/2017.
 */

public class AudioStream {

    private Integer rowid;
    private String path;
    private Boolean play;
    private Integer mediaPosition;

    public AudioStream() {}

    public AudioStream(Integer rowId, String path, Boolean play, Integer position) {
        this.rowid = rowId;
        this.play = play;
        this.path = path;
        this.mediaPosition = position;
    }

    public Integer getRowid() {
        return rowid;
    }

    public void setRowid(Integer rowid) {
        this.rowid = rowid;
    }

    public void setPlay(Boolean play) {
        this.play = play;
    }

    public void setMediaPosition(Integer mediaPosition) {
        this.mediaPosition = mediaPosition;
    }

    public Boolean getPlay() {
        return play;
    }

    public Integer getMediaPosition() {
        return mediaPosition;
    }

    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }
}
