package com.example.stylyagav4.ViewHolder;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

import com.example.stylyagav4.Interface.ItemClickListener;
import com.example.stylyagav4.R;

public class CartViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

    public TextView txtProductName, txtProductPrice, txtProductQuantity;
    private ItemClickListener itemClickListener;

    public CartViewHolder(View view){
        super(view);

        txtProductName = view.findViewById(R.id.product_name_cart2);
        txtProductPrice = view.findViewById(R.id.product_price_card2);
        txtProductQuantity = view.findViewById(R.id.product_quantity_card2);
    }

    @Override
    public void onClick(View v) {
        itemClickListener.onClick(v, getAdapterPosition(), false);
    }

    public void setItemClickListener(ItemClickListener itemClickListener) {
        this.itemClickListener = itemClickListener;
    }
}
