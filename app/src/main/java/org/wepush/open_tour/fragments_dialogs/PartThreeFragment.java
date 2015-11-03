//package org.wepush.open_tour.fragments_dialogs;
//
//import android.os.Bundle;
//import android.support.annotation.Nullable;
//import android.support.v4.app.Fragment;
//import android.support.v7.widget.LinearLayoutManager;
//import android.support.v7.widget.RecyclerView;
//import android.view.LayoutInflater;
//import android.view.View;
//import android.view.ViewGroup;
//
//import org.wepush.open_tour.R;
//import org.wepush.open_tour.utils.RecyclerAdapter;
//
//import java.util.ArrayList;
//import java.util.List;
//
///**
// * Created by antoniocoppola on 27/10/15.
// */
//public class PartThreeFragment extends Fragment {
//
//
//    public final static String ITEMS_COUNT_KEY = "PartThreeFragment$ItemsCount";
//
//    public static PartThreeFragment createInstance(int itemsCount) {
//        PartThreeFragment partThreeFragment = new PartThreeFragment();
//        Bundle bundle = new Bundle();
//        bundle.putInt(ITEMS_COUNT_KEY, itemsCount);
//        partThreeFragment.setArguments(bundle);
//        return partThreeFragment;
//    }
//
//    @Nullable
//    @Override
//    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
//        RecyclerView recyclerView = (RecyclerView) inflater.inflate(
//                R.layout.fragment_part_three, container, false);
//        setupRecyclerView(recyclerView);
//        return recyclerView;
//    }
//
//    private void setupRecyclerView(RecyclerView recyclerView) {
//        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
//        RecyclerAdapter recyclerAdapter = new RecyclerAdapter(createItemList());
//        recyclerView.setAdapter(recyclerAdapter);
//    }
//
//    private List<String> createItemList() {
//        List<String> itemList = new ArrayList<>();
//        Bundle bundle = getArguments();
//        if(bundle!=null) {
//            int itemsCount = bundle.getInt(ITEMS_COUNT_KEY);
//            for (int i = 0; i < itemsCount; i++) {
//                itemList.add("Item " + i);
//            }
//        }
//        return itemList;
//    }
//}
//
//
