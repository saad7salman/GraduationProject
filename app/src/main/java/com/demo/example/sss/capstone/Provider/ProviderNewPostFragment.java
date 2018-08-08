package com.demo.example.sss.capstone.Provider;


import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.app.Fragment;
import android.provider.MediaStore;
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

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link ProviderNewPostFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class ProviderNewPostFragment extends android.support.v4.app.Fragment implements IFragment{
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";
    private final int IMG_REQUEST = 1;
    private Bitmap bitmap;
    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;
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

    public ProviderNewPostFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment ProviderNewPostFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static ProviderNewPostFragment newInstance(String param1, String param2) {
        ProviderNewPostFragment fragment = new ProviderNewPostFragment();
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
        View view = inflater.inflate(R.layout.fragment_provider, container, false);
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
        sharedPreferences = getActivity().getSharedPreferences("userinfo", Context.MODE_PRIVATE);
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(getActivity(),
                android.R.layout.simple_spinner_dropdown_item,arraySpinner);
         pCategory.setAdapter(adapter);
        pUpload.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                openGallery();
            }
        });
        pPost.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
//                sendDataToDB();
                sendData();
            }
        });

        return view;
    }

    private void openGallery(){

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
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

    }

    public void sendData(){
        DatabaseManager manager = new DatabaseManager(getContext(),
                "http://34.203.215.247/uploadImages.php",
                this);
        Map<String,String> params = new HashMap<>();
        params.put("name",pName.getText().toString());
        params.put("description", pDescription.getText().toString());
        params.put("price",pPrice.getText().toString());
        params.put("estimationTime",estimationTime.getText().toString());
        params.put("maxQnty",maxQnty.getText().toString());
        params.put("category",pCategory.getSelectedItem().toString());
        params.put("image",imgToString(bitmap));
        params.put("provID",sharedPreferences.getString("userID","defaultvalue"));
        manager.sendDataToDatabase(params);

    }

    private String imgToString(Bitmap bitmap){
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.JPEG,100,byteArrayOutputStream);
        byte[] imBytes = byteArrayOutputStream.toByteArray();

        return android.util.Base64.encodeToString(imBytes, android.util.Base64.DEFAULT);

    }

    @Override
    public void databaseResponse(String response) {
        if (response.equals("Image Uploaded Successfully")){
            Toast.makeText(getContext(), "Product Created Successfully", Toast.LENGTH_LONG).show();
            getFragmentManager().beginTransaction().replace(R.id.fragmentContentId,new ProviderMyProducts()).commit();
        }else{
            Toast.makeText(getContext(), "Sorry, Something went wrong", Toast.LENGTH_LONG).show();
        }
    }
}
