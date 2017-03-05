package com.inshodesign.bossrss;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.content.ContextCompat;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import org.w3c.dom.Text;


/**
 * Created by JClassic on 3/3/2017.
 */


public class RemoveFeedDialog extends DialogFragment {

    public RemoveRSSDialogListener mRemoveRSSDialogListener;
    String TAG = "TEST";

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

//    private static RemoveFeedDialog instance;

    public static RemoveFeedDialog newInstance(int removeid) {
//        if(RemoveFeedDialog.instance == null) {
//
//        }
//
        RemoveFeedDialog frag = new RemoveFeedDialog();
        Bundle args = new Bundle();
        args.putInt("removeid", removeid);
        frag.setArguments(args);
        return frag;
    }

//    public static MyDialogController getInstance() {
//        if ( MyDialogController.instance == null ) {
//            MyDialogController.instance = new MyDialogController();
//        }
//
//        return MyDialogController.instance;
//    }


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

//        builder.setCustomTitle(customTitle);
        builder.setCancelable(true);


        return builder.create();
    }

    @Override
    public void onDismiss(DialogInterface dialog) {
        super.onDismiss(dialog);

        mRemoveRSSDialogListener.onRemoveRSSDialogDismiss();

    }
}