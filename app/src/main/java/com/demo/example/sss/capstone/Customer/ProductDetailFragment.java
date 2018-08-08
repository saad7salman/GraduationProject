package com.demo.example.sss.capstone.Customer;

import android.app.AlertDialog;
import android.app.FragmentManager;
import android.content.Context;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.demo.example.sss.capstone.Activities.IFragment;
import com.demo.example.sss.capstone.DBPackage.DatabaseManager;
import com.travijuu.numberpicker.library.Enums.ActionEnum;
import com.travijuu.numberpicker.library.Interface.ValueChangedListener;
import com.travijuu.numberpicker.library.NumberPicker;

import com.demo.example.sss.capstone.R;
import com.squareup.picasso.Picasso;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link ProductDetailFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link ProductDetailFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class ProductDetailFragment extends android.support.v4.app.Fragment implements IFragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;
    private ImageView pImg;
    private TextView pName;
    private TextView pDescr;
    private TextView pPrice;
    private EditText pSpecialReq;
    private TextView pQnty;
    private NumberPicker pQuantity;
    private Button pAddToCart;
    private TextView pTotal;
    private String productID;
    private String userID;
    private OnFragmentInteractionListener mListener;
    SharedPreferences sharedPreferences;
    int sumQuantity = 0;
    int maxQuantity = 0;

    public ProductDetailFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment ProductDetailFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static ProductDetailFragment newInstance(String param1, String param2) {
        ProductDetailFragment fragment = new ProductDetailFragment();
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
        View view = inflater.inflate(R.layout.fragment_product_detail, container, false);
        pImg = view.findViewById(R.id.pdImageView) ;
        pName = view.findViewById(R.id.pdNameTextView) ;
        pDescr = view.findViewById(R.id.pdDescrTextView) ;
        pPrice = view.findViewById(R.id.pdPriceTextView) ;
        pSpecialReq = view.findViewById(R.id.pdSpecialReqEditText) ;
        pQnty = view.findViewById(R.id.pdQuantityTextView) ;
        pQuantity = view.findViewById(R.id.number_picker) ;
        pAddToCart = view.findViewById(R.id.pdBtn) ;
        pTotal = view.findViewById(R.id.pdTotalTextView);
        sharedPreferences = getActivity().getSharedPreferences("userinfo", Context.MODE_PRIVATE);
        userID = sharedPreferences.getString("userID","defaultvalue");
        pName.setText(getArguments().getString("productName"));
        pDescr.setText(getArguments().getString("productDesc"));
        pPrice.setText(getArguments().getString("productPrice"));
        String imgPath = getArguments().getString("imgPath");
        productID = getArguments().getString("productID");
        String fullUrl = imgPath;
        Picasso.with(view.getContext())
                .load(fullUrl)
                .placeholder(R.drawable.ic_menu_gallery)
                .error(android.R.drawable.stat_notify_error)
                .into(pImg);
        final double price = Double.parseDouble(
                getArguments().getString("productPrice").replaceAll("[^0-9\\.]", ""));
        pQuantity.setValue(1);
        pQnty.setText("Quantity : "+1);
        pTotal.setText("Total is :$"+price);
        pQuantity.setValueChangedListener(new ValueChangedListener() {
            @Override
            public void valueChanged(int value, ActionEnum action) {
                pQnty.setText("Quantity : "+value);
                pTotal.setText("Total is :$"+ String.format("%.2f",price * value));
            }
        });
        pAddToCart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
//                senddDataToDatabase();
                sendData();
            }
        });
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
        if (response.equals("Success")){
            Toast.makeText(getActivity(), "Has been Added to the Cart Successfully", Toast.LENGTH_SHORT).show();
            android.support.v4.app.FragmentManager fragmentManager = getFragmentManager();
            fragmentManager.beginTransaction().replace(
                    R.id.fragmentContentId, new CusListOfFoodFragment()).commit();
        }else if (response.equals("Failed")){
            Toast.makeText(getActivity(), "Oops, something went wrong", Toast.LENGTH_SHORT).show();
        }else{
            JSONObject parentObject = null;
            try {
                parentObject = new JSONObject(response);
                JSONArray parentArray = parentObject.getJSONArray("quantityMax");
                JSONObject finalObject = parentArray.getJSONObject(0);
                sumQuantity = Integer.parseInt(finalObject.getString("SUM"));
                Log.i("SUMQuantity",finalObject.getString("SUM"));
                setQuantity();
            }catch (JSONException e) {
                e.printStackTrace();
            }
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

    public void sendData(){
        DatabaseManager databaseManager = new DatabaseManager(getContext(),
                "http://34.203.215.247/addToCart.php",
                this);
        Map<String, String> params = new HashMap<>();
        params.put("productID",productID);
        params.put("userid",userID);
        params.put("quantity",""+pQuantity.getValue());
        params.put("specialRequest",pSpecialReq.getText().toString());
      databaseManager.sendDataToDatabase(params);
    }

    public void getData(){
        DatabaseManager databaseManager = new DatabaseManager(getContext(),
                "http://34.203.215.247/getSUMQuantity.php",
                this);
        Map<String, String> params = new HashMap<>();
        params.put("productID",productID);
        databaseManager.getDataFromDatabase(params);
    }

    private void setQuantity(){
        maxQuantity = Integer.parseInt(getArguments().getString("maxQuantity")) - sumQuantity ;
        if (maxQuantity < 1){
            Log.i("max",""+maxQuantity);
            pQuantity.setMin(0);
            pQuantity.setMax(0);
            quantityAlert();
        }else {
            pQuantity.setMin(1);
            pQuantity.setMax(maxQuantity);
        }
    }
    private void quantityAlert(){
        AlertDialog.Builder builder;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            builder = new AlertDialog.Builder(getContext(), android.R.style.Theme_Material_Dialog_Alert);
        } else {
            builder = new AlertDialog.Builder(getContext());
        }
        builder.setTitle("Sold Out")
                .setCancelable(false)
                .setMessage("The product has reached the max quantity for the day")
                .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        getFragmentManager().beginTransaction().replace(
                                R.id.fragmentContentId, new CusListOfFoodFragment(),
                                new CusListOfFoodFragment().getTag()).commit();
                    }
                })
                .setIcon(android.R.drawable.ic_dialog_alert)
                .show();
    }
}
