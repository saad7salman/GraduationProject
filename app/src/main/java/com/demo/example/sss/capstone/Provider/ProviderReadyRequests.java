package com.demo.example.sss.capstone.Provider;

import android.content.Context;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.demo.example.sss.capstone.Activities.IFragment;
import com.demo.example.sss.capstone.Adapters.ProvCusRequests;
import com.demo.example.sss.capstone.DBPackage.DatabaseManager;
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
 * {@link ProviderReadyRequests.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link ProviderReadyRequests#newInstance} factory method to
 * create an instance of this fragment.
 */
public class ProviderReadyRequests extends android.support.v4.app.Fragment implements IFragment{
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;
    private RecyclerView rvProvReadyRequests;
    private SharedPreferences sharedPreferences;
    private LinearLayoutManager linearLayoutManager;
    private OnFragmentInteractionListener mListener;
    private android.support.v4.app.FragmentManager fragmentManager;
    private ArrayList<Products> productsAL;
    private ProvCusRequests provCusRequests;

    public ProviderReadyRequests() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment ProviderReadyRequests.
     */
    // TODO: Rename and change types and number of parameters
    public static ProviderReadyRequests newInstance(String param1, String param2) {
        ProviderReadyRequests fragment = new ProviderReadyRequests();
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
        View view = inflater.inflate(R.layout.fragment_provider_ready_requests, container, false);
        rvProvReadyRequests = view.findViewById(R.id.rvProvReadyRequests);
        sharedPreferences = getActivity().getSharedPreferences("userinfo", Context.MODE_PRIVATE);
        rvProvReadyRequests.setHasFixedSize(true);
        linearLayoutManager = new LinearLayoutManager(getActivity());
        rvProvReadyRequests.setLayoutManager(linearLayoutManager);
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

    @Override
    public void databaseResponse(String response) {
        JSONObject parentObject = null;
        productsAL = new ArrayList<>();
        if (response.equals("failed")){
            Toast.makeText(getContext(), "Sorry, You Have Not Ordered", Toast.LENGTH_SHORT).show();
        }else {
            try {
                parentObject = new JSONObject(response);
                JSONArray parentArray = parentObject.getJSONArray("provCusRequests");
                for (int i = 0; i < parentArray.length(); i++) {
                    JSONObject finalObject = parentArray.getJSONObject(i);
                    productsAL.add(new Products(finalObject.getString("Order_Detail_ID"),
                            finalObject.getString("name"),
                            finalObject.getString("Quantity"),
                            finalObject.getString("Status")
                            , finalObject.getString("fullname")
                            ,finalObject.getString("imagepath")));
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }

            provCusRequests = new ProvCusRequests(getActivity().getApplicationContext(),
                    fragmentManager, productsAL, getActivity(),"Ready");
            rvProvReadyRequests.setAdapter(provCusRequests);
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

    public void getData(){
        DatabaseManager manager = new DatabaseManager(getContext(),
                "http://34.203.215.247/provCusRequest.php",
                this);
        Map<String,String> params = new HashMap<>();
        params.put("provID",sharedPreferences.getString("userID","defaultvalue"));
        params.put("status","Ready To Go");
        manager.getDataFromDatabase(params);
    }
}
