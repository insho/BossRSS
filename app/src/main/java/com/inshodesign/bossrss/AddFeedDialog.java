package com.inshodesign.bossrss;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.inshodesign.bossrss.DB.InternalDB;

import java.util.Arrays;
import java.util.List;

public class AddFeedDialog extends DialogFragment {

    public AddRSSDialogListener mAddRSSDialogListener;
    String TAG = "TEST";

    /** TEST FEEDS */
    List<String> testFeeds;
//    ArrayAdapter<String> adapter;

    public interface AddRSSDialogListener {
        void onAddRSSDialogPositiveClick(String rssURI);
        void onAddRSSDialogDismiss();
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        try {
            mAddRSSDialogListener = (AddRSSDialogListener) activity;
        } catch (ClassCastException e) {
            throw new ClassCastException(activity.toString() + " must implement mAddRSSDialogListener");
        }
    }

    public static AddFeedDialog newInstance(Boolean renamelist) {
        AddFeedDialog frag = new AddFeedDialog();
        Bundle args = new Bundle();
        args.putBoolean("renamelist", renamelist);
        frag.setArguments(args);
        return frag;
    }

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());

        //TODO -- Remove this after testing
        /** TESTING! Queue up the test feeds**/
        String[] feeds = getResources().getStringArray(R.array.testfeeds);
        testFeeds = Arrays.asList(feeds);


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
//        editText.setText(R.string.testurl);

        for(int i =0; i<testFeeds.size(); i++) {
            Log.d("TEST","TESTFEEDS: " + testFeeds.get(i));
            if(!InternalDB.getInstance(getActivity()).existingRSSListValues(testFeeds.get(i)) && editText.getText().toString().isEmpty()) {
                editText.setText(testFeeds.get(i));
                testFeeds.remove(i);
            }
        }

        builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                Log.d(TAG,"mAddRSSDialogListener: " + mAddRSSDialogListener);
                if(editText.getText().toString().trim().length() == 0) {
                    Toast.makeText(getActivity(), "Enter an RSS Feed", Toast.LENGTH_SHORT).show();
                } else {
                    mAddRSSDialogListener.onAddRSSDialogPositiveClick(editText.getText().toString().trim());
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

    @Override
    public void onDismiss(DialogInterface dialog) {
        super.onDismiss(dialog);

        mAddRSSDialogListener.onAddRSSDialogDismiss();

    }
}