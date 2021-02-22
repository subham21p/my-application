package com.example.onlineshoping.Activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.drawerlayout.widget.DrawerLayout;

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
import android.text.TextUtils;
import android.util.Log;
import android.view.Gravity;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.toolbox.ImageLoader;
import com.android.volley.toolbox.NetworkImageView;
import com.example.onlineshoping.Adapters.AddCartAdapter;
import com.example.onlineshoping.ModelClasses.AddCartClass;
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

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;

import static com.example.onlineshoping.ModelClasses.Api.URL_BUY;
import static com.example.onlineshoping.ModelClasses.Api.URL_DELETE;
import static com.example.onlineshoping.ModelClasses.Api.URL_DELETE_ADDCART_AFTER_BUY;
import static com.example.onlineshoping.ModelClasses.Api.URL_LOGIN;
import static com.example.onlineshoping.ModelClasses.Api.URL_SHOWCART;

public class BuyPage extends AppCompatActivity {

    TextView textViewCurrentLoc, textViewName,textViewAppName, textViewAddCartCount, textViewProducts, textViewTotalPrice, textViewCurrAddress;
    DrawerLayout drawerLayout;
    NavigationView navigationView;
    ImageView imageViewMenu,imageViewSearch, imageViewProfilePic, imageViewAddCart;
    RelativeLayout relativeLayoutAddCart;
    NetworkImageView networkImageView;
    FusedLocationProviderClient fusedLocationProviderClient;
    private ImageLoader imageLoader;


    EditText editTextOtherAddress;
    Button buttonBuy;
    RadioGroup radioGroup;
    RadioButton radioButton1,radioButton2;

