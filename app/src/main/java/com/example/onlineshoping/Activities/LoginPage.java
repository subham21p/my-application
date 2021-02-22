package com.example.onlineshoping.Activities;

import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.text.TextUtils;
import android.text.method.PasswordTransformationMethod;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.example.onlineshoping.ModelClasses.RequestHandler;
import com.example.onlineshoping.ModelClasses.SharedPrefManager;
import com.example.onlineshoping.ModelClasses.User;
import com.example.onlineshoping.R;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;

import static com.example.onlineshoping.ModelClasses.Api.URL_LOGIN;

public class LoginPage extends AppCompatActivity {

    EditText editTextPassword, editTextEmail;
    ImageView imageViewPassShow;
    Button buttonLogin;
    TextView textViewFrgtPass, textViewRegPage;
    int a = 0;
    ProgressDialog progressDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login_page);

        // If user is Logged in
        if (SharedPrefManager.getInstance(this).isLoggedIn()){
            finish();
            startActivity(new Intent(this,HomePage.class));
            return;
        }


        editTextEmail = (EditText)findViewById(R.id.editTextTextEmailAddressLP);
        editTextPassword = (EditText)findViewById(R.id.editTextTextPasswordLP);
        imageViewPassShow = (ImageView)findViewById(R.id.imageViewHidePassLP);
        textViewFrgtPass = (TextView)findViewById(R.id.textViewFrgtPassLP);
        textViewRegPage = (TextView)findViewById(R.id.textViewGoToRegPL);
        buttonLogin = (Button)findViewById(R.id.buttonLoginLP);

        progressDialog = new ProgressDialog(this);

        textViewRegPage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(LoginPage.this,RegistrationPage.class));
            }
        });


        imageViewPassShow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (a == 0) {
                    editTextPassword.setTransformationMethod(null);
                    imageViewPassShow.setImageResource(R.drawable.show_pass);
                    a++;
                }
                else {
                    editTextPassword.setTransformationMethod(new PasswordTransformationMethod());
                    imageViewPassShow.setImageResource(R.drawable.hide_pass);
                    a--;
                }
            }
        });

        buttonLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                userLogin();
            }
        });


        textViewFrgtPass.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(LoginPage.this,GetEmail.class));
            }
        });

    }



    private void userLogin() {
        //first getting the values
        final String email = editTextEmail.getText().toString();
        final String password = editTextPassword.getText().toString();

        //validating inputs
        if (TextUtils.isEmpty(email)) {
            editTextEmail.setError("Please enter your email");
            editTextEmail.requestFocus();
            return;
        }

        if (TextUtils.isEmpty(password)) {
            editTextPassword.setError("Please enter your password");
            editTextPassword.requestFocus();
            return;
        }

        //if everything is fine

        class UserLogin extends AsyncTask<Void, Void, String> {

            // ProgressBar progressBar;


            @Override
            protected void onPreExecute() {
                super.onPreExecute();
                // progressBar = (ProgressBar) findViewById(R.id.progressBar2);
//                progressBar.setVisibility(View.VISIBLE);
                progressDialog.setMessage("Login...");
                progressDialog.show();
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
                        progressDialog.dismiss();
                        //starting the profile activity
                        finish();
                        startActivity(new Intent(getApplicationContext(), HomePage.class));
                    } else {
                        progressDialog.dismiss();
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
                params.put("password", password);

                //returing the response
                return requestHandler.sendPostRequest(URL_LOGIN, params);
            }
        }

        UserLogin ul = new UserLogin();
        ul.execute();
    }





}