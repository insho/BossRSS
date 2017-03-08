package com.inshodesign.bossrss.XMLModel;

import android.os.Parcel;
import android.os.Parcelable;
import android.util.Log;

/**
 * This class exists because I wasn't able to parcel this xml template when passing it from the mainactivity
 * to the fragment, so I transfer the data to this thing and pass this.... hmm....
 * */

public class ParcebleItem implements Parcelable {

    private String title;//The title of the item.	Venice Film Festival Tries to Quit Sinking
    private String link;//The URL of the item.	http://www.nytimes.com/2002/09/07/movies/07FEST.html
    private String description;//The item synopsis.	Some of the most heated chatter at the Venice Film Festival this week was about the way that the arrival of the stars at the Palazzo del Cinema was being staged.
    private String author;//Email address of the author of the item. More.	oprah@oxygen.net
    private String category;//Includes the item in one or more categories. More.	Simpsons Characters
    private String comments;//URL of a page for comments relating to the item. More.	http://www.myblog.org/cgi-local/mt/mt-comments.cgi?entry_id=290
    private  String enclosure;//	Describes a media object that is attached to the item. More.	<enclosure url="http://live.curry.com/mp3/celebritySCms.mp3" length="1069871" type="audio/mpeg"/>
    private String guid;//A string that uniquely identifies the item. More.	<guid isPermaLink="true">http://inessential.com/2002/09/01.php#a2</guid>
    private  String pubDate;//	Indicates when the item was published. More.	Sun, 19 May 2002 15:21:36 GMT
    private  String source;//	The RSS channel that the item came from. More.
    private  String enclosureLink;
    private  Integer enclosureLength;
    private  String enclosureType;

    private String contentURL;
    private String mediaThumbnail;
    private String mediaDescription;

        public ParcebleItem(Channel.Item item) {
            this.title = item.title;

            this.description = item.description;

            this.link = item.link;
            this.author = item.author;
            if(item.pubDate != null) {
                this.pubDate = item.pubDate;
            }

            if(item.getContent() != null) {

                /** Only showing the first pic (if there are multiples) **/
                if(item.getContent().get(0).getUrl() != null) {
                    this.contentURL = item.getContent().get(0).getUrl();
                }

                if(item.getContent().get(0).getThumbnail() != null) {
                    this.mediaThumbnail = item.getContent().get(0).getThumbnail().getUrl();
                }
                if(item.getContent().get(0).getDescription() != null) {
                    this.mediaDescription = item.getContent().get(0).getDescription();
                }

            }

            if(item.getEnclosure() != null) {

                if(item.getEnclosure().getUrl() != null) {
                    this.enclosureLink = item.getEnclosure().getUrl();
                }

                if(item.getEnclosure().getLength() != null
                        && android.text.TextUtils.isDigitsOnly(item.getEnclosure().getLength())) {
                    this.enclosureLength = Integer.parseInt(item.getEnclosure().getLength());
                }
                if(item.getEnclosure().getType() != null ) {
                    this.enclosureType = item.getEnclosure().getType();
                }

            }


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

    public String getEnclosureLink() {
        return enclosureLink;
    }

    public Integer getEnclosureLength() {
        return enclosureLength;
    }

    public String getEnclosureType() {
        return enclosureType;
    }

    public String getPubDate() {
        return pubDate;
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
        }

    public String getContentURL() {
        return contentURL;
    }

    public String getMediaThumbnail() {
        return mediaThumbnail;
    }

    public String getMediaDescription() {
        return mediaDescription;
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
            }

    public static final Parcelable.Creator<ParcebleItem> CREATOR
            = new Parcelable.Creator<ParcebleItem>() {
        public ParcebleItem createFromParcel(Parcel in) {
            return new ParcebleItem(in);
        }

        public ParcebleItem[] newArray(int size) {
            return new ParcebleItem[size];
        }
    };


}

