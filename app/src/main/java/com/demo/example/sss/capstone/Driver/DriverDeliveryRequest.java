package com.demo.example.sss.capstone.Driver;

import android.content.Context;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.demo.example.sss.capstone.Activities.IFragment;
import com.demo.example.sss.capstone.Adapters.DrivDelivRequest;
import com.demo.example.sss.capstone.DBPackage.DatabaseManager;
import com.demo.example.sss.capstone.DBPackage.MySingleton;
import com.demo.example.sss.capstone.R;
import com.demo.example.sss.capstone.model.Products;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.security.Provider;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link DriverDeliveryRequest.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link DriverDeliveryRequest#newInstance} factory method to
 * create an instance of this fragment.
 */
public class DriverDeliveryRequest extends android.support.v4.app.Fragment implements IFragment{
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;
    private RecyclerView rvDriDelivRqst;
    SharedPreferences sharedPreferences;
    private android.support.v4.app.FragmentManager fragmentManager;
    private LinearLayoutManager linearLayoutManager;
    private ArrayList<Products> orderIDsAL;
    private DrivDelivRequest drivDelivRequest;
    private TextView txtDeliveryRequests;

    private OnFragmentInteractionListener mListener;

    public DriverDeliveryRequest() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment DriverDeliveryRequest.
     */
    // TODO: Rename and change types and number of parameters
    public static DriverDeliveryRequest newInstance(String param1, String param2) {
        DriverDeliveryRequest fragment = new DriverDeliveryRequest();
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
        View view = inflater.inflate(R.layout.fragment_driver_delivery_request, container, false);
        sharedPreferences = getActivity().getSharedPreferences("userinfo", Context.MODE_PRIVATE);
        rvDriDelivRqst = view.findViewById(R.id.rvDriDelivRqst);
        txtDeliveryRequests = view.findViewById(R.id.txtDeliveryRequests);
        rvDriDelivRqst.setHasFixedSize(true);
        txtDeliveryRequests.setVisibility(View.GONE);
        linearLayoutManager = new LinearLayoutManager(getActivity());
        rvDriDelivRqst.setLayoutManager(linearLayoutManager);
        fragmentManager = getFragmentManager();
        ScheduledExecutorService exec = Executors.newSingleThreadScheduledExecutor();
        exec.scheduleAtFixedRate(new Runnable() {
            @Override
            public void run() {
                getData();
            }
        }, 0, 10, TimeUnit.SECONDS);
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


    public void getData(){
        DatabaseManager manager = new DatabaseManager(getContext(),
                "http://34.203.215.247/selectDriverDeliveryRequest.php",
                this);
        Map<String,String> params = new HashMap<>();
        params.put("","");
        manager.getDataFromDatabase(params);
    }

    @Override
    public void databaseResponse(String response) {
        JSONObject parentObject = null;
        orderIDsAL = new ArrayList<>();
        if (response.equals("failed")){
            if(isAdded()) {
                txtDeliveryRequests.setVisibility(View.VISIBLE);
            }
        }else {
            try {
                txtDeliveryRequests.setVisibility(View.GONE);
                parentObject = new JSONObject(response);
                JSONArray parentArray = parentObject.getJSONArray("deliveryRequests");
                Products product;
                int tempProvID = 0,tempCusID = 0;
                for (int i = 0; i < parentArray.length(); i++) {
                    JSONObject finalObject = parentArray.getJSONObject(i);
//                    orderIDsAL.add(Integer.parseInt(finalObject.getString("Order_ID")));
                    int providerID = Integer.parseInt(finalObject.getString("Prov_ID"));
                    int customerID = Integer.parseInt(finalObject.getString("Cus_ID"));
                    int orderID = Integer.parseInt(finalObject.getString("Order_Id"));
                   if (tempProvID != 0 && tempCusID !=0){
                       if(tempProvID == providerID&&
                               tempCusID == customerID){

                       }else {
                           product =new Products();
                           product.provid = ""+providerID;
                           product.cusID = ""+customerID;
                           product.orderNumber= ""+orderID;
                           orderIDsAL.add(product);
                           tempProvID = providerID;
                           tempCusID = customerID;
                       }
                   }else{
                       product =new Products();
                       product.provid = ""+providerID;
                       product.cusID = ""+customerID;
                       product.orderNumber= ""+orderID;
                       orderIDsAL.add(product);
                       tempProvID = providerID;
                       tempCusID = customerID;
                   }
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
        if (!this.isAdded()){

        }
        drivDelivRequest = new DrivDelivRequest(getContext(),
                fragmentManager,orderIDsAL,getActivity()
                ,sharedPreferences.getString("userID","defaultvalue"));
        rvDriDelivRqst.setAdapter(drivDelivRequest);
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
