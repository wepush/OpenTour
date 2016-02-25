package org.wepush.open_tour.fragments_dialogs;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.util.Log;
import android.view.View;
import android.widget.CheckBox;
import android.widget.Toast;

import org.wepush.open_tour.R;
import org.wepush.open_tour.SettingTourActivity;
import org.wepush.open_tour.utils.Constants;
import org.wepush.open_tour.utils.Repository;

/**
 * Created by Antonio on 22/04/2015.
 */
public class HowFragment extends DialogFragment {

    private CheckBox cbWalk;
    private CheckBox cbBike;
    private String howSelected;

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        View view = getActivity().getLayoutInflater().inflate(R.layout.dialog_how, null);


        builder.setView(view);

       builder.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
           public void onClick(DialogInterface dialog, int id) {

               savingSelection();

           }
       });
        builder.setNegativeButton(R.string.cancel, new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                dismiss();
            }
        });

        cbWalk=(CheckBox) view.findViewById(R.id.howWalk);
        cbBike=(CheckBox) view.findViewById(R.id.howBike);


        cbWalk.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(!cbWalk.isChecked()) {
                    cbWalk.setChecked(true);
                }
                if (cbBike.isChecked()){
                    cbBike.setChecked(false);

                }
            }
        });
        howSelected=Repository.retrieve(getActivity(),Constants.HOW_SAVE,String.class);

        if (howSelected!=null){
            if (howSelected.equals(getResources().getString(R.string.by_walk))){
                cbWalk.setChecked(true);
                cbBike.setChecked(false);
            } else {
                cbWalk.setChecked(false);
                cbBike.setChecked(true);

            }
        }




        cbBike.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(!cbBike.isChecked()){
                    cbBike.setChecked(true);
                }

                if (cbWalk.isChecked()){
                    cbWalk.setChecked(false);

                }
            }
        });


        return builder.create();
    }

    private void savingSelection() {
        if (
                (!(cbBike.isChecked())) &&
                        (!(cbWalk.isChecked()))
                ) {
            Toast.makeText(getActivity(),R.string.select_how,Toast.LENGTH_SHORT).show();
        } else if (
                  (cbBike.isChecked())
                ){

            Repository.save(getActivity(), Constants.HOW_SAVE, getResources().getString(R.string.by_bike));
            SettingTourActivity.txtHow.setText(R.string.by_bike);
        } else if (
                (cbWalk.isChecked())
                ){
            Repository.save(getActivity(), Constants.HOW_SAVE, getResources().getString(R.string.by_walk));
            SettingTourActivity.txtHow.setText(R.string.by_walk);

        }
    }

}//fine classe
