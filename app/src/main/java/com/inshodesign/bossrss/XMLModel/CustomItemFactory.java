//package com.inshodesign.bossrss.XMLModel;
//
//        import org.simpleframework.xml.convert.Convert;
//
//        import java.io.IOException;
//        import java.lang.annotation.Annotation;
//        import java.lang.reflect.Type;
//
//        import okhttp3.ResponseBody;
//        import retrofit2.Converter;
//        import retrofit2.Retrofit;
//
//
//
//public class CustomItemFactory extends Converter.Factory {
//
//    public static CustomItemFactory create() {
//        return new CustomItemFactory();
//    }
//
//    @Override
//    public Converter<ResponseBody, ?> responseBodyConverter(Type type, Annotation[] annotations, Retrofit retrofit) {
//        return CustomItemFactory.INSTANCE;
//    }
//
//    final static class JsonConverter implements Converter<ResponseBody, Channel.Item> {
//        static final JsonConverter INSTANCE = new JsonConverter();
//
//        @Override
//        public Channel.Item convert(ResponseBody responseBody) throws IOException {
//            try {
//                return new Channel.Item(responseBody.string());
//            } catch (JSONException e) {
//                throw new IOException("Failed to parse JSON", e);
//            }
//        }
//    }
////
////
////    @Override
////    public Converter<ResponseBody, ?> responseBodyConverter(Type type, Annotation[] annotations, Retrofit retrofit) {
////        if (Channel.Item.class.equals(type)) {
////            return new Converter<ResponseBody, String>() {
////                @Override
////                public String convert(ResponseBody value) throws IOException {
////                    return value.string();
////                }
////            };
////        }
////        return null;
////    }
//}
