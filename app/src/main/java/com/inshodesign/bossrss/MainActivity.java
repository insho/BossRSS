package com.inshodesign.bossrss;

import android.app.DialogFragment;
//import android.app.Fragment;
//import android.app.FragmentManager;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.drawable.Drawable;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

import com.inshodesign.bossrss.DB.InternalDB;
import com.inshodesign.bossrss.XMLModel.Channel;
import com.inshodesign.bossrss.XMLModel.RSS;
import com.inshodesign.bossrss.XMLModel.RSSList;

import rx.Observable;
import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

public class MainActivity extends AppCompatActivity implements MainFragment.OnMainOptionSelectedListener, AddFeedDialog.AddRSSDialogListener, RemoveFeedDialog.RemoveRSSDialogListener {

    MainFragment mainFragment;
    private AddFeedDialog addFeedDialogFragment;
    private RemoveFeedDialog removeFeedDialogFragment;

    private static final boolean debug = true;
    private static final String TAG = "MainActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        new InternalDB(this);

        if (savedInstanceState == null) {
            mainFragment = new MainFragment();
            getSupportFragmentManager().beginTransaction()
                    .add(R.id.container, mainFragment)
                    .commit();
        }

    }

    public boolean onCreateOptionsMenu(Menu menu) {

            if(getSupportActionBar() != null) {
                getSupportActionBar().setTitle("BossRSS");
            }

            Drawable addRSS = ContextCompat.getDrawable(this, R.drawable.ic_add_white_24dp);
            menu.add(0, 1, 0, "Add Feed").setIcon(addRSS)
                    .setShowAsAction(MenuItem.SHOW_AS_ACTION_ALWAYS);
        return true;
    }



    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        switch (item.getItemId()) {
            case android.R.id.home:
                onBackPressed();
                return true;
            case 1: // addRSS
                if(addFeedDialogFragment == null || !addFeedDialogFragment.isAdded() ) {
                    addFeedDialogFragment = AddFeedDialog.newInstance(false);
                    addFeedDialogFragment.show(getFragmentManager(), "dialogAdd");
                }
                return true;
            default:
                return false;
        }
    };

    public void showRSSListFragment(RSSList rssList) {


    }



    @Override
    public void onAddRSSDialogPositiveClick(String inputText) {
//        Toast.makeText(this, "POS CLICK", Toast.LENGTH_SHORT).show();
        getRSS(inputText);
    }


    public void showRemoveDialog(Integer removeRowID) {
        if(removeFeedDialogFragment == null || !removeFeedDialogFragment.isAdded()) {
            removeFeedDialogFragment = RemoveFeedDialog.newInstance(removeRowID);
            removeFeedDialogFragment.show(getFragmentManager(), "dialogRemove");
        }



    }

    @Override
    public void onRemoveRSSDialogPositiveClick(int removeid) {


        if(InternalDB.getInstance(getBaseContext()).deletedRSSFeed(removeid)) {
            //If successfuly deleted, update fragment adapter
            mainFragment.filltheAdapter();
        } else {
            Toast.makeText(this, "Could not remove item", Toast.LENGTH_SHORT).show();
        }

    }


    /**
     * You can add a time and section chooser element to the main activity if you want

     **/



    @Override
    public void onBackPressed() {
        super.onBackPressed();
    }

    protected void onSaveInstanceState(Bundle savedInstanceState) {
        super.onSaveInstanceState(savedInstanceState);

    }



    private void saveURL(RSSList rssList) {
            int success = InternalDB.getInstance(getBaseContext()).saveEndPointURL(rssList);
            if(success == -2) {
                Toast.makeText(this, "Feed already exists", Toast.LENGTH_SHORT).show();
            } else if(!isOnline()) {
                Toast.makeText(getBaseContext(), "Device is not online", Toast.LENGTH_SHORT).show();
            } else if(success != 0) {
                   //It was a true error, and we couldn't get the feed
                    Toast.makeText(getBaseContext(), "Error, couldn't access RSS feed", Toast.LENGTH_SHORT).show();
            }
            mainFragment.filltheAdapter();
    }


    private void getRSS(final String endpoint) {



        final RSSList rssList = new RSSList();
        Channel channel = new Channel();

        //TODO -- SWITCH FOR REAL URL!
//        String endpoint = "http://www.thestar.com/feeds.topstories.rss";
 RSSService xmlAdapterFor = APIService.createXmlAdapterFor(RSSService.class, "http://www.thestar.com/");
        Observable<RSS> rssObservable = xmlAdapterFor.getFeed(endpoint);


        rssObservable.subscribeOn(Schedulers.io())
                 .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Subscriber<RSS>() {
            @Override
            public void onCompleted() {
                Log.d(TAG, "onCompleted() called");

                if(!rssList.hasURL()) {
                    rssList.setURL(endpoint);
                    Log.d(TAG,"Setting URL: " + endpoint);
                    Log.d(TAG,"GETURL FROM RSSLIT: " + rssList.getURL());
                }
                //Add URL to Key EndpointURL and notify user
                saveURL(rssList);

            }



            @Override
            public void onError(final Throwable e) {
                Log.d(TAG, "onError() called with: e = [" + e + "]");
                if(!rssList.hasURL()) {
                    rssList.setURL(endpoint);
                }
                saveURL(rssList);


            }

            @Override
            public void onNext(final RSS rss) {
                Log.d(TAG, "onNext() called with: rss = [" + rss + "]");
                if(rss.getChannel() != null ) {

                    /** Do something with the list */


                    /** Assign a title for the feed*/
                    if(rss.getChannel().getTitle() != null && !rssList.hasTitle()) {
                        rssList.setTitle(rss.getChannel().getTitle());
                    }

//                    if(rss.getChannel().getImage() != null && !rssList.hasImage()) {
//                        rssList.setTitle(rss.getChannel().getImage());
//                    };

                };
            }
        });

//
//        RSSFeedClient.getInstance(endpoint)
//                .getRSSFeed(endpoint)
//                .subscribeOn(Schedulers.io())
//                .observeOn(AndroidSchedulers.mainThread())
//                .subscribe(new Observer<RSS>() {
//                    @Override public void onCompleted() {
//                        if(debug){Log.d(TAG, "In onCompleted()");}
//
//
//                    }
//
//                    @Override public void onError(Throwable e) {
//                        e.printStackTrace();
//                        if(debug){Log.d(TAG, "In onError()");}
//                        Toast.makeText(getBaseContext(), "Unable to connect to RSS feed", Toast.LENGTH_SHORT).show();
//                    }
//
//                    @Override public void onNext(RSS RSS) {
//                        if(debug) {
//                            Log.d(TAG, "In onNext()");
////                            Log.d(TAG, "nytimesArticles: " + rssObjec);
//                        }
//
//
//                    }
//                });
    }


    public boolean isOnline() {
        ConnectivityManager cm =
                (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo netInfo = cm.getActiveNetworkInfo();
        return netInfo != null && netInfo.isConnectedOrConnecting();
    }
}

