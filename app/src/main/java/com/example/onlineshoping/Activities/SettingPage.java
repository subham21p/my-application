package com.example.onlineshoping.Activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.core.app.ActivityCompat;
import androidx.drawerlayout.widget.DrawerLayout;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.provider.MediaStore;
import android.text.Html;
import android.text.TextUtils;
import android.util.Base64;
import android.util.Log;
import android.view.Gravity;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
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

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;

import static com.example.onlineshoping.ModelClasses.Api.ROOT_URL2;
import static com.example.onlineshoping.ModelClasses.Api.UPLOAD_URL;
import static com.example.onlineshoping.ModelClasses.Api.URL_DELETE;
import static com.example.onlineshoping.ModelClasses.Api.URL_IMAGE_SHOW;
import static com.example.onlineshoping.ModelClasses.Api.URL_LOGIN;
import static com.example.onlineshoping.ModelClasses.Api.URL_SHOWCART;
import static com.example.onlineshoping.ModelClasses.Api.URL_UPDATE_PROFILE;


public class SettingPage extends AppCompatActivity {

    ImageView imageView, imageViewEditPic, imageViewEditName, imageViewEditPhone;
    EditText editTextName, editTextEmail, editTextPhone;
    CardView cardView;
    NetworkImageView networkImageViewSP;
    private ImageLoader imageLoader;

    Button buttonSave, buttonUpdate;
    private int PICK_IMAGE_REQUEST = 1;

    private Button buttonChoose;
    private Button buttonUpload;
    private Button buttonView;

    private Bitmap bitmap;

    private Uri filePath;


