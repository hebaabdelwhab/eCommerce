package com.example.ecommerce.ViewHolder;
import android.view.View;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import com.example.ecommerce.Interface.ItemClickListner;
import com.example.ecommerce.R;
import org.jetbrains.annotations.NotNull;

public class CartViewHolder  extends RecyclerView.ViewHolder implements View.OnClickListener{
    public TextView txtProductName ,txtProductPrice ,txtProductQuantity ;
    private ItemClickListner itemClickListner;

    public CartViewHolder(@NonNull @NotNull View itemView) {
        super(itemView);
        txtProductName = itemView.findViewById(R.id.Cart_Product_name);
        txtProductPrice = itemView.findViewById(R.id.Cart_Product_Price);
        txtProductQuantity = itemView.findViewById(R.id.Cart_Product_Quantity);
    }
    @Override
    public void onClick(View v) {

        itemClickListner.onClick(v,getAdapterPosition(),false);
    }
    public void setItemClickListner(ItemClickListner theItemClickListner) {
        this.itemClickListner = theItemClickListner;
    }
}
