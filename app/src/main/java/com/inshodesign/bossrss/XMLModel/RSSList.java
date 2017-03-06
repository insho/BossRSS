package com.inshodesign.bossrss.XMLModel;


import android.graphics.Bitmap;

public class RSSList {

    private Integer id;
    private String title;
    private String imageURL;
    private String imageURI;
    private Bitmap bitmap;
    private String URL;

    public RSSList(String _URL) {
        this.URL = _URL;
     }
    public RSSList(Integer _id, String title, String _imageURL, String _imageURI, Bitmap _bitmap, String _URL) {
        this.id = _id;
        this.title = title;
        this.imageURL = _imageURL;
        this.imageURI = _imageURI;
        this.bitmap = _bitmap;
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

    public boolean hasImageURL() {
        return (imageURL != null);
    }

    public String getImageURI() {
        return imageURI;
    }

    public void setImageURI(String imageURI) {
        this.imageURI = imageURI;
    }

    public boolean hasImageURI() {
        return (imageURI != null);
    }

    public Bitmap getBitmap() {
        return bitmap;
    }

    public void setBitmap(Bitmap bitmap) {
        this.bitmap = bitmap;
    }

    public boolean hasBitmap() {
        return (bitmap != null);
    }

}