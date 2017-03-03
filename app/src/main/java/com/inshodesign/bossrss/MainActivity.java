package com.inshodesign.bossrss;

import android.app.DialogFragment;
//import android.app.Fragment;
//import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;
import java.util.ArrayList;
import rx.Observer;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

public class MainActivity extends AppCompatActivity implements MainFragment.OnMainOptionSelectedListener, AddRSSDialog.AddRSSDialogListener {

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
                DialogFragment newFragment = AddRSSDialog.newInstance(2);
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
    public void onDialogPositiveClick(String rssURI) {
        Toast.makeText(this, "POS CLICK", Toast.LENGTH_SHORT).show();

    }



    private void getRSS(String URL) {

        RSSFeedClient.getInstance(URL)
                .getArticles(requesttype, section,time,apikey)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<NYTimesArticleWrapper>() {
                    @Override public void onCompleted() {
                        if(debug){Log.d(TAG, "In onCompleted()");}

                        if(loadedArticles != null) {

                            if (articleListFragment == null) {
                                articleListFragment = new ArticleListFragment();

                                Bundle args = new Bundle();
                                args.putParcelableArrayList("loadedArticles",loadedArticles);
                                articleListFragment.setArguments(args);
                            }

                            getSupportFragmentManager().beginTransaction()
                                    .addToBackStack("articlelistfrag")
                                    .replace(R.id.container, articleListFragment)
                                    .commit();


                            showToolBarBackButton(true,getArticleRequestType(requesttype));
                        }
                    }

                    @Override public void onError(Throwable e) {
                        e.printStackTrace();
                        if(debug){Log.d(TAG, "In onError()");}
                        Toast.makeText(getBaseContext(), "Unable to connect to NYTimes API", Toast.LENGTH_SHORT).show();
                    }

                    @Override public void onNext(NYTimesArticleWrapper nytimesArticles) {
                        if(debug) {
                            Log.d(TAG, "In onNext()");
                            Log.d(TAG, "nytimesArticles: " + nytimesArticles.num_results);
                        }

                        /***TMP**/
                        if(loadedArticles == null) {
                            loadedArticles = nytimesArticles.results;
                        }


                    }
                });
    }

}

