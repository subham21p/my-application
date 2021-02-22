package com.example.onlineshoping.Adapters;

import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Build;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.RatingBar;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.ImageLoader;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.NetworkImageView;
import com.android.volley.toolbox.Volley;
import com.example.onlineshoping.Activities.AddCartList;
import com.example.onlineshoping.Activities.HomePage;
import com.example.onlineshoping.Activities.ProductDetailPage;
import com.example.onlineshoping.ModelClasses.AddCartClass;
import com.example.onlineshoping.ModelClasses.Config;
import com.example.onlineshoping.ModelClasses.CustomVolleyRequest;
import com.example.onlineshoping.ModelClasses.RequestHandler;
import com.example.onlineshoping.ModelClasses.SharedPrefManager;
import com.example.onlineshoping.ModelClasses.SuperCar;
import com.example.onlineshoping.ModelClasses.User;
import com.example.onlineshoping.R;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Random;

import static com.example.onlineshoping.ModelClasses.Api.URL_DELETE_ADDCART;
import static com.example.onlineshoping.ModelClasses.Api.URL_LOGIN;
import static com.example.onlineshoping.ModelClasses.Api.URL_UPDATE_NOS_OF_PRODUCTS;
import static com.example.onlineshoping.ModelClasses.Config.DATA_URL;
import static com.example.onlineshoping.ModelClasses.Config.DATA_URL_CLOTHES;
import static com.example.onlineshoping.ModelClasses.Config.DATA_URL_COSMETICS;
import static com.example.onlineshoping.ModelClasses.Config.DATA_URL_ELECTRONIC;
import static com.example.onlineshoping.ModelClasses.Config.DATA_URL_FOOTWEAR;
import static com.example.onlineshoping.ModelClasses.Config.DATA_URL_GROCERY;
import static com.example.onlineshoping.ModelClasses.Config.DATA_URL_JEWELLERY;
import static com.example.onlineshoping.ModelClasses.Config.DATA_URL_TOYS;
import static java.security.AccessController.getContext;

public class AddCartAdapter extends RecyclerView.Adapter<AddCartAdapter.ItemViewHolder> {

    //  int a = 1;
    //Imageloader to load image
    private ImageLoader imageLoader;
    private Context context;

            //List to store all superheroes
            List<AddCartClass> superCars;

    //Constructor of this class
    public AddCartAdapter(List<AddCartClass> superCars, Context context){
            super();
            //Getting all superheroes
            this.superCars = superCars;
            this.context = context;
            }

    @Override
    public ItemViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

