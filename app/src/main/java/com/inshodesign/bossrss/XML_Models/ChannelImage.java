package com.inshodesign.bossrss.XML_Models;

import org.simpleframework.xml.Element;
import org.simpleframework.xml.Root;

/**
 * Object representing the RSS list channel image icon (Which
 * is saved and displayed in the {@link com.inshodesign.bossrss.Fragments.RSSListFragment} next
 * to the title)
 */
@Root(name = "image", strict = false)
public class ChannelImage {
    @Element(name = "title", required = false)
    private String title;
    @Element(name = "link", required = false)
    private String link;
    @Element(name = "url", required = false)
    private String url;

    public String getUrl() {
        return url;
    }
}
