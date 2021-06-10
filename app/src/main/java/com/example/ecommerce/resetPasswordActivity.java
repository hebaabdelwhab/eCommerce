package com.example.ecommerce;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.ecommerce.Models.Products;
import com.example.ecommerce.Models.Users;
import com.example.ecommerce.Prevalent.Prevalent;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class resetPasswordActivity extends AppCompatActivity {
    private TextView  PhoneNumber;
    private EditText PasswordEditText;
    private Button UpdateBtn;
    String ThePhoneNumber = "";
    String parentDbName = "";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_reset_password);
        //Step[1];
        PhoneNumber = findViewById(R.id.reset_phone_number);
        PasswordEditText = findViewById(R.id.reset_password);
        UpdateBtn = findViewById(R.id.reset_Update_Password);
        //Step[2];
        ThePhoneNumber = getIntent().getStringExtra("PhotoNumber");
        PhoneNumber.setText(ThePhoneNumber);
        //Step3;
        UpdateBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final DatabaseReference RootRef= FirebaseDatabase.getInstance().getReference();
                RootRef.child("Users").child(ThePhoneNumber).child("password").setValue(PasswordEditText.getText().toString());
                Toast.makeText(resetPasswordActivity.this, "Update Successfully...", Toast.LENGTH_SHORT).show();
                Intent intent = new Intent(resetPasswordActivity.this, LoginActivity.class);
                startActivity(intent);
            }
        });

    }
}