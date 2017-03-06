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
import android.widget.EditText;
import android.widget.TextView;

public class AddFeedDialog extends DialogFragment {

    public AddRSSDialogListener mAddRSSDialogListener;
    String TAG = "TEST";

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
        editText.setText(R.string.testurl);
        builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                Log.d(TAG,"mAddRSSDialogListener: " + mAddRSSDialogListener);
                mAddRSSDialogListener.onAddRSSDialogPositiveClick(editText.getText().toString().trim());
                dialog.dismiss();
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