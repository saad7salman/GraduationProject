package com.demo.example.sss.capstone.Adapters;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Color;
import android.support.v4.app.FragmentManager;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.text.InputType;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.demo.example.sss.capstone.Activities.MainPageActivity;
import com.demo.example.sss.capstone.DBPackage.MySingleton;
import com.demo.example.sss.capstone.Provider.ProviderCusRequest;
import com.demo.example.sss.capstone.R;
import com.demo.example.sss.capstone.model.Products;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by SSS on 11/22/17.
 */

public class ProviderCusWaitingRequset extends RecyclerView.Adapter<ProviderCusWaitingRequset.ProvCusWitingRequestViewHolder>{

    private Context context;
    private android.support.v4.app.FragmentManager fragmentManager;
    private ArrayList<Products> productsAL;
    private Products product;
    private Context activityContext;

    public ProviderCusWaitingRequset(Context context, FragmentManager fragmentManager,
                                     ArrayList<Products> productsAL, Context activityContext) {
        this.context = context;
        this.fragmentManager = fragmentManager;
        this.productsAL = productsAL;
        this.activityContext = activityContext;
    }

    @Override
    public ProvCusWitingRequestViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        View view = inflater.from(parent.getContext())
                .inflate(R.layout.provider_requestwaiting_cardview_layout,parent,false);
        ProvCusWitingRequestViewHolder viewHolder = new ProvCusWitingRequestViewHolder(view);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(ProvCusWitingRequestViewHolder holder, int position) {
        product = productsAL.get(position);
        holder.tvProvCusWRqstName.setText(product.name);
        holder.tvProvCusWRqstQnty.setText("Quantity: "+product.quantity);
        String fullUrl = product.imagePath;
        Picasso.with(context)
                .load(fullUrl)
                .placeholder(R.drawable.appetizer)
                .error(android.R.drawable.stat_notify_error)
                .into(holder.ivProvCusWaitingRequest);

        holder.btnProvCusWRqstAccept.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                updateStatus(product.orderNumber,"In The Kitchen","");
            }
        });
        holder.btnProvCusWRqstReject.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //updateStatus(product.orderNumber,"Rejected");
                alertForRejection(Integer.parseInt(product.orderNumber),false);
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

    public static class ProvCusWitingRequestViewHolder extends RecyclerView.ViewHolder{
        CardView cvProvCusWaitingRqst;
        TextView tvProvCusWRqstName;
        TextView tvProvCusWRqstQnty;
        Button btnProvCusWRqstAccept;
        Button btnProvCusWRqstReject;
        ImageView ivProvCusWaitingRequest;


        public ProvCusWitingRequestViewHolder(View itemView) {
            super(itemView);

            cvProvCusWaitingRqst = itemView.findViewById(R.id.cvProvisderWaitingRequest);
            tvProvCusWRqstName = itemView.findViewById(R.id.tvProvCusWaitingRqstPrdctName);
            tvProvCusWRqstQnty = itemView.findViewById(R.id.tvProvCusWaitingRqstPrdctQnty);
            btnProvCusWRqstAccept = itemView.findViewById(R.id.btnProvCusWaitingRqstAccept);
            btnProvCusWRqstReject = itemView.findViewById(R.id.btnProvCusWaitingRqstReject);
            ivProvCusWaitingRequest = itemView.findViewById(R.id.ivProvCusWaitingRequest);
        }
    }

    private void updateStatus(final String order_ID, final String status, final String comment){
        String url = "http://34.203.215.247/updateStatusProvCus.php";
        StringRequest stringRequest = new StringRequest(Request.Method.POST,
                url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        if (response.equals("status updated successfully")){
                                if (status.equals("Rejected")){
                                    productsAL.remove(productsAL.indexOf(product));
                                    notifyDataSetChanged();
                                }else if (status.equals("In The Kitchen")){
                                    productsAL.remove(productsAL.indexOf(product));
                                    notifyDataSetChanged();
                                    fragmentManager.beginTransaction().replace( R.id.fragmentContentId,
                                            new ProviderCusRequest(),new ProviderCusRequest().getTag()).commit();
                                }
                        }else{
                            Toast.makeText(activityContext, "Oops, something went wrong"+response, Toast.LENGTH_SHORT).show();
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
        ) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<>();
                params.put("orderDetailID",order_ID);
                params.put("status",status);
                params.put("comment",comment);
                return params;
            }
        };
        MySingleton.getInstance(activityContext.getApplicationContext()).addToRequestQueue(stringRequest);

    }


    private void alertForRejection(final int orderNumber,boolean validation){

        AlertDialog.Builder builder = new AlertDialog.Builder(activityContext);
        builder.setTitle("Rejection Reason");
        final EditText input = new EditText(activityContext);
        input.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_CLASS_TEXT);
        if (validation){
            input.setBackgroundColor(Color.RED);
            input.setError("Minimum 10 characters is required");
        }
        builder.setView(input);
        builder.setPositiveButton("Submit", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                String reason = input.getText().toString();
                if (reason.equals("") || reason == null || reason.length() < 10){
                    alertForRejection(orderNumber,true);
                }else {
                    updateStatus("" + orderNumber, "Rejected",reason);
                }
            }
        });
        builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.cancel();
            }
        });
        builder.setCancelable(false);
        builder.show();
    }
}
