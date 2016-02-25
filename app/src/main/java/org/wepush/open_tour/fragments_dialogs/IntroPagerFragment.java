package org.wepush.open_tour.fragments_dialogs;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.TextView;

import org.wepush.open_tour.R;

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

}
