package com.demo.example.sss.capstone.Driver;

import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.location.Address;
import android.location.Geocoder;
import android.net.Uri;
import android.os.Bundle;
import android.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;
import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.demo.example.sss.capstone.Activities.IFragment;
import com.demo.example.sss.capstone.Activities.MainPageActivity;
import com.demo.example.sss.capstone.Adapters.DrivMyDelivery;
import com.demo.example.sss.capstone.DBPackage.DatabaseManager;
import com.demo.example.sss.capstone.DBPackage.MySingleton;
import com.demo.example.sss.capstone.R;
import com.demo.example.sss.capstone.model.Products;
import com.demo.example.sss.capstone.model.User;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.w3c.dom.Text;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link DriverMyDelivery.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link DriverMyDelivery#newInstance} factory method to
 * create an instance of this fragment.
 */
public class DriverMyDelivery extends android.support.v4.app.Fragment implements IFragment{
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;
    private RecyclerView rvDrivMyDeliv;
    private Button btnDrivMyDeliv,btnDrivMyDelivStatus;
    private SharedPreferences sharedPreferences;
    private android.support.v4.app.FragmentManager fragmentManager;
    private LinearLayoutManager linearLayoutManager;
    private ArrayList<User> userAL;
    private ArrayList<Products> ordersAL;
    private DrivMyDelivery drivMyDelivery;
    private TextView tvCurrentStatusDelivery;
    private TextView customerInfo;
    private TextView customerName;
    private TextView customerPhone;
    private TextView customerAddress;
    private TextView providerInfo;
    private TextView providerName;
    private TextView providerPhone;
    private TextView providerAddress;
    private String deliverOnProgressId;
    private RelativeLayout relativeLayoutDeliveryProviderInfo;
    private RelativeLayout relativeLayoutDeliveryCustomerInfo;
    private TextView tvOrderToBePickedUp;



