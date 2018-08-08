package com.demo.example.sss.capstone.Activities;

import android.app.AlertDialog;
import android.app.ListActivity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.app.Fragment;
import android.os.Message;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.helper.ItemTouchHelper;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.demo.example.sss.capstone.Adapters.CartAdapter;
import com.demo.example.sss.capstone.Adapters.MessagesAdapter;
import com.demo.example.sss.capstone.DBPackage.DatabaseManager;
import com.demo.example.sss.capstone.DBPackage.MySingleton;
import com.demo.example.sss.capstone.R;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link ChatMessages.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link ChatMessages#newInstance} factory method to
 * create an instance of this fragment.
 */
public class ChatMessages extends android.support.v4.app.Fragment implements IFragment{
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;
    private FloatingActionButton floatingButton;
    private EditText messageText;
    private RecyclerView rvMessages;
    private SharedPreferences sharedPreferences;
    private OnFragmentInteractionListener mListener;
    android.support.v4.app.FragmentManager fragmentManager;
    LinearLayoutManager linearLayoutManager;
    private MessagesAdapter messagesAdapter;
    private String userType;
    private ArrayList<com.demo.example.sss.capstone.model.Message> messagesAL;

    public ChatMessages() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment ChatMessages.
     */
    // TODO: Rename and change types and number of parameters
    public static ChatMessages newInstance(String param1, String param2) {
        ChatMessages fragment = new ChatMessages();
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
        View view = inflater.inflate(R.layout.fragment_chat_messages, container, false);

        floatingButton = view.findViewById(R.id.fab);
        messageText = view.findViewById(R.id.messageText);
        rvMessages = view.findViewById(R.id.list_of_messages);
        rvMessages.setHasFixedSize(true);
        linearLayoutManager = new LinearLayoutManager(getActivity());
        fragmentManager = getFragmentManager();
        rvMessages.setLayoutManager(linearLayoutManager);
        sharedPreferences = getActivity().getSharedPreferences("userinfo", Context.MODE_PRIVATE);
        userType = sharedPreferences.getString("type","defaultvalue");

        getData();
        floatingButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                postMessage();
            }
        });
        ScheduledExecutorService exec = Executors.newSingleThreadScheduledExecutor();
        exec.scheduleAtFixedRate(new Runnable() {
            @Override
            public void run() {
                getData();
            }
        }, 0, 5, TimeUnit.SECONDS);
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
            Log.i("resp",response);
        }else {
            JSONObject parentObject = null;
            messagesAL = new ArrayList<>();
            try {
                parentObject = new JSONObject(response);
                JSONArray parentArray = parentObject.getJSONArray("userMessages");
                for (int i = 0; i < parentArray.length(); i++) {
                    JSONObject finalObject = parentArray.getJSONObject(i);
                    messagesAL.add(new com.demo.example.sss.capstone.model.Message(
                            finalObject.getString("Message"),
                            finalObject.getString("userType"),
                            finalObject.getString("M_Date"),
                            finalObject.getString("User_ID")));
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
            if(isAdded()) {
                messagesAdapter = new MessagesAdapter(getActivity().getApplicationContext(),
                        messagesAL, sharedPreferences.getString("type", "defaultvalue"));
                rvMessages.setAdapter(messagesAdapter);
                rvMessages.scrollToPosition(messagesAL.size() - 1);
            }else{
            }
        }
    }
    public void getData(){

        DatabaseManager databaseManager = new DatabaseManager(getContext(),
                "http://34.203.215.247/displayChatMessages.php",
                this);
        Map<String,String> params = new HashMap<>();
        if(userType.equals("Manager")){
            params.put("userID",getArguments().getString("userID"));
        }else{
            params.put("userID",sharedPreferences.getString("uID","defaultvalue"));
        }
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

    private void postMessage(){
        String url = "http://34.203.215.247/SavechatMessages.php";
        StringRequest stringRequest = new StringRequest(Request.Method.POST,
                url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        JSONObject parentObject = null;
                        if (response.equals("saved")){
                            messageText.setText("");
                        }else {
                            //Toast.makeText(getActivity(), "Oops, Something went wrong", Toast.LENGTH_SHORT).show();
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
                if(sharedPreferences.getString("type","defaultvalue").equals("Manager")){
                    params.put("userID",getArguments().getString("userID"));
                }else{
                    params.put("userID",sharedPreferences.getString("uID","defaultvalue"));
                }
                params.put("message",messageText.getText().toString());
                params.put("userType",sharedPreferences.getString("type","defaultvalue"));
                return params;
            }
        };
        MySingleton.getInstance(getActivity().getApplicationContext()).addToRequestQueue(stringRequest);
    }

}
