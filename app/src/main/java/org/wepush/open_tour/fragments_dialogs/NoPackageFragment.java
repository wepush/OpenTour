package org.wepush.open_tour.fragments_dialogs;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.provider.Settings;
import android.support.v4.app.DialogFragment;
import android.view.View;

import org.wepush.open_tour.ChooseCityActivity;
import org.wepush.open_tour.HomeActivity;
import org.wepush.open_tour.R;
import org.wepush.open_tour.SettingTourActivity;


/**
 * Created by antoniocoppola on 22/07/15.
 */
public class NoPackageFragment extends DialogFragment {


    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {

        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        View view = getActivity().getLayoutInflater().inflate(R.layout.dialog_nopackages, null);

        builder.setView(view);

        builder.setPositiveButton(R.string.downloadPackage, new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {

                startActivity(new Intent(getActivity(), ChooseCityActivity.class));
                getActivity().finish();
            }
        });


        return builder.create();
    }





}//fine classe
