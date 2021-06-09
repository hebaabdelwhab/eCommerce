package com.example.ecommerce;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.ecommerce.Models.Products;
import com.example.ecommerce.Prevalent.Prevalent;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

import org.jetbrains.annotations.NotNull;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.HashMap;


public class ProductDetailsActivity extends AppCompatActivity {
    private ImageView TheImageView;
    private TextView ProductName,ProductDescription ,ProductPrice,value;
    private  String productId ="";
    private Button  incrementBTn ,DecrementBTN,addtocartbutton;
    int count =0;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_product_details);
        //Step1
        TheImageView = (ImageView)findViewById(R.id.product_image_details);
         ProductName = (TextView)findViewById(R.id.Product_name_details);
         ProductPrice = findViewById(R.id.product_Price_Details);
         ProductDescription = findViewById(R.id.Product_Description_Details);
         productId = getIntent().getStringExtra("pid");
         value = findViewById(R.id.Value_Product);
         incrementBTn = findViewById(R.id.IcrementBtn);
         DecrementBTN = findViewById(R.id.DecrementBTN);
         addtocartbutton = (Button)findViewById(R.id.pd_add_to_cart_button);
        //step2
        getProductDetails(productId);
        addtocartbutton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                addingToCartList();
            }
        });
        incrementBTn.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                count++;
                value.setText(""+count);
            }
        });
        DecrementBTN.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                count--;
                if(count<0)
                    count = 0;
                value.setText(""+count);
            }
        });
    }

    private void addingToCartList() {
        String saveCurrentTime , saveCurrentDate;
        Calendar calForDate = Calendar.getInstance();
        SimpleDateFormat currentDate = new SimpleDateFormat("MMM dd, yyyy");
        saveCurrentDate = currentDate.format(calForDate.getTime());
        SimpleDateFormat currentTime = new SimpleDateFormat("HH:mm:ss a");
        saveCurrentTime = currentDate.format(calForDate.getTime());
        DatabaseReference cartListRef = FirebaseDatabase.getInstance().getReference().child("cart List");
        final HashMap<String,Object> cartMap = new HashMap<>();
        cartMap.put("pid",productId);
        cartMap.put("pname",ProductName.getText().toString());
        cartMap.put("price",ProductPrice.getText().toString());
        cartMap.put("date",saveCurrentDate);
        cartMap.put("time",saveCurrentTime);
        cartMap.put("quantity",count);
        cartMap.put("discount","");
        cartListRef.child("User View").child(Prevalent.currentOnlineUser.getPhone()).child("Product").child(productId).updateChildren(cartMap).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull @NotNull Task<Void> task) {
                if(task.isSuccessful())
                {
                    cartListRef.child("Admin View").child(Prevalent.currentOnlineUser.getPhone())
                    .child("Products").child(productId)
                    .updateChildren(cartMap)
                    .addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull @NotNull Task<Void> task) {
                            if(task.isSuccessful())
                            {
                                Toast.makeText(ProductDetailsActivity.this,"Added to Cart List.",Toast.LENGTH_SHORT).show();
                                Intent TheIntent = new Intent(ProductDetailsActivity.this,HomeActivity.class);
                                startActivity(TheIntent);
                            }
                        }
                    });

                }
            }
        });
    }
    private void getProductDetails(String productId) {
        DatabaseReference ProductsRef = FirebaseDatabase.getInstance().getReference().child("Products");
        ProductsRef.child(productId).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if(snapshot.exists())
                {
                    Products TheProducts = snapshot.getValue(Products.class);
                    ProductName.setText(TheProducts.getPname());
                    ProductPrice.setText(TheProducts.getPrice());
                    ProductDescription.setText(TheProducts.getDescription());
                    Picasso.get().load(TheProducts.getImage()).into(TheImageView);
                }
            }
            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }
}