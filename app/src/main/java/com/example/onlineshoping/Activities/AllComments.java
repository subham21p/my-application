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
import com.example.onlineshoping.Adapters.CommentAdapter;
import com.example.onlineshoping.ModelClasses.CommentsClass;
import com.example.onlineshoping.ModelClasses.CustomVolleyRequest;
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

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;

import static com.example.onlineshoping.ModelClasses.Api.URL_GET_COMMENTS;
import static com.example.onlineshoping.ModelClasses.Api.URL_SHOWCART;

public class AllComments extends AppCompatActivity {

    TextView textViewCurrentLoc, textViewName,textViewAppName, textViewAddCartCount;
    DrawerLayout drawerLayout;
    NavigationView navigationView;
    ImageView imageViewMenu,imageViewSearch, imageViewProfilePic, imageViewAddCart;
    RelativeLayout relativeLayoutAddCart;
    NetworkImageView networkImageView;
    FusedLocationProviderClient fusedLocationProviderClient;
    private ImageLoader imageLoader;

    private RecyclerView recyclerView;
    private List<CommentsClass> commentsClasses;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_all_comments);

        if (!SharedPrefManager.getInstance(this).isLoggedIn()) {
            finish();
            startActivity(new Intent(this, LoginPage.class));
        }


        User user = SharedPrefManager.getInstance(this).getUser();

        ProfilePic profilePic = SharedPrefManager.getInstance(this).getProfilePic();


        //For Search Items List
        recyclerView = findViewById(R.id.recyclerViewAllComments);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(AllComments.this));

        commentsClasses = new ArrayList<>();


        // get all comments
        getComments();



        drawerLayout = (DrawerLayout) findViewById(R.id.drawerLayoutAllComments);
        navigationView = (NavigationView) findViewById(R.id.navigationViewAllComments);
        imageViewMenu = (ImageView) findViewById(R.id.imageViewMenuAllComments);


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
        textViewAddCartCount = (TextView)findViewById(R.id.textViewCartCountAllComments);
        relativeLayoutAddCart = (RelativeLayout)findViewById(R.id.relativeLayoutCartCountAllComments);

        // For Location
        fusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(this);


        if (ActivityCompat.checkSelfPermission(AllComments.this, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
            getLocation();
        } else {
            ActivityCompat.requestPermissions(AllComments.this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, 44);
        }


        navigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                int id = item.getItemId();
                switch(id)
                {
                    case R.id.nvAboutUs:
                        Toast.makeText(AllComments.this, "About Us", Toast.LENGTH_SHORT).show();
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
                        Toast.makeText(AllComments.this, "Logout", Toast.LENGTH_SHORT).show();
                    default:
                        return true;
                }


            }
        });


        textViewAppName = (TextView)findViewById(R.id.textViewAppNameAllComments);

        textViewAppName.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getApplicationContext(),HomePage.class));
            }
        });

        imageViewSearch = (ImageView)findViewById(R.id.imageViewSearchAllComments);

        imageViewSearch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(AllComments.this,SearchPage.class));
            }
        });


        // for add cart page

        imageViewAddCart = (ImageView)findViewById(R.id.imageViewCartAllComments);

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





    @SuppressLint("MissingPermission")
    private void getLocation() {


        fusedLocationProviderClient.getLastLocation().addOnCompleteListener(new OnCompleteListener<Location>() {
            @Override
            public void onComplete(@NonNull Task<Location> task) {

                Location location = task.getResult();

                if (location != null) {


                    try {
                        Geocoder geocoder = new Geocoder(AllComments.this, Locale.getDefault());

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



    // For comments
    private void getComments() {

        ProgressBar progressBar = (ProgressBar)findViewById(R.id.progressBarAllComments);
        progressBar.setVisibility(View.VISIBLE);
        class GetComments extends AsyncTask<Void, Void, String> {

            @Override
            protected void onPreExecute() {
                progressBar.setVisibility(View.VISIBLE);
                super.onPreExecute();
            }


            @Override
            protected void onPostExecute(String s) {
                super.onPostExecute(s);
                //   Toast.makeText(getApplicationContext(), s, Toast.LENGTH_SHORT).show();
                try {
                    loadIntoListView(s);
                    progressBar.setVisibility(View.INVISIBLE);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }

            @Override
            protected String doInBackground(Void... voids) {
                try {

                    URL url = new URL(URL_GET_COMMENTS);
                    HttpURLConnection con = (HttpURLConnection) url.openConnection();
                    StringBuilder sb = new StringBuilder();
                    BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(con.getInputStream()));
                    String json;
                    while ((json = bufferedReader.readLine()) != null) {
                        sb.append(json + "\n");
                    }
                    return sb.toString().trim();
                } catch (Exception e) {
                    return null;
                }
            }
        }
        GetComments getJSON = new GetComments();
        getJSON.execute();
    }

    private void loadIntoListView(String json) throws JSONException {
        JSONArray jsonArray = new JSONArray(json);
        String[] stocks = new String[jsonArray.length()];
        // stocks1 = new String[jsonArray.length()];
        for (int i = 0; i < jsonArray.length(); i++) {
            JSONObject obj = jsonArray.getJSONObject(i);


            //  stocks1[i] = obj.getString("correct_option");



            commentsClasses.add(new CommentsClass(

                    obj.getString("profile_pic"),
                    obj.getString("email"),
                    obj.getString("name"),
                    obj.getInt("items_id"),
                    obj.getDouble("rating"),
                    obj.getString("comment"),
                    obj.getString("date")

            ));


            Log.i("tagconvertstr", "["+commentsClasses+"]");

        }
       /* ArrayAdapter<String> arrayAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, stocks);
        listView.setAdapter(arrayAdapter);*/

        CommentAdapter adapter = new CommentAdapter(commentsClasses, AllComments.this);

        recyclerView.setAdapter(adapter);
        //  setRadioGroup();
    }





}