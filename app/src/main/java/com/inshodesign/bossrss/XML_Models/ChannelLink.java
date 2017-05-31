package com.inshodesign.bossrss.XML_Models;

import org.simpleframework.xml.Attribute;
import org.simpleframework.xml.Text;

/**
 * Object representing link data for the {@link Channel}
 */

public class ChannelLink {
    @Attribute(required = false)
    public String href;

    @Attribute(required = false)
    public String rel;

    @Attribute(name = "type", required = false)
    public String contentType;

    @Text(required = false)
    public String link;
}