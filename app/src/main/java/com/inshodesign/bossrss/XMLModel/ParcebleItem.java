package com.inshodesign.bossrss.XMLModel;

/**
 * Created by JClassic on 3/5/2017.
 */
import android.os.Parcel;
import android.os.Parcelable;
import android.util.Log;

//import org.simpleframework.xml.Attribute;
import org.simpleframework.xml.Element;
import org.simpleframework.xml.ElementList;
import org.simpleframework.xml.Namespace;
import org.simpleframework.xml.NamespaceList;
import org.simpleframework.xml.Root;
import org.simpleframework.xml.Text;

import java.util.ArrayList;

public class ParcebleItem implements Parcelable {


//        public ArrayList<MediaContent> mediaContentList = new ArrayList<MediaContent>();
//        public ArrayList<MediaThumbnail> mediaThumbnailList = new ArrayList<MediaThumbnail>();

        private String title;//The title of the item.	Venice Film Festival Tries to Quit Sinking
    private String link;//The URL of the item.	http://www.nytimes.com/2002/09/07/movies/07FEST.html
    private String description;//The item synopsis.	Some of the most heated chatter at the Venice Film Festival this week was about the way that the arrival of the stars at the Palazzo del Cinema was being staged.
    private String author;//Email address of the author of the item. More.	oprah@oxygen.net
    private String category;//Includes the item in one or more categories. More.	Simpsons Characters
    private String comments;//URL of a page for comments relating to the item. More.	http://www.myblog.org/cgi-local/mt/mt-comments.cgi?entry_id=290
    private  String enclosure;//	Describes a media object that is attached to the item. More.	<enclosure url="http://live.curry.com/mp3/celebritySCms.mp3" length="1069871" type="audio/mpeg"/>

    private String thumbnailURL;
//        MediaContent mediaContent;
//        MediaThumbnail mediaThumbnail;

    private String guid;//A string that uniquely identifies the item. More.	<guid isPermaLink="true">http://inessential.com/2002/09/01.php#a2</guid>
    private  String pubDate;//	Indicates when the item was published. More.	Sun, 19 May 2002 15:21:36 GMT
    private  String source;//	The RSS channel that the item came from. More.


        public ParcebleItem(Channel.Item item) {
            this.title = item.title;
            Log.d("TEST", "HAS THUMBNAIL? -- " + item.hasThumbnail());
            if(item.hasThumbnail()) {
//                Log.d("TEST","item thumnbail: " + item.getMediaContent().getThumbnail().getUrl());
                this.thumbnailURL = item.getThumbnail().getUrl();
            }

            this.link = item.link;
//            item.mediaThumbnail.url = this.mediaThumbnail.url;
        }

        public String getTitle() {
            return title;
        }

    public String getLink() {
        return link;
    }

    public String getAuthor() {
            return author;
        }

        public String getCategory() {
            return category;
        }

//        public MediaThumbnail getMediaThumbnail() {
//            return mediaThumbnail;
//        }


    public String getThumbnailURL() {
        return thumbnailURL;
    }

    // Parcelling part
        private  ParcebleItem(Parcel in){
            String[] datafirst = new String[10];

            in.readStringArray(datafirst);
            this.title = datafirst[0];
            this.link = datafirst[1];
            this.description = datafirst[2];
            this.author = datafirst[3];
            this.category = datafirst[4];
            this.comments = datafirst[5];
            this.enclosure = datafirst[6];

            this.guid = datafirst[0];
            this.pubDate = datafirst[1];
            this.source = datafirst[2];

//            in.readTypedList(mediaContentList,MediaContent.CREATOR);
//            in.readTypedList(mediaThumbnailList,MediaThumbnail.CREATOR);


        }

        public int describeContents(){
            return 0;
        }

        @Override
        public void writeToParcel(Parcel dest, int flags) {

            dest.writeStringArray(new String[] {this.title,
                    this.link,
                    this.description,
                    this.author,
                    this.category,
                    this.comments,
                    this.enclosure,
                    this.guid,
                    this.pubDate,
                    this.source});

//            dest.writeTypedList(mediaContentList);
//            dest.writeTypedList(mediaThumbnailList);

            };

    public static final Parcelable.Creator<ParcebleItem> CREATOR
            = new Parcelable.Creator<ParcebleItem>() {
        public ParcebleItem createFromParcel(Parcel in) {
            return new ParcebleItem(in);
        }

        public ParcebleItem[] newArray(int size) {
            return new ParcebleItem[size];
        }
    };
//        public static final Parcelable.Creator CREATOR = new Parcelable.Creator() {
//            public Item createFromParcel(Parcel in) {
//                return new Item(in);
//            }
//
//            public Item[] newArray(int size) {
//                return new Item[size];
//            }
//        };

}

//
//    public static class Image {
//
//        String title;
//        String link;
//        String url;
//
//        public void setTitle(String title) {
//            this.title = title;
//        }
//
//        public void setLink(String link) {
//            this.link = link;
//        }
//
//
//
//        public String getUrl() {
//            return url;
//        }
//
//
//    }

//    public static class MediaContent implements Parcelable {
//
//        private String url;
//        private String medium;
//
//        public String getUrl() {
//            return url;
//        }
//
//        public String getMedium() {
//            return medium;
//        }
//
//
//        // Parcelling part
//        private MediaContent(Parcel in){
//            String[] data = new String[12];
//
//            in.readStringArray(data);
//            this.url = data[0];
//            this.medium = data[1];
//
//
//        }
//
//        public int describeContents(){
//            return 0;
//        }
//
//        @Override
//        public void writeToParcel(Parcel dest, int flags) {
//            dest.writeStringArray(new String[] {this.url,
//                    this.medium
//            });
//        }
//        public static final Parcelable.Creator CREATOR = new Parcelable.Creator() {
//            public MediaContent createFromParcel(Parcel in) {
//                return new MediaContent(in);
//            }
//
//            public MediaContent[] newArray(int size) {
//                return new MediaContent[size];
//            }
//        };
//
//    }
//
//    public static class MediaThumbnail implements Parcelable {
//        String url;
//
//        public String getUrl() {
//            return url;
//        }
//
//        // Parcelling part
//        public MediaThumbnail(Parcel in){
//            String[] data = new String[12];
//
//            in.readStringArray(data);
//            this.url = data[0];
//
//        }
//
//        public int describeContents(){
//            return 0;
//        }
//
//        @Override
//        public void writeToParcel(Parcel dest, int flags) {
//            dest.writeStringArray(new String[] {this.url
//            });
//        }
//        public static final Parcelable.Creator CREATOR = new Parcelable.Creator() {
//            public MediaThumbnail createFromParcel(Parcel in) {
//                return new MediaThumbnail(in);
//            }
//
//            public MediaThumbnail[] newArray(int size) {
//                return new MediaThumbnail[size];
//            }
//        };
//    }
//
//    public List<Link> getLinks() {
//        return links;
//    }
//
//    public List<Item> getItemList() {
//        return itemList;
//    }
//
//    public Image getImage() {
//        return image;
//    }
//
//    public String getTitle() {
//        return title;
//    }

//}
