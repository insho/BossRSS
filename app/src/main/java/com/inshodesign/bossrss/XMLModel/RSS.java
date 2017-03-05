package com.inshodesign.bossrss.XMLModel;

/**
 * Created by JClassic on 3/3/2017.
 */

import android.os.Parcel;
import android.os.Parcelable;

import com.inshodesign.bossrss.XMLModel.Channel;

import org.simpleframework.xml.Attribute;
import org.simpleframework.xml.Element;
import org.simpleframework.xml.Root;

@Root
public class RSS {

    @Attribute
    String version;

    @Element
    Channel channel;


    public Channel getChannel() {
        return channel;
    }

    @Override
    public String toString() {
        return "RSS{" +
                "version='" + version + '\'' +
                ", channel=" + channel +
                '}';
    }

//    // Parcelling part
//    public RSS(Parcel in){
//        String[] data = new String[2];
//
//        in.readStringArray(data);
//        this.version = data[0];
//        this.channel = data[1];
//
//
//    }
//
//    public int describeContents(){
//        return 0;
//    }
//
//    @Override
//    public void writeToParcel(Parcel dest, int flags) {
//        dest.writeStringArray(new String[] {this.url,
//                this.count_type,
//                this.column,
//                this.section,
//                this.byline,
//                this.title,
//                this.published_date,
//                this.source
//
//        });
//    }
//    public static final Parcelable.Creator CREATOR = new Parcelable.Creator() {
//        public RSS createFromParcel(Parcel in) {
//            return new RSS(in);
//        }
//
//        public RSS[] newArray(int size) {
//            return new RSS[size];
//        }
//    };

}
