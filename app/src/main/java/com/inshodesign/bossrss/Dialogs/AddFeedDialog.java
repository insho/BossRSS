package com.inshodesign.bossrss.Dialogs;

import android.app.AlertDialog;
import android.app.Dialog;

import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.support.v4.content.ContextCompat;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.inshodesign.bossrss.Database.InternalDB;
import com.inshodesign.bossrss.Interfaces.AddRSSDialogListener;
import com.inshodesign.bossrss.R;

import java.util.Arrays;
import java.util.List;

/**
 * Dialog for adding a new RSS Feed. New feed is entered into the edit text
 * and then input into the database
 */
public class AddFeedDialog extends DialogFragment {

    public AddRSSDialogListener mAddRSSDialogListener;
    String TAG = "TEST";

    /** TEST FEEDS */
    List<String> testFeeds;

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        try {


            mAddRSSDialogListener = (AddRSSDialogListener) context;
            Log.i(TAG,"mAddRSSDialogListener: " + mAddRSSDialogListener);
        } catch (ClassCastException e) {
            throw new ClassCastException(context.toString() + " must implement mAddUserDialogListener");
        }
    }

    public static AddFeedDialog newInstance() {
        return new AddFeedDialog();
    }

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());




        LayoutInflater inflater = getActivity().getLayoutInflater();
        View dialogView = inflater.inflate(R.layout.fragment_dialog, null);

        TextView title = new TextView(getActivity());
        title.setText(R.string.dialog_title);
        title.setTextSize(18);
        title.setMinHeight(80);
        title.setGravity(Gravity.CENTER);
        title.setTextColor(ContextCompat.getColor(getActivity(),android.R.color.white));
        title.setBackgroundColor(ContextCompat.getColor(getActivity(),R.color.colorPrimary));
        builder.setCustomTitle(title);
        builder.setView(dialogView);

        final EditText editText = (EditText) dialogView.findViewById(R.id.input);

        //TODO -- Remove this after testing
        String[] feeds = getResources().getStringArray(R.array.testfeeds);
        testFeeds = Arrays.asList(feeds);
        for(int i =0; i<testFeeds.size(); i++) {
            Log.d("TEST","TESTFEEDS: " + testFeeds.get(i));
            if(!InternalDB.getInstance(getActivity()).rssDataExistsInDB(testFeeds.get(i), true) && editText.getText().toString().isEmpty()) {
                editText.setText(testFeeds.get(i));
            }
        }

        //
        builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                Log.i(TAG,"mAddRSSDialogListener: " + mAddRSSDialogListener);
                if(editText.getText().toString().trim().length() == 0) {
                    Toast.makeText(getActivity(), "Enter an RSS Feed", Toast.LENGTH_SHORT).show();
                } else {
                    mAddRSSDialogListener.saveRSSFeed(editText.getText().toString().trim(),false);
                    dialog.dismiss();
                }
            }
        });

        builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.cancel();
            }
        });
        builder.setCancelable(true);


        return builder.create();
    }

//    @Override
//    public void onDismiss(DialogInterface dialog) {
//        super.onDismiss(dialog);
//        mAddRSSDialogListener.onAddRSSDialogDismiss();
//    }
}