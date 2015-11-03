package org.wepush.open_tour.fragments_dialogs;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.media.Image;
import android.os.Bundle;
import android.os.Environment;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.CardView;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.ScrollView;
import android.widget.TextView;

import org.wepush.open_tour.DetailsActivity;
import org.wepush.open_tour.DiscoveryPreviewPagerActivity;
import org.wepush.open_tour.R;
import org.wepush.open_tour.SettingTourActivity;
import org.wepush.open_tour.structures.DB1SqlHelper;
import org.wepush.open_tour.structures.Site;
import org.wepush.open_tour.utils.Constants;
import org.wepush.open_tour.utils.DiscoveryListViewAdapter;
import org.wepush.open_tour.utils.Repository;

import java.util.ArrayList;
import java.util.Locale;

/**
 * Created by antoniocoppola on 05/10/15.
 */
public class DiscoveryPreviewPagerFragment extends Fragment implements View.OnClickListener{

    private ImageView previewMainImage,previewNlImage,previewNrImage,previewSlImage,previewSrImage,circleImage0,circleImage1,circleImage2,circleImage3,circleImage4;
    private TextView txtMain,txtNl,txtNr,txtSl,txtSr;
    private ArrayList<Site> arrayList;
//    private static ArrayList<Site> elementsOfList;
    private ArrayList<ImageView>arrayImageReferencesList,arrayCircleList;
    private ArrayList<TextView> arrayTextViewList,arrayTextBodyList;
//    private ArrayList<String>arraySiteNames;
    private static Context context;
    private String placeholderName="";
    private String city="";
    private String tabTypeOfSiteSelected="";
    private Intent i;
    private static int tabPosition;
    private CardView cd0,cd1,cd2,cd3,cd4;
    private boolean placeholderEverywhere=false;
    private String categoryForListView;

    private  Intent intent;

    private ListView lwDiscovery;



    public static DiscoveryPreviewPagerFragment newInstance(Context ctx,String  category, int position) {
        context=ctx;
//        elementsOfList=sites;
//        Log.d("miotag","FRAGMENT elementsList: "+elementsOfList.size());
        DiscoveryPreviewPagerFragment fragment = new DiscoveryPreviewPagerFragment();
        Bundle bundle = new Bundle();
        tabPosition=position;
        switch (category){
            case Constants.CATEGORY_VIEW_PAGER_MUSEUM:
                bundle.putString("category",Constants.CATEGORY_VIEW_PAGER_MUSEUM);
                break;

            case Constants.CATEGORY_VIEW_PAGER_CASTLES:
                bundle.putString("category",Constants.CATEGORY_VIEW_PAGER_CASTLES);
                break;

            case Constants.CATEGORY_VIEW_PAGER_CHURCH:
                bundle.putString("category",Constants.CATEGORY_VIEW_PAGER_CHURCH);
                break;

            case Constants.CATEGORY_VIEW_PAGER_VILLAS:
                bundle.putString("category",Constants.CATEGORY_VIEW_PAGER_VILLAS);
                break;

        }

        fragment.setArguments(bundle);
        return fragment;
    }



    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_discoverypreviewadapter, null);
        city=Repository.retrieve(context,Constants.KEY_CURRENT_CITY,String.class);

        String packagesChosen=Repository.retrieve(context, Constants.WHAT_I_WANT_TO_DOWNLOAD,String.class);
        if (packagesChosen.equals(Constants.DOWNLOADING_MAPS_ONLY)){
            placeholderEverywhere=true;
        }

        //section for ListView

        //TODO DUMMY arraylist<site>
//        ArrayList<Site> arrayListDummy=new ArrayList<>();
//        arrayListDummy.add(DB1SqlHelper.getInstance(getContext()).getSite("5506ae76694d610b31ae0000"));
//        arrayListDummy.add(DB1SqlHelper.getInstance(getContext()).getSite("5506ae76694d610b319d0200"));
//        arrayListDummy.add(DB1SqlHelper.getInstance(getContext()).getSite("5506ae76694d610b31960000"));
//        arrayListDummy.add(DB1SqlHelper.getInstance(getContext()).getSite("5506ae76694d610b31b90200"));
//        arrayListDummy.add(DB1SqlHelper.getInstance(getContext()).getSite("5506ae76694d610b31980200"));
//        arrayListDummy.add(DB1SqlHelper.getInstance(getContext()).getSite("5506ae76694d610b31b90200"));
//        arrayListDummy.add(DB1SqlHelper.getInstance(getContext()).getSite("5506ae76694d610b31980200"));
//        arrayListDummy.add(DB1SqlHelper.getInstance(getContext()).getSite("5506ae76694d610b31b90200"));
//        arrayListDummy.add(DB1SqlHelper.getInstance(getContext()).getSite("5506ae76694d610b31980200"));
//        arrayListDummy.add(DB1SqlHelper.getInstance(getContext()).getSite("5506ae76694d610b31b90200"));
//        arrayListDummy.add(DB1SqlHelper.getInstance(getContext()).getSite("5506ae76694d610b31980200"));
//        arrayListDummy.add(DB1SqlHelper.getInstance(getContext()).getSite("5506ae76694d610b31b90200"));
//        arrayListDummy.add(DB1SqlHelper.getInstance(getContext()).getSite("5506ae76694d610b31980200"));
//        arrayListDummy.add(DB1SqlHelper.getInstance(getContext()).getSite("5506ae76694d610b31b90200"));
//        arrayListDummy.add(DB1SqlHelper.getInstance(getContext()).getSite("5506ae76694d610b31980200"));
//        arrayListDummy.add(DB1SqlHelper.getInstance(getContext()).getSite("5506ae76694d610b31b90200"));
//        arrayListDummy.add(DB1SqlHelper.getInstance(getContext()).getSite("5506ae76694d610b31980200"));
//        arrayListDummy.add(DB1SqlHelper.getInstance(getContext()).getSite("5506ae76694d610b31b90200"));
//        arrayListDummy.add(DB1SqlHelper.getInstance(getContext()).getSite("5506ae76694d610b31980200"));

        lwDiscovery=(ListView) view.findViewById(R.id.lwDiscoveryActivity);
        lwDiscovery.setAdapter(new DiscoveryListViewAdapter(context, SettingTourActivity.churches));
        setListViewHeightBasedOnChildren(lwDiscovery);
        lwDiscovery.setFocusable(false);
