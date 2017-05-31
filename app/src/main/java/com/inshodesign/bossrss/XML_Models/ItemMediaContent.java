package com.inshodesign.bossrss.XML_Models;

import android.os.Parcel;
import android.os.Parcelable;

import org.simpleframework.xml.Attribute;
import org.simpleframework.xml.Element;
import org.simpleframework.xml.Root;

/**
 * Object within an RSS feed item ({@link Item}), with links to
 * media content for the item
 */
@Root(name = "content", strict=false)
public class ItemMediaContent implements Parcelable {

    public ItemMediaContent(){}

    @Attribute(name = "url", required = false)
    private String url;
    @Attribute(required = false)
    private String medium;
    @Element(name = "thumbnail", required = false)
    private ItemMediaContentThumbnail thumbnail;
    @Element(name = "description", required = false)
    private String description;

    //Getters and setters
    public ItemMediaContentThumbnail getThumbnail() {
        return thumbnail;
    }
    public String getUrl() {
        return url;
    }
    public String getDescription() {
        return description;
    }


    // Parcelling part
    private ItemMediaContent(Parcel in){

        thumbnail = in.readParcelable(getClass().getClassLoader());

        String[] datafirst = new String[3];
        in.readStringArray(datafirst);
        this.url = datafirst[0];
        this.medium = datafirst[1];
        this.description = datafirst[2];


    }

    public int describeContents(){
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {

        dest.writeParcelable(this.thumbnail,flags);
        dest.writeStringArray(new String[] {this.url,
                this.medium,
                this.description});

    }

    public static final Parcelable.Creator<ItemMediaContent> CREATOR
            = new Parcelable.Creator<ItemMediaContent>() {
        public ItemMediaContent createFromParcel(Parcel in) {
            return new ItemMediaContent(in);
        }

        public ItemMediaContent[] newArray(int size) {
            return new ItemMediaContent[size];
        }
    };
}