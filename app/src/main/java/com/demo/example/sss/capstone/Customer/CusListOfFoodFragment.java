package com.demo.example.sss.capstone.Customer;
import android.graphics.Color;
import android.location.Location;
import android.os.Bundle;
import android.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.SearchView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;
import com.demo.example.sss.capstone.Activities.IFragment;
import com.demo.example.sss.capstone.Activities.MainPageActivity;
import com.demo.example.sss.capstone.DBPackage.DatabaseManager;
import com.demo.example.sss.capstone.Adapters.ProductAdapter;
import com.demo.example.sss.capstone.R;
import com.demo.example.sss.capstone.model.DaysOfWeek;
import com.demo.example.sss.capstone.model.Products;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link CusListOfFoodFragment#newInstance} factory method to
 * create an instance of this fragment.
 */

public class CusListOfFoodFragment extends android.support.v4.app.Fragment implements IFragment,SearchView.OnQueryTextListener {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";
    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;
    RecyclerView rvProduct;
    Location providerlocation = new Location("providerLocation");
    ArrayList<Products> prdlist;
    android.support.v4.app.FragmentManager fragmentManager;
    LinearLayoutManager linearLayoutManager;
    Button filterPriceBtn;
    Button filterDisBtn;
    ProductAdapter adapter;
    View view;
    Spinner spinnerFilterByCategory;
    SearchView searchView;


    public CusListOfFoodFragment() {
        // Required empty public constructor
    }
    // TODO: Rename and change types and number of parameters
    public static CusListOfFoodFragment newInstance(String param1, String param2) {
        CusListOfFoodFragment fragment = new CusListOfFoodFragment();
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
         view =  inflater.inflate(R.layout.fragment_cus_list_of_food, container, false);
         searchView = view.findViewById(R.id.searchViewFoodList);
        searchView.setOnQueryTextListener(this);
        searchView.setQueryHint("Search ...");
        rvProduct = view.findViewById(R.id.rvProduct);
        rvProduct.setHasFixedSize(true);
        filterPriceBtn = view.findViewById(R.id.filterPriceBtn);
        filterDisBtn = view.findViewById(R.id.filterDisBtn);
        spinnerFilterByCategory = view.findViewById(R.id.spinnerFilterByCategory);
        String[] items = new String[]{"Category","Appetizer", "Main Dish", "Dessert","Other"};
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(getActivity(),R.layout.customized_spinner_layout, items);
        spinnerFilterByCategory.setAdapter(adapter);
        spinnerFilterByCategory.setSelection(0,false);
        linearLayoutManager = new LinearLayoutManager(getActivity());
        rvProduct.setLayoutManager(linearLayoutManager);
        fragmentManager = getFragmentManager();
        getData();
        filterDistance();
        filterPrice();
        spinnerListner();
        return view;

    }

    private void spinnerListner(){
        spinnerFilterByCategory.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener()
        {
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id)
            {

                String selectedItem = parent.getItemAtPosition(position).toString();
                Log.i("whichOne",selectedItem);
                adapter.filterByCategory(selectedItem);
            } // to close the onItemSelected
            public void onNothingSelected(AdapterView<?> parent)
            {

            }
        });
    }

    public void filterDistance(){
        filterDisBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                adapter.filterByDistance();
            }
        });
    }

    public void filterPrice(){
        filterPriceBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                adapter.filterByPrice();
            }
        });
    }

    private double getDistance(String latit, String longit){

        providerlocation.setLatitude(Double.parseDouble(latit));
        providerlocation.setLongitude(Double.parseDouble(longit));
        return (double) MainPageActivity.userLocation.distanceTo(providerlocation)/1609.344;

    }
    public void getData(){
        DatabaseManager databaseManager = new DatabaseManager(getContext(),
                "http://34.203.215.247/displayproducttable.php",
                this);
        Map<String,String> params = new HashMap<>();
        params.put("","");
        databaseManager.getDataFromDatabase(params);
    }

    @Override
    public void databaseResponse(String response) {
        ArrayList<DaysOfWeek> businessHrslist = null;
        if (response.equals("failed")){
            Toast.makeText(getContext(), "Oops, something went wrong", Toast.LENGTH_SHORT).show();
            return;
        }
        JSONObject parentObject = null;
        prdlist = new ArrayList<>();
        try {
            parentObject = new JSONObject(response);
            JSONArray parentArray = parentObject.getJSONArray("products");
            for (int i = 0; i < parentArray.length(); i++) {
                JSONObject finalObject = parentArray.getJSONObject(i);
                double dis = getDistance(finalObject.getString("Latitude"),finalObject.getString("Longitude"));
               if (dis < 50.0) {
                   prdlist.add(new Products(finalObject.getString("Product_ID"), finalObject.getString("name"),
                           finalObject.getString("description"),
                           finalObject.getString("price"), finalObject.getString("category"), finalObject.getString("imagepath")
                           , finalObject.getString("Prov_ID"), dis,finalObject.getString("Estimation_Time")
                            ,finalObject.getString("Max_Quantity"), finalObject.getString("AverageRating")));
               }

            }
            JSONArray parentArray2 = parentObject.getJSONArray("Business_Hours");
            businessHrslist = new ArrayList<>();
            for(int i = 0; i < parentArray2.length();i++){
                JSONObject finalObject = parentArray2.getJSONObject(i);
                businessHrslist.add(new DaysOfWeek(finalObject.getString("DayOfWeek"),finalObject.getString("FromTime"),
                        finalObject.getString("ToTime"),Boolean.valueOf(finalObject.getString("OpeningStatus")),
                        finalObject.getString("Prov_ID")));
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
        adapter = new ProductAdapter(getActivity().getApplicationContext(),prdlist,fragmentManager,businessHrslist);
        rvProduct.setAdapter(adapter);
    }

    @Override
    public boolean onQueryTextSubmit(String s) {
        return false;
    }

    @Override
    public boolean onQueryTextChange(String s) {
        s = s.toLowerCase();
        ArrayList<Products> newList = new ArrayList<Products>();

        for(Products product : prdlist){
            String category = product.category.toLowerCase();
            String name = product.name.toLowerCase();
            if(category.contains(s) || name.contains(s))
                newList.add(product);
        }
        adapter.filterAL(newList);
        return true;
    }
}
