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
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.amazonaws.HttpMethod;
import com.amazonaws.auth.BasicAWSCredentials;
import com.amazonaws.services.s3.AmazonS3Client;
import com.amazonaws.services.s3.model.GeneratePresignedUrlRequest;
import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.demo.example.sss.capstone.DBPackage.MySingleton;
import com.demo.example.sss.capstone.Provider.ProviderUpdateFood;
import com.demo.example.sss.capstone.R;
import com.demo.example.sss.capstone.model.Products;
import com.squareup.picasso.Picasso;

import java.net.URL;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by SSS on 10/28/17.
 */

public class ProvProductsList extends RecyclerView.Adapter<ProvProductsList.ProvProdViewHolder>{

    private Context context;
    private android.support.v4.app.FragmentManager fragmentManager;
    private android.support.v4.app.FragmentManager frgmntMngr;
    private ArrayList<Products> productsAL;
    private Products product;
    private Context activityContext;
    public ProvProductsList(Context context, android.support.v4.app.FragmentManager fragmentManager,
                            ArrayList<Products> productsAL,Context activityContext,FragmentManager frgmntMngr) {
        this.context = context;
        this.fragmentManager = fragmentManager;
        this.productsAL = productsAL;
        this.activityContext = activityContext;
        this.frgmntMngr = frgmntMngr;
    }

    @Override
    public ProvProdViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        View view = inflater.from(parent.getContext())
                .inflate(R.layout.layout_cardview_provider_products,parent,false);
        ProvProdViewHolder provProdViewHolder = new ProvProdViewHolder(view);
        return provProdViewHolder;
    }

    @Override
    public void onBindViewHolder(final ProvProdViewHolder holder, int position) {
                  product = productsAL.get(position);
            holder.tvProdName.setText(product.name);
            holder.tvProdDes.setText("Description: \n"+product.description);
            holder.tvProdPrice.setText("$"+product.price);
            holder.tvProdCateg.setText("Category: "+product.category);
            holder.prodID = product.productID;

        holder.updateBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ProviderUpdateFood updateFood = new ProviderUpdateFood();
                Bundle bundle = new Bundle();
                bundle.putString("prodID",product.productID);
                bundle.putString("prodName",product.name);
                bundle.putString("prodDes",product.description);
                bundle.putString("prodPrice",""+product.price);
                bundle.putString("estimationTime",""+product.estimationTime);
                bundle.putString("maxQnty",""+product.maxQuantity);
                bundle.putString("prodCategory",""+product.category);
                bundle.putString("prodImgPath",""+product.imagePath);
                updateFood.setArguments(bundle);
                frgmntMngr.beginTransaction().replace(
                        R.id.fragmentContentId, updateFood,
                        updateFood.getTag()).commit();
            }
        });

        BasicAWSCredentials credentials = new BasicAWSCredentials("AKIAIIXR6X4MPDOM52SQ",
                                                    "NUyDMdXdQdaRWHghrf21YRjVgZki0DlmEFxug81Y");
        AmazonS3Client s3Client = new AmazonS3Client(credentials);
        holder.deleteBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
               deleteProvProd(Integer.parseInt(holder.prodID));
            }
        });
        Date expiration = new Date();
        long msec = expiration.getTime();
        msec += 1000 * 60 * 60;
        expiration.setTime(msec);
        GeneratePresignedUrlRequest generatePresignedUrlRequest =
                new GeneratePresignedUrlRequest("capstone-s3",product.imagePath);
        generatePresignedUrlRequest.setMethod(HttpMethod.POST);
        generatePresignedUrlRequest.setExpiration(expiration);

        URL s = s3Client.generatePresignedUrl(generatePresignedUrlRequest);
        String fullUrl = product.imagePath;
        //String fullUrl = "http://10.0.2.2/"+product.imagePath;
        Log.i("s3Link",fullUrl);
        Picasso.with(context)
                .load(fullUrl)
                .placeholder(R.drawable.appetizer)
                .error(android.R.drawable.stat_notify_error)
                .into(holder.ivProvImage);

    }

    @Override
    public int getItemCount() {
        if (productsAL != null){
            return productsAL.size();
        }
        return 0;
    }

    public static class ProvProdViewHolder extends RecyclerView.ViewHolder{

        public CardView cvProvProd;
        public ImageView ivProvImage;
        public TextView tvProdName;
        public TextView tvProdDes;
        public TextView tvProdPrice;
        public TextView tvProdCateg;
        public String prodID;
        public Button updateBtn;
        public Button deleteBtn;

        public ProvProdViewHolder(View provProdView) {
            super(provProdView);
            cvProvProd = provProdView.findViewById(R.id.cvProvProd);
            ivProvImage = provProdView.findViewById(R.id.ivProvProdImage);
            tvProdName =  provProdView.findViewById(R.id.tvProvProdName);
            tvProdDes =  provProdView.findViewById(R.id.tvProvProdDesc);
            tvProdPrice =  provProdView.findViewById(R.id.tvProvProdPrice);
            tvProdCateg =  provProdView.findViewById(R.id.tvProvProdCategory);
            updateBtn = provProdView.findViewById(R.id.updateBtnProv);
            deleteBtn = provProdView.findViewById(R.id.deleteBtnProv);
        }

    }

    private void deleteProvProd(final int prodID){
        String url = "http://34.203.215.247/deleteProvProd.php";
        StringRequest stringRequest = new StringRequest(Request.Method.POST,
                url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        Log.i("resp ",response);
                        if (response.equals("product deleted successfully")){
                            Toast.makeText(context, "product deleted successfully", Toast.LENGTH_SHORT).show();
                            productsAL.remove(productsAL.indexOf(product));
                            notifyDataSetChanged();
                        }else if (response.equals("Customer's order is in progress")){
                            Toast.makeText(context, "Oops, Customer has ordered this product and still not delivered yet"
                                    , Toast.LENGTH_LONG).show();
                        }else {
                            Toast.makeText(context, "Oops, Something went wrong"
                                    , Toast.LENGTH_SHORT).show();
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        if (error != null){
                            Toast.makeText(context, "Something went wrong", Toast.LENGTH_LONG).show();
                        }
                    }
                }
        ){
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String,String> params = new HashMap<>();
                params.put("prodID",""+prodID);
                return params;
            }
        };
        MySingleton.getInstance(context.getApplicationContext()).addToRequestQueue(stringRequest);
    }
}
