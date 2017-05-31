package com.inshodesign.bossrss.XML_Models;

import org.simpleframework.xml.Element;
import org.simpleframework.xml.ElementList;
import org.simpleframework.xml.Namespace;
import org.simpleframework.xml.NamespaceList;
import org.simpleframework.xml.Root;
import java.util.List;

@NamespaceList({
        @Namespace(reference = "http://www.w3.org/2005/Atom", prefix = "atom"),
        @Namespace(reference = "http://search.yahoo.com/mrss/", prefix = "media"),
        @Namespace(reference = "http://www.itunes.com/dtds/podcast-1.0.dtd", prefix = "itunes")
})


/**
 * Object representing the results of an RSS simple xml call, contained in the {@link RSS} result.
 * The RSS result contains a channel. The Channel contains a list of RSS {@link Item} objects.
 */

@Root(name = "channel", strict = false)
public class Channel {

    // Tricky part in Simple XML because the link is named twice
    @ElementList(entry = "link", inline = true, required = false)
    private List<ChannelLink> links;
    @ElementList(name = "item", inline = true)
    private List<Item> itemList;
    @Element
    private String title;
    @Element
    private String language;
    @Element(name = "ttl", required = false)
    private int ttl;
    @Element(name = "pubDate", required = false)
    private String pubDate;
    @ElementList(entry="image", inline=true, required = false)
    private List<ChannelImage> imageList;


    public String getTitle() {
        return title;
    }
    public void setTitle(String title) {
        this.title = title;
    }
    public List<ChannelImage> getImageList() {
        return imageList;
    }
    public List<Item> getItemList() {
        return itemList;
    }

}
