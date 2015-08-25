package org.wepush.open_tour.fragments_dialogs;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.Fragment;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.Switch;
import android.widget.TextView;

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
 * Created by Antonio on 10/04/2015.
 */
public class IntroPagerFragment extends Fragment {

    public static final String KEY_IMAGE_PLACEHOLDER="org.wepush.opentour.image.walkthrough.placeholder";
    public static final String KEY_STRING_TITLE_PLACEHOLDER="org.wepush.opentour.string.walkthrough.title";
    public static final String KEY_STRING_BODY_PLACEHOLDER="org.wepush.opentour.string.walkthrough.body";

    public static IntroPagerFragment newInstance(int imageId,int stringTitleId, int stringBodyId) {
        final Bundle bundle = new Bundle();
        final IntroPagerFragment fragment = new IntroPagerFragment();

        bundle.putInt(KEY_IMAGE_PLACEHOLDER, imageId);
        bundle.putInt(KEY_STRING_TITLE_PLACEHOLDER,stringTitleId);
        bundle.putInt(KEY_STRING_BODY_PLACEHOLDER,stringBodyId);

        fragment.setArguments(bundle);

        return fragment;
    }



    @Override
    public View onCreateView(LayoutInflater inflater,  ViewGroup container, Bundle savedInstanceState){

        getActivity().getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);

        final ViewGroup viewGroup = (ViewGroup) inflater.inflate(R.layout.fragment_intro_base_pager,null);
        final ImageView imageView = (ImageView) viewGroup.findViewById(R.id.imageIntro);
        final TextView bodyView=(TextView) viewGroup.findViewById(R.id.txtBodyWalkThroughDescription);
        final TextView titleView = (TextView) viewGroup.findViewById(R.id.txtTitleWalkThroughDescription);

        final Bundle args=getArguments();

