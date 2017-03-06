package com.inshodesign.bossrss;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.view.Gravity;
import android.widget.TextView;



public class RemoveFeedDialog extends DialogFragment {

    public RemoveRSSDialogListener mRemoveRSSDialogListener;

    public interface RemoveRSSDialogListener {
        void onRemoveRSSDialogPositiveClick(int removeid);
        void onRemoveRSSDialogDismiss();

    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        try {
            mRemoveRSSDialogListener = (RemoveRSSDialogListener) activity;
        } catch (ClassCastException e) {
            throw new ClassCastException(activity.toString() + " must implement mAddRSSDialogListener");
        }
    }

    public static RemoveFeedDialog newInstance(int removeid) {

        RemoveFeedDialog frag = new RemoveFeedDialog();
        Bundle args = new Bundle();
        args.putInt("removeid", removeid);
        frag.setArguments(args);
        return frag;
    }


    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());


        final int removeid = getArguments().getInt("removeid",-1);

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
//                Log.d(TAG,"mRemoveRSSDialogListener: " + mRemoveRSSDialogListener);
                mRemoveRSSDialogListener.onRemoveRSSDialogPositiveClick(removeid);
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

        mRemoveRSSDialogListener.onRemoveRSSDialogDismiss();

    }
}