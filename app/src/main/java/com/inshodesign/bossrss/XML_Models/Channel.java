package com.inshodesign.bossrss.XML_Models;

import android.os.Parcel;
import android.os.Parcelable;

import org.simpleframework.xml.Attribute;
import org.simpleframework.xml.Element;
import org.simpleframework.xml.ElementList;
import org.simpleframework.xml.Namespace;
import org.simpleframework.xml.NamespaceList;
import org.simpleframework.xml.Root;
import org.simpleframework.xml.Text;

import java.util.ArrayList;
import java.util.List;

@NamespaceList({
        @Namespace(reference = "http://www.w3.org/2005/Atom", prefix = "atom"),
        @Namespace(reference = "http://search.yahoo.com/mrss/", prefix = "media"),
        @Namespace(reference = "http://www.itunes.com/dtds/podcast-1.0.dtd", prefix = "itunes")

})

@Root(name = "channel", strict = false)
public class Channel {

    // Tricky part in Simple XML because the link is named twice
    @ElementList(entry = "link", inline = true, required = false)
    public List<Link> links;


    @ElementList(name = "item", inline = true)
    public List<Item> itemList;


    @Element
    String title;
    public String getTitle() {
        return title;
    }
    public void setTitle(String title) {
        this.title = title;
    }

    @Element
    String language;

    @Element(name = "ttl", required = false)
    int ttl;

    @Element(name = "pubDate", required = false)
    String pubDate;

    @ElementList(entry="image", inline=true, required = false)
    private List<Image> imageList;

    public List<Image> getImageList() {
        return imageList;
    }

    @Root(name = "image", strict = false)

    public static class Image {

        @Element(name = "title", required = false)
        String title;
        @Element(name = "link", required = false)
        String link;
        @Element(name = "url", required = false)
        String url;

        public String getUrl() {
            return url;
        }

    }

    @Override
    public String toString() {
        return "Channel{" +
                "links=" + links +
                ", itemList=" + itemList +
                ", title='" + title + '\'' +
                ", language='" + language + '\'' +
                ", ttl=" + ttl +
                ", pubDate='" + pubDate + '\'' +
                '}';
    }



    public static class Link {
        @Attribute(required = false)
        public String href;

        @Attribute(required = false)
        public String rel;

        @Attribute(name = "type", required = false)
        public String contentType;

        @Text(required = false)
        public String link;
    }

    @Root(name = "item", strict = false)
    public static class Item implements  Parcelable {

        public Item(){}

        @Element(name = "title", required = true)
        String title;//The title of the item.	Venice Film Festival Tries to Quit Sinking
        @Element(name = "link", required = true)
        public String link;//The URL of the item.	http://www.nytimes.com/2002/09/07/movies/07FEST.html
        public String getLink() {return link;}

        @Element(name = "description", required = true)
        String description;//The item synopsis.	Some of the most heated chatter at the Venice Film Festival this week was about the way that the arrival of the stars at the Palazzo del Cinema was being staged.
        @Element(name = "author", required = false)
        String author;//Email address of the author of the item. More.	oprah@oxygen.net
        @Element(name = "category", required = false)
        String category;//Includes the item in one or more categories. More.	Simpsons Characters
        @Element(name = "comments", required = false)
        String comments;//URL of a page for comments relating to the item. More.	http://www.myblog.org/cgi-local/mt/mt-comments.cgi?entry_id=290
        @Element(name = "enclosure", required = false)
        Enclosure enclosure;

        @Root(name = "enclosure", strict = false)
        public static class Enclosure implements Parcelable {

            public Enclosure(){}


            @Attribute(name = "url", required = false)
            public String url;

            @Attribute(name = "length", required = false)
            public String length;

            @Attribute(name = "type", required = false)
            public String type;

            public String getUrl() {
                return url;
            }
            //TODO make exception catch
            public Integer getLength() {
                return Integer.parseInt(length);
            }

            public String getType() {
                return type;
            }

            // Parcelling part
            private Enclosure(Parcel in){

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

            public static final Parcelable.Creator<Enclosure> CREATOR
                    = new Parcelable.Creator<Enclosure>() {
                public Enclosure createFromParcel(Parcel in) {
                    return new Enclosure(in);
                }

                public Enclosure[] newArray(int size) {
                    return new Enclosure[size];
                }
            };
        }


        public Enclosure getEnclosure() {
            return enclosure;
        }


        @Element(name = "guid", required = false)
        String guid;//A string that uniquely identifies the item. More.	<guid isPermaLink="true">http://inessential.com/2002/09/01.php#a2</guid>
        @Element(name = "pubDate", required = false)
        String pubDate;//	Indicates when the item was published. More.	Sun, 19 May 2002 15:21:36 GMT
        @Element(name = "source", required = false)
        String source;//	The RSS channel that the item came from. More.

