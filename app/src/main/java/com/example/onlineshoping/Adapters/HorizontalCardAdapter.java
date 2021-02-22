package com.example.onlineshoping.Adapters;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RatingBar;
import android.widget.TextView;

import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.android.volley.toolbox.ImageLoader;
import com.android.volley.toolbox.NetworkImageView;
import com.example.onlineshoping.Activities.ProductDetailPage;
import com.example.onlineshoping.ModelClasses.CustomVolleyRequest;
import com.example.onlineshoping.ModelClasses.SuperCar;
import com.example.onlineshoping.R;

import java.util.List;

public class HorizontalCardAdapter extends RecyclerView.Adapter<HorizontalCardAdapter.ViewHolder> {


    //Imageloader to load image
    private ImageLoader imageLoader;
    private Context context;

    //List to store all superheroes
    List<SuperCar> superCars;

    //Constructor of this class
    public HorizontalCardAdapter(List<SuperCar> superCars, Context context){
        super();
        //Getting all superheroes
        this.superCars = superCars;
        this.context = context;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.horizontal_recyclerview_list, parent, false);
       ViewHolder viewHolder = new ViewHolder(v);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {

        //Getting the particular item from the list
        SuperCar superCar =  superCars.get(position);

        //Loading image from url
        imageLoader = CustomVolleyRequest.getInstance(context).getImageLoader();
        imageLoader.get(superCar.getImageUrl(), ImageLoader.getImageListener(holder.imageView1, R.drawable.ic_launcher_background, android.R.drawable.ic_dialog_alert));

        //Showing data on the views
        holder.imageView1.setImageUrl(superCar.getImageUrl(), imageLoader);

        holder.textViewName.setText(superCar.getName());
        holder.textViewBrand.setText(superCar.getBrand());
        holder.textViewQuantity.setText(superCar.getQuantity());
        holder.textViewPrice.setText(String.valueOf(superCar.getPrice()));
        holder.ratingBar.setRating(superCar.getRating());


        Log.i("image2", "["+superCar.getImageUrl()+"]");
        holder.cardView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(HorizontalCardAdapter.this.context, ProductDetailPage.class);
                intent.putExtra("image",superCar.getImageUrl().trim());
                intent .putExtra("itemId", superCar.getId());
                intent .putExtra("category", superCar.getCategory().trim());
                intent .putExtra("brand", superCar.getBrand().trim());
                intent.putExtra("name", holder.textViewName.getText().toString());
                intent .putExtra("quantity", superCar.getQuantity().trim());
                intent .putExtra("price", superCar.getPrice());
                intent .putExtra("rating", superCar.getRating());
                context.startActivity(intent);
            }
        });

    }

    @Override
    public int getItemCount() {
        return superCars.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder{
        //Views
        public NetworkImageView imageView1, imageView2, imageView3;
        public TextView textViewName,textViewBrand,textViewQuantity,textViewPrice;
        public RatingBar ratingBar;
        // public TextView textViewDescription;
        private CardView cardView;
        //Initializing Views
        public ViewHolder(View itemView) {
            super(itemView);
            imageView1 = (NetworkImageView) itemView.findViewById(R.id.imageViewCar1);
            //  imageView2 = (NetworkImageView) itemView.findViewById(R.id.imageViewCar2);
            //  imageView3 = (NetworkImageView) itemView.findViewById(R.id.imageViewCar3);
            textViewName = (TextView) itemView.findViewById(R.id.textViewName);
            textViewBrand = (TextView) itemView.findViewById(R.id.textViewBrand);
            textViewQuantity = (TextView) itemView.findViewById(R.id.textViewQuantity);
            textViewPrice = (TextView) itemView.findViewById(R.id.textViewPrice);
            ratingBar = (RatingBar)itemView.findViewById(R.id.ratingBar);
            cardView = (CardView)itemView.findViewById(R.id.cardView);
        }
    }
}
