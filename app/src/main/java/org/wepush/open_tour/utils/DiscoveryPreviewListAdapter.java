//package org.wepush.open_tour.utils;
//
//import android.content.Context;
//import android.content.Intent;
//import android.graphics.Bitmap;
//import android.graphics.BitmapFactory;
//import android.graphics.drawable.BitmapDrawable;
//import android.graphics.drawable.Drawable;
//import android.os.Environment;
//import android.text.TextUtils;
//import android.util.Log;
//import android.view.LayoutInflater;
//import android.view.View;
//import android.view.ViewGroup;
//import android.widget.BaseAdapter;
//import android.widget.ImageView;
//import android.widget.TextView;
//
//import org.wepush.open_tour.DetailsActivity;
//import org.wepush.open_tour.R;
//import org.wepush.open_tour.structures.Site;
//
//import java.util.ArrayList;
//import java.util.Locale;
//
///**
// * Created by antoniocoppola on 02/10/15.
// */
//public class DiscoveryPreviewListAdapter extends BaseAdapter implements View.OnClickListener{
//    private Context context;
//    private ArrayList<Site> sitesInList;
//    private LayoutInflater layoutInflater;
//    private String currentCity,placeholderName;
//    private Bitmap bitmap;
//    private Intent i;
//    private Site site;
//
//    public DiscoveryPreviewListAdapter(Context ctx,ArrayList<Site> s) {
//        context = ctx;
//        sitesInList=s;
//        layoutInflater = LayoutInflater.from(context);
//        i=new Intent(context, DetailsActivity.class);
//    }
//
//    @Override
//    public Object getItem(int position) {
//        return sitesInList.get(position);
//    }
//
//    @Override
//    public long getItemId(int position) {
//        return position;
//    }
//
//    @Override
//    public int getCount() {
//        return sitesInList.size();
//    }
//
//    public View getView(int position, View convertView, ViewGroup parent) {
//
//        ViewHolderDiscovery holder;
//        if (convertView == null) {
//            convertView = layoutInflater.inflate(R.layout.listview_discoverypreview, null);
//            holder = new ViewHolderDiscovery();
////            holder.vTitle = (TextView) convertView.findViewById(R.id.txtTitle);
////            holder.vDescription = (TextView) convertView.findViewById(R.id.txtAddress);
////            holder.vTime = (TextView) convertView.findViewById(R.id.txtTime);
////            holder.vPlaceHolder= (ImageView) convertView.findViewById(R.id.imgPlaceHolder);
//            holder.vTitle=(TextView) convertView.findViewById(R.id.txtTitleDiscoveryPreviewList);
//            holder.vDescription=(TextView) convertView.findViewById(R.id.txtBodyDiscoveryPreviewList);
//            holder.placeholderName=(ImageView) convertView.findViewById(R.id.circleImageDiscoveryPreviewList);
//            holder.id=sitesInList.get(position).id;
//
//
//            convertView.setTag(holder);
//        } else {
//            holder = (ViewHolderDiscovery) convertView.getTag();
//        }
//
//
//        holder.vTitle.setText(sitesInList.get(position).name);
//        holder.vDescription.setText(sitesInList.get(position).address+","+sitesInList.get(position).addressCivic);
////        holder.vTime.setText(String.valueOf(sitesInList.get(position).showingTime));
//
//
//
//
//        site=sitesInList.get(position);
//        String imagePath=site.pictureUrl;
//        Log.d("miotag", "immagine sito da mostrare: " + site.name+", imagePath: "+imagePath);
//
//
//        currentCity=Repository.retrieve(context,Constants.KEY_CURRENT_CITY,String.class);
//
//        if (currentCity.equals(Constants.CITY_MILAN)) {
//
//            if (TextUtils.equals(imagePath, "placeholder")) {
//
//                if (TextUtils.equals(Locale.getDefault().getDisplayLanguage(), "English")) {
//                    convertLanguageTypeOfSite(site);
//                } else {
//                    chooseThemeColors(site);
//                }
//                imagePath=placeholderName;
//                int drawableResource=context.getResources().getIdentifier(imagePath, "drawable", context.getPackageName());
//                holder.placeholderName.setImageResource(drawableResource);
//
//
//            } else {
//                imagePath = imagePath.substring(79, imagePath.length() - 4);
//                imagePath = "milano_images/" + imagePath + ".jpg";
//                Log.d("miotag","immagine da mostrare: "+imagePath);
//                bitmap = BitmapFactory.decodeFile(context.getExternalFilesDir(Environment.DIRECTORY_PICTURES) + "/" + imagePath);
//                Drawable mDrawable = new BitmapDrawable(context.getResources(), bitmap);
//                holder.placeholderName.setImageDrawable(mDrawable);
////                mImageDetail.setImageDrawable(mDrawable);
////                mImageDetail.setScaleType(ImageView.ScaleType.FIT_XY);
//            }
//        } else {
//            if (TextUtils.equals(imagePath, "placeholder")) {
//
//                if (TextUtils.equals(Locale.getDefault().getDisplayLanguage(), "English")) {
//                    convertLanguageTypeOfSite(site);
//                } else {
//                    chooseThemeColors(site);
//                }
//
//                imagePath = placeholderName;
//                Log.d("miotag", "Timeline placehoder " + imagePath + " per il sito " + site.name);
//                int drawableResource = context.getResources().getIdentifier(imagePath, "drawable", context.getPackageName());
//                holder.placeholderName.setImageResource(drawableResource);
//
//            } else {
//                imagePath = imagePath.substring(79, imagePath.length() - 4);
//                imagePath = "palermo_images/" + imagePath + ".jpg";
//                Log.d("miotag", "immagine da mostrare: " + imagePath);
//                bitmap = BitmapFactory.decodeFile(context.getExternalFilesDir(Environment.DIRECTORY_PICTURES) + "/" + imagePath);
//                Drawable mDrawable = new BitmapDrawable(context.getResources(), bitmap);
//                holder.placeholderName.setImageDrawable(mDrawable);
//
//            }
//        }
//
//
//
//            return convertView;
//
//    }
//
//
//
//    private void chooseThemeColors(Site site) {
//        Log.d("miotag","TimeLine ChooseTheme: "+site.typeOfSite);
//
////TODO check if there's a better solution to manage sitesType in languages
//        switch (site.typeOfSite) {
//            case "Teatri":
//
//                placeholderName=context.getResources().getString(R.string.placeholderTheaters);
//                break;
//
//            case "Palazzi e Castelli":
//
//                placeholderName=context.getResources().getString(R.string.placeholderCastles);
//
//                break;
//
//            case "Ville, Giardini e Parchi":
//
//                placeholderName=context.getResources().getString(R.string.placeholderVillas);
//
//                break;
//
//            case "Musei e Gallerie d'arte":
//
//                placeholderName=context.getResources().getString(R.string.placeholderMuseums);
//
//                break;
//
//            case "Statue e Fontane":
//
//
//                placeholderName=context.getResources().getString(R.string.placeholderStatues);
//
//                break;
//
//            case "Piazze e Strade":
//
//                placeholderName=context.getString(R.string.placeholderSquares);
//
//
//                break;
//
//            case "Archi, Porte e Mura":
//
//                placeholderName=context.getResources().getString(R.string.placeholderArcs);
//
//                break;
//
//            case "Fiere e Mercati":
//
//                placeholderName=context.getResources().getString(R.string.placeholderArcs);
//
//                break;
//
//            case "Cimiteri e Memoriali":
//
//                placeholderName=context.getResources().getString(R.string.placeholderCemeteries);
//
//                break;
//
//
//            case "Edifici":
//
//                placeholderName=context.getResources().getString(R.string.placeholderBuildings);
//
//                break;
//
//            case "Ponti":
//
//                placeholderName=context.getResources().getString(R.string.placeholderBridges);
//
//                break;
//
//            case "Chiese, Oratori e Luoghi di culto":
//
//                placeholderName=context.getResources().getString(R.string.placeholderChurches);
//
//                break;
//
//            case "Altri monumenti e Luoghi di interesse":
//
//                placeholderName=context.getResources().getString(R.string.placeholderOtherSites);
//                break;
//
//        }
//
//    }
//
//
//    private void convertLanguageTypeOfSite(Site site) {
//
//
//        switch (site.typeOfSite) {
//            case "Theaters":
//
//                placeholderName = context.getResources().getString(R.string.placeholderTheaters);
//
//                break;
//
//            case "Palaces and Castles":
//
//                placeholderName = context.getResources().getString(R.string.placeholderCastles);
//
//                break;
//
//            case "Villas, Gardens and Parks":
//
//                placeholderName = context.getResources().getString(R.string.placeholderVillas);
//
//                break;
//
//            case "Museums and Art galleries":
//
//                placeholderName = context.getResources().getString(R.string.placeholderMuseums);
//
//                break;
//
//            case "Statues and Fountains":
//
//                placeholderName = context.getResources().getString(R.string.placeholderStatues);
//
//                break;
//
//            case "Squares and Streets":
//
//                placeholderName = context.getResources().getString(R.string.placeholderSquares);
//
//
//                break;
//
//            case "Arches, Gates and Walls":
//
//                placeholderName = context.getResources().getString(R.string.placeholderArcs);
//
//                break;
//
//            case "Fairs and Markets":
//
//                placeholderName = context.getResources().getString(R.string.placeholderArcs);
//
//                break;
//
//            case "Cemeteries and Memorials":
//
//                placeholderName = context.getResources().getString(R.string.placeholderCemeteries);
//                break;
//
//            case "Buildings":
//
//                placeholderName = context.getResources().getString(R.string.placeholderBuildings);
//
//                break;
//
//            case "Bridges":
//
//                placeholderName = context.getResources().getString(R.string.placeholderBridges);
//
//                break;
//
//            case "Churches, Oratories and Places of worship":
//
//                placeholderName = context.getResources().getString(R.string.placeholderChurches);
//
//                break;
//
//            case "Other monuments and Places of interest":
//
//                placeholderName = context.getResources().getString(R.string.placeholderOtherSites);
//
//                break;
//
//        }
//    }
//
//
//
//
//
//
//    static class ViewHolderDiscovery {
//        TextView vTitle;
//        TextView vDescription;
////        TextView vTime;
//        String id;
//        ImageView placeholderName;
//
//    }
//
//
//@Override
//public void onClick(View v){
//        i.putExtra("siteId",site.id);
//        context.startActivity(i);
//        }
//
//
//
//}//fine classe
