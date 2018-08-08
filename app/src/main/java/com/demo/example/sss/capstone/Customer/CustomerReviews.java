package com.demo.example.sss.capstone.Customer;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.app.Fragment;
import android.support.annotation.NonNull;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;

import com.demo.example.sss.capstone.Activities.IFragment;
import com.demo.example.sss.capstone.Adapters.CustomerReviewsAdapter;
import com.demo.example.sss.capstone.DBPackage.DatabaseManager;
import com.demo.example.sss.capstone.R;
import com.demo.example.sss.capstone.model.ProductReview;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.Delayed;
import java.util.concurrent.TimeUnit;

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link CustomerReviews.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link CustomerReviews#newInstance} factory method to
 * create an instance of this fragment.
 */
public class CustomerReviews extends android.support.v4.app.Fragment implements IFragment{
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;
    private RatingBar ratingBarCusReviews;
    private TextView noOfReviews;
    private ProgressBar p5Stars;
    private ProgressBar p4Stars;
    private ProgressBar p3Stars;
    private ProgressBar p2Stars;
    private ProgressBar p1Star;
    private TextView tv5Stars;
    private TextView tv4Stars;
    private TextView tv3Stars;
    private TextView tv2Stars;
    private TextView tv1Star;
    private ArrayList<ProductReview> reviewsAL;
    private RecyclerView rvCustomerReviews;
    private LinearLayoutManager linearLayoutManager;
    private OnFragmentInteractionListener mListener;

    public CustomerReviews() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment CustomerReviews.
     */
    // TODO: Rename and change types and number of parameters
    public static CustomerReviews newInstance(String param1, String param2) {
        CustomerReviews fragment = new CustomerReviews();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_customer_reviews, container, false);
         ratingBarCusReviews = view.findViewById(R.id.ratingBarCusRevs);
         noOfReviews= view.findViewById(R.id.numberOfReviews);
         p5Stars= view.findViewById(R.id.progressBar5Stars);
         p4Stars= view.findViewById(R.id.progressBar4Stars);
         p3Stars= view.findViewById(R.id.progressBar3Stars);
         p2Stars= view.findViewById(R.id.progressBar2Stars);
         p1Star= view.findViewById(R.id.progressBar1Star);
         tv5Stars= view.findViewById(R.id.tvProgress5Stars);
         tv4Stars= view.findViewById(R.id.tvProgress4Stars);
         tv3Stars= view.findViewById(R.id.tvProgress3Stars);
         tv2Stars= view.findViewById(R.id.tvProgress2Stars);
         tv1Star= view.findViewById(R.id.tvProgress1Star);
        rvCustomerReviews = view.findViewById(R.id.rvCustomerReviews);
        rvCustomerReviews.setHasFixedSize(true);
        linearLayoutManager = new LinearLayoutManager(getActivity());
        rvCustomerReviews.setLayoutManager(linearLayoutManager);
        getData();
        return view;
    }

    // TODO: Rename method, update argument and hook method into UI event
    public void onButtonPressed(Uri uri) {
        if (mListener != null) {
            mListener.onFragmentInteraction(uri);
        }
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnFragmentInteractionListener) {
            mListener = (OnFragmentInteractionListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnFragmentInteractionListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    @Override
    public void databaseResponse(String response) {
        Log.i("reviewResponse",response);
        if (response.equals("failed")){
            //Toast.makeText(getContext(), "NOOOOOOOOO", Toast.LENGTH_LONG).show();
        }else {
            JSONObject parentObject = null;
            reviewsAL = new ArrayList<>();
            try {
                parentObject = new JSONObject(response);
                JSONArray parentArray = parentObject.getJSONArray("reviews");

                for (int i = 0; i < parentArray.length(); i++) {
                    JSONObject finalObject = parentArray.getJSONObject(i);
                    reviewsAL.add(new ProductReview(finalObject.getString("Product_ID"),
                            finalObject.getString("fullname"),
                            finalObject.getString("Create_Date"),
                            finalObject.getString("Rating"),
                            finalObject.getString("Comments")));
                }
                calculateReviews();
                CustomerReviewsAdapter adapter = new CustomerReviewsAdapter(getContext(),reviewsAL);
                rvCustomerReviews.setAdapter(adapter);
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
    }

    public void getData(){
        DatabaseManager databaseManager = new DatabaseManager(getActivity(),
                "http://34.203.215.247/selectProductReviews.php",
                this);
        Map<String,String> params = new HashMap<>();
        params.put("prodID",getArguments().getString("productID"));
        databaseManager.getDataFromDatabase(params);
    }
    /**
     * This interface must be implemented by activities that contain this
     * fragment to allow an interaction in this fragment to be communicated
     * to the activity and potentially other fragments contained in that
     * activity.
     * <p>
     * See the Android Training lesson <a href=
     * "http://developer.android.com/training/basics/fragments/communicating.html"
     * >Communicating with Other Fragments</a> for more information.
     */
    public interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        void onFragmentInteraction(Uri uri);
    }

    private void calculateReviews(){
        double fiveStars = 0,fourStars = 0,threeStars = 0,
                twoStars = 0,oneStar = 0,sum;

        for(ProductReview review : reviewsAL) {
            double rate = Double.parseDouble(review.rating);
            if (rate > 4) {
                fiveStars++;
            } else if (rate <= 4 && rate > 3) {
                fourStars++;
            } else if (rate <= 3 && rate > 2) {
                threeStars++;
            } else if (rate <= 2 && rate > 1) {
                twoStars++;
            } else if (rate >= 1) {
                oneStar++;
            }
        }
            sum = fiveStars + fourStars + threeStars + twoStars + oneStar;
            double fiveStarsPercentage = ((fiveStars / sum) * 100);
            double fourStarsPercentage = ((fourStars / sum) * 100);
            double threeStarsPercentage = ((threeStars / sum) * 100);
            double twoStarsPercentage = ((twoStars / sum) * 100);
            double oneStarsPercentage = ((oneStar / sum) * 100);
       // Toast.makeText(getContext(), "sum = "+sum+", fourPerc = "+fourStars, Toast.LENGTH_LONG).show();

            p5Stars.setProgress((int)fiveStarsPercentage);
           p4Stars.setProgress((int)fourStarsPercentage);
           p3Stars.setProgress((int)threeStarsPercentage);
           p2Stars.setProgress((int)twoStarsPercentage);
           p1Star.setProgress((int)oneStarsPercentage);

           tv5Stars.setText(" "+roundDoubleToOne(fiveStarsPercentage,1)+"%");
           tv4Stars.setText(" "+roundDoubleToOne(fourStarsPercentage,1)+"%");
           tv3Stars.setText(" "+roundDoubleToOne(threeStarsPercentage,1)+"%");
           tv2Stars.setText(" "+roundDoubleToOne(twoStarsPercentage,1)+"%");
           tv1Star.setText(" "+roundDoubleToOne(oneStarsPercentage,1)+"%");

           double noOfStars = (5 * fiveStars + 4 * fourStars + 3 * threeStars + 2 * twoStars + 1 * oneStar) / sum;
           ratingBarCusReviews.setRating((float)noOfStars);
            noOfReviews.setText("  "+(int)sum+" reviews");
    }

    public double roundDoubleToOne(double v,int places){
        if (v == 0)
            return 0;
        BigDecimal bd = new BigDecimal(v);
        bd = bd.setScale(places, RoundingMode.HALF_UP);
        return bd.doubleValue();
    }
}