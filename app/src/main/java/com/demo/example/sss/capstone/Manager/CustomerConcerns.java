package com.demo.example.sss.capstone.Manager;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.helper.ItemTouchHelper;
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
import com.demo.example.sss.capstone.Adapters.ChatGroupAdapter;
import com.demo.example.sss.capstone.DBPackage.DatabaseManager;
import com.demo.example.sss.capstone.DBPackage.MySingleton;
import com.demo.example.sss.capstone.R;
import com.demo.example.sss.capstone.model.ChatGroup;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link CustomerConcerns.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link CustomerConcerns#newInstance} factory method to
 * create an instance of this fragment.
 */
public class CustomerConcerns extends android.support.v4.app.Fragment implements IFragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;
    private RecyclerView rvChatGroup;
    private SharedPreferences sharedPreferences;
    LinearLayoutManager linearLayoutManager;
    private ChatGroupAdapter adapter;
    private ArrayList<ChatGroup> chatGroupsAL;
    private OnFragmentInteractionListener mListener;

    public CustomerConcerns() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment CustomerConcerns.
     */
    // TODO: Rename and change types and number of parameters
    public static CustomerConcerns newInstance(String param1, String param2) {
        CustomerConcerns fragment = new CustomerConcerns();
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
        View view = inflater.inflate(R.layout.fragment_customer_concerns, container, false);
        rvChatGroup = view.findViewById(R.id.rvMessageGroup);
        rvChatGroup.setHasFixedSize(true);
        linearLayoutManager = new LinearLayoutManager(getActivity());
        rvChatGroup.setLayoutManager(linearLayoutManager);
        sharedPreferences = getActivity().getSharedPreferences("userinfo", Context.MODE_PRIVATE);
        ItemTouchHelper itemTouchHelper = new ItemTouchHelper(simpleCallback);
        itemTouchHelper.attachToRecyclerView(rvChatGroup);
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
        DatabaseManager databaseManager = new DatabaseManager(getContext(),
                "http://34.203.215.247/getChatGroups.php",
                this);
        Map<String,String> params = new HashMap<>();
        params.put("","");
        databaseManager.getDataFromDatabase(params);
    }

    @Override
    public void databaseResponse(String response) {
        if (response.equals("failed")){
            //Toast.makeText(getActivity(), "Oops, Not Good" +", "+response, Toast.LENGTH_SHORT).show();
        }else {
            JSONObject parentObject = null;
            chatGroupsAL = new ArrayList<>();
            try {
                parentObject = new JSONObject(response);
                JSONArray parentArray = parentObject.getJSONArray("chatGroups");
                for (int i = 0; i < parentArray.length(); i++) {
                    JSONObject finalObject = parentArray.getJSONObject(i);
                    chatGroupsAL.add(new ChatGroup(
                            finalObject.getString("Chat_ID"),
                            finalObject.getString("Chat_Create_Date"),
                            finalObject.getString("User_ID"),
                            finalObject.getString("fullname")));
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
            adapter = new ChatGroupAdapter(getActivity().getApplicationContext(),
                    chatGroupsAL,getFragmentManager());
            rvChatGroup.setAdapter(adapter);
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

    ItemTouchHelper.SimpleCallback simpleCallback = new ItemTouchHelper.SimpleCallback(0, ItemTouchHelper.LEFT) {
        @Override
        public boolean onMove(RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder, RecyclerView.ViewHolder target) {
            return false;
        }

        @Override
        public void onSwiped(final RecyclerView.ViewHolder viewHolder, int direction) {
            final int position = viewHolder.getAdapterPosition(); //get position which is swipe

            if (direction == ItemTouchHelper.LEFT) {    //if swipe left

                AlertDialog.Builder builder = new AlertDialog.Builder(getActivity()); //alert for confirm to delete
                builder.setMessage("Are you sure to Close The chat?");    //set message

                builder.setPositiveButton("REMOVE", new DialogInterface.OnClickListener() { //when click on DELETE
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        adapter.notifyItemRemoved(position);    //item removed from recylcerview
                        //query for delete
                        ChatGroup chatGroup = chatGroupsAL.get(position);
                        closeChat(chatGroup.chatID);
                        chatGroupsAL.remove(position);  //then remove item

                        return;
                    }
                }).setNegativeButton("CANCEL", new DialogInterface.OnClickListener() {  //not removing items if cancel is done
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        adapter.notifyItemRemoved(position + 1);    //notifies the RecyclerView Adapter that data in adapter has been removed at a particular position.
                        adapter.notifyItemRangeChanged(position, adapter.getItemCount());   //notifies the RecyclerView Adapter that positions of element in adapter has been changed from position(removed element index to end of list), please update it.
                        return;
                    }
                }).show();  //show alert dialog
            }
        }
    };

    private void closeChat(final String chatID){
        String url = "http://34.203.215.247/closeChat.php";
        StringRequest stringRequest = new StringRequest(Request.Method.POST,
                url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        JSONObject parentObject = null;
                        if (response.equals("Chat Closed successfully")){
                            Toast.makeText(getActivity(), "Chat Closed successfully", Toast.LENGTH_SHORT).show();

                        }else {
                            Toast.makeText(getActivity(), "Oops, Something went wrong", Toast.LENGTH_LONG).show();
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
                params.put("chatID",chatID);
                return params;
            }
        };
        MySingleton.getInstance(getActivity().getApplicationContext()).addToRequestQueue(stringRequest);
    }

}
