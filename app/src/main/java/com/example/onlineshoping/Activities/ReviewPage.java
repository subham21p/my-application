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
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.toolbox.ImageLoader;
import com.android.volley.toolbox.NetworkImageView;
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

import static com.example.onlineshoping.ModelClasses.Api.URL_CHECK_COMMENT;
import static com.example.onlineshoping.ModelClasses.Api.URL_DELETE_ADDCART_AFTER_BUY;
import static com.example.onlineshoping.ModelClasses.Api.URL_DELETE_COMMENT;
import static com.example.onlineshoping.ModelClasses.Api.URL_REGISTER;
import static com.example.onlineshoping.ModelClasses.Api.URL_REVIEW;
import static com.example.onlineshoping.ModelClasses.Api.URL_SHOWCART;
import static com.example.onlineshoping.ModelClasses.Api.URL_UPDATE_COMMENT;

public class ReviewPage extends AppCompatActivity {

    EditText editTextReview, editTextRating;
    Button buttonSubmit, buttonUpdate, buttonDelete;


    TextView textViewCurrentLoc, textViewName, textViewAddCartCount, textViewAppName;
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
        setContentView(R.layout.activity_review_page);

        if (!SharedPrefManager.getInstance(this).isLoggedIn()) {
            finish();
            startActivity(new Intent(this, LoginPage.class));
        }

        editTextReview = (EditText)findViewById(R.id.editTextTextReviewRVP);
        editTextRating = (EditText)findViewById(R.id.editTextRatingRVP);
        buttonSubmit = (Button)findViewById(R.id.buttonSubmitRVP);
        buttonUpdate = (Button)findViewById(R.id.buttonUpdateRVP);
        buttonDelete = (Button)findViewById(R.id.buttonDeleteRVP);

        buttonDelete.setVisibility(View.INVISIBLE);
        buttonUpdate.setVisibility(View.INVISIBLE);
        buttonSubmit.setVisibility(View.VISIBLE);

        User user = SharedPrefManager.getInstance(this).getUser();

        ProfilePic profilePic = SharedPrefManager.getInstance(this).getProfilePic();


        Intent intent = getIntent();
        int itemId = intent.getIntExtra("itemId",0);

        // check comment
        checkComment(user.getEmail(),itemId);

        Calendar calendar = Calendar.getInstance();
        Date c = calendar.getTime();
        SimpleDateFormat df = new SimpleDateFormat("dd/MM/yyyy");
        String current_Date = df.format(c);

        buttonSubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String rating = editTextRating.getText().toString().trim();
                String comm = editTextReview.getText().toString().trim();

                if (TextUtils.isEmpty(comm)) {
                    editTextReview.setError("Please enter your Comment");
                    editTextReview.requestFocus();
                    return;
                }

                if (TextUtils.isEmpty(rating)) {
                    editTextRating.setError("Please give rate");
                    editTextRating.requestFocus();
                    return;
                }

                float rate = Float.parseFloat(editTextRating.getText().toString().trim());
                Log.i("rate2", "[" + rate+ "]");
                if (rate > 5.0){
                    Log.i("rate1", "[" + rate+ "]");
                    editTextRating.setError("Please rate out of 5");
                    editTextRating.requestFocus();
                    return;
                }