        imageView.setImageResource(args.getInt(KEY_IMAGE_PLACEHOLDER));
        titleView.setText(getResources().getString(args.getInt(KEY_STRING_TITLE_PLACEHOLDER)));
        bodyView.setText(getResources().getString(args.getInt(KEY_STRING_BODY_PLACEHOLDER)));
        return viewGroup;
    }

    /**
     * Created by Antonio on 24/04/2015.
     */
    public static class RecapTimeLineFragment extends Fragment {

        @Override
        public View onCreateView(LayoutInflater inflater, ViewGroup container,Bundle savedInstanceState) {
            View view = inflater.inflate(R.layout.fragment_recap_timeline, null);


            return view;
        }
    }

    /**
     * Created by Antonio on 13/04/2015.
     */
    public static class RectSelectorFragment extends Fragment {

        @Override
        public View onCreateView(LayoutInflater inflater, ViewGroup container,Bundle savedInstanceState) {
            View view = inflater.inflate(R.layout.fragment_rect_selector, null);
            return view;
        }


    }

    /**
     * Created by Antonio on 19/04/2015.
     */
    public static class SettingDialogFragment extends DialogFragment {

        private static int dialogType=0;
        private View dialogView;
        private static Context diagContext;

        private CheckBox cbWalk;
        private CheckBox cbBike;
        private String[] selectionItems;

        public SettingDialogFragment() {
            // Empty constructor required for DialogFragment
        }

        public static SettingDialogFragment newInstance(Context context,int idLayout,int diagType) {
            diagContext=context;
            SettingDialogFragment frag = new SettingDialogFragment();
            Bundle args = new Bundle();
            args.putInt("layout", idLayout);
            if (diagType==0){
                dialogType=0;

            } else {
                dialogType=1;
            }
            frag.setArguments(args);
            return frag;
        }



        @Override
        public Dialog onCreateDialog(Bundle savedInstanceState) {

            Log.d("miotag"," ON CREATE DIALOG");

            if(dialogType==0){
                Log.d("miotag", "selectionItems: HOW");
                selectionItems=new String[]{
                    "A Piedi",
                    "In Bici"
                };

            } else {
                Log.d("miotag","entro selezione Items");
                selectionItems=new String[]{
                        "Tutte",
                        "Chiese",
                        "Palazzi",
                        "Archi",
                        "Statue"
                };
            }

            LayoutInflater inflater = getActivity().getLayoutInflater();
            int resourceIdLayout=getArguments().getInt("layout");
            dialogView=inflater.inflate(resourceIdLayout,null);

    //        ListView lv=(ListView)dialogView.findViewById(R.id.listViewDialog);



        // Use the Builder class for convenient dialog construction
            AlertDialog.Builder builder = new AlertDialog.Builder(diagContext);

    //        ArrayAdapter< String> adapter =
    //                new ArrayAdapter<String>(diagContext, android.R.layout.simple_list_item_1, selectionItems);
    //        lv.setAdapter(adapter);
              builder.setView(inflater.inflate(resourceIdLayout, null))
                          .setPositiveButton(R.string.confirm, new DialogInterface.OnClickListener() {
                              public void onClick(DialogInterface dialog, int id) {


                                  validatingRadioButton(dialogType);
                              }
                          })
                          .setNegativeButton(R.string.cancel, new DialogInterface.OnClickListener() {
                              public void onClick(DialogInterface dialog, int id) {
                                  dismiss();
                              }
                          });
            // Create the AlertDialog object and return it
           return builder.create();

        }




    private void validatingRadioButton(int d){
        if (d==0){
            //how
            Log.d("miotag","dialog selected: HOW");

            Log.d("miotag","Control over radiobuttons: rdWalk: "+", "+cbWalk.isChecked()+", rdBike: "+", "+cbBike.isChecked());

            if (cbWalk.isChecked() && cbBike.isChecked()){
                Log.d("miotag","entrambi Checked");
               cbBike.toggle();
                cbWalk.toggle();

            }else if (
                    (!(cbWalk.isChecked())) &&
                    (!(cbBike.isChecked()))
                    )
            {
                Log.d("miotag","neither checked");



            } else if
                (cbWalk.isChecked()){
                    Repository.save(getActivity(), Constants.HOW_SAVE, "walk");
                Log.d("miotag", "checked on Walk. Saved: " + Repository.retrieve(getActivity(), Constants.HOW_SAVE, String.class));
                }
            else {
                Repository.save(getActivity(),Constants.HOW_SAVE,"bike");
                Log.d("miotag","checked on Bike. Saved: "+Repository.retrieve(getActivity(), Constants.HOW_SAVE,  String.class));
            }

        } else {
            Log.d("miotag", "dialog selected: What");{
                Log.d("miotag","SOON");
            }
        }
    }


    }

    /**
     * Created by Antonio on 22/04/2015.
     */
    public static class WhatFragment extends DialogFragment {

        private Switch tb;
        private CheckBox rbAll;
        private CheckBox rbChurch;
        private CheckBox rbMuseum;
        private CheckBox rbVilla;
        private CheckBox rbBuildings;
        private CheckBox rbSquare;
        private CheckBox rbStatue;
        private CheckBox rbArc,rbTheater,rbMonuments,rbBridges,rbCemetery,rbMarket,rbOthers,rbStructures;
        private ArrayList<String> whatToSeeItems;
        private boolean atLeastOneCheck=false;

        @Override
        public Dialog onCreateDialog(Bundle savedInstanceState) {
            whatToSeeItems=new ArrayList<String>();
            AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
            View view = getActivity().getLayoutInflater().inflate(R.layout.dialog_what, null);

            builder.setView(view);

            builder.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialog, int id) {

                    storeCheckedBox();


                }
            });
//            builder.setNegativeButton(R.string.cancel, new DialogInterface.OnClickListener() {
//                public void onClick(DialogInterface dialog, int id) {
//                    dismiss();
//                }
//            });

            tb = (Switch) view.findViewById(R.id.whatAll);


