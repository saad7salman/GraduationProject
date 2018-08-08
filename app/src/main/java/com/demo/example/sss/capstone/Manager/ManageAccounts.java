package com.demo.example.sss.capstone.Manager;

import android.content.Context;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.SearchView;
import android.widget.Toast;

import com.demo.example.sss.capstone.Activities.IFragment;
import com.demo.example.sss.capstone.Adapters.ManageAccountAdapter;
import com.demo.example.sss.capstone.DBPackage.DatabaseManager;
import com.demo.example.sss.capstone.R;
import com.demo.example.sss.capstone.model.User;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link ManageAccounts.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link ManageAccounts#newInstance} factory method to
 * create an instance of this fragment.
 */
public class ManageAccounts extends android.support.v4.app.Fragment implements IFragment,SearchView.OnQueryTextListener {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;
    private RecyclerView rvManageAcc;
    LinearLayoutManager linearLayoutManager;
    SharedPreferences sharedPreferences;
    private ArrayList<User> userAL;
    SearchView searchView;
    ManageAccountAdapter manageAccountAdapter;

    private OnFragmentInteractionListener mListener;

    public ManageAccounts() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment ManageAccounts.
     */
    // TODO: Rename and change types and number of parameters
    public static ManageAccounts newInstance(String param1, String param2) {
        ManageAccounts fragment = new ManageAccounts();
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
        setHasOptionsMenu(true);
        View view = inflater.inflate(R.layout.fragment_manage_accounts, container, false);
        rvManageAcc = view.findViewById(R.id.rvManageAccounts);
        rvManageAcc.setHasFixedSize(true);
        linearLayoutManager = new LinearLayoutManager(getActivity());
        rvManageAcc.setLayoutManager(linearLayoutManager);
        sharedPreferences = getActivity().getSharedPreferences("userinfo", Context.MODE_PRIVATE);
        searchView = view.findViewById(R.id.searchViewManageAccount);
        searchView.setOnQueryTextListener(this);
        searchView.setQueryHint("Search ...");
        getData();
        return  view;
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
            Toast.makeText(getActivity(), "Oops, Something went wrong", Toast.LENGTH_SHORT).show();
        }else {
            JSONObject parentObject = null;
            userAL = new ArrayList<>();
            try {
                parentObject = new JSONObject(response);
                JSONArray parentArray = parentObject.getJSONArray("userList");

                for (int i = 0; i < parentArray.length(); i++) {
                    JSONObject finalObject = parentArray.getJSONObject(i);
                    userAL.add(new User(finalObject.getString("User_ID"),
                            finalObject.getString("fullname"),
                            finalObject.getString("type"),
                            finalObject.getString("AccountStatus"),
                            ""));
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
            manageAccountAdapter= new ManageAccountAdapter(getActivity().getApplicationContext(), userAL);
            rvManageAcc.setAdapter(manageAccountAdapter);
        }
    }

    @Override
    public boolean onQueryTextSubmit(String s) {
        return false;
    }

    @Override
    public boolean onQueryTextChange(String s) {
        s = s.toLowerCase();
        ArrayList<User> newList = new ArrayList<User>();

        for(User user : userAL){
            String fullName = user.fullName.toLowerCase();
            String id = user.id.toLowerCase();
            if(fullName.contains(s) || id.contains(s))
                newList.add(user);
        }
        manageAccountAdapter.filterAL(newList);
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

    public void getData(){
        DatabaseManager databaseManager = new DatabaseManager(getContext(),
                "http://34.203.215.247/manageAccounts.php",
                this);
        Map<String,String> params = new HashMap<>();
        params.put("","");
        databaseManager.getDataFromDatabase(params);
    }
}
