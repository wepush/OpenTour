package org.wepush.open_tour.utils;

import android.content.Context;
import android.os.Environment;
import android.widget.ImageView;

import com.bumptech.glide.Glide;

import org.wepush.open_tour.R;

/**
 * Created by antoniocoppola on 30/11/15.
 */
public class OpenTourUtils {



    private static String picturesDirectory;
    private static String category;
    private static Context context;
    private static boolean placeholderEverywhere=false;

    public OpenTourUtils(Context ctx, String cityConstructor){
        context=ctx;

        String packagesChosen=Repository.retrieve(context, Constants.WHAT_I_WANT_TO_DOWNLOAD,String.class);
        if (packagesChosen.equals(Constants.DOWNLOADING_MAPS_ONLY)){
            placeholderEverywhere=true;
        }


        switch (cityConstructor){
            case Constants.CITY_MILAN:
                picturesDirectory="milano_images/";
                break;

            case Constants.CITY_PALERMO:
                picturesDirectory="palermo_images/";
                break;

            case Constants.CITY_ALCAMO:
                picturesDirectory="alcamo_images/";
                break;

            case Constants.CITY_MODENA:
                picturesDirectory="modena_images/";
                break;

            case Constants.CITY_TURIN:
                picturesDirectory="turin_images/";
        }



    }


    public static void setImage(String pictureUrlConstructor, String languageConstructor, ImageView imageToSet, String categoryConstructor, boolean setScale){

        switch (languageConstructor) {

            case Constants.ITA_LANGUAGE:
                category = chooseItalianCategory(categoryConstructor);
                break;

            case Constants.EN_LANGUAGE:
                category = chooseEngCategory(categoryConstructor);
                break;
        }

        if (pictureUrlConstructor.equals("placeholder") ||  (placeholderEverywhere)) {

            int drawableResource = context.getResources().getIdentifier(category, "drawable", context.getPackageName());

            Glide.with(context)
                    .load(drawableResource)
                    .into(imageToSet);

        } else {

                    pictureUrlConstructor = pictureUrlConstructor.substring(79, pictureUrlConstructor.length() - 4);
                    pictureUrlConstructor = picturesDirectory + pictureUrlConstructor + ".jpg";

            if (setScale) {

                Glide.with(context)
                        .load(context.getExternalFilesDir(Environment.DIRECTORY_PICTURES) + "/" + pictureUrlConstructor)
                        .placeholder(R.drawable.placeholder_glide)
                        .into(imageToSet);
                imageToSet.setScaleType(ImageView.ScaleType.FIT_XY);
            } else {
                Glide.with(context)
                        .load(context.getExternalFilesDir(Environment.DIRECTORY_PICTURES) + "/" + pictureUrlConstructor)
                        .placeholder(R.drawable.placeholder_glide)
                        .into(imageToSet);
            }

             }

    }

    private static String chooseItalianCategory(String cat){

        String placeholderName="";

        switch (cat) {
            case "Teatri":
                placeholderName=context.getResources().getString(R.string.placeholderTheaters);
                break;

            case "Palazzi e Castelli":
                placeholderName=context.getResources().getString(R.string.placeholderCastles);

                break;

            case "Ville, Giardini e Parchi":
                placeholderName=context.getResources().getString(R.string.placeholderVillas);

                break;

            case "Musei e Gallerie d'arte":
                placeholderName=context.getResources().getString(R.string.placeholderMuseums);

                break;

            case "Statue e Fontane":
                placeholderName=context.getResources().getString(R.string.placeholderStatues);

                break;

            case "Piazze e Strade":
                placeholderName=context.getString(R.string.placeholderSquares);

                break;

            case "Archi, Porte e Mura":
                placeholderName=context.getResources().getString(R.string.placeholderArcs);

                break;

            case "Fiere e Mercati":
                placeholderName=context.getResources().getString(R.string.placeholderArcs);

                break;

            case "Cimiteri e Memoriali":
                placeholderName=context.getResources().getString(R.string.placeholderCemeteries);

                break;


            case "Edifici":
                placeholderName=context.getResources().getString(R.string.placeholderBuildings);

                break;

            case "Ponti":
                placeholderName=context.getResources().getString(R.string.placeholderBridges);

                break;

            case "Chiese, Oratori e Luoghi di culto":
                placeholderName=context.getResources().getString(R.string.placeholderChurches);

                break;

            case "Altri monumenti e Luoghi di interesse":
                placeholderName=context.getResources().getString(R.string.placeholderOtherSites);
                break;

        }

        return placeholderName;

    }



    private static String chooseEngCategory(String cat) {

        String placeholderName="";

        switch (cat) {
            case "Theaters":

                placeholderName=context.getResources().getString(R.string.placeholderTheaters);

                break;

            case "Palaces and Castles":

                placeholderName=context.getResources().getString(R.string.placeholderCastles);

                break;

            case "Villas, Gardens and Parks":

                placeholderName=context.getResources().getString(R.string.placeholderVillas);

                break;

            case "Museums and Art galleries":

                placeholderName=context.getResources().getString(R.string.placeholderMuseums);

                break;

            case "Statues and Fountains":

                placeholderName=context.getResources().getString(R.string.placeholderStatues);

                break;

            case "Squares and Streets":

                placeholderName=context.getResources().getString(R.string.placeholderSquares);


                break;

            case "Arches, Gates and Walls":

                placeholderName=context.getResources().getString(R.string.placeholderArcs);

                break;

            case "Fairs and Markets":

                placeholderName=context.getResources().getString(R.string.placeholderArcs);

                break;

            case "Cemeteries and Memorials":

                placeholderName=context.getResources().getString(R.string.placeholderCemeteries);
                break;

            case "Buildings":

                placeholderName=context.getResources().getString(R.string.placeholderBuildings);

                break;

            case "Bridges":

                placeholderName=context.getResources().getString(R.string.placeholderBridges);

                break;

            case "Churches, Oratories and Places of worship":

                placeholderName=context.getResources().getString(R.string.placeholderChurches);

                break;

            case "Other monuments and Places of interest":

                placeholderName=context.getResources().getString(R.string.placeholderOtherSites);

                break;

        }

        return placeholderName;


    }




}
