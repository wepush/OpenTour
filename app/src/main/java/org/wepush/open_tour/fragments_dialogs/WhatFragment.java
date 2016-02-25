package org.wepush.open_tour.fragments_dialogs;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.text.TextUtils;
import android.view.View;
import android.widget.CheckBox;
import android.widget.Switch;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import org.wepush.open_tour.R;
import org.wepush.open_tour.SettingTourActivity;
import org.wepush.open_tour.utils.Constants;
import org.wepush.open_tour.utils.Repository;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Locale;

/**
 * Created by antoniocoppola on 01/12/15.
 */


    public class WhatFragment extends DialogFragment {

        private Switch tb;
        private CheckBox rbAll;
        private CheckBox rbChurch;
        private CheckBox rbMuseum;
        private CheckBox rbVilla;
        private CheckBox rbBuildings;
        private CheckBox rbSquare;
        private CheckBox rbStatue;
        private CheckBox rbArc, rbTheater, rbBridges, rbCemetery, rbMarket, rbOthers, rbStructures;
        private ArrayList<String> whatToSeeItems;
        private boolean atLeastOneCheck = false;

        @Override
        public Dialog onCreateDialog(Bundle savedInstanceState) {

            whatToSeeItems = new ArrayList<String>();
            AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
            View view = getActivity().getLayoutInflater().inflate(R.layout.dialog_what, null);

            builder.setView(view);

            builder.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialog, int id) {

                    storeCheckedBox();


                }
            });


            tb = (Switch) view.findViewById(R.id.whatAll);


//            rbAll=(CheckBox) view.findViewById(R.id.whatAll);
            rbArc = (CheckBox) view.findViewById(R.id.whatArc);
            rbBuildings = (CheckBox) view.findViewById(R.id.whatBuildings);
            rbChurch = (CheckBox) view.findViewById(R.id.whatChurch);
            rbMuseum = (CheckBox) view.findViewById(R.id.whatMuseum);
            rbSquare = (CheckBox) view.findViewById(R.id.whatSquare);
            rbStatue = (CheckBox) view.findViewById(R.id.whatStatue);
            rbVilla = (CheckBox) view.findViewById(R.id.whatVilla);

            //aggiunzioni del 24 Giugno

            rbTheater = (CheckBox) view.findViewById(R.id.whatTheater);
//            rbMonuments=(CheckBox) view.findViewById(R.id.whatMonuments);
            rbBridges = (CheckBox) view.findViewById(R.id.whatBridges);
            rbCemetery = (CheckBox) view.findViewById(R.id.whatCemetery);
            rbMarket = (CheckBox) view.findViewById(R.id.whatMarket);
            rbOthers = (CheckBox) view.findViewById(R.id.whatOthers);
            rbStructures = (CheckBox) view.findViewById(R.id.whatStructures);


            Type type2 = new TypeToken<ArrayList<String>>() {
            }.getType();
            Gson gson2 = new Gson();
            whatToSeeItems = gson2.fromJson(Repository.retrieve(getActivity(), Constants.WHAT_SAVE, String.class), type2);


            if (whatToSeeItems == null) {
                setAllTrue();
                whatToSeeItems = new ArrayList<>();
            } else {
                setAllFalse();
                for (String s : whatToSeeItems) {
                    switch (s) {
                        case "all":
                            setAllTrue();
                            break;
                        case "Chiese, Oratori e Luoghi di culto":
                            rbChurch.setChecked(true);
                            break;

                        case "Musei e Gallerie d'Arte":
                            rbMuseum.setChecked(true);
                            break;
                        case "Ville, Giardini e Parchi":
                            rbVilla.setChecked(true);
                            break;
                        case "Palazzi e Castelli":
                            rbBuildings.setChecked(true);
                            break;
                        case "Piazze e Strade":
                            rbSquare.setChecked(true);
                            break;

                        case "Archi, Porte e Mura":
                            rbArc.setChecked(true);
                            break;
                        case "Teatri":
                            rbTheater.setChecked(true);
                            break;
//                        case "Monumenti e Gallerie d'Arte":
//                            rbMonuments.setChecked(true);
//                            break;
                        case "Ponti":
                            rbBridges.setChecked(true);
                            break;
                        case "Cimiteri e Memoriali":
                            rbCemetery.setChecked(true);
                            break;
                        case "Fiere e Mercati":
                            rbMarket.setChecked(true);
                            break;
                        case "Altri monumenti e Luoghi di interesse":
                            rbOthers.setChecked(true);
                            break;
                        case "Edifici":
                            rbStructures.setChecked(true);
                            break;
                        case "Statue e Fontane":
                            rbStatue.setChecked(true);
                            break;
                        default:
                            setAllFalse();
                    }
                }


            }

            tb.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (!(tb.isChecked())) {
                        tb.setChecked(false);
                        setAllFalse();
                    } else {
                        tb.setChecked(true);
                        setAllTrue();
                    }
                }
            });

            rbArc.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (!(rbArc.isChecked())) {
                        rbArc.setChecked(false);
                    } else {
                        rbArc.setChecked(true);
                    }
                }
            });
            rbBuildings.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (!(rbBuildings.isChecked())) {
                        rbBuildings.setChecked(false);
                    } else {
                        rbBuildings.setChecked(true);
                    }
                }
            });
            rbChurch.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (!(rbChurch.isChecked())) {
                        rbChurch.setChecked(false);
                    } else {
                        rbChurch.setChecked(true);
                    }
                }
            });
            rbMuseum.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (!(rbMuseum.isChecked())) {
                        rbMuseum.setChecked(false);
                    } else {
                        rbMuseum.setChecked(true);
                    }
                }
            });
            rbSquare.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (!(rbSquare.isChecked())) {
                        rbSquare.setChecked(false);
                    } else {
                        rbSquare.setChecked(true);
                    }
                }
            });
            rbStatue.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (!(rbStatue.isChecked())) {
                        rbStatue.setChecked(false);
                    } else {
                        rbStatue.setChecked(true);
                    }
                }
            });
            rbVilla.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (!(rbVilla.isChecked())) {
                        rbVilla.setChecked(false);
                    } else {
                        rbVilla.setChecked(true);
                    }
                }
            });

            rbTheater.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (!(rbTheater.isChecked())) {
                        rbTheater.setChecked(false);
                    } else {
                        rbTheater.setChecked(true);
                    }
                }
            });

