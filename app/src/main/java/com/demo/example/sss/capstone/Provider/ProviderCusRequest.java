package com.demo.example.sss.capstone.Provider;

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
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.demo.example.sss.capstone.Activities.IFragment;
import com.demo.example.sss.capstone.DBPackage.DatabaseManager;
import com.demo.example.sss.capstone.DBPackage.MySingleton;
import com.demo.example.sss.capstone.Adapters.ProvCusRequests;
import com.demo.example.sss.capstone.R;
import com.demo.example.sss.capstone.model.Products;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link ProviderCusRequest.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link ProviderCusRequest#newInstance} factory method to
 * create an instance of this fragment.
 */
public class ProviderCusRequest extends android.support.v4.app.Fragment implements IFragment{
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;
    SharedPreferences sharedPreferences;
    public RecyclerView rvCusRequests;
    private android.support.v4.app.FragmentManager fragmentManager;
    private LinearLayoutManager linearLayoutManager;
    private ArrayList<Products> productsAL;
    private ProvCusRequests provCusRequests;
    private OnFragmentInteractionListener mListener;

    public ProviderCusRequest() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment ProviderCusRequest.
     */
    // TODO: Rename and change types and number of parameters
    public static ProviderCusRequest newInstance(String param1, String param2) {
        ProviderCusRequest fragment = new ProviderCusRequest();
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
        View view = inflater.inflate(R.layout.fragment_provider_cus_request, container, false);
        sharedPreferences = getActivity().getSharedPreferences("userinfo", Context.MODE_PRIVATE);
        rvCusRequests = view.findViewById(R.id.rvProvCusRequest);
        rvCusRequests.setHasFixedSize(true);
        linearLayoutManager = new LinearLayoutManager(getActivity());
        rvCusRequests.setLayoutManager(linearLayoutManager);
        fragmentManager = getFragmentManager();
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


    public void getData(){
        DatabaseManager manager = new DatabaseManager(getContext(),
                "http://34.203.215.247/provCusRequest.php",
                this);
        Map<String,String> params = new HashMap<>();
        params.put("provID",sharedPreferences.getString("userID","defaultvalue"));
        params.put("status","In The Kitchen");
        manager.getDataFromDatabase(params);
    }

    @Override
    public void databaseResponse(String response) {
        JSONObject parentObject = null;
        productsAL = new ArrayList<>();
        if (response == "failed"){
            Toast.makeText(getContext(), "Sorry, You Have No Order In The Kitchen", Toast.LENGTH_SHORT).show();
            return;
        }else {
            try {
                parentObject = new JSONObject(response);
                JSONArray parentArray = parentObject.getJSONArray("provCusRequests");

                for (int i = 0; i < parentArray.length(); i++) {
                    Products product = new Products();
                    JSONObject finalObject = parentArray.getJSONObject(i);
                    product.orderNumber = finalObject.getString("Order_Detail_ID");
                    product.name =  finalObject.getString("name");
                    product.quantity = finalObject.getString("Quantity");
                    product.status = finalObject.getString("Status");
                    product.customerName = finalObject.getString("fullname");
                    product.imagePath = finalObject.getString("imagepath");
                    if (finalObject.getString("Special_Request").equals("")
                            || finalObject.getString("Special_Request") == null
                            || finalObject.getString("Special_Request").equals("null")){
                        product.specialRequest = "None";
                    }else {
                        product.specialRequest = finalObject.getString("Special_Request");
                    }

                    productsAL.add(product);
                }
            } catch (JSONException e) {
                //Toast.makeText(getContext(), "Sorry, You Have No Order In The Kitchen", Toast.LENGTH_SHORT).show();
                e.printStackTrace();
            }
            provCusRequests = new ProvCusRequests(getActivity().getApplicationContext(),
                    fragmentManager, productsAL, getActivity(),"Prepare");
            rvCusRequests.setAdapter(provCusRequests);
            provCusRequests.notifyDataSetChanged();
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

}
