//package com.inshodesign.bossrss.XML_Models;
//
//import android.os.Parcel;
//import android.os.Parcelable;
//
//import org.simpleframework.xml.Attribute;
//import org.simpleframework.xml.Element;
//import org.simpleframework.xml.ElementList;
//import org.simpleframework.xml.Root;
//
//import java.util.List;
//
//
//
//@Root(name = "item", strict = false)
//public class Item implements Parcelable {
//
//    @Element(name = "title", required = true)
//    String title;//The title of the item.	Venice Film Festival Tries to Quit Sinking
//    @Element(name = "link", required = true)
//    public String link;//The URL of the item.	http://www.nytimes.com/2002/09/07/movies/07FEST.html
//
//    @Element(name = "description", required = true)
//    String description;//The item synopsis.	Some of the most heated chatter at the Venice Film Festival this week was about the way that the arrival of the stars at the Palazzo del Cinema was being staged.
//    @Element(name = "author", required = false)
//    String author;//Email address of the author of the item. More.	oprah@oxygen.net
//    @Element(name = "category", required = false)
//    String category;//Includes the item in one or more categories. More.	Simpsons Characters
//    @Element(name = "comments", required = false)
//    String comments;//URL of a page for comments relating to the item. More.	http://www.myblog.org/cgi-local/mt/mt-comments.cgi?entry_id=290
//    @Element(name = "enclosure", required = false)
//    Enclosure enclosure;
//
//
//    public Item() {}
//
//    @Root(name = "enclosure", strict = false)
//    public static class Enclosure {
//        @Attribute(name = "url", required = false)
//        public String url;
//
//        @Attribute(name = "length", required = false)
//        public String length;
//
//        @Attribute(name = "type", required = false)
//        public String type;
//
//        public String getUrl() {
//            return url;
//        }
//
//        public String getLength() {
//            return length;
//        }
//
//        public String getType() {
//            return type;
//        }
//    }
//
//
//    public Enclosure getEnclosure() {
//        return enclosure;
//    }
//
//
//    @Element(name = "guid", required = false)
//    String guid;//A string that uniquely identifies the item. More.	<guid isPermaLink="true">http://inessential.com/2002/09/01.php#a2</guid>
//    @Element(name = "pubDate", required = false)
//    String pubDate;//	Indicates when the item was published. More.	Sun, 19 May 2002 15:21:36 GMT
//    @Element(name = "source", required = false)
//    String source;//	The RSS channel that the item came from. More.
//
//    @Override
//    public String toString() {
//        return "Item{" +
//                "title='" + title + '\'' +
//                ", link='" + link + '\'' +
//                ", description='" + description + '\'' +
//                ", author='" + author + '\'' +
//                ", category='" + category + '\'' +
//                ", comments='" + comments + '\'' +
//                ", enclosure='" + enclosure + '\'' +
//                ", guid='" + guid + '\'' +
//                ", pubDate='" + pubDate + '\'' +
//                ", source='" + source + '\'' +
//                '}';
//    }
//
//    public void setTitle(String title) {
//        this.title = title;
//    }
//
//    public void setDescription(String description) {
//        this.description = description;
//    }
//
//    public String getDescription() {
//        return description;
//    }
//
//    @ElementList(entry = "content", inline = true, required = false)
//    public List<MediaContentTEST> content;
//
//    @Root(name = "content", strict=false)
//    public static class MediaContentTEST {
//        @Attribute(name = "url", required = false)
//        public String url;
//
//        public String getUrl() {
//            return url;
//        }
//
//        @Attribute(required = false)
//        public String medium;
//
//        @Element(name = "thumbnail", required = false)
//        MediaContentThumbnail thumbnail;
//
//
//
//        public MediaContentThumbnail getThumbnail() {
//            return thumbnail;
//        }
//
//
//        @Element(name = "description", required = false)
//        String description;
//
//
//        public String getDescription() {
//            return description;
//        }
//    }
//
//    /** In the BBC Feeds they do not have media:content ROOT with media:thumbnail children
//     *  Instead they have the media:thumbnail as an element
//     * **/
//    @ElementList(name = "thumbnail", inline = true, required = false)
//    List<MediaContentThumbnail> thumbnailList;
//    @Root(name = "thumbnail", strict = false)
//    public static class MediaContentThumbnail {
//        @Attribute(required = false)
//        public String url;
//
//        @Attribute(required = false)
//        public String medium;
//
//        @Attribute(required = false)
//        public String width;
//
//        @Attribute(required = false)
//        public String height;
//
//        public String getUrl() {
//            return url;
//        }
//    }
//
//    public List<MediaContentThumbnail> getThumbnailList() {
//        return thumbnailList;
//    }
//
//
//    public List<MediaContentTEST> getContent() {
//        return content;
//    }
//    public String getTitle() {
//        return title;
//
//    }
//
//    public String getLink() {
//        return link;
//    }
//
//    public String getAuthor() {
//        return author;
//    }
//
//    public String getCategory() {
//        return category;
//    }
//
////    public String getEnclosureLink() {
////        return enclosureLink;
////    }
////
////    public Integer getEnclosureLength() {
////        return enclosureLength;
////    }
////
////    public String getEnclosureType() {
////        return enclosureType;
////    }
//
//    public String getPubDate() {
//        return pubDate;
//    }
//
//
//    // Parcelling part
//    private Item(Parcel in){
//        String[] datafirst = new String[10];
//
//        in.readStringArray(datafirst);
//        this.title = datafirst[0];
//        this.link = datafirst[1];
//        this.description = datafirst[2];
//        this.author = datafirst[3];
//        this.category = datafirst[4];
//        this.comments = datafirst[5];
//        this.enclosure.url = datafirst[6];
//        this.guid = datafirst[7];
//        this.pubDate = datafirst[8];
//    }
//
//    public int describeContents(){
//        return 0;
//    }
//
//    @Override
//    public void writeToParcel(Parcel dest, int flags) {
//
//        dest.writeStringArray(new String[] {this.title,
//                this.link,
//                this.description,
//                this.author,
//                this.category,
//                this.comments,
//                this.enclosure.url,
//                this.guid,
//                this.pubDate,
//                this.source});
//    }
//
//    public static final Parcelable.Creator<Item> CREATOR
//            = new Parcelable.Creator<Item>() {
//        public Item createFromParcel(Parcel in) {
//            return new Item(in);
//        }
//
//        public Item[] newArray(int size) {
//            return new Item[size];
//        }
//    };
//
//
//}