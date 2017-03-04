package com.inshodesign.bossrss.XMLModel;


        import android.graphics.Bitmap;
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
    private Bitmap image;
    private String imageURL;
    private String imageURI;
    private String URL;

    public RSSList(Integer _id) {
     }
    public RSSList(Integer _id, String title, Bitmap _image, String _imageURL, String _imageURI, String _URL) {
        this.id = _id;
        this.title = title;
        this.image = _image;
        this.imageURL = _imageURL;
        this.imageURI = _imageURI;
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

    public Bitmap getImage() {
        return image;
    }

    public boolean hasImage() {
        return (image != null);
    }

    public void setImage(Bitmap image) {
        this.image = image;
    }

    public String getURL() {
        return URL;
    }

    public void setURL(String URL) {
        this.URL = URL;}

    public boolean hasURL() {
        return (URL != null);
    }

    public String getImageURL() {
        return imageURL;
    }

    public void setImageURL(String imageURL) {
        this.imageURL = imageURL;
    }

    public String getImageURI() {
        return imageURI;
    }

    public void setImageURI(String imageURI) {
        this.imageURI = imageURI;
    }
}