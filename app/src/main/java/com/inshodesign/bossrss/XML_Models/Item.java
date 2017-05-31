package com.inshodesign.bossrss.XML_Models;

import android.os.Parcel;
import android.os.Parcelable;

import org.simpleframework.xml.Element;
import org.simpleframework.xml.ElementList;
import org.simpleframework.xml.Namespace;
import org.simpleframework.xml.NamespaceList;
import org.simpleframework.xml.Root;

import java.util.ArrayList;
import java.util.List;

@NamespaceList({
        @Namespace(reference = "http://www.w3.org/2005/Atom", prefix = "atom"),
        @Namespace(reference = "http://search.yahoo.com/mrss/", prefix = "media"),
        @Namespace(reference = "http://www.itunes.com/dtds/podcast-1.0.dtd", prefix = "itunes")

})

/**
 * Object representing a single Item (article, etc) in an RSS feed. Items appear as an array in {@link Channel} when
 * pulled by {@link com.inshodesign.bossrss.APIService}. Items also contain {@link ItemEnclosure}, {@link ItemMediaContent},
 * and {@link ItemMediaContentThumbnail}
 */

@Root(name = "item", strict = false)
public class Item implements  Parcelable {

    public Item(){}

    @Element(name = "title", required = true)
    private String title;//The title of the item.	Venice Film Festival Tries to Quit Sinking
    @Element(name = "link", required = true)
    private String link;//The URL of the item.	http://www.nytimes.com/2002/09/07/movies/07FEST.html

    @Element(name = "description", required = true)
    private String description;//The item synopsis.	Some of the most heated chatter at the Venice Film Festival this week was about the way that the arrival of the stars at the Palazzo del Cinema was being staged.
    @Element(name = "author", required = false)
    private String author;//Email address of the author of the item. More.	oprah@oxygen.net
    @Element(name = "category", required = false)
    private String category;//Includes the item in one or more categories. More.	Simpsons Characters
    @Element(name = "comments", required = false)
    private String comments;//URL of a page for comments relating to the item. More.	http://www.myblog.org/cgi-local/mt/mt-comments.cgi?entry_id=290
    @Element(name = "enclosure", required = false)
    private ItemEnclosure enclosure;
    @Element(name = "guid", required = false)
    private String guid;//A string that uniquely identifies the item. More.	<guid isPermaLink="true">http://inessential.com/2002/09/01.php#a2</guid>
    @Element(name = "pubDate", required = false)
    private String pubDate;//	Indicates when the item was published. More.	Sun, 19 May 2002 15:21:36 GMT
    @Element(name = "source", required = false)
    private String source;//	The RSS channel that the item came from. More.
    @ElementList(entry = "content", inline = true, required = false)
    public List<ItemMediaContent> content;

    /* NOTE: In the BBC Feeds they do not have media:content ROOT with media:thumbnail children
     *  Instead they have the media:thumbnail as an element
     * */
    @ElementList(name = "thumbnail", inline = true, required = false)
    private List<ItemMediaContentThumbnail> thumbnailList;


    public String getLink() {return link;}


    public ItemEnclosure getEnclosure() {
        return enclosure;
    }


//    @Override
//    public String toString() {
//        return "ChannelItem{" +
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
    public List<ItemMediaContentThumbnail> getThumbnailList() {
        return thumbnailList;
    }
    public List<ItemMediaContent> getContent() {
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