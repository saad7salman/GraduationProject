package com.demo.example.sss.capstone.Provider;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.app.Fragment;
import android.provider.MediaStore;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.demo.example.sss.capstone.Activities.IFragment;
import com.demo.example.sss.capstone.DBPackage.DatabaseManager;
import com.demo.example.sss.capstone.DBPackage.MySingleton;
import com.demo.example.sss.capstone.R;
import com.squareup.picasso.Picasso;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link ProviderUpdateFood.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link ProviderUpdateFood#newInstance} factory method to
 * create an instance of this fragment.
 */
public class ProviderUpdateFood extends android.support.v4.app.Fragment implements IFragment{
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";
    private final int IMG_REQUEST = 1;
    private Bitmap bitmap;


    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;
    String prodID;
    EditText pName;
    EditText pDescription;
    EditText pPrice;
    EditText estimationTime;
    EditText maxQnty;
    Spinner pCategory;
    ImageView pImage;
    Button pUpload;
    Button pPost;
    private String[] arraySpinner;
    SharedPreferences sharedPreferences;
    Bundle bundle;
    int pos=0;
    boolean isPicChanged = false;
    private OnFragmentInteractionListener mListener;

    public ProviderUpdateFood() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment ProviderUpdateFood.
     */
    // TODO: Rename and change types and number of parameters
    public static ProviderUpdateFood newInstance(String param1, String param2) {
        ProviderUpdateFood fragment = new ProviderUpdateFood();
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
        View view = inflater.inflate(R.layout.fragment_provider, container, false);
        bundle = this.getArguments();
        arraySpinner = new String[]{"Appetizer","Main Dish","Dessert","Other"};
        pName = (EditText) view.findViewById(R.id.pnameEditText);
        pDescription = (EditText) view.findViewById(R.id.pdescriptionEditText);
        pPrice = (EditText) view.findViewById(R.id.ppriceEditText);
        estimationTime = view.findViewById(R.id.pEstimationTimeEditText);
        maxQnty = view.findViewById(R.id.pMaxQntyEditText);
        pCategory = (Spinner) view.findViewById(R.id.pcategorySpinner);
        pImage = (ImageView) view.findViewById(R.id.pimageView);
        pUpload = (Button) view.findViewById(R.id.puploadButton);
        pPost = (Button) view.findViewById(R.id.ppostButton);
        pPost.setText("Update");
        sharedPreferences = getActivity().getSharedPreferences("userinfo", Context.MODE_PRIVATE);
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(getActivity(),
                android.R.layout.simple_spinner_dropdown_item,arraySpinner);
        pCategory.setAdapter(adapter);
        updateLayout();
        pPost.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
//                updateDataInDatabase();
                updateData();
            }
        });
        return view;
    }
    //////////
    private void updateLayout(){
        prodID = bundle.getString("prodID");
        pName.setText(bundle.getString("prodName"));
        pDescription.setText(bundle.getString("prodDes"));
        pPrice.setText(bundle.getString("prodPrice"));
        estimationTime.setText(bundle.getString("estimationTime"));
        maxQnty.setText(bundle.getString("maxQnty"));

        whichCategory();
        pCategory.setSelection(pos);
        //String fullUrl = "http://34.203.215.247/"+bundle.getString("prodImgPath");
        String fullUrl = bundle.getString("prodImgPath");
        Picasso.with(getContext())
                .load(fullUrl)
                .placeholder(R.drawable.ic_menu_gallery)
                .error(android.R.drawable.stat_notify_error)
                .into(pImage);
        pImage.setVisibility(View.VISIBLE);

        pUpload.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                selectImage();
            }
        });

    }
    private void selectImage(){

        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(intent.ACTION_GET_CONTENT);
        startActivityForResult(intent,IMG_REQUEST);

    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == IMG_REQUEST && resultCode == getActivity().RESULT_OK && data != null){
            Uri path = data.getData();
            try {
                bitmap = MediaStore.Images.Media.getBitmap(getActivity().getContentResolver(),path);
                pImage.setImageBitmap(bitmap);
                pImage.setVisibility(View.VISIBLE);
                isPicChanged = true;
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

    }

    private String imgToString(Bitmap bitmap){

        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.JPEG,100,byteArrayOutputStream);
        byte[] imBytes = byteArrayOutputStream.toByteArray();

        return android.util.Base64.encodeToString(imBytes, android.util.Base64.DEFAULT);

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
        if (response.equals("Updated Successfully")){
            Toast.makeText(getActivity(), "Has been update Successfully", Toast.LENGTH_SHORT).show();
            android.support.v4.app.FragmentManager fragmentManager = getFragmentManager();
            fragmentManager.beginTransaction().replace(
                    R.id.fragmentContentId, new ProviderMyProducts(),
                    new ProviderMyProducts().getTag()).commit();
        }else{
            Toast.makeText(getActivity(), "Oops, something went wrong"+response, Toast.LENGTH_SHORT).show();
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

    private void whichCategory(){

       String category =  bundle.getString("prodCategory");

       if (category.equals("Appetizer")){
           pos = 0;
       }else if (category.equals("Main Dish")){
           pos = 1;
       }else if (category.equals("Dessert")){
           pos = 2;
       }else if (category.equals("Other")){
           pos = 3;
       }
    }

    private void updateDataInDatabase(){
        String url = "http://34.203.215.247/updatProvProd.php";
        StringRequest stringRequest = new StringRequest(Request.Method.POST,
                url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        if (response.equals("Updated Successfully")){
                            Toast.makeText(getActivity(), "Has been update Successfully", Toast.LENGTH_SHORT).show();
                            android.support.v4.app.FragmentManager fragmentManager = getFragmentManager();
                            fragmentManager.beginTransaction().replace(
                                    R.id.fragmentContentId, new ProviderMyProducts(),
                                    new ProviderMyProducts().getTag()).commit();
                        }else{
                            Toast.makeText(getActivity(), "Oops, something went wrong"+response, Toast.LENGTH_SHORT).show();
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
        ) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<>();
                params.put("prodID",prodID);
                params.put("name",pName.getText().toString());
                params.put("description", pDescription.getText().toString());
                params.put("price",pPrice.getText().toString());
                params.put("category",pCategory.getSelectedItem().toString());
                if (isPicChanged){
                    params.put("image",imgToString(bitmap));
                    params.put("imgChange","Yes");
                }else{
                    params.put("image",bundle.getString("prodImgPath"));
                    params.put("imgChange","No");
                }
                params.put("provID",sharedPreferences.getString("userID","defaultvalue"));
                return params;
            }
        };
        MySingleton.getInstance(getActivity().getApplicationContext()).addToRequestQueue(stringRequest);

    }

    public void updateData(){
        DatabaseManager manager = new DatabaseManager(getContext(),
                "http://34.203.215.247/updatProvProd.php",
                this);
        Map<String, String> params = new HashMap<>();
        params.put("prodID",prodID);
        params.put("name",pName.getText().toString());
        params.put("description", pDescription.getText().toString());
        params.put("price",pPrice.getText().toString());
        params.put("estimationTime",estimationTime.getText().toString());
        params.put("maxQnty",maxQnty.getText().toString());
        params.put("category",pCategory.getSelectedItem().toString());
        if (isPicChanged){
            params.put("image",imgToString(bitmap));
            params.put("imgChange","Yes");
        }else{
            params.put("image",bundle.getString("prodImgPath"));
            params.put("imgChange","No");
        }
        params.put("provID",sharedPreferences.getString("userID","defaultvalue"));
        manager.sendDataToDatabase(params);
    }

}
