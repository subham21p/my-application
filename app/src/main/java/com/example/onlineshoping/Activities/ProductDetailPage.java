package com.example.onlineshoping.Activities;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager.widget.ViewPager;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.text.Editable;
import android.text.Html;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Log;
import android.view.ContextMenu;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.ScaleGestureDetector;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.HorizontalScrollView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RatingBar;
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
import com.example.onlineshoping.Adapters.AddCartAdapter;
import com.example.onlineshoping.Adapters.CardAdapter;
import com.example.onlineshoping.Adapters.CommentAdapter;
import com.example.onlineshoping.Adapters.HorizontalCardAdapter;
import com.example.onlineshoping.Adapters.SearchAdapter;
import com.example.onlineshoping.Adapters.ViewPagerAdapter;
import com.example.onlineshoping.ModelClasses.AddCartClass;
import com.example.onlineshoping.ModelClasses.CommentsClass;
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

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Random;

import static com.example.onlineshoping.ModelClasses.Api.URL_ADDCART;
import static com.example.onlineshoping.ModelClasses.Api.URL_GET_COMMENTS;
import static com.example.onlineshoping.ModelClasses.Api.URL_GET_PRODUCT;
import static com.example.onlineshoping.ModelClasses.Api.URL_LOGIN;
import static com.example.onlineshoping.ModelClasses.Api.URL_SHOWCART;
import static com.example.onlineshoping.ModelClasses.Config.DATA_URL;
import static com.example.onlineshoping.ModelClasses.Config.DATA_URL_CLOTHES;
import static com.example.onlineshoping.ModelClasses.Config.DATA_URL_COSMETICS;
import static com.example.onlineshoping.ModelClasses.Config.DATA_URL_ELECTRONIC;
import static com.example.onlineshoping.ModelClasses.Config.DATA_URL_FOOTWEAR;
import static com.example.onlineshoping.ModelClasses.Config.DATA_URL_GROCERY;
import static com.example.onlineshoping.ModelClasses.Config.DATA_URL_JEWELLERY;
import static com.example.onlineshoping.ModelClasses.Config.DATA_URL_TOYS;
import static com.example.onlineshoping.ModelClasses.CustomVolleyRequest.getInstance;

@RequiresApi(api = Build.VERSION_CODES.M)
public class ProductDetailPage extends AppCompatActivity implements RecyclerView.OnScrollChangeListener{

    ImageView imageViewCart;
    NetworkImageView networkImageView;
    TextView textViewName, textViewDes, textViewBrand, textViewQunt, textViewPrice, textViewAddCartCount, textViewAppName;
    RatingBar ratingBar;
    Button buttonCmnts, buttonAddCart;
    TextView textView,textViewNext;
    RelativeLayout relativeLayoutAddCart;

    ImageView imageViewMenu,imageViewSearch;

    TextView textViewCurrentLoc,textViewNoItems;
    DrawerLayout drawerLayout;
    NavigationView navigationView;
    ImageView imageViewProfilePic, imageViewAddCart;

    FusedLocationProviderClient fusedLocationProviderClient;
    private ImageLoader imageLoader;

    ViewPager viewPager;
    LinearLayout sliderDotspanel;
    private int dotscount;
    private ImageView[] dots;

    private List<SuperCar> superCars;
    private RecyclerView recyclerView;
    private RecyclerView.Adapter adapter;
    private RecyclerView.LayoutManager layoutManager, layoutManager2;
    private RequestQueue requestQueue;

    private int a = 0;
    private int requestCount = 1;

    private RecyclerView recyclerView2;
    private List<CommentsClass> commentsClasses;


