//package com.inshodesign.bossrss.XMLModel;
//
//import org.simpleframework.xml.Attribute;
//import org.simpleframework.xml.Element;
//import org.simpleframework.xml.Namespace;
//import org.simpleframework.xml.Root;
//import org.simpleframework.xml.convert.Convert;
//import org.simpleframework.xml.convert.Converter;
//import org.simpleframework.xml.stream.InputNode;
//import org.simpleframework.xml.stream.OutputNode;
//
///**
// * Created by JClassic on 3/5/2017.
// */
//
//
//@Root()
//@Convert(Channel.ChannelConverter.class) // Specify the Converter
//public class Channel
//{
//    @Element
//    private String title;
//    @Element
//    private String link;
//    @Element
//    private String description;
//    @Namespace(reference = "http://www.w3.org/2005/Atom", prefix = "atom")
//    @Element()
//    private AtomLink atomLink;
//
//    // Ctor's / getter / setter ...
//
//
//    static class ChannelConverter implements Converter<Channel>
//    {
//        @Override
//        public Channel read(InputNode node) throws Exception
//        {
//            Channel channel = new Channel();
//
//            InputNode child;
//
//            // Iterate over all childs an get their values
//            while( ( child = node.getNext() ) != null )
//            {
//                switch(child.getName())
//                {
//                    case "title":
//                        channel.setTitle(child.getValue());
//                        break;
//                    case "item":
//                        channel.setTitle(child.getValue());
//                        break;
//
//                    case "description":
//                        channel.setDescription(child.getValue());
//                        break;
//                    case "link":
//                        /*
//                         * "link" can be either a <link>...</link> or
//                         * a <atom:link>...</atom:link>
//                         */
//                        if( child.getPrefix().equals("atom") == true )
//                        {
//                            AtomLink atom = new AtomLink();
//                            atom.setHref(child.getAttribute("href").getValue());
//                            atom.setRel(child.getAttribute("rel").getValue());
//                            atom.setType(child.getAttribute("type").getValue());
//                            channel.setAtomLink(atom);
//                        }
//                        else
//                        {
//                            channel.setLink(child.getValue());
//                        }
//                        break;
//                    default:
//                        throw new RuntimeException("Unknown Element found: " + child);
//                }
//            }
//
//            return channel;
//        }
//
//
//        @Override
//        public void write(OutputNode node, Channel value) throws Exception
//        {
//            /*
//             * TODO: Implement if necessary
//             */
//            throw new UnsupportedOperationException("Not supported yet.");
//        }
//
//    }
//
//
//    @Root
//    public static class AtomLink
//    {
//        @Attribute
//        private String href;
//        @Attribute
//        private String rel;
//        @Attribute
//        private String type;
//
//        // Ctor's / getter / setter ...
//    }
//}