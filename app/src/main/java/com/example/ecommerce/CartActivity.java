package com.example.ecommerce;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.example.ecommerce.Models.Cart;
import com.example.ecommerce.Models.Products;
import com.example.ecommerce.Prevalent.Prevalent;
import com.example.ecommerce.ViewHolder.CartViewHolder;
import com.example.ecommerce.ViewHolder.ProductViewHolder;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.squareup.picasso.Picasso;

import org.jetbrains.annotations.NotNull;

public class CartActivity extends AppCompatActivity {
    private RecyclerView recyclerView;
    RecyclerView.LayoutManager layoutManager;
    private Button nextProgressbtn;
    private TextView TotalCost;
    private  int OverTotalAmount=0;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cart);
        //step1
        recyclerView = findViewById(R.id.cart_List);
        recyclerView.setHasFixedSize(true);
        layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);
        nextProgressbtn = findViewById(R.id.next_Process_btn);
        TotalCost = findViewById(R.id.TotalPrice);
        ///-------------------------------------------------------
        nextProgressbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                TotalCost.setText("Total Price = "+String.valueOf(OverTotalAmount));
                Intent intent = new Intent(CartActivity.this,ConfirmFinalOrderActivity.class);
                intent.putExtra("Total Price",String.valueOf(OverTotalAmount));
                startActivity(intent);
                finish();
            }
        });
    }
    @Override
    protected void onStart(){
        super.onStart();
        DatabaseReference cartListRef = FirebaseDatabase.getInstance().getReference().child("cart List");

        FirebaseRecyclerOptions<Cart> options =
                new FirebaseRecyclerOptions.Builder<Cart>()
                        .setQuery(cartListRef.child("User View").child(Prevalent.currentOnlineUser.getPhone()).child("Product"), Cart.class)
                        .build();
        FirebaseRecyclerAdapter<Cart, CartViewHolder> adapter =
                new FirebaseRecyclerAdapter<Cart, CartViewHolder>(options){
                    @Override
                    protected void onBindViewHolder(@NonNull CartViewHolder holder, int position, @NonNull Cart model)
                    {
                        holder.txtProductName.setText("name = " + model.getPname());
                        holder.txtProductPrice.setText("Price = " + model.getPrice());
                        holder.txtProductQuantity.setText("Quantity = " + model.getQuantity());
                        int OnTypeProductTypePrice = ((Integer.valueOf(model.getPrice())))*Integer.valueOf(model.getQuantity());
                        OverTotalAmount +=OnTypeProductTypePrice;
                        holder.itemView.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                CharSequence options[] = new CharSequence[]{
                                          "Edit",
                                          "Remove"};
                                AlertDialog.Builder builder = new AlertDialog.Builder(CartActivity.this);
                                builder.setTitle("Cart Options:");
                                builder.setItems(options, new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {
                                        if(which==0)
                                        {
                                            Intent intent = new Intent(CartActivity.this,ProductDetailsActivity.class);
                                            intent.putExtra("pid",model.getPid());
                                            startActivity(intent);
                                        }
                                        if(which==1)
                                        {
                                            cartListRef.child("User View")
                                            .child(Prevalent.currentOnlineUser.getPhone())
                                            .child("Product")
                                            .child(model.getPid())
                                            .removeValue().addOnCompleteListener(new OnCompleteListener<Void>() {
                                                @Override
                                                public void onComplete(@NonNull @NotNull Task<Void> task) {
                                                     if(task.isSuccessful())
                                                     {
                                                         Toast.makeText(CartActivity.this,"Item removed successfully",Toast.LENGTH_SHORT).show();
                                                         Intent intent = new Intent(CartActivity.this,HomeActivity.class);
                                                         startActivity(intent);

                                                     }
                                                }
                                            });
                                        }

                                    }
                                });
                                builder.show();
                             }
                        });
                    }
                    @Override
                    public CartViewHolder onCreateViewHolder(@NonNull  ViewGroup parent, int viewType) {
                        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.cart_items_layout, parent, false);
                        CartViewHolder holder = new CartViewHolder(view);
                        return holder;
                    }
                };
                recyclerView.setAdapter(adapter);
                adapter.startListening();
    }
}