package com.inshodesign.bossrss.XML_Models;

import org.simpleframework.xml.Attribute;
import org.simpleframework.xml.Element;
import org.simpleframework.xml.Root;

/**
 * Container for an RSS simple xml call, containing one {@link Channel} object, with
 * a list of {@link Item} objects for each RSS item
 */

@Root
public class RSS {

    @Attribute
    String version;

    @Element
    Channel channel;


    public Channel getChannel() {
        return channel;
    }

    @Override
    public String toString() {
        return "RSS{" +
                "version='" + version + '\'' +
                ", channel=" + channel +
                '}';
    }
}
