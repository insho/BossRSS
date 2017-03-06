package com.inshodesign.bossrss.XMLModel;


import android.util.Log;

import org.simpleframework.xml.Attribute;
import org.simpleframework.xml.Element;
import org.simpleframework.xml.ElementList;
import org.simpleframework.xml.Namespace;
import org.simpleframework.xml.NamespaceList;
import org.simpleframework.xml.Root;
import org.simpleframework.xml.Text;
import org.simpleframework.xml.convert.Convert;
import org.simpleframework.xml.convert.Converter;
import org.simpleframework.xml.stream.InputNode;
import org.simpleframework.xml.stream.OutputNode;

import java.util.List;

@NamespaceList({
        @Namespace(reference = "http://www.w3.org/2005/Atom", prefix = "atom")
})
@Root(strict = false)
@Convert(Channel.ChannelConverter.class) // Specify the Converter
public class Channel {

    String imageURL;

    public String getImageURL() {
        return imageURL;
    }

    public void setImageURL(String imageURL) {
        this.imageURL = imageURL;
    }

    // Tricky part in Simple XML because the link is named twice
    @ElementList(entry = "link", inline = true, required = false)
    public List<Link> links;

    @ElementList(name = "item", required = true, inline = true)
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

    @Element
    Image image;

    public Image getImage() {
        return image;
    }

    @Root(name = "image", strict = false)
    public static class Image {

        @Element(name = "title", required = false)
        String title;
        @Element(name = "link", required = false)
        String link;
        @Element(name = "url", required = true)
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
    @Convert(ItemConverter.class) // Specify the Converter
    public static class Item {

        String mediaThumbnailURL;

        public String getMediaThumbnailURL() {
            return mediaThumbnailURL;
        }

        public void setMediaThumbnailURL(String mediaThumbnailURL) {
            this.mediaThumbnailURL = mediaThumbnailURL;
        }

        public String getPubDate() {
            return pubDate;
        }


        @Element(name = "title", required = true)
        String title;//The title of the item.	Venice Film Festival Tries to Quit Sinking
        @Element(name = "link", required = true)
        String link;//The URL of the item.	http://www.nytimes.com/2002/09/07/movies/07FEST.html
        @Element(name = "description", required = true)
        String description;//The item synopsis.	Some of the most heated chatter at the Venice Film Festival this week was about the way that the arrival of the stars at the Palazzo del Cinema was being staged.
        @Element(name = "author", required = false)
        String author;//Email address of the author of the item. More.	oprah@oxygen.net
        @Element(name = "category", required = false)
        String category;//Includes the item in one or more categories. More.	Simpsons Characters
        @Element(name = "comments", required = false)
        String comments;//URL of a page for comments relating to the item. More.	http://www.myblog.org/cgi-local/mt/mt-comments.cgi?entry_id=290
        @Element(name = "enclosure", required = false)
        String enclosure;//	Describes a media object that is attached to the item. More.	<enclosure url="http://live.curry.com/mp3/celebritySCms.mp3" length="1069871" type="audio/mpeg"/>
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
            return title;
        }

        public void setDescription(String description) {
            this.description = description;
        }

        public String getDescription() {
            return description;
        }
    }



    /*** Could never get the custom converter factory to work :( **/
    public static class ChannelConverter implements Converter<Channel>
    {


        @Override
        public Channel read(InputNode node) throws Exception
        {
            Channel channel = new Channel();

            InputNode child;

            // Iterate over all childs an get their values
            while( ( child = node.getNext() ) != null )
            {
                switch(child.getName())
                {
                    case "title":
                        if(child.getParent() != null && child.getParent().getName().equals("image")) {
                            channel.setImageURL(child.getValue());
                        } else {
                            channel.setTitle(child.getValue());
                        }
                        break;

                    default:
                        throw new RuntimeException("Unknown Element found: " + child);
                }
            }

            return channel;
        }


        @Override
        public void write(OutputNode node, Channel value) throws Exception
        {
            /*
             * TODO: Implement if necessary
             */
            throw new UnsupportedOperationException("Not supported yet.");
        }

    }




    static class ItemConverter implements Converter<Item>
    {
        @Override
        public Item read(InputNode node) throws Exception
        {
            Item item = new Item();

            InputNode child;

            // Iterate over all childs an get their values
            while( ( child = node.getNext() ) != null )
            {
                switch(child.getName())
                {
                    case "title":
                        Log.d("TEST","TITLEEEEEE: " + child.getValue());
                        item.setTitle("COCK");
                        break;

                    case "description":
                        Log.d("TEST","desc: " + child.getValue());
                        if(child.getAttribute("src") != null) {
                            item.setMediaThumbnailURL(child.getValue());
                        } else {
                            item.setDescription(child.getValue());
                        }
                        break;

                    case "thumbnail":
                        /*
                         * "link" can be either a <link>...</link> or
                         * a <atom:link>...</atom:link>
                         */
                        Log.d("TEST","found thumbnail");
                        if( child.getPrefix().equals("media"))
                        {
                            Log.d("TEST","found media -- " + child.getAttributes());
                            if(child.getAttribute("url") != null) {
                                item.setMediaThumbnailURL(child.getAttribute("url").getValue());
                            }

                        }
                        break;
                    default:
                        throw new RuntimeException("Unknown Element found: " + child);
                }
            }

            return item;
        }


        @Override
        public void write(OutputNode node, Item value) throws Exception
        {
            /*
             * TODO: Implement if necessary
             */
            throw new UnsupportedOperationException("Not supported yet.");
        }

    }

    public List<Item> getItemList() {
        return itemList;
    }

}
