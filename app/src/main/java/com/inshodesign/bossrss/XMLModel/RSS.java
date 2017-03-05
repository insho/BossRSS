package com.inshodesign.bossrss.XMLModel;

/**
 * Created by JClassic on 3/3/2017.
 */

import com.inshodesign.bossrss.XMLModel.Channel;

import org.simpleframework.xml.Attribute;
import org.simpleframework.xml.Element;
import org.simpleframework.xml.Root;

@Root
public class RSS {

    @Attribute
    String version;

    @Element
    Channel channel;

    @Element
    Channel.Image image;

    public Channel.Image getImage() { return image;}

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
