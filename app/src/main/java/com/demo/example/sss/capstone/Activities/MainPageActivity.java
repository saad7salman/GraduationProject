package com.demo.example.sss.capstone.Activities;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.FragmentManager;
import android.support.v4.content.ContextCompat;

import com.demo.example.sss.capstone.Customer.CusListOfFoodFragment;
import com.demo.example.sss.capstone.Customer.CustomerCart;
import com.demo.example.sss.capstone.Customer.CustomerReviews;
import com.demo.example.sss.capstone.Customer.MainTabsFragment;
import com.demo.example.sss.capstone.Customer.MyOrdersCusFragment;
import com.demo.example.sss.capstone.Customer.PlaceOrderCus;
import com.demo.example.sss.capstone.Customer.ProductDeliveryStatus;
import com.demo.example.sss.capstone.Customer.ProductDetailFragment;
import com.demo.example.sss.capstone.Driver.DriverDeliveryRequest;
import com.demo.example.sss.capstone.Driver.DriverMyDelivery;
import com.demo.example.sss.capstone.Driver.PreviousDelivery;
import com.demo.example.sss.capstone.Manager.CustomerConcerns;
import com.demo.example.sss.capstone.Manager.ManageAccounts;
import com.demo.example.sss.capstone.Provider.ProviderBusinessHours;
import com.demo.example.sss.capstone.Provider.ProviderCusRequest;
import com.demo.example.sss.capstone.Provider.ProviderNewPostFragment;
import com.demo.example.sss.capstone.Provider.ProviderMyProducts;
import com.demo.example.sss.capstone.Provider.ProviderReadyRequests;
import com.demo.example.sss.capstone.Provider.ProviderSoldProduct;
import com.demo.example.sss.capstone.Provider.ProviderUpdateFood;
import com.demo.example.sss.capstone.Provider.ProviderWaitingRequest;
import com.demo.example.sss.capstone.R;
import com.google.android.gms.maps.model.LatLng;

import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;


public class MainPageActivity extends AppCompatActivity
        implements ProviderMyProducts.OnFragmentInteractionListener,
        ProductDetailFragment.OnFragmentInteractionListener,
        CustomerCart.OnFragmentInteractionListener,
        PlaceOrderCus.OnFragmentInteractionListener,
        MyOrdersCusFragment.OnFragmentInteractionListener,
        ProviderUpdateFood.OnFragmentInteractionListener,
        ProviderWaitingRequest.OnFragmentInteractionListener,
        ProviderCusRequest.OnFragmentInteractionListener,
        DriverDeliveryRequest.OnFragmentInteractionListener,
        ProductDeliveryStatus.OnFragmentInteractionListener,
        ProviderReadyRequests.OnFragmentInteractionListener,
        DriverMyDelivery.OnFragmentInteractionListener,
        ProviderSoldProduct.OnFragmentInteractionListener,
        ManageAccounts.OnFragmentInteractionListener,
        MyInfo.OnFragmentInteractionListener,
        ChatMessages.OnFragmentInteractionListener,
        CustomerConcerns.OnFragmentInteractionListener,
        ProviderBusinessHours.OnFragmentInteractionListener,
        MainTabsFragment.OnFragmentInteractionListener,
        CustomerReviews.OnFragmentInteractionListener,
        PreviousDelivery.OnFragmentInteractionListener,
        NavigationView.OnNavigationItemSelectedListener{

    LocationManager locationManager;
    LocationListener locationListener;
    public static Location userLocation;
    NavigationView navigationView;
    SharedPreferences sharedPreferences;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_page);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        Intent intent = getIntent();
        userLocation = new Location("userLocation");
        sharedPreferences = getSharedPreferences("userinfo", Context.MODE_PRIVATE);
        locationManager = (LocationManager) this.getSystemService(Context.LOCATION_SERVICE);
        locationListener = new LocationListener() {
            @Override
            public void onLocationChanged(Location location) {
                getLoc(location);
            }
            @Override
            public void onStatusChanged(String s, int i, Bundle bundle) {
            }
            @Override
            public void onProviderEnabled(String s) {
            }
            @Override
            public void onProviderDisabled(String s) {
            }
        };
