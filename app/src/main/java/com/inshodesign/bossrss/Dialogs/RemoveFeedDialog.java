package com.inshodesign.bossrss.Dialogs;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;

import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.support.v4.content.ContextCompat;
import android.view.Gravity;
import android.widget.TextView;

import com.inshodesign.bossrss.Interfaces.RemoveRSSDialogListener;
import com.inshodesign.bossrss.R;

/**
 * Dialog for removing a saved RSS feed from the database
 */
public class RemoveFeedDialog extends DialogFragment {

    public RemoveRSSDialogListener mRemoveRSSDialogListener;

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        try {
            mRemoveRSSDialogListener = (RemoveRSSDialogListener) context;
        } catch (ClassCastException e) {
            throw new ClassCastException(context.toString() + " must implement mRemoveRSSDialogListener");
        }
    }
    public static RemoveFeedDialog newInstance(String rssUrl) {

        RemoveFeedDialog frag = new RemoveFeedDialog();
        Bundle args = new Bundle();
        args.putString("rssUrl", rssUrl);
        frag.setArguments(args);
        return frag;
    }

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());


        final String rssUrl = getArguments().getString("rssUrl");

        TextView title = new TextView(getActivity());
        title.setText(R.string.remove_dialog_title);
        title.setTextSize(18);
        title.setPadding(20,0,0,0);
        title.setGravity(Gravity.CENTER_VERTICAL);
        title.setTextColor(ContextCompat.getColor(getActivity(),android.R.color.black));
        title.setMinHeight(80);
        builder.setView(title);
        builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                mRemoveRSSDialogListener.onRemoveRSSDialogPositiveClick(rssUrl);
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

//    @Override
//    public void onDismiss(DialogInterface dialog) {
//        super.onDismiss(dialog);
//        mRemoveRSSDialogListener.onRemoveRSSDialogDismiss();
//    }
}