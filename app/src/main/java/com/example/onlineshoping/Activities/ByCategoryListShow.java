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
import android.os.Build;
import android.os.Bundle;
import android.text.Html;
import android.util.Log;
import android.view.Gravity;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.ImageLoader;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.NetworkImageView;
import com.android.volley.toolbox.Volley;
import com.example.onlineshoping.Adapters.CardAdapter;
import com.example.onlineshoping.Adapters.SearchAdapter;
import com.example.onlineshoping.ModelClasses.Config;
import com.example.onlineshoping.ModelClasses.CustomVolleyRequest;
import com.example.onlineshoping.ModelClasses.ProfilePic;
import com.example.onlineshoping.ModelClasses.RequestHandler;
import com.example.onlineshoping.ModelClasses.SearchData;
import com.example.onlineshoping.ModelClasses.SharedPrefManager;
import com.example.onlineshoping.ModelClasses.SuperCar;
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
import java.util.Random;

import static com.example.onlineshoping.ModelClasses.Api.URL_SHOWCART;
import static com.example.onlineshoping.ModelClasses.Config.DATA_URL;
import static com.example.onlineshoping.ModelClasses.Config.DATA_URL_CLOTHES;
import static com.example.onlineshoping.ModelClasses.Config.DATA_URL_COSMETICS;
import static com.example.onlineshoping.ModelClasses.Config.DATA_URL_ELECTRONIC;
import static com.example.onlineshoping.ModelClasses.Config.DATA_URL_FOOTWEAR;
import static com.example.onlineshoping.ModelClasses.Config.DATA_URL_GROCERY;
import static com.example.onlineshoping.ModelClasses.Config.DATA_URL_JEWELLERY;
import static com.example.onlineshoping.ModelClasses.Config.DATA_URL_TOYS;

@SuppressLint("NewApi")
public class ByCategoryListShow extends AppCompatActivity implements RecyclerView.OnScrollChangeListener{

    private List<SuperCar> superCars;
    private RecyclerView recyclerView;
    int i = 0;
    private int page = 1;
    private static final String ROOT_URL = "http://192.168.1.6/online_examination/includes/Api.php?apicall=c";
    //SwipeRefreshLayout swipeRefreshLayout;
    private RecyclerView.Adapter adapter;
    private RecyclerView.LayoutManager layoutManager;
    private RequestQueue requestQueue;

    private int a = 0;

    //The request counter to send ?page=1, ?page=2  requests
    private int requestCount = 1;

    private RecyclerView recyclerView2;
    public static   List<SearchData> searchData;


    public static final int CONNECTION_TIMEOUT = 10000;
    public static final int READ_TIMEOUT = 15000;
    private RecyclerView mRVFish;
    private SearchAdapter mAdapter;
    private ImageLoader imageLoader;


    androidx.appcompat.widget.SearchView searchView = null;


    Button button;


