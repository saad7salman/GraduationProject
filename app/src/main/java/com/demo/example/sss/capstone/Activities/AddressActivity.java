package com.demo.example.sss.capstone.Activities;

import android.content.Intent;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.demo.example.sss.capstone.DBPackage.DBClass;
import com.demo.example.sss.capstone.R;
import com.demo.example.sss.capstone.validation.Validation;

import java.io.IOException;
import java.util.List;

public class AddressActivity extends AppCompatActivity {

    EditText addressline1;
    EditText addressline2;
    EditText city;
    EditText state;
    EditText zipcode;
    EditText country;
    Button rgstrBtn;
    String strAddress;
    Intent intent;
    Location userLocation;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register2);
        addressline1 = (EditText) findViewById(R.id.addressLine1EditText);
        addressline2 = (EditText) findViewById(R.id.addressLine2EditText);
        city = (EditText) findViewById(R.id.cityEditText);
        state = (EditText) findViewById(R.id.stateEditText);
        zipcode = (EditText) findViewById(R.id.zipCodeEditText);
        country = (EditText) findViewById(R.id.countryEditText);
        rgstrBtn = (Button) findViewById(R.id.rgstrButton);
        strAddress = "";
        userLocation = new Location("User Location");
         intent = getIntent();
    }


    public void rgstrrBtn(View view){

    if (validated()) {
        if (!getLatAndLong()){
            Toast.makeText(this, "The address is invalid, Please follow google map's format", Toast.LENGTH_LONG).show();
          return;
        }
    String addrs1 = addressline1.getText().toString();
    String addrs2 = addressline2.getText().toString();
    String cty = city.getText().toString();
    String stat = state.getText().toString();
    String zpcde = zipcode.getText().toString();
    String cntry = country.getText().toString();
    DBClass dbClass = new DBClass(this, new DBClass.MyAsyncResponse() {
        @Override
        public void processFinish(String output) {
            if (output.equals("inserted successfully")) {
                Toast.makeText(AddressActivity.this, "inserted successfully", Toast.LENGTH_LONG).show();
                Intent intent = new Intent(getApplicationContext(), LoginActivity.class);
                startActivity(intent);

            } else {
                Toast.makeText(AddressActivity.this, "Oops,something went wrong", Toast.LENGTH_LONG).show();
            }
        }
    });
    dbClass.execute("register", intent.getStringExtra("fullname"),intent.getStringExtra("email")
            ,intent.getStringExtra("pass"),intent.getStringExtra("userType"),addrs1, addrs2, cty,
            stat, zpcde, cntry,""+userLocation.getLatitude(),""+userLocation.getLongitude(),intent.getStringExtra("cell"));
    }else {
        Toast.makeText(this, "please check some fields", Toast.LENGTH_SHORT).show();
    }
    }
    private boolean validated(){
        Validation valid = new Validation();
        if( valid.validateEditTexts(addressline1)
                && valid.validateEditTexts(city)
                && valid.validateEditTexts(state)
                && valid.validateEditTexts(zipcode)){
            return true;
        }else{
            return false;
        }
    }

    private boolean getLatAndLong() {
        strAddress = addressline1.getText().toString()+city.getText().toString()+
                state.getText().toString()+zipcode.getText().toString();
        Geocoder geocoder = new Geocoder(this);
        List<Address> address = null;
        try {
            address = geocoder.getFromLocationName(strAddress, 1);
        } catch (IOException e) {
            e.printStackTrace();
        }
        if (address == null || address.size() == 0) {
            return false;
        }
        Address location = address.get(0);
        userLocation.setLatitude(location.getLatitude());
        userLocation.setLongitude(location.getLongitude());


    return true;
    }
}
