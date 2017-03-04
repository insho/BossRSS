package com.inshodesign.bossrss;

import android.app.DialogFragment;
//import android.app.Fragment;
//import android.app.FragmentManager;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.drawable.Drawable;
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

public class MainActivity extends AppCompatActivity implements MainFragment.OnMainOptionSelectedListener, AddFeedDialog.AddRSSDialogListener {

    MainFragment mainFragment;
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
            menu.add(0, 1, 0, "Cancel").setIcon(addRSS)
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
                DialogFragment newFragment = AddFeedDialog.newInstance(2);
                newFragment.show(getFragmentManager(), "dialog");
                return true;
            default:
                return false;
        }
    };

    public void onMainOptionSelected(int position) {


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

    @Override
    public void onDialogPositiveClick(String inputText) {
//        Toast.makeText(this, "POS CLICK", Toast.LENGTH_SHORT).show();
        getRSS(inputText);
    }

    private void saveURL(RSSList rssList) {
            InternalDB.getInstance(getBaseContext()).saveEndPointURL(rssList);
            Toast.makeText(this, "Saved ", Toast.LENGTH_SHORT).show();

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

}