    TextView textViewCurrentLoc, textViewName,textViewAppName, textViewAddCartCount, textViewProducts, textViewTotalPrice, textViewCurrAddress;
    DrawerLayout drawerLayout;
    NavigationView navigationView;
    ImageView imageViewMenu,imageViewSearch, imageViewProfilePic, imageViewAddCart;
    RelativeLayout relativeLayoutAddCart;
    NetworkImageView networkImageView;
    FusedLocationProviderClient fusedLocationProviderClient;




    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_setting_page);

        if (!SharedPrefManager.getInstance(this).isLoggedIn()) {
            finish();
            startActivity(new Intent(this, LoginPage.class));
        }


        User user = SharedPrefManager.getInstance(this).getUser();

        ProfilePic profilePic = SharedPrefManager.getInstance(this).getProfilePic();


        networkImageViewSP = (NetworkImageView) findViewById(R.id.networkImageViewSP);
        imageView = (ImageView)findViewById(R.id.imageViewSP);
        cardView = (CardView)findViewById(R.id.cardViewSP);
        buttonSave = (Button)findViewById(R.id.buttonSaveSP);
        buttonUpdate = (Button)findViewById(R.id.buttonUpdateSP);
        imageViewEditName = (ImageView)findViewById(R.id.imageViewEditNameSP);
        imageViewEditPic = (ImageView)findViewById(R.id.imageViewEditPicSP);
        imageViewEditPhone = (ImageView)findViewById(R.id.imageViewEditPhoneSP);
        editTextName = (EditText)findViewById(R.id.editTextTextPersonNameSP);
        editTextEmail = (EditText)findViewById(R.id.editTextTextEmailSP);
        editTextPhone = (EditText)findViewById(R.id.editTextPhoneSP);

        getProfilePic(user.getEmail());

        editTextName.setText(user.getName());
        editTextEmail.setText(user.getEmail());
        editTextPhone.setText(user.getPhone());

        editTextName.setEnabled(false);
        editTextEmail.setEnabled(false);
        editTextPhone.setEnabled(false);

        imageViewEditPic.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showFileChooser();
            }
        });

        imageViewEditName.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                editTextName.setEnabled(true);
            }
        });

        imageViewEditPhone.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                editTextPhone.setEnabled(true);
            }
        });



        networkImageViewSP.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showFileChooser();
            }
        });

        buttonSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                imageDelete(user.getEmail());
                uploadImage(user.getEmail());
            }
        });

        buttonUpdate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                updateProfile(user.getEmail(),editTextName.getText().toString().trim(),editTextPhone.getText().toString().trim());
            }
        });




        drawerLayout = (DrawerLayout) findViewById(R.id.drawerLayoutSP);
        navigationView = (NavigationView) findViewById(R.id.navigationViewSP);
        imageViewMenu = (ImageView) findViewById(R.id.imageViewMenuSP);


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
        textViewAddCartCount = (TextView)findViewById(R.id.textViewCartCountSP);
        relativeLayoutAddCart = (RelativeLayout)findViewById(R.id.relativeLayoutCartCountSP);


        // For Location
        fusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(this);


        if (ActivityCompat.checkSelfPermission(SettingPage.this, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
            getLocation();
        } else {
            ActivityCompat.requestPermissions(SettingPage.this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, 44);
        }


        navigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                int id = item.getItemId();
                switch(id)
                {
                    case R.id.nvAboutUs:
                        Toast.makeText(SettingPage.this, "About Us", Toast.LENGTH_SHORT).show();
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
                        Toast.makeText(SettingPage.this, "Logout", Toast.LENGTH_SHORT).show();
                    default:
                        return true;
                }


            }
        });


        textViewAppName = (TextView)findViewById(R.id.textViewAppNameSP);

        textViewAppName.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getApplicationContext(),HomePage.class));
            }
        });

        imageViewSearch = (ImageView)findViewById(R.id.imageViewSearchSP);

        imageViewSearch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(SettingPage.this,SearchPage.class));
            }
        });

        // for add cart page

        imageViewAddCart = (ImageView)findViewById(R.id.imageViewCartSP);

        imageViewAddCart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getApplicationContext(),AddCartList.class));
            }
        });






    }

   private void showFileChooser() {
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(Intent.createChooser(intent, "Select Picture"), PICK_IMAGE_REQUEST);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == PICK_IMAGE_REQUEST && resultCode == RESULT_OK && data != null && data.getData() != null) {

            filePath = data.getData();
            try {
                bitmap = MediaStore.Images.Media.getBitmap(getContentResolver(), filePath);
                imageView.setVisibility(View.VISIBLE);
                networkImageViewSP.setVisibility(View.INVISIBLE);
                imageView.setImageBitmap(bitmap);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    public String getStringImage(Bitmap bmp){
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        bmp.compress(Bitmap.CompressFormat.JPEG, 100, baos);
        byte[] imageBytes = baos.toByteArray();
        String encodedImage = Base64.encodeToString(imageBytes, Base64.DEFAULT);
        return encodedImage;
    }

    private void uploadImage(String email){
        class UploadImage extends AsyncTask<Bitmap,Void,String> {

            ProgressDialog loading;
            RequestHandler rh = new RequestHandler();

            @Override
            protected void onPreExecute() {
                super.onPreExecute();
                loading = ProgressDialog.show(SettingPage.this, "Uploading...", null,true,true);
            }

            @Override
            protected void onPostExecute(String s) {
                super.onPostExecute(s);
                loading.dismiss();
                Toast.makeText(getApplicationContext(),s, Toast.LENGTH_LONG).show();
            }

            @Override
            protected String doInBackground(Bitmap... params) {
                Bitmap bitmap = params[0];
                String uploadImage = getStringImage(bitmap);

                HashMap<String,String> data = new HashMap<>();

                data.put("image", uploadImage);
                data.put("email",email);
                String result = rh.sendPostRequest(ROOT_URL2,data);

                ProfilePic profilePic = new ProfilePic(
                       uploadImage
                );

                SharedPrefManager.getInstance(getApplicationContext()).profilePic(profilePic);

                return result;
            }
        }

        UploadImage ui = new UploadImage();
        ui.execute(bitmap);
    }


    private void imageDelete(String email){

        class UserLogin extends AsyncTask<Void, Void, String> {


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
               // params.put("subject", subject);

                //returing the response
                return requestHandler.sendPostRequest(URL_DELETE, params);
            }
        }

        UserLogin ul = new UserLogin();
        ul.execute();


    }



    private void getProfilePic(String email) {
        //if everything is fine

        class GetProfilePic extends AsyncTask<Void, Void, String> {

            @Override
            protected void onPreExecute() {
                super.onPreExecute();
            }

            @Override
            protected void onPostExecute(String s) {
                super.onPostExecute(s);
                Log.i("imagesssssss", "[" + s + "]");

                try {
                    //converting response to json object
                    JSONObject obj = new JSONObject(s);

                    //if no error in response
                    if (!obj.getBoolean("error")) {
                        Toast.makeText(getApplicationContext(), obj.getString("message"), Toast.LENGTH_SHORT).show();

                        //getting the user from the response
                        JSONObject userJson = obj.getJSONObject("pic");


                        imageView.setVisibility(View.INVISIBLE);

                        imageLoader = CustomVolleyRequest.getInstance(getApplicationContext()).getImageLoader();
                        imageLoader.get(userJson.getString("image"), ImageLoader.getImageListener(networkImageViewSP, R.drawable.ic_launcher_background, android.R.drawable.ic_dialog_alert));

                        networkImageViewSP.setImageUrl(userJson.getString("image"),imageLoader);


                        Log.i("imagesssssss", "[" + userJson.getString("image") + "]");

                    } else {
                      //  progressDialog.dismiss();
                        imageView.setVisibility(View.VISIBLE);
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
              //  params.put("password", password);

                //returing the response
                return requestHandler.sendPostRequest(URL_IMAGE_SHOW, params);
            }
        }

        GetProfilePic ul = new GetProfilePic();
        ul.execute();
    }


    private void updateProfile(String email,String name,String phone){

        class UpdateProfile extends AsyncTask<Void, Void, String> {

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
                        User user = new User(
                                userJson.getString("name"),
                                userJson.getString("gender"),
                                userJson.getString("email"),
                                userJson.getString("phone")
                        );

                        //storing the user in shared preferences
                        SharedPrefManager.getInstance(getApplicationContext()).userLogin(user);

                    } else {
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
                params.put("name", name);
                params.put("phone", phone);
                // params.put("subject", subject);

                //returing the response
                return requestHandler.sendPostRequest(URL_UPDATE_PROFILE, params);
            }
        }

        UpdateProfile ul = new UpdateProfile();
        ul.execute();


    }


    @SuppressLint("MissingPermission")
    private void getLocation() {


        fusedLocationProviderClient.getLastLocation().addOnCompleteListener(new OnCompleteListener<Location>() {
            @Override
            public void onComplete(@NonNull Task<Location> task) {

                Location location = task.getResult();

                if (location != null) {


                    try {
                        Geocoder geocoder = new Geocoder(SettingPage.this, Locale.getDefault());

                        List<Address> addresses = geocoder.getFromLocation(location.getLatitude(), location.getLongitude(), 1);
                        textViewCurrentLoc.setText(Html.fromHtml("<font color='#000000'></font>" + addresses.get(0).getLocality() + ", " + addresses.get(0).getPostalCode()));
                        // For full address
                       // textViewCurrAddress.setText(addresses.get(0).getAddressLine(0));
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



}