package com.inshodesign.bossrss;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.TextView;

import org.w3c.dom.Text;


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

//    @Override
//public void onActivityCreated(Bundle savedInstanceState) {
//    super.onActivityCreated(savedInstanceState);
//
//        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
//        LayoutInflater inflater = getActivity().getLayoutInflater();
//        View dialogView = inflater.inflate(R.layout.fragment_dialog, null);
//        builder.setView(dialogView);
//
//        final EditText editText = (EditText) dialogView.findViewById(R.id.input);
//        editText.setText(R.string.testurl);
//    builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
//        @Override
//        public void onClick(DialogInterface dialog, int which) {
//            Log.d(TAG,"mAddRSSDialogListener: " + mAddRSSDialogListener);
//            mAddRSSDialogListener.onDialogPositiveClick(editText.getText().toString().trim());
//            dialog.dismiss();
//        }
//    });
//
//    builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
//        @Override
//        public void onClick(DialogInterface dialog, int which) {
//            dialog.cancel();
//        }
//    });
//
//        AlertDialog alertDialog = builder.create();
//        alertDialog.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_VISIBLE);
//        alertDialog.show();
//
//}

//
//    @Override
//    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
//
//
//        View view = inflater.inflate(R.layout.fragment_dialog, container, false);
//
//        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
//
////        View dialogView = inflater.inflate(R.layout.fragment_dialog, null);
//        builder.setView(view);
//
//        final EditText editText = (EditText) dialogView.findViewById(R.id.input);
//        editText.setText(R.string.testurl);
//        builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
//            @Override
//            public void onClick(DialogInterface dialog, int which) {
//                Log.d(TAG,"mAddRSSDialogListener: " + mAddRSSDialogListener);
//                mAddRSSDialogListener.onDialogPositiveClick(editText.getText().toString().trim());
//                dialog.dismiss();
//            }
//        });
//
//        builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
//            @Override
//            public void onClick(DialogInterface dialog, int which) {
//                dialog.cancel();
//            }
//        });
//
//        AlertDialog alertDialog = builder.create();
//        alertDialog.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_VISIBLE);
//        alertDialog.show();
//
//        return view;
//
//    }

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());

        LayoutInflater inflater = getActivity().getLayoutInflater();
        View dialogView = inflater.inflate(R.layout.fragment_dialog, null);
        View customtitleview = inflater.inflate(R.layout.customtitleview, null);

        builder.setView(dialogView);

        TextView customTitle = (TextView) customtitleview.findViewById(R.id.customtitle);

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

//        builder.setCustomTitle(customTitle);
        builder.setCancelable(true);


        return builder.create();
    }
}