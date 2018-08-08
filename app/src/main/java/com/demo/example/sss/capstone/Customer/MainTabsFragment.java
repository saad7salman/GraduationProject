package com.demo.example.sss.capstone.Customer;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.app.Fragment;
import android.support.design.widget.TabLayout;
import android.support.v4.app.FragmentManager;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TableLayout;
import android.widget.Toast;

import com.demo.example.sss.capstone.R;

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link MainTabsFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link MainTabsFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class MainTabsFragment extends android.support.v4.app.Fragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;
    private ViewPager viewPager;
    private SectionPageAdapter sectionPageAdapter;

    private OnFragmentInteractionListener mListener;

    public MainTabsFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment MainTabsFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static MainTabsFragment newInstance(String param1, String param2) {
        MainTabsFragment fragment = new MainTabsFragment();
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

        View view =  inflater.inflate(R.layout.fragment_main_tabs, container, false);
        Log.i("Status","In The MainTabsFragment before init.");

        sectionPageAdapter = new SectionPageAdapter(getActivity().getSupportFragmentManager());
        viewPager = (ViewPager) view.findViewById(R.id.container);
        setupViewPager(viewPager);
        TabLayout tabLayout = (TabLayout) view.findViewById(R.id.tabs);
        tabLayout.setupWithViewPager(viewPager);
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

    private void setupViewPager(ViewPager viewPager){
        SectionPageAdapter adapter = new SectionPageAdapter(getFragmentManager());
        ProductDetailFragment productDetailFragment = new ProductDetailFragment();
        CustomerReviews customerReviews = new CustomerReviews();
        Bundle bundle = new Bundle();
        bundle.putString("productName", getArguments().getString("productName"));
        bundle.putString("productPrice", getArguments().getString("productPrice"));
        bundle.putString("productDesc", getArguments().getString("productDesc"));
        bundle.putString("imgPath",getArguments().getString("imgPath"));
        bundle.putString("productID",getArguments().getString("productID"));
        bundle.putString("maxQuantity",getArguments().getString("maxQuantity"));
        productDetailFragment.setArguments(bundle);
        Log.i("Status-GetData",getArguments().getString("productName"));
        Bundle bundle2 = new Bundle();
        bundle2.putString("productID",getArguments().getString("productID"));
        customerReviews.setArguments(bundle2);
        adapter.addFragment(productDetailFragment,"Product Detail");
        adapter.addFragment(customerReviews,"Reviews");
        viewPager.setAdapter(adapter);
    }
}
























