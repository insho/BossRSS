package com.inshodesign.bossrss;

import android.app.DialogFragment;
import android.app.Fragment;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
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
    private static final boolean debug = false;

    private static final String TAG = "MainFragment";
    private ArrayList<RSSList> loadedRSSLists;
//    private Toolbar toolbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        new InternalDB(this);

        if (savedInstanceState == null) {
            mainFragment = new MainFragment();
//            showToolBarBackButton(false, "NYxBrewPunk");
            getSupportFragmentManager().beginTransaction()
                    .add(R.id.container, mainFragment)
                    .commit();


        } else {
//            showToolBarBackButton(savedInstanceState.getBoolean("showbackbutton"),savedInstanceState.getString("toolbartitle"));
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
//                AddRSSDialog.newInstance();
//                FragmentTransaction ft = getFragmentManager().beginTransaction();
//                Fragment prev = getFragmentManager().findFragmentByTag("dialog");
//                if (prev != null) {
//                    ft.remove(prev);
//                }
//                ft.addToBackStack(null);
//                ft.commit();
//                // Create and show the dialog.
////                DialogFragment newFragment = AddRSSDialog.newInstance();
////                newFragment.show(ft, "dialog");
//
//                FragmentManager fm = getFragmentManager();
//                AddRSSDialog dialogFragment = new AddRSSDialog();
//                dialogFrag.setTargetFragment(this, DIALOG_FRAGMENT);
//                dialogFragment.show(fm, "AddRSSDialog");
//
//                DialogFragment dialogFrag = AddRSSDialog.newInstance();
//                dialogFrag.setTargetFragment(this, DIALOG_FRAGMENT);
//                dialogFrag.show(getFragmentManager().beginTransaction(), "dialog");
//
//                AddRSSDialog addRSSDialog = new AddRSSDialog();
//                DialogFragment newFragment = AddRSSDialog.newInstance();
//                newFragment.show(ft, "dialog");
//                getSupportFragmentManager().beginTransaction()
//                        .show(R.id.container, mainFragment)
//                        .commit();

                FragmentManager fm = getFragmentManager();
                DialogFragment newFragment = AddRSSDialog.newInstance(1);
                newFragment.show(fm, "abc");

//
//                getSupportFragmentManager().beginTransaction().show(AddRSSDialog.newInstance())
//                getSupportFragmentManager().beginTransaction()
//                        .add(R.id.container, mainFragment)
//                        .commit();
// DialogFragment.show() will take care of adding the fragment
                // in a transaction.  We also want to remove any currently showing
                // dialog, so make our own transaction and take care of that here.


//                FragmentTransaction ft = getFragmentManager().beginTransaction();
//                Fragment prev = getFragmentManager().findFragmentByTag("dialog");
//                if (prev != null) {
//                    ft.remove(prev);
//                }
//                ft.addToBackStack(null);
//
//                // Create and show the dialog.
//                DialogFragment newFragment = AddRSSDialog.newInstance();
//                newFragment.show(ft, "dialog");


//                DialogFragment dialog = new YesNoDialog();
//                Bundle args = new Bundle();
//                args.putString("title", "title");
//                args.putString("message", "message");
//                dialog.setArguments(args);
////                dialog.setTargetFragment(this, 1);
//                dialog.show(getFragmentManager(), "tag");

                return true;
            default:
                return false;
        }

    };

    public void onMainOptionSelected(int position) {

//        if (position == 0) {
//            if (subFragment == null) {
//                subFragment = new SubFragment();
//            }
//
//            getSupportFragmentManager().beginTransaction()
//                    .addToBackStack("subFragment")
//                    .replace(R.id.container, subFragment)
//                    .commit();
//
//
//            showToolBarBackButton(true, "Article Categories");
//
//        } else {
//            Toast.makeText(this, "Choice not linked up yet...", Toast.LENGTH_SHORT).show();
//        }


    }

    /**
     * You can add a time and section chooser element to the main activity if you want

     **/