//        lwDiscovery.setOnItemClickListener(this);

        intent = new Intent(context,DetailsActivity.class);


        lwDiscovery.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                if (categoryForListView != null){
                    switch (categoryForListView){
                        case Constants.CATEGORY_VIEW_PAGER_MUSEUM:
                            intent.putExtra("siteId",SettingTourActivity.museums.get(position).id);
                            startActivity(intent);
                        break;


                        case Constants.CATEGORY_VIEW_PAGER_VILLAS:
                            intent.putExtra("siteId", SettingTourActivity.villas.get(position).id);
                            startActivity(intent);
                            break;


                        case Constants.CATEGORY_VIEW_PAGER_CHURCH:
                            intent.putExtra("siteId",SettingTourActivity.churches.get(position).id);
                            startActivity(intent);
                            break;


                        case Constants.CATEGORY_VIEW_PAGER_CASTLES:
                            intent.putExtra("siteId",SettingTourActivity.palaces.get(position).id);
                            startActivity(intent);
                            break;
                    }
                }
            }
        });







//TODO semplificazione
        arrayList=new ArrayList<>();
        arrayImageReferencesList=new ArrayList<ImageView>();
        arrayTextViewList=new ArrayList<>();
//        arraySiteNames=new ArrayList<>();
//        arrayCircleList=new ArrayList<ImageView>();
//        arrayTextTitleList=new ArrayList<TextView>();
//        arrayTextBodyList=new ArrayList<TextView>();
//
//
        previewMainImage=(ImageView) view.findViewById(R.id.discoveryPreviewMainImage);
        previewNlImage=(ImageView) view.findViewById(R.id.discoverPreviewImageNl);
        previewNrImage=(ImageView) view.findViewById(R.id.discoverPreviewImageNr);
        previewSlImage=(ImageView) view.findViewById(R.id.discoverPreviewImageSl);
        previewSrImage=(ImageView) view.findViewById(R.id.discoverPreviewImageSr);

        txtMain=(TextView) view.findViewById(R.id.txtMainImage);
        txtNl=(TextView) view.findViewById(R.id.txtNlImage);
        txtNr=(TextView) view.findViewById(R.id.txtNrImage);
        txtSl=(TextView) view.findViewById(R.id.txtSlImage);
        txtSr=(TextView) view.findViewById(R.id.txtSrImage);

//        circleImage0=(ImageView) view.findViewById(R.id.circleImageDiscoveryPreviewList0);
//        circleImage1=(ImageView) view.findViewById(R.id.circleImageDiscoveryPreviewList1);
//        circleImage2=(ImageView) view.findViewById(R.id.circleImageDiscoveryPreviewList2);
//        circleImage3=(ImageView) view.findViewById(R.id.circleImageDiscoveryPreviewList3);
//        circleImage4=(ImageView) view.findViewById(R.id.circleImageDiscoveryPreviewList4);
//
//        txtTitle0=(TextView) view.findViewById(R.id.txtTitleDiscoveryPreviewList0);
//        txtTitle1=(TextView) view.findViewById(R.id.txtTitleDiscoveryPreviewList1);
//        txtTitle2=(TextView) view.findViewById(R.id.txtTitleDiscoveryPreviewList2);
//        txtTitle3=(TextView) view.findViewById(R.id.txtTitleDiscoveryPreviewList3);
//        txtTitle4=(TextView) view.findViewById(R.id.txtTitleDiscoveryPreviewList4);
//
//        txtBody0=(TextView) view.findViewById(R.id.txtBodyDiscoveryPreviewList0);
//        txtBody1=(TextView) view.findViewById(R.id.txtBodyDiscoveryPreviewList1);
//        txtBody2=(TextView) view.findViewById(R.id.txtBodyDiscoveryPreviewList2);
//        txtBody3=(TextView) view.findViewById(R.id.txtBodyDiscoveryPreviewList3);
//        txtBody4=(TextView) view.findViewById(R.id.txtBodyDiscoveryPreviewList4);
//
//        cd0=(CardView)view.findViewById(R.id.cdTouch0);
//        cd1=(CardView)view.findViewById(R.id.cdTouch1);
//        cd2=(CardView)view.findViewById(R.id.cdTouch2);
//        cd3=(CardView)view.findViewById(R.id.cdTouch3);
//        cd4=(CardView)view.findViewById(R.id.cdTouch4);
//
//        cd0.setOnClickListener(this);
//        cd1.setOnClickListener(this);
//        cd2.setOnClickListener(this);
//        cd3.setOnClickListener(this);
//        cd4.setOnClickListener(this);
//
//
//
//
//
//
//
//
        arrayImageReferencesList.add(previewMainImage);
        arrayImageReferencesList.add(previewNlImage);
        arrayImageReferencesList.add(previewNrImage);
        arrayImageReferencesList.add(previewSlImage);
        arrayImageReferencesList.add(previewSrImage);

        arrayTextViewList.add(txtMain);
        arrayTextViewList.add(txtNl);
        arrayTextViewList.add(txtNr);
        arrayTextViewList.add(txtSl);
        arrayTextViewList.add(txtSr);
