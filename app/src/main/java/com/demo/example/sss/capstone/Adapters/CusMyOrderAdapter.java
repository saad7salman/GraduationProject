package com.demo.example.sss.capstone.Adapters;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.demo.example.sss.capstone.Customer.ProductDeliveryStatus;
import com.demo.example.sss.capstone.DBPackage.MySingleton;
import com.demo.example.sss.capstone.R;
import com.demo.example.sss.capstone.model.Products;
import com.squareup.picasso.Picasso;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Map;
/**
 * Created by SSS on 10/26/17.
 */
public class CusMyOrderAdapter extends RecyclerView.Adapter<CusMyOrderAdapter.CusMyOrderViewHolder>{

    private Context context;
    private android.support.v4.app.FragmentManager fragmentManager;
    private ArrayList<Products> productsAL;
    DateFormat df;

    public CusMyOrderAdapter(Context context, FragmentManager fragmentManager, ArrayList<Products> productsArrayList) {
        this.context = context;
        this.fragmentManager = fragmentManager;
        this.productsAL = productsArrayList;
        Log.i("Arraylist",productsAL.toString());
        df = new SimpleDateFormat("MM/dd/yyyy");
        setHasStableIds(true);
    }

    @Override
    public CusMyOrderViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        View view = inflater.from(parent.getContext())
                .inflate(R.layout.layout_cardview_customer_myorders,parent,false);
        CusMyOrderViewHolder cusMyOrderViewHolder = new CusMyOrderViewHolder(view);
        return cusMyOrderViewHolder;
    }

    @Override
    public void onBindViewHolder(CusMyOrderViewHolder holder, int position) {
            final Products product = productsAL.get(position);
            holder.tvProdName.setText(product.name);
            holder.tvQuantity.setText("Quantity: "+product.quantity);
            holder.tvPrice.setText("$"+product.price);
            holder.tvDate.setText(""+df.format(product.date));
            product.status = updateStatus(product.status,product.delivOption);
            holder.tvStatus.setText("Status: " + product.status);
            holder.tvOrderNumber.setText("Order No.: "+product.orderNumber);
        String fullUrl =product.imagePath;
        Picasso.with(context)
                .load(fullUrl)
                .placeholder(R.drawable.appetizer)
                .error(android.R.drawable.stat_notify_error)
                .into(holder.ivImage);
        holder.cvMyOrder.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ProductDeliveryStatus deliveryStatus = new ProductDeliveryStatus();
                Bundle bundle = new Bundle();
                bundle.putString("productID",product.productID);
                bundle.putString("rating",product.rating);
                bundle.putString("productName",product.name);
                bundle.putString("status",product.status);
                bundle.putString("providerName",product.providerName);
                bundle.putString("cellPhone",product.phoneNumber);
                bundle.putString("address",product.address);
                bundle.putString("deliveryOption",product.delivOption);
                deliveryStatus.setArguments(bundle);
                fragmentManager.beginTransaction().replace(R.id.fragmentContentId,
                        deliveryStatus,deliveryStatus.getTag()).commit();
            }
        });
    }
    @Override
    public int getItemCount() {
        if (productsAL != null){
            return productsAL.size();
        }
        return 0;
    }
    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public int getItemViewType(int position) {
        return position;
    }
    private String updateStatus(String status,String deliveryOption){

        if (status.equals("Delivery Accepted")){
            return "Waiting For Driver";
        }if (status.equals("Ready To Go") && deliveryOption.equals("pickup")){
                return "Ready For Pick Up";
        }if (status.equals("Ready To Go")&& deliveryOption.equals("delivery")){
            return "Waiting For Driver";
        }
        else{
            return status;
        }

    }

    public static class CusMyOrderViewHolder extends RecyclerView.ViewHolder{

        public CardView cvMyOrder;
        public TextView tvProdName;
        public TextView tvQuantity;
        public TextView tvPrice;
        public TextView tvDate;
        public TextView tvStatus;
        public ImageView ivImage;
        public TextView tvOrderNumber;

        public CusMyOrderViewHolder(View myOrderView) {
            super(myOrderView);
            cvMyOrder = myOrderView.findViewById(R.id.cvCusMyOders);
            tvProdName = myOrderView.findViewById(R.id.tvCusOrderTitle);
            tvQuantity = myOrderView.findViewById(R.id.tvCusOrderQuantity);
            tvPrice = myOrderView.findViewById(R.id.tvCusOrderPrice);
            tvDate = myOrderView.findViewById(R.id.tvCusOrderDate);
            tvStatus = myOrderView.findViewById(R.id.tvCusOrderStatus);
            ivImage = myOrderView.findViewById(R.id.imCusOrderImage);
            tvOrderNumber = myOrderView.findViewById(R.id.tvCusOrderNumber);
        }
    }

    public void filterByDate(){
        Collections.sort(productsAL, new Comparator<Products>() {
            public int compare(Products o1, Products o2) {
                if (o1.date == null || o2.date == null)
                    return 0;
                return o2.date.compareTo(o1.date);
            }
        });
        notifyDataSetChanged();
    }
}
