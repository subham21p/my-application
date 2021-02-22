package com.example.onlineshoping.Activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.core.app.ActivityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.OrientationHelper;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

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
import android.text.Html;
import android.util.Log;
import android.view.Gravity;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.HorizontalScrollView;
import android.widget.ImageView;
import android.widget.LinearLayout;
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

import static com.example.onlineshoping.ModelClasses.Api.URL_IMAGE_SHOW;
import static com.example.onlineshoping.ModelClasses.Api.URL_SHOWCART;
import static com.google.android.material.internal.ViewUtils.dpToPx;

@SuppressLint("NewApi")
public class HomePage extends AppCompatActivity implements RecyclerView.OnScrollChangeListener {

    private List<SuperCar> superCars;

    private RecyclerView recyclerView2;
    public static List<SearchData> searchData;
    ArrayList<String> names;
    private EditText editText;
    private SearchAdapter adapter1;

ImageView imageView;

    //Creating Views
    private RecyclerView recyclerView;
    private RecyclerView.LayoutManager layoutManager;
    private RecyclerView.Adapter adapter;
    private int a = 0;

    private SwipeRefreshLayout swipeRefreshLayout;

    //Volley Request Queue
    private RequestQueue requestQueue;

    //The request counter to send ?page=1, ?page=2  requests
    private int requestCount = 1;

    DrawerLayout drawerLayout;
    TextView textViewAddCartCount, textViewAppName;
    NavigationView navigationView;
    ImageView imageViewMenu, imageViewProfilePic, imageViewAddCart, imageViewSearch;
    NetworkImageView networkImageView;
    RelativeLayout relativeLayoutAddCart;
    private ImageLoader imageLoader;

    FusedLocationProviderClient fusedLocationProviderClient;

    private TextView textViewCurrentLoc, textViewName, textViewGrocery, textViewClothes, textViewFootwear, textViewElectronic, textViewCosmetics, textViewToys, textViewJewellery;
    Button button;

    private Context context;

    LinearLayout linearLayout1, linearLayout2;
    ConstraintLayout constraintLayout;
    HorizontalScrollView horizontalScrollView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home_page);


// Checked user is Login or not
        if (!SharedPrefManager.getInstance(this).isLoggedIn()) {
            finish();
            startActivity(new Intent(this, LoginPage.class));
        }


        User user = SharedPrefManager.getInstance(this).getUser();


        recyclerView = (RecyclerView) findViewById(R.id.recyclerView);
        recyclerView.setHasFixedSize(true);
        layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);

        editText = (EditText)findViewById(R.id.editTextSearchHome);
        linearLayout1 = (LinearLayout)findViewById(R.id.linearLayout);
        linearLayout2 = (LinearLayout)findViewById(R.id.linearLayoutHSV);
        constraintLayout = (ConstraintLayout)findViewById(R.id.constraintLayout);
        horizontalScrollView = (HorizontalScrollView)findViewById(R.id.horizontalScrollView);
        imageViewSearch = (ImageView)findViewById(R.id.imageViewSearchHome);
        imageViewSearch.setVisibility(View.INVISIBLE);


       // int findFirstVisibleItemPosition = ((LinearLayoutManager) recyclerView.getLayoutManager()).findFirstVisibleItemPosition();

        recyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {

            @SuppressLint("RestrictedApi")
            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                if (findFirstVisibleItemPosition() > 0) {

                    imageViewSearch.setVisibility(View.VISIBLE);

                    linearLayout2.getLayoutParams().height = 0;
                    linearLayout2.requestLayout();

                    constraintLayout.getLayoutParams().height = ViewGroup.LayoutParams.WRAP_CONTENT;
                    constraintLayout.requestLayout();

                    horizontalScrollView.getLayoutParams().height = 0;
                    horizontalScrollView.requestLayout();

                    editText.getLayoutParams().height = 0;
                    editText.requestLayout();

                    Log.d("SCROLLINGDOWN","SCROLL");
                } else {

                    imageViewSearch.setVisibility(View.INVISIBLE);

                    linearLayout2.getLayoutParams().height = ViewGroup.LayoutParams.MATCH_PARENT;
                    linearLayout2.requestLayout();

                    constraintLayout.getLayoutParams().height = ViewGroup.LayoutParams.WRAP_CONTENT;
                    constraintLayout.requestLayout();

                    horizontalScrollView.getLayoutParams().height = (int) dpToPx(50,getApplicationContext());
                    horizontalScrollView.requestLayout();

                    editText.getLayoutParams().height = ViewGroup.LayoutParams.WRAP_CONTENT;
                    editText.requestLayout();

                    Log.d("SCROLLINGUP","SCROLL");
                }
            }

            public int dpToPx(int dp, Context context) {
                float density = context.getResources().getDisplayMetrics().density;
                return Math.round((float) dp * density);
            }
        });

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


        drawerLayout = (DrawerLayout) findViewById(R.id.drawerLayoutHome);
        navigationView = (NavigationView) findViewById(R.id.navigationViewHome);
        imageViewMenu = (ImageView) findViewById(R.id.imageViewMenuHome);


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
        networkImageView = (NetworkImageView)header.findViewById(R.id.networkImageViewNV);


        // get profile image
        getProfilePic(user.getEmail());

        textViewName.setText(user.getName());
