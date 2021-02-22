package com.example.onlineshoping.Adapters;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.android.volley.toolbox.ImageLoader;
import com.android.volley.toolbox.NetworkImageView;
import com.example.onlineshoping.Activities.ProductDetailPage;
import com.example.onlineshoping.Activities.ReviewPage;
import com.example.onlineshoping.ModelClasses.CustomVolleyRequest;
import com.example.onlineshoping.ModelClasses.OrdersClass;
import com.example.onlineshoping.ModelClasses.RequestHandler;
import com.example.onlineshoping.ModelClasses.SharedPrefManager;
import com.example.onlineshoping.ModelClasses.User;
import com.example.onlineshoping.R;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

import static com.example.onlineshoping.ModelClasses.Api.URL_CHECK_COMMENT;
import static com.example.onlineshoping.ModelClasses.Api.URL_SHOWCART;

public class OrdersAdapter extends RecyclerView.Adapter<OrdersAdapter.ViewHolder> {


    private static final int TYPE_HEADER = 0;
    private static final int TYPE_FOOTER = 1;
    private static final int TYPE_ITEM = 2;


    //  int a = 1;
//Imageloader to load image
    private ImageLoader imageLoader;
    private Context context;
    int b = 0;

    //List to store all superheroes
    List<OrdersClass> ordersClasses;

    //Constructor of this class
    public OrdersAdapter(List<OrdersClass> ordersClasses, Context context){
        super();
        //Getting all superheroes
        this.ordersClasses = ordersClasses;
        this.context = context;
    }

    @Override
    public OrdersAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.orders_list, parent, false);
        OrdersAdapter.ViewHolder viewHolder = new OrdersAdapter.ViewHolder(v);
        return viewHolder;
    }

    @SuppressLint("ResourceAsColor")
    @Override
    public void onBindViewHolder(OrdersAdapter.ViewHolder holder, int position) {

        //Getting the particular item from the list
        OrdersClass superCar =  ordersClasses.get(position);
        final int[] a = {0};
        //Loading image from url
        imageLoader = CustomVolleyRequest.getInstance(context).getImageLoader();
        imageLoader.get(superCar.getImage(), ImageLoader.getImageListener(holder.imageView1, R.drawable.ic_launcher_background, android.R.drawable.ic_dialog_alert));

        //Showing data on the views
        holder.imageView1.setImageUrl(superCar.getImage(), imageLoader);
        holder.textViewQuantityCount.setText(String.valueOf(superCar.getNos_of_products()));
        holder.textViewName.setText(superCar.getName());
        holder.textViewBrand.setText(superCar.getBrand());
        holder.textViewQuantity.setText(superCar.getQuantity());
        holder.textViewDeliveryDate.setText(superCar.getDelivery_date());
        holder.textViewPrice.setText(String.valueOf(superCar.getPrice()));
      //  holder.ratingBar.setRating((float) superCar.getRating());

        // To check the delivery date and show the product is delivered
        String dtStart = superCar.getDelivery_date();
        SimpleDateFormat format = new SimpleDateFormat("dd/MM/yyyy");

        Date strDate = null;
        try {
            strDate = format.parse(dtStart);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        if (new Date().after(strDate)) {
           holder.textViewDelivery.setText(R.string.deliveredOn);
           holder.textViewDeliveryDate.setTextColor(R.color.green);
           holder.textViewDelivery.setTextColor(R.color.green);
        }

        User user = SharedPrefManager.getInstance(context).getUser();



        Log.i("image2", "["+superCar.getImage()+"]");
        holder.imageView1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(OrdersAdapter.this.context, ProductDetailPage.class);
                intent .putExtra("category", superCar.getCategory().trim());
                intent .putExtra("brand", superCar.getBrand().trim());
                intent .putExtra("itemId", superCar.getItem_id());
                intent.putExtra("name", holder.textViewName.getText().toString());
                context.startActivity(intent);
            }
        });



        holder.buttonReview.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent = new Intent(OrdersAdapter.this.context, ReviewPage.class);
                intent .putExtra("itemId", superCar.getItem_id());
                context.startActivity(intent);

            }
        });

    }

    @Override
    public int getItemCount() {
        return ordersClasses.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder{
        //Views
        public NetworkImageView imageView1, imageView2, imageView3;
        public TextView textViewName,textViewBrand,textViewQuantity,textViewPrice, textViewQuantityCount, textViewDelivery, textViewDeliveryDate;

        Button buttonReview, buttonDelete;
     //   RelativeLayout relativeLayoutPluse, relativeLayoutMinus;

        //Initializing Views
        public ViewHolder(View itemView) {
            super(itemView);
            imageView1 = (NetworkImageView) itemView.findViewById(R.id.networkImageViewOL);
            textViewName = (TextView) itemView.findViewById(R.id.textViewNameOL);
            textViewBrand = (TextView) itemView.findViewById(R.id.textViewBrandOL);
            textViewQuantity = (TextView) itemView.findViewById(R.id.textViewQuantityOL);
            textViewPrice = (TextView) itemView.findViewById(R.id.textViewTotalPriceOL);

            buttonReview = (Button)itemView.findViewById(R.id.buttonAddReviewOL);
           // buttonDelete = (Button)itemView.findViewById(R.id.buttonDelectACL);
            textViewDelivery = (TextView) itemView.findViewById(R.id.textViewDeliveryOL);
            textViewDeliveryDate = (TextView) itemView.findViewById(R.id.textViewDeliveryDateOL);
            textViewQuantityCount = (TextView)itemView.findViewById(R.id.textViewNosOfProductOL);

        }
    }








}