//        arrayCircleList.add(circleImage0);
//        arrayCircleList.add(circleImage1);
//        arrayCircleList.add(circleImage2);
//        arrayCircleList.add(circleImage3);
//        arrayCircleList.add(circleImage4);
//
//        arrayTextTitleList.add(txtTitle0);
//        arrayTextTitleList.add(txtTitle1);
//        arrayTextTitleList.add(txtTitle2);
//        arrayTextTitleList.add(txtTitle3);
//        arrayTextTitleList.add(txtTitle4);
//
//        arrayTextBodyList.add(txtBody0);
//        arrayTextBodyList.add(txtBody1);
//        arrayTextBodyList.add(txtBody2);
//        arrayTextBodyList.add(txtBody3);
//        arrayTextBodyList.add(txtBody4);
//

        Bundle bundle=getArguments();
        categoryForListView=bundle.getString("category");


        switch (categoryForListView){
            case Constants.CATEGORY_VIEW_PAGER_VILLAS:
                tabTypeOfSiteSelected=Constants.CATEGORY_VIEW_PAGER_VILLAS;
                selectAppropriateSite(Constants.CATEGORY_VIEW_PAGER_VILLAS);
                lwDiscovery.setAdapter(new DiscoveryListViewAdapter(context, SettingTourActivity.villas));

                break;

            case Constants.CATEGORY_VIEW_PAGER_CHURCH:
                tabTypeOfSiteSelected=Constants.CATEGORY_VIEW_PAGER_CHURCH;
                selectAppropriateSite(Constants.CATEGORY_VIEW_PAGER_CHURCH);
                lwDiscovery.setAdapter(new DiscoveryListViewAdapter(context, SettingTourActivity.churches));

                break;

            case Constants.CATEGORY_VIEW_PAGER_CASTLES:
                tabTypeOfSiteSelected=Constants.CATEGORY_VIEW_PAGER_CASTLES;
                selectAppropriateSite(Constants.CATEGORY_VIEW_PAGER_CASTLES);
                lwDiscovery.setAdapter(new DiscoveryListViewAdapter(context, SettingTourActivity.palaces));


                break;

            case Constants.CATEGORY_VIEW_PAGER_MUSEUM:
                tabTypeOfSiteSelected=Constants.CATEGORY_VIEW_PAGER_MUSEUM;
                selectAppropriateSite(Constants.CATEGORY_VIEW_PAGER_MUSEUM);
                lwDiscovery.setAdapter(new DiscoveryListViewAdapter(context, SettingTourActivity.museums));


                break;
        }


        return view;
    }


    private void selectAppropriateSite(String s){
        String cityToChoose=Repository.retrieve(context,Constants.KEY_CURRENT_CITY,String.class);


        if (cityToChoose.equals(Constants.CITY_PALERMO)) {
//            Log.d("miotag", "dal fragment la città selezionata è PALERMO");
            switch (s) {

                case Constants.CATEGORY_VIEW_PAGER_MUSEUM:
                    //First site is always the main one

                    arrayList.add(DB1SqlHelper.getInstance(getContext()).getSite("5506ae76694d610b31ae0000"));
                    arrayList.add(DB1SqlHelper.getInstance(getContext()).getSite("5506ae76694d610b319d0200"));
                    arrayList.add(DB1SqlHelper.getInstance(getContext()).getSite("5506ae76694d610b31960000"));
                    arrayList.add(DB1SqlHelper.getInstance(getContext()).getSite("5506ae76694d610b31b90200"));
                    arrayList.add(DB1SqlHelper.getInstance(getContext()).getSite("5506ae76694d610b31980200"));

//                    arraySiteNames.add(arrayList.get(0).name);
//                    arraySiteNames.add(arrayList.get(1).name);
//                    arraySiteNames.add(arrayList.get(2).name);
//                    arraySiteNames.add(arrayList.get(3).name);
//                    arraySiteNames.add(arrayList.get(4).name);
//                    lwDiscovery.setAdapter(new DiscoveryListViewAdapter(context, DiscoveryPreviewPagerActivity.museums));
//                    fillList(arrayList);


//                    Log.d("miotag", "fragment MUSEUM: arrayList.get(3" + arrayList.get(3).name);

                    break;

                case Constants.CATEGORY_VIEW_PAGER_VILLAS:
                    arrayList.add(DB1SqlHelper.getInstance(context).getSite("5506ae76694d610b31560000"));
                    arrayList.add(DB1SqlHelper.getInstance(context).getSite("5506ae76694d610b31060100"));
                    arrayList.add(DB1SqlHelper.getInstance(context).getSite("5506ae76694d610b31920000"));
                    arrayList.add(DB1SqlHelper.getInstance(context).getSite("5506ae76694d610b31230100"));
                    arrayList.add(DB1SqlHelper.getInstance(context).getSite("5506ae76694d610b31f50000"));


//                    arraySiteNames.add(arrayList.get(0).name);
//                    arraySiteNames.add(arrayList.get(1).name);
//                    arraySiteNames.add(arrayList.get(2).name);
//                    arraySiteNames.add(arrayList.get(3).name);
//                    arraySiteNames.add(arrayList.get(4).name);
//                    lwDiscovery.setAdapter(new DiscoveryListViewAdapter(context, DiscoveryPreviewPagerActivity.villas));

//                    fillList(arrayList);
                    break;


                case Constants.CATEGORY_VIEW_PAGER_CASTLES:
                    arrayList.add(DB1SqlHelper.getInstance(context).getSite("5506ae76694d610b31760000"));
                    arrayList.add(DB1SqlHelper.getInstance(context).getSite("5506ae76694d610b31140000"));
                    arrayList.add(DB1SqlHelper.getInstance(context).getSite("5506ae76694d610b31660000"));
                    arrayList.add(DB1SqlHelper.getInstance(context).getSite("5506ae76694d610b31a50000"));
                    arrayList.add(DB1SqlHelper.getInstance(context).getSite("5506ae77694d610b31b10300"));

//                    arraySiteNames.add("PALAZZO DEI NORMANNI");
//                    arraySiteNames.add("CASTELLO DELLA ZISA");
//                    arraySiteNames.add("VILLA GARIBALDI");
//                    arraySiteNames.add("PARCO D'ORLEANS");
//                    arraySiteNames.add("VILLA GIULIA");
//                    lwDiscovery.setAdapter(new DiscoveryListViewAdapter(context, DiscoveryPreviewPagerActivity.palaces));


//                    fillList(arrayList);
                    break;


                case Constants.CATEGORY_VIEW_PAGER_CHURCH:
                    arrayList.add(DB1SqlHelper.getInstance(getActivity()).getSite("5506ae76694d610b31220000"));
                    arrayList.add(DB1SqlHelper.getInstance(getActivity()).getSite("5506ae76694d610b31820000"));
                    arrayList.add(DB1SqlHelper.getInstance(getActivity()).getSite("5506ae76694d610b31ed0000"));
                    arrayList.add(DB1SqlHelper.getInstance(getActivity()).getSite("5506ae76694d610b313c0000"));
                    arrayList.add(DB1SqlHelper.getInstance(getActivity()).getSite("5506ae76694d610b315b0100"));
//                    lwDiscovery.setAdapter(new DiscoveryListViewAdapter(context, DiscoveryPreviewPagerActivity.churches));


//                    fillList(arrayList);

//                    Log.d("miotag", "fragment CHURCH: arrayList.get(3" + arrayList.get(3).name);
                    break;


            }
        } else
        {
            //Milan

//            Log.d("miotag","dal fragment la città scelta è : MILANO");

            switch (s) {

                case Constants.CATEGORY_VIEW_PAGER_MUSEUM:
                    //First site is always the main one

                    arrayList.add(DB1SqlHelper.getInstance(getContext()).getSite("5551cf11694d6103fa040100"));
                    arrayList.add(DB1SqlHelper.getInstance(getContext()).getSite("5551fe27694d6103fa2b0100"));
                    arrayList.add(DB1SqlHelper.getInstance(getContext()).getSite("55530f60694d6103fac80100"));
                    arrayList.add(DB1SqlHelper.getInstance(getContext()).getSite("5551cc00694d6103faf10000"));
                    arrayList.add(DB1SqlHelper.getInstance(getContext()).getSite("5550be56694d6103fa020000"));

//                    lwDiscovery.setAdapter(new DiscoveryListViewAdapter(context, SettingTourActivity.museums));

//                    fillList(arrayList);


                    Log.d("miotag", "fragment MUSEUM: arrayList.get(3" + arrayList.get(3).name);

                    break;

                case Constants.CATEGORY_VIEW_PAGER_VILLAS:
                    arrayList.add(DB1SqlHelper.getInstance(context).getSite("5550cbaa694d6103fa4c0000"));
                    arrayList.add(DB1SqlHelper.getInstance(context).getSite("5552156f694d6103faa30100"));
                    arrayList.add(DB1SqlHelper.getInstance(context).getSite("55531d6c694d6103faff0100"));
                    arrayList.add(DB1SqlHelper.getInstance(context).getSite("5552208e694d6103fabd0100"));
                    arrayList.add(DB1SqlHelper.getInstance(context).getSite("554b2c3a694d61267ec90000"));
//                    lwDiscovery.setAdapter(new DiscoveryListViewAdapter(context, SettingTourActivity.villas));


//                    fillList(arrayList);
                    break;


                case Constants.CATEGORY_VIEW_PAGER_CASTLES:
                    arrayList.add(DB1SqlHelper.getInstance(context).getSite("554b7461694d61267e9b0100"));
                    arrayList.add(DB1SqlHelper.getInstance(context).getSite("5549f352694d61267e880000"));
                    arrayList.add(DB1SqlHelper.getInstance(context).getSite("554b3360694d61267ef90000"));
                    arrayList.add(DB1SqlHelper.getInstance(context).getSite("554b3a3e694d61267e020100"));
                    arrayList.add(DB1SqlHelper.getInstance(context).getSite("5549e224694d61267e2b0000"));
//                    lwDiscovery.setAdapter(new DiscoveryListViewAdapter(context, SettingTourActivity.palaces));


//                    fillList(arrayList);
                    break;


                case Constants.CATEGORY_VIEW_PAGER_CHURCH:
                    arrayList.add(DB1SqlHelper.getInstance(getActivity()).getSite("554b78f9694d61267ea70100"));
                    arrayList.add(DB1SqlHelper.getInstance(getActivity()).getSite("554b6d88694d61267e5f0100"));
                    arrayList.add(DB1SqlHelper.getInstance(getActivity()).getSite("554b8a6a694d6147f85e0000"));
                    arrayList.add(DB1SqlHelper.getInstance(getActivity()).getSite("555c9def694d6103faf70200"));
                    arrayList.add(DB1SqlHelper.getInstance(getActivity()).getSite("554b8b24694d6147f8670000"));
//                    lwDiscovery.setAdapter(new DiscoveryListViewAdapter(context, SettingTourActivity.churches));

//                    fillList(arrayList);

                    Log.d("miotag", "fragment CHURCH: arrayList.get(3" + arrayList.get(3).name);
                    break;


            }
        }
        setLayoutInPlace(arrayList);


    }

    private void setLayoutInPlace(ArrayList<Site> arrayList) {
        String currentCity = "";

        Bitmap bitmap;
        for (int i=0; i<arrayList.size();i++) {


            Site site=arrayList.get(i);
            String imagePath = site.pictureUrl;
            ImageView imageToSet=arrayImageReferencesList.get(i);
            TextView textOver=arrayTextViewList.get(i);
            textOver.setText(arrayList.get(i).name);
            textOver.setAllCaps(true);
            imageToSet.setOnClickListener(this);

            currentCity = Repository.retrieve(context, Constants.KEY_CURRENT_CITY, String.class);
//            Log.d("miotag", "currentCity: " + currentCity);


            if (currentCity.equals(Constants.CITY_MILAN)) {

                if ((TextUtils.equals(imagePath, "placeholder")) || (placeholderEverywhere)) {

                    if (TextUtils.equals(Locale.getDefault().getDisplayLanguage(), "English")) {
                        convertLanguageTypeOfSite(site);
                    } else {
                        chooseThemeColors(site);
                    }
                    imagePath = placeholderName;
                    int drawableResource = context.getResources().getIdentifier(imagePath, "drawable", context.getPackageName());
                    imageToSet.setImageResource(drawableResource);
                    imageToSet.setScaleType(ImageView.ScaleType.FIT_XY);


                } else {
                    imagePath = imagePath.substring(79, imagePath.length() - 4);
                    imagePath = "milano_images/" + imagePath + ".jpg";
                    bitmap = BitmapFactory.decodeFile(context.getExternalFilesDir(Environment.DIRECTORY_PICTURES) + "/" + imagePath);
                    Drawable mDrawable = new BitmapDrawable(context.getResources(), bitmap);
                    imageToSet.setImageDrawable(mDrawable);
                    imageToSet.setScaleType(ImageView.ScaleType.FIT_XY);

                }
            } else {
                if ((TextUtils.equals(imagePath, "placeholder")) || (placeholderEverywhere)) {

                    if (TextUtils.equals(Locale.getDefault().getDisplayLanguage(), "English")) {
                        convertLanguageTypeOfSite(site);
                    } else {
                        chooseThemeColors(site);
                    }

                    imagePath = placeholderName;
                    int drawableResource = context.getResources().getIdentifier(imagePath, "drawable", context.getPackageName());
                    imageToSet.setImageResource(drawableResource);
                    imageToSet.setScaleType(ImageView.ScaleType.FIT_XY);


                } else {

                    if (TextUtils.equals(Locale.getDefault().getDisplayLanguage(), "English")) {
                        convertLanguageTypeOfSite(site);
                    } else {
                        chooseThemeColors(site);
                    }

                    imagePath = imagePath.substring(79, imagePath.length() - 4);
                    imagePath = "palermo_images/" + imagePath + ".jpg";
                    bitmap = BitmapFactory.decodeFile(context.getExternalFilesDir(Environment.DIRECTORY_PICTURES) + "/" + imagePath);
                    Drawable mDrawable = new BitmapDrawable(context.getResources(), bitmap);
                    imageToSet.setImageDrawable(mDrawable);
                    imageToSet.setScaleType(ImageView.ScaleType.FIT_XY);
                }


            }

        }//Fine for
    }//Fine setLayout



    private void chooseThemeColors(Site site) {
//        Log.d("miotag","TimeLine ChooseTheme: "+site.typeOfSite);

//TODO check if there's a better solution to manage sitesType in languages
        switch (site.typeOfSite) {
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

    }


    private void convertLanguageTypeOfSite(Site site) {


        switch (site.typeOfSite) {
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


    }

    @Override
    public void onClick(View v){
        Log.d("miotag", "ON CLICK");

        if (city.equals(Constants.CITY_PALERMO)) {


            switch (v.getId()){

                case R.id.discoveryPreviewMainImage:
                    switch (tabTypeOfSiteSelected) {
                        case Constants.CATEGORY_VIEW_PAGER_MUSEUM:
                            i = new Intent(context, DetailsActivity.class);
                            i.putExtra("siteId", "5506ae76694d610b31ae0000");
                            startActivity(i);
                            break;

                        case Constants.CATEGORY_VIEW_PAGER_CASTLES:
                            i = new Intent(context, DetailsActivity.class);
                            i.putExtra("siteId", "5506ae76694d610b31760000");
                            startActivity(i);
                            break;

                        case Constants.CATEGORY_VIEW_PAGER_CHURCH:
                            i = new Intent(context, DetailsActivity.class);
                            i.putExtra("siteId", "5506ae76694d610b31220000");
                            startActivity(i);
                            break;

                        case Constants.CATEGORY_VIEW_PAGER_VILLAS:
                            i = new Intent(context, DetailsActivity.class);
                            i.putExtra("siteId", "5506ae76694d610b31560000");
                            startActivity(i);
                            break;
                    }
                    break;

                case R.id.discoverPreviewImageNl:
                    switch (tabTypeOfSiteSelected) {
                        case Constants.CATEGORY_VIEW_PAGER_MUSEUM:
                            i = new Intent(context, DetailsActivity.class);
                            i.putExtra("siteId", "5506ae76694d610b319d0200");
                            startActivity(i);
                            break;

                        case Constants.CATEGORY_VIEW_PAGER_CASTLES:
                            i = new Intent(context, DetailsActivity.class);
                            i.putExtra("siteId", "5506ae76694d610b31140000");
                            startActivity(i);
                            break;

                        case Constants.CATEGORY_VIEW_PAGER_CHURCH:
                            i = new Intent(context, DetailsActivity.class);
                            i.putExtra("siteId", "5506ae76694d610b31820000");
                            startActivity(i);
                            break;

                        case Constants.CATEGORY_VIEW_PAGER_VILLAS:
                            i = new Intent(context, DetailsActivity.class);
                            i.putExtra("siteId", "5506ae76694d610b31060100");
                            startActivity(i);
                            break;

                    }
                    break;

                case R.id.discoverPreviewImageNr:

                    switch (tabTypeOfSiteSelected) {
                        case Constants.CATEGORY_VIEW_PAGER_MUSEUM:
                            i = new Intent(context, DetailsActivity.class);
                            i.putExtra("siteId", "5506ae76694d610b31960000");
                            startActivity(i);
                            break;

                        case Constants.CATEGORY_VIEW_PAGER_CASTLES:
                            i = new Intent(context, DetailsActivity.class);
                            i.putExtra("siteId", "5506ae76694d610b31660000");
                            startActivity(i);
                            break;

                        case Constants.CATEGORY_VIEW_PAGER_CHURCH:
                            i = new Intent(context, DetailsActivity.class);
                            i.putExtra("siteId", "5506ae76694d610b31ed0000");
                            startActivity(i);
                            break;

                        case Constants.CATEGORY_VIEW_PAGER_VILLAS:
                            i = new Intent(context, DetailsActivity.class);
                            i.putExtra("siteId", "5506ae76694d610b31920000");
                            startActivity(i);
                            break;

                    }
                    break;

                case R.id.discoverPreviewImageSl:
                    switch (tabTypeOfSiteSelected) {
                        case Constants.CATEGORY_VIEW_PAGER_MUSEUM:
                            i = new Intent(context, DetailsActivity.class);
                            i.putExtra("siteId", "5506ae76694d610b31b90200");
                            startActivity(i);
                            break;

                        case Constants.CATEGORY_VIEW_PAGER_CASTLES:
                            i = new Intent(context, DetailsActivity.class);
                            i.putExtra("siteId", "5506ae76694d610b31a50000");
                            startActivity(i);
                            break;

                        case Constants.CATEGORY_VIEW_PAGER_CHURCH:
                            i = new Intent(context, DetailsActivity.class);
                            i.putExtra("siteId", "5506ae76694d610b313c0000");
                            startActivity(i);
                            break;

                        case Constants.CATEGORY_VIEW_PAGER_VILLAS:
                            i = new Intent(context, DetailsActivity.class);
                            i.putExtra("siteId", "5506ae76694d610b31230100");
                            startActivity(i);
                            break;

                    }
                    break;


                case R.id.discoverPreviewImageSr:

                    switch (tabTypeOfSiteSelected) {
                        case Constants.CATEGORY_VIEW_PAGER_MUSEUM:
                            i = new Intent(context, DetailsActivity.class);
                            i.putExtra("siteId", "5506ae76694d610b31980200");
                            startActivity(i);
                            break;

                        case Constants.CATEGORY_VIEW_PAGER_CASTLES:
                            i = new Intent(context, DetailsActivity.class);
                            i.putExtra("siteId", "5506ae77694d610b31b10300");
                            startActivity(i);
                            break;

                        case Constants.CATEGORY_VIEW_PAGER_CHURCH:
                            i = new Intent(context, DetailsActivity.class);
                            i.putExtra("siteId", "5506ae76694d610b315b0100");
                            startActivity(i);
                            break;

                        case Constants.CATEGORY_VIEW_PAGER_VILLAS:
                            i = new Intent(context, DetailsActivity.class);
                            i.putExtra("siteId", "5506ae76694d610b31f50000");
                            startActivity(i);
                            break;

                    }
                    break;


                //section for list


//                case R.id.cdTouch0:
//                    switch (tabTypeOfSiteSelected) {
//                        case Constants.CATEGORY_VIEW_PAGER_MUSEUM:
//                            i = new Intent(context, DetailsActivity.class);
//                            i.putExtra("siteId", "5506ae76694d610b31ae0000");
//                            startActivity(i);
//                            break;
//
//                        case Constants.CATEGORY_VIEW_PAGER_CASTLES:
//                            i = new Intent(context, DetailsActivity.class);
//                            i.putExtra("siteId", "5506ae76694d610b31760000");
//                            startActivity(i);
//                            break;
//
//                        case Constants.CATEGORY_VIEW_PAGER_CHURCH:
//                            i = new Intent(context, DetailsActivity.class);
//                            i.putExtra("siteId", "5506ae76694d610b31220000");
//                            startActivity(i);
//                            break;
//
//                        case Constants.CATEGORY_VIEW_PAGER_VILLAS:
//                            i = new Intent(context, DetailsActivity.class);
//                            i.putExtra("siteId", "5506ae76694d610b31560000");
//                            startActivity(i);
//                            break;
//                    }
//                    break;
//
//                case R.id.cdTouch1:
//                    switch (tabTypeOfSiteSelected) {
//                        case Constants.CATEGORY_VIEW_PAGER_MUSEUM:
//                            i = new Intent(context, DetailsActivity.class);
//                            i.putExtra("siteId", "5506ae76694d610b319d0200");
//                            startActivity(i);
//                            break;
//
//                        case Constants.CATEGORY_VIEW_PAGER_CASTLES:
//                            i = new Intent(context, DetailsActivity.class);
//                            i.putExtra("siteId", "5506ae76694d610b31140000");
//                            startActivity(i);
//                            break;
//
//                        case Constants.CATEGORY_VIEW_PAGER_CHURCH:
//                            i = new Intent(context, DetailsActivity.class);
//                            i.putExtra("siteId", "5506ae76694d610b31820000");
//                            startActivity(i);
//                            break;
//
//                        case Constants.CATEGORY_VIEW_PAGER_VILLAS:
//                            i = new Intent(context, DetailsActivity.class);
//                            i.putExtra("siteId", "5506ae76694d610b31060100");
//                            startActivity(i);
//                            break;
//
//                    }
//                    break;
//
//                case R.id.cdTouch2:
//
//                    switch (tabTypeOfSiteSelected) {
//                        case Constants.CATEGORY_VIEW_PAGER_MUSEUM:
//                            i = new Intent(context, DetailsActivity.class);
//                            i.putExtra("siteId", "5506ae76694d610b31960000");
//                            startActivity(i);
//                            break;
//
//                        case Constants.CATEGORY_VIEW_PAGER_CASTLES:
//                            i = new Intent(context, DetailsActivity.class);
//                            i.putExtra("siteId", "5506ae76694d610b31660000");
//                            startActivity(i);
//                            break;
//
//                        case Constants.CATEGORY_VIEW_PAGER_CHURCH:
//                            i = new Intent(context, DetailsActivity.class);
//                            i.putExtra("siteId", "5506ae76694d610b31ed0000");
//                            startActivity(i);
//                            break;
//
//                        case Constants.CATEGORY_VIEW_PAGER_VILLAS:
//                            i = new Intent(context, DetailsActivity.class);
//                            i.putExtra("siteId", "5506ae76694d610b31920000");
//                            startActivity(i);
//                            break;
//
//                    }
//                    break;
//
//                case R.id.cdTouch3:
//                    switch (tabTypeOfSiteSelected) {
//                        case Constants.CATEGORY_VIEW_PAGER_MUSEUM:
//                            i = new Intent(context, DetailsActivity.class);
//                            i.putExtra("siteId", "5506ae76694d610b31b90200");
//                            startActivity(i);
//                            break;
//
//                        case Constants.CATEGORY_VIEW_PAGER_CASTLES:
//                            i = new Intent(context, DetailsActivity.class);
//                            i.putExtra("siteId", "5506ae76694d610b31a50000");
//                            startActivity(i);
//                            break;
//
//                        case Constants.CATEGORY_VIEW_PAGER_CHURCH:
//                            i = new Intent(context, DetailsActivity.class);
//                            i.putExtra("siteId", "5506ae76694d610b313c0000");
//                            startActivity(i);
//                            break;
//
//                        case Constants.CATEGORY_VIEW_PAGER_VILLAS:
//                            i = new Intent(context, DetailsActivity.class);
//                            i.putExtra("siteId", "5506ae76694d610b31230100");
//                            startActivity(i);
//                            break;
//
//                    }
//                    break;
//
//
//                case R.id.cdTouch4:
//
//                    switch (tabTypeOfSiteSelected) {
//                        case Constants.CATEGORY_VIEW_PAGER_MUSEUM:
//                            i = new Intent(context, DetailsActivity.class);
//                            i.putExtra("siteId", "5506ae76694d610b31980200");
//                            startActivity(i);
//                            break;
//
//                        case Constants.CATEGORY_VIEW_PAGER_CASTLES:
//                            i = new Intent(context, DetailsActivity.class);
//                            i.putExtra("siteId", "5506ae77694d610b31b10300");
//                            startActivity(i);
//                            break;
//
//                        case Constants.CATEGORY_VIEW_PAGER_CHURCH:
//                            i = new Intent(context, DetailsActivity.class);
//                            i.putExtra("siteId", "5506ae76694d610b315b0100");
//                            startActivity(i);
//                            break;
//
//                        case Constants.CATEGORY_VIEW_PAGER_VILLAS:
//                            i = new Intent(context, DetailsActivity.class);
//                            i.putExtra("siteId", "5506ae76694d610b31f50000");
//                            startActivity(i);
//                            break;
//
//                    }
//                    break;




            }
        } else {
            //milan
            switch (v.getId()){


                case R.id.discoveryPreviewMainImage:

                    switch (tabTypeOfSiteSelected) {
                        case Constants.CATEGORY_VIEW_PAGER_MUSEUM:
                            i = new Intent(context, DetailsActivity.class);
                            i.putExtra("siteId", "5551cf11694d6103fa040100");
                            startActivity(i);
                            break;

                        case Constants.CATEGORY_VIEW_PAGER_CASTLES:
                            i = new Intent(context, DetailsActivity.class);
                            i.putExtra("siteId", "554b7461694d61267e9b0100");
                            startActivity(i);
                            break;

                        case Constants.CATEGORY_VIEW_PAGER_CHURCH:
                            i = new Intent(context, DetailsActivity.class);
                            i.putExtra("siteId", "554b78f9694d61267ea70100");
                            startActivity(i);
                            break;

                        case Constants.CATEGORY_VIEW_PAGER_VILLAS:
                            i = new Intent(context, DetailsActivity.class);
                            i.putExtra("siteId", "5550cbaa694d6103fa4c0000");
                            startActivity(i);
                            break;
                    }
                    break;

                case R.id.discoverPreviewImageNl:
                    switch (tabTypeOfSiteSelected) {
                        case Constants.CATEGORY_VIEW_PAGER_MUSEUM:
                            i = new Intent(context, DetailsActivity.class);
                            i.putExtra("siteId", "5551fe27694d6103fa2b0100");
                            startActivity(i);
                            break;

                        case Constants.CATEGORY_VIEW_PAGER_CASTLES:
                            i = new Intent(context, DetailsActivity.class);
                            i.putExtra("siteId", "5549f352694d61267e880000");
                            startActivity(i);
                            break;

                        case Constants.CATEGORY_VIEW_PAGER_CHURCH:
                            i = new Intent(context, DetailsActivity.class);
                            i.putExtra("siteId", "554b6d88694d61267e5f0100");
                            startActivity(i);
                            break;

                        case Constants.CATEGORY_VIEW_PAGER_VILLAS:
                            i = new Intent(context, DetailsActivity.class);
                            i.putExtra("siteId", "5552156f694d6103faa30100");
                            startActivity(i);
                            break;

                    }
                    break;

                case R.id.discoverPreviewImageNr:

                    switch (tabTypeOfSiteSelected) {
                        case Constants.CATEGORY_VIEW_PAGER_MUSEUM:
                            i = new Intent(context, DetailsActivity.class);
                            i.putExtra("siteId", "55530f60694d6103fac80100");
                            startActivity(i);
                            break;

                        case Constants.CATEGORY_VIEW_PAGER_CASTLES:
                            i = new Intent(context, DetailsActivity.class);
                            i.putExtra("siteId", "554b3360694d61267ef90000");
                            startActivity(i);
                            break;

                        case Constants.CATEGORY_VIEW_PAGER_CHURCH:
                            i = new Intent(context, DetailsActivity.class);
                            i.putExtra("siteId", "554b8a6a694d6147f85e0000");
                            startActivity(i);
                            break;

                        case Constants.CATEGORY_VIEW_PAGER_VILLAS:
                            i = new Intent(context, DetailsActivity.class);
                            i.putExtra("siteId", "55531d6c694d6103faff0100");
                            startActivity(i);
                            break;

                    }
                    break;

                case R.id.discoverPreviewImageSl:
                    switch (tabTypeOfSiteSelected) {
                        case Constants.CATEGORY_VIEW_PAGER_MUSEUM:
                            i = new Intent(context, DetailsActivity.class);
                            i.putExtra("siteId", "5551cc00694d6103faf10000");
                            startActivity(i);
                            break;

                        case Constants.CATEGORY_VIEW_PAGER_CASTLES:
                            i = new Intent(context, DetailsActivity.class);
                            i.putExtra("siteId", "554b3a3e694d61267e020100");
                            startActivity(i);
                            break;

                        case Constants.CATEGORY_VIEW_PAGER_CHURCH:
                            i = new Intent(context, DetailsActivity.class);
                            i.putExtra("siteId", "555c9def694d6103faf70200");
                            startActivity(i);
                            break;

                        case Constants.CATEGORY_VIEW_PAGER_VILLAS:
                            i = new Intent(context, DetailsActivity.class);
                            i.putExtra("siteId", "5552208e694d6103fabd0100");
                            startActivity(i);
                            break;

                    }
                    break;


                case R.id.discoverPreviewImageSr:

                    switch (tabTypeOfSiteSelected) {
                        case Constants.CATEGORY_VIEW_PAGER_MUSEUM:
                            i = new Intent(context, DetailsActivity.class);
                            i.putExtra("siteId", "5550be56694d6103fa020000");
                            startActivity(i);
                            break;

                        case Constants.CATEGORY_VIEW_PAGER_CASTLES:
                            i = new Intent(context, DetailsActivity.class);
                            i.putExtra("siteId", "5549e224694d61267e2b0000");
                            startActivity(i);
                            break;

                        case Constants.CATEGORY_VIEW_PAGER_CHURCH:
                            i = new Intent(context, DetailsActivity.class);
                            i.putExtra("siteId", "554b8b24694d6147f8670000");
                            startActivity(i);
                            break;

                        case Constants.CATEGORY_VIEW_PAGER_VILLAS:
                            i = new Intent(context, DetailsActivity.class);
                            i.putExtra("siteId", "554b2c3a694d61267ec90000");
                            startActivity(i);
                            break;

                    }
                    break;


                //section for list



//                case R.id.cdTouch0:
//
//                    switch (tabTypeOfSiteSelected) {
//                        case Constants.CATEGORY_VIEW_PAGER_MUSEUM:
//                            i = new Intent(context, DetailsActivity.class);
//                            i.putExtra("siteId", "5551cf11694d6103fa040100");
//                            startActivity(i);
//                            break;
//
//                        case Constants.CATEGORY_VIEW_PAGER_CASTLES:
//                            i = new Intent(context, DetailsActivity.class);
//                            i.putExtra("siteId", "554b7461694d61267e9b0100");
//                            startActivity(i);
//                            break;
//
//                        case Constants.CATEGORY_VIEW_PAGER_CHURCH:
//                            i = new Intent(context, DetailsActivity.class);
//                            i.putExtra("siteId", "554b78f9694d61267ea70100");
//                            startActivity(i);
//                            break;
//
//                        case Constants.CATEGORY_VIEW_PAGER_VILLAS:
//                            i = new Intent(context, DetailsActivity.class);
//                            i.putExtra("siteId", "5550cbaa694d6103fa4c0000");
//                            startActivity(i);
//                            break;
//                    }
//                    break;
//
//                case R.id.cdTouch1:
//                    switch (tabTypeOfSiteSelected) {
//                        case Constants.CATEGORY_VIEW_PAGER_MUSEUM:
//                            i = new Intent(context, DetailsActivity.class);
//                            i.putExtra("siteId", "5551fe27694d6103fa2b0100");
//                            startActivity(i);
//                            break;
//
//                        case Constants.CATEGORY_VIEW_PAGER_CASTLES:
//                            i = new Intent(context, DetailsActivity.class);
//                            i.putExtra("siteId", "5549f352694d61267e880000");
//                            startActivity(i);
//                            break;
//
//                        case Constants.CATEGORY_VIEW_PAGER_CHURCH:
//                            i = new Intent(context, DetailsActivity.class);
//                            i.putExtra("siteId", "554b6d88694d61267e5f0100");
//                            startActivity(i);
//                            break;
//
//                        case Constants.CATEGORY_VIEW_PAGER_VILLAS:
//                            i = new Intent(context, DetailsActivity.class);
//                            i.putExtra("siteId", "5552156f694d6103faa30100");
//                            startActivity(i);
//                            break;
//
//                    }
//                    break;
//
//                case R.id.cdTouch2:
//
//                    switch (tabTypeOfSiteSelected) {
//                        case Constants.CATEGORY_VIEW_PAGER_MUSEUM:
//                            i = new Intent(context, DetailsActivity.class);
//                            i.putExtra("siteId", "55530f60694d6103fac80100");
//                            startActivity(i);
//                            break;
//
//                        case Constants.CATEGORY_VIEW_PAGER_CASTLES:
//                            i = new Intent(context, DetailsActivity.class);
//                            i.putExtra("siteId", "554b3360694d61267ef90000");
//                            startActivity(i);
//                            break;
//
//                        case Constants.CATEGORY_VIEW_PAGER_CHURCH:
//                            i = new Intent(context, DetailsActivity.class);
//                            i.putExtra("siteId", "554b8a6a694d6147f85e0000");
//                            startActivity(i);
//                            break;
//
//                        case Constants.CATEGORY_VIEW_PAGER_VILLAS:
//                            i = new Intent(context, DetailsActivity.class);
//                            i.putExtra("siteId", "55531d6c694d6103faff0100");
//                            startActivity(i);
//                            break;
//
//                    }
//                    break;
//
//                case R.id.cdTouch3:
//                    switch (tabTypeOfSiteSelected) {
//                        case Constants.CATEGORY_VIEW_PAGER_MUSEUM:
//                            i = new Intent(context, DetailsActivity.class);
//                            i.putExtra("siteId", "5551cc00694d6103faf10000");
//                            startActivity(i);
//                            break;
//
//                        case Constants.CATEGORY_VIEW_PAGER_CASTLES:
//                            i = new Intent(context, DetailsActivity.class);
//                            i.putExtra("siteId", "554b3a3e694d61267e020100");
//                            startActivity(i);
//                            break;
//
//                        case Constants.CATEGORY_VIEW_PAGER_CHURCH:
//                            i = new Intent(context, DetailsActivity.class);
//                            i.putExtra("siteId", "555c9def694d6103faf70200");
//                            startActivity(i);
//                            break;
//
//                        case Constants.CATEGORY_VIEW_PAGER_VILLAS:
//                            i = new Intent(context, DetailsActivity.class);
//                            i.putExtra("siteId", "5552208e694d6103fabd0100");
//                            startActivity(i);
//                            break;
//
//                    }
//                    break;
//
//
//                case R.id.cdTouch4:
//
//                    switch (tabTypeOfSiteSelected) {
//                        case Constants.CATEGORY_VIEW_PAGER_MUSEUM:
//                            i = new Intent(context, DetailsActivity.class);
//                            i.putExtra("siteId", "5550be56694d6103fa020000");
//                            startActivity(i);
//                            break;
//
//                        case Constants.CATEGORY_VIEW_PAGER_CASTLES:
//                            i = new Intent(context, DetailsActivity.class);
//                            i.putExtra("siteId", "5549e224694d61267e2b0000");
//                            startActivity(i);
//                            break;
//
//                        case Constants.CATEGORY_VIEW_PAGER_CHURCH:
//                            i = new Intent(context, DetailsActivity.class);
//                            i.putExtra("siteId", "554b8b24694d6147f8670000");
//                            startActivity(i);
//                            break;
//
//                        case Constants.CATEGORY_VIEW_PAGER_VILLAS:
//                            i = new Intent(context, DetailsActivity.class);
//                            i.putExtra("siteId", "554b2c3a694d61267ec90000");
//                            startActivity(i);
//                            break;
//
//                    }
//                    break;
//
//
            }
//
//
        }//fine else su Milano







    }


//    private void fillList(ArrayList<Site> array){
//
//        for (int i=0; i<array.size();i++){
//
//            setCircleList(array.get(i),i);
//            setTitleTextList(array.get(i).name, i);
//            setBodyTextList(array.get(i).address + ", " + array.get(i).addressCivic, i);
//        }
//    }

//    private void setTitleTextList(String s,int position){
//        arrayTextTitleList.get(position).setText(s);
//    }
//
//    private void setBodyTextList(String s,int position){
//        arrayTextBodyList.get(position).setText(s);
//    }
//
//    private void setCircleList(Site site, int position){
//
//        Bitmap bitmap;
//        String imagePath = site.pictureUrl;
//        ImageView imageToSet=arrayCircleList.get(position);
//
//        String currentCity = Repository.retrieve(context, Constants.KEY_CURRENT_CITY, String.class);
//        Log.d("miotag", "currentCity: " + currentCity);
//
//
//        if (currentCity.equals(Constants.CITY_MILAN)) {
//
//            if ((TextUtils.equals(imagePath, "placeholder")) || (placeholderEverywhere)) {
//
//                if (TextUtils.equals(Locale.getDefault().getDisplayLanguage(), "English")) {
//                    convertLanguageTypeOfSite(site);
//                } else {
//                    chooseThemeColors(site);
//                }
//                imagePath = placeholderName;
//                int drawableResource = context.getResources().getIdentifier(imagePath, "drawable", context.getPackageName());
//                imageToSet.setImageResource(drawableResource);
////                imageToSet.setScaleType(ImageView.ScaleType.FIT_XY);
//
//
//            } else {
//                imagePath = imagePath.substring(79, imagePath.length() - 4);
//                imagePath = "milano_images/" + imagePath + ".jpg";
//                bitmap = BitmapFactory.decodeFile(context.getExternalFilesDir(Environment.DIRECTORY_PICTURES) + "/" + imagePath);
//                Drawable mDrawable = new BitmapDrawable(context.getResources(), bitmap);
//                imageToSet.setImageDrawable(mDrawable);
////                imageToSet.setScaleType(ImageView.ScaleType.FIT_XY);
//
//            }
//        } else {
//            if ((TextUtils.equals(imagePath, "placeholder")) || (placeholderEverywhere)){
//
//                if (TextUtils.equals(Locale.getDefault().getDisplayLanguage(), "English")) {
//                    convertLanguageTypeOfSite(site);
//                } else {
//                    chooseThemeColors(site);
//                }
//
//                imagePath = placeholderName;
//                int drawableResource = context.getResources().getIdentifier(imagePath, "drawable", context.getPackageName());
//                imageToSet.setImageResource(drawableResource);
////                imageToSet.setScaleType(ImageView.ScaleType.FIT_XY);
//
//
//            } else {
//
//                if (TextUtils.equals(Locale.getDefault().getDisplayLanguage(), "English")) {
//                    convertLanguageTypeOfSite(site);
//                } else {
//                    chooseThemeColors(site);
//                }
//
//                imagePath = imagePath.substring(79, imagePath.length() - 4);
//                imagePath = "palermo_images/" + imagePath + ".jpg";
//                bitmap = BitmapFactory.decodeFile(context.getExternalFilesDir(Environment.DIRECTORY_PICTURES) + "/" + imagePath);
//                Drawable mDrawable = new BitmapDrawable(context.getResources(), bitmap);
//                imageToSet.setImageDrawable(mDrawable);
////                imageToSet.setScaleType(ImageView.ScaleType.FIT_XY);
//            }
//
//
//        }
//
//
//
//    }



    public static void setListViewHeightBasedOnChildren(ListView listView) {
        ListAdapter listAdapter = listView.getAdapter();
        if (listAdapter == null) {
            // pre-condition
            return;
        }

        int totalHeight = 0;
        for (int i = 0; i < listAdapter.getCount(); i++) {
            View listItem = listAdapter.getView(i, null, listView);
            listItem.measure(0, 0);
            totalHeight += listItem.getMeasuredHeight();
        }

        ViewGroup.LayoutParams params = listView.getLayoutParams();
        params.height = totalHeight + (listView.getDividerHeight() * (listAdapter.getCount() - 1));
        listView.setLayoutParams(params);
        listView.requestLayout();
    }



}//fine classe
