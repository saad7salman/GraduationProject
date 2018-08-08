package com.demo.example.sss.capstone.Adapters;

import android.content.Context;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.demo.example.sss.capstone.R;
import com.demo.example.sss.capstone.model.User;

import java.util.ArrayList;

/**
 * Created by SSS on 4/27/18.
 */

public class PreviousDeliveryAdapter extends RecyclerView.Adapter<PreviousDeliveryAdapter.PrevDelivViewHolder>{
    private Context context;
    private ArrayList<User> userAL;

    public PreviousDeliveryAdapter(Context context, ArrayList<User> userAL) {
        this.context = context;
        this.userAL = userAL;
    }

    @Override
    public PrevDelivViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        View view = inflater.from(parent.getContext())
                .inflate(R.layout.layout_cardview_driver_previous_delivery,parent,false);
        PrevDelivViewHolder viewHolder = new PrevDelivViewHolder(view);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(PrevDelivViewHolder holder, int position) {
                User user = userAL.get(position);
                holder.deliveryNo.setText("Delivery No. : "+user.deliveryOnProgressID);
                holder.providerName.setText("Provider Name : "+user.fullName);
                holder.customerName.setText("Customer Name : "+user.fullName2);
                holder.orderNo.setText("Order No. : "+user.orderID);
    }

    @Override
    public int getItemCount() {
        if (userAL != null){
            return userAL.size();
        }
        return 0;
    }

    public static class PrevDelivViewHolder extends RecyclerView.ViewHolder{

        CardView cvPrevDeliv;
        TextView deliveryNo;
        TextView customerName;
        TextView providerName;
        TextView orderNo;
        public PrevDelivViewHolder(View itemView) {
            super(itemView);
             cvPrevDeliv = itemView.findViewById(R.id.cvPrevDeliv);
             deliveryNo= itemView.findViewById(R.id.txtPrevDelivNo);
             customerName= itemView.findViewById(R.id.txtPrevDelivCusName);
             providerName= itemView.findViewById(R.id.txtPrevDelivProvName);
             orderNo= itemView.findViewById(R.id.txtPrevDelivOrderNo);
        }
    }
}
