package com.demo.example.sss.capstone.Adapters;

import android.content.Context;
import android.support.v4.app.FragmentManager;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.demo.example.sss.capstone.DBPackage.MySingleton;
import com.demo.example.sss.capstone.R;
import com.demo.example.sss.capstone.model.Products;
import com.squareup.picasso.Picasso;
import com.travijuu.numberpicker.library.Enums.ActionEnum;
import com.travijuu.numberpicker.library.Interface.ValueChangedListener;
import com.travijuu.numberpicker.library.NumberPicker;

import org.json.JSONObject;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by SSS on 10/21/17.
 */

public class CartAdapter extends RecyclerView.Adapter<CartAdapter.CartViewHolder> {
    private Context context;
    private ArrayList<Products> productsArrayList;
    private android.support.v4.app.FragmentManager fragmentManager;
    double total;
    TextView tvTotal;


    public CartAdapter(Context context, ArrayList<Products> productsArrayList,
                       FragmentManager fragmentManager,TextView tvTotal) {
        this.context = context;
        this.productsArrayList = productsArrayList;
        this.fragmentManager = fragmentManager;
         total = 0;
        this.tvTotal = tvTotal;
    }

    public CartAdapter() {
    }

    @Override
    public CartViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        View view = inflater.from(parent.getContext())
                .inflate(R.layout.layout_cardview_customer_cart,parent,false);
        CartViewHolder cartViewHolder = new CartViewHolder(view,fragmentManager);
        return cartViewHolder;
    }

    @Override
    public void onBindViewHolder(final CartViewHolder holder, int position) {
        final Products products = productsArrayList.get(position);
        holder.tvNameCusCart.setText(products.name);
        holder.tvPriceCusCart.setText("$"+products.price);
        holder.tvProvCusCart.setText("by :"+products.providerName);
        holder.imagePath = products.imagePath;
        holder.provID = products.provid;
        holder.cartID = products.cartID;

         total += products.price * Double.parseDouble(products.quantity);

        tvTotal.setText("Total: "+roundDoubleToOne(total,1));
        holder.npQuantity.setValue(Integer.parseInt(products.quantity));

        holder.npQuantity.setValueChangedListener(new ValueChangedListener() {
            @Override
            public void valueChanged(int value, ActionEnum action) {
                updateCartQuantity(holder.cartID,""+value);
                products.quantity = ""+value;
                notifyDataSetChanged();
                total = 0;
            }
        });
        holder.removeBtnCusCart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                removeCartItem(holder.cartID);
                productsArrayList.remove(productsArrayList.indexOf(products));
                notifyDataSetChanged();
            }
        });
        String fullUrl =products.imagePath;
        Picasso.with(context)
                .load(fullUrl)
                .placeholder(R.drawable.appetizer)
                .error(android.R.drawable.stat_notify_error)
                .into(holder.ivImageUrlCusCart);
    }
    @Override
    public int getItemCount() {
        if (productsArrayList != null){
            return productsArrayList.size();
        }
        return 0;
    }

    public double roundDoubleToOne(double v,int places){

        BigDecimal bd = new BigDecimal(v);
        bd = bd.setScale(places, RoundingMode.HALF_UP);
        return bd.doubleValue();

    }

    public static class CartViewHolder extends RecyclerView.ViewHolder{

        public CardView cvCart;
        ImageView ivImageUrlCusCart;
        public TextView tvNameCusCart;
        public TextView tvPriceCusCart;
        public TextView tvProvCusCart;
        public String imagePath;
        public String provID;
        public NumberPicker npQuantity;
        public String cartID;
        Button removeBtnCusCart;


        public CartViewHolder(final View cartView, final android.support.v4.app.FragmentManager fragmentManager){
            super(cartView);
            cvCart = (CardView) cartView.findViewById(R.id.cvCustomerCart);
            ivImageUrlCusCart = (ImageView) cartView.findViewById(R.id.ivImageUrlCusCart);
            tvNameCusCart = (TextView) cartView.findViewById(R.id.tvNameCusCart);
            tvPriceCusCart = (TextView) cartView.findViewById(R.id.tvPriceCusCart);
            tvProvCusCart = (TextView) cartView.findViewById(R.id.tvNameProvCusCart);
            npQuantity = (NumberPicker) cartView.findViewById(R.id.npQuantity);
            removeBtnCusCart = (Button) cartView.findViewById(R.id.removeBtnCusCart);
        }
    }

    private void updateCartQuantity(final String cartID, final String value){
        String url = "http://34.203.215.247/updateCartTable.php";
        StringRequest stringRequest = new StringRequest(Request.Method.POST,
                url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        JSONObject parentObject = null;
                        if (response.equals("Cart updated successfully")){
                            Toast.makeText(context, "Cart updated successfully", Toast.LENGTH_SHORT).show();

                        }else {
                            Toast.makeText(context, "Oops, Something went wrong", Toast.LENGTH_SHORT).show();
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

                Log.i("cartid",cartID);
                Log.i("quant",value);
                params.put("cartid",cartID);
                params.put("quantity",value);

                return params;
            }
        };
        MySingleton.getInstance(context.getApplicationContext()).addToRequestQueue(stringRequest);
    }

    private void removeCartItem(final String cartID){
        String url = "http://34.203.215.247/removeCartItem.php";
        StringRequest stringRequest = new StringRequest(Request.Method.POST,
                url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        JSONObject parentObject = null;
                        if (response.equals("Item deleted successfully")){
                            Toast.makeText(context, "Item removed successfully", Toast.LENGTH_SHORT).show();

                        }else {
                            Toast.makeText(context, "Oops, Something went wrong", Toast.LENGTH_SHORT).show();
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

                params.put("cartid",cartID);

                return params;
            }
        };
        MySingleton.getInstance(context.getApplicationContext()).addToRequestQueue(stringRequest);
    }

}
