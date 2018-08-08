package com.demo.example.sss.capstone.Customer;

import android.content.Context;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.support.v7.app.AlertDialog;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RatingBar;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.demo.example.sss.capstone.DBPackage.MySingleton;
import com.demo.example.sss.capstone.R;
import com.demo.example.sss.capstone.model.Products;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by SSS on 4/7/18.
 */

public class RatingProduct {
    Context context;

    public RatingProduct(Context context) {
    this.context = context;
    }


    public void showDialog(ArrayList<Products> productsAL, final String customerID){
        Products productToRate = null;
        for(Products p : productsAL){
            if (p.status.equals("Delivered") && p.rating.equals("")
                    || p.rating.equals("null") && p.status.equals("Delivered")){
                productToRate = p;
                break;
            }else{
            }
        }
        if (productToRate == null) return;
        showReviewDialog(productToRate.name,productToRate.productID,customerID);
    }

    public void showReviewDialog(String productName, final String productID, final String customerID){
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setTitle("Rate "+ productName);
        final RatingBar ratingBar = new RatingBar(context);
        LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        layoutParams.setMargins(30, 30, 30, 30);
        ratingBar.setLayoutParams(layoutParams);
        ratingBar.setStepSize((float) 0.5);
        ratingBar.setNumStars(5);
        ratingBar.setMax(5);
        LinearLayout layout = new LinearLayout(context);
        layout.setOrientation(LinearLayout.VERTICAL);
        final EditText et = new EditText(context);
        et.setHint("Please leave a review!");
        layout.addView(ratingBar);
        layout.addView(et);
        builder.setView(layout);

        builder.setPositiveButton("Submit", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                addReviewToDB(productID,customerID,
                        ""+ratingBar.getRating(),et.getText().toString());
            }
        });
        builder.setNeutralButton("Remind me later", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {

            }
        });
        builder.setNegativeButton("Cancel", null);
        AlertDialog dialog = builder.create();
        dialog.show();
    }

    private void addReviewToDB(final String prodID,final String cusID,final String rate,final String comment){
        String url = "http://34.203.215.247/AddProductReview.php";
        StringRequest stringRequest = new StringRequest(Request.Method.POST,
                url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        if (response.equals("Added Successfully")){
                            Toast.makeText(context, "Added Successfully", Toast.LENGTH_LONG).show();
                        }else{
                            Toast.makeText(context, "Sorry,please try again"+response, Toast.LENGTH_LONG).show();
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
                params.put("Comment",""+comment);
                params.put("ProdID",""+prodID);
                params.put("CusID",""+cusID);
                params.put("Rate",""+rate);
                return params;
            }
        };
        MySingleton.getInstance(context.getApplicationContext()).addToRequestQueue(stringRequest);
    }



}