    @RequiresApi(api = Build.VERSION_CODES.M)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_product_detail_page);

        if (!SharedPrefManager.getInstance(this).isLoggedIn()) {
            finish();
            startActivity(new Intent(this, LoginPage.class));
        }


        User user = SharedPrefManager.getInstance(this).getUser();

        ProfilePic profilePic = SharedPrefManager.getInstance(this).getProfilePic();

        Intent intent = getIntent();

        String image = intent.getStringExtra("image");
        int itemId = intent.getIntExtra("itemId",0);
        String category = intent.getStringExtra("category");
        String name = intent.getStringExtra("name");
        String brand = intent.getStringExtra("brand");
        String quantity = intent.getStringExtra("quantity");
        int price = intent.getIntExtra("price",0);
        float rating = intent.getFloatExtra("rating",0);


        getProductDetail(category,name);

        recyclerView = (RecyclerView) findViewById(R.id.recyclerViewProductDetail);
        recyclerView.setHasFixedSize(true);
        layoutManager = new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false);
        recyclerView.setLayoutManager(layoutManager);

        //Initializing our superheroes list
        superCars = new ArrayList<>();
        requestQueue = Volley.newRequestQueue(this);

        //Calling method to get data to fetch data
        //Adding an scroll change listener to recyclerview
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            recyclerView.setOnScrollChangeListener(this);
        }


        getData();

        //initializing our adapter
        adapter = new HorizontalCardAdapter(superCars, this);

        //Adding adapter to recyclerview
        recyclerView.setAdapter(adapter);


        //For Search Items List
        recyclerView2 = findViewById(R.id.recyclerViewCommentsProductDetail);
        recyclerView2.setHasFixedSize(true);
        recyclerView2.setLayoutManager(new LinearLayoutManager(ProductDetailPage.this));

        commentsClasses = new ArrayList<>();

        showComments();




        buttonCmnts = (Button)findViewById(R.id.buttonSeeCmntsProductDetail);
        buttonAddCart = (Button)findViewById(R.id.buttonAddCartProductDetail);
        imageViewCart = (ImageView)findViewById(R.id.imageViewCartProductDetail);



        drawerLayout = (DrawerLayout) findViewById(R.id.drawerLayoutProductDetail);
        navigationView = (NavigationView) findViewById(R.id.navigationViewProductDetail);
        imageViewMenu = (ImageView) findViewById(R.id.imageViewMenuProductDetail);


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
        textViewAddCartCount = (TextView)findViewById(R.id.textViewCartCountProductDetail);
        relativeLayoutAddCart = (RelativeLayout)findViewById(R.id.relativeLayoutCartCountProductDetail);

        // For Location
        fusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(this);


        if (ActivityCompat.checkSelfPermission(ProductDetailPage.this, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
            getLocation();
        } else {
            ActivityCompat.requestPermissions(ProductDetailPage.this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, 44);
        }


        navigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                int id = item.getItemId();
                switch(id)
                {
                    case R.id.nvAboutUs:
                        Toast.makeText(ProductDetailPage.this, "About Us", Toast.LENGTH_SHORT).show();
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
                        Toast.makeText(ProductDetailPage.this, "Logout", Toast.LENGTH_SHORT).show();
                    default:
                        return true;
                }


            }
        });



        buttonCmnts.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(ProductDetailPage.this,AllComments.class));
            }
        });


        buttonAddCart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                addCart(user.getEmail(), image, itemId, 1,category,name,brand,quantity,price,rating);
                Log.i("addcart", "[" + image + "]");
               /* finish();
                startActivity(getIntent());*/
                nosOfAddCartList(user.getEmail());
            }
        });


        textViewAppName = (TextView)findViewById(R.id.textViewAppNameProductDetail);

        textViewAppName.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getApplicationContext(),HomePage.class));
            }
        });

        imageViewSearch = (ImageView)findViewById(R.id.imageViewSearchProductDetail);

        imageViewSearch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(ProductDetailPage.this,SearchPage.class));
            }
        });


        imageViewCart.setOnClickListener(new View.OnClickListener() {
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
                        Geocoder geocoder = new Geocoder(ProductDetailPage.this, Locale.getDefault());

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






    // get All data
    private void getProductDetail(String cat, String nam) {
        //first getting the values
        final String category = cat;
        final String name = nam;



        //if everything is fine

        class GetProductDetail extends AsyncTask<Void, Void, String> {


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
                        Toast.makeText(getApplicationContext(), obj.getString("message"), Toast.LENGTH_SHORT).show();

                        //getting the user from the response
                        JSONObject userJson = obj.getJSONObject("user");

                        //creating a new user object
                        textViewName = (TextView)findViewById(R.id.textViewNameProductDetail);
                        textViewBrand = (TextView)findViewById(R.id.textViewBrandProductDetail);
                        textViewDes = (TextView)findViewById(R.id.textViewDesProductDetail);
                        textViewQunt = (TextView)findViewById(R.id.textViewQuantityProductDetail);
                        textViewPrice = (TextView)findViewById(R.id.textViewPriceProductDetail);
                        ratingBar = (RatingBar) findViewById(R.id.ratingBarProductDetail);

                        textViewName.setText(userJson.getString("name"));
                        textViewBrand.setText(userJson.getString("brand"));
                        textViewDes.setText(userJson.getString("description"));
                        textViewQunt.setText(userJson.getString("quantity"));
                        textViewPrice.setText(userJson.getString("price"));

                        ratingBar.setRating(Float.parseFloat(userJson.getString("rating")));

                        String [] img = {userJson.getString("img_one"),userJson.getString("img_two"),userJson.getString("img_three")};

                        viewPager = (ViewPager) findViewById(R.id.viewPagerProductDetail);

                        sliderDotspanel = (LinearLayout) findViewById(R.id.SliderDots);

                        ViewPagerAdapter viewPagerAdapter = new ViewPagerAdapter(ProductDetailPage.this,img);

                        viewPager.setAdapter(viewPagerAdapter);

                        dotscount = viewPagerAdapter.getCount();
                        dots = new ImageView[dotscount];

                        for (int i = 0; i < dotscount; i++) {

                            dots[i] = new ImageView(ProductDetailPage.this);
                            dots[i].setImageDrawable(ContextCompat.getDrawable(getApplicationContext(), R.drawable.non_active_dot));

                            LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT);

                            params.setMargins(8, 0, 8, 0);

                            sliderDotspanel.addView(dots[i], params);

                        }


                        dots[0].setImageDrawable(ContextCompat.getDrawable(getApplicationContext(), R.drawable.active_dot));
                        Log.i("location", "[" +dots+ "]");
                        viewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
                            @Override
                            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

                            }

                            @Override
                            public void onPageSelected(int position) {

                                for(int i = 0; i< dotscount; i++){
                                    dots[i].setImageDrawable(ContextCompat.getDrawable(getApplicationContext(), R.drawable.non_active_dot));
                                }

                                dots[position].setImageDrawable(ContextCompat.getDrawable(getApplicationContext(), R.drawable.active_dot));

                            }

                            @Override
                            public void onPageScrollStateChanged(int state) {

                            }
                        });


                        viewPager.setOnTouchListener(
                                new View.OnTouchListener() {
                                    private boolean moved;

                                    @Override
                                    public boolean onTouch(View view, MotionEvent motionEvent) {
                                        if (motionEvent.getAction() == MotionEvent.ACTION_DOWN) {
                                            moved = false;
                                        }
                                        if (motionEvent.getAction() == MotionEvent.ACTION_MOVE) {
                                            moved = true;
                                        }
                                        if (motionEvent.getAction() == MotionEvent.ACTION_UP) {
                                            if (!moved) {
                                                view.performClick();
                                            }
                                        }

                                        return false;
                                    }
                                }
                        );

