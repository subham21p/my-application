package com.example.onlineshoping.Activities;

import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.text.TextUtils;
import android.text.method.HideReturnsTransformationMethod;
import android.text.method.PasswordTransformationMethod;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.example.onlineshoping.ModelClasses.RequestHandler;
import com.example.onlineshoping.ModelClasses.SharedPrefManager;
import com.example.onlineshoping.ModelClasses.User;
import com.example.onlineshoping.R;
import com.google.android.material.textfield.TextInputLayout;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;

import static com.example.onlineshoping.ModelClasses.Api.URL_REGISTER;

public class RegistrationPage extends AppCompatActivity {

    EditText editTextName;
    EditText editTextEmail;
    EditText editTextPhone;
    EditText editTextPassword;
    RadioGroup radioGroup;
    RadioButton radioButtonM, radioButtonF, radioButtonO;
    ImageView imageViewPassShow;
    Button buttonReg;
    TextInputLayout textInputLayout;
    TextView textViewGen, textViewBackToLoginRP;
    int a = 0;
    ProgressDialog progressDialog;
    String gen;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        java.text.DateFormat dateFormat = android.text.format.DateFormat.getDateFormat(getApplicationContext());
        setContentView(R.layout.activity_registration_page);

        editTextName = (EditText)findViewById(R.id.editTextTextPersonNameRP);
        editTextEmail = (EditText)findViewById(R.id.editTextTextEmailAddressRP);
        editTextPhone = (EditText)findViewById(R.id.editTextPhoneRP);
        editTextPassword = (EditText)findViewById(R.id.editTextTextPasswordRP);
        imageViewPassShow = (ImageView)findViewById(R.id.imageViewHidePassRP);
        radioGroup = (RadioGroup)findViewById(R.id.radioGroupRP);
        buttonReg = (Button)findViewById(R.id.buttonRegistrationRP);
        textViewGen = (TextView)findViewById(R.id.textViewGenderRP);
        radioButtonF = (RadioButton)findViewById(radioGroup.getCheckedRadioButtonId());
        textViewBackToLoginRP = (TextView)findViewById(R.id.textViewBackToLoginRP);

        progressDialog = new ProgressDialog(this);





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


        radioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId)
           {
               radioButtonM = (RadioButton) findViewById(checkedId);
               gen = radioButtonM.getText().toString();
               Toast.makeText(getBaseContext(), gen, Toast.LENGTH_SHORT).show(); }
        });

        buttonReg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                Log.i("image", "["+radioButtonF.getText().toString()+"]");
                registerUser();
                //Toast.makeText(getApplicationContext(),radioButtonF.getText().toString(),Toast.LENGTH_SHORT).show();
            }
        });

        textViewBackToLoginRP.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

    }



    private void registerUser() {
        radioGroup = (RadioGroup)findViewById(R.id.radioGroupRP);
        final   String name = editTextName.getText().toString().trim();
      //  final   String gender = editTextgender.getText().toString().trim();
        final String email = editTextEmail.getText().toString().trim();
        final    String phone = editTextPhone.getText().toString().trim();
        final   String password = editTextPassword.getText().toString().trim();


         final RadioButton radioButton1 = ((RadioButton) findViewById(radioGroup.getCheckedRadioButtonId()));


       // int selectedId = radioGroup.getCheckedRadioButtonId();

        int selectedId = radioGroup.getCheckedRadioButtonId();

        // find the radiobutton by returned id
        radioButtonO = (RadioButton) findViewById(selectedId);
        String gender = gen;

        if (TextUtils.isEmpty(name)){
            editTextName.setError("Enter Name");
            editTextName.requestFocus();
            return;
        }
        if (TextUtils.isEmpty(gender)){
            textViewGen.setError("Fill gender");
            textViewGen.requestFocus();
            return;
        }
        if (TextUtils.isEmpty(email)){
            editTextEmail.setError("Enter email");
            editTextEmail.requestFocus();
            return;
        }
        if (TextUtils.isEmpty(phone)){
            editTextPhone.setError("Enter phone");
            editTextPhone.requestFocus();
            return;
        }
        if (TextUtils.isEmpty(password)){
            editTextPassword.setError("Enter password");
            editTextPassword.requestFocus();
            imageViewPassShow.setVisibility(View.INVISIBLE);
            return;

        }

            imageViewPassShow.setVisibility(View.VISIBLE);


        //if it passes all the validations

        class RegisterUser extends AsyncTask<Void, Void, String> {

            //  private ProgressBar progressBar;



            @Override
            protected void onPreExecute() {
                super.onPreExecute();
                //displaying the progress bar while user registers on the server
                /*progressBar = (ProgressBar) findViewById(R.id.progressBar1);
                progressBar.setVisibility(View.VISIBLE);*/
                progressDialog.setMessage("Registering...");
                progressDialog.show();
            }

            @Override
            protected void onPostExecute(String s) {
                super.onPostExecute(s);
                //hiding the progressbar after completion
                //   progressBar.setVisibility(View.GONE);



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
                         startActivity(new Intent(getApplicationContext(), LoginPage.class));
                    } else {
                        progressDialog.dismiss();
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
                params.put("name",name);
                params.put("gender",gender);
                params.put("email",email);
                params.put("phone",phone);
                params.put("password",password);

                //returing the response
                return requestHandler.sendPostRequest(URL_REGISTER, params);
            }
        }

        //executing the async task
        RegisterUser ru = new RegisterUser();
        ru.execute();




    }





}