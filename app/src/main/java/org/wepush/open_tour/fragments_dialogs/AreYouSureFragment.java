package org.wepush.open_tour.fragments_dialogs;

import android.app.Activity;
import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.support.v7.app.AlertDialog;
import android.view.View;

import org.wepush.open_tour.R;

/**
 * Created by antoniocoppola on 24/11/15.
 */
public class AreYouSureFragment extends DialogFragment {

//    private static String responseFromFragment;
    private OnCompleteListener mListener;

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {


        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity(), R.style.AreYouSureAlertDialogStyle);
        View view = getActivity().getLayoutInflater().inflate(R.layout.dialog_areyousure, null);
//        responseFromFragment="Frase dal DialogFragment";

        builder.setView(view);
        // Click on OK
        builder.setPositiveButton(R.string.confirm, new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                mListener.onComplete(true);
                dismiss();

            }});

        builder.setNegativeButton(R.string.cancel,new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                dismiss();
            }});

        return builder.create();


    }



//    public static interface OnCompleteListener {
//        public abstract void onComplete(boolean responseFromFragment);
//    }

    public interface OnCompleteListener {
        void onComplete(boolean responseFromFragment);
    }



    public void onAttach(Activity activity) {
        super.onAttach(activity);
        try {
            mListener = (OnCompleteListener)activity;
        }
        catch (final ClassCastException e) {
            throw new ClassCastException(activity.toString() + " must implement OnCompleteListener");
        }
    }

}
