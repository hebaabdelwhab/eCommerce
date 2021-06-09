package com.example.ecommerce;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;

public class ConfirmFinalOrderActivity extends AppCompatActivity {
    private  EditText nameEditText , PhoneEditText , AddressEditText , CityEditText;
    private Button ConfirmBtn;
    private  String TotalPrice ="";
     @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_confirm_final_order);
        //Step[1]
         TotalPrice = getIntent().getStringExtra("Total Price");
         nameEditText = findViewById(R.id.shippment_name);
         PhoneEditText = findViewById(R.id.shippment_PhoneNumber);
         AddressEditText = findViewById(R.id.shippment_Address);
         CityEditText = findViewById(R.id.shippment_City_name);
         ConfirmBtn = findViewById(R.id.Confirm_final_order_btn);
         //Step[2]
    }
}