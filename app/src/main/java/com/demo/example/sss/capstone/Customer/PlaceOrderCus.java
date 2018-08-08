package com.demo.example.sss.capstone.Customer;

import android.content.Context;
import android.content.SharedPreferences;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.net.Uri;
import android.os.Bundle;
import android.app.Fragment;
import android.support.annotation.IdRes;
import android.support.v4.app.FragmentManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.demo.example.sss.capstone.Activities.IFragment;
import com.demo.example.sss.capstone.Activities.MainPageActivity;
import com.demo.example.sss.capstone.DBPackage.DatabaseManager;
import com.demo.example.sss.capstone.DBPackage.MySingleton;
import com.demo.example.sss.capstone.R;
import com.demo.example.sss.capstone.model.Products;
import com.demo.example.sss.capstone.validation.Validation;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link PlaceOrderCus.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link PlaceOrderCus#newInstance} factory method to
 * create an instance of this fragment.
 */
public class PlaceOrderCus extends android.support.v4.app.Fragment implements IFragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;
    RadioGroup radioGrAdrs,radioGrpDeliv;
    RadioButton myLocRBtn,myAddressRBtn,newAddressRBtn,pickUpdRBtn,delivRBtn;
    SharedPreferences sharedPreferences;
    TextView textViewPlTotal;
    Button placeOrder;
    boolean deliveryOption;
    String addressType;
    LinearLayout newAddressLayout;
    EditText streetAddress,streetAddress2,city,state,zipcode;
    Location customerLocation;


    private OnFragmentInteractionListener mListener;

    public PlaceOrderCus() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment PlaceOrderCus.
     */
    // TODO: Rename and change types and number of parameters
    public static PlaceOrderCus newInstance(String param1, String param2) {
        PlaceOrderCus fragment = new PlaceOrderCus();
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
        View view = inflater.inflate(R.layout.fragment_place_order_cus, container, false);
        customerLocation = new Location("Customer Location");
        radioGrAdrs = view.findViewById(R.id.radioGroupAddress);
        radioGrpDeliv = view.findViewById(R.id.radioGroupDeliv);
            myLocRBtn = view.findViewById(R.id.myLocRBtn);
            myAddressRBtn = view.findViewById(R.id.myAddressRBtn);
            newAddressRBtn = view.findViewById(R.id.newAddressRBtn);
            pickUpdRBtn = view.findViewById(R.id.pickUpRadioBtn);
            delivRBtn = view.findViewById(R.id.deliveryRadioBtn);
            textViewPlTotal = view.findViewById(R.id.textViewPlTotal);
             newAddressLayout = view.findViewById(R.id.newAddressLayout);
            newAddressLayout.setVisibility(View.GONE);
            streetAddress = view.findViewById(R.id.etPlaceOrderStreetAddress);
            streetAddress2 = view.findViewById(R.id.etPlaceOrderStreetAddress2);
            city = view.findViewById(R.id.etPlaceOrderCity);
            state = view.findViewById(R.id.etPlaceOrderState);
            zipcode = view.findViewById(R.id.etPlaceOrderZip);
            sharedPreferences = getActivity().getSharedPreferences("userinfo", Context.MODE_PRIVATE);
            placeOrder = view.findViewById(R.id.placeOrderBtn);
        placeOrder.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (!checkRadioGroup()){
                    Toast.makeText(getActivity(), "please choose on above", Toast.LENGTH_SHORT).show();
                }else{
//                        setDataToDatabase();
                    sendData();
                }
            }
        });
