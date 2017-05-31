package com.inshodesign.bossrss.XML_Models;


import android.os.Parcel;
import android.os.Parcelable;

import org.simpleframework.xml.Attribute;
import org.simpleframework.xml.Root;

@Root(name = "enclosure", strict = false)
public class ItemEnclosure implements Parcelable {

    public ItemEnclosure(){}

    @Attribute(name = "url", required = false)
    private String url;
    @Attribute(name = "length", required = false)
    private String length;
    @Attribute(name = "type", required = false)
    private String type;

    //Getters and setters
    public String getUrl() {return url;}
    public Integer getLength() {
        try {
            return Integer.parseInt(length);
        } catch (NullPointerException e) {
          return 0;
        }
    }
    public String getType() {return type;}

    // Parcelling part
    private ItemEnclosure(Parcel in){

        String[] datafirst = new String[3];
        in.readStringArray(datafirst);
        this.url = datafirst[0];
        this.length = datafirst[1];
        this.type = datafirst[2];
    }

    public int describeContents(){
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeStringArray(new String[] {this.url,
                this.length,
                this.type});

    }

    public static final Parcelable.Creator<ItemEnclosure> CREATOR
            = new Parcelable.Creator<ItemEnclosure>() {
        public ItemEnclosure createFromParcel(Parcel in) {
            return new ItemEnclosure(in);
        }

        public ItemEnclosure[] newArray(int size) {
            return new ItemEnclosure[size];
        }
    };
}