package com.demo.example.sss.capstone.Adapters;

import android.content.Context;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RatingBar;
import android.widget.TextView;

import com.demo.example.sss.capstone.Customer.CustomerReviews;
import com.demo.example.sss.capstone.R;
import com.demo.example.sss.capstone.model.ProductReview;

import java.util.ArrayList;

/**
 * Created by SSS on 4/8/18.
 */

public class CustomerReviewsAdapter extends RecyclerView.Adapter<CustomerReviewsAdapter.CustomerReviewViewHolder>{

    Context context;
    ArrayList<ProductReview> reviewsAL;

    public CustomerReviewsAdapter(Context context, ArrayList<ProductReview> reviewsAL) {
        this.context = context;
        this.reviewsAL = reviewsAL;
    }

    @Override
    public CustomerReviewViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        View view = inflater.from(parent.getContext())
                .inflate(R.layout.customer_reviews_cardview,parent,false);
    CustomerReviewViewHolder viewHolder = new CustomerReviewViewHolder(view);
    return viewHolder;
    }

    @Override
    public void onBindViewHolder(CustomerReviewViewHolder holder, int position) {
        ProductReview review = reviewsAL.get(position);
        holder.customerRating.setRating(Float.valueOf(review.rating));
        holder.reviewDate.setText(review.createdDate);
        holder.customerName.setText("By "+review.customerName);
        holder.reviewTxt.setText(review.comment);
    }

    @Override
    public int getItemCount() {
        if (reviewsAL != null){
            return reviewsAL.size();
        }
        return 0;
    }

    public static class CustomerReviewViewHolder extends RecyclerView.ViewHolder {
        CardView cvCusRev;
        RatingBar customerRating;
        TextView customerName;
        TextView reviewDate;
        TextView reviewTxt;

        public CustomerReviewViewHolder(View itemView) {
            super(itemView);
            cvCusRev = itemView.findViewById(R.id.cvCusRev);
            customerRating = itemView.findViewById(R.id.ratingBarCustomerReview);
            customerName = itemView.findViewById(R.id.tvByCustomer);
            reviewDate = itemView.findViewById(R.id.tvRevCreatedDate);
            reviewTxt = itemView.findViewById(R.id.tvReviewTxt);
        }
    }
}
