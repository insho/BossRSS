package com.inshodesign.bossrss.XMLModel;


        import android.os.Parcel;
        import android.os.Parcelable;
        import android.support.annotation.NonNull;
        import android.support.annotation.Nullable;

        import java.sql.Blob;

/**
 * Created by JClassic on 3/3/2017.
 */


public class RSSList {

    private Integer id;
    private String title;
    private byte[] image;
    private String URL;

    public RSSList(Integer _id) {
     }
    public RSSList(Integer _id, String title, byte[] _image, String _URL) {
        this.id = _id;
        this.title = title;
        this.image = _image;
        this.URL = _URL;
    }

    public RSSList() {
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }


    public String getTitle() {
        return title;
    }

    public void setTitle(String name) {
        this.title = name;
    }

    public boolean hasTitle() {
        return (title != null);
    }

    public byte[] getImage() {
        return image;
    }

    public boolean hasImage() {
        return (image != null);
    }

    public void setImage(byte[] image) {
        this.image = image;
    }

    public String getURL() {
        return URL;
    }

    public void setURL(String URL) {
        this.URL = URL;
    }

    public boolean hasURL() {
        return (URL != null);
    }
}