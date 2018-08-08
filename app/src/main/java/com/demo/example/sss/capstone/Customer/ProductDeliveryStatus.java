package com.demo.example.sss.capstone.Customer;

import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.net.Uri;
import android.os.Bundle;
import android.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.baoyachi.stepview.VerticalStepView;
import com.demo.example.sss.capstone.Activities.MainPageActivity;
import com.demo.example.sss.capstone.R;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link ProductDeliveryStatus.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link ProductDeliveryStatus#newInstance} factory method to
 * create an instance of this fragment.
 */
public class ProductDeliveryStatus extends android.support.v4.app.Fragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;
    VerticalStepView deliveryStatus;
    List<String> status;
    Button getDirection;
    Button leaveRevBtn;
    TextView providerName;
    TextView cellPhone;
    TextView address;
    String deliveryOption;
    private SharedPreferences sharedPreferences;

    private OnFragmentInteractionListener mListener;

    public ProductDeliveryStatus() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment ProductDeliveryStatus.
     */
    // TODO: Rename and change types and number of parameters
    public static ProductDeliveryStatus newInstance(String param1, String param2) {
        ProductDeliveryStatus fragment = new ProductDeliveryStatus();
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
        View view =  inflater.inflate(R.layout.fragment_product_delivery_status, container, false);
        deliveryStatus = view.findViewById(R.id.verticalStepView);
        getDirection = view.findViewById(R.id.btnGetDirectionForCus);
        leaveRevBtn = view.findViewById(R.id.leaveRevBtn);
        providerName = view.findViewById(R.id.tvProvName);
        cellPhone = view.findViewById(R.id.tvProvCellPhone);
        address = view.findViewById(R.id.tvProviderAddressDetail);
        sharedPreferences = getActivity().getSharedPreferences("userinfo", Context.MODE_PRIVATE);

        updateLayout();
        setGetDirection();
        openReviewPage();
        return view;
    }

    private void openReviewPage(){
        leaveRevBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                RatingProduct ratingProduct = new RatingProduct(getContext());
                ratingProduct.showReviewDialog(getArguments().getString("productName"),
                        getArguments().getString("productID"),
                        sharedPreferences.getString("userID","defaultvalue"));
            }
        });
    }
    private void setGetDirection(){
        getDirection.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                openDirectionOnMap();
            }
        });
    }

    private void openDirectionOnMap() {
        String destinations = MainPageActivity.userLocation.getLatitude()+","+ MainPageActivity.userLocation.getLongitude()+"/";
        Location providerLoc = getLatAndLong(address.getText().toString());
        if (providerLoc != null){
            destinations += providerLoc.getLatitude()+","+ providerLoc.getLongitude();
        }
        Intent directionsIntentTempOne = new Intent(android.content.Intent.ACTION_VIEW,
                Uri.parse("https://www.google.co.in/maps/dir/"+destinations));
//        directionsIntentTempOne.setPackage("com.android.chrome");
        try {
            getContext().startActivity(directionsIntentTempOne);
        } catch (ActivityNotFoundException ex) {
            directionsIntentTempOne.setPackage(null);
            getContext().startActivity(directionsIntentTempOne);
        }
    }

    private Location getLatAndLong(String strAddress) {

        Geocoder geocoder = new Geocoder(getActivity());
        List<Address> address = null;
        try {
            address = geocoder.getFromLocationName(strAddress, 1);
        } catch (IOException e) {
            e.printStackTrace();
        }
        if (address == null || address.size() == 0) {
            return null;
        }
        Address location = address.get(0);
        Location providerLoc = new Location("provider Location");
        providerLoc.setLatitude(location.getLatitude());
        providerLoc.setLongitude(location.getLongitude());
        return providerLoc;
    }

    private void updateLayout(){
        providerName.setText(getArguments().getString("providerName"));
        cellPhone.setText(getArguments().getString("cellPhone"));
        address.setText(getArguments().getString("address"));
        deliveryOption = getArguments().getString("deliveryOption");
        createStatusList();
    }

    private void createStatusList(){
        getDirection.setVisibility(View.INVISIBLE);
        status = new ArrayList<>();
        status.add("Order Placed");
        status.add("Order Accepted");
        status.add("In The Kitchen");
        if (deliveryOption.equals("delivery")) {

            status.add("Waiting For The Driver");
            status.add("On The Way");
            status.add("Delivered");
        }else{
            status.add("Ready For Pick Up");
        }
        setStatus();
    }

    private int getCurrentStatus(){

       String currentStatus = getArguments().getString("status");
        if (currentStatus.equals("Delivered") || currentStatus.equals("Ready For Pick Up")){
            if (currentStatus.equals("Ready For Pick Up")){
                getDirection.setVisibility(View.VISIBLE);
            }
            if(getArguments().getString("rating") == null ||
                    getArguments().getString("rating").equals("")
                    || getArguments().getString("rating").equals("null"))
            {
                leaveRevBtn.setVisibility(View.VISIBLE);
            }
        }

        if (currentStatus.equals("Order Placed")&& deliveryOption.equals("delivery")){
            return status.size()-5;
        }else  if (currentStatus.equals("Order Placed")&& deliveryOption.equals("pickup")) {
            return status.size() - 3;
        }else if (currentStatus.equals("In The Kitchen")&& deliveryOption.equals("delivery")){
            return status.size() - 3;
        }else if (currentStatus.equals("In The Kitchen")&& deliveryOption.equals("pickup")){
            return status.size() - 1;
        }else if (currentStatus.equals("Waiting For Driver")&& deliveryOption.equals("delivery")){
            return status.size() - 2;
        }else if (currentStatus.equals("Ready For Pick Up")&& deliveryOption.equals("pickup")){
            return status.size();
        }else if (currentStatus.equals("On The Way")&& deliveryOption.equals("delivery")){
            return status.size() - 1;
        }else if (currentStatus.equals("Delivered")&& deliveryOption.equals("delivery")){
            return status.size();
        }else{
            return 0;
        }
    }

    private void setStatus(){
        if (getCurrentStatus() == 0){
            return;
        }
        deliveryStatus.setStepsViewIndicatorComplectingPosition(getCurrentStatus())
                .reverseDraw(false)
                .setStepViewTexts(status)
                .setLinePaddingProportion(0.80f)
                .setStepsViewIndicatorCompletedLineColor(ContextCompat.getColor(getActivity(), android.R.color.holo_green_dark))
                .setStepsViewIndicatorUnCompletedLineColor(ContextCompat.getColor(getActivity(), android.R.color.black))
                .setStepViewComplectedTextColor(ContextCompat.getColor(getActivity(), R.color.colorPrimaryDark))
                .setStepViewUnComplectedTextColor(ContextCompat.getColor(getActivity(), android.R.color.black))
                .setStepsViewIndicatorCompleteIcon(ContextCompat.getDrawable(getActivity(), R.drawable.completed))
                .setStepsViewIndicatorDefaultIcon(ContextCompat.getDrawable(getActivity(), R.drawable.still))
                .setStepsViewIndicatorAttentionIcon(ContextCompat.getDrawable(getActivity(), R.drawable.attention));
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
}