    private OnFragmentInteractionListener mListener;
    public DriverMyDelivery() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment DriverMyDelivery.
     */
    // TODO: Rename and change types and number of parameters
    public static DriverMyDelivery newInstance(String param1, String param2) {
        DriverMyDelivery fragment = new DriverMyDelivery();
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
        View view = inflater.inflate(R.layout.fragment_driver_my_delivery, container, false);
        relativeLayoutDeliveryProviderInfo = view.findViewById(R.id.relativeLayoutDeliveryProviderInfo);
        relativeLayoutDeliveryCustomerInfo = view.findViewById(R.id.relativeLayoutDeliveryCustomerInfo);
        rvDrivMyDeliv = view.findViewById(R.id.rvDrivMyDeliv);
        tvOrderToBePickedUp = view.findViewById(R.id.tvOrderToBePickedUp);
        rvDrivMyDeliv.setHasFixedSize(true);
        btnDrivMyDeliv = view.findViewById(R.id.btnDrivDelivDirections);
        btnDrivMyDelivStatus =  view.findViewById(R.id.btnDrivDelivStatus);
        tvCurrentStatusDelivery = view.findViewById(R.id.tvCurrentStatusDelivery);
        customerInfo = view.findViewById(R.id.rvtvDrivMyDelivInfo);
        customerName = view.findViewById(R.id.rvtvDrivDelivName);
        customerPhone = view.findViewById(R.id.rvtvDrivDelivPhone);
        customerAddress = view.findViewById(R.id.rvtvDrivDelivAddress);
        providerInfo = view.findViewById(R.id.rvtvDrivMyDelivInfoP);
        providerName = view.findViewById(R.id.rvtvDrivDelivNameP);
        providerPhone = view.findViewById(R.id.rvtvDrivDelivPhoneP);
        providerAddress = view.findViewById(R.id.rvtvDrivDelivAddressP);
        tvCurrentStatusDelivery.setTextColor(Color.BLACK);
        sharedPreferences = getActivity().getSharedPreferences("userinfo", Context.MODE_PRIVATE);
        linearLayoutManager = new LinearLayoutManager(getActivity());
        rvDrivMyDeliv.setLayoutManager(linearLayoutManager);
        fragmentManager = getFragmentManager();
        getData();
        btnDrivMyDeliv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                openDirectionsOnMap();
            }
        });
        btnDrivMyDelivStatus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                whichStatus(btnDrivMyDelivStatus.getText().toString());
                changeStatusDelivery();
            }
        });
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
        if (response.equals("failed")){
            Toast.makeText(getContext(), "Sorry, You Have No Delivery", Toast.LENGTH_SHORT).show();
            rvDrivMyDeliv.setVisibility(View.GONE);
            btnDrivMyDelivStatus.setVisibility(View.GONE);
            btnDrivMyDeliv.setVisibility(View.GONE);
            tvCurrentStatusDelivery.setVisibility(View.GONE);
            relativeLayoutDeliveryCustomerInfo.setVisibility(View.GONE);
            relativeLayoutDeliveryProviderInfo.setVisibility(View.GONE);
            tvOrderToBePickedUp.setText("No Delivery On Progress");
            tvOrderToBePickedUp.setGravity(Gravity.CENTER);
            tvOrderToBePickedUp.setTextColor(Color.GRAY);
        }else if (response.equals("Failed Updating")){
            Toast.makeText(getContext(), "Sorry, Updates Failed", Toast.LENGTH_SHORT).show();
        }else if (response.equals("Status updated successfully")){
            Toast.makeText(getContext(), "Status Updated Successfully", Toast.LENGTH_SHORT).show();
        }else{
            tvOrderToBePickedUp.setText("Orders To Be Picked Up: ");
            tvOrderToBePickedUp.setGravity(Gravity.CENTER);
            tvOrderToBePickedUp.setTextColor(Color.BLACK);
            Log.i("resp",response);
            JSONObject parentObject = null;
            userAL = new ArrayList<>();
            ordersAL = new ArrayList<>();
            try {
                parentObject = new JSONObject(response);
                JSONArray parentArray = parentObject.getJSONArray("result");
                for (int i = 0; i < parentArray.length(); i++) {
                    JSONObject finalObject = parentArray.getJSONObject(i);
                    userAL.add(new User( finalObject.getString("Deliv_On_ID"),
                            finalObject.getString("fullname"),
                            finalObject.getString("phoneNumber"),
                            finalObject.getString("Latitude"),
                            finalObject.getString("Longitude"),
                            finalObject.getString("Delivery_On_Status")));
                    whichStatus(finalObject.getString("Delivery_On_Status"));
                }
                JSONArray pArray = parentObject.getJSONArray("orders");
                Products product;
                for (int i = 0; i < pArray.length(); i++) {
                    product = new Products();
                    JSONObject finalObject = pArray.getJSONObject(i);
                    product.orderNumber = finalObject.getString("Order_Detail_ID");
                    product.quantity = finalObject.getString("Quantity");
                    product.name = finalObject.getString("name");
                    product.price = Double.parseDouble(finalObject.getString("price"));
                    product.imagePath = finalObject.getString("imagepath");
                    ordersAL.add(product);
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
            updateLayout();
            drivMyDelivery = new DrivMyDelivery(getActivity().getApplicationContext(),
                    fragmentManager,ordersAL);
            rvDrivMyDeliv.setAdapter(drivMyDelivery);
        }
    }

    private void updateLayout(){
        for (int i = 0;i < userAL.size();i++){
            User user = userAL.get(i);
            deliverOnProgressId = user.deliveryOnProgressID;
            if (i == 0){
                customerInfo.setText("Customer Info");
                customerName.setText("Name: "+user.fullName);
                customerPhone.setText("Ph: "+user.phoneNumber);
                customerAddress.setText("Address: "+getAddressFromLatAndLong(user.latitude,user.longitude));
            }else{
                providerInfo.setText("Provider Info");
                providerName.setText("Name: "+user.fullName);
                providerPhone.setText("Ph: "+user.phoneNumber);
                providerAddress.setText("Address: "+getAddressFromLatAndLong(user.latitude,user.longitude));
            }
        }
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

    public void getData(){
        DatabaseManager manager = new DatabaseManager(getContext(),
                "http://34.203.215.247/selectDeliveryInfo.php",
                this);

        Map<String,String> params = new HashMap<>();
        params.put("drivID",sharedPreferences.getString("userID","defaultvalue"));
        manager.getDataFromDatabase(params);
    }

    public void changeStatusDelivery(){
        DatabaseManager manager = new DatabaseManager(getContext(),
                "http://34.203.215.247/updateDeliveryStatusForDriver.php",
                this);
        Map<String,String> params = new HashMap<>();
        params.put("drivID",sharedPreferences.getString("userID","defaultvalue"));
        params.put("status",tvCurrentStatusDelivery.getText().toString());
        params.put("statusOrderDetail",getOrderDetailStatus(tvCurrentStatusDelivery.getText().toString()));
        params.put("deliveryID",deliverOnProgressId);
        manager.sendDataToDatabase(params);
    }

    private String getOrderDetailStatus(String status){
        if (!status.equals("Delivered"))
                return "On The Way";
         else
             return "Delivered";
    }

    private void openDirectionsOnMap(){
        String destinations = MainPageActivity.userLocation.getLatitude()+","+ MainPageActivity.userLocation.getLongitude()+"/";
        for (int i = 1; i< userAL.size(); i ++){
            User user2 = userAL.get(i);
            destinations += user2.latitude+","+ user2.longitude+"/";
        }
        User user2 = userAL.get(0);
        destinations += user2.latitude+","+ user2.longitude;
        Intent directionsIntentTempOne = new Intent(android.content.Intent.ACTION_VIEW,
                Uri.parse("https://www.google.co.in/maps/dir/"+destinations));
        directionsIntentTempOne.setPackage("com.android.chrome");
        try {
            getContext().startActivity(directionsIntentTempOne);
        } catch (ActivityNotFoundException ex) {
            directionsIntentTempOne.setPackage(null);
            getContext().startActivity(directionsIntentTempOne);
        }
    }

    private String getAddressFromLatAndLong(String latitude, String longitude){
        Geocoder geocoder;
        List<Address> addresses;
        String addressLine="",city="",state="",zipcode="",county="";
        geocoder = new Geocoder(getActivity(), Locale.getDefault());

        try {
            addresses = geocoder.getFromLocation(Double.parseDouble(latitude),Double.parseDouble( longitude), 1); // Here 1 represent max location result to returned, by documents it recommended 1 to 5
            addressLine = addresses.get(0).getAddressLine(0); // If any additional address line present than only, check with max available address lines by getMaxAddressLineIndex()
             city = addresses.get(0).getLocality();
             state = addresses.get(0).getAdminArea();
            zipcode = addresses.get(0).getPostalCode();
//            county = addresses.get(0).getCountryName();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return addressLine+", "+city+", "+state+" "+zipcode;
    }

    private void whichStatus(String status) {
        switch (status) {
            case "Delivery Accepted":
                btnDrivMyDelivStatus.setText("On The Way To Provider");
                tvCurrentStatusDelivery.setText("Delivery Accepted");
                break;
            case "On The Way To Provider":
                btnDrivMyDelivStatus.setText("On The Way To Customer");
                tvCurrentStatusDelivery.setText(status);
                break;
            case "On The Way To Customer":
                btnDrivMyDelivStatus.setText("Delivered");
                tvCurrentStatusDelivery.setText(status);
                break;
            case "Delivered":
                tvCurrentStatusDelivery.setText(status);
                Toast.makeText(getActivity(), "Congrats", Toast.LENGTH_SHORT).show();
                break;
        }
    }
}