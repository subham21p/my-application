package com.example.onlineshoping.Activities;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.example.onlineshoping.R;

public class GetEmail extends AppCompatActivity {

    EditText editTextEmail;
    Button button;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_get_email);

        editTextEmail = (EditText)findViewById(R.id.editTextTextEmailAddressGE);
        button = (Button)findViewById(R.id.buttonGE);

        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(GetEmail.this,ForgetPassword.class);
                intent.putExtra("email",editTextEmail.getText().toString().trim());
                startActivity(intent);
                finish();
            }
        });

    }
}