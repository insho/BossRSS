//package com.inshodesign.bossrss;
//
//import android.app.Activity;
//import android.app.Dialog;
//import android.app.DialogFragment;
//import android.content.Context;
//import android.content.DialogInterface;
//import android.os.Bundle;
////import android.support.v4.app.DialogFragment;
//import android.support.v7.app.AlertDialog;
//
///**
// * Created by JClassic on 3/3/2017.
// */
//public class YesNoDialog extends DialogFragment {
//    public YesNoDialog() {
//
//    }
//
//    public AddRSSDialogListener mAddRSSDialogListener;
//      public interface AddRSSDialogListener {
//                void onDialogPositiveClick(String rssURI);
//    }
//    @Override
//    public Dialog onCreateDialog(Bundle savedInstanceState) {
//        Bundle args = getArguments();
//        String title = args.getString("title", "");
//        String message = args.getString("message", "");
//
//        return new AlertDialog.Builder(getActivity())
//                .setTitle(title)
//                .setMessage(message)
//                .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
//                    @Override
//                    public void onClick(DialogInterface dialog, int which) {
////                        getTargetFragment().onActivityResult(getTargetRequestCode(), Activity.RESULT_OK, null);
//                        mAddRSSDialogListener.onDialogPositiveClick("XXX");
//                    }
//                })
//                .setNegativeButton(android.R.string.no, new DialogInterface.OnClickListener() {
//                    @Override
//                    public void onClick(DialogInterface dialog, int which) {
//                        getTargetFragment().onActivityResult(getTargetRequestCode(), Activity.RESULT_CANCELED, null);
//                    }
//                })
//                .create();
//    }
//
//        @Override
//    public void onAttach(Context context) {
//        super.onAttach(context);
//        try {
//            mAddRSSDialogListener = (AddRSSDialogListener) context;
//        } catch (ClassCastException e) {
//            throw new ClassCastException(context.toString() + " must implement mAddRSSDialogListener");
//        }
//    }
//
//
//}