// then you can simply use the standard onClickListener ...
                        viewPager.setOnClickListener(
                                new View.OnClickListener() {

                                    @Override
                                    public void onClick(View view) {
                                        Intent intent1 = new Intent(ProductDetailPage.this,ZoomImagePage.class);
                                        try {
                                            intent1.putExtra("img1",userJson.getString("img_one"));
                                        } catch (JSONException e) {
                                            e.printStackTrace();
                                        }
                                        try {
                                            intent1.putExtra("img2",userJson.getString("img_two"));
                                        } catch (JSONException e) {
                                            e.printStackTrace();
                                        }
                                        try {
                                            intent1.putExtra("img3",userJson.getString("img_three"));
                                        } catch (JSONException e) {
                                            e.printStackTrace();
                                        }
                                        Log.i("intent", "["+intent1+"]");
                                        startActivity(intent1);
                                    }
                                }
                        );



                    } else {
                        Toast.makeText(getApplicationContext(), "Invalid username or password", Toast.LENGTH_SHORT).show();
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
                params.put("category", category);
                params.put("name", name);

                //returing the response
                return requestHandler.sendPostRequest(URL_GET_PRODUCT, params);
            }
        }

        GetProductDetail ul = new GetProductDetail();
        ul.execute();
    }




// Horizontal RecyclerView Elements (Related Products)

    private JsonArrayRequest getDataFromServer(int requestCount) {
        //Initializing ProgressBar
        final ProgressBar progressBar = (ProgressBar) findViewById(R.id.progressBarProductDetail);

        //Displaying Progressbar
        progressBar.setVisibility(View.VISIBLE);
        setProgressBarIndeterminateVisibility(true);


        Intent intent = getIntent();

        String category = intent.getStringExtra("category");

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

                //Adding data to the superhero object

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

@NonNull
    public void onScrollChange(View v, int scrollX, int scrollY, int oldScrollX, int oldScrollY) {
        //Ifscrolled at last then
        if (isLastItemDisplaying(recyclerView)) {
            //Calling the method getdata again
            getData();
        }
    }





// For comments
    private void showComments() {

        class DownloadJSON extends AsyncTask<Void, Void, String> {

            @Override
            protected void onPreExecute() {
                super.onPreExecute();
            }


            @Override
            protected void onPostExecute(String s) {
                super.onPostExecute(s);
                //   Toast.makeText(getApplicationContext(), s, Toast.LENGTH_SHORT).show();
                try {
                    loadIntoListView(s);
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
        DownloadJSON getJSON = new DownloadJSON();
        getJSON.execute();
    }

    private void loadIntoListView(String json) throws JSONException {
        JSONArray jsonArray = new JSONArray(json);
        String[] stocks = new String[jsonArray.length()];
       // stocks1 = new String[jsonArray.length()];
        for (int i = 0; i < jsonArray.length(); i++) {
            JSONObject obj = jsonArray.getJSONObject(i);

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

        CommentAdapter adapter = new CommentAdapter(commentsClasses, ProductDetailPage.this);

        recyclerView2.setAdapter(adapter);
        //  setRadioGroup();
    }


    private void addCart(String email, String image,int items_id,int nos_of_products,String category,String name,String brand, String quantity, float price, float rating) {

        class AddCart extends AsyncTask<Void, Void, String> {

            @Override
            protected void onPreExecute() {
                super.onPreExecute();
                Log.i("addcart3", "[" + image + "]");

            }

            @Override
            protected void onPostExecute(String s) {
                super.onPostExecute(s);

                try {
                    //converting response to json object
                    JSONObject obj = new JSONObject(s);

                    //if no error in response
                    if (!obj.getBoolean("error")) {
                        Toast.makeText(getApplicationContext(), obj.getString("message"), Toast.LENGTH_SHORT).show();

                        //getting the user from the response
                        JSONObject userJson = obj.getJSONObject("user");

                        //creating a new user object

                    } else {
                       // progressDialog.dismiss();
                        Toast.makeText(getApplicationContext(), obj.getString("message"), Toast.LENGTH_SHORT).show();
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }

            @Override
            protected String doInBackground(Void... voids) {
                Log.i("addcart2", "[" + image + "]");
                //creating request handler object
                RequestHandler requestHandler = new RequestHandler();

                //creating request parameters
                HashMap<String, String> params = new HashMap<>();

                params.put("email", email);
                params.put("image", image);
                params.put("items_id", String.valueOf(items_id));
                params.put("nos_of_products", String.valueOf(nos_of_products));
                params.put("category", category);
                params.put("name", name);
                params.put("brand", brand);
                params.put("quantity", quantity);
                params.put("price", String.valueOf(price));
                params.put("rating", String.valueOf(rating));
                Log.i("addcart2", "[" + image + "]");
                //returing the response
                return requestHandler.sendPostRequest(URL_ADDCART, params);
            }
        }

        AddCart ul = new AddCart();
        ul.execute();
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