            View v = LayoutInflater.from(parent.getContext())
            .inflate(R.layout.add_cart_list, parent, false);
            ItemViewHolder viewHolder = new ItemViewHolder(v);
            return viewHolder;
            }

    @Override
    public void onBindViewHolder(@NonNull ItemViewHolder holder, int position) {

        AddCartClass superCar = superCars.get(position);
        final int[] a = {1};
        //Loading image from url
        imageLoader = CustomVolleyRequest.getInstance(context).getImageLoader();
        imageLoader.get(superCar.getImageUrl(), ImageLoader.getImageListener(((ItemViewHolder) holder).imageView1, R.drawable.ic_launcher_background, android.R.drawable.ic_dialog_alert));

        //Showing data on the views
        ((ItemViewHolder) holder).imageView1.setImageUrl(superCar.getImageUrl(), imageLoader);
        ((ItemViewHolder) holder).textViewName.setText(superCar.getName());
        ((ItemViewHolder) holder).textViewBrand.setText(superCar.getBrand());
        ((ItemViewHolder) holder).textViewQuantity.setText(superCar.getQuantity());
        ((ItemViewHolder) holder).textViewPrice.setText(String.valueOf(superCar.getPrice()));
        ((ItemViewHolder) holder).ratingBar.setRating((float) superCar.getRating());

        User user = SharedPrefManager.getInstance(context).getUser();

        ((ItemViewHolder) holder).relativeLayoutPluse.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                a[0]++;
                if (a[0] > 9) {
                    ((ItemViewHolder) holder).relativeLayoutPluse.setEnabled(false);
                }
                String b = String.valueOf(a[0]);
                ((ItemViewHolder) holder).textViewQuantityCount.setText(b);
                updateNosOfProducts(user.getEmail(), superCar.getItem_id(), a[0]);
                ((ItemViewHolder) holder).relativeLayoutMinus.setEnabled(true);
            }
        });

        ((ItemViewHolder) holder).relativeLayoutMinus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (a[0] < 2) {
                    ((ItemViewHolder) holder).relativeLayoutMinus.setEnabled(false);
                }
                String b = String.valueOf(a[0]);
                ((ItemViewHolder) holder).textViewQuantityCount.setText(b);
                updateNosOfProducts(user.getEmail(), superCar.getItem_id(), a[0]);
                ((ItemViewHolder) holder).relativeLayoutPluse.setEnabled(true);
                a[0]--;

            }
        });

        Log.i("image2", "[" + superCar.getImageUrl() + "]");
        ((ItemViewHolder) holder).imageView1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(AddCartAdapter.this.context, ProductDetailPage.class);
                intent.putExtra("category", superCar.getCategory().trim());
                intent.putExtra("brand", superCar.getBrand().trim());
                intent.putExtra("itemId", superCar.getItem_id());
                intent.putExtra("name", ((ItemViewHolder) holder).textViewName.getText().toString());
                context.startActivity(intent);
            }
        });

        ((ItemViewHolder) holder).buttonDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                AlertDialog.Builder alertDialog = new AlertDialog.Builder(context);
                alertDialog.setTitle(R.string.app_name);
                alertDialog.setMessage("Do you want to delete " + ((ItemViewHolder) holder).textViewBrand.getText().toString().trim() + " " + ((ItemViewHolder) holder).textViewName.getText().toString().trim() + " from Cart ?");
                //alertDialog.setCancelable(false);
                alertDialog.setPositiveButton("YES", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                        deteleCart(user.getEmail(), superCar.getItem_id());

                        superCars.remove(position);
                        notifyDataSetChanged();
                        dialog.cancel();
                        ((Activity) context).finish();
                        context.startActivity(new Intent(context, AddCartList.class));
                    }
                });
                alertDialog.setNegativeButton("NO", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                        notifyDataSetChanged();

                        dialog.cancel();

                    }
                });

                AlertDialog dialog = alertDialog.create();
                dialog.show();


            }
        });

    }

    @Override
    public int getItemCount() {
            return superCars.size() ;
            }



    class ItemViewHolder extends RecyclerView.ViewHolder{
        //Views
        public NetworkImageView imageView1, imageView2, imageView3;
        public TextView textViewName,textViewBrand,textViewQuantity,textViewPrice, textViewQuantityCount;
        public RatingBar ratingBar;
        Button buttonBuy, buttonDelete;
        RelativeLayout relativeLayoutPluse, relativeLayoutMinus;

        //Initializing Views
        public ItemViewHolder(View itemView) {
            super(itemView);
            imageView1 = (NetworkImageView) itemView.findViewById(R.id.networkImageViewACL);
            textViewName = (TextView) itemView.findViewById(R.id.textViewNameACL);
            textViewBrand = (TextView) itemView.findViewById(R.id.textViewBrandACL);
            textViewQuantity = (TextView) itemView.findViewById(R.id.textViewQuantityACL);
            textViewPrice = (TextView) itemView.findViewById(R.id.textViewPriceACL);
            ratingBar = (RatingBar)itemView.findViewById(R.id.ratingBarACL);
          //  buttonBuy = (Button)itemView.findViewById(R.id.buttonBuyACL);
            buttonDelete = (Button)itemView.findViewById(R.id.buttonDelectACL);
            relativeLayoutPluse = (RelativeLayout)itemView.findViewById(R.id.relativeLayoutPlusACL);
            relativeLayoutMinus = (RelativeLayout)itemView.findViewById(R.id.relativeLayoutMinusACL);
            textViewQuantityCount = (TextView)itemView.findViewById(R.id.textViewCountACL);

        }
     }



        private void deteleCart(String email, int items_id) {

            //if everything is fine

            class DeleteCart extends AsyncTask<Void, Void, String> {

                // ProgressBar progressBar;


                @Override
                protected void onPreExecute() {
                    super.onPreExecute();

                }

                @Override
                protected void onPostExecute(String s) {
                    super.onPostExecute(s);

                    try {
                        //converting response to json object
                        JSONObject obj = new JSONObject(s);

                        //if no error in response
                        if (!obj.getBoolean("error")) {
                            Toast.makeText(context, obj.getString("message"), Toast.LENGTH_SHORT).show();


                        } else {
                           // progressDialog.dismiss();
                            Toast.makeText(context, obj.getString("message"), Toast.LENGTH_SHORT).show();
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }

                @Override
                protected String doInBackground(Void... voids) {
                    //creating request handler object
                    RequestHandler requestHandler = new RequestHandler();

                    //creating request parameters
                    HashMap<String, String> params = new HashMap<>();
                    params.put("email", email);
                    params.put("items_id", String.valueOf(items_id));

                    //returing the response
                    return requestHandler.sendPostRequest(URL_DELETE_ADDCART, params);
                }
            }

            DeleteCart ul = new DeleteCart();
            ul.execute();
        }



        private void updateNosOfProducts(String email, int items_id, int nos_of_products) {


            //if everything is fine

            class UpdateNosOfProducts extends AsyncTask<Void, Void, String> {

                // ProgressBar progressBar;


                @Override
                protected void onPreExecute() {
                    super.onPreExecute();

                }

                @Override
                protected void onPostExecute(String s) {
                    super.onPostExecute(s);
                    //       progressBar.setVisibility(View.GONE);


                    try {
                        //converting response to json object
                        JSONObject obj = new JSONObject(s);

                        //if no error in response
                        if (!obj.getBoolean("error")) {

                        } else {

                            Toast.makeText(context, "Invalid username or password", Toast.LENGTH_SHORT).show();
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }

                @Override
                protected String doInBackground(Void... voids) {
                    //creating request handler object
                    RequestHandler requestHandler = new RequestHandler();

                    //creating request parameters
                    HashMap<String, String> params = new HashMap<>();
                    params.put("email", email);
                    params.put("items_id", String.valueOf(items_id));
                    params.put("nos_of_products", String.valueOf(nos_of_products));

                    //returing the response
                    return requestHandler.sendPostRequest(URL_UPDATE_NOS_OF_PRODUCTS, params);
                }
            }

            UpdateNosOfProducts ul = new UpdateNosOfProducts();
            ul.execute();
        }





}
