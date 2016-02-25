package org.wepush.open_tour.fragments_dialogs;


import android.app.Dialog;
import android.content.DialogInterface;
import android.graphics.drawable.AnimationDrawable;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.support.v7.app.AlertDialog;
import android.view.KeyEvent;
import android.view.View;
import android.widget.ImageView;

import org.wepush.open_tour.R;

/**
 * Created by antoniocoppola on 10/11/15.
 */
public class SettingTutorialFragment extends DialogFragment {

    private AlertDialog ad;

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {

        final AlertDialog.Builder builder = new AlertDialog.Builder(getActivity(), R.style.TutorialAlertDialogStyle);
        View view = getActivity().getLayoutInflater().inflate(R.layout.dialog_tutorial_setting, null);

        builder.setView(view);

        ImageView gifLoading = (ImageView) view.findViewById(R.id.settingRoggioAnimation);
        AnimationDrawable animDrawable=(AnimationDrawable) gifLoading.getBackground();
        animDrawable.start();

        // Click on OK
        builder.setPositiveButton(R.string.confirm, new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {


            }
        });


        builder.setOnKeyListener(new Dialog.OnKeyListener() {

            @Override
            public boolean onKey(DialogInterface arg0, int keyCode,
                                 KeyEvent event) {
                // TODO Auto-generated method stub
                if (keyCode == KeyEvent.KEYCODE_BACK) {
//                    startActivity(new Intent(getActivity(), SettingTourActivity.class));
//                    getActivity().finish();
//                    dialog.dismiss();
                }
                return true;
            }
        });


        return builder.create();
    }





}
