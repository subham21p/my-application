package com.example.onlineshoping.Fragments;

import android.content.Context;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.ScaleGestureDetector;
import android.view.View;
import android.view.ViewGroup;

import com.android.volley.toolbox.ImageLoader;
import com.android.volley.toolbox.NetworkImageView;
import com.example.onlineshoping.ModelClasses.CustomVolleyRequest;
import com.example.onlineshoping.R;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link FragmentZoomImage3#newInstance} factory method to
 * create an instance of this fragment.
 */
public class FragmentZoomImage3 extends Fragment {

    private ScaleGestureDetector scaleGestureDetector;
    private float mScaleFactor = 1.0f;


    NetworkImageView networkImageView;
    private ImageLoader imageLoader;
    private Context context;

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public FragmentZoomImage3() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment FragmentZoomImage3.
     */
    // TODO: Rename and change types and number of parameters
    public static FragmentZoomImage3 newInstance(String param1, String param2) {
        FragmentZoomImage3 fragment = new FragmentZoomImage3();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_zoom_image3, container, false);


        String image1 = getActivity().getIntent().getExtras().getString("img1");
        String image2 = getActivity().getIntent().getExtras().getString("img2");
        String image3 = getActivity().getIntent().getExtras().getString("img3");

        networkImageView = view.findViewById(R.id.networkImageView3);

        imageLoader = CustomVolleyRequest.getInstance(context).getImageLoader();
        imageLoader.get(image3, ImageLoader.getImageListener(networkImageView, R.drawable.ic_launcher_background, android.R.drawable.ic_dialog_alert));
        networkImageView.setImageUrl(image3,imageLoader);


        //For zoom image
        scaleGestureDetector = new ScaleGestureDetector(getContext(), new ScaleListener());

        view.setOnTouchListener(new View.OnTouchListener() {
            public boolean onTouch(View v, MotionEvent event) {

                if(event.getAction() == MotionEvent.ACTION_MOVE){
                    //do something
                    scaleGestureDetector.onTouchEvent(event);
                }
                return true;
            }
        });

        // Inflate the layout for this fragment
        return view;
    }


    private class ScaleListener extends ScaleGestureDetector.SimpleOnScaleGestureListener {
        @Override
        public boolean onScale(ScaleGestureDetector scaleGestureDetector) {
            mScaleFactor *= scaleGestureDetector.getScaleFactor();
            mScaleFactor = Math.max(1.0f, Math.min(mScaleFactor, 10.0f));
            networkImageView.setScaleX(mScaleFactor);
            networkImageView.setScaleY(mScaleFactor);
            return true;
        }
    }

}