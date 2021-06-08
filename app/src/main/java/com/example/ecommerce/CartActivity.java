package com.example.ecommerce;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.example.ecommerce.Models.Cart;
import com.example.ecommerce.Prevalent.Prevalent;
import com.example.ecommerce.ViewHolder.CartViewHolder;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class CartActivity extends AppCompatActivity {
    private RecyclerView recyclerView;
    private LinearLayoutManager TheLayoutManager;
    private Button nextProgressbtn;
    private TextView TotalCost;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cart);
        //step1
        recyclerView = findViewById(R.id.cart_List);
        TheLayoutManager = new LinearLayoutManager(this);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(TheLayoutManager);
        nextProgressbtn = findViewById(R.id.next_Process_btn);
        TotalCost = findViewById(R.id.TotalPrice);
    }
    @Override
    protected void onStart() {
        super.onStart();
        final DatabaseReference cartListRef = FirebaseDatabase.getInstance().getReference().child("cart List");
        FirebaseRecyclerOptions<Cart> options
                = new FirebaseRecyclerOptions.Builder<Cart>()
                .setQuery(cartListRef.child("User View")
                        .child(Prevalent.currentOnlineUser.getPhone())
                        .child("Products"),Cart.class)
                        .build();
        FirebaseRecyclerAdapter<Cart, CartViewHolder> adapter =
            new FirebaseRecyclerAdapter<Cart, CartViewHolder>(options){
            @Override
            protected void onBindViewHolder(@NonNull CartViewHolder holder, int position, @NonNull Cart model) {
                  //holder.txtProductName.setText(model.getPname());
                  //holder.txtProductPrice.setText(model.getPrice());
                  //holder.txtProductQuantity.setText(model.getQuantity());
            }
            @Override
            public CartViewHolder onCreateViewHolder(@NonNull  ViewGroup parent, int viewType) {
                //View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.cart_items_layout, parent, false);
                //CartViewHolder holder = new CartViewHolder(view);
                return null;
            }
        };
        recyclerView.setAdapter(adapter);
        adapter.startListening();
    }
}