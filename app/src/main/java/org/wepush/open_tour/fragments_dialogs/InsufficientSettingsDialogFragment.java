package org.wepush.open_tour.fragments_dialogs;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.view.KeyEvent;
import android.view.View;


import org.wepush.open_tour.R;
import org.wepush.open_tour.SettingTourActivity;


/**
 * Created by antoniocoppola on 22/07/15.
 */
public class InsufficientSettingsDialogFragment extends DialogFragment {


    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {

        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        View view = getActivity().getLayoutInflater().inflate(R.layout.dialog_insufficient_settings, null);

        builder.setView(view);

        // Click on OK
        builder.setPositiveButton(R.string.confirmSettingsLaunch, new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {

                startActivity(new Intent(getActivity(), SettingTourActivity.class));
                getActivity().finish();

            }
        });
//        builder.setNegativeButton(R.string.cancel, new DialogInterface.OnClickListener() {
//            public void onClick(DialogInterface dialog, int id) {
//                dismiss();
//            }
//        });

        builder.setOnKeyListener(new Dialog.OnKeyListener() {

            @Override
            public boolean onKey(DialogInterface arg0, int keyCode,
                                 KeyEvent event) {
                // TODO Auto-generated method stub
                if (keyCode == KeyEvent.KEYCODE_BACK) {
                    startActivity(new Intent(getActivity(), SettingTourActivity.class));
                    getActivity().finish();
//                    dialog.dismiss();
                }
                return true;
            }
        });

        return builder.create();
    }





}//fine classe

