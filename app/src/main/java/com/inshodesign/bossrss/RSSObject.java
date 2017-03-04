//package com.inshodesign.bossrss;
//
//
//
//import java.util.List;
//
///**
// * Created by JClassic on 3/3/2017.
// */
//
//import org.simpleframework.xml.Attribute;
//import org.simpleframework.xml.Element;
//import org.simpleframework.xml.Text;
//
//@Element(name = "item")
//public class RSSObject {
//
//    @Element(name = "title")
//    private String title;
//
//    @Element(name = "link")
//    private String link;
//
//    @Element(name = "description")
//    private String description;
//
//    @Element(name = "enclosure")
//    private Enclosure enclosure;
//
//    @Element(name = "guid")
//    private String guid;
//
//    @Element(name = "pubDate")
//    private String pubDate;
//
//    @Element(name = "source")
//    private Source source;
//
//    public class Enclosure {
//
//        @Attribute(name = "url")
//        private String url;
//
//        @Attribute(name = "length")
//        private long length;
//
//        @Attribute(name = "type")
//        private String type;
//    }
//
//    public class Source {
//
//        @Attribute(name = "url")
//        private String url;
//
//        @Text
//        private String text;
//    }
//}
