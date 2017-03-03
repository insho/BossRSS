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
import android.text.InputType;
import android.view.View;
        import android.view.Window;
        import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;


/**
 * Created by JClassic on 3/3/2017.
 */


public class AddRSSDialog extends DialogFragment {



    public AddRSSDialog(){

    }

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


    @Override
    public void onCreate(Bundle savedInstanceState) {

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
    };






}