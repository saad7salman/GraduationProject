package com.demo.example.sss.capstone.Adapters;

import android.content.Context;
import android.os.Bundle;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.demo.example.sss.capstone.Customer.MainTabsFragment;
import com.demo.example.sss.capstone.Customer.ProductDetailFragment;
import com.demo.example.sss.capstone.DBPackage.DatabaseManager;
import com.demo.example.sss.capstone.DBPackage.MySingleton;
import com.demo.example.sss.capstone.R;
import com.demo.example.sss.capstone.model.DaysOfWeek;
import com.demo.example.sss.capstone.model.Products;
import com.squareup.picasso.Picasso;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by SSS on 10/12/17.
 */

public class ProductAdapter extends RecyclerView.Adapter<ProductAdapter.ProductViewHolder>{

    private Context context;
    private ArrayList<Products> productsArrayList;
    private ArrayList<Products> productsCopyArrayList;
    private ArrayList<DaysOfWeek> businessHrsList;
    android.support.v4.app.FragmentManager fragmentManager;
    int sumQuantity = 0;


    public ProductAdapter(Context context, ArrayList<Products> productsArrayList,
                          android.support.v4.app.FragmentManager fragmentManager,ArrayList<DaysOfWeek> businessHrsList) {
        this.context = context;
        this.productsArrayList = productsArrayList;
        productsCopyArrayList = new ArrayList<>();
        productsCopyArrayList.addAll(this.productsArrayList);
        this.fragmentManager = fragmentManager;
        this.businessHrsList = businessHrsList;
    }

    @Override
    public ProductAdapter.ProductViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        View view = inflater.from(parent.getContext())
                .inflate(R.layout.product_cardview_layout,parent,false);
        ProductViewHolder productViewHolder = new ProductViewHolder(view,fragmentManager);
        return productViewHolder;
    }

    @Override
    public void onBindViewHolder(ProductAdapter.ProductViewHolder holder, int position) {

            Products products = productsArrayList.get(position);

                    holder.tvName.setText(products.name);
                    holder.tvPrice.setText("$" + products.price);
                    holder.tvDesc.setText(products.description);
                    holder.imagePath = products.imagePath;
                    holder.provid = products.provid;
                    holder.productID = products.productID;

                    holder.tvEstimationTime.setText("Avg: " + products.estimationTime + " mins");
                    holder.ratingBar.setRating(Float.valueOf(products.rating));
                     boolean isOpen = isItOpen(products.provid);
                    if (!isOpen) {
                        holder.closedTxt.setVisibility(View.VISIBLE);
                        holder.cvProduct.setClickable(false);
                    }else {
                        holder.closedTxt.setVisibility(View.GONE);
                        holder.cvProduct.setClickable(true);
                    }

                    holder.tvMaxQuantity.setText(products.maxQuantity);
                    if (products.distanceP != 0) {
                        products.distanceP = roundDoubleToOne(products.distanceP, 1);
                        holder.distanceTV.setText(products.distanceP + " mi away");
                    } else {
                        holder.distanceTV.setText("distance N/A");
                    }
                    String fullUrl =products.imagePath;
                    Picasso.with(context)
                            .load(fullUrl)
                            .placeholder(R.drawable.appetizer)
                            .error(android.R.drawable.stat_notify_error)
                            .into(holder.ivImageUrl);
    }
    @Override
    public int getItemCount() {
        if (productsArrayList != null){
            return productsArrayList.size();
        }
        return 0;
    }

    public static class ProductViewHolder extends RecyclerView.ViewHolder{

        public CardView cvProduct;
        ImageView ivImageUrl;
        public TextView tvName;
        public TextView tvPrice;
        public TextView tvDesc;
        public String imagePath;
        public String provid;
        public TextView distanceTV;
        public String productID;
        public TextView tvEstimationTime;
        public TextView tvMaxQuantity;
        public TextView closedTxt;
        public RatingBar ratingBar;

        public ProductViewHolder(final View productView, final android.support.v4.app.FragmentManager fragmentManager){
            super(productView);
            cvProduct = (CardView) productView.findViewById(R.id.cvProduct);
            ivImageUrl = (ImageView) productView.findViewById(R.id.ivImageUrl);
            tvName = (TextView) productView.findViewById(R.id.tvName);
            tvPrice = (TextView) productView.findViewById(R.id.tvPrice);
            tvDesc = (TextView) productView.findViewById(R.id.tvDescCard);
            distanceTV = (TextView) productView.findViewById(R.id.distanceTextView);
            tvEstimationTime = productView.findViewById(R.id.tvEstimationTime);
            tvMaxQuantity = productView.findViewById(R.id.tvMaxQuantity);
            closedTxt = productView.findViewById(R.id.closedTxt);
            ratingBar = productView.findViewById(R.id.ratingBar);

            cvProduct.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    MainTabsFragment productDetailFragment = new MainTabsFragment();
                    Bundle bundle = new Bundle();
                    bundle.putString("productName", tvName.getText().toString());
                    bundle.putString("productPrice", tvPrice.getText().toString());
                    bundle.putString("productDesc", tvDesc.getText().toString());
                    bundle.putString("imgPath",imagePath);
                    bundle.putString("productID",productID);
                    bundle.putString("maxQuantity",tvMaxQuantity.getText().toString());
                    productDetailFragment.setArguments(bundle);
                    Log.i("Status","In The Product Adapter");
                    fragmentManager.beginTransaction().replace(
                            R.id.fragmentContentId, productDetailFragment).commit();
                }
            });
        }
    }

    public double roundDoubleToOne(double v,int places){

        BigDecimal bd = new BigDecimal(v);
        bd = bd.setScale(places, RoundingMode.HALF_UP);
        return bd.doubleValue();
    }

    public void filterByPrice(){

        Collections.sort(productsArrayList, new Comparator<Products>(){
            public int compare(Products o1, Products o2){
                if(o1.price == o2.price)
                    return 0;
                return o1.price < o2.price ? -1 : 1;
            }
        });

        notifyDataSetChanged();
    }

    public void filterByDistance(){
        Collections.sort(productsArrayList, new Comparator<Products>(){
            public int compare(Products o1, Products o2){
                if(o1.distanceP == o2.distanceP)
                    return 0;
                return o1.distanceP < o2.distanceP ? -1 : 1;
            }
        });

        notifyDataSetChanged();
    }

