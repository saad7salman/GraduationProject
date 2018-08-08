package com.demo.example.sss.capstone.Activities;

import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.net.Uri;
import android.os.Bundle;
import android.app.Fragment;
import android.os.Handler;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.demo.example.sss.capstone.DBPackage.DatabaseManager;
import com.demo.example.sss.capstone.DBPackage.MySingleton;
import com.demo.example.sss.capstone.R;
import com.demo.example.sss.capstone.validation.Validation;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.Delayed;
import java.util.concurrent.TimeUnit;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link MyInfo.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link MyInfo#newInstance} factory method to
 * create an instance of this fragment.
 */
public class MyInfo extends android.support.v4.app.Fragment implements IFragment{
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;
    private EditText fullName;
    private EditText email;
    private EditText password;
    private EditText cellPhone;
    private EditText addressLine;
    private EditText city;
    private EditText state;
    private EditText zipCode;
    private Button updateBtn;
    SharedPreferences sharedPreferences;
    Location userLocation;
    boolean isEmailExists = true;


    private OnFragmentInteractionListener mListener;

    public MyInfo() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment MyInfo.
     */
    // TODO: Rename and change types and number of parameters
    public static MyInfo newInstance(String param1, String param2) {
        MyInfo fragment = new MyInfo();
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
        View view = inflater.inflate(R.layout.fragment_my_info, container, false);
        sharedPreferences = getActivity().getSharedPreferences("userinfo", Context.MODE_PRIVATE);
        fullName = view.findViewById(R.id.eTFullName);
        email = view.findViewById(R.id.eTEmail);
        password = view.findViewById(R.id.eTPassword);
        cellPhone = view.findViewById(R.id.eTPhoneNum);
        addressLine = view.findViewById(R.id.eTAddressLine);
        city = view.findViewById(R.id.eTCity);
        state = view.findViewById(R.id.eTState);
        zipCode = view.findViewById(R.id.eTZipCode);
        updateBtn = view.findViewById(R.id.updateBtn);
        userLocation = new Location("User Location");
        getData();
        updateBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (validated()) {
                    if (!getLatAndLong()) {
                        Toast.makeText(getActivity(), "The address is invalid, Please follow google map's format", Toast.LENGTH_LONG).show();
                        return;
                    }
                    checkEmailFromDatabase();
                }else{
                    Toast.makeText(getActivity(), "Oops,something went wrong", Toast.LENGTH_LONG).show();

                }
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
        Log.i("myInfo",response);
        if (response.equals("failed")){
            Toast.makeText(getActivity(), "Oops, Something went wrong", Toast.LENGTH_SHORT).show();
        }else {
            JSONObject parentObject = null;
            try {
                parentObject = new JSONObject(response);
                JSONArray parentArray = parentObject.getJSONArray("userInfo");
                    JSONObject finalObject = parentArray.getJSONObject(0);
                    fullName.setText(finalObject.getString("fullname"));
                    email.setText(finalObject.getString("email"));
                    password.setText(finalObject.getString("password"));
                        cellPhone.setText(finalObject.getString("phoneNumber"));
                if (finalObject.getString("addressline1") != "null") {
                    Log.i("myI",finalObject.getString("addressline1"));
                    addressLine.setText(finalObject.getString("addressline1"));
                        city.setText(finalObject.getString("city"));
                        state.setText(finalObject.getString("state"));
                        zipCode.setText(finalObject.getString("zipcode"));
                    }
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
    }
    public void getData(){
        DatabaseManager databaseManager = new DatabaseManager(getContext(),
                "http://34.203.215.247/getMyInfo.php",
                this);
        Map<String,String> params = new HashMap<>();
        params.put("userID",sharedPreferences.getString("uID","defaultvalue"));
        databaseManager.getDataFromDatabase(params);
    }
    private boolean validated(){
        Validation valid = new Validation();
        if( valid.validateEditTexts(fullName)
                && valid.validateEditTexts(email)
                && valid.validateEditTexts(password)
                && valid.validateEditTexts(cellPhone)
                && valid.validateEditTexts(addressLine)
                && valid.validateEditTexts(city)
                && valid.validateEditTexts(state)
                && valid.validateEditTexts(zipCode)
                && isEmailValid(email)
                )
        {
            return true;
        }else{
            return false;
        }
    }
    private boolean getLatAndLong() {
        String strAddress = addressLine.getText().toString()+city.getText().toString()+
                state.getText().toString()+zipCode.getText().toString();
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
        userLocation.setLatitude(location.getLatitude());
        userLocation.setLongitude(location.getLongitude());
        return true;
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

    private boolean isEmailValid(EditText emailET) {
        String email = emailET.getText().toString();
        String expression = "^[\\w\\.-]+@([\\w\\-]+\\.)+[A-Z]{2,4}$";
        Pattern pattern = Pattern.compile(expression, Pattern.CASE_INSENSITIVE);
        Matcher matcher = pattern.matcher(email);
        if (!matcher.matches()) {
            emailET.setError("please enter a proper email " + emailET.getHint());
            emailET.setBackgroundColor(Color.RED);
        }
        return matcher.matches();
    }

    private void checkEmailFromDatabase(){
        String url = "http://34.203.215.247/checkEmail.php";
        StringRequest stringRequest = new StringRequest(Request.Method.POST,
                url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        Log.i("res",response);
                        if (response.equals("email exists")){
                            Toast.makeText(getActivity(), "Email already exists", Toast.LENGTH_SHORT).show();
                            email.setError("email already exists ");
                            email.setBackgroundColor(Color.RED);
                        }else {
                            updateInfoDatabase();
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        if (error != null){
                            Toast.makeText(getActivity(), "Something went wrong", Toast.LENGTH_LONG).show();
                        }
                    }
                }
        ){
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String,String> params = new HashMap<>();
                params.put("email",email.getText().toString());
                String userID = sharedPreferences.getString("uID","defaultvalue");
                Log.i("userID",userID);
                params.put("userID",userID);

                return params;
            }

        };
        MySingleton.getInstance(getActivity().getApplicationContext()).addToRequestQueue(stringRequest);
    }
    private void updateInfoDatabase(){
        String url = "http://34.203.215.247/updateUserInfo.php";
        StringRequest stringRequest = new StringRequest(Request.Method.POST,
                url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        Log.i("resUpdate",response);
                        if (response.equals("Failed")){
                            Toast.makeText(getActivity(), "Oops!,Something went wrong", Toast.LENGTH_SHORT).show();
                        }else {
                            Toast.makeText(getActivity(), "Info Updated Successfully", Toast.LENGTH_LONG).show();
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        if (error != null){
                            Toast.makeText(getActivity(), "Something went wrong", Toast.LENGTH_LONG).show();
                        }
                    }
                }
        ){
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String,String> params = new HashMap<>();
                String userID = sharedPreferences.getString("uID","defaultvalue");
                params.put("userID",userID);
                params.put("fullname",fullName.getText().toString());
                params.put("email",email.getText().toString());
                params.put("password",password.getText().toString());
                params.put("phonenumber",cellPhone.getText().toString());
                params.put("addressline",addressLine.getText().toString());
                params.put("city",city.getText().toString());
                params.put("state",state.getText().toString());
                params.put("zipcode",zipCode.getText().toString());
                params.put("lat",""+userLocation.getLatitude());
                params.put("long",""+userLocation.getLongitude());
                return params;
            }

        };
        MySingleton.getInstance(getActivity().getApplicationContext()).addToRequestQueue(stringRequest);
    }
}
