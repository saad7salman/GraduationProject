package com.demo.example.sss.capstone.DBPackage;

import android.content.Context;
import android.util.Log;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.demo.example.sss.capstone.Activities.IFragment;

import java.util.Map;

/**
 * Created by SSS on 11/16/17.
 */

public class DatabaseManager {

    Context context;
    String url;
    IFragment fragment;
    public DatabaseManager(Context context, String url, IFragment fragment) {
        this.context = context;
        this.url = url;
        this.fragment = fragment;
    }

    public void sendDataToDatabase(final Map<String,String> params){
        StringRequest request = new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                fragment.databaseResponse(response);
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                if (error != null){
                    Toast.makeText(context, "Something went wrong", Toast.LENGTH_LONG).show();
                }
            }
        }){
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                return params;
            }
        };
        MySingleton.getInstance(context).addToRequestQueue(request);
    }

    public void getDataFromDatabase(final Map<String,String> params){
        StringRequest request = new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                    fragment.databaseResponse(response);
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                if (error != null){
                    Log.i("error",error.toString());
                }
            }
        }){
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                return params;
            }
        };
        MySingleton.getInstance(context).addToRequestQueue(request);
    }
}
