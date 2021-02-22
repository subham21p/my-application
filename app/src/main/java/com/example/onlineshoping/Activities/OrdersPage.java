package com.example.onlineshoping.Activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.os.AsyncTask;
import android.os.Bundle;
import android.text.Html;
import android.util.Log;
import android.view.Gravity;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.toolbox.ImageLoader;
import com.android.volley.toolbox.NetworkImageView;
import com.example.onlineshoping.Adapters.AddCartAdapter;
import com.example.onlineshoping.Adapters.OrdersAdapter;
import com.example.onlineshoping.ModelClasses.AddCartClass;
import com.example.onlineshoping.ModelClasses.CustomVolleyRequest;
import com.example.onlineshoping.ModelClasses.OrdersClass;
import com.example.onlineshoping.ModelClasses.ProfilePic;
import com.example.onlineshoping.ModelClasses.RequestHandler;
import com.example.onlineshoping.ModelClasses.SharedPrefManager;
import com.example.onlineshoping.ModelClasses.User;
import com.example.onlineshoping.R;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.navigation.NavigationView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;

import static com.example.onlineshoping.ModelClasses.Api.URL_SHOWCART;
import static com.example.onlineshoping.ModelClasses.Api.URL_SHOW_ORDERS;

public class OrdersPage extends AppCompatActivity {

    private List<OrdersClass> ordersClasses;
    private RecyclerView recyclerView;
    private RecyclerView.LayoutManager layoutManager;

