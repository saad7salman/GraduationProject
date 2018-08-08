package com.demo.example.sss.capstone.Activities;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.demo.example.sss.capstone.DBPackage.DBClass;
import com.demo.example.sss.capstone.R;
import com.demo.example.sss.capstone.validation.Validation;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.w3c.dom.Text;

public class LoginActivity extends AppCompatActivity {

    EditText emailText;
    EditText passText;
    Button loginBtn;
    SharedPreferences sharedPreferences;
    TextView registerLink;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        emailText = (EditText) findViewById(R.id.lgnEmailEditText);
        passText = (EditText) findViewById(R.id.PassLoginEditText);
        loginBtn = (Button) findViewById(R.id.logintBtn);
        sharedPreferences = getSharedPreferences("userinfo", Context.MODE_PRIVATE);
        registerLink = (TextView) findViewById(R.id.registerLinktextView);
        registerLink.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(),RegisterActivity.class);
                startActivity(intent);
            }
        });
    }
    public void loginBtnOnClick(View view) {

        if (validated()) {
            String email = emailText.getText().toString();
            //email = email.replace("'","''");
            String pass = passText.getText().toString();
            String type = "login";

            DBClass dbClass = new DBClass(this, new DBClass.MyAsyncResponse() {
                @Override
                public void processFinish(String output) {
                    String userid = "";
                    String name = "";
                    String type = "";
                        if (output.equals("failed")) {
                            Toast.makeText(LoginActivity.this, "Sorry, invalid inputs", Toast.LENGTH_SHORT).show();
                        } else {
                            try {
                                Log.i("Manager",output);
                                JSONArray parentArray = new JSONArray(output);
                                JSONArray finalArray = parentArray.getJSONArray(0);
                                SharedPreferences.Editor editor = sharedPreferences.edit();
                                editor.putString("userID",finalArray.get(0).toString());
                                editor.putString("uID",finalArray.get(2).toString());
                                editor.putString("type",finalArray.get(1).toString());
                                editor.commit();
                                Intent intent = new Intent(LoginActivity.this.getApplicationContext(), MainPageActivity.class);
                                intent.putExtra("userid", userid);
                                intent.putExtra("type", finalArray.get(1).toString());
                                startActivity(intent);
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                        }
                    }

            });
            dbClass.execute(type, email, pass);
        }
        }

    private boolean validated() {
        Validation valid = new Validation();
        if (valid.validateEditTexts(emailText) && valid.validateEditTexts(passText)) {
            return true;
        } else {
            return false;
        }
    }

}


