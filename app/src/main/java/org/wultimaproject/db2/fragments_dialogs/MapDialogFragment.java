package org.wultimaproject.db2.fragments_dialogs;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.location.Location;
import android.location.LocationManager;
import android.os.Bundle;
import android.os.Handler;
import android.os.ResultReceiver;
import android.support.v4.app.DialogFragment;
import android.util.Log;
import android.view.View;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.mapbox.mapboxsdk.api.ILatLng;
import com.mapbox.mapboxsdk.geometry.LatLng;
import com.mapbox.mapboxsdk.overlay.Marker;
import com.mapbox.mapboxsdk.overlay.UserLocationOverlay;
import com.mapbox.mapboxsdk.views.MapView;
import com.mapbox.mapboxsdk.views.MapViewListener;

import org.wultimaproject.db2.R;
import org.wultimaproject.db2.SettingTourActivity;
import org.wultimaproject.db2.services.LookUpIntentService;
import org.wultimaproject.db2.structures.Constants;
import org.wultimaproject.db2.utils.Repository;

import java.lang.reflect.Type;
import java.util.Calendar;

/**
 * Created by antoniocoppola on 30/06/15.
 */
public class MapDialogFragment extends DialogFragment {

    private static MapView mv;
    private Marker mark;
    private UserLocationOverlay myLocationOverlay;
    private String currentMap = null;
    private Type type;

    @Override
        public Dialog onCreateDialog(Bundle savedInstanceState) {

            AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
            View view = getActivity().getLayoutInflater().inflate(R.layout.dialog_map, null);

            builder.setView(view);
            builder.setPositiveButton(R.string.confirm, new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialog, int id) {


                    //the lookup request for marker's coordinates on maps doesn't happen inside GeoMarker- too annoyin
                    //what is done is to instantiate a new Location object beefed up with user settings


                    Location mDialogLocation = new Location(LocationManager.GPS_PROVIDER);


                    LatLng thisLatLng = new LatLng
                            (
                                    Double.valueOf(Repository.retrieve(getActivity(), Constants.LATITUDE_STARTING_POINT, String.class)),
                                    Double.valueOf(Repository.retrieve(getActivity(), Constants.LONGITUDE_STARTING_POINT, String.class))
                            );

                    mDialogLocation.setLatitude(thisLatLng.getLatitude());
                    mDialogLocation.setLongitude(thisLatLng.getLongitude());

                    Log.d("miotag", "coordinates from dialog: " + mDialogLocation.getLatitude() + ", " + mDialogLocation.getLongitude());
                    Intent intent = new Intent(getActivity(), LookUpIntentService.class);
                    ResultReceiver mResultReceiver = new SettingTourActivity.AddressResultReceiver(new Handler());
                    intent.putExtra(Constants.RECEIVER, mResultReceiver);
                    intent.putExtra(Constants.LOCATION_DATA_EXTRA, mDialogLocation);
                    getActivity().startService(intent);
//                    }

                }
            });
            builder.setNegativeButton(R.string.cancel, new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialog, int id) {
                    dismiss();
                }
            });


        LatLng thisLatLng=new LatLng(Double.valueOf(Repository.retrieve(getActivity(),Constants.LATITUDE_STARTING_POINT,String.class)),
                                Double.valueOf(Repository.retrieve(getActivity(),Constants.LONGITUDE_STARTING_POINT,String.class)));

        mark=new Marker(getResources().getString(R.string.im_here_marker), getResources().getString(R.string.description_marker),thisLatLng);
        mark.setMarker(getResources().getDrawable(R.drawable.pin_blue));
        mv = (MapView) view.findViewById(R.id.mapview);
        mv.setMinZoomLevel(mv.getTileProvider().getMinimumZoomLevel());
        mv.setMaxZoomLevel(mv.getTileProvider().getMaximumZoomLevel());
        mv.setCenter(thisLatLng);
        mv.setZoom(17);
        mv.addMarker(mark);
//        mv.setUserLocationEnabled(true);

        mv.setMapViewListener(new MapViewListener() {
            @Override
            public void onShowMarker(MapView mapView, Marker marker) {
                Log.d("miotag", "onShowMarker");

            }

            @Override
            public void onHideMarker(MapView mapView, Marker marker) {

                Log.d("miotag", "onHideMarker");


            }

            @Override
            public void onTapMarker(MapView mapView, Marker marker) {
                Log.d("miotag", "onTapMarker");
            }

            @Override
            public void onLongPressMarker(MapView mapView, Marker marker) {
                Log.d("miotag", "Coordinate: " + marker.getPosition().getLatitude() + ", " + marker.getPosition().getLongitude());
            }

            @Override
            public void onTapMap(MapView mapView, ILatLng iLatLng) {

            }

            @Override
            public void onLongPressMap(MapView mapView, ILatLng iLatLng) {

                LatLng lt = ((LatLng) iLatLng);
                try {
                    mark.closeToolTip();
                } catch (NullPointerException e){
                    e.printStackTrace();
                }
                mark=new Marker("I'm here","Description",lt);
                mark.setMarker(getResources().getDrawable(R.drawable.pin_blue));
                mv.clearMarkerFocus();
                mv.clear();
                mv.addMarker(mark);





                Repository.save(getActivity(), Constants.LATITUDE_STARTING_POINT, String.valueOf(lt.getLatitude()));
                Repository.save(getActivity(),Constants.LONGITUDE_STARTING_POINT,String.valueOf(lt.getLongitude()));


            }
        });





        return builder.create();
    }




}