                addReview(profilePic.getProfilePic(),user.getEmail(),user.getName(),itemId, Float.parseFloat(editTextRating.getText().toString().trim()),editTextReview.getText().toString().trim(),current_Date);
                finish();
            }
        });

        buttonUpdate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                String rating = editTextRating.getText().toString().trim();
                String comm = editTextReview.getText().toString().trim();

                if (TextUtils.isEmpty(comm)) {
                    editTextReview.setError("Please enter your Comment");
                    editTextReview.requestFocus();
                    return;
                }

                if (TextUtils.isEmpty(rating)) {
                    editTextRating.setError("Please give rate");
                    editTextRating.requestFocus();
                    return;
                }

                float rate = Float.parseFloat(editTextRating.getText().toString().trim());
                Log.i("rate2", "[" + rate+ "]");
                if (rate > 5.0){
                    Log.i("rate1", "[" + rate+ "]");
                    editTextRating.setError("Please rate out of 5");
                    editTextRating.requestFocus();
                    return;
                }

                updateComment(user.getEmail(),user.getName(),itemId, Float.parseFloat(editTextRating.getText().toString().trim()),editTextReview.getText().toString().trim(),current_Date);
                finish();
            }
        });

        buttonDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                deleteComment(user.getEmail(),itemId);
                finish();

            }
        });




        drawerLayout = (DrawerLayout) findViewById(R.id.drawerLayoutRVP);
        navigationView = (NavigationView) findViewById(R.id.navigationViewRVP);
        imageViewMenu = (ImageView) findViewById(R.id.imageViewMenuRVP);


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
        textViewAddCartCount = (TextView)findViewById(R.id.textViewCartCountRVP);
        relativeLayoutAddCart = (RelativeLayout)findViewById(R.id.relativeLayoutCartCountRVP);

        // For Location
        fusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(this);


        if (ActivityCompat.checkSelfPermission(ReviewPage.this, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
            getLocation();
        } else {
            ActivityCompat.requestPermissions(ReviewPage.this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, 44);
        }


        navigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                int id = item.getItemId();
                switch(id)
                {
                    case R.id.nvAboutUs:
                        Toast.makeText(ReviewPage.this, "About Us", Toast.LENGTH_SHORT).show();
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
                        Toast.makeText(ReviewPage.this, "Logout", Toast.LENGTH_SHORT).show();
                    default:
                        return true;
                }


            }
        });


        textViewAppName = (TextView)findViewById(R.id.textViewAppNameRVP);

        textViewAppName.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getApplicationContext(),HomePage.class));
            }
        });

        // for search page
        imageViewSearch = (ImageView)findViewById(R.id.imageViewSearchRVP);

        imageViewSearch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(ReviewPage.this,SearchPage.class));
            }
        });

        // for add cart page

        imageViewAddCart = (ImageView)findViewById(R.id.imageViewCartRVP);

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
                        Geocoder geocoder = new Geocoder(ReviewPage.this, Locale.getDefault());

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






    @SuppressLint("LongLogTag")
    private void addReview(String profile_pic, String email, String name, int items_id, float rating, String comment, String date) {
        //if it passes all the validations

        class AddReview extends AsyncTask<Void, Void, String> {

            //  private ProgressBar progressBar;



            @Override
            protected void onPreExecute() {
                super.onPreExecute();

            }

            @Override
            protected void onPostExecute(String s) {
                super.onPostExecute(s);
                //hiding the progressbar after completion
                //   progressBar.setVisibility(View.GONE);

                Log.i("sfdfdfjggoifhgofdhg", "[" + s+ "]");

                try {
                    //converting response to json object
                    JSONObject obj = new JSONObject(s);

                    //if no error in response
                    if (!obj.getBoolean("error")) {
                        Toast.makeText(getApplicationContext(), obj.getString("message"), Toast.LENGTH_SHORT).show();

                        JSONObject userJson = obj.getJSONObject("user");
                    } else {
                     //   progressDialog.dismiss();
                        Toast.makeText(getApplicationContext(), obj.getString("message"), Toast.LENGTH_SHORT).show();
                        //Toast.makeText(getApplicationContext(), "Some error occurred", Toast.LENGTH_LONG).show();
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
                params.put("profile_pic",profile_pic);
                params.put("email",email);
                params.put("name",name);
                params.put("items_id", String.valueOf(items_id));
                params.put("rating", String.valueOf(rating));
                params.put("comment",comment);
                params.put("date",date);

                //returing the response
                return requestHandler.sendPostRequest(URL_REVIEW, params);
            }
        }

        //executing the async task
        AddReview ru = new AddReview();
        ru.execute();

    }



    private void checkComment(String email, int items_id) {

        class CheckComment extends AsyncTask<Void, Void, String> {

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
                params.put("items_id", String.valueOf(items_id));

                return requestHandler.sendPostRequest(URL_CHECK_COMMENT, params);
            }
        }
        CheckComment getJSON = new CheckComment();
        getJSON.execute();

    }

    private void loadIntoListView(String json) throws JSONException {
        Log.i("dfdfdfdfd", "[" + json+ "]");

        JSONArray jsonArray = new JSONArray(json);

        if (jsonArray.length() > 0){

            buttonDelete.setVisibility(View.VISIBLE);
            buttonUpdate.setVisibility(View.VISIBLE);
            buttonSubmit.setVisibility(View.INVISIBLE);

            for (int i = 0; i < jsonArray.length(); i++) {
                JSONObject obj = jsonArray.getJSONObject(i);


                String rating = String.valueOf(obj.getDouble("rating"));

                editTextReview.setText(obj.getString("comment"));
                editTextRating.setText(rating);

               // Log.i("sum", "[" + obj.getString("image") + "]");

            }

        }

    }



    private void deleteComment(String email, int items_id) {

        //if everything is fine

        class DeleteComment extends AsyncTask<Void, Void, String> {

            // ProgressBar progressBar;


            @Override
            protected void onPreExecute() {
                super.onPreExecute();
                // progressBar = (ProgressBar) findViewById(R.id.progressBar2);
//                progressBar.setVisibility(View.VISIBLE);

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
                params.put("items_id", String.valueOf(items_id));

                //returing the response
                return requestHandler.sendPostRequest(URL_DELETE_COMMENT, params);
            }
        }

        DeleteComment ul = new DeleteComment();
        ul.execute();
    }



    private void updateComment(String email, String name, int items_id, float rating, String comment, String date) {

        //if everything is fine

        class UpdateComment extends AsyncTask<Void, Void, String> {

            // ProgressBar progressBar;


            @Override
            protected void onPreExecute() {
                super.onPreExecute();
                // progressBar = (ProgressBar) findViewById(R.id.progressBar2);
//                progressBar.setVisibility(View.VISIBLE);

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
                params.put("name", name);
                params.put("items_id", String.valueOf(items_id));
                params.put("rating", String.valueOf(rating));
                params.put("comment", comment);
                params.put("date", date);

                //returing the response
                return requestHandler.sendPostRequest(URL_UPDATE_COMMENT, params);
            }
        }

        UpdateComment ul = new UpdateComment();
        ul.execute();
    }





}