//            rbMonuments.setOnClickListener(new View.OnClickListener() {
//                @Override
//                public void onClick(View v) {
//                    if (!(rbMonuments.isChecked())){
//                        rbMonuments.setChecked(false);
//                    }else {
//                        rbMonuments.setChecked(true);
//                    }
//                }
//            });

            rbBridges.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (!(rbBridges.isChecked())) {
                        rbBridges.setChecked(false);
                    } else {
                        rbBridges.setChecked(true);
                    }
                }
            });

            rbCemetery.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (!(rbCemetery.isChecked())) {
                        rbCemetery.setChecked(false);
                    } else {
                        rbCemetery.setChecked(true);
                    }
                }
            });

            rbMarket.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (!(rbMarket.isChecked())) {
                        rbMarket.setChecked(false);
                    } else {
                        rbMarket.setChecked(true);
                    }
                }
            });

            rbOthers.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (!(rbOthers.isChecked())) {
                        rbOthers.setChecked(false);
                    } else {
                        rbOthers.setChecked(true);
                    }
                }
            });

            rbStructures.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (!(rbStructures.isChecked())) {
                        rbStructures.setChecked(false);
                    } else {
                        rbStructures.setChecked(true);
                    }
                }
            });


            return builder.create();
        }  //fine classe


    private void setAllTrue(){

        tb.setChecked(true);
        rbArc.setChecked(true);
        rbBuildings.setChecked(true);
        rbChurch.setChecked(true);
        rbMuseum.setChecked(true);
        rbSquare.setChecked(true);
        rbStatue.setChecked(true);
        rbStatue.setChecked(true);
        rbVilla.setChecked(true);

        //CAMBIAMENTI DAL 24 GIUNO

        rbTheater.setChecked(true);
//            rbMonuments.setChecked(true);
        rbBridges.setChecked(true);
        rbCemetery.setChecked(true);
        rbMarket.setChecked(true);
        rbOthers.setChecked(true);
        rbStructures.setChecked(true);
    }

    private void setAllFalse(){
        rbArc.setChecked(false);
        rbBuildings.setChecked(false);
        rbChurch.setChecked(false);
        rbMuseum.setChecked(false);
        rbSquare.setChecked(false);
        rbStatue.setChecked(false);
        rbVilla.setChecked(false);

        //CAMBIAMENTI DEL 24 GIUGNO

        rbTheater.setChecked(false);
//            rbMonuments.setChecked(false);
        rbBridges.setChecked(false);
        rbCemetery.setChecked(false);
        rbMarket.setChecked(false);
        rbOthers.setChecked(false);
        rbStructures.setChecked(false);

//            rbAll.setChecked(false);
        tb.setChecked(false);


    }

    public void storeCheckedBox(){

        whatToSeeItems.clear();

        if (tb.isChecked()){

            whatToSeeItems.add("all");
            atLeastOneCheck=true;
        }

        if(rbChurch.isChecked()){

            whatToSeeItems.add(getResources().getString(R.string.churchs_oratories_worship));

            atLeastOneCheck=true;
        }

        if(rbMuseum.isChecked()){
            whatToSeeItems.add(getResources().getString(R.string.museums_and_art_galleries));
            atLeastOneCheck=true;
        }

        if(rbVilla.isChecked()){
            whatToSeeItems.add(getResources().getString(R.string.villas_gardens_parks));
            atLeastOneCheck=true;
        }

        if(rbBuildings.isChecked()){
            whatToSeeItems.add(getResources().getString(R.string.buildings));
            atLeastOneCheck=true;
        }

        if(rbSquare.isChecked()){
            whatToSeeItems.add(getResources().getString(R.string.squares_and_streets));
            atLeastOneCheck=true;
        }

        if(rbStatue.isChecked()){
            whatToSeeItems.add(getResources().getString(R.string.statues_funtains));
            atLeastOneCheck=true;
        }

        if(rbArc.isChecked()){
            whatToSeeItems.add(getResources().getString(R.string.arches_gates_walls));
            atLeastOneCheck=true;
        }

        //DAL 24 GIUGNO

        if(rbTheater.isChecked()){
            whatToSeeItems.add(getResources().getString(R.string.theaters));
            atLeastOneCheck=true;
        }

//            if(rbMonuments.isChecked()){
//                whatToSeeItems.add(getResources().getString(R.string.museums_and_art_galleries));
//                atLeastOneCheck=true;
//            }

        if(rbBridges.isChecked()){
            whatToSeeItems.add(getResources().getString(R.string.bridge));
            atLeastOneCheck=true;
        }

        if(rbCemetery.isChecked()){
            whatToSeeItems.add(getResources().getString(R.string.cemeteries_memorials));
            atLeastOneCheck=true;
        }

        if(rbMarket.isChecked()){
            whatToSeeItems.add(getResources().getString(R.string.fairs_markets));
            atLeastOneCheck=true;
        }

        if(rbOthers.isChecked()){
            whatToSeeItems.add(getResources().getString(R.string.others_monuments));
            atLeastOneCheck=true;
        }

        if(rbStructures.isChecked()){
            whatToSeeItems.add(getResources().getString(R.string.buildings));
            atLeastOneCheck=true;
        }

        Gson gson = new Gson();



        String json = gson.toJson(whatToSeeItems);
        Repository.save(getActivity(), Constants.WHAT_SAVE, json);
        setCorrectWhatSelection();

//            for (String s: whatToSeeItems){
//            }

    }

    private void setCorrectWhatSelection(){
        //check for at least 1 valid selection

        if (whatToSeeItems.size()==0 ){


            return;

        }

        if (TextUtils.equals(whatToSeeItems.get(0), "all")){
            SettingTourActivity.txtWhatToSee.setText(R.string.all_sites);
        } else if(whatToSeeItems.size()==1){
            SettingTourActivity.txtWhatToSee.setText(translateNamesSites(whatToSeeItems.get(0)));
        } else {
            SettingTourActivity.txtWhatToSee.setText(translateNamesSites(whatToSeeItems.get(0))+ ", "+getActivity().getResources().getString(R.string.and_more));
        }

    }

    private String translateNamesSites(String s){

        boolean isEnglish=false;
        if (TextUtils.equals(Locale.getDefault().getDisplayLanguage(), "English")){
            isEnglish=true;
        } else {
            isEnglish=false;
        }

        switch(s){
            case "all":
                if (isEnglish) {
                    return "all sites";
                }else {
                    return "Tutti i siti";
                }

            case "Chiese, Oratori e Luoghi di culto":


                if (isEnglish) {
                    return "Churches";
                }else {
                    return "Chiese";
                }

            case "Musei e Gallerie d'Arte":
                if (isEnglish) {
                    return "Museums";
                }else {
                    return "Musei e Gallerie d'arte";
                }


            case "Ville, Giardini e Parchi":
                if (isEnglish) {
                    return "Villas and Gardens";
                }else {
                    return "Ville e Giardini";
                }


            case "Palazzi e Castelli":
                if (isEnglish) {
                    return "Palaces and Castels";
                }else {
                    return "Palazzi e Castelli";
                }


            case "Piazze e Strade":
                if (isEnglish) {
                    return "Squares and Street";
                }else {
                    return "Piazze e Strade";
                }

            case "Archi, Porte e Mura":
                if (isEnglish) {
                    return "Arches, Ports and Walls";
                }else {
                    return "Archi, Porte, Mura";
                }


            case "Teatri":
                if (isEnglish) {
                    return "Theaters";
                }else {
                    return "Teatri";
                }


//                case "Monumenti e Gallerie d'Arte":
//                    if (isEnglish) {
//                        return "Monuments, Art Galleries";
//                    }else {
//                        return "Musei e Gallerie d'Arte";
//                    }


            case "Ponti":
                if (isEnglish) {
                    return "Bridges";
                }else {
                    return "Ponti";
                }


            case "Cimiteri e Memoriali":
                if (isEnglish) {
                    return "Cimiteri";
                }else {
                    return "Cemeteries";
                }


            case "Fiere e Mercati":
                if (isEnglish) {
                    return "Markets";
                }else {
                    return "Fiere e Mercati";
                }


            case "Altri monumenti e Luoghi di interesse":
                if (isEnglish) {
                    return "Other Monuments";
                }else {
                    return "Altri Monumenti";
                }


            case "Edifici":
                if (isEnglish) {
                    return "Buildings";
                }else {
                    return "Ediici";
                }


            case "Statue e Fontane":
                if (isEnglish) {
                    return "Statues";
                }else {
                    return "Statue";
                }


            default:
                if (isEnglish) {
                    return "All";
                }else {
                    return "Tutti i siti";
                }
        }


    }



}

