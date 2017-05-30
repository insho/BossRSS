package com.inshodesign.bossrss.XML_Models;

import com.inshodesign.bossrss.XML_Models.Channel;

import org.simpleframework.xml.Attribute;
import org.simpleframework.xml.Element;
import org.simpleframework.xml.Root;

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
