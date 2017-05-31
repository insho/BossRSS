package com.inshodesign.bossrss;

import android.content.Context;
import android.content.Intent;
import android.media.MediaPlayer;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.Handler;
import android.os.SystemClock;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.MediaController;
import android.widget.Toast;
import com.facebook.share.model.ShareLinkContent;
import com.facebook.share.widget.ShareDialog;
import com.inshodesign.bossrss.Database.InternalDB;
import com.inshodesign.bossrss.Dialogs.AddFeedDialog;
import com.inshodesign.bossrss.Dialogs.RemoveFeedDialog;
import com.inshodesign.bossrss.Fragments.RSSListFragment;
import com.inshodesign.bossrss.Fragments.RSSItemsFragment;
import com.inshodesign.bossrss.Interfaces.AddRSSDialogListener;
import com.inshodesign.bossrss.Interfaces.OnFragmentInteractionListener;
import com.inshodesign.bossrss.Interfaces.RSSService;
import com.inshodesign.bossrss.Interfaces.RemoveRSSDialogListener;
import com.inshodesign.bossrss.Models.AudioStream;
import com.inshodesign.bossrss.XML_Models.Item;
import com.inshodesign.bossrss.XML_Models.RSS;
import com.inshodesign.bossrss.Models.RSSList;
import java.util.ArrayList;

import fr.castorflex.android.smoothprogressbar.SmoothProgressBar;
import rx.Observable;
import rx.Subscriber;
import rx.Subscription;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

/**
 * Main activity and traffic control between fragments
 */
