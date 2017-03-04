//package com.inshodesign.bossrss;
//
//import android.content.ContentResolver;
//import android.content.Context;
//import android.graphics.Bitmap;
//import android.graphics.drawable.Drawable;
//import android.provider.MediaStore;
//import android.view.View;
//import android.widget.TextView;
//
//import com.squareup.picasso.Callback;
//import com.squareup.picasso.Picasso;
//import com.squareup.picasso.Target;
//
//import java.lang.ref.WeakReference;
//
///**
// * Created by JClassic on 3/4/2017.
// */
//
//public class ImageDownloadService {
//
////    save image
////    public static void imageDownload(Context context, String url){
////        Picasso.with(ctx)
////                .load("http://blog.concretesolutions.com.br/wp-content/uploads/2015/04/Android1.png")
////                .into(getTarget(url));
////    }
//
//    //target to save
////    private static Target getTarget(final String url){
////        Target target = new Target(){
////
////            @Override
////            public void onBitmapLoaded(final Bitmap bitmap, Picasso.LoadedFrom from) {
////                new Thread(new Runnable() {
////
////                    @Override
////                    public void run() {
////
////                        File file = new File(Environment.getExternalStorageDirectory().getPath() + "/" + url);
////                        try {
////                            file.createNewFile();
////                            FileOutputStream ostream = new FileOutputStream(file);
////                            bitmap.compress(Bitmap.CompressFormat.JPEG, 80, ostream);
////                            ostream.flush();
////                            ostream.close();
////                        } catch (IOException e) {
////                            Log.e("IOException", e.getLocalizedMessage());
////                        }
////                    }
////                }).start();
////
////            }
////
////            @Override
////            public void onBitmapFailed(Drawable errorDrawable) {
////
////            }
////
////            @Override
////            public void onPrepareLoad(Drawable placeHolderDrawable) {
////
////            }
////        };
////        return target;
////    }
//
//    String mURL;
//    Integer mID;
//
//    public ImageDownloadService() {}
//
//    public ImageDownloadService(String url, Integer id) {
//        this.mURL = url;
//        this.mID = mID;
//
//    }
//
//
//
//
//
//    private Target target = new Target() {
//        @Override
//        public void onBitmapLoaded(Bitmap bitmap, Picasso.LoadedFrom from) {
//        }
//
//        @Override
//        public void onBitmapFailed(Drawable errorDrawable) {
//        }
//
//        @Override
//        public void onPrepareLoad(Drawable placeHolderDrawable) {
//        }
//    };
//
//    public static void DownloadImage(Context context, String url, Integer id) {
////        Bitmap bitmap = new Bitmap();
//
//        final Callback loadedCallback = new Callback() {
//            @Override public void onSuccess()
//            {
//                //Do db stuff
//
//            }
//
//            @Override public void onError() {
//                //TODO error stuff
//
//
//            } };
////
//        bitmap.setTag(loadedCallback);
//
//        Picasso.with(context)
//                .load(mBeerData.getImage_url())
////                .transform(new RoundedTransformation(15, 0))
//                .resize(250, 300)
//                .centerInside()
//                .into(, loadedCallback);
//
//        Picasso.with(context).load(image[position]).into(new TargetPhoneGallery(view.getContentResolver(), "image name", "image desc"))
//    }
//
//
//
//
//}
