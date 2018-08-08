package com.demo.example.sss.capstone.DBPackage;

import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.AsyncTask;
import android.util.Log;

import com.demo.example.sss.capstone.Activities.LoginActivity;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLEncoder;
import java.nio.Buffer;

/**
 * Created by SSS on 10/6/17.
 */

public class DBClass extends AsyncTask<String,String,String> {

    Context context;
    public interface MyAsyncResponse {
        public void processFinish(String output);
    }
    public MyAsyncResponse obj = null;
    public DBClass(Context context,MyAsyncResponse obj) {
        this.context = context;
        this.obj = obj;
    }
    @Override
    protected String doInBackground(String... params) {
        String type = params[0];
        String login_url = "http://34.203.215.247/login.php";
        String register_url = "http://34.203.215.247/register.php";
        String display_url = "http://34.203.215.247/displayTable.php";
        if (type.equals("login")) {
            try {
                String email = params[1];
                String password = params[2];
                String postData = URLEncoder.encode("email", "UTF-8") + "=" + URLEncoder.encode(email, "UTF-8") + "&"
                        + URLEncoder.encode("password", "UTF-8") + "=" + URLEncoder.encode(password, "UTF-8");
                return helperConn(login_url, postData);
            } catch (UnsupportedEncodingException e) {
                e.printStackTrace();
            }
        } else if (type.equals("register")) {
            try {
                String fullname = params[1];
                String email = params[2];
                String password = params[3];
                String usertype = params[4];
                String addressline1 = params[5];
                String addressline2 = params[6];
                String city = params[7];
                String state = params[8];
                String zipcode = params[9];
                String country = params[10];
                String latitude = params[11];
                String longitude = params[12];
                String cell = params[13];


                String postData = URLEncoder.encode("fullname", "UTF-8") + "=" + URLEncoder.encode(fullname, "UTF-8") + "&"
                        + URLEncoder.encode("email", "UTF-8") + "=" + URLEncoder.encode(email, "UTF-8") + "&"
                        + URLEncoder.encode("password", "UTF-8") + "=" + URLEncoder.encode(password, "UTF-8") + "&"
                        + URLEncoder.encode("usertype", "UTF-8") + "=" + URLEncoder.encode(usertype, "UTF-8")+ "&"
                +URLEncoder.encode("addressline1", "UTF-8") + "=" + URLEncoder.encode(addressline1, "UTF-8") + "&"
                        + URLEncoder.encode("addressline2", "UTF-8") + "=" + URLEncoder.encode(addressline2, "UTF-8") + "&"
                        + URLEncoder.encode("city", "UTF-8") + "=" + URLEncoder.encode(city, "UTF-8") + "&"
                        + URLEncoder.encode("state", "UTF-8") + "=" + URLEncoder.encode(state, "UTF-8") + "&"
                        + URLEncoder.encode("zipcode", "UTF-8") + "=" + URLEncoder.encode(zipcode, "UTF-8") + "&"
                        + URLEncoder.encode("country", "UTF-8") + "=" + URLEncoder.encode(country, "UTF-8") +"&"
                        + URLEncoder.encode("latitude", "UTF-8") + "=" + URLEncoder.encode(latitude, "UTF-8") +"&"
                        + URLEncoder.encode("cell", "UTF-8") + "=" + URLEncoder.encode(cell, "UTF-8") +"&"
                        + URLEncoder.encode("longitude", "UTF-8") + "=" + URLEncoder.encode(longitude, "UTF-8");
                return helperConn(register_url, postData);
            } catch (UnsupportedEncodingException e) {
                e.printStackTrace();
            }
        }else if (type.equals("display")){
            String email = params[1];
            try {
                String postData = URLEncoder.encode("email", "UTF-8") + "=" + URLEncoder.encode(email, "UTF-8");
                return helperConn(display_url, postData);

            } catch (UnsupportedEncodingException e) {
                e.printStackTrace();
            }
        }
            return null;
    }
    @Override
    protected void onPreExecute() {
    }
    @Override
    protected void onPostExecute(String result) {
            obj.processFinish(result);
    }
    @Override
    protected void onProgressUpdate(String... values) {
        super.onProgressUpdate(values);
    }
    private String helperConn(String strUrl,String postData){

        try {
            URL url = new URL(strUrl);
            HttpURLConnection httpURLConnection = (HttpURLConnection) url.openConnection();
            httpURLConnection.setRequestMethod("POST");
            httpURLConnection.setDoOutput(true);
            httpURLConnection.setDoInput(true);
            OutputStream outputStream = httpURLConnection.getOutputStream();
            BufferedWriter bufferedWriter = new BufferedWriter(new OutputStreamWriter(outputStream, "UTF-8"));
            bufferedWriter.write(postData);
            bufferedWriter.flush();
            bufferedWriter.close();
            outputStream.close();
            InputStream inputStream = httpURLConnection.getInputStream();
            BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(inputStream, "iso-8859-1"));
            String result = "";
            String line = "";
            while ((line = bufferedReader.readLine()) != null) {
                result += line;
            }
            bufferedReader.close();
            inputStream.close();
            httpURLConnection.disconnect();
            return result;
        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }
}