public class MainActivity extends AppCompatActivity implements OnFragmentInteractionListener
        , AddRSSDialogListener, RemoveRSSDialogListener
        , MediaPlayer.OnPreparedListener, MediaController.MediaPlayerControl{


    private static final String TAG = "TEST-MAIN";
    private Menu menu;

    private MediaPlayer mediaPlayer;
    private MediaController mediaController;
    private Handler handler = new Handler();
    private Subscription subscription;
    private String searchinProgressURL = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        new InternalDB(this);
        setContentView(R.layout.activity_main);

        // Handle a URL intent from web, if user is on RSS feed there
        Intent intent = getIntent();
        if (intent.getAction().equals(Intent.ACTION_SEND)) {
            getWindow().getDecorView().setBackgroundResource(android.R.color.transparent);
            String url = intent.getStringExtra(Intent.EXTRA_TEXT);
            if(url != null) {
                saveRSSFeed(url.trim(),true);
                finish();
            }
        } else {
            Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
            setSupportActionBar(toolbar);
            if (savedInstanceState == null) {
                getSupportFragmentManager().beginTransaction()
                        .add(R.id.container, new RSSListFragment(), "mainfragment")
                        .commit();
            }

            if(savedInstanceState!=null) {
                searchinProgressURL = savedInstanceState.getString("searchinProgressURL");
                if(searchinProgressURL != null) {
                    getRSSFeed(searchinProgressURL);
                }


            }
        }
    }


    public boolean onCreateOptionsMenu(Menu menu) {

        if (getSupportActionBar() != null) {
            getSupportActionBar().setTitle("BossRSS");
        }
        this.menu = menu;
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu,menu);
        return true;

    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                onBackPressed();
                return true;
            case R.id.addFeed:
                if(getSupportFragmentManager().findFragmentByTag("dialogAdd") == null || !getSupportFragmentManager().findFragmentByTag("dialogAdd").isAdded()) {
                    AddFeedDialog.newInstance().show(getSupportFragmentManager(), "dialogAdd");
                }
                return true;
            case R.id.shareFacebook:
                if(!this.openFacebookDialog()) {
                    Toast.makeText(this, "Unable to access facebook", Toast.LENGTH_SHORT).show();
                }

                return true;
            default:
                return false;
        }
    }

    /**
     * Displays or hides the actionbar menu button
     * @param id id of menu bar button item
     * @param show boolean true to show, false to hide
     */
    private void showOption(int id, boolean show)
    {
        try {
            MenuItem item = menu.findItem(id);
            item.setVisible(show);
        } catch (NullPointerException e) {
            Log.e(TAG,"show/hide options failure in Mainactivity");
        }
    }


    /**
     * Checks if a new RSS feed exists in the db already, and if not, saves the feed. Called from AddFeedDialog and
     * when MainActivity receives an intent to add a new feed.
     *
     * @param inputText RSS feed address
     * @param isIntent bool true if RSS feed is being saved from an outside intent, false if not. If the intent is external,
     *                 the method displays a toast on success. If the RSS feed is being added from inside the app, there is no
     *                 need to make the toast.
     *
     * @see AddFeedDialog
     */
    public void saveRSSFeed(String inputText, boolean isIntent) {

        InternalDB internalDBInstance = InternalDB.getInstance(getBaseContext());

        // Check to DB to see if the new feed is a duplicate
        if(internalDBInstance.rssDataExistsInDB(inputText.trim(),true)) {
            Toast.makeText(this, "Feed already exists", Toast.LENGTH_SHORT).show();
        } else {
            // Otherwise enter the URL into the DB and update the adapter
            boolean saveSuccessful = internalDBInstance.saveFeedURL(inputText.trim());

            /* If the user is saving the feed from outside the app, show success message, and
             * do not try to add feed icon or content at this time. Otherwise do not show any message
             * and go straight to the feed. */
            if(saveSuccessful)  {
                if(isIntent) {
                    Toast.makeText(getBaseContext(), "Feed added to BossRSS", Toast.LENGTH_SHORT).show();
                } else {
                    updateRSSListFragment();

                    // Now try to go to the feed. First check for internet connection.
                    if (!isOnline()) {
                        Toast.makeText(getBaseContext(), "Device is not online", Toast.LENGTH_SHORT).show();
                    } else {
                        getRSSFeed(inputText.trim());
                    }
                }

            } else {
                Toast.makeText(getBaseContext(), "Unable to add RSS feed", Toast.LENGTH_SHORT).show();
            }

        }
    }


    /**
     * Retrieves RSS feed subscription from {@link APIService} for an RSS url, and passes the user on to {@link RSSItemsFragment} on success
     * @param feedURL url of rss feed
     */
    public void getRSSFeed(final String feedURL) {
        showProgressBar(true);
        searchinProgressURL = feedURL;
        // If all of the data for this feed already exists in the database, don't try to save it again
        final boolean rssListValuesAlreadyExist = InternalDB.getInstance(getBaseContext()).rssDataExistsInDB(feedURL.trim(), false);
        if(BuildConfig.DEBUG){Log.d(TAG, "EXISTING VALUE " + rssListValuesAlreadyExist);}

        RSSService xmlAdapterFor = APIService.createXmlAdapterFor(RSSService.class);
        Observable<RSS> rssObservable = xmlAdapterFor.getFeed(feedURL);

        rssObservable.subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Subscriber<RSS>() {

                    ArrayList<Item> items =  new ArrayList<>();
                    RSSList rssList = new RSSList(feedURL);

                    @Override
                    public void onCompleted() {

                        showProgressBar(false);
                        searchinProgressURL = null;
                        showRSSItemsFragmentForList(rssList,items);

                        // Put the RSS List thumbnail URL and Title into the db
                        if(!rssListValuesAlreadyExist) {
                            Log.i(TAG,rssList.getImageURL());
                            InternalDB.getInstance(getBaseContext()).addTitleandImageURLtoDB(rssList.getURL(),rssList.getTitle(),rssList.getImageURL());
                            // Ty to download the image icon and save it
                            InternalDB.getInstance(getBaseContext()).downloadRSSListIcon(getBaseContext(),rssList);
                        }
                    }


                    @Override
                    public void onError(final Throwable e) {
                        Log.e(TAG, "onError() called with: e = [" + e + "]");

                        showProgressBar(false);
                        searchinProgressURL = null;
                        Toast.makeText(getBaseContext(), "Could not retrieve feed", Toast.LENGTH_SHORT).show();
                    }

                    @Override
                    public void onNext(final RSS rss) {
                        Log.d(TAG, "onNext() called with: rss = [" + rss + "]");
                        if (rss.getChannel() != null) {


                            /* If the current RSS list item that is saved in the db has blank columns
                            * for title/image etc, pull them from the Channel xml response, assign them to the
                            * rssList object for this RSS list, and update the database entry for the list in the onComplete method */

                            //Set RSSList title
                            if(!rssList.hasTitle() && rss.getChannel().getTitle()!=null) {
                                rssList.setTitle(rss.getChannel().getTitle());
                            }

                            //Set RSSList title
                            if(!rssList.hasImageURL()) {
                                try {
                                    rssList.setImageURL(rss.getChannel().getImageList().get(0).getUrl());
                                } catch (NullPointerException e) {
                                    Log.e(TAG,"Nullpointer in assigning rssList image url");
                                }
                            }


                            items = new ArrayList<>(rss.getChannel().getItemList());

                        }
                    }
                });


    }


    /**
     * Shows {@link RSSItemsFragment} for a given RSS feed
     * @param rssList RSSList object for the RSS feed whose items are being shown
     * @param items List of items pulled from {@link APIService}, to be shown in RSSItemsFrag
     */
    public void showRSSItemsFragmentForList(RSSList rssList, ArrayList<Item> items) {
        RSSItemsFragment rssItemsFragment = RSSItemsFragment.newInstance(rssList.getTitle(),rssList.getURL(),rssList.getImageURL(), items);
        getSupportFragmentManager().beginTransaction()
                .addToBackStack("rssItemsFragment")
                .replace(R.id.container, rssItemsFragment,"rssItemsFragment")
                .commit();
        getSupportFragmentManager().executePendingTransactions();
        showActionBarBackButton(true, rssList.getTitle());
    }

    /**
     * Shows {@link RemoveFeedDialog} when user long-clicks on a saved RSS feed row
     * in the {@link RSSListFragment}
     * @param rssUrl url for saved RSS feed
     */
    public void showRemoveDialog(String rssUrl) {
        if(getSupportFragmentManager().findFragmentByTag("dialogRemove") == null || !getSupportFragmentManager().findFragmentByTag("dialogRemove").isAdded()) {
            RemoveFeedDialog.newInstance(rssUrl).show(getSupportFragmentManager(), "dialogRemove");
        }
    }

    /**
     * Recieves callback from {@link RemoveFeedDialog} when user clicks to OK to remove a saved RSS feed,
     * and deletes the saved RSS feed from the database
     * @param rssUrl Url of saved RSS feed
     */
    public void onRemoveRSSDialogPositiveClick(String rssUrl) {

        boolean deleteSuccessful = InternalDB.getInstance(getBaseContext()).deletedRSSFeed(getBaseContext(),rssUrl);
        if (deleteSuccessful) {
            updateRSSListFragment();
        } else {
            Toast.makeText(this, "Could not remove item", Toast.LENGTH_SHORT).show();
        }

    }


    @Override
    public void onBackPressed() {
        /*Fragment only goes one level deep (ItemsFragment), so on back pressed should
          always return the action bar to its original state */
        showActionBarBackButton(false,"BossRSS");
        super.onBackPressed();
    }

    /**
     * Checks whether device is online. Used when pulling user information & user timeline data
     * @return bool true if device can access a network
     */
    private boolean isOnline() {
        ConnectivityManager cm =
                (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo netInfo = cm.getActiveNetworkInfo();
        return netInfo != null && netInfo.isConnectedOrConnecting();
    }

    /**
     * Shows or hides the actionbar back arrow
     * @param showBack bool true to show the button
     * @param title title to show next to button
     */
    public void showActionBarBackButton(Boolean showBack, CharSequence title) {
        Log.i(TAG,"TITLE: " + title);
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(showBack);
            getSupportActionBar().setDisplayShowTitleEnabled(true);
            getSupportActionBar().setTitle(title);

            if(showBack) {
                showOption(R.id.addFeed,false);
                showOption(R.id.shareFacebook,true);
            } else {
                showOption(R.id.shareFacebook,false);
                showOption(R.id.addFeed,true);

            }
        }

    }

    /**
     * Opens facebook's {@link ShareLinkContent} window when user
     * clicks on the facebook icon in the navbar for a given RSS feed
     * @return bool true if success, false if rsslist or its url was null for some reason (i.e. fail)
     */
    public boolean openFacebookDialog() {
        if(getSupportFragmentManager().findFragmentByTag("rssItemsFragment") != null
                && getSupportFragmentManager().findFragmentByTag("rssItemsFragment").isAdded()) {
            RSSItemsFragment rssItemsFragment = (RSSItemsFragment) getSupportFragmentManager().findFragmentByTag("rssItemsFragment");

            RSSList rssList =  rssItemsFragment.getCurrentList();
            if(rssList == null || rssList.getURL() == null) {
                return false;
            } else {
                ShareLinkContent linkContent;

                if(rssList.getImageURL() != null) {
                    linkContent = new ShareLinkContent.Builder()
                            .setContentTitle(rssList.getTitle())
                            .setContentDescription("Sweet new RSS feed")
                            .setContentUrl(Uri.parse(rssList.getURL()))
                            .setImageUrl(Uri.parse(rssList.getImageURL()))
                            .build();
                } else {
                    linkContent = new ShareLinkContent.Builder()
                            .setContentTitle(rssList.getTitle())
                            .setContentDescription("Sweet new RSS feed")
                            .setContentUrl(Uri.parse(rssList.getURL()))
                            .build();
                }

                ShareDialog.show(this,linkContent);
                return true;
            }
        }
        return false;
    }

    /**
     * Searches for RSSListFragment and updates the adapter for the list of RSS Feeds (When items
     * have been added/removed)
     *
     * @see #onRemoveRSSDialogPositiveClick(String)
     * @see #saveRSSFeed(String, boolean)
     */
    private void updateRSSListFragment() {
        if(getSupportFragmentManager().findFragmentByTag("mainfragment") != null
                && getSupportFragmentManager().findFragmentByTag("mainfragment").isAdded()) {
            ((RSSListFragment) getSupportFragmentManager().findFragmentByTag("mainfragment")).updateAdapter();
        }
    }


    @Override
    protected void onResume() {
        super.onResume();
        showProgressBar(false);
    }

    /**
     * Shows progress bar during RSS lookups, hides otherwise
     * @param show boolean True for show, False for hide
     */
    public void showProgressBar(Boolean show) {
        SmoothProgressBar progressbar = (SmoothProgressBar) findViewById(R.id.progressbar);
        if(show) {
            progressbar.setVisibility(View.VISIBLE);
        } else {
            progressbar.setVisibility(View.INVISIBLE);

        }
    }


    /**
     * Checks how many milliseconds have elapsed since the last time "mLastClickTime" was updated
     * If enough time has elapsed, returns True and updates mLastClickTime.
     * This is to stop unwanted rapid clicks of the same button
     * @param elapsedMilliSeconds threshold of elapsed milliseconds before a new button click is allowed
     * @return bool True if enough time has elapsed, false if not
     */
    public static boolean isUniqueClick(int elapsedMilliSeconds, long lastClickTime) {
        if(SystemClock.elapsedRealtime() - lastClickTime > elapsedMilliSeconds) {
            return true;
        } else {
            return false;
        }
    }



    /** Audio stuff **/
    public void playAudio(AudioStream audioStream) {
        showProgressBar(true);

        mediaPlayer = new MediaPlayer();
        mediaPlayer.setOnPreparedListener(this);
        mediaController = new MediaController(this);

        try {
            mediaPlayer.setDataSource(audioStream.getPath());
            subscription =  RxMediaPlayer.play(mediaPlayer)
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe();
        } catch (Exception e) {
            Log.e("TEST", "Could not open file " + audioStream.getPath() + " for playback.", e);
        }
    }


    @Override
    protected void onPause() {
        if(subscription!=null) {
            subscription.unsubscribe();
        }
        super.onPause();
    }

    @Override
    protected void onDestroy() {
        if(subscription!=null) {
            subscription.unsubscribe();
        }
        super.onDestroy();
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putString("searchinProgressURL",searchinProgressURL);
    }

    // Media Player Stuff
    @Override
    public boolean onTouchEvent(MotionEvent event) {
        //the MediaController will hide after 3 seconds - tap the screen to make it appear again
        if(mediaController != null) {
            mediaController.show(5000);
        }
        return false;
    }

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

    public void onPrepared(MediaPlayer mediaPlayer) {
        showProgressBar(false);
        Log.d("Test", "onPrepared");
        mediaController.setMediaPlayer(this);

        LinearLayout anchorLayout = (LinearLayout) findViewById(R.id.container);
        mediaController.setAnchorView(anchorLayout);

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

