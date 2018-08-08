package com.demo.example.sss.capstone.Adapters;

import android.content.Context;
import android.graphics.Color;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CompoundButton;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.demo.example.sss.capstone.DBPackage.MySingleton;
import com.demo.example.sss.capstone.R;
import com.demo.example.sss.capstone.model.User;

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by SSS on 2/3/18.
 */

public class ManageAccountAdapter extends RecyclerView.Adapter<ManageAccountAdapter.MAccountViewHolder>{

    private Context context;
    private ArrayList<User> userAL;
    private boolean checkNotifyChanged;

    public ManageAccountAdapter(Context context, ArrayList<User> userAL) {
        this.context = context;
        this.userAL = userAL;
    }
    @Override
    public MAccountViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        View view = inflater.from(parent.getContext())
                .inflate(R.layout.manage_accounts_cardview_layout,parent,false);
        MAccountViewHolder mAccountViewHolder= new MAccountViewHolder(view);
        return mAccountViewHolder;
    }

    @Override
    public void onBindViewHolder(MAccountViewHolder holder, int position) {
                final User user = userAL.get(position);
                holder.tvUserName.setText("Name: "+user.fullName);
                holder.tvID.setText("ID: "+user.id);
                holder.tvType.setText("Type: "+user.type);
        holder.userStatus.setOnCheckedChangeListener(null);
        Log.i("userList",userAL.toString());
        if(user.status.equals("Active")) {
                    holder.userStatus.setChecked(true);
                    holder.userStatus.setText("Active");
                    holder.userStatus.setTextColor(Color.GREEN);
                }
                else {
                    holder.userStatus.setChecked(false);
                    holder.userStatus.setText("Inactive");
                    holder.userStatus.setTextColor(Color.RED);
                }
                holder.userStatus.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                    @Override
                    public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                        if(b == true){
                            compoundButton.setText("Active");
                            compoundButton.setTextColor(Color.GREEN);
                            changeUserAccountStatus(user.id,"Active");
                        }else{
                            compoundButton.setText("Inactive");
                            compoundButton.setTextColor(Color.RED);
                            changeUserAccountStatus(user.id,"Inactive");

                        }
                    }
                });
    }

    @Override
    public int getItemCount() {
        if (userAL != null){
            return userAL.size();
        }
        return 0;
    }

    public void filterAL(ArrayList<User> newList){

        userAL = new ArrayList<>();
        userAL.addAll(newList);
        notifyDataSetChanged();

    }

    public static class MAccountViewHolder extends RecyclerView.ViewHolder{

        CardView cvManageAcc;
        TextView tvUserName;
        TextView tvID;
        TextView tvType;
        Switch userStatus;
        public MAccountViewHolder(View manageView) {
            super(manageView);
            cvManageAcc = manageView.findViewById(R.id.cvMAccount);
            tvUserName = manageView.findViewById(R.id.tvUserName);
            tvID = manageView.findViewById(R.id.tvID);
            tvType = manageView.findViewById(R.id.tvUserType);
            userStatus = manageView.findViewById(R.id.userStatusSwitch);
        }
    }

    private void changeUserAccountStatus(final String userID, final String accStatus){
        String url = "http://34.203.215.247/changeUserAccountStatus.php";
        StringRequest stringRequest = new StringRequest(Request.Method.POST,
                url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        JSONObject parentObject = null;
                        if (response.equals("Account Status Updated Successfully")){
                            Toast.makeText(context, "Account Status Updated Successfully", Toast.LENGTH_SHORT).show();

                        }else {
                            Toast.makeText(context, "Oops, Something went wrong", Toast.LENGTH_SHORT).show();
                            Log.i("changeStatus",response);
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        if (error != null){
                            Toast.makeText(context, "Something went wrong", Toast.LENGTH_LONG).show();
                        }
                    }
                }
        ){
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String,String> params = new HashMap<>();
                params.put("userID",userID);
                params.put("accStatus",accStatus);

                return params;
            }
        };
        MySingleton.getInstance(context.getApplicationContext()).addToRequestQueue(stringRequest);
    }
}
