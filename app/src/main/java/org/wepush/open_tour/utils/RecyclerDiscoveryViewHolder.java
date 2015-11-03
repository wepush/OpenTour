package org.wepush.open_tour.utils;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.Environment;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import org.wepush.open_tour.R;
import org.wepush.open_tour.structures.Site;

import java.util.Locale;

import de.hdodenhof.circleimageview.CircleImageView;

/**
 * Created by antoniocoppola on 26/10/15.
 */
public class RecyclerDiscoveryViewHolder extends RecyclerView.ViewHolder {
    private static Context context;
    private CircleImageView circleImageView;
    private TextView title,address;
    private String placeholderName;
    private Bitmap bitmap;
    private boolean placeholderEverywhere=false;

    public static RecyclerDiscoveryViewHolder newInstance (View view, Context ctx){
        CircleImageView imageItem=(CircleImageView) view.findViewById(R.id.circleImageRecyclerDiscovery);
        TextView txtTime=(TextView)view.findViewById(R.id.txtTitleRecyclerDiscovery);
        TextView txtDistance=(TextView)view.findViewById(R.id.txtBodyRecyclerDiscovery);
        context=ctx;
        return new RecyclerDiscoveryViewHolder(view,imageItem,txtTime,txtDistance);


    }

    public RecyclerDiscoveryViewHolder(final View parent,CircleImageView image,TextView ttl,TextView addr) {
        super(parent);

        Log.d("miotag","costruttore RecyclerDiscoveryViewHolder");
        String packagesChosen=Repository.retrieve(context, Constants.WHAT_I_WANT_TO_DOWNLOAD,String.class);
        if (packagesChosen.equals(Constants.DOWNLOADING_MAPS_ONLY)){
            placeholderEverywhere=true;
        }
        circleImageView=image;
        title=ttl;
        address=addr;

    }

    public void showHolderElement(String imageToShow,String txtTitle,String txtAddress,String category){

        String currentCity=Repository.retrieve(context,Constants.KEY_CURRENT_CITY,String.class);

        if (currentCity.equals(Constants.CITY_MILAN)) {

            if ((TextUtils.equals(imageToShow, "placeholder")) || (placeholderEverywhere)) {

                if (TextUtils.equals(Locale.getDefault().getDisplayLanguage(), "English")) {
                    convertLanguageTypeOfSite(category);
                } else {
                    chooseThemeColors(category);
                }
                imageToShow = placeholderName;
                int drawableResource = context.getResources().getIdentifier(imageToShow, "drawable", context.getPackageName());
                circleImageView.setImageResource(drawableResource);
//                circleImageView.setScaleType(ImageView.ScaleType.FIT_XY);


            } else {
                imageToShow = imageToShow.substring(79, imageToShow.length() - 4);
                imageToShow = "milano_images/" + imageToShow + ".jpg";
                bitmap = BitmapFactory.decodeFile(context.getExternalFilesDir(Environment.DIRECTORY_PICTURES) + "/" + imageToShow);
                Drawable mDrawable = new BitmapDrawable(context.getResources(), bitmap);
                circleImageView.setImageDrawable(mDrawable);
//                circleImageView.setScaleType(ImageView.ScaleType.FIT_XY);

            }
        } else {
            if ((TextUtils.equals(imageToShow, "placeholder")) || (placeholderEverywhere)) {

                if (TextUtils.equals(Locale.getDefault().getDisplayLanguage(), "English")) {
                    convertLanguageTypeOfSite(category);
                } else {
                    chooseThemeColors(category);
                }

                imageToShow = placeholderName;
                int drawableResource = context.getResources().getIdentifier(imageToShow, "drawable", context.getPackageName());
                circleImageView.setImageResource(drawableResource);
//                circleImageView.setScaleType(ImageView.ScaleType.FIT_XY);


            } else {

                if (TextUtils.equals(Locale.getDefault().getDisplayLanguage(), "English")) {
                    convertLanguageTypeOfSite(category);
                } else {
                    chooseThemeColors(category);
                }

                imageToShow = imageToShow.substring(79, imageToShow.length() - 4);
                imageToShow = "palermo_images/" + imageToShow + ".jpg";
                bitmap = BitmapFactory.decodeFile(context.getExternalFilesDir(Environment.DIRECTORY_PICTURES) + "/" + imageToShow);
                Drawable mDrawable = new BitmapDrawable(context.getResources(), bitmap);
                circleImageView.setImageDrawable(mDrawable);
//                circleImageView.setScaleType(ImageView.ScaleType.FIT_XY);
            }


        }


        title.setText(txtTitle);
        address.setText(txtAddress);


    }

    private void chooseThemeColors(String category) {

//TODO check if there's a better solution to manage sitesType in languages
        switch (category) {


            case "Palazzi e Castelli":

                placeholderName=context.getResources().getString(R.string.placeholderCastles);

                break;

            case "Ville, Giardini e Parchi":

                placeholderName=context.getResources().getString(R.string.placeholderVillas);

                break;

            case "Musei e Gallerie d'arte":

                placeholderName=context.getResources().getString(R.string.placeholderMuseums);

                break;


            case "Chiese, Oratori e Luoghi di culto":

                placeholderName=context.getResources().getString(R.string.placeholderChurches);

                break;


        }

    }

    private void convertLanguageTypeOfSite(String category) {


        switch (category) {


            case "Palaces and Castles":

                placeholderName=context.getResources().getString(R.string.placeholderCastles);

                break;

            case "Villas, Gardens and Parks":

                placeholderName=context.getResources().getString(R.string.placeholderVillas);

                break;

            case "Museums and Art galleries":

                placeholderName=context.getResources().getString(R.string.placeholderMuseums);

                break;

            case "Churches, Oratories and Places of worship":

                placeholderName=context.getResources().getString(R.string.placeholderChurches);

                break;


        }


    }




}
