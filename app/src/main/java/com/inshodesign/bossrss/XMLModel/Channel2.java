//package com.inshodesign.bossrss.XMLModel;
//
///**
// * Created by github/macsystems
// */
//
//import android.os.Parcel;
//import android.os.Parcelable;
//
//import org.simpleframework.xml.Attribute;
//import org.simpleframework.xml.Element;
//import org.simpleframework.xml.ElementList;
//import org.simpleframework.xml.Namespace;
//import org.simpleframework.xml.NamespaceList;
//import org.simpleframework.xml.Root;
//import org.simpleframework.xml.Text;
//
//import java.util.ArrayList;
//import java.util.List;
//
//@NamespaceList({
//        @Namespace(reference = "http://www.w3.org/2005/Atom", prefix = "atom")
//})
//@Root(strict = false)
//public class Channel2 {
//    // Tricky part in Simple XML because the link is named twice
//    @ElementList(entry = "link", inline = true, required = false)
//    public List<Link> links;
//
//    @ElementList(name = "item", required = true, inline = true)
//    public List<Item> itemList;
//
//    //    @ElementList(name = "image", required = false, inline = true)
////    public List<Image> imageList;
//    @Element(name = "image", required = false)
//    Image image;
//
//    @Element
//    String title;
//    @Element
//    String language;
//
//    @Element(name = "ttl", required = false)
//    int ttl;
//
//    @Element(name = "pubDate", required = false)
//    String pubDate;
//
//
//    @Override
//    public String toString() {
//        return "Channel{" +
//                "links=" + links +
//                ", itemList=" + itemList +
//                ", title='" + title + '\'' +
//                ", language='" + language + '\'' +
//                ", ttl=" + ttl +
//                ", pubDate='" + pubDate + '\'' +
//                '}';
//    }
//
//    public static class Link {
//        @Attribute(required = false)
//        public String href;
//
//        @Attribute(required = false)
//        public String rel;
//
//        @Attribute(name = "type", required = false)
//        public String contentType;
//
//        @Text(required = false)
//        public String link;
//    }
//
////    public static class MediaContent {
////        @Element(name = "thumbnail", required = false)
////        @Namespace(prefix = "media") // Add your reference here!
////        public Thumbnail thumbnail;
////        static class Thumbnail {
////
////            @Attribute(name = "url")
////            private String url;
////
////            public String getUrl() {
////                return url;
////            }
////        }
////        public Thumbnail getThumbnail() {
////            return thumbnail;
////        }
////
////
////    }
//
//
//    public static class Thumbnail {
//
//        @Attribute(name = "url")
//        private String url;
//
//        public String getUrl() {
//            return url;
//        }
//    }
//
//
//
//
//    @Root(name = "item", strict = false)
//    public static class Item  {
//
////        public ArrayList<MediaContent> mediaContentList = new ArrayList<MediaContent>();
////        public ArrayList<MediaThumbnail> mediaThumbnailList = new ArrayList<MediaThumbnail>();
//
//        @Element(name = "title", required = true)
//        String title;//The title of the item.	Venice Film Festival Tries to Quit Sinking
//        @Element(name = "link", required = true)
//        String link;//The URL of the item.	http://www.nytimes.com/2002/09/07/movies/07FEST.html
//        @Element(name = "description", required = true)
//        String description;//The item synopsis.	Some of the most heated chatter at the Venice Film Festival this week was about the way that the arrival of the stars at the Palazzo del Cinema was being staged.
//        @Element(name = "author", required = false)
//        String author;//Email address of the author of the item. More.	oprah@oxygen.net
//        @Element(name = "category", required = false)
//        String category;//Includes the item in one or more categories. More.	Simpsons Characters
//        @Element(name = "comments", required = false)
//        String comments;//URL of a page for comments relating to the item. More.	http://www.myblog.org/cgi-local/mt/mt-comments.cgi?entry_id=290
//        @Element(name = "enclosure", required = false)
//        String enclosure;//	Describes a media object that is attached to the item. More.	<enclosure url="http://live.curry.com/mp3/celebritySCms.mp3" length="1069871" type="audio/mpeg"/>
//
////        @Element(name = "media:content", required = false)
////        MediaContent mediaContent;
////        @Element(name = "media:thumbnail", required = false)
////        MediaThumbnail mediaThumbnail;
//
//
//
//        @Element(name = "guid", required = false)
//        String guid;//A string that uniquely identifies the item. More.	<guid isPermaLink="true">http://inessential.com/2002/09/01.php#a2</guid>
//        @Element(name = "pubDate", required = false)
//        String pubDate;//	Indicates when the item was published. More.	Sun, 19 May 2002 15:21:36 GMT
//        @Element(name = "source", required = false)
//        String source;//	The RSS channel that the item came from. More.
//
////        @Element(name = "content", required = false)
////        Content content;
//
////        @Element(name = "content")
////        @Namespace(prefix = "media") // Add your reference here!
////        MediaContent content;
//
//
//        @Element(name = "thumbnail")
//        @Namespace(prefix = "media") // Add your reference here!
//                Thumbnail thumbnail;
//
////        @Root(name = "content", strict = false)
////
////        static class Content {
////
////            @Attribute(name = "thumbnail")
////            private String url;
////
////            public String getUrl() {
////                return url;
////            }
////        }
//
//        @Override
//        public String toString() {
//            return "Item{" +
//                    "title='" + title + '\'' +
//                    ", link='" + link + '\'' +
//                    ", description='" + description + '\'' +
//                    ", author='" + author + '\'' +
//                    ", category='" + category + '\'' +
//                    ", comments='" + comments + '\'' +
//                    ", enclosure='" + enclosure + '\'' +
//                    ", guid='" + guid + '\'' +
//                    ", pubDate='" + pubDate + '\'' +
//                    ", source='" + source + '\'' +
//                    '}';
//        }
//
//        public String getTitle() {
//            return title;
//        }
//
//        public String getAuthor() {
//            return author;
//        }
//
//        public String getCategory() {
//            return category;
//        }
//
////        public MediaThumbnail getMediaThumbnail() {
////            return mediaThumbnail;
////        }
//
////        public Content getContent() {
////            return content;
////        }
//
//
//        //        public boolean hasContent() {
////            return (content != null && content.getThumbnail() != null && content.getThumbnail().getUrl() != null);
////        }
////
//        public Thumbnail getThumbnail() {
//            return thumbnail;
//        }
//        public boolean hasThumbnail() {
//            return (thumbnail != null && thumbnail.getUrl() != null);
//        }
////
////        public MediaContent getMediaContent() {
////            return content;
////        }
////        public boolean hasThumbnail() {
////            return (content != null && content.getThumbnail() != null && content.getThumbnail().getUrl() != null);
////        }
//
//
//        //        // Parcelling part
////        private  Item(Parcel in){
////            String[] datafirst = new String[10];
////
////            in.readStringArray(datafirst);
////            this.title = datafirst[0];
////            this.link = datafirst[1];
////            this.description = datafirst[2];
////            this.author = datafirst[3];
////            this.category = datafirst[4];
////            this.comments = datafirst[5];
////            this.enclosure = datafirst[6];
////
////            this.guid = datafirst[0];
////            this.pubDate = datafirst[1];
////            this.source = datafirst[2];
////
//////            in.readTypedList(mediaContentList,MediaContent.CREATOR);
//////            in.readTypedList(mediaThumbnailList,MediaThumbnail.CREATOR);
////
////
////        }
////
////        public int describeContents(){
////            return 0;
////        }
////
////        @Override
////        public void writeToParcel(Parcel dest, int flags) {
////
////            dest.writeStringArray(new String[] {this.title,
////                    this.link,
////                    this.description,
////                    this.author,
////                    this.category,
////                    this.comments,
////                    this.enclosure,
////                    this.guid,
////                    this.pubDate,
////                    this.source});
////
//////            dest.writeTypedList(mediaContentList);
//////            dest.writeTypedList(mediaThumbnailList);
////
////            };
////
////    public static final Parcelable.Creator<Item> CREATOR
////            = new Parcelable.Creator<Item>() {
////        public Item createFromParcel(Parcel in) {
////            return new Item(in);
////        }
////
////        public Item[] newArray(int size) {
////            return new Item[size];
////        }
////    };
////        public static final Parcelable.Creator CREATOR = new Parcelable.Creator() {
////            public Item createFromParcel(Parcel in) {
////                return new Item(in);
////            }
////
////            public Item[] newArray(int size) {
////                return new Item[size];
////            }
////        };
//
//    }
//
//
////
////    @Attribute(name = "thumbnail")
////    @Namespace(prefix = "media") // Add your reference here!
////            Thumbnail thumbnail;
////
////    @Root(name = "thumbnail", strict = false)
////    static class Thumbnail {
////
////        public String getUrl() {
////            return url;
////        }
////
////        @Attribute(name = "url")
////        private String url;
////
////
////    }
//
//    @Root(name = "image", strict = false)
//    public static class Image {
//
//        @Element(name = "title", required = false)
//        String title;
//        @Element(name = "link", required = false)
//        String link;
//        @Element(name = "url", required = true)
//        String url;
//
//        public String getUrl() {
//            return url;
//        }
//
//
//
//    }
////
////    public static class MediaContent {
////
////        @Attribute(name = "url", required = false)
////        private String url;
////
////        @Attribute(name = "medium", required = false)
////        private String medium;
////
////        public String getUrl() {
////            return url;
////        }
////
////        public String getMedium() {
////            return medium;
////        }
//
//
////        // Parcelling part
////        private MediaContent(Parcel in){
////            String[] data = new String[12];
////
////            in.readStringArray(data);
////            this.url = data[0];
////            this.medium = data[1];
////
////
////        }
////
////        public int describeContents(){
////            return 0;
////        }
////
////        @Override
////        public void writeToParcel(Parcel dest, int flags) {
////            dest.writeStringArray(new String[] {this.url,
////                    this.medium
////            });
////        }
////        public static final Parcelable.Creator CREATOR = new Parcelable.Creator() {
////            public MediaContent createFromParcel(Parcel in) {
////                return new MediaContent(in);
////            }
////
////            public MediaContent[] newArray(int size) {
////                return new MediaContent[size];
////            }
////        };
//
////    }
////
////    public static class Thumbnail {
////        @Attribute(name = "url")
////        private String url;
////
////        public String getUrl() {
////            return url;
////        }
////
////
////    }
//
////    public static class Content {
////
////        @Attribute(name = "thumbnail", required = false)
////        private Thumbnail thumbnail;
////
////        @Root(name = "thumbnail", strict = false)
////        static class Thumbnail {
////
////            @Attribute(name = "url")
////            private String url;
////
////            public String getUrl() {
////                return url;
////            }
////        }
////
////        public Thumbnail getThumbnail() {
////            return thumbnail;
////        }
////    }
//
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
//
//
//}