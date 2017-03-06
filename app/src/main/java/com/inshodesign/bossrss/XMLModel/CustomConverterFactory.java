//package com.inshodesign.bossrss.XMLModel;
//
//import java.io.IOException;
//import java.lang.annotation.Annotation;
//import java.lang.reflect.Type;
//
//import okhttp3.ResponseBody;
//import retrofit2.Converter;
//import retrofit2.Retrofit;
//
///**
// * Created by JClassic on 3/6/2017.
// */
//
//public class CustomConverterFactory extends Converter.Factory {
//
//    public static CustomConverterFactory create() {
//        return new CustomConverterFactory();
//    }
//    private final Channel channel;
//
//    public CustomConverterFactory(Channel channel) {
//        this.channel = channel;
//    }
//
//
//
//
//    @Override
//    public Converter<ResponseBody, ?> responseBodyConverter(Type type, Annotation[] annotations, Retrofit retrofit) {
//        if (Channel.class.equals(type)) {
//
//            return new Channel.ChannelConverter<>(channel);
////            return new Converter<ResponseBody, Channel>() {
////                @Override
////                public ChannelConverter(Channel value) throws IOException {
////                    return Channel(value.toString());
////                }
////            };
//        }
//        return null;
//    }
//}
