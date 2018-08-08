package com.demo.example.sss.capstone.Adapters;

import android.content.Context;
import android.support.v4.app.FragmentManager;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.text.Spannable;
import android.text.SpannableStringBuilder;
import android.text.style.ForegroundColorSpan;
import android.text.style.StyleSpan;
import android.util.Log;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.demo.example.sss.capstone.DBPackage.MySingleton;
import com.demo.example.sss.capstone.Provider.ProviderCusRequest;
import com.demo.example.sss.capstone.Provider.ProviderReadyRequests;
import com.demo.example.sss.capstone.R;


import com.demo.example.sss.capstone.model.Products;
import com.squareup.picasso.Picasso;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by SSS on 11/5/17.
 */

public class ProvCusRequests extends RecyclerView.Adapter<ProvCusRequests.ProvCusRequestViewHolder>{

    private Context context;
    private android.support.v4.app.FragmentManager fragmentManager;
    private ArrayList<Products> productsAL;
    private Products product;
    private Context activityContext;
    private String fragmentType;

    public ProvCusRequests() {
    }

    public ProvCusRequests(Context context, FragmentManager fragmentManager,
                           ArrayList<Products> productsAL, Context activityContext,String fragmentType) {
        this.context = context;
        this.fragmentManager = fragmentManager;
        this.productsAL = productsAL;
        this.activityContext = activityContext;
        this.fragmentType = fragmentType;

    }

    @Override
    public ProvCusRequestViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        View view = inflater.from(parent.getContext())
                .inflate(R.layout.provider_cusrequest_cardview_layout,parent,false);
        ProvCusRequestViewHolder viewHolder = new ProvCusRequestViewHolder(view);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(final ProvCusRequestViewHolder holder, final int position) {
        product = productsAL.get(position);
        holder.tvProvCusRqstName.setText(styleString("Name: ",product.name));
        holder.tvProvCusRqstQnty.setText(styleString("Quantity: ", product.quantity));
        holder.tvProvCusRqstCusName.setText(styleString("Cust. Name: ",product.customerName));
        holder.tvProvCusRqstOrderNumber.setText(styleString("Order No.: ",product.orderNumber));
        holder.orderNum = product.orderNumber;
        String fullUrl = product.imagePath;
        Picasso.with(context)
                .load(fullUrl)
                .placeholder(R.drawable.appetizer)
                .error(android.R.drawable.stat_notify_error)
                .into(holder.ivProvProduct);
       if (fragmentType.equals("Ready") || fragmentType.equals("Delivered")){
            holder.btnProvCusRqst.setVisibility(View.GONE);
            holder.tvProvReadyStatus.setVisibility(View.VISIBLE);
           holder.tvProvReadyStatus.setText(styleString("Status: ",product.status));
            holder.tvProvCusRqstPrdctCusSpecialRequest.setVisibility(View.GONE);
            holder.tvProvCusRqstPrdctCusUpdateStatus.setVisibility(View.GONE);
           int pixel = (int)TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 125, context.getResources().getDisplayMetrics());
            holder.cvProvCusRqst.setLayoutParams(new RelativeLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, pixel));
        }else {
            holder.btnProvCusRqst.setVisibility(View.VISIBLE);
            holder.tvProvReadyStatus.setVisibility(View.GONE);
            holder.tvProvCusRqstPrdctCusSpecialRequest.setText(styleString("Special Request: ",product.specialRequest));
            holder.tvProvCusRqstPrdctCusUpdateStatus.setText(styleString("Update Status To ",""));
            holder.btnProvCusRqst.setText("Ready To Go");
            holder.btnProvCusRqst.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    updateDataInDatabase(holder.orderNum, "Ready To Go");
                }
            });
        }
    }

    private SpannableStringBuilder styleString(String str,String str2){
        final SpannableStringBuilder styleStr = new SpannableStringBuilder(str+str2);
        if (str2.equals("Delivered")){
            styleStr.setSpan(new ForegroundColorSpan(context.getResources()
                .getColor(R.color.greenColor)), str.length(), str.length()+str2.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
        }
        styleStr.setSpan(new android.text.style.StyleSpan(android.graphics.Typeface.BOLD),
                0, str.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
        Log.i("style",styleStr.toString());
        return styleStr;
    }
    @Override
    public int getItemCount() {
        if (productsAL != null){
            return productsAL.size();
        }
        return 0;
    }

    public static class ProvCusRequestViewHolder extends RecyclerView.ViewHolder{

        CardView cvProvCusRqst;
        TextView tvProvCusRqstName;
        TextView tvProvCusRqstQnty;
        TextView tvProvCusRqstOrderNumber;
        TextView tvProvCusRqstCusName;
        Button btnProvCusRqst;
        String orderNum;
        TextView tvProvReadyStatus;
        ImageView ivProvProduct;
        TextView tvProvCusRqstPrdctCusSpecialRequest;
        TextView tvProvCusRqstPrdctCusUpdateStatus;
        public ProvCusRequestViewHolder(View itemView) {
            super(itemView);
             cvProvCusRqst = itemView.findViewById(R.id.cvProvCusRequest);
             tvProvCusRqstName = itemView.findViewById(R.id.tvProvCusRqstPrdctName);
             tvProvCusRqstQnty = itemView.findViewById(R.id.tvProvCusRqstPrdctQnty);;
             tvProvCusRqstOrderNumber = itemView.findViewById(R.id.tvProvCusRqstPrdctOrderNum);;
             tvProvCusRqstCusName = itemView.findViewById(R.id.tvProvCusRqstPrdctCusName);;
             btnProvCusRqst= itemView.findViewById(R.id.tvProvCusRqstPrdctBtn);
            tvProvReadyStatus = itemView.findViewById(R.id.tvProvReadyStatus);
            ivProvProduct =itemView.findViewById(R.id.ivProvProduct);
            tvProvCusRqstPrdctCusSpecialRequest = itemView.findViewById(R.id.tvProvCusRqstPrdctCusSpecialRequest);
            tvProvCusRqstPrdctCusUpdateStatus = itemView.findViewById(R.id.tvProvCusRqstPrdctCusUpdateStatus);
            orderNum = "";
        }
    }

    private void updateDataInDatabase(final String order_ID, final String status){
        String url = "http://34.203.215.247/updateStatusProvCus.php";
        StringRequest stringRequest = new StringRequest(Request.Method.POST,
                url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        if (response.equals("status updated successfully")){
                            ProviderReadyRequests readyRequests = new ProviderReadyRequests();
                            fragmentManager.beginTransaction().replace( R.id.fragmentContentId,
                                    readyRequests,readyRequests.getTag()).commit();
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
                String timeStamp = new SimpleDateFormat("MM/dd/yyyy HH:mm:ss").format(Calendar.getInstance().getTime());
                params.put("comment", timeStamp);
                return params;
            }
        };
        MySingleton.getInstance(activityContext.getApplicationContext()).addToRequestQueue(stringRequest);

    }

}