//    public String estimatesTime(String time){
//        int t = Integer.parseInt(time);
//        int hour = t / 60;
//        int minute = t % 60;
//        return "Avg: "+hour+" hrs and "+minute+" mins";
//    }

    public void filterByCategory(String text) {
        if (text.equals("Category")){
            productsArrayList.clear();
            productsArrayList.addAll(productsCopyArrayList);
        }else {
            productsArrayList.clear();
            if (text.isEmpty()) {
                productsArrayList.addAll(productsCopyArrayList);
            } else {
                text = text.toLowerCase();
                for (Products item : productsCopyArrayList) {
                    if (item.category.toLowerCase().contains(text)) {
                        productsArrayList.add(item);
                    }
                }
            }
        }
        notifyDataSetChanged();
    }
    public void filterAL(ArrayList<Products> newList){

        productsArrayList = new ArrayList<>();
        productsArrayList.addAll(newList);
        notifyDataSetChanged();

    }

    private boolean isItOpen(String provID){
        String todaysDay = whichDayIsToday();
        DaysOfWeek daysOfWeek = null;
        boolean found = false;
        for(int i = 0; i < businessHrsList.size();i++){
            daysOfWeek = businessHrsList.get(i);
               if(daysOfWeek.day.equals(todaysDay) && daysOfWeek.provID.equals(provID)){
                    found = true;
                   break;
               }
        }
        if (!found){
            return true;
        }
        if(daysOfWeek.isClosed){
            return false;
        }else{

            Calendar currTime = Calendar.getInstance();
            int hour = currTime.get(Calendar.HOUR_OF_DAY);

               int  openHr = Integer.parseInt(daysOfWeek.fromTime.substring(0,daysOfWeek.fromTime.indexOf(":")));
               int  closeHr = Integer.parseInt(daysOfWeek.toTime.substring(0,daysOfWeek.toTime.indexOf(":")));

            if(hour >= openHr && hour <= closeHr){
                    return true;
             
            }else{
                return false;
            }
        }
    }

    private String whichDayIsToday(){
        Calendar calendar = Calendar.getInstance();
        int day = calendar.get(Calendar.DAY_OF_WEEK);

        switch (day) {
            case Calendar.MONDAY:
                return "Monday";
            case Calendar.TUESDAY:
                return "Tuesday";
            case Calendar.WEDNESDAY:
                return "Wednesday";
            case Calendar.THURSDAY:
                return "Thursday";
            case Calendar.FRIDAY:
                return "Friday";
            case Calendar.SATURDAY:
                return "Saturday";
            case Calendar.SUNDAY:
                return "Sunday";
            default:
                return "NONE";
        }
    }

}
