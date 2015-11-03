package org.wepush.open_tour.fragments_dialogs;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import org.wepush.open_tour.R;


public class CityFragment extends Fragment {

    private static final String CITY_NAME = "org.wepush.opentour.fragments.CityName";

    public static CityFragment newInstance(String cityName) {
        CityFragment fragment = new CityFragment();
        Bundle bundle = new Bundle();
        bundle.putString(CITY_NAME, cityName);
        fragment.setArguments(bundle);
        return fragment;
    }



    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_city, null);
       TextView textCity = (TextView) view.findViewById(R.id.txtCityChooserName);
        Bundle bundle = getArguments();
        ImageView imageView=(ImageView)view.findViewById(R.id.cityChoose);
           textCity.setText(bundle.getString(CITY_NAME));

        if(TextUtils.equals(bundle.getString(CITY_NAME),"Milano")){

            imageView.setImageDrawable(getResources().getDrawable(R.drawable.milano));


        }else {
            imageView.setImageDrawable(getResources().getDrawable(R.drawable.palermo2));

        }



        return view;
    }
}
