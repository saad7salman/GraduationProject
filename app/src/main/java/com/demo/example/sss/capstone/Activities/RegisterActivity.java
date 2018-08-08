package com.demo.example.sss.capstone.Activities;

import android.content.Intent;
import android.graphics.Color;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.demo.example.sss.capstone.DBPackage.MySingleton;
import com.demo.example.sss.capstone.R;
import com.demo.example.sss.capstone.validation.Validation;

import java.util.HashMap;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class RegisterActivity extends AppCompatActivity {

    EditText fullNameText;
    EditText emailText;
    EditText passText;
    EditText confirmPassText;
    EditText cellPhone;
    Button registerBtn;
    Spinner dropdown;
    TextView backToLogin;
    boolean isEmailExists = true;

    public void loginBtnClicked(View view){
        Intent intent = new Intent(this.getApplicationContext(),LoginActivity.class);
        this.startActivity(intent);
    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        fullNameText = (EditText) findViewById(R.id.FullNameEditText);
        emailText = (EditText) findViewById(R.id.emailEditText);
        passText = (EditText) findViewById(R.id.PasswordEditText);
        confirmPassText = (EditText) findViewById(R.id.ConfirmPassEditText);
        cellPhone = findViewById(R.id.CellPhoneEditText);
        registerBtn = (Button) findViewById(R.id.RegisterButton);
        dropdown = (Spinner)findViewById(R.id.userTypeSpinner);
        backToLogin = (TextView) findViewById(R.id.backToLogintextView);
        backToLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(),LoginActivity.class);
                startActivity(intent);
            }
        });

        String[] items = new String[]{"Customer", "Provider", "Driver"};
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_dropdown_item, items);
        dropdown.setAdapter(adapter);

    }

    public void registerBtnOnClick(View view) {
        if (validated()) {
            String fullname = fullNameText.getText().toString();
            String email = emailText.getText().toString();
            String pass = passText.getText().toString();
            String cell = cellPhone.getText().toString();
             String userType = dropdown.getSelectedItem().toString();
            Intent intent = new Intent(getApplicationContext(),AddressActivity.class);
            intent.putExtra("fullname",fullname);
            intent.putExtra("email",email);
            intent.putExtra("pass",pass);
            intent.putExtra("cell",cell);
            intent.putExtra("userType",userType);
            startActivity(intent);
        }
    }
    private boolean validated(){
        Validation valid = new Validation();
        if( valid.validateEditTexts(fullNameText)
        && valid.validateEditTexts(emailText)
        && valid.validateEditTexts(passText)
        && valid.validateEditTexts(confirmPassText)
        && passCheck() && isEmailValid(emailText)
                &&!isEmailAvailable()){
            return true;
        }else{
            return false;
        }
    }
    private boolean passCheck(){
        if(!confirmPassText.getText().toString().equals(passText.getText().toString())){
            confirmPassText.setError("password don't match");
            return false;
        }else{
            return true;
        }
    }
    private boolean isEmailValid(EditText emailET) {
        String email = emailET.getText().toString();
        String expression = "^[\\w\\.-]+@([\\w\\-]+\\.)+[A-Z]{2,4}$";
        Pattern pattern = Pattern.compile(expression, Pattern.CASE_INSENSITIVE);
        Matcher matcher = pattern.matcher(email);
        if (!matcher.matches()) {
            emailET.setError("please enter a proper email " + emailET.getHint());
            emailET.setBackgroundColor(Color.RED);
        }
        return matcher.matches();
    }

    private boolean isEmailAvailable(){
        checkEmailFromDatabase();
        if (isEmailExists){
            return true;
        }else{
            return false;
        }
    }

    private void checkEmailFromDatabase(){
        String url = "http://34.203.215.247/checkEmailExistance.php";
        StringRequest stringRequest = new StringRequest(Request.Method.POST,
                url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {

                        if (response.equals("email exists")){
                             Toast.makeText(RegisterActivity.this, "Email already exists", Toast.LENGTH_SHORT).show();
                            emailText.setError("email already exists ");
                            emailText.setBackgroundColor(Color.RED);
                        }else {
                            isEmailExists = false;
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        if (error != null){
                            Toast.makeText(RegisterActivity.this, "Something went wrong", Toast.LENGTH_LONG).show();
                        }
                    }
                }
        ){
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
               Map<String,String> params = new HashMap<>();
                params.put("email",emailText.getText().toString());

                return params;
            }
        };
        MySingleton.getInstance(RegisterActivity.this.getApplicationContext()).addToRequestQueue(stringRequest);
    }

}
