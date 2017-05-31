package com.inshodesign.bossrss.XML_Models;

import android.os.Parcel;
import android.os.Parcelable;

import org.simpleframework.xml.Attribute;
import org.simpleframework.xml.Root;

@Root(name = "thumbnail", strict = false)
public class ItemMediaContentThumbnail implements Parcelable {

    public ItemMediaContentThumbnail(){}

    @Attribute(required = false)
    private String url;
    @Attribute(required = false)
    private String medium;
    @Attribute(required = false)
    private String width;
    @Attribute(required = false)
    private String height;

    //Getters and setters
    public String getUrl() {
        return url;
    }


    // Parcelling part
    private ItemMediaContentThumbnail(Parcel in){
        String[] datafirst = new String[4];

        in.readStringArray(datafirst);
        this.url = datafirst[0];
        this.medium = datafirst[1];
        this.width = datafirst[2];
        this.height = datafirst[3];
    }

    public int describeContents(){
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {

        dest.writeStringArray(new String[] {this.url,
                this.medium,
                this.width,
                this.height});
    }

    public static final Parcelable.Creator<ItemMediaContentThumbnail> CREATOR
            = new Parcelable.Creator<ItemMediaContentThumbnail>() {
        public ItemMediaContentThumbnail createFromParcel(Parcel in) {
            return new ItemMediaContentThumbnail(in);
        }

        public ItemMediaContentThumbnail[] newArray(int size) {
            return new ItemMediaContentThumbnail[size];
        }
    };

}