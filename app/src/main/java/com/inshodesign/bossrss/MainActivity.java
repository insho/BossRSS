package com.inshodesign.bossrss;

//import android.app.Fragment;
//import android.app.FragmentManager;
import android.content.Context;
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
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

        import rx.Observable;
import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

public class MainActivity extends AppCompatActivity implements MainFragment.OnMainOptionSelectedListener
        , AddFeedDialog.AddRSSDialogListener, RemoveFeedDialog.RemoveRSSDialogListener {

    private AddFeedDialog addFeedDialogFragment;
    private RemoveFeedDialog removeFeedDialogFragment;
    RSSItemsFragment rssItemsFragment;
    private static boolean debug = true;
    private static final String TAG = "TEST -- MAIN";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        new InternalDB(this);
        if (savedInstanceState == null) {
            MainFragment mainFragment = new MainFragment();
            getSupportFragmentManager().beginTransaction()
                    .add(R.id.container, mainFragment, "mainfragment")
                    .commit();
        } else {
            //TODO -- handle data recycle
        }
    }

    protected void onSaveInstanceState(Bundle savedInstanceState) {
        super.onSaveInstanceState(savedInstanceState);

    }


    public boolean onCreateOptionsMenu(Menu menu) {

        if (getSupportActionBar() != null) {
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
                if (addFeedDialogFragment == null || !addFeedDialogFragment.isAdded()) {
                    addFeedDialogFragment = AddFeedDialog.newInstance(false);
                    addFeedDialogFragment.show(getFragmentManager(), "dialogAdd");
                }
                return true;
            default:
                return false;
        }
    }


    /** The user has entered a new RSS feed into the window and clicked OK */

    @Override
    public void onAddRSSDialogPositiveClick(String inputText) {
        InternalDB internalDBInstance = InternalDB.getInstance(getBaseContext());
        /** Check to DB to see if the new feed is a duplicate*/
        if(internalDBInstance.duplicateFeed(inputText.trim())) {
            Toast.makeText(this, "Feed already exists", Toast.LENGTH_SHORT).show();
        } else {
            /** Otherwise enter the URL into the DB and update the adapter */
            internalDBInstance.saveFeedURL(inputText.trim());
            if(((MainFragment) getSupportFragmentManager().findFragmentByTag("mainfragment")) != null) {
                ((MainFragment) getSupportFragmentManager().findFragmentByTag("mainfragment")).updateAdapter();
            }

            /** Now try to pull the feed. First check for internet connection. **/
            if (!isOnline()) {
                Toast.makeText(getBaseContext(), "Device is not online", Toast.LENGTH_SHORT).show();
            } else {
                getRSSFeed(inputText.trim());
            }


        }


    }



    /** Try to pull the feed.  */
    private void getRSSFeed(final String feedURL) {
        final MainFragment mainFragment = (MainFragment) getSupportFragmentManager().findFragmentByTag("mainfragment");
        if (mainFragment != null && mainFragment.isAdded()) {
            mainFragment.showProgressBar(true);
        }

        /** If data for this feed already exists in the database, don't try to pull it again**/
            final boolean rssListValuesAlreadyExist = InternalDB.getInstance(getBaseContext()).existingRSSListValues();

        RSSService xmlAdapterFor = APIService.createXmlAdapterFor(RSSService.class, "");
        Observable<RSS> rssObservable = xmlAdapterFor.getFeed(feedURL);

        rssObservable.subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Subscriber<RSS>() {

                    ArrayList<Channel.Item> items =  new ArrayList<>();

                        RSSList rssList = new RSSList(feedURL);

                    @Override
                    public void onCompleted() {
                        Log.d(TAG, "onCompleted() called");

                        /** Hide progress bar **/
                        if (mainFragment != null && mainFragment.isAdded()) {
                            mainFragment.showProgressBar(false);
                        }

                        /**Put the RSS List thumbnail URL and Title into the db */
                        if(!rssListValuesAlreadyExist) {
                            InternalDB.getInstance(getBaseContext()).addTitleandImageURLtoDB(rssList.getURL(),rssList.getTitle(),rssList.getImageURL());
                        }


                        /** Instantiate RSSItemsFragment **/
                        showRSSListFragment(rssList.getTitle(),rssList.getURL(),items);

                        /** Ty to download the image icon **/
                        if (!rssListValuesAlreadyExist && rssList.getImageURL() != null) {
                            getFeedIcon(getBaseContext(), rssList);
                        }

                    }


                    @Override
                    public void onError(final Throwable e) {

                        if(debug){Log.d(TAG, "onError() called with: e = [" + e + "]");}

                        if (mainFragment != null && mainFragment.isAdded()) {
                            mainFragment.showProgressBar(false);
                        }

                    }

                    @Override
                    public void onNext(final RSS rss) {
                        Log.d(TAG, "onNext() called with: rss = [" + rss + "]");
                        if (rss.getChannel() != null) {



                            /** Assign a title for the feed to the RSSList object */
                            if (!rssListValuesAlreadyExist && rss.getChannel() != null && !rssList.hasTitle()) {
                                rssList.setTitle(rss.getChannel().getTitle());
                            }

                            /** Assign the imageURL for the feed to the RSSList object */
                            if (!rssListValuesAlreadyExist && rss.getChannel() != null && !rssList.hasImageURL()) {
                                rssList.setImageURL(rss.getChannel().getImage().getUrl());
                            }

                            /**** Assign items list to "items" to be passed through to fragment in OnComplete somewhat redundant ***/
                            if (rss.getChannel() != null) {
                                items = new ArrayList<Channel.Item>(rss.getChannel().getItemList());
                            }


                        }
                        ;
                    }
                });


    }


    public void showRSSListFragment(String title, String feedURL, ArrayList<Channel.Item> items) {

        rssItemsFragment = RSSItemsFragment.newInstance(title,feedURL,items);
        getSupportFragmentManager().beginTransaction()
                .addToBackStack("rssItemsFragment")
                .replace(R.id.container, rssItemsFragment)
                .commit();
        showToolBarBackButton(true, title);
    }


    private void getFeedIcon(Context context, RSSList rssList) {
        int rowID = InternalDB.getInstance(getBaseContext()).getRowIDforURL(rssList.getURL());
        Picasso.with(context).load(rssList.getImageURL()).into(new TargetPhoneGallery(getContentResolver(), rowID, rssList.getTitle(), getBaseContext()));
    }




    /** Probably not the best way to get only one popup window showing at a time..**/

    @Override
    public void onRemoveRSSDialogDismiss() {
        removeFeedDialogFragment = null;
    }

    @Override
    public void onAddRSSDialogDismiss() {
        addFeedDialogFragment = null;
    }


    public void showRemoveDialog(Integer removeRowID) {
        if (removeFeedDialogFragment == null) {
            removeFeedDialogFragment = RemoveFeedDialog.newInstance(removeRowID);
            removeFeedDialogFragment.show(getFragmentManager(), "dialogRemove");
        }
    }


    /** User has chosen to remove a feed in the RemoveFeedDialog. Delete feed and update recyclerview in MainFragment **/
    @Override
    public void onRemoveRSSDialogPositiveClick(int removeid) {

        if (InternalDB.getInstance(getBaseContext()).deletedRSSFeed(removeid)) {
            ((MainFragment) getSupportFragmentManager().findFragmentByTag("mainfragment")).updateAdapter();
        } else {
            Toast.makeText(this, "Could not remove item", Toast.LENGTH_SHORT).show();
        }

    }


//    @Override
//    public void onBackPressed() {
//        super.onBackPressed();
//    }






    private boolean isOnline() {
        ConnectivityManager cm =
                (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo netInfo = cm.getActiveNetworkInfo();
        return netInfo != null && netInfo.isConnectedOrConnecting();
    }


    public void showToolBarBackButton(Boolean showBack, CharSequence title) {
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(showBack);
            getSupportActionBar().setTitle(title);
        }
    }

}

