package com.demo.example.sss.capstone.Adapters;

import android.content.Context;
import android.support.v4.app.FragmentManager;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.demo.example.sss.capstone.R;
import com.demo.example.sss.capstone.model.Products;
import com.demo.example.sss.capstone.model.User;
import com.squareup.picasso.Picasso;

import org.w3c.dom.Text;

import java.util.ArrayList;

/**
 * Created by SSS on 11/6/17.
 */

public class DrivMyDelivery extends RecyclerView.Adapter<DrivMyDelivery.DrivMyDeliveryViewHolder> {

    private Context context;
    private android.support.v4.app.FragmentManager fragmentManager;
    private ArrayList<Products> orderAL;

    public DrivMyDelivery(Context context, FragmentManager fragmentManager, ArrayList<Products> orderAL) {
        this.context = context;
        this.fragmentManager = fragmentManager;
        this.orderAL = orderAL;
    }

    @Override
    public DrivMyDeliveryViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
        View view = layoutInflater.from(parent.getContext())
                .inflate(R.layout.layout_cardview_driver_mydelivery,parent,false);
        DrivMyDeliveryViewHolder viewHolder = new DrivMyDeliveryViewHolder(view);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(DrivMyDeliveryViewHolder holder, int position) {
                 Products products = orderAL.get(position);
                holder.tvProductName.setText("Name: "+products.name);
                holder.tvOrderNumber.setText("Order No. "+products.orderNumber);
                holder.tvPrice.setText("$"+products.price);
                holder.tvQuantity.setText("Quantity: "+products.quantity);

        String fullUrl = products.imagePath;
        Picasso.with(context)
                .load(fullUrl)
                .placeholder(R.drawable.appetizer)
                .error(android.R.drawable.stat_notify_error)
                .into(holder.imgviewDelivery);

    }

    @Override
    public int getItemCount() {
        if (orderAL != null){
            return orderAL.size();
        }
        return 0;
    }

    public static class DrivMyDeliveryViewHolder extends RecyclerView.ViewHolder{

        CardView cvDrivMyDeliv;
        TextView tvProductName;
        TextView tvOrderNumber;
        TextView tvPrice;
        TextView tvQuantity;
        ImageView imgviewDelivery;
        public DrivMyDeliveryViewHolder(View itemView) {
            super(itemView);
            cvDrivMyDeliv = itemView.findViewById(R.id.cvDrivMyDeliv);
            tvProductName = itemView.findViewById(R.id.tvDrivMyDelivInfo);
            tvOrderNumber = itemView.findViewById(R.id.tvDrivDelivName);
            tvPrice = itemView.findViewById(R.id.tvDrivDelivPhone);
            tvQuantity = itemView.findViewById(R.id.tvDrivDelivAddress);
            imgviewDelivery = itemView.findViewById(R.id.imgviewDelivery);
        }
    }
}