   // TextView textViewCurrentLoc, textViewName, textViewNoItems;
    TextView textViewCurrentLoc, textViewName,textViewNoItems, textViewAddCartCount, textViewAppName;
    DrawerLayout drawerLayout;
    NavigationView navigationView;
    ImageView imageViewMenu,imageViewSearch, imageViewProfilePic, imageViewAddCart;
    RelativeLayout relativeLayoutAddCart;
    NetworkImageView networkImageView;
    FusedLocationProviderClient fusedLocationProviderClient;
    private ImageLoader imageLoader;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_orders_page);

        if (!SharedPrefManager.getInstance(this).isLoggedIn()) {
            finish();
            startActivity(new Intent(this, LoginPage.class));
        }

        User user = SharedPrefManager.getInstance(this).getUser();

        ProfilePic profilePic = SharedPrefManager.getInstance(this).getProfilePic();


        textViewNoItems = (TextView)findViewById(R.id.textViewNoItemaOP);
        textViewNoItems.setVisibility(View.INVISIBLE);


        recyclerView = (RecyclerView) findViewById(R.id.recyclerViewOP);
        recyclerView.setHasFixedSize(true);
        layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);

        ordersClasses = new ArrayList<>();

        // for order list
        showOrders(user.getEmail());


        drawerLayout = (DrawerLayout) findViewById(R.id.drawerLayoutOP);
        navigationView = (NavigationView) findViewById(R.id.navigationViewOP);
        imageViewMenu = (ImageView) findViewById(R.id.imageViewMenuOP);


        imageViewMenu.setOnClickListener(new View.OnClickListener() {
            @SuppressLint("WrongConstant")
            @Override
            public void onClick(View v) {
                if (!drawerLayout.isDrawerOpen(Gravity.START))
                    drawerLayout.openDrawer(Gravity.START);
                else drawerLayout.closeDrawer(Gravity.END);
            }
        });


        View header = navigationView.getHeaderView(0);
        textViewCurrentLoc = (TextView) header.findViewById(R.id.textViewnvHeaderCurrentLoc);
        textViewName = (TextView)header.findViewById(R.id.textViewUserNameNV);
        imageViewProfilePic = (ImageView)header.findViewById(R.id.imageViewProfilePicNV);
        networkImageView = (NetworkImageView) header.findViewById(R.id.networkImageViewNV);


        if (!profilePic.getProfilePic().isEmpty()) {
            Log.i("network1", "[" + profilePic.getProfilePic() + "]");
            imageLoader = CustomVolleyRequest.getInstance(getApplicationContext()).getImageLoader();
            imageLoader.get(profilePic.getProfilePic(), ImageLoader.getImageListener(networkImageView, R.drawable.ic_launcher_background, android.R.drawable.ic_dialog_alert));
            networkImageView.setImageUrl(profilePic.getProfilePic(), imageLoader);
            imageViewProfilePic.setVisibility(View.INVISIBLE);
        }else {
            Log.i("network2", "[" + profilePic.getProfilePic() + "]");
            imageViewProfilePic.setVisibility(View.VISIBLE);
            networkImageView.setVisibility(View.INVISIBLE);
        }

        textViewName.setText(user.getName());

        networkImageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getApplicationContext(),SettingPage.class));
            }
        });


        // Add Cart number
        nosOfAddCartList(user.getEmail());
        textViewAddCartCount = (TextView)findViewById(R.id.textViewCartCountOP);
        relativeLayoutAddCart = (RelativeLayout)findViewById(R.id.relativeLayoutCartCountOP);

        // For Location
        fusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(this);


        if (ActivityCompat.checkSelfPermission(OrdersPage.this, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
            getLocation();
        } else {
            ActivityCompat.requestPermissions(OrdersPage.this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, 44);
        }


        navigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                int id = item.getItemId();
                switch(id)
                {
                    case R.id.nvAboutUs:
                        Toast.makeText(OrdersPage.this, "About Us", Toast.LENGTH_SHORT).show();
                        drawerLayout.closeDrawers();
                        return true;

                    case R.id.nvCart:
                        startActivity(new Intent(getApplicationContext(),AddCartList.class));
                        drawerLayout.closeDrawers();
                        return true;

                    case R.id.nvOrders:
                        startActivity(new Intent(getApplicationContext(),OrdersPage.class));
                        drawerLayout.closeDrawers();
                        return true;

                    case R.id.nvSetting:
                        startActivity(new Intent(getApplicationContext(),SettingPage.class));
                        drawerLayout.closeDrawers();
                        return true;

                    case R.id.nvLogout:
                        SharedPrefManager.getInstance(getApplicationContext()).logout();
                        Toast.makeText(OrdersPage.this, "Logout", Toast.LENGTH_SHORT).show();
                    default:
                        return true;
                }


            }
        });


        textViewAppName = (TextView)findViewById(R.id.textViewAppNameOP);

        textViewAppName.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getApplicationContext(),HomePage.class));
            }
        });

        imageViewSearch = (ImageView)findViewById(R.id.imageViewSearchOP);

        imageViewSearch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(OrdersPage.this,SearchPage.class));
            }
        });

        // for add cart page

        imageViewAddCart = (ImageView)findViewById(R.id.imageViewCartOP);

        imageViewAddCart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getApplicationContext(),AddCartList.class));
            }
        });



    }


    // for add cart
    private void nosOfAddCartList(final String email) {

        class OnsOfAddCartList extends AsyncTask<Void, Void, String> {

            @Override
            protected void onPreExecute() {
                super.onPreExecute();
            }


            @Override
            protected void onPostExecute(String s) {
                super.onPostExecute(s);

                try {
                    nosOfAddCart(s);

                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }

            @Override
            protected String doInBackground(Void... voids) {
                RequestHandler requestHandler = new RequestHandler();
                HashMap<String, String> params = new HashMap<>();
                params.put("email", email);

                return requestHandler.sendPostRequest(URL_SHOWCART, params);
            }
        }
        OnsOfAddCartList getJSON = new OnsOfAddCartList();
        getJSON.execute();

    }

    private void nosOfAddCart(String json) throws JSONException {

        JSONArray jsonArray = new JSONArray(json);
        String nos = String.valueOf(jsonArray.length());
        textViewAddCartCount.setText(nos);
        Log.i("jasonArray", "[" + jsonArray.length()+ "]");

        if (jsonArray.length() == 0){
            relativeLayoutAddCart.setVisibility(View.INVISIBLE);
        }
        else {
            relativeLayoutAddCart.setVisibility(View.VISIBLE);
        }

    }




    private void showOrders(final String email) {

        /*ProgressBar progressBar = (ProgressBar)findViewById(R.id.progressBarAC);
        progressBar.setVisibility(View.VISIBLE);*/

        class ShowOrders extends AsyncTask<Void, Void, String> {

            @Override
            protected void onPreExecute() {
                super.onPreExecute();
            }


            @Override
            protected void onPostExecute(String s) {
                super.onPostExecute(s);
             //   progressBar.setVisibility(View.INVISIBLE);

                //   Toast.makeText(getApplicationContext(), s, Toast.LENGTH_SHORT).show();
                try {
                    loadIntoListView(s);

                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }

            @Override
            protected String doInBackground(Void... voids) {
                RequestHandler requestHandler = new RequestHandler();
                HashMap<String, String> params = new HashMap<>();
                params.put("email", email);

                return requestHandler.sendPostRequest(URL_SHOW_ORDERS, params);
            }
        }
        ShowOrders getJSON = new ShowOrders();
        getJSON.execute();

    }

    private void loadIntoListView(String json) throws JSONException {
        Log.i("tagconvertstr11111", "[" + json+ "]");

        JSONArray jsonArray = new JSONArray(json);
        if (jsonArray.length() == 0){
            textViewNoItems.setVisibility(View.VISIBLE);
            recyclerView.setVisibility(View.INVISIBLE);
        }
        String[] stocks = new String[jsonArray.length()];

        for (int i = 0; i < jsonArray.length(); i++) {
            JSONObject obj = jsonArray.getJSONObject(i);

            stocks[i] = obj.getString("name");

            Log.i("tagconvertstr2222", "[" + String.valueOf(stocks) + "]");


            ordersClasses.add(new OrdersClass(
                    obj.getString("image"),
                    obj.getInt("items_id"),
                    obj.getInt("nos_of_products"),
                    obj.getString("category"),
                    obj.getString("name"),
                    obj.getString("brand"),
                    obj.getString("order_date"),
                    obj.getString("delivery_date"),
                    obj.getString("quantity"),
                    obj.getInt("price")

            ));
        }


        OrdersAdapter adapter = new OrdersAdapter(ordersClasses, OrdersPage.this);
        recyclerView.setAdapter(adapter);


    }


    @SuppressLint("MissingPermission")
    private void getLocation() {


        fusedLocationProviderClient.getLastLocation().addOnCompleteListener(new OnCompleteListener<Location>() {
            @Override
            public void onComplete(@NonNull Task<Location> task) {

                Location location = task.getResult();

                if (location != null) {


                    try {
                        Geocoder geocoder = new Geocoder(OrdersPage.this, Locale.getDefault());

                        List<Address> addresses = geocoder.getFromLocation(location.getLatitude(), location.getLongitude(), 1);
                        textViewCurrentLoc.setText(Html.fromHtml("<font color='#000000'></font>" + addresses.get(0).getLocality() + ", " + addresses.get(0).getPostalCode()));
                        // For full address   textView.setText(addresses.get(0).getAddressLine(0));
                        Log.i("location", "[" + addresses.get(0).getAddressLine(0) + "]");
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }
        });

    }







}