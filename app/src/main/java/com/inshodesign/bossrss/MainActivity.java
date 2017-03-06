package com.inshodesign.bossrss;

//import android.app.Fragment;
//import android.app.FragmentManager;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
        import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.SubMenu;
import android.widget.Toast;

import com.facebook.share.model.ShareLinkContent;
import com.inshodesign.bossrss.DB.InternalDB;
import com.inshodesign.bossrss.XMLModel.Channel;
//import com.inshodesign.bossrss.XMLModel.ItemParceble;
import com.inshodesign.bossrss.XMLModel.ParcebleItem;
import com.inshodesign.bossrss.XMLModel.RSS;
import com.inshodesign.bossrss.XMLModel.RSSList;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

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
    private Menu menu;
    MainFragment mainFragment;
//    private Toolbar toolbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        new InternalDB(this);


        if (savedInstanceState == null) {
            mainFragment = new MainFragment();
            getSupportFragmentManager().beginTransaction()
                    .add(R.id.container, mainFragment, "mainfragment")
                    .commit();
        } else {

            if(getSupportFragmentManager().getBackStackEntryCount() >0 &&
                    savedInstanceState.getString("title") != null) {
                showToolBarBackButton(true, savedInstanceState.getString("title"));
            }
        }


        /** Handle a URL intent from web, if user is on RSS feed there */
        Intent intent = getIntent();
        if (intent.getAction().equals(Intent.ACTION_SEND)) {
            String url = intent.getStringExtra(Intent.EXTRA_TEXT);

            if(url != null) {
                onAddRSSDialogPositiveClick(url.trim());
            }
        }
    }

    protected void onSaveInstanceState(Bundle savedInstanceState) {
        super.onSaveInstanceState(savedInstanceState);
        if(getSupportActionBar() != null && getSupportActionBar().getTitle() != null) {
            savedInstanceState.putString("title",getSupportActionBar().getTitle().toString());
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
//
//        Drawable addRSS = ContextCompat.getDrawable(this, R.drawable.ic_add_white_24dp);
//
//        menu.add(0, 1, 0, "Add Feed").setIcon(addRSS)
//                .setShowAsAction(MenuItem.SHOW_AS_ACTION_ALWAYS);
//        mOptionsMenu = menu;
//        return true;
    }

    private void hideOption(int id)
    {
        MenuItem item = menu.findItem(id);
        item.setVisible(false);
    }

    private void showOption(int id)
    {
        MenuItem item = menu.findItem(id);
        item.setVisible(true);
    }

//
//    private void updateOptionsMenu(Menu menu) {
//        if (menu != null) {
//            onPrepareOptionsMenu(menu);
//        }
//    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
Log.d("TEST","ITEM ID: " + item.getItemId());
        switch (item.getItemId()) {
            case android.R.id.home:
                onBackPressed();
                return true;
            case R.id.addFeed:
                if (addFeedDialogFragment == null || !addFeedDialogFragment.isAdded()) {
                    addFeedDialogFragment = AddFeedDialog.newInstance(false);
                    addFeedDialogFragment.show(getFragmentManager(), "dialogAdd");
                }


                return true;
            case R.id.shareFacebook:

                if(rssItemsFragment != null) {
                    Log.d("TEST","sharing");
                    rssItemsFragment.shareURL();
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
    public void getRSSFeed(final String feedURL) {
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
//                .timeout(12, TimeUnit.SECONDS)
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

                        showRSSListFragment(rssList,items);

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

                        Toast.makeText(getBaseContext(), "Could not retrieve feed", Toast.LENGTH_SHORT).show();

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
                                rssList.setImageURL(rss.getChannel().getImageURL());
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


    public void showRSSListFragment(RSSList rssList,ArrayList<Channel.Item> items) {

        /** Convert items that aren't parceble to the parceble class... **/
        ArrayList<ParcebleItem> parcebleItems = new ArrayList<>();
        for (Channel.Item item: items) {
            // use currInstance
            parcebleItems.add(new ParcebleItem(item));
        }

//        ParcebleItem converteditem = new ParcebleItem(items.get(0));

        rssItemsFragment = RSSItemsFragment.newInstance(rssList.getTitle(),rssList.getURL(),rssList.getImageURL(), parcebleItems);
        getSupportFragmentManager().beginTransaction()
                .addToBackStack("mainfragment")
                .replace(R.id.container, rssItemsFragment,"rssItemsFragment")
                .commit();
        showToolBarBackButton(true, rssList.getTitle());
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


    @Override
    public void onBackPressed() {
        super.onBackPressed();

        int backStackEntryCount = getSupportFragmentManager().getBackStackEntryCount();
        Log.d(TAG,"Backstack entry: " + backStackEntryCount);
        if (backStackEntryCount == 0) {
            showToolBarBackButton(false, "BossRSS");
//            if(((MainFragment) getSupportFragmentManager().findFragmentByTag("mainfragment")) == null || !((MainFragment) getSupportFragmentManager().findFragmentByTag("mainfragment")).isAdded())
                mainFragment = new MainFragment();
                getSupportFragmentManager().beginTransaction()
                        .replace(R.id.container, mainFragment, "mainfragment")
                        .commit();
        }
    }






    private boolean isOnline() {
        ConnectivityManager cm =
                (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo netInfo = cm.getActiveNetworkInfo();
        return netInfo != null && netInfo.isConnectedOrConnecting();
    }


    public void showToolBarBackButton(Boolean showBack, CharSequence title) {

//        if (toolbar == null) {
//            toolbar = (Toolbar) findViewById(R.id.toolbar);
//        }
//        setSupportActionBar(toolbar);
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(showBack);
            getSupportActionBar().setTitle(title);

            if(showBack) {
//                Drawable shareFacebook = ContextCompat.getDrawable(this, R.drawable.ic_facebook);
//getMenuInflater().
//
//                mOptionsMenu.add(0, 1, 0, "Share").setIcon(shareFacebook)
//                        .setShowAsAction(MenuItem.SHOW_AS_ACTION_ALWAYS);
                hideOption(R.id.addFeed);
                showOption(R.id.shareFacebook);
            } else {

                hideOption(R.id.shareFacebook);
                showOption(R.id.addFeed);

//                if(mOptionsMenu.getItem(1) != null) {
//                    mOptionsMenu.removeItem(1);
//                }
//                Drawable addRSS = ContextCompat.getDrawable(this, R.drawable.ic_add_white_24dp);
//                mOptionsMenu.add(0, 0, 0, "Add Feed").setIcon(addRSS)
//                        .setShowAsAction(MenuItem.SHOW_AS_ACTION_ALWAYS);
            }
//            updateOptionsMenu();
        }

    }



}