//        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
//        fab.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
//                        .setAction("Action", null).show();
//            }
//        });

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();
         navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, 1);
        } else {
            locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER,
                    0, 0, locationListener);
            Location lastKnownLocation = locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER);
            if (lastKnownLocation != null) {
                getLoc(lastKnownLocation);
            }
        }
        FragmentManager fragmentManager = getSupportFragmentManager();
        if(intent.getStringExtra("type").equals("Customer")) {
            hideItemCustomer();
            fragmentManager.beginTransaction().replace(
                    R.id.fragmentContentId, new CusListOfFoodFragment()).commit();

        }else if (intent.getStringExtra("type").equals("Provider")){
            hideItemProvider();
                  fragmentManager.beginTransaction().replace(
                    R.id.fragmentContentId, new ProviderMyProducts()).commit();

        }else if (intent.getStringExtra("type").equals("Driver")){
                    hideItemDriver();
            fragmentManager.beginTransaction().replace(R.id.fragmentContentId
                    ,new DriverDeliveryRequest()).commit();
        }else if (intent.getStringExtra("type").equals("Manager")){
            hideItemManager();
            fragmentManager.beginTransaction().replace(R.id.fragmentContentId
                    ,new ManageAccounts()).commit();
        }

    }
    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.customer, menu);
        return true;
    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();
            FragmentManager fragmentManager = getSupportFragmentManager();
        if (id == R.id.nav_provider_post) {
            fragmentManager.beginTransaction().replace(R.id.fragmentContentId
            ,new ProviderNewPostFragment()).commit();
        }
        else if (id == R.id.nav_customer_view_post) {
            fragmentManager.beginTransaction().replace(R.id.fragmentContentId
                    ,new CusListOfFoodFragment()).commit();

        } else if (id == R.id.nav_customer_cart) {
            fragmentManager.beginTransaction().replace(R.id.fragmentContentId
                    ,new CustomerCart()).commit();
        }
        else if (id == R.id.nav_provider_products) {
            fragmentManager.beginTransaction().replace(R.id.fragmentContentId
                    ,new ProviderMyProducts()).commit();
        }else if (id == R.id.nav_customer_orders) {
            fragmentManager.beginTransaction().replace(R.id.fragmentContentId
                    ,new MyOrdersCusFragment()).commit();
        }else if(id == R.id.nav_provider_cusWaitingRequest){
            fragmentManager.beginTransaction().replace(R.id.fragmentContentId
                    ,new ProviderWaitingRequest()).commit();
        }else if (id == R.id.nav_provider_cusReadyRequest){
            fragmentManager.beginTransaction().replace(R.id.fragmentContentId
                    ,new ProviderReadyRequests()).commit();
        }
        else if (id == R.id.nav_provider_cusRequest) {
            fragmentManager.beginTransaction().replace(R.id.fragmentContentId
                    ,new ProviderCusRequest()).commit();
        }else if (id == R.id.nav_provider_soldProducts) {
            fragmentManager.beginTransaction().replace(R.id.fragmentContentId
                    ,new ProviderSoldProduct()).commit();
        }else if (id == R.id.nav_driver_cusRequest) {
            fragmentManager.beginTransaction().replace(R.id.fragmentContentId
                    ,new DriverDeliveryRequest()).commit();
        }else if(id == R.id.nav_driver_deliveryOnProgress){
            fragmentManager.beginTransaction().replace(R.id.fragmentContentId
                    ,new DriverMyDelivery()).commit();
        }else if(id == R.id.nav_logout){
            Intent intent = new Intent(this.getApplicationContext(),LoginActivity.class);
            this.startActivity(intent);
        }else if(id == R.id.nav_manager_manageAccounts){
            fragmentManager.beginTransaction().replace(R.id.fragmentContentId
                    ,new ManageAccounts()).commit();
        }else if(id == R.id.nav_user_info){
            fragmentManager.beginTransaction().replace(R.id.fragmentContentId
                    ,new MyInfo()).commit();
        }else if(id == R.id.nav_concerns){
            fragmentManager.beginTransaction().replace(R.id.fragmentContentId
                    ,new ChatMessages()).commit();
        }else if(id == R.id.nav_manager_customerConcerns){
            fragmentManager.beginTransaction().replace(R.id.fragmentContentId
            ,new CustomerConcerns()).commit();
        }else if(id == R.id.nav_provider_businessHours){
            fragmentManager.beginTransaction().replace(R.id.fragmentContentId
                    ,new ProviderBusinessHours()).commit();
        }else if(id == R.id.nav_driver_previousDelivery){
            fragmentManager.beginTransaction().replace(R.id.fragmentContentId
                    ,new PreviousDelivery()).commit();
        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }
    @Override
    public void onFragmentInteraction(Uri uri) {
    }
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions,
                                           @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == 1) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
                    locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 0, 0, locationListener);
                    Location lastKnownLocation = locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER);
                    if (lastKnownLocation != null) {
                        getLoc(lastKnownLocation);
                    }
                }
            }
        }
        }
    public void getLoc(Location location) {
        userLocation.setLatitude(location.getLatitude());
        userLocation.setLongitude(location.getLongitude());
            LatLng userLocation = new LatLng(location.getLatitude(), location.getLongitude());
    }
    private void hideItemProvider()
    {
        navigationView = (NavigationView) findViewById(R.id.nav_view);
        Menu nav_Menu = navigationView.getMenu();
        nav_Menu.findItem(R.id.nav_customer_cart).setVisible(false);
        nav_Menu.findItem(R.id.nav_customer_view_post).setVisible(false);
        nav_Menu.findItem(R.id.nav_driver_cusRequest).setVisible(false);
        nav_Menu.findItem(R.id.nav_customer_orders).setVisible(false);
        nav_Menu.findItem(R.id.nav_driver_deliveryOnProgress).setVisible(false);
        nav_Menu.findItem(R.id.nav_manager_manageAccounts).setVisible(false);
        nav_Menu.findItem(R.id.nav_manager_searchBy).setVisible(false);
        nav_Menu.findItem(R.id.nav_manager_sendMessage).setVisible(false);
        nav_Menu.findItem(R.id.nav_manager_viewConcerns).setVisible(false);
        nav_Menu.findItem(R.id.nav_manager_customerConcerns).setVisible(false);
        nav_Menu.findItem(R.id.nav_driver_previousDelivery).setVisible(false);

    }
    private void hideItemCustomer()
    {
        navigationView = (NavigationView) findViewById(R.id.nav_view);
        Menu nav_Menu = navigationView.getMenu();
        nav_Menu.findItem(R.id.nav_provider_post).setVisible(false);
        nav_Menu.findItem(R.id.nav_driver_cusRequest).setVisible(false);
        nav_Menu.findItem(R.id.nav_provider_products).setVisible(false);
        nav_Menu.findItem(R.id.nav_provider_cusRequest).setVisible(false);
        nav_Menu.findItem(R.id.nav_provider_soldProducts).setVisible(false);
        nav_Menu.findItem(R.id.nav_driver_deliveryOnProgress).setVisible(false);
        nav_Menu.findItem(R.id.nav_provider_cusWaitingRequest).setVisible(false);
        nav_Menu.findItem(R.id.nav_provider_cusReadyRequest).setVisible(false);
        nav_Menu.findItem(R.id.nav_manager_manageAccounts).setVisible(false);
        nav_Menu.findItem(R.id.nav_manager_searchBy).setVisible(false);
        nav_Menu.findItem(R.id.nav_manager_sendMessage).setVisible(false);
        nav_Menu.findItem(R.id.nav_manager_viewConcerns).setVisible(false);
        nav_Menu.findItem(R.id.nav_manager_customerConcerns).setVisible(false);
        nav_Menu.findItem(R.id.nav_provider_businessHours).setVisible(false);
        nav_Menu.findItem(R.id.nav_driver_previousDelivery).setVisible(false);


    }
    private void hideItemDriver()
    {
        navigationView = (NavigationView) findViewById(R.id.nav_view);

        Menu nav_Menu = navigationView.getMenu();
        nav_Menu.findItem(R.id.nav_provider_post).setVisible(false);
        nav_Menu.findItem(R.id.nav_customer_cart).setVisible(false);
        nav_Menu.findItem(R.id.nav_customer_view_post).setVisible(false);
        nav_Menu.findItem(R.id.nav_provider_products).setVisible(false);
        nav_Menu.findItem(R.id.nav_customer_orders).setVisible(false);
        nav_Menu.findItem(R.id.nav_provider_cusRequest).setVisible(false);
        nav_Menu.findItem(R.id.nav_provider_soldProducts).setVisible(false);
        nav_Menu.findItem(R.id.nav_provider_cusWaitingRequest).setVisible(false);
        nav_Menu.findItem(R.id.nav_provider_cusReadyRequest).setVisible(false);
        nav_Menu.findItem(R.id.nav_manager_manageAccounts).setVisible(false);
        nav_Menu.findItem(R.id.nav_manager_searchBy).setVisible(false);
        nav_Menu.findItem(R.id.nav_manager_sendMessage).setVisible(false);
        nav_Menu.findItem(R.id.nav_manager_viewConcerns).setVisible(false);
        nav_Menu.findItem(R.id.nav_manager_customerConcerns).setVisible(false);
        nav_Menu.findItem(R.id.nav_provider_businessHours).setVisible(false);

    }
    private void hideItemManager(){
        navigationView = (NavigationView) findViewById(R.id.nav_view);

        Menu nav_Menu = navigationView.getMenu();
        nav_Menu.findItem(R.id.nav_provider_post).setVisible(false);
        nav_Menu.findItem(R.id.nav_customer_cart).setVisible(false);
        nav_Menu.findItem(R.id.nav_customer_view_post).setVisible(false);
        nav_Menu.findItem(R.id.nav_provider_products).setVisible(false);
        nav_Menu.findItem(R.id.nav_customer_orders).setVisible(false);
        nav_Menu.findItem(R.id.nav_provider_cusRequest).setVisible(false);
        nav_Menu.findItem(R.id.nav_provider_soldProducts).setVisible(false);
        nav_Menu.findItem(R.id.nav_provider_cusWaitingRequest).setVisible(false);
        nav_Menu.findItem(R.id.nav_provider_cusReadyRequest).setVisible(false);
        nav_Menu.findItem(R.id.nav_driver_deliveryOnProgress).setVisible(false);
        nav_Menu.findItem(R.id.nav_driver_cusRequest).setVisible(false);
        nav_Menu.findItem(R.id.nav_concerns).setVisible(false);
        nav_Menu.findItem(R.id.nav_provider_businessHours).setVisible(false);
        nav_Menu.findItem(R.id.nav_manager_searchBy).setVisible(false);
        nav_Menu.findItem(R.id.nav_manager_sendMessage).setVisible(false);
        nav_Menu.findItem(R.id.nav_manager_viewConcerns).setVisible(false);
        nav_Menu.findItem(R.id.nav_driver_previousDelivery).setVisible(false);


    }
}