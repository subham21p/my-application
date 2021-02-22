package com.example.onlineshoping.Adapters;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.annotation.Nullable;
import androidx.viewpager.widget.PagerAdapter;
import androidx.viewpager.widget.ViewPager;

import com.android.volley.toolbox.NetworkImageView;
import com.example.onlineshoping.Activities.ProductDetailPage;
import com.example.onlineshoping.Activities.ZoomImagePage;
import com.example.onlineshoping.ModelClasses.CustomVolleyRequest;
import com.example.onlineshoping.R;

import java.util.Map;
import java.util.Set;

import static android.os.ParcelFileDescriptor.MODE_APPEND;

public class ViewPagerAdapter extends PagerAdapter {

    private Context context;
    private LayoutInflater layoutInflater;
    private Integer [] images;


    private String [] img ;

    public ViewPagerAdapter(Context context, String[] image) {
        this.context = context;
        this.img = image;
    }

    @Override
    public int getCount() {
        return img.length;
    }

    @Override
    public boolean isViewFromObject(View view, Object object) {
        return view == object;
    }

    @Override
    public Object instantiateItem(ViewGroup container, final int position) {


        layoutInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View view = layoutInflater.inflate(R.layout.custom_layout, null);
        NetworkImageView imageView = (NetworkImageView) view.findViewById(R.id.imageView);
        imageView.setImageUrl(img[position], CustomVolleyRequest.getInstance(context).getImageLoader());

        ViewPager vp = (ViewPager) container;
        vp.addView(view, 0);
        return view;

    }



    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {

        ViewPager vp = (ViewPager) container;
        View view = (View) object;
        vp.removeView(view);

    }

}
