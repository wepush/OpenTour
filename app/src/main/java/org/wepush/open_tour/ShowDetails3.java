//package org.wepush.open_tour;
//
//import android.graphics.Color;
//import android.os.Build;
//import android.os.Bundle;
//import android.support.design.widget.AppBarLayout;
//import android.support.design.widget.CollapsingToolbarLayout;
//import android.support.v7.app.AppCompatActivity;
//import android.view.View;
//
//import com.github.ksoichiro.android.observablescrollview.ObservableScrollView;
//
///**
// * Created by antoniocoppola on 29/09/15.
// */
//public class ShowDetails3 extends AppCompatActivity {
//    private ObservableScrollView mScroll;
//    private AppBarLayout appBarLayout;
//
//    @Override
//    protected void onCreate(Bundle savedInstanceState) {
//        super.onCreate(savedInstanceState);
//        setContentView(R.layout.showdetails3_layout);
//
//        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
//
//
//            getWindow().getDecorView().setSystemUiVisibility(
//                    View.SYSTEM_UI_FLAG_LAYOUT_STABLE
//                            | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN);
//            getWindow().setStatusBarColor(Color.TRANSPARENT);
//
//        }
//
//
//
////        Toolbar toolbar=(Toolbar)findViewById(R.id.toolbar);
////        setSupportActionBar(toolbar);
//
//        CollapsingToolbarLayout collapsingToolbarLayout = (CollapsingToolbarLayout) findViewById(R.id.collapsing_toolbar);
//        collapsingToolbarLayout.setTitle("Ciao");
//        collapsingToolbarLayout.setCollapsedTitleTextColor(getResources().getColor(R.color.white));
//        collapsingToolbarLayout.setExpandedTitleColor(getResources().getColor(android.R.color.transparent));
//        collapsingToolbarLayout.setStatusBarScrimColor(getResources().getColor(R.color.amber600));
//        appBarLayout = (AppBarLayout) findViewById(R.id.appBarLayout);
//        collapsingToolbarLayout.setContentScrimColor(getResources().getColor(R.color.amber400));
//
//
//    }
//}