package com.inshodesign.bossrss;

import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.Toast;
import com.facebook.share.model.ShareLinkContent;
import com.facebook.share.widget.ShareDialog;
import com.inshodesign.bossrss.DB.InternalDB;
import com.inshodesign.bossrss.XMLModel.Channel;
import com.inshodesign.bossrss.XMLModel.ParcebleItem;
import com.inshodesign.bossrss.XMLModel.RSS;
import com.inshodesign.bossrss.XMLModel.RSSList;
import com.squareup.picasso.Picasso;
import java.util.ArrayList;

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
        }



        /** Handle a URL intent from web, if user is on RSS feed there */
        Intent intent = getIntent();
        if (intent.getAction().equals(Intent.ACTION_SEND)) {
            String url = intent.getStringExtra(Intent.EXTRA_TEXT);

            if(url != null) {

                if(InternalDB.getInstance(getBaseContext()).duplicateFeed(url)) {
                    Toast.makeText(this, "Feed already exists", Toast.LENGTH_SHORT).show();
                } else {
                    /** Otherwise enter the URL into the DB */
                    InternalDB.getInstance(getBaseContext()).saveFeedURL(url);
                    Toast.makeText(this, "Added feed to BossRSS", Toast.LENGTH_SHORT).show();

                }
               finish();
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
                if(!this.openFacebookDialog()) {
                    Toast.makeText(this, "Unable to access facebook", Toast.LENGTH_SHORT).show();
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
            if(mainFragment!= null) {
                mainFragment.updateAdapter();
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
//        final MainFragment mainFragment = (MainFragment) getSupportFragmentManager().findFragmentByTag("mainfragment");
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
                            Log.d("TEST",rssList.getImageURL());
                            InternalDB.getInstance(getBaseContext()).addTitleandImageURLtoDB(rssList.getURL(),rssList.getTitle(),rssList.getImageURL());
                        }

                        /** Ty to download the image icon **/
                        if (rssList.getImageURL() == null) {
                            getFeedIcon(getBaseContext(), rssList);
                        }
                        /** Instantiate RSSItemsFragment **/
                        showRSSListFragment(rssList,items);



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
//                            if (!rssListValuesAlreadyExist && rss.getChannel() != null && rss.getChannel().getImage() != null && !rssList.hasImageURL()) {
//
//                                rssList.setImageURL(rss.getChannel().getImage().getUrl());
//                            }
                            /** Get imageURL*/
                            if (rss.getChannel() != null && rss.getChannel().getImage() != null && rss.getChannel().getImage().getUrl() != null) {
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


    public void showRSSListFragment(RSSList rssList,ArrayList<Channel.Item> items) {

        /** Convert items that aren't parceble to the parceble class... **/
        ArrayList<ParcebleItem> parcebleItems = new ArrayList<>();
        for (Channel.Item item: items) {
            parcebleItems.add(new ParcebleItem(item));
        }

        rssItemsFragment = RSSItemsFragment.newInstance(rssList.getTitle(),rssList.getURL(),rssList.getImageURL(), parcebleItems);
        getSupportFragmentManager().beginTransaction()
                .addToBackStack("mainfragment")
                .replace(R.id.container, rssItemsFragment,"rssItemsFragment")
                .commit();
        showToolBarBackButton(true, rssList.getTitle());


    }


    private void getFeedIcon(Context context, RSSList rssList) {
        int rowID = InternalDB.getInstance(getBaseContext()).getRowIDforURL(rssList.getURL());
//        Log.d("TEST", "In feedicon -- " + rowID);

        Picasso.with(context).load(rssList.getImageURL()).into(new TargetPhoneGallery(getContentResolver(), rowID, rssList.getTitle(), getBaseContext()));

        if(mainFragment != null && mainFragment.isAdded()) {
            mainFragment.updateAdapter();
        }
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

        if (InternalDB.getInstance(getBaseContext()).deletedRSSFeed(removeid) && mainFragment != null) {
            mainFragment.updateAdapter();
        } else {
            Toast.makeText(this, "Could not remove item", Toast.LENGTH_SHORT).show();
        }

    }


    @Override
    public void onBackPressed() {
        super.onBackPressed();

        int backStackEntryCount = getSupportFragmentManager().getBackStackEntryCount();
//        Log.d(TAG,"Backstack entry: " + backStackEntryCount);
        if (backStackEntryCount == 1) {
            showToolBarBackButton(false, "BossRSS");
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

        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(showBack);
            getSupportActionBar().setTitle(title);

            if(showBack) {
                hideOption(R.id.addFeed);
                showOption(R.id.shareFacebook);
            } else {

                hideOption(R.id.shareFacebook);
                showOption(R.id.addFeed);

            }
        }

    }

    public boolean openFacebookDialog() {
        if(rssItemsFragment != null) {
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


}

