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
import java.util.regex.Matcher;
import java.util.regex.Pattern;

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
//    @Convert(ItemConverter.class)
    public static class Item {

        public String getPubDate() {
            return pubDate;
        }

        @Element(name = "title", required = true)
        String title;//The title of the item.	Venice Film Festival Tries to Quit Sinking

//        @Element(name = "link", required = false, data = true)
//        String datalink;


        @Element(name = "link", required = true)
//        @Convert(LinkConverter.class)
        public String link;//The URL of the item.	http://www.nytimes.com/2002/09/07/movies/07FEST.html




        public void setLink(String link) {
            this.link = link;
        }

        @Element(name = "description", required = true)
        String description;//The item synopsis.	Some of the most heated chatter at the Venice Film Festival this week was about the way that the arrival of the stars at the Palazzo del Cinema was being staged.
        @Element(name = "author", required = false)
        String author;//Email address of the author of the item. More.	oprah@oxygen.net
        @Element(name = "category", required = false)
        String category;//Includes the item in one or more categories. More.	Simpsons Characters
        @Element(name = "comments", required = false)
        String comments;//URL of a page for comments relating to the item. More.	http://www.myblog.org/cgi-local/mt/mt-comments.cgi?entry_id=290
//        @Element(name = "enclosure", required = false)
//        String enclosure;//	Describes a media object that is attached to the item. More.	<enclosure url="http://live.curry.com/mp3/celebritySCms.mp3" length="1069871" type="audio/mpeg"/>

        @Element(name = "enclosure", required = false)
        Enclosure enclosure;

        @Root(name = "enclosure", strict = false)
        public static class Enclosure {
            @Attribute(name = "url", required = false)
            public String url;

            @Attribute(name = "length", required = false)
            public String length;

            @Attribute(name = "type", required = false)
            public String type;

            public String getUrl() {
                return url;
            }

            public String getLength() {
                return length;
            }

            public String getType() {
                return type;
            }
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
            return title;
        }

        public void setDescription(String description) {
            this.description = description;
        }

        public String getDescription() {
            return description;
        }

//        @Element(name = "content", required = false)

        @ElementList(entry = "content", inline = true, required = false)
        public List<MediaContentTEST> content;

        @Root(name = "content", strict=false)
        public static class MediaContentTEST {
            @Attribute(name = "url", required = false)
            public String url;

            public String getUrl() {
                return url;
            }

            @Attribute(required = false)
            public String medium;

            @Element(name = "thumbnail", required = false)
            MediaContentThumbnail thumbnail;

            @Root(name = "thumbnail", strict = false)
            public static class MediaContentThumbnail {
                @Attribute(required = false)
                public String url;

                @Attribute(required = false)
                public String medium;

                public String getUrl() {
                    return url;
                }
            }

            public MediaContentThumbnail getThumbnail() {
                return thumbnail;
            }


            @Element(name = "description", required = false)
            String description;


            public String getDescription() {
                return description;
            }
        }

        public List<MediaContentTEST> getContent() {
            return content;
        }
    }



    public List<Item> getItemList() {
        return itemList;
    }







//
//
//    static class ItemConverter implements Converter<Item>
//    {
//        @Override
//        public Item read(InputNode node) throws Exception
//        {
//            Item item = new Item();
//
//            InputNode child;
//            final String HTML_TAG_REG_EX = "</?[^>]+>";
//
//            // Iterate over all childs an get their values
//            while( ( child = node.getNext() ) != null )
//            {
//                switch(child.getName())
//                {
//                    case "link":
//
//                        if(child.getValue().contains("![CDATA")) {
//                            String text = child.getValue().replaceAll(HTML_TAG_REG_EX, "");
//                            Log.d("TEST","NEW TEXT: " + text);
//                            item.setLink(text);
//                        }
//
//
//                        break;
//                    default:
//                        throw new RuntimeException("Unknown Element found: " + child);
//                }
//
//
//
//            }
//
//            return item;
//        }
//
//
//        @Override
//        public void write(OutputNode node, Item value) throws Exception
//        {
//            /*
//             * TODO: Implement if necessary
//             */
//            throw new UnsupportedOperationException("Not supported yet.");
//        }
//
//    }



}