//        getDataFromDatabase();
        getData();
        radioGroupAddress();
        radioGrpDelivery();
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
            Toast.makeText(getActivity(), "Oops!", Toast.LENGTH_SHORT).show();
        }else   if (response.equals("placed")){
            Toast.makeText(getActivity(), "The Order has been placed!", Toast.LENGTH_SHORT).show();
            MyOrdersCusFragment myOrdersCusFragment = new MyOrdersCusFragment();
            FragmentManager fragmentManager = getFragmentManager();
            fragmentManager.beginTransaction().replace(
                    R.id.fragmentContentId, myOrdersCusFragment,
                    myOrdersCusFragment.getTag()).commit();
        }else{
            JSONObject parentObject = null;
            try {
                parentObject = new JSONObject(response);
                JSONArray parentArray = parentObject.getJSONArray("cartproducts");
                double total = 0;
                for (int i = 0; i < parentArray.length(); i++) {
                    JSONObject finalObject = parentArray.getJSONObject(i);
                    total += finalObject.getInt("quantity") * finalObject.getDouble("price");
                    textViewPlTotal.setText("Total : $"+total);
                }

            } catch (JSONException e) {
                e.printStackTrace();
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

    private boolean checkRadioGroup(){
        if (radioGrAdrs.getCheckedRadioButtonId() == -1 || radioGrpDeliv.getCheckedRadioButtonId() == -1)
        {
           return false;
        }
        else
        {
            return true;
        }
    }

    private void radioGroupAddress(){
        radioGrAdrs.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup radioGroup, @IdRes int i) {
                switch (i){
                    case R.id.myLocRBtn:
                        addressType = "myLocation";
                        newAddressLayout.setVisibility(View.GONE);
                        break;
                    case R.id.myAddressRBtn:
                        addressType = "myAddress";
                        newAddressLayout.setVisibility(View.GONE);
                        break;
                    case R.id.newAddressRBtn:
                        addressType="newAddress";
                        newAddressLayout.setVisibility(View.VISIBLE);
                        break;
                }
            }
        });
    }

    private void radioGrpDelivery(){
        radioGrpDeliv.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup radioGroup, @IdRes int i) {
                switch (i){
                    case R.id.pickUpRadioBtn:
                        deliveryOption = false;
                        break;
                    case R.id.deliveryRadioBtn:
                        deliveryOption = true;
                       break;
                }
            }
        });
    }
    public void getData(){
        DatabaseManager databaseManager = new DatabaseManager(getContext(),
                "http://34.203.215.247/selectQntyAndPrice.php",
                this);
        Map<String,String> params = new HashMap<>();
        params.put("userid",sharedPreferences.getString("userID","defaultvalue"));
        databaseManager.getDataFromDatabase(params);
    }

    public void sendData(){
        DatabaseManager databaseManager = new DatabaseManager(getContext(),
                "http://34.203.215.247/placeOrder.php",
                this);
        Map<String,String> params = new HashMap<>();
        params.put("cusID",sharedPreferences.getString("userID","defaultvalue"));
        if (addressType.equals("myLocation")) {
            params.put("latit", "" + MainPageActivity.userLocation.getLatitude());
            params.put("longit", "" + MainPageActivity.userLocation.getLongitude());
        }else if (addressType.equals("newAddress")){
                if (!validated()){
                    Toast.makeText(getActivity(), "Please Enter correct Address", Toast.LENGTH_SHORT).show();
                    return;
                }else if(getLatLong()){
                    params.put("latit", "" + customerLocation.getLatitude());
                    params.put("longit", "" + customerLocation.getLongitude());
                }
            Log.i("inside","newAddress");
        }else{
            params.put("latit", "");
            params.put("longit", "");
        }
        params.put("addressType",addressType);
       if (deliveryOption)
            params.put("deliveryOption","delivery");
       else
           params.put("deliveryOption","pickup");
        Log.i("inside","sendData");
        databaseManager.sendDataToDatabase(params);
    }

    private boolean getLatLong(){
            String strAddress = streetAddress.getText().toString()+city.getText().toString()+
                    state.getText().toString()+zipcode.getText().toString();
            Geocoder geocoder = new Geocoder(getActivity());
            List<Address> address = null;
            try {
                address = geocoder.getFromLocationName(strAddress, 1);
            } catch (IOException e) {
                e.printStackTrace();
            }
            if (address == null || address.size() == 0) {
                return false;
            }
            Address location = address.get(0);
            customerLocation.setLatitude(location.getLatitude());
            customerLocation.setLongitude(location.getLongitude());
            return true;
    }

    private boolean validated(){
        Validation valid = new Validation();
        if( valid.validateEditTexts(streetAddress)
                && valid.validateEditTexts(city)
                && valid.validateEditTexts(state)
                && valid.validateEditTexts(zipcode)){
            return true;
        }else{
            return false;
        }
    }
}


