//            rbAll=(CheckBox) view.findViewById(R.id.whatAll);
            rbArc=(CheckBox) view.findViewById(R.id.whatArc);
            rbBuildings=(CheckBox) view.findViewById(R.id.whatBuildings);
            rbChurch=(CheckBox) view.findViewById(R.id.whatChurch);
            rbMuseum=(CheckBox) view.findViewById(R.id.whatMuseum);
            rbSquare=(CheckBox) view.findViewById(R.id.whatSquare);
            rbStatue=(CheckBox) view.findViewById(R.id.whatStatue);
            rbVilla=(CheckBox) view.findViewById(R.id.whatVilla);

            //aggiunzioni del 24 Giugno

            rbTheater=(CheckBox) view.findViewById(R.id.whatTheater);
            rbMonuments=(CheckBox) view.findViewById(R.id.whatMonuments);
            rbBridges=(CheckBox) view.findViewById(R.id.whatBridges);
            rbCemetery=(CheckBox) view.findViewById(R.id.whatCemetery);
            rbMarket=(CheckBox) view.findViewById(R.id.whatMarket);
            rbOthers=(CheckBox) view.findViewById(R.id.whatOthers);
            rbStructures=(CheckBox) view.findViewById(R.id.whatStructures);


            Type type2=new TypeToken<ArrayList<String>>() {}.getType();
            Gson gson2=new Gson();
            whatToSeeItems=gson2.fromJson(Repository.retrieve(getActivity(),Constants.WHAT_SAVE,String.class),type2);


//            if (whatToSeeItems!=null) {
//                for (int i = 0; i < whatToSeeItems.size(); i++) {
////                    Log.d("miotag", "whatToSeeItems: " + whatToSeeItems.get(i) + "\n");
//                }
//            }


            if (whatToSeeItems==null) {
                setAllTrue();
                whatToSeeItems=new ArrayList<String>();
            } else {
                setAllFalse();
                for (String s : whatToSeeItems){
//                    Log.d("miotag","stringa in valutazione: "+s);
                    switch(s){
                        case "all":
                            setAllTrue();
                            break;
                        case "Chiese e luoghi di culto":
//                            Log.d("miotag"," CHECCKARE Chiese e luoghi di culto");
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
                        case "Piazze":
                            rbSquare.setChecked(true);
                            break;

                        case "Porte":
                            rbArc.setChecked(true);
                            break;
                        case "Teatri":
                            rbTheater.setChecked(true);
                            break;
                        case "Monumenti e Gallerie d'Arte":
                            rbMonuments.setChecked(true);
                            break;
                        case "Ponti":
                            rbBridges.setChecked(true);
                            break;
                        case "Cimiteri":
                            rbCemetery.setChecked(true);
                            break;
                        case "Fiere e Mercati":
                            rbMarket.setChecked(true);
                            break;
                        case "Altri luoghi di interesse":
                            rbOthers.setChecked(true);
                            break;
                        case "Edifici":
                            rbStructures.setChecked(true);
                            break;
                        case "Statue":
                            rbStatue.setChecked(true);
                            break;
                        default:
                            setAllFalse();
                    }
                }



            }

//            rbAll.setOnClickListener(new View.OnClickListener() {
//                @Override
//                public void onClick(View v) {
//
//                    if (!(rbAll.isChecked())) {
//                        //    Log.d("miotag","NON ERA checked");
//                        rbAll.setChecked(false);
//                        setAllFalse();
//
//
//                    } else {
//                        //    Log.d("miotag", " ERA checked");
//                        rbAll.setChecked(true);
//                        setAllTrue();
//                    }
//                }
//            });


