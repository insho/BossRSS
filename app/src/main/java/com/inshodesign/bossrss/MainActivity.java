package com.inshodesign.bossrss;

import android.app.DialogFragment;
//import android.app.Fragment;
//import android.app.FragmentManager;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.drawable.Drawable;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

import com.inshodesign.bossrss.DB.InternalDB;
import com.inshodesign.bossrss.XMLModel.Channel;
import com.inshodesign.bossrss.XMLModel.RSS;
import com.inshodesign.bossrss.XMLModel.RSSList;
import com.squareup.picasso.Picasso;

import fr.castorflex.android.smoothprogressbar.SmoothProgressDrawable;
import rx.Observable;
import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

public class MainActivity extends AppCompatActivity implements MainFragment.OnMainOptionSelectedListener
        , AddFeedDialog.AddRSSDialogListener, RemoveFeedDialog.RemoveRSSDialogListener {

    //TargetPhoneGallery.addMediaURIListener
//    MainFragment mainFragment;
    private AddFeedDialog addFeedDialogFragment;
    private RemoveFeedDialog removeFeedDialogFragment;
    DisplayRSSFragment displayRSSFragment;

    private static final boolean debug = true;
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

    ;


    /**
     * The user has clicked on one of the feeds in the list
     * Either go to the listfragment and pull rss list, or
     * firstly (if the list is missing title and whatnot), pull title data into the db,
     * and then go into the list
     * <p>
     * Either way firstly run the progress bar and search for info
     **/


    //TODO remove this nullable RSS rss. I'm just going to pull it again in the fragment
    public void showRSSListFragment(RSSList rssList, @Nullable RSS rss) {

        //If it's missing the title, try to find it first
        if (!rssList.hasTitle()) {
            getRSS(rssList.getURL());
        }

        if (!rssList.hasImage() && rssList.getImageURL() != null) {
            getFeedIcon(getBaseContext(), rssList);
        }


        //If the RSS data is already downloaded, plug it in and move forward. If not, download data
        if (rss != null) {


//                DisplayRSSFragment rssFragment;

//                if(((DisplayRSSFragment) getSupportFragmentManager().findFragmentByTag("listfragment")) == null) {
//                    rssFragment = new DisplayRSSFragment();
//                } else {
//                    rssFragment = ((DisplayRSSFragment) getSupportFragmentManager().findFragmentByTag("listfragment"));
//                }
//
//                getSupportFragmentManager().beginTransaction()
//                        .addToBackStack("mainfragment")
//                        .replace(R.id.container, rssFragment)
//                        .commit();

            if (displayRSSFragment == null) {
                displayRSSFragment = new DisplayRSSFragment();

                Bundle args = new Bundle();
                args.putString("link", rssList.getURL());
                args.putString("title", rssList.getTitle());

                displayRSSFragment.setArguments(args);
            }

            getSupportFragmentManager().beginTransaction()
                    .addToBackStack("articlelistfrag")
                    .replace(R.id.container, displayRSSFragment)
                    .commit();


            showToolBarBackButton(true, rssList.getTitle());

        } else {
            //GET RSS DATA
        }

    }


    @Override
    public void onAddRSSDialogPositiveClick(String inputText) {
        getRSS(inputText);
    }

    @Override
    public void onRemoveRSSDialogDismiss() {
        removeFeedDialogFragment = null;
    }


    public void showRemoveDialog(Integer removeRowID) {
        if (removeFeedDialogFragment == null) {
            removeFeedDialogFragment = RemoveFeedDialog.newInstance(removeRowID);
            removeFeedDialogFragment.show(getFragmentManager(), "dialogRemove");
        }
    }


    @Override
    public void onRemoveRSSDialogPositiveClick(int removeid) {


        if (InternalDB.getInstance(getBaseContext()).deletedRSSFeed(removeid)) {
            //If successfuly deleted, update fragment adapter
            ((MainFragment) getSupportFragmentManager().findFragmentByTag("mainfragment")).filltheAdapter();
        } else {
            Toast.makeText(this, "Could not remove item", Toast.LENGTH_SHORT).show();
        }

    }


//    @Override
//    public void onBackPressed() {
//        super.onBackPressed();
//    }


    /***
     * After adding a RSS feed in the AddFeed dialog,
     */
    private Boolean saveURL(RSSList rssList) {
        int success = InternalDB.getInstance(getBaseContext()).saveEndPointURL(rssList);
        Log.d(TAG, "SAVE URL SUCCESS TAG: " + success);
        if (success == -2) {
            Toast.makeText(this, "Feed already exists", Toast.LENGTH_SHORT).show();
        } else {
            if (!isOnline()) {
                Toast.makeText(getBaseContext(), "Device is not online", Toast.LENGTH_SHORT).show();
            }
            ((MainFragment) getSupportFragmentManager().findFragmentByTag("mainfragment")).filltheAdapter();
            return true;
        }
        return false;
    }


    private void getFeedIcon(Context context, RSSList rssList) {
        int rowID = InternalDB.getInstance(getBaseContext()).getRowIDforURL(rssList.getURL());

        Picasso.with(context).load(rssList.getImageURL()).into(new TargetPhoneGallery(getContentResolver(), rowID, rssList.getTitle(), getBaseContext()));

        //TODO -- make it update the recycler view when its loaded
    }

    private void getRSS(final String endpoint) {
        final MainFragment mainFragment = (MainFragment) getSupportFragmentManager().findFragmentByTag("mainfragment");
        //Show progressbar

        if (mainFragment != null && mainFragment.isAdded()) {
            mainFragment.showProgressBar(true);
        }

        final RSSList rssList = new RSSList();

        //TODO -- SWITCH FOR REAL URL!
//        String endpoint = "http://www.thestar.com/feeds.topstories.rss";
        RSSService xmlAdapterFor = APIService.createXmlAdapterFor(RSSService.class, endpoint);
        Observable<RSS> rssObservable = xmlAdapterFor.getFeed(endpoint);

        rssObservable.subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Subscriber<RSS>() {
                    @Override
                    public void onCompleted() {
                        Log.d(TAG, "onCompleted() called");

                        if (mainFragment != null && mainFragment.isAdded()) {
                            mainFragment.showProgressBar(false);
                        }

                        if (!rssList.hasURL()) {
                            rssList.setURL(endpoint);
                        }

                        //If the rssList data was a success, try to download the image icon
                        if (saveURL(rssList) && rssList.getImageURL() != null) {

                            getFeedIcon(getBaseContext(), rssList);
                        }
                        ;


                    }


                    @Override
                    public void onError(final Throwable e) {
                        Log.d(TAG, "onError() called with: e = [" + e + "]");

                        if (mainFragment != null && mainFragment.isAdded()) {
                            mainFragment.showProgressBar(false);
                        }

                        if (!rssList.hasURL()) {
                            rssList.setURL(endpoint);
                        }
                        saveURL(rssList);


                    }

                    @Override
                    public void onNext(final RSS rss) {
                        Log.d(TAG, "onNext() called with: rss = [" + rss + "]");
                        if (rss.getChannel() != null) {


                            /** Assign a title for the feed*/
                            if (rss.getChannel() != null && !rssList.hasTitle()) {
                                rssList.setTitle(rss.getChannel().getTitle());
                            }

                            /** Get imageURL*/
                            if (rss.getChannel() != null && rss.getChannel().getImage() != null && rss.getChannel().getImage().getUrl() != null) {
                                rssList.setImageURL(rss.getChannel().getImage().getUrl());
                            }

                        }
                        ;
                    }
                });


    }


    private boolean isOnline() {
        ConnectivityManager cm =
                (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo netInfo = cm.getActiveNetworkInfo();
        return netInfo != null && netInfo.isConnectedOrConnecting();
    }

//    public void addMediaURItoDB(String URI, int rowID) {
//        InternalDB.getInstance(getBaseContext()).addMediaURItoDB(URI,rowID);
//    }

    public void showToolBarBackButton(Boolean showBack, CharSequence title) {

        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(showBack);
            getSupportActionBar().setTitle(title);
        }

    }

}

