package com.demo.example.sss.capstone.Customer;

import android.content.Context;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.app.Fragment;
import android.provider.Telephony;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RatingBar;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.demo.example.sss.capstone.Activities.IFragment;
import com.demo.example.sss.capstone.Adapters.CusMyOrderAdapter;
import com.demo.example.sss.capstone.DBPackage.DatabaseManager;
import com.demo.example.sss.capstone.DBPackage.MySingleton;
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
 * {@link MyOrdersCusFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link MyOrdersCusFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class MyOrdersCusFragment extends android.support.v4.app.Fragment implements IFragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;
    private android.support.v4.app.FragmentManager fragmentManager;
    private LinearLayoutManager linearLayoutManager;
    private SharedPreferences sharedPreferences;
    private RecyclerView rvMyOrders;
    private ArrayList<Products> productsAL;
    private CusMyOrderAdapter cusMyOrderAdapter;
    private OnFragmentInteractionListener mListener;

    public MyOrdersCusFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment MyOrdersCusFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static MyOrdersCusFragment newInstance(String param1, String param2) {
        MyOrdersCusFragment fragment = new MyOrdersCusFragment();
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
        View view = inflater.inflate(R.layout.fragment_my_orders_cus, container, false);
        rvMyOrders = view.findViewById(R.id.rvMyOrdersCus);
        rvMyOrders.setHasFixedSize(true);
        linearLayoutManager = new LinearLayoutManager(getActivity());
        rvMyOrders.setLayoutManager(linearLayoutManager);
        fragmentManager = getFragmentManager();
        sharedPreferences = getActivity().getSharedPreferences("userinfo", Context.MODE_PRIVATE);
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
        JSONObject parentObject = null;
        productsAL = new ArrayList<>();
        if (response.equals("failed")){
            Log.i("resp",response);
            Toast.makeText(getContext(), "Sorry, You Have Not Ordered", Toast.LENGTH_SHORT).show();
        }else {
            try {
                parentObject = new JSONObject(response);
                JSONArray parentArray = parentObject.getJSONArray("cusOrders");
                for (int i = 0; i < parentArray.length(); i++) {
                    JSONObject finalObject = parentArray.getJSONObject(i);
                  //  String address = finalObject.getString("Latitude") + finalObject.getString("Longitude");
                    productsAL.add(new Products(finalObject.getString("Product_ID"),
                            finalObject.getString("name"),
                            finalObject.getString("price"),
                            finalObject.getString("imagepath"),
                            finalObject.getString("Order_Detail_ID")
                            , finalObject.getString("Quantity")
                            , finalObject.getString("Order_Date")
                            , finalObject.getString("fullname")
                            , finalObject.getString("Status")
                            , finalObject.getString("addressline1")+",\n"+
                            finalObject.getString("city")+", "+
                            finalObject.getString("state")+", "+
                            finalObject.getString("zipcode"),
                            finalObject.getString("phoneNumber")
                            ,finalObject.getString("Delivery_Option"),
                            finalObject.getString("Rating")));
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
        cusMyOrderAdapter = new CusMyOrderAdapter(getActivity().getApplicationContext(),fragmentManager,productsAL);
        rvMyOrders.setAdapter(cusMyOrderAdapter);
        cusMyOrderAdapter.filterByDate();
        RatingProduct ratingProduct = new RatingProduct(getContext());
        ratingProduct.showDialog(productsAL,sharedPreferences.getString("userID","defaultvalue"));
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
        DatabaseManager databaseManager = new DatabaseManager(getContext(),
                "http://34.203.215.247/displayCusOrders.php",
                this);
        Map<String,String> params = new HashMap<>();
        params.put("cusID",sharedPreferences.getString("userID","defaultvalue"));
        databaseManager.getDataFromDatabase(params);
    }

}
