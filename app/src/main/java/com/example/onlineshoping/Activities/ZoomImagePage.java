package com.example.onlineshoping.Activities;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.util.Log;
import android.view.MotionEvent;
import android.view.ScaleGestureDetector;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.RelativeLayout;

import com.android.volley.toolbox.ImageLoader;
import com.android.volley.toolbox.NetworkImageView;
import com.example.onlineshoping.Fragments.FragmentZoomImage1;
import com.example.onlineshoping.Fragments.FragmentZoomImage2;
import com.example.onlineshoping.Fragments.FragmentZoomImage3;
import com.example.onlineshoping.ModelClasses.CustomVolleyRequest;
import com.example.onlineshoping.R;
import com.google.android.material.tabs.TabItem;
import com.google.android.material.tabs.TabLayout;

public class ZoomImagePage extends AppCompatActivity {

    private ScaleGestureDetector scaleGestureDetector;
    private float mScaleFactor = 1.0f;

    NetworkImageView networkImageView, networkImageView1, networkImageView2, networkImageView3;

    private ImageLoader imageLoader;
    private Context context;

    RelativeLayout relativeLayout1, relativeLayout2, relativeLayout3;

    @SuppressLint("ResourceAsColor")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_zoom_image_page);

        relativeLayout1 = (RelativeLayout)findViewById(R.id.relativeLayout1);
        relativeLayout2 = (RelativeLayout)findViewById(R.id.relativeLayout2);
        relativeLayout3 = (RelativeLayout)findViewById(R.id.relativeLayout3);


        Intent intent = getIntent();

        String image1 = intent.getStringExtra("img1");
        String image2 = intent.getStringExtra("img2");
        String image3 = intent.getStringExtra("img3");
        Log.i("image", "["+image1+"]");
        networkImageView1 = (NetworkImageView)findViewById(R.id.networkImageViewFragmentA);
        networkImageView2 = (NetworkImageView)findViewById(R.id.networkImageViewFragmentB);
        networkImageView3 = (NetworkImageView)findViewById(R.id.networkImageViewFragmentC);

        imageLoader = CustomVolleyRequest.getInstance(context).getImageLoader();
        imageLoader.get(image1, ImageLoader.getImageListener(networkImageView1, R.drawable.ic_launcher_background, android.R.drawable.ic_dialog_alert));
        networkImageView1.setImageUrl(image1,imageLoader);
        networkImageView2.setImageUrl(image2,imageLoader);
        networkImageView3.setImageUrl(image3,imageLoader);

        FragmentZoomImage1 fragmentZoomImage1 = new FragmentZoomImage1();
        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.replace(R.id.frag_container, fragmentZoomImage1);
        fragmentTransaction.commit();
        relativeLayout1.setBackgroundResource(R.drawable.zoom_background);
        networkImageView1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                FragmentZoomImage1 fragmentZoomImage1 = new FragmentZoomImage1();
                FragmentManager fragmentManager = getSupportFragmentManager();
                FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                fragmentTransaction.replace(R.id.frag_container, fragmentZoomImage1);
                fragmentTransaction.commit();

                relativeLayout1.setBackgroundResource(R.drawable.zoom_background);
                relativeLayout2.setBackgroundResource(R.color.white);
                relativeLayout3.setBackgroundResource(R.color.white);

            }
        });

        networkImageView2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                FragmentZoomImage2 fragmentZoomImage2 = new FragmentZoomImage2();
                FragmentManager fragmentManager = getSupportFragmentManager();
                FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                fragmentTransaction.replace(R.id.frag_container, fragmentZoomImage2);
                fragmentTransaction.commit();

                relativeLayout1.setBackgroundResource(R.color.white);
                relativeLayout2.setBackgroundResource(R.drawable.zoom_background);
                relativeLayout3.setBackgroundResource(R.color.white);

            }
        });

        networkImageView3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                FragmentZoomImage3 fragmentZoomImage3 = new FragmentZoomImage3();
                FragmentManager fragmentManager = getSupportFragmentManager();
                FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                fragmentTransaction.replace(R.id.frag_container, fragmentZoomImage3);
                fragmentTransaction.commit();

                relativeLayout1.setBackgroundResource(R.color.white);
                relativeLayout2.setBackgroundResource(R.color.white);
                relativeLayout3.setBackgroundResource(R.drawable.zoom_background);
            }
        });






    }


}