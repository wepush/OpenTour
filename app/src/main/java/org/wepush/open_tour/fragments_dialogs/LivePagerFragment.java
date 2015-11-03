package org.wepush.open_tour.fragments_dialogs;


import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.os.Environment;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import org.wepush.open_tour.R;
import org.wepush.open_tour.structures.DB1SqlHelper;
import org.wepush.open_tour.structures.Site;
import org.wepush.open_tour.utils.Constants;
import org.wepush.open_tour.utils.Repository;

import java.util.Locale;


/**
 * Created by antoniocoppola on 07/07/15.
 */
public class LivePagerFragment extends Fragment {
    private String placeholderName;
    private Bitmap bitmap;
    private boolean placeholderEverywhere=false;


    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container,@Nullable Bundle savedInstanceState){

       Log.d("miotag","nuovo fragment");
        View view=inflater.inflate(R.layout.fragment_livepager,null);


        final TextView bodyView=(TextView) view.findViewById(R.id.liveAddressSite);
        final TextView titleView = (TextView) view.findViewById(R.id.liveTitleSite);
        final ImageView placeHolder=(ImageView) view.findViewById(R.id.pinMapHolder);
        final Bundle args=getArguments();

        String packagesChosen=Repository.retrieve(getContext(), Constants.WHAT_I_WANT_TO_DOWNLOAD, String.class);
        if (packagesChosen.equals(Constants.DOWNLOADING_MAPS_ONLY)){
            placeholderEverywhere=true;
        }

        titleView.setText(args.getString("title"));
        bodyView.setText(args.getString("description"));


        String idOfTheImageToRetrieve=args.getString("id");

        Site site=DB1SqlHelper.getInstance(getActivity()).getSite(idOfTheImageToRetrieve);
        String imagePath=site.pictureUrl;

        String currentCity= Repository.retrieve(getActivity(), Constants.KEY_CURRENT_CITY, String.class);
        Log.d("miotag","currentCity: "+currentCity);



        if (currentCity.equals(Constants.CITY_MILAN)) {

            if ((TextUtils.equals(imagePath, "placeholder")) || (placeholderEverywhere)) {

                if (TextUtils.equals(Locale.getDefault().getDisplayLanguage(), "English")) {
                    convertLanguageTypeOfSite(site);
                } else {
                    chooseThemeColors(site);
                }
                imagePath=placeholderName;
                int drawableResource=getActivity().getResources().getIdentifier(imagePath, "drawable", getActivity().getPackageName());
                placeHolder.setImageResource(drawableResource);
//                bitmap = BitmapFactory.decodeFile(imagePath);
//                Drawable mDrawable = new BitmapDrawable(getActivity().getResources(), bitmap);
//                placeHolder.setImageDrawable(mDrawable);
//                holder.vPlaceHolder.setImageDrawable(mDrawable);
//                mImageDetail.setImageDrawable(mDrawable);
//                mImageDetail.setScaleType(ImageView.ScaleType.FIT_XY);

            } else {
                imagePath = imagePath.substring(79, imagePath.length() - 4);
                imagePath = "milano_images/" + imagePath + ".jpg";
                bitmap = BitmapFactory.decodeFile(getActivity().getExternalFilesDir(Environment.DIRECTORY_PICTURES) + "/" + imagePath);
                Drawable mDrawable = new BitmapDrawable(getActivity().getResources(), bitmap);
                placeHolder.setImageDrawable(mDrawable);
//                mImageDetail.setImageDrawable(mDrawable);
//                mImageDetail.setScaleType(ImageView.ScaleType.FIT_XY);
            }
        } else {
            if ((TextUtils.equals(imagePath, "placeholder")) || (placeholderEverywhere)) {

                if (TextUtils.equals(Locale.getDefault().getDisplayLanguage(), "English")) {
                    convertLanguageTypeOfSite(site);
                } else {
                    chooseThemeColors(site);
                }

                imagePath=placeholderName;
                Log.d("miotag","in TimeLine: imagepath: "+imagePath);
//                bitmap = BitmapFactory.decodeFile(imagePath);
                int drawableResource=getActivity().getResources().getIdentifier(imagePath, "drawable", getActivity().getPackageName());
                placeHolder.setImageResource(drawableResource);
//                mImageDetail.setImageResource(drawableResource);
//                mImageDetail.setScaleType(ImageView.ScaleType.FIT_CENTER);
            } else {
                imagePath =imagePath.substring(79, imagePath.length() - 4);
                imagePath="palermo_images/"+imagePath+".jpg";
                bitmap = BitmapFactory.decodeFile(getActivity().getExternalFilesDir(Environment.DIRECTORY_PICTURES) + "/" + imagePath);
                Drawable mDrawable = new BitmapDrawable(getActivity().getResources(), bitmap);
                placeHolder.setImageDrawable(mDrawable);

            }

        }
        //TODO 1 ottobre 2015
//        String imagePath= DB1SqlHelper.getInstance(getActivity()).getPictureSite(args.getString("id"));
//        String imagePathToUse="";
//
//        if(TextUtils.equals(imagePath, "placeholder")){
//            imagePathToUse="header_milan";
//        } else {
//            imagePathToUse = imagePath.substring(79, imagePath.length()-4);
//        }
//
//        imagePathToUse="milano_images/"+imagePathToUse+".jpg";
//        Bitmap bitmap = BitmapFactory.decodeFile(getActivity().getExternalFilesDir(Environment.DIRECTORY_PICTURES) + "/" + imagePathToUse);
//        Drawable mDrawable = new BitmapDrawable(getActivity().getResources(), bitmap);
//        try {
//            placeHolder.setImageDrawable(mDrawable);
//        } catch (Resources.NotFoundException e){
//            Log.d("miotag","eccezione!!!!! immagine mancante: "+imagePathToUse);
//            imagePathToUse="header_milan.jpg";
//            Bitmap bitmapToUse = BitmapFactory.decodeFile(getActivity().getExternalFilesDir(Environment.DIRECTORY_PICTURES) + "/" + imagePathToUse);
//            Drawable mDrawableToUse = new BitmapDrawable(getActivity().getResources(), bitmapToUse);
//            placeHolder.setImageDrawable(mDrawableToUse);
//            placeHolder.setScaleType(ImageView.ScaleType.FIT_XY);
//        }
        //TODO fine OTTOBRE 2015

        return view;
    } //fine onCreateView



    private void chooseThemeColors(Site site) {
        Log.d("miotag","TimeLine ChooseTheme: "+site.typeOfSite);

//TODO check if there's a better solution to manage sitesType in languages
        switch (site.typeOfSite) {
            case "Teatri":

                placeholderName=getActivity().getResources().getString(R.string.placeholderTheaters);
                break;

            case "Palazzi e Castelli":

                placeholderName=getActivity().getResources().getString(R.string.placeholderCastles);

                break;

            case "Ville, Giardini e Parchi":

                placeholderName=getActivity().getResources().getString(R.string.placeholderVillas);

                break;

            case "Musei e Gallerie d'arte":

                placeholderName=getActivity().getResources().getString(R.string.placeholderMuseums);

                break;

            case "Statue e Fontane":


                placeholderName=getActivity().getResources().getString(R.string.placeholderStatues);

                break;

            case "Piazze e Strade":

                placeholderName=getActivity().getString(R.string.placeholderSquares);


                break;

            case "Archi, Porte e Mura":

                placeholderName=getActivity().getResources().getString(R.string.placeholderArcs);

                break;

            case "Fiere e Mercati":

                placeholderName=getActivity().getResources().getString(R.string.placeholderArcs);

                break;

            case "Cimiteri e Memoriali":

                placeholderName=getActivity().getResources().getString(R.string.placeholderCemeteries);

                break;


            case "Edifici":

                placeholderName=getActivity().getResources().getString(R.string.placeholderBuildings);

                break;

            case "Ponti":

                placeholderName=getActivity().getResources().getString(R.string.placeholderBridges);

                break;

            case "Chiese, Oratori e Luoghi di culto":

                placeholderName=getActivity().getResources().getString(R.string.placeholderChurches);

                break;

            case "Altri monumenti e Luoghi di interesse":

                placeholderName=getActivity().getResources().getString(R.string.placeholderOtherSites);
                break;

        }

    }


    private void convertLanguageTypeOfSite(Site site) {


        switch (site.typeOfSite) {
            case "Theaters":

                placeholderName=getActivity().getResources().getString(R.string.placeholderTheaters);

                break;

            case "Palaces and Castles":

                placeholderName=getActivity().getResources().getString(R.string.placeholderCastles);

                break;

            case "Villas, Gardens and Parks":

                placeholderName=getActivity().getResources().getString(R.string.placeholderVillas);

                break;

            case "Museums and Art galleries":

                placeholderName=getActivity().getResources().getString(R.string.placeholderMuseums);

                break;

            case "Statues and Fountains":

                placeholderName=getActivity().getResources().getString(R.string.placeholderStatues);

                break;

            case "Squares and Streets":

                placeholderName=getActivity().getResources().getString(R.string.placeholderSquares);


                break;

            case "Arches, Gates and Walls":

                placeholderName=getActivity().getResources().getString(R.string.placeholderArcs);

                break;

            case "Fairs and Markets":

                placeholderName=getActivity().getResources().getString(R.string.placeholderArcs);

                break;

            case "Cemeteries and Memorials":

                placeholderName=getActivity().getResources().getString(R.string.placeholderCemeteries);
                break;

            case "Buildings":

                placeholderName=getActivity().getResources().getString(R.string.placeholderBuildings);

                break;

            case "Bridges":

                placeholderName=getActivity().getResources().getString(R.string.placeholderBridges);

                break;

            case "Churches, Oratories and Places of worship":

                placeholderName=getActivity().getResources().getString(R.string.placeholderChurches);

                break;

            case "Other monuments and Places of interest":

                placeholderName=getActivity().getResources().getString(R.string.placeholderOtherSites);

                break;

        }


    }







}//fine classe
