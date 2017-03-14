package com.fudd.fragment;

import android.app.AlertDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.content.DialogInterface;
import android.os.Bundle;

import com.fudd.adapter.TimeListAdapter;

/**
 * Created by fudd-office on 2017-3-14 16:15.
 * Email: 5036175@qq.com
 * QQ: 5036175
 * Description:
 */

public class ConfirmClearDialogFragment extends DialogFragment {
    private TimeListAdapter timeListAdapter;

//    public ConfirmClearDialogFragment() {
//        super();
//    }

    public static ConfirmClearDialogFragment instance(TimeListAdapter timeListAdapter) {
        ConfirmClearDialogFragment dialogFragment = new ConfirmClearDialogFragment();
        dialogFragment.timeListAdapter = timeListAdapter;
        return dialogFragment;
    }

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        return new AlertDialog.Builder(getActivity())
                .setTitle("11")
                .setMessage("222")
                .setPositiveButton("ok", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                        timeListAdapter.clear();
                    }
                })
                .setNegativeButton("cancel",null)
                .create();

    }
}