    TextView textViewCurrentLoc, textViewName, textViewAddCartCount, textViewAppName;
    DrawerLayout drawerLayout;
    NavigationView navigationView;
    ImageView imageViewMenu,imageViewSearch, imageViewProfilePic, imageViewAddCart;
    RelativeLayout relativeLayoutAddCart;
    NetworkImageView networkImageView;
    FusedLocationProviderClient fusedLocationProviderClient;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_by_category_list_show);

        if (!SharedPrefManager.getInstance(this).isLoggedIn()) {
            finish();
            startActivity(new Intent(this, LoginPage.class));
        }

     //   swipeRefreshLayout = (SwipeRefreshLayout) findViewById(R.id.refresh);



        recyclerView = (RecyclerView) findViewById(R.id.recyclerViewCategoryList);
        recyclerView.setHasFixedSize(true);
        layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);

        //Initializing our superheroes list
        superCars = new ArrayList<>();
        requestQueue = Volley.newRequestQueue(this);

        //Calling method to get data to fetch data


        getData();

        //Adding an scroll change listener to recyclerview
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            recyclerView.setOnScrollChangeListener(this);
        }

        //initializing our adapter
        adapter = new CardAdapter(superCars, this);

        //Adding adapter to recyclerview
        recyclerView.setAdapter(adapter);


        drawerLayout = (DrawerLayout) findViewById(R.id.drawerLayoutCategoryList);
        navigationView = (NavigationView) findViewById(R.id.navigationViewCategoryList);
        imageViewMenu = (ImageView) findViewById(R.id.imageViewMenuCategoryList);


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

        User user = SharedPrefManager.getInstance(this).getUser();

        ProfilePic profilePic = SharedPrefManager.getInstance(this).getProfilePic();

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
        textViewAddCartCount = (TextView)findViewById(R.id.textViewCartCountCategoryList);
        relativeLayoutAddCart = (RelativeLayout)findViewById(R.id.relativeLayoutCartCountCategoryList);

        // For Location
        fusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(this);


        if (ActivityCompat.checkSelfPermission(ByCategoryListShow.this, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
            getLocation();
        } else {
            ActivityCompat.requestPermissions(ByCategoryListShow.this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, 44);
        }


        navigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                int id = item.getItemId();
                switch(id)
                {
                    case R.id.nvAboutUs:
                        Toast.makeText(ByCategoryListShow.this, "About Us", Toast.LENGTH_SHORT).show();
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
                        Toast.makeText(ByCategoryListShow.this, "Logout", Toast.LENGTH_SHORT).show();
                    default:
                        return true;
                }


            }
        });


        textViewAppName = (TextView)findViewById(R.id.textViewAppNameCategoryList);

        textViewAppName.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getApplicationContext(),HomePage.class));
            }
        });

        imageViewSearch = (ImageView)findViewById(R.id.imageViewSearchCategoryList);

        imageViewSearch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(ByCategoryListShow.this,SearchPage.class));
            }
        });



        // for add cart page

        imageViewAddCart = (ImageView)findViewById(R.id.imageViewCartCategoryList);

        imageViewAddCart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getApplicationContext(),AddCartList.class));
            }
        });




    }



    @SuppressLint("MissingPermission")
    private void getLocation() {


        fusedLocationProviderClient.getLastLocation().addOnCompleteListener(new OnCompleteListener<Location>() {
            @Override
            public void onComplete(@NonNull Task<Location> task) {

                Location location = task.getResult();

                if (location != null) {


                    try {
                        Geocoder geocoder = new Geocoder(ByCategoryListShow.this, Locale.getDefault());

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



    private JsonArrayRequest getDataFromServer(int requestCount) {
        //Initializing ProgressBar
        final ProgressBar progressBar = (ProgressBar) findViewById(R.id.progressBarCategoryList);

        //Displaying Progressbar
        progressBar.setVisibility(View.VISIBLE);
        setProgressBarIndeterminateVisibility(true);

        Intent intent = getIntent();

        String category = intent.getStringExtra("searchItem");

        String URL = DATA_URL;

        Log.i("category112151", "[" + category+ "]");

        if (category.matches("grocery")){
            URL = DATA_URL_GROCERY;
        }
        else if (category.matches("clothes")){
            URL = DATA_URL_CLOTHES;
        }
        else if (category.matches("footwear")){
            URL = DATA_URL_FOOTWEAR;
        }
        else if (category.matches("electronic")){
            URL = DATA_URL_ELECTRONIC;
        }
        else if (category.matches("cosmetics")){
            URL = DATA_URL_COSMETICS;
        }
        else if (category.matches("toys")){
            URL = DATA_URL_TOYS;
        }
        else if (category.matches("jewellery")){
            URL = DATA_URL_JEWELLERY;
        }
        //JsonArrayRequest of volley
        JsonArrayRequest jsonArrayRequest = new JsonArrayRequest(URL + String.valueOf(requestCount),
                new Response.Listener<JSONArray>() {
                    @Override
                    public void onResponse(JSONArray response) {
                        //Calling method parseData to parse the json response

                        parseData(response);
                        //Hiding the progressbar
                        progressBar.setVisibility(View.GONE);
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        progressBar.setVisibility(View.GONE);
                        //If an error occurs that means end of the list has reached

                        count();
                    }

                });

        //Returning the request
        return jsonArrayRequest;
    }

    private void count() {

        requestCount = 1;
    }

    //This method will get data from the web api
    private void getData() {
        //Adding the method to the queue by calling the method getDataFromServer
        requestQueue.add(getDataFromServer(requestCount));
        //Incrementing the request counter

        requestCount++;
    }

    //This method will parse json data
    private void parseData(JSONArray array) {
        Random r = new Random();

        Intent intent = getIntent();
        String itemToSearch = intent.getStringExtra("searchItem");

        Log.i("tagconvertstraaan1", "[" + array.length() + "]");
        for (int i = 0; i < array.length(); i++) {
            //Creating the superhero object
            int index = r.nextInt(array.length());

            JSONObject json = null;
            try {
                //Getting json
                json = array.getJSONObject(i);

                //  Collections.shuffle(Collections.singletonList(json.getString("name")), new Random(System.currentTimeMillis()));
                Log.i("tagconvertstraaan2", "[" + json.getString("category") + "]");
                //Adding data to the superhero object

             //  if(json.getString(Config.TAG_CATEGORY).equals(itemToSearch) || json.getString(Config.TAG_NAME).equals(itemToSearch) || json.getString(Config.TAG_BRAND).equals(itemToSearch)){
                   SuperCar superHero = new SuperCar();
                    Log.i("tagconvertstraaan3", "[" + json.getString("category") + "]");

                superHero.setId(Integer.parseInt(json.getString(Config.TAG_ID)));
                superHero.setImageUrl(json.getString(Config.TAG_IMAGE_URL));
                superHero.setImageUrl2(json.getString(Config.TAG_IMAGE_URL2));
                superHero.setImageUrl3(json.getString(Config.TAG_IMAGE_URL3));
                superHero.setName(json.getString(Config.TAG_NAME));
                superHero.setCategory(json.getString(Config.TAG_CATEGORY));
                superHero.setBrand(json.getString(Config.TAG_BRAND));
                superHero.setQuantity(json.getString(Config.TAG_QUANTITY));
                superHero.setPrice((int) Float.parseFloat(json.getString(Config.TAG_PRICE)));
                superHero.setRating(Float.parseFloat(json.getString(Config.TAG_RATING)));
                   //Adding the superhero object to the list
                   superCars.add(superHero);
          //      }

            } catch (JSONException e) {
                e.printStackTrace();
            }


        }

        //Notifying the adapter that data has been added or changed
        adapter.notifyDataSetChanged();
    }

    //This method would check that the recyclerview scroll has reached the bottom or not
    private boolean isLastItemDisplaying(RecyclerView recyclerView) {
        if (recyclerView.getAdapter().getItemCount() != 0) {
            int lastVisibleItemPosition = ((LinearLayoutManager) recyclerView.getLayoutManager()).findLastCompletelyVisibleItemPosition();
            if (lastVisibleItemPosition != RecyclerView.NO_POSITION && lastVisibleItemPosition == recyclerView.getAdapter().getItemCount() - 1) {
                if (a == 50) {
                    // requestCount = 1;
                    return false;
                } else {
                    // This needs to be stopped.
                    a++;
                    // requestCount = 1;
                    return true;
                }
            }

        }

        return false;
    }

    //Overriden method to detect scrolling
    @Override
    public void onScrollChange(View v, int scrollX, int scrollY, int oldScrollX, int oldScrollY) {
        //Ifscrolled at last then
        if (isLastItemDisplaying(recyclerView)) {
            //Calling the method getdata again
            getData();
        }
    }



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











}