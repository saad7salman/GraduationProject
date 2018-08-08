package com.demo.example.sss.capstone.Adapters;

import android.app.Activity;
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
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.demo.example.sss.capstone.DBPackage.MySingleton;
import com.demo.example.sss.capstone.Driver.DriverDeliveryRequest;
import com.demo.example.sss.capstone.Driver.DriverMyDelivery;
import com.demo.example.sss.capstone.R;
import com.demo.example.sss.capstone.model.Products;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by SSS on 11/6/17.
 */

public class DrivDelivRequest extends RecyclerView.Adapter<DrivDelivRequest.DrivDelivRequestViewHolder>{

    private Context context;
    private android.support.v4.app.FragmentManager fragmentManager;
    private ArrayList<Products> orderIDs;
    private Context activityContext;
    private int drivID;


    public DrivDelivRequest(Context context, FragmentManager fragmentManager,
                            ArrayList<Products> orderIDs, Context activityContext,
                            String drivID) {
        this.context = context;
        this.fragmentManager = fragmentManager;
        this.orderIDs = orderIDs;
        this.activityContext = activityContext;
        this.drivID = Integer.parseInt(drivID);
    }

    @Override
    public DrivDelivRequestViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
        View view = layoutInflater.from(parent.getContext())
                .inflate(R.layout.layout_cardview_driver_deliv_rqst,parent,false);
        DrivDelivRequestViewHolder viewHolder = new DrivDelivRequestViewHolder(view);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(DrivDelivRequestViewHolder holder, int position) {
//                final int orderID = orderIDs.get(position);
                final Products products = orderIDs.get(position);
                int num = position + 1;
                holder.tvDrivDelivRqstName.setText("Delivery Request "+num);
                holder.btnDrivDelivRqst.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        insertDataFromDatabase(Integer.parseInt(products.provid),
                                Integer.parseInt(products.cusID),
                                Integer.parseInt(products.orderNumber),drivID);
                    }
                });
    }
    @Override
    public int getItemCount() {
        if (orderIDs != null){
            return orderIDs.size();
        }
        return 0;
    }


    public static class DrivDelivRequestViewHolder extends RecyclerView.ViewHolder{

        CardView cvDrivDeliv;
        TextView tvDrivDelivRqstName;
        Button btnDrivDelivRqst;
        public DrivDelivRequestViewHolder(View itemView) {
            super(itemView);
            cvDrivDeliv = itemView.findViewById(R.id.cvDrivDelivRqst);
            tvDrivDelivRqstName = itemView.findViewById(R.id.tvDrivDelivRqstName);
            btnDrivDelivRqst = itemView.findViewById(R.id.btnDrivDelivRqst);
        }
    }

    private void insertDataFromDatabase(final int provID,final int cusID,final int orderID,final int drivID){
        String url = "http://34.203.215.247/insertDrivRequest.php";
        StringRequest stringRequest = new StringRequest(Request.Method.POST,
                url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        if (response.equals("Failed")){
                            Toast.makeText(activityContext, "Sorry, Something went wrong", Toast.LENGTH_SHORT).show();
                        }else if (response.equals("Delivery On Progress")) {
                            Toast.makeText(activityContext, "Sorry, You Have Delivery On Progress", Toast.LENGTH_LONG).show();
                        }else{
                            DriverMyDelivery driverMyDelivery = new DriverMyDelivery();
                            fragmentManager.beginTransaction().replace( R.id.fragmentContentId,
                                    driverMyDelivery,driverMyDelivery.getTag()).commit();
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        if (error != null){
                            Toast.makeText(activityContext, "Something went wrong", Toast.LENGTH_LONG).show();
                        }
                    }
                }
        ){
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String,String> params = new HashMap<>();
                params.put("drivID",""+drivID);
                params.put("provID",""+provID);
                params.put("cusID",""+cusID);
                params.put("orderID",""+orderID);
                return params;
            }
        };
        MySingleton.getInstance(activityContext.getApplicationContext()).addToRequestQueue(stringRequest);
    }

}
