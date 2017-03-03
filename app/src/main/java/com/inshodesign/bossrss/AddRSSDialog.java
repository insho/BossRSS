package com.inshodesign.bossrss;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;

import android.app.Dialog;
import android.app.DialogFragment;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.os.Message;
import android.support.annotation.Nullable;
import android.text.InputType;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
        import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;


/**
 * Created by JClassic on 3/3/2017.
 */


public class AddRSSDialog extends DialogFragment {

    public AddRSSDialogListener mAddRSSDialogListener;

    public interface AddRSSDialogListener {
        void onDialogPositiveClick(String rssURI);
//        void onDialogNegativeClick();
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        try {
            mAddRSSDialogListener = (AddRSSDialogListener) context;
        } catch (ClassCastException e) {
            throw new ClassCastException(context.toString() + " must implement mAddRSSDialogListener");
        }
    }

    public static AddRSSDialog newInstance(int title) {
        AddRSSDialog frag = new AddRSSDialog();
        Bundle args = new Bundle();
        args.putInt("title", title);
        frag.setArguments(args);
        return frag;
    }


//    @Override
//    public Dialog onCreateDialog(Bundle savedInstanceState) {
//
//        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
//        builder.setTitle(getString(R.string.dialog_title));
//
//        final EditText input = new EditText(getActivity());
//        input.setInputType(InputType.TYPE_CLASS_TEXT);
//        builder.setView(input);
//        builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
//            @Override
//            public void onClick(DialogInterface dialog, int which) {
//                mAddRSSDialogListener.onDialogPositiveClick(input.getText().toString().trim());
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
//        builder.show();
//
//        return super.onCreateDialog(savedInstanceState);
//    }

//    @Override
//    public void onCreate(Bundle savedInstanceState) {
//
//        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
//        builder.setTitle(getString(R.string.dialog_title));
//
//        final EditText input = new EditText(getActivity());
//        input.setInputType(InputType.TYPE_CLASS_TEXT);
//        builder.setView(input);
//        builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
//            @Override
//            public void onClick(DialogInterface dialog, int which) {
//                mAddRSSDialogListener.onDialogPositiveClick(input.getText().toString().trim());
//                    dialog.dismiss();
//                }
//            });
//
//        builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
//            @Override
//            public void onClick(DialogInterface dialog, int which) {
//                dialog.cancel();
//            }
//        });
//
//        builder.show();
//    };

//    @Override
//    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
////        View rootView = inflater.inflate(R.layout.dialog_fragment, container, false);
////        getDialog().setTitle("Simple Dialog");
//
//
//        /****
//         *
//
//         final View view=inflater.inflate(R.layout.fragment_dialog, null);
//         AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
//         builder.setTitle(R.string.dialog_title)
//         .setView(view);
//         final EditText input = (EditText)view. findViewById(R.id.input);
//         //        final EditText input = new EditText(getActivity());
//         input.setInputType(InputType.TYPE_CLASS_TEXT);
//         //        builder.setView(input);
//         builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
//        @Override
//        public void onClick(DialogInterface dialog, int which) {
//        mAddRSSDialogListener.onDialogPositiveClick(input.getText().toString().trim());
//        dialog.dismiss();
//        }
//        });
//
//         builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
//        @Override
//        public void onClick(DialogInterface dialog, int which) {
//        dialog.cancel();
//        }
//        });
//
//         builder.show();
//
//         *
//         */
//
//
//        return view;
//    }
//
@Override
public void onActivityCreated(Bundle savedInstanceState) {
    super.onActivityCreated(savedInstanceState);
    AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
    builder.setTitle(getString(R.string.dialog_title));




    final EditText input = new EditText(getActivity());
    input.setInputType(InputType.TYPE_CLASS_TEXT);
    builder.setView(input);
    builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
        @Override
        public void onClick(DialogInterface dialog, int which) {
            mAddRSSDialogListener.onDialogPositiveClick(input.getText().toString().trim());
            dialog.dismiss();
        }
    });

    builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
        @Override
        public void onClick(DialogInterface dialog, int which) {
            dialog.cancel();
        }
    });

    builder.show();

}


}