//        imageViewProfilePic.setImageResource(Integer.parseInt(user.getProfile_pic()));

        // Add Cart number
        nosOfAddCartList(user.getEmail());
        textViewAddCartCount = (TextView)findViewById(R.id.textViewCartCountHome);
        relativeLayoutAddCart = (RelativeLayout)findViewById(R.id.relativeLayoutCartCountHome);


        networkImageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getApplicationContext(),SettingPage.class));
            }
        });

        // For Location
        fusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(this);


        if (ActivityCompat.checkSelfPermission(HomePage.this, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
            getLocation();
        } else {
            ActivityCompat.requestPermissions(HomePage.this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, 44);
        }


        navigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                int id = item.getItemId();
                switch(id)
                {
                    case R.id.nvAboutUs:
                        Toast.makeText(HomePage.this, "About Us", Toast.LENGTH_SHORT).show();
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
                        Toast.makeText(HomePage.this, "Logout", Toast.LENGTH_SHORT).show();
                    default:
                        return true;
                }


            }
        });


        textViewGrocery = (TextView) findViewById(R.id.textViewGrocery);
        textViewClothes = (TextView) findViewById(R.id.textViewClothes);
        textViewFootwear = (TextView)findViewById(R.id.textViewFootwear);
        textViewElectronic = (TextView) findViewById(R.id.textViewElectronic);
        textViewCosmetics = (TextView) findViewById(R.id.textViewCosmetic);
        textViewToys = (TextView) findViewById(R.id.textViewToys);
        textViewJewellery = (TextView)findViewById(R.id.textViewJewellery);
        textViewAppName = (TextView)findViewById(R.id.textViewAppNameHome);

        textViewGrocery.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(HomePage.this,ByCategoryListShow.class);
                intent.putExtra("searchItem","grocery");
                startActivity(intent);
               // startActivity(new Intent(HomePage.this, ByCategoryListShow.class));
            }
        });


        textViewClothes.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(HomePage.this,ByCategoryListShow.class);
                intent.putExtra("searchItem","clothes");
                startActivity(intent);

            }
        });

        textViewFootwear.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(HomePage.this,ByCategoryListShow.class);
                intent.putExtra("searchItem","footwear");
                startActivity(intent);

            }
        });

        textViewElectronic.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(HomePage.this,ByCategoryListShow.class);
                intent.putExtra("searchItem","electronic");
                startActivity(intent);

            }
        });

        textViewCosmetics.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(HomePage.this,ByCategoryListShow.class);
                intent.putExtra("searchItem","cosmetics");
                startActivity(intent);

            }
        });

        textViewToys.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(HomePage.this,ByCategoryListShow.class);
                intent.putExtra("searchItem","toys");
                startActivity(intent);

            }
        });

        textViewJewellery.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(HomePage.this,ByCategoryListShow.class);
                intent.putExtra("searchItem","jewellery");
                startActivity(intent);
            }
        });





        editText.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                startActivity(new Intent(HomePage.this,SearchPage.class));
            }
        });

        imageViewSearch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(HomePage.this,SearchPage.class));
            }
        });


        textViewAppName.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getApplicationContext(),HomePage.class));
            }
        });

        // for add cart page

        imageViewAddCart = (ImageView)findViewById(R.id.imageViewCartHome);

        imageViewAddCart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getApplicationContext(),AddCartList.class));
            }
        });




    }


    public int findFirstVisibleItemPosition() {
        final View child = findOneVisibleChild(0, layoutManager.getChildCount(), false, true);
        return child == null ? RecyclerView.NO_POSITION : recyclerView.getChildAdapterPosition(child);
    }




    View findOneVisibleChild(int fromIndex, int toIndex, boolean completelyVisible,
                             boolean acceptPartiallyVisible) {
        OrientationHelper helper;
        if (layoutManager.canScrollVertically()) {
            helper = OrientationHelper.createVerticalHelper(layoutManager);
        } else {
            helper = OrientationHelper.createHorizontalHelper(layoutManager);
        }

        final int start = helper.getStartAfterPadding();
        final int end = helper.getEndAfterPadding();
        final int next = toIndex > fromIndex ? 1 : -1;
        View partiallyVisible = null;
        for (int i = fromIndex; i != toIndex; i += next) {
            final View child = layoutManager.getChildAt(i);
            final int childStart = helper.getDecoratedStart(child);
            final int childEnd = helper.getDecoratedEnd(child);
            if (childStart < end && childEnd > start) {
                if (completelyVisible) {
                    if (childStart >= start && childEnd <= end) {
                        return child;
                    } else if (acceptPartiallyVisible && partiallyVisible == null) {
                        partiallyVisible = child;
                    }
                } else {
                    return child;
                }
            }
        }
        return partiallyVisible;
    }




    private JsonArrayRequest getDataFromServer(int requestCount) {
        //Initializing ProgressBar
        final ProgressBar progressBar = (ProgressBar) findViewById(R.id.progressBar1);

        //Displaying Progressbar
        progressBar.setVisibility(View.VISIBLE);
        setProgressBarIndeterminateVisibility(true);

        //JsonArrayRequest of volley
        JsonArrayRequest jsonArrayRequest = new JsonArrayRequest(Config.DATA_URL + String.valueOf(requestCount),
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

        Log.i("tagconvertstraaan1", "[" + array.length() + "]");
        for (int i = 0; i < array.length(); i++) {
            //Creating the superhero object
            int index = r.nextInt(array.length());
            SuperCar superHero = new SuperCar();
            JSONObject json = null;
            try {
                //Getting json
                json = array.getJSONObject(i);

                //  Collections.shuffle(Collections.singletonList(json.getString("name")), new Random(System.currentTimeMillis()));
                Log.i("tagconvertstraaan2", "[" + json.getString("name") + "]");
                //Adding data to the superhero object
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
               // superHero.setBrand(json.getString(Config.TAG_BRAND));


            } catch (JSONException e) {
                e.printStackTrace();
            }
            //Adding the superhero object to the list
            superCars.add(superHero);
        }

        //Notifying the adapter that data has been added or changed
        adapter.notifyDataSetChanged();
    }

    //This method would check that the recyclerview scroll has reached the bottom or not
    private boolean isLastItemDisplaying(RecyclerView recyclerView) {
        if (recyclerView.getAdapter().getItemCount() != 0) {
            int lastVisibleItemPosition = ((LinearLayoutManager) recyclerView.getLayoutManager()).findLastCompletelyVisibleItemPosition();
            if (lastVisibleItemPosition != RecyclerView.NO_POSITION && lastVisibleItemPosition == recyclerView.getAdapter().getItemCount() - 1) {
                if (a == 6) {
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


    @SuppressLint("MissingPermission")
    private void getLocation() {


        fusedLocationProviderClient.getLastLocation().addOnCompleteListener(new OnCompleteListener<Location>() {
            @Override
            public void onComplete(@NonNull Task<Location> task) {

                Location location = task.getResult();

                if (location != null) {


                    try {
                        Geocoder geocoder = new Geocoder(HomePage.this, Locale.getDefault());

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






    private void getProfilePic(String eml) {
        //first getting the values
        final String email = eml;
        //     final String password = editTextPassword.getText().toString();

        //validating inputs


        //if everything is fine

        class GetProfilePic extends AsyncTask<Void, Void, String> {

            // ProgressBar progressBar;


            @Override
            protected void onPreExecute() {
                super.onPreExecute();
                // progressBar = (ProgressBar) findViewById(R.id.progressBar2);
//                progressBar.setVisibility(View.VISIBLE);
                //   progressDialog.setMessage("Login...");
                //  progressDialog.show();
            }

            @Override
            protected void onPostExecute(String s) {
                super.onPostExecute(s);
                //       progressBar.setVisibility(View.GONE);
                Log.i("imagesssssss", "[" + s + "]");

                try {
                    //converting response to json object
                    JSONObject obj = new JSONObject(s);

                    //if no error in response
                    if (!obj.getBoolean("error")) {
                        Toast.makeText(getApplicationContext(), obj.getString("message"), Toast.LENGTH_SHORT).show();

                        //getting the user from the response
                        JSONObject userJson = obj.getJSONObject("pic");

                        //creating a new user object

                            ProfilePic profilePic = new ProfilePic(
                                    userJson.getString("image")
                            );

                            SharedPrefManager.getInstance(getApplicationContext()).profilePic(profilePic);

                        imageViewProfilePic.setVisibility(View.INVISIBLE);

                        imageLoader = CustomVolleyRequest.getInstance(getApplicationContext()).getImageLoader();
                        imageLoader.get(userJson.getString("image"), ImageLoader.getImageListener(networkImageView, R.drawable.ic_launcher_background, android.R.drawable.ic_dialog_alert));

                        networkImageView.setImageUrl(userJson.getString("image"),imageLoader);


                    } else {
                        //  progressDialog.dismiss();
                        ProfilePic profilePic = new ProfilePic("");
                        SharedPrefManager.getInstance(getApplicationContext()).profilePic(profilePic);
                        imageViewProfilePic.setVisibility(View.VISIBLE);
                        Toast.makeText(getApplicationContext(), obj.getString("message"), Toast.LENGTH_SHORT).show();
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

                //returing the response
                return requestHandler.sendPostRequest(URL_IMAGE_SHOW, params);
            }
        }

        GetProfilePic ul = new GetProfilePic();
        ul.execute();
    }







    @Override
    public void onRestart()
    /*private void downloadJSON(final String urlWebService) {

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
                  *//*  RequestHandler requestHandler = new RequestHandler();
                    HashMap<String, String> params = new HashMap<>();
                    params.put("username", username);
                    params.put("password", password);

                    //returing the response
                    return requestHandler.sendPostRequest(Constants.URL_LOGIN, params);*//*
                    URL url = new URL(urlWebService);
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
     //   names = new ArrayList<>();
        for (int i = 0; i < jsonArray.length(); i++) {
            JSONObject obj = jsonArray.getJSONObject(i);
//            stocks[i] = obj.getInt("id") + ")" + obj.getString("question") + "\n" + obj.getString("option_one") + "\n" + obj.getString("option_two") + "\n" + obj.getString("option_three") + "\n" + obj.getString("option_four") + "\n" + obj.getString("correct_option");
            //  cQuestions cQuestions = new cQuestions()

            stocks[i] = obj.getString("name");
          *//*  textArray.append(stocks1[i]);
            textArray.append("\n");*//*
            //   textArray.getText().toString();
            Log.i("tagconvertstr11111", "[" + String.valueOf(stocks) + "]");

            names.add(obj.getString("name"));

            searchData.add(new SearchData(
                    obj.getString("name")
            ));

            //storing the user in shared preferences
            // Subjects.SharedPrefManager1.getInstance(getApplicationContext()).Question(cquestions);


        }
        // names = new ArrayList<String>(Arrays.asList(stocks));

        adapter1 = new SearchAdapter(names,HomePage.this);

        // recyclerView2.setAdapter(adapter);
        //  SearchAdapter adapter1 = new SearchAdapter(HomePage.this, searchData);


      *//*  SearchAdapter adapter = new SearchAdapter(MainActivity3.this, searchData);

        recyclerView.setAdapter(adapter);*//*


        editText = (EditText) findViewById(R.id.editTextTextPersonName);

        editText.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                recyclerView2.setAdapter(adapter1);
                recyclerView.setVisibility(View.INVISIBLE);
            }
        });

        editText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {
                //after the change calling the method and passing the search input
                filter(editable.toString());
            }
        });

    }


    private void filter(String text) {
        //new array list that will hold the filtered data
        ArrayList<String> filterdNames = new ArrayList<>();

        //looping through existing elements
        for (String s : names) {
            //if the existing elements contains the search input
            if (s.toLowerCase().contains(text.toLowerCase())) {
                //adding the element to filtered list
                filterdNames.add(s);
            }
        }
       adapter1.filterList(filterdNames);

        //calling a method of the adapter class and passing the filtered list

    }*/

    {
        super.onRestart();
        finish();
        startActivity(getIntent());
    }


}