//            tb.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
//                public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
//                    if (!isChecked) {
//                        tb.setChecked(false);
//                    } else {
//                        tb.setChecked(true);
//                    }
//                }
//            });

            tb.setOnClickListener(new View.OnClickListener(){
                @Override
                public void onClick(View v){
                    if(!(tb.isChecked())){
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
                    if (!(rbArc.isChecked())){
                        rbArc.setChecked(false);
                    }else {
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
                    if (!(rbChurch.isChecked())){
                        rbChurch.setChecked(false);
                    }else {
                        rbChurch.setChecked(true);
                    }
                }
            });
            rbMuseum.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (!(rbMuseum.isChecked())){
                        rbMuseum.setChecked(false);
                    }else {
                        rbMuseum.setChecked(true);
                    }
                }
            });
            rbSquare.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (!(rbSquare.isChecked())){
                        rbSquare.setChecked(false);
                    }else {
                        rbSquare.setChecked(true);
                    }
                }
            });
            rbStatue.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (!(rbStatue.isChecked())){
                        rbStatue.setChecked(false);
                    }else {
                        rbStatue.setChecked(true);
                    }
                }
            });
            rbVilla.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (!(rbVilla.isChecked())){
                        rbVilla.setChecked(false);
                    }else {
                        rbVilla.setChecked(true);
                    }
                }
            });

            rbTheater.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (!(rbTheater.isChecked())){
                        rbTheater.setChecked(false);
                    }else {
                        rbTheater.setChecked(true);
                    }
                }
            });

            rbMonuments.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (!(rbMonuments.isChecked())){
                        rbMonuments.setChecked(false);
                    }else {
                        rbMonuments.setChecked(true);
                    }
                }
            });

            rbBridges.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (!(rbBridges.isChecked())){
                        rbBridges.setChecked(false);
                    }else {
                        rbBridges.setChecked(true);
                    }
                }
            });

            rbCemetery.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (!(rbCemetery.isChecked())){
                        rbCemetery.setChecked(false);
                    }else {
                        rbCemetery.setChecked(true);
                    }
                }
            });

            rbMarket.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (!(rbMarket.isChecked())){
                        rbMarket.setChecked(false);
                    }else {
                        rbMarket.setChecked(true);
                    }
                }
            });

            rbOthers.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (!(rbOthers.isChecked())){
                        rbOthers.setChecked(false);
                    }else {
                        rbOthers.setChecked(true);
                    }
                }
            });

            rbStructures.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (!(rbStructures.isChecked())){
                        rbStructures.setChecked(false);
                    }else {
                        rbStructures.setChecked(true);
                    }
                }
            });


            return builder.create();
        }  //fine classe






        private void setAllTrue(){
         //   Log.d("miotag","setAlltrue");
//            rbAll.setChecked(true);
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
            rbMonuments.setChecked(true);
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
            rbMonuments.setChecked(false);
            rbBridges.setChecked(false);
            rbCemetery.setChecked(false);
            rbMarket.setChecked(false);
            rbOthers.setChecked(false);
            rbStructures.setChecked(false);

//            rbAll.setChecked(false);
            tb.setChecked(false);


        }

        public void storeCheckedBox(){
            boolean isEnglish=false;
            if (TextUtils.equals(Locale.getDefault().getDisplayLanguage(), "English")){
                isEnglish=true;
            } else {
                isEnglish=false;
            }

            whatToSeeItems.clear();

//            if(rbAll.isChecked()){
//                whatToSeeItems.add("all");
//                atLeastOneCheck=true;
//            }

            if (tb.isChecked()){

                whatToSeeItems.add("all");
                atLeastOneCheck=true;
            }

            if(rbChurch.isChecked()){

                    whatToSeeItems.add("Chiese e luoghi di culto");

                atLeastOneCheck=true;
            }

            if(rbMuseum.isChecked()){
                whatToSeeItems.add("Musei e Gallerie d'Arte");
                atLeastOneCheck=true;
            }

            if(rbVilla.isChecked()){
                whatToSeeItems.add("Ville, Giardini e Parchi");
                atLeastOneCheck=true;
            }

            if(rbBuildings.isChecked()){
                whatToSeeItems.add("Palazzi e Castelli");
                atLeastOneCheck=true;
            }

            if(rbSquare.isChecked()){
                whatToSeeItems.add("Piazze");
                atLeastOneCheck=true;
            }

            if(rbStatue.isChecked()){
                whatToSeeItems.add("Statue");
                atLeastOneCheck=true;
            }

            if(rbArc.isChecked()){
                whatToSeeItems.add("Porte");
                atLeastOneCheck=true;
            }

            //DAL 24 GIUGNO

            if(rbTheater.isChecked()){
                whatToSeeItems.add("Teatri");
                atLeastOneCheck=true;
            }

            if(rbMonuments.isChecked()){
                whatToSeeItems.add("Monumenti e Gallerie d'Arte");
                atLeastOneCheck=true;
            }

            if(rbBridges.isChecked()){
                whatToSeeItems.add("Ponti");
                atLeastOneCheck=true;
            }

            if(rbCemetery.isChecked()){
                whatToSeeItems.add("Cimiteri");
                atLeastOneCheck=true;
            }

            if(rbMarket.isChecked()){
                whatToSeeItems.add("Fiere e Mercati");
                atLeastOneCheck=true;
            }

            if(rbOthers.isChecked()){
                whatToSeeItems.add("Altri luoghi di interesse");
                atLeastOneCheck=true;
            }

            if(rbStructures.isChecked()){
                whatToSeeItems.add("Edifici");
                atLeastOneCheck=true;
            }

            Gson gson = new Gson();
           // type = new TypeToken<Calendar>() {}.getType();



            String json = gson.toJson(whatToSeeItems);
            Repository.save(getActivity(), Constants.WHAT_SAVE, json);
            setCorrectWhatSelection();

            Log.d("miotag","i typeSite salvati sono:");
            for (String s: whatToSeeItems){
                Log.d("miotag",s+"\n");
            }

          //  Log.d("miotag","Store json"+PreferencesHelper.get(getActivity(),String.class,Constants.WHAT_SAVE));


        }

        private void setCorrectWhatSelection(){
           //check for at least 1 valid selection

            if (whatToSeeItems.size()==0 ){


                return;

            }

           if (TextUtils.equals(whatToSeeItems.get(0), "all")){
               Log.d("miotag","solo all");
               SettingTourActivity.txtWhatToSee.setText(R.string.all_sites);
           } else if(whatToSeeItems.size()==1){
                Log.d("miotag","whatTosee ha un solo elemento"+ whatToSeeItems.get(0));
               SettingTourActivity.txtWhatToSee.setText(translateNamesSites(whatToSeeItems.get(0)));
           } else {
               Log.d("miotag","whatTosee ha più elementi"+ whatToSeeItems.size());
               SettingTourActivity.txtWhatToSee.setText(translateNamesSites(whatToSeeItems.get(0))+ ", "+getActivity().getResources().getString(R.string.and_more));
           }

        }

        private String translateNamesSites(String s){

            switch(s){
                case "all":
                   return "all sites";
                case "Chiese e luoghi di culto":
                           return "Churches";

                case "Musei e Gallerie d'Arte":
                    return "Museums";
                case "Ville, Giardini e Parchi":
                    return "Villas and Gardens";
                case "Palazzi e Castelli":
                    return "Palaces and Castels";
                case "Piazze":
                    return "Squares and Streets";

                case "Porte":
                    return "Arches, Ports and Walls";
                case "Teatri":
                    return "Theaters";
                case "Monumenti e Gallerie d'Arte":
                    return "Monuments";
                case "Ponti":
                    return "Bridges";
                case "Cimiteri":
                    return "Cemeteries";
                case "Fiere e Mercati":
                    return "Fairs and Markets";
                case "Altri luoghi di interesse":
                    return "Other Monuments";
                case "Edifici":
                    return "Buildings";
                case "Statue":
                    return "Statues and Fountains";
                default:
                    return "all";
            }


        }



    }//fine classe
}