//    public void getArticles(String requesttype, String apikey) {
//
//        NYTimesClient.getInstance()
//                .getArticles(requesttype, section,time,apikey)
//                .subscribeOn(Schedulers.io())
//                .observeOn(AndroidSchedulers.mainThread())
//                .subscribe(new Observer<NYTimesArticleWrapper>() {
//                    @Override public void onCompleted() {
//                        if(debug){Log.d(TAG, "In onCompleted()");}
//
//                        if(loadedArticles != null) {
//
//                            if (articleListFragment == null) {
//                                articleListFragment = new ArticleListFragment();
//
//                                Bundle args = new Bundle();
//                                args.putParcelableArrayList("loadedArticles",loadedArticles);
//                                articleListFragment.setArguments(args);
//                            }
//
//                            getSupportFragmentManager().beginTransaction()
//                                    .addToBackStack("articlelistfrag")
//                                    .replace(R.id.container, articleListFragment)
//                                    .commit();
//
//
//                            showToolBarBackButton(true,getArticleRequestType(requesttype));
//                        }
//                    }
//
//                    @Override public void onError(Throwable e) {
//                        e.printStackTrace();
//                        if(debug){Log.d(TAG, "In onError()");}
//                        Toast.makeText(getBaseContext(), "Unable to connect to NYTimes API", Toast.LENGTH_SHORT).show();
//                    }
//
//                    @Override public void onNext(NYTimesArticleWrapper nytimesArticles) {
//                        if(debug) {
//                            Log.d(TAG, "In onNext()");
//                            Log.d(TAG, "nytimesArticles: " + nytimesArticles.num_results);
//                        }
//
//                        /***TMP**/
//                        if(loadedArticles == null) {
//                            loadedArticles = nytimesArticles.results;
//                        }
//
//
//                    }
//                });
//    }
//



    @Override
    public void onBackPressed() {
        super.onBackPressed();
//        int backStackEntryCount = getSupportFragmentManager().getBackStackEntryCount();
//        if (backStackEntryCount == 0) {
//            showToolBarBackButton(false, "NYxBrewPunk");
//        } else if(backStackEntryCount == 1) {
//            showToolBarBackButton(true, "Article Categories");
//        }
    }

//    public void showToolBarBackButton(Boolean showBack, CharSequence title) {
//        if (toolbar == null) {
//            toolbar = (Toolbar) findViewById(R.id.toolbar);
//        }
//        setSupportActionBar(toolbar);
//        if(getSupportActionBar() != null) {
//            getSupportActionBar().setDisplayHomeAsUpEnabled(showBack);
//            getSupportActionBar().setTitle(title);
//        }
//
//    }



    protected void onSaveInstanceState(Bundle savedInstanceState) {
        super.onSaveInstanceState(savedInstanceState);
//        if(getSupportActionBar() != null && getSupportActionBar().getTitle() != null) {
//            savedInstanceState.putString("toolbartitle", getSupportActionBar().getTitle().toString());
//        }
//        if((subFragment != null && subFragment.isVisible()) ||(articleListFragment != null && articleListFragment.isVisible())){
//            savedInstanceState.putBoolean("showbackbutton", true);
//        } else  {
//            savedInstanceState.putBoolean("showbackbutton", false);
//        }

    }



//    public void showAddRSSDialog() {
//        // Create an instance of the dialog fragment and show it
//        DialogFragment dialog = new AddRSSDialog();
//        dialog.show(getFragmentManager(), "AddRSSDialog");
//    }

    // The dialog fragment receives a reference to this Activity through the
    // Fragment.onAttach() callback, which it uses to call the following methods
    // defined by the NoticeDialogFragment.NoticeDialogListener interface

    @Override
    public void onDialogPositiveClick(String rssURI) {
        Toast.makeText(this, "POS CLICK", Toast.LENGTH_SHORT).show();
    }




}

