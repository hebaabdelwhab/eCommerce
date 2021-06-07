package com.example.ecommerce;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.widget.Button;
import android.widget.TextView;

public class CartActivity extends AppCompatActivity {
    private RecyclerView TheRecyclerView ;
    private RecyclerView.LayoutManager TheLayoutManager;
    private Button nextProgressbtn;
    private TextView TotalCost;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cart);
        //step1
        TheRecyclerView = findViewById(R.id.cart_List);
        TheRecyclerView.setHasFixedSize(true);
        TheLayoutManager = new LinearLayoutManager(this);
        TheRecyclerView.setLayoutManager(TheLayoutManager);
        nextProgressbtn = findViewById(R.id.next_Process_btn);
        TotalCost = findViewById(R.id.TotalPrice);
    }
}