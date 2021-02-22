package com.example.onlineshoping.Adapters;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.RatingBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.android.volley.toolbox.ImageLoader;
import com.android.volley.toolbox.NetworkImageView;
import com.example.onlineshoping.Activities.ProductDetailPage;
import com.example.onlineshoping.ModelClasses.CustomVolleyRequest;
import com.example.onlineshoping.ModelClasses.SuperCar;
import com.example.onlineshoping.R;

import java.util.List;

public class PagingAdapter extends RecyclerView.Adapter<PagingAdapter.PagingAdapterViewHolder>{

    private ImageLoader imageLoader;
    private List<SuperCar> superCars;
    private Context context;

    public PagingAdapter(List<SuperCar> superCars, Context context) {
        this.superCars = superCars;
        this.context = context;
    }

    @NonNull
    @Override
    public PagingAdapterViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {


        View view  = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.recyclerview_list, parent, false);
        return new PagingAdapter.PagingAdapterViewHolder(view);

    }

    @Override
    public void onBindViewHolder(@NonNull PagingAdapterViewHolder holder, int position) {

        SuperCar superCar =  superCars.get(position);

        //Loading image from url
        imageLoader = CustomVolleyRequest.getInstance(context).getImageLoader();
        imageLoader.get(superCar.getImageUrl(), ImageLoader.getImageListener(holder.imageView, R.drawable.ic_launcher_background, android.R.drawable.ic_dialog_alert));

        //Showing data on the views
        holder.imageView.setImageUrl(superCar.getImageUrl(), imageLoader);
        holder.textViewName.setText(superCar.getName());
        holder.textViewBrand.setText(superCar.getBrand());
        holder.textViewQuantity.setText(superCar.getQuantity());
        holder.textViewPrice.setText((int) superCar.getPrice());
        holder.ratingBar.setRating(superCar.getRating());

        holder.cardView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(PagingAdapter.this.context, ProductDetailPage.class);
               /* intent .putExtra("image", holder.imageView.toString());
                intent.putExtra("name", holder.textViewName.getText().toString());
                intent.putExtra("des", holder.textViewDescription.getText().toString());*/
                context.startActivity(intent);
            }
        });

    }

    @Override
    public int getItemCount() {
        return superCars.size();
    }

    public class PagingAdapterViewHolder extends RecyclerView.ViewHolder {

        public NetworkImageView imageView;
        public TextView textViewName,textViewBrand,textViewQuantity,textViewPrice;
        public RatingBar ratingBar;
        private CardView cardView;
        private LinearLayout linearLayout;

        public PagingAdapterViewHolder(@NonNull View itemView) {
            super(itemView);

            imageView = (NetworkImageView) itemView.findViewById(R.id.imageViewCar1);
            textViewName = (TextView) itemView.findViewById(R.id.textViewName);
            textViewBrand = (TextView) itemView.findViewById(R.id.textViewBrand);
            textViewQuantity = (TextView) itemView.findViewById(R.id.textViewQuantity);
            textViewPrice = (TextView) itemView.findViewById(R.id.textViewPrice);
            ratingBar = (RatingBar)itemView.findViewById(R.id.ratingBar);
            cardView = (CardView)itemView.findViewById(R.id.cardView);

        }
    }
}
