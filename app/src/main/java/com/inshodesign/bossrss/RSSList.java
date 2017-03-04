package com.inshodesign.bossrss;


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
    private Integer rowNumber;
    private String name;
    private byte[] image;
    private String URL;

    public RSSList(Integer _id, Integer _rowNumber, String name, byte[] _image, String _URL) {
        this.id = _id;
        this.rowNumber = _rowNumber;
        this.name = name;
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

//    public Integer getRowNumber() {
//        return rowNumber;
//    }
//
//    public void setRowNumber(Integer rowNumber) {
//        this.rowNumber = rowNumber;
//    }


    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
//

    public byte[] getImage() {
        return image;
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
}