        @Override
        public String toString() {
            return "Item{" +
                    "title='" + title + '\'' +
                    ", link='" + link + '\'' +
                    ", description='" + description + '\'' +
                    ", author='" + author + '\'' +
                    ", category='" + category + '\'' +
                    ", comments='" + comments + '\'' +
                    ", enclosure='" + enclosure + '\'' +
                    ", guid='" + guid + '\'' +
                    ", pubDate='" + pubDate + '\'' +
                    ", source='" + source + '\'' +
                    '}';
        }

        public void setTitle(String title) {
            this.title = title;
        }

        public String getTitle() {
            if(title == null) {
                return "";
            } else {
                return title;
            }
        }


        public void setDescription(String description) {
            this.description = description;
        }

        public String getDescription() {
            return description;
        }

        @ElementList(entry = "content", inline = true, required = false)
        public List<MediaContentTEST> content;

        @Root(name = "content", strict=false)
        public static class MediaContentTEST implements Parcelable {

            public MediaContentTEST(){}

            @Attribute(name = "url", required = false)
            public String url;

            public String getUrl() {
                return url;
            }

            @Attribute(required = false)
            public String medium;

            @Element(name = "thumbnail", required = false)
            MediaContentThumbnail thumbnail;



            public MediaContentThumbnail getThumbnail() {
                return thumbnail;
            }


            @Element(name = "description", required = false)
            String description;


            public String getDescription() {
                return description;
            }


            // Parcelling part
            private MediaContentTEST(Parcel in){

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

            public static final Parcelable.Creator<MediaContentTEST> CREATOR
                    = new Parcelable.Creator<MediaContentTEST>() {
                public MediaContentTEST createFromParcel(Parcel in) {
                    return new MediaContentTEST(in);
                }

                public MediaContentTEST[] newArray(int size) {
                    return new MediaContentTEST[size];
                }
            };
        }

        /* NOTE: In the BBC Feeds they do not have media:content ROOT with media:thumbnail children
         *  Instead they have the media:thumbnail as an element
         * */
        @ElementList(name = "thumbnail", inline = true, required = false)
        List<MediaContentThumbnail> thumbnailList;


        @Root(name = "thumbnail", strict = false)
        public static class MediaContentThumbnail implements Parcelable {

            public MediaContentThumbnail(){}

            @Attribute(required = false)
            public String url;

            @Attribute(required = false)
            public String medium;

            @Attribute(required = false)
            public String width;

            @Attribute(required = false)
            public String height;

            public String getUrl() {
                return url;
            }


            // Parcelling part
            private MediaContentThumbnail(Parcel in){
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

            public static final Parcelable.Creator<MediaContentThumbnail> CREATOR
                    = new Parcelable.Creator<MediaContentThumbnail>() {
                public MediaContentThumbnail createFromParcel(Parcel in) {
                    return new MediaContentThumbnail(in);
                }

                public MediaContentThumbnail[] newArray(int size) {
                    return new MediaContentThumbnail[size];
                }
            };

        }

        public List<MediaContentThumbnail> getThumbnailList() {
            return thumbnailList;
        }


        public List<MediaContentTEST> getContent() {
            return content;
        }

        public String getPubDate() {
            return pubDate;
        }


        // Parcelling part
        private Item(Parcel in){

            this.content = new ArrayList<>();
            in.readList(content,getClass().getClassLoader());
            this.thumbnailList = new ArrayList<>();
            in.readList(thumbnailList,getClass().getClassLoader());


            this.enclosure = in.readParcelable(getClass().getClassLoader());

            String[] datafirst = new String[9];
            in.readStringArray(datafirst);
            this.title = datafirst[0];
            this.link = datafirst[1];
            this.description = datafirst[2];
            this.author = datafirst[3];
            this.category = datafirst[4];
            this.comments = datafirst[5];
            this.guid = datafirst[7];
            this.pubDate = datafirst[8];
        }

        public int describeContents(){
            return 0;
        }

        @Override
        public void writeToParcel(Parcel dest, int flags) {
            dest.writeList(this.content);
            dest.writeList(this.thumbnailList);
            dest.writeParcelable(this.enclosure,flags);

            dest.writeStringArray(new String[] {this.title,
                    this.link,
                    this.description,
                    this.author,
                    this.category,
                    this.comments,
                    this.guid,
                    this.pubDate,
                    this.source});


        }

        public static final Parcelable.Creator<Item> CREATOR
                = new Parcelable.Creator<Item>() {
            public Item createFromParcel(Parcel in) {
                return new Item(in);
            }

            public Item[] newArray(int size) {
                return new Item[size];
            }
        };

    }

    public List<Item> getItemList() {
        return itemList;
    }


}
