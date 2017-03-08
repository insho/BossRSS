
package com.inshodesign.bossrss;

        import android.media.AudioManager;
        import android.media.MediaPlayer;
        import android.os.Bundle;
        import android.os.Handler;
        import android.os.SystemClock;
        import android.support.annotation.NonNull;
        import android.support.annotation.Nullable;
        import android.support.v4.app.Fragment;
        import android.support.v7.widget.LinearLayoutManager;
        import android.support.v7.widget.RecyclerView;
        import android.util.Log;
        import android.view.LayoutInflater;
        import android.view.MotionEvent;
        import android.view.View;
        import android.view.ViewGroup;
        import android.widget.LinearLayout;
        import android.widget.MediaController;
        import android.widget.Toast;

//        import com.github.piasy.rxandroidaudio.StreamAudioPlayer;
        import com.inshodesign.bossrss.Adapters.RSSContentsAdapter;
        import com.inshodesign.bossrss.Adapters.RxBus;
        import com.inshodesign.bossrss.XMLModel.AudioStream;
        import com.inshodesign.bossrss.XMLModel.ParcebleItem;
        import com.inshodesign.bossrss.XMLModel.RSSList;

        import java.io.File;
        import java.io.FileInputStream;
        import java.io.IOException;
        import java.nio.Buffer;
        import java.util.ArrayList;

        import rx.Observable;
        import rx.functions.Action1;
        import rx.schedulers.Schedulers;
        import rx.subscriptions.Subscriptions;


/**
 * Created by JClassic on 3/5/2017.
 */

public class RSSItemsFragment extends Fragment implements MediaPlayer.OnPreparedListener, MediaController.MediaPlayerControl, TouchEv{

    private RecyclerView mRecyclerView;
    RSSContentsAdapter mAdapter;
    private ArrayList<ParcebleItem> mDataset;
    private RxBus _rxBus = new RxBus();
    private long mLastClickTime = 0;

//    private StreamAudioPlayer mStreamAudioPlayer;
//    private byte[] mBuffer = new byte[1024];
//    private RxMediaPlayer mediaPlayer;
//    private MediaPlayer mp;


    /****/
    private LinearLayout anchorlayout;
    private MediaPlayer mediaPlayer;
    private MediaController mediaController;
    private Handler handler = new Handler();

    @Override
    public View onCreateView(LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_list, container, false);
        mRecyclerView = (RecyclerView) view.findViewById(R.id.recycler);
        anchorlayout = (LinearLayout) view.findViewById(R.id.anchorlayout);
        return view;
    }

    public static RSSItemsFragment newInstance(@Nullable final String listTitle, @Nullable String feedURL, @Nullable String imageURL,@NonNull final ArrayList<ParcebleItem> items) {
        final RSSItemsFragment fragment = new RSSItemsFragment();
        final Bundle args = new Bundle();
        args.putString("title",listTitle);
        args.putString("feedURL",feedURL);
        args.putString("imageURL",imageURL);
        args.putParcelableArrayList("items",items);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getContext());
        mRecyclerView.setLayoutManager(layoutManager);

        getArguments().getString("title");
        mDataset = getArguments().getParcelableArrayList("items");
        updateAdapter(mDataset);
    }



    private void updateAdapter(ArrayList<ParcebleItem> items) {
        mAdapter = new RSSContentsAdapter(items, _rxBus, getContext());

        _rxBus.toClickObserverable()
                .subscribe(new Action1<Object>() {
                    @Override
                    public void call(Object event) {

                        if (SystemClock.elapsedRealtime() - mLastClickTime < 1000){
                            return;
                        }
                        mLastClickTime = SystemClock.elapsedRealtime();

                        /** Stupid way of differentiating between short and long clicks... **/
                        if(event instanceof AudioStream) {
                            AudioStream audioStream = (AudioStream) event;
//                            Log.d("TEST - ItemsFrag","callback to MediaPlay");
////                            mCallback.getRSSFeed(rssList.getURL());
//                            //TODO -- play music from here
//                            Toast.makeText(getActivity(), "PLAY PRESSED - "  + audioStream.getPlay(), Toast.LENGTH_SHORT).show();
//
//                            mp = new MediaPlayer();
//                            mp.setAudioStreamType(AudioManager.STREAM_MUSIC);
//                            try {
//                                mp.setDataSource(audioStream.getPath());
//                            }  catch (Exception e) {
//                            // java.io.IOException: setDataSourceFD failed.: status=0x80000000
//                            e.printStackTrace();
//                            }
//
//                            RxMediaPlayer.play(RxMediaPlayer.from(audioStream.getPath()));

                            runMediaPlayer(audioStream);

                        }
                    }

                });
        mRecyclerView.setAdapter(mAdapter);
    }



//    private void determinePattern(){
        //TODO

        /**
         * Determine pattern of the adapter? Would that work?
         *
         * spit out a the type of container to fill with the stream...
         * */
//    }


    public RSSList getCurrentList() {
        RSSList rssList = new RSSList();
        if(getArguments() != null) {
            rssList.setURL(getArguments().getString("feedURL"));
            rssList.setImageURL(getArguments().getString("imageURL"));
            rssList.setTitle(getArguments().getString("title"));
        }
        return rssList;
    }

    private void runMediaPlayer(AudioStream audioStream) {


        mediaPlayer = new MediaPlayer();
        mediaPlayer.setOnPreparedListener(this);
        mediaController = new MediaController(getActivity());
        try {
            mediaPlayer.setDataSource(audioStream.getPath());
            mediaPlayer.prepare();
            mediaPlayer.start();
        } catch (IOException e) {
            Log.e("TEST", "Could not open file " + audioStream.getPath() + " for playback.", e);
        }
    }


    @Override
    public void onStop() {
       super.onStop();
        mediaController.hide();
        mediaPlayer.stop();
        mediaPlayer.release();
    }

    /** Media Player Stuff **/


//
//    @Override
//    private void onStop() {
//        super.onStop();
//        mediaController.hide();
//        mediaPlayer.stop();
//        mediaPlayer.release();
//    }

//    @Override
//    public boolean onTouchEvent(MotionEvent event) {
//        //the MediaController will hide after 3 seconds - tap the screen to make it appear again
//        mediaController.show();
//        return false;
//    }

    //--MediaPlayerControl methods----------------------------------------------------
    public void start() {
        mediaPlayer.start();
    }

    public void pause() {
        mediaPlayer.pause();
    }

    public int getDuration() {
        return mediaPlayer.getDuration();
    }

    public int getCurrentPosition() {
        return mediaPlayer.getCurrentPosition();
    }

    public void seekTo(int i) {
        mediaPlayer.seekTo(i);
    }

    public boolean isPlaying() {
        return mediaPlayer.isPlaying();
    }

    public int getBufferPercentage() {
        return 0;
    }

    public boolean canPause() {
        return true;
    }

    public boolean canSeekBackward() {
        return true;
    }

    public boolean canSeekForward() {
        return true;
    }
    //--------------------------------------------------------------------------------

    public void onPrepared(MediaPlayer mediaPlayer) {
        Log.d("Test", "onPrepared");
        mediaController.setMediaPlayer(this);
        mediaController.setAnchorView(anchorlayout);

        handler.post(new Runnable() {
            public void run() {
                mediaController.setEnabled(true);
                mediaController.show();
            }
        });
    }

    @Override
    public int getAudioSessionId() {
        return 0;
    }
}
