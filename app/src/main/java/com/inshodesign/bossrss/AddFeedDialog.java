package com.inshodesign.bossrss;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.DialogFragment;
import android.content.DialogInterface;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.WindowManager;
import android.widget.EditText;


/**
 * Created by JClassic on 3/3/2017.
 */


public class AddFeedDialog extends DialogFragment {

    public AddRSSDialogListener mAddRSSDialogListener;
    String TAG = "TEST";

    public interface AddRSSDialogListener {
        void onDialogPositiveClick(String rssURI);
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

    public static AddFeedDialog newInstance(int title) {
        AddFeedDialog frag = new AddFeedDialog();
        Bundle args = new Bundle();
        args.putInt("title", title);
        frag.setArguments(args);
        return frag;
    }

    @Override
public void onActivityCreated(Bundle savedInstanceState) {
    super.onActivityCreated(savedInstanceState);

        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        LayoutInflater inflater = getActivity().getLayoutInflater();
        View dialogView = inflater.inflate(R.layout.fragment_dialog, null);
        builder.setView(dialogView);

        final EditText editText = (EditText) dialogView.findViewById(R.id.input);
        editText.setText(R.string.testurl);
    builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
        @Override
        public void onClick(DialogInterface dialog, int which) {
            Log.d(TAG,"mAddRSSDialogListener: " + mAddRSSDialogListener);
            mAddRSSDialogListener.onDialogPositiveClick(editText.getText().toString().trim());
            dialog.dismiss();
        }
    });

    builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
        @Override
        public void onClick(DialogInterface dialog, int which) {
            dialog.cancel();
        }
    });

        AlertDialog alertDialog = builder.create();
        alertDialog.show();
        getDialog().getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_VISIBLE);
}

}