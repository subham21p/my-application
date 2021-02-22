package com.example.onlineshoping.Activities;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.text.method.PasswordTransformationMethod;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.onlineshoping.ModelClasses.RequestHandler;
import com.example.onlineshoping.ModelClasses.SharedPrefManager;
import com.example.onlineshoping.ModelClasses.User;
import com.example.onlineshoping.R;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;

import static com.example.onlineshoping.ModelClasses.Api.URL_UPDATE_PASSWORD;
import static com.example.onlineshoping.ModelClasses.Api.URL_UPDATE_PROFILE;

public class ForgetPassword extends AppCompatActivity {

    EditText editTextPassword, editTextConfirmPassword;
    TextView textViewBack;
    ImageView imageViewHidePass, imageViewHideConPass;
 //   Button button;
    Button button;
    int a=0,b=0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_forget_password);

        editTextPassword = (EditText)findViewById(R.id.editTextTextPasswordFP);
        editTextConfirmPassword = (EditText)findViewById(R.id.editTextTextPasswordConfirmFP);
        textViewBack = (TextView)findViewById(R.id.textViewBackToLoginFP);
        imageViewHidePass = (ImageView)findViewById(R.id.imageViewHidePassFP);
        imageViewHideConPass = (ImageView)findViewById(R.id.imageViewHidePassConfirmFP);
        button = (Button)findViewById(R.id.buttonFP);

        imageViewHidePass.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (a == 0) {
                    editTextPassword.setTransformationMethod(null);
                    imageViewHidePass.setImageResource(R.drawable.show_pass);
                    a++;
                }
                else {
                    editTextPassword.setTransformationMethod(new PasswordTransformationMethod());
                    imageViewHidePass.setImageResource(R.drawable.hide_pass);
                    a--;
                }
            }
        });


        imageViewHideConPass.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (b == 0) {
                    editTextConfirmPassword.setTransformationMethod(null);
                    imageViewHideConPass.setImageResource(R.drawable.show_pass);
                    b++;
                }
                else {
                    editTextConfirmPassword.setTransformationMethod(new PasswordTransformationMethod());
                    imageViewHideConPass.setImageResource(R.drawable.hide_pass);
                    b--;
                }
            }
        });


        editTextConfirmPassword.addTextChangedListener(new TextWatcher() {
            public void afterTextChanged(Editable s) {
                String strPass1 = editTextPassword.getText().toString();
                String strPass2 = editTextConfirmPassword.getText().toString();
                if (strPass1.equals(strPass2)) {
                   // error.setText(R.string.settings_pwd_equal);

                } else {
                    editTextConfirmPassword.setError("Password Mismatch");
                   // error.setText(R.string.settings_pwd_not_equal);
                }
            }

            public void beforeTextChanged(CharSequence s, int start, int count, int after) {}
            public void onTextChanged(CharSequence s, int start, int before, int count) {}
        });


        textViewBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        Intent intent = getIntent();
        String email = intent.getStringExtra("email");

       button.setOnClickListener(new View.OnClickListener() {
           @Override
           public void onClick(View v) {
               updatePassword(email,editTextPassword.getText().toString());
               finish();
           }
       });

    }




    private void updatePassword(String email,String password){

        if (editTextPassword.getText().toString().isEmpty()){
            editTextPassword.setError("Please create your password");
            editTextPassword.requestFocus();
            return;
        }

        if (editTextConfirmPassword.getText().toString().isEmpty()){
            editTextConfirmPassword.setError("Please confirm your password");
            editTextConfirmPassword.requestFocus();
            return;
        }


        class UpdatePassword extends AsyncTask<Void, Void, String> {

            // ProgressBar progressBar;


            @Override
            protected void onPreExecute() {
                super.onPreExecute();
                // progressBar = (ProgressBar) findViewById(R.id.progressBar2);
                //  progressBar.setVisibility(View.VISIBLE);
            }

            @Override
            protected void onPostExecute(String s) {
                super.onPostExecute(s);
                //     progressBar.setVisibility(View.GONE);


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
                params.put("password", password);
               // params.put("phone", phone);
                // params.put("subject", subject);

                //returing the response
                return requestHandler.sendPostRequest(URL_UPDATE_PASSWORD, params);
            }
        }

        UpdatePassword ul = new UpdatePassword();
        ul.execute();
    }




}