    boolean currentAddress = true;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_buy_page);

        if (!SharedPrefManager.getInstance(this).isLoggedIn()) {
            finish();
            startActivity(new Intent(this, LoginPage.class));
        }


        User user = SharedPrefManager.getInstance(this).getUser();

        ProfilePic profilePic = SharedPrefManager.getInstance(this).getProfilePic();

        textViewTotalPrice = (TextView)findViewById(R.id.textViewTotalPriceBP);
        textViewCurrAddress = (TextView)findViewById(R.id.textViewCurrentAddBP);
        editTextOtherAddress = (EditText)findViewById(R.id.editTextOtherAddBP);
        editTextOtherAddress.setVisibility(View.INVISIBLE);
        buttonBuy = (Button)findViewById(R.id.buttonBuyBP);
        radioGroup = (RadioGroup)findViewById(R.id.radioGroupBP);
        radioButton1 = (RadioButton)findViewById(R.id.radioButton1BP);
        radioButton1.setChecked(true);
        radioButton2 = (RadioButton)findViewById(R.id.radioButton2BP);



        radioButton1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                currentAddress = true;
                onRadioButtonClicked(v);
            }
        });

        radioButton2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                currentAddress = false;
                onRadioButtonClicked(v);
            }
        });

        // get products
        getProducts(user.getEmail());

        buttonBuy.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                  onBuyClick(user.getEmail());
                  deleteAddCartListAfterBuy(user.getEmail());
                startActivity(new Intent(getApplicationContext(),OrdersPage.class));
            }
        });


        drawerLayout = (DrawerLayout) findViewById(R.id.drawerLayoutBP);
        navigationView = (NavigationView) findViewById(R.id.navigationViewBP);
        imageViewMenu = (ImageView) findViewById(R.id.imageViewMenuBP);


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
        textViewAddCartCount = (TextView)findViewById(R.id.textViewCartCountBP);
        relativeLayoutAddCart = (RelativeLayout)findViewById(R.id.relativeLayoutCartCountBP);


        // For Location
        fusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(this);


        if (ActivityCompat.checkSelfPermission(BuyPage.this, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
            getLocation();
        } else {
            ActivityCompat.requestPermissions(BuyPage.this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, 44);
        }


        navigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                int id = item.getItemId();
                switch(id)
                {
                    case R.id.nvAboutUs:
                        Toast.makeText(BuyPage.this, "About Us", Toast.LENGTH_SHORT).show();
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
                        Toast.makeText(BuyPage.this, "Logout", Toast.LENGTH_SHORT).show();
                    default:
                        return true;
                }


            }
        });


        textViewAppName = (TextView)findViewById(R.id.textViewAppNameBP);

        textViewAppName.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getApplicationContext(),HomePage.class));
            }
        });

        imageViewSearch = (ImageView)findViewById(R.id.imageViewSearchBP);

        imageViewSearch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(BuyPage.this,SearchPage.class));
            }
        });

        // for add cart page

        imageViewAddCart = (ImageView)findViewById(R.id.imageViewCartBP);

        imageViewAddCart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getApplicationContext(),AddCartList.class));
            }
        });



    }



    public void onRadioButtonClicked(View view) {
        boolean checked = ((RadioButton) view).isChecked();
        // Check which radio button was clicked
        switch(view.getId()) {
            case R.id.radioButton1BP:
                if(checked){
                    editTextOtherAddress.setVisibility(View.INVISIBLE);
                    textViewCurrAddress.setVisibility(View.VISIBLE);
                    }
                break;
            case R.id.radioButton2BP:
                if(checked){
                    editTextOtherAddress.setVisibility(View.VISIBLE);
                    textViewCurrAddress.setVisibility(View.INVISIBLE);
                    }
                break;
        }

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
                        Geocoder geocoder = new Geocoder(BuyPage.this, Locale.getDefault());

                        List<Address> addresses = geocoder.getFromLocation(location.getLatitude(), location.getLongitude(), 1);
                        textViewCurrentLoc.setText(Html.fromHtml("<font color='#000000'></font>" + addresses.get(0).getLocality() + ", " + addresses.get(0).getPostalCode()));
                        // For full address
                        textViewCurrAddress.setText(addresses.get(0).getAddressLine(0));
                        Log.i("location", "[" + addresses.get(0).getAddressLine(0) + "]");
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }
        });

    }



    private void getProducts(final String email) {

        class GetProducts extends AsyncTask<Void, Void, String> {

            @Override
            protected void onPreExecute() {
                super.onPreExecute();
            }


            @Override
            protected void onPostExecute(String s) {
                super.onPostExecute(s);

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

                return requestHandler.sendPostRequest(URL_SHOWCART, params);
            }
        }
        GetProducts getJSON = new GetProducts();
        getJSON.execute();

    }

    private void loadIntoListView(String json) throws JSONException {
        Log.i("dfdfdfdfd", "[" + json+ "]");

        JSONArray jsonArray = new JSONArray(json);

        LinearLayout linearLayout = findViewById(R.id.relativeLayoutDetailBP);

        String[] stocks = new String[jsonArray.length()];
        int a = 0, b=0;
        for (int i = 0; i < jsonArray.length(); i++) {
            JSONObject obj = jsonArray.getJSONObject(i);

             a = obj.getInt("price") * obj.getInt("nos_of_products");

            Log.i("product", "[" + a + "]");

             b= b+a;
            Log.i("sum", "[" + obj.getString("image") + "]");


            textViewProducts = new TextView(getApplicationContext());
            textViewProducts.setLayoutParams(new LinearLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT));
            textViewProducts.setTextSize(20);
            textViewProducts.setGravity(Gravity.LEFT);
            textViewProducts.setText(obj.getString("brand")+" "+obj.getString("name")+" = "+"Rs. "+obj.getString("price")+" * "+obj.getString("nos_of_products"));
            linearLayout.addView(textViewProducts);

            textViewTotalPrice.setText("Rs. "+b);


        }

    }


    private void onBuyClick(final String email) {

        class OnBuyClick extends AsyncTask<Void, Void, String> {

            @Override
            protected void onPreExecute() {
                super.onPreExecute();
            }


            @Override
            protected void onPostExecute(String s) {
                super.onPostExecute(s);

                try {
                    loadIntoListView2(s);

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
        OnBuyClick getJSON = new OnBuyClick();
        getJSON.execute();

    }

    private void loadIntoListView2(String json) throws JSONException {
        Log.i("dfdfdfdfd", "[" + json+ "]");

        JSONArray jsonArray = new JSONArray(json);
        User user = SharedPrefManager.getInstance(this).getUser();
        int a = 0, b=0;

        // for current date
        Calendar calendar = Calendar.getInstance();
        Date c = calendar.getTime();
        SimpleDateFormat df = new SimpleDateFormat("dd/MM/yyyy");
        String current_Date = df.format(c);
        calendar.add(Calendar.DAY_OF_YEAR, 2);
        c = calendar.getTime();
        String delivery_date = df.format(c);


        // for address select check
        String address = "";

        if (currentAddress == true){
            address =  textViewCurrAddress.getText().toString().trim();
            Log.i("CurrentAddress1", "[" + address + "]");
        }
        else if(currentAddress == false){
            address = editTextOtherAddress.getText().toString().trim();
            Log.i("CurrentAddress2", "[" + address + "]");
            if (TextUtils.isEmpty(address)) {
                editTextOtherAddress.setError("Please enter your Address");
                editTextOtherAddress.requestFocus();
                return;
            }
        }


        for (int i = 0; i < jsonArray.length(); i++) {
            JSONObject obj = jsonArray.getJSONObject(i);

            // calculation of prices
            a = obj.getInt("price") * obj.getInt("nos_of_products");

            b= b+a;

            textViewTotalPrice.setText("Rs. "+b);


           buy(user.getEmail(),obj.getString("image"),obj.getInt("items_id"),obj.getInt("nos_of_products"),obj.getString("category"),obj.getString("name"),obj.getString("brand"), current_Date, delivery_date,obj.getString("quantity"), a, address);


        }

    }




    private void buy(String email, String image, int itemes_id, int nos_of_product, String category, String name, String brand, String order_date, String delivery_date, String quantity, int price, String delivery_address) {

        //if everything is fine

        class Buy extends AsyncTask<Void, Void, String> {

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


                    } else {
                      //  progressDialog.dismiss();
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
                params.put("email", email);
                params.put("image", image);
                params.put("items_id", String.valueOf(itemes_id));
                params.put("nos_of_products", String.valueOf(nos_of_product));
                params.put("category", category);
                params.put("name", name);
                params.put("brand", brand);
                params.put("order_date", order_date);
                params.put("delivery_date", delivery_date);
                params.put("quantity", quantity);
                params.put("price", String.valueOf(price));
                params.put("delivery_address",delivery_address);

                //returing the response
                return requestHandler.sendPostRequest(URL_BUY, params);
            }
        }

        Buy ul = new Buy();
        ul.execute();
    }



    private void deleteAddCartListAfterBuy(String email) {

        //if everything is fine

        class DeleteAddCartListAfterBuy extends AsyncTask<Void, Void, String> {

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

                    } else {
                        //  progressDialog.dismiss();
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
                params.put("email", email);

                //returing the response
                return requestHandler.sendPostRequest(URL_DELETE_ADDCART_AFTER_BUY, params);
            }
        }

        DeleteAddCartListAfterBuy ul = new DeleteAddCartListAfterBuy();
        ul.execute();
    }






}