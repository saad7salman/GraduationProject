package com.demo.example.sss.capstone.Provider;

import android.app.TimePickerDialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.TimePicker;
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
import com.demo.example.sss.capstone.model.DaysOfWeek;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link ProviderBusinessHours.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link ProviderBusinessHours#newInstance} factory method to
 * create an instance of this fragment.
 */
public class ProviderBusinessHours extends android.support.v4.app.Fragment implements IFragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;
    TextView mondayFromTxt;
    TextView mondayToTxt;
    Button mondayFromBtn;
    Button mondayToBtn;
    TextView tuesdayFromTxt;
    TextView tuesdayToTxt;
    Button tuesdayFromBtn;
    Button tuesdayToBtn;
    TextView wednesdayFromTxt;
    TextView wednesdayToTxt;
    Button wednesdayFromBtn;
    Button wednesdayToBtn;
    TextView thursdayFromTxt;
    TextView thursdayToTxt;
    Button thursdayFromBtn;
    Button thursdayToBtn;
    TextView fridayFromTxt;
    TextView fridayToTxt;
    Button fridayFromBtn;
    Button fridayToBtn;
    TextView saturdayFromTxt;
    TextView saturdayToTxt;
    Button saturdayFromBtn;
    Button saturdayToBtn;
    TextView sundayFromTxt;
    TextView sundayToTxt;
    Button sundayFromBtn;
    Button sundayToBtn;
    Calendar calendar;
    CheckBox mondayChBx;
    CheckBox tuesdayChBx;
    CheckBox wednesdayChBx;
    CheckBox thursdayChBx;
    CheckBox fridayChBx;
    CheckBox saturdayChBx;
    CheckBox sundayChBx;
    Button saveBHBtn;
    String lastFromTime;
    String lastToTime;
    private ArrayList<DaysOfWeek> businessHoursAL;
    SharedPreferences sharedPreferences;
    private OnFragmentInteractionListener mListener;

    public ProviderBusinessHours() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment ProviderBusinessHours.
     */
    // TODO: Rename and change types and number of parameters
    public static ProviderBusinessHours newInstance(String param1, String param2) {
        ProviderBusinessHours fragment = new ProviderBusinessHours();
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
        View view = inflater.inflate(R.layout.fragment_provider_business_hours, container, false);
        mondayFromTxt = view.findViewById(R.id.MondayFromTxt);
        mondayToTxt = view.findViewById(R.id.MondayToTxt);
        mondayFromBtn = view.findViewById(R.id.MondayFromBtn);
        mondayToBtn = view.findViewById(R.id.MondayToBtn);
        mondayChBx = view.findViewById(R.id.MondayCheckBox);
        tuesdayFromTxt = view.findViewById(R.id.TuesdayFromTxt);
        tuesdayToTxt = view.findViewById(R.id.TuesdayToTxt);
        tuesdayFromBtn = view.findViewById(R.id.TuesdayFromBtn);
        tuesdayToBtn = view.findViewById(R.id.TuesdayToBtn);
        tuesdayChBx = view.findViewById(R.id.TuesdayCheckBox);
        wednesdayFromTxt = view.findViewById(R.id.WednesdayFromTxt);
        wednesdayToTxt = view.findViewById(R.id.WednesdayToTxt);
        wednesdayFromBtn = view.findViewById(R.id.WednesdayFromBtn);
        wednesdayToBtn = view.findViewById(R.id.WednesdayToBtn);
        wednesdayChBx = view.findViewById(R.id.WednesdayCheckBox);
        thursdayFromTxt = view.findViewById(R.id.ThursdayFromTxt);
        thursdayToTxt = view.findViewById(R.id.ThursdayToTxt);
        thursdayFromBtn = view.findViewById(R.id.ThursdayFromBtn);
        thursdayToBtn = view.findViewById(R.id.ThursdayToBtn);
        thursdayChBx = view.findViewById(R.id.ThursdayCheckBox);
        fridayFromTxt = view.findViewById(R.id.FridayFromTxt);
        fridayToTxt = view.findViewById(R.id.FridayToTxt);
        fridayFromBtn = view.findViewById(R.id.FridayFromBtn);
        fridayToBtn = view.findViewById(R.id.FridayToBtn);
        fridayChBx = view.findViewById(R.id.FridayCheckBox);
        saturdayFromTxt = view.findViewById(R.id.SaturdayFromTxt);
        saturdayToTxt = view.findViewById(R.id.SaturdayToTxt);
        saturdayFromBtn = view.findViewById(R.id.SaturdayFromBtn);
        saturdayToBtn = view.findViewById(R.id.SaturdayToBtn);
        saturdayChBx = view.findViewById(R.id.SaturdayCheckBox);
        sundayFromTxt = view.findViewById(R.id.SundayFromTxt);
        sundayToTxt = view.findViewById(R.id.SundayToTxt);
        sundayFromBtn = view.findViewById(R.id.SundayFromBtn);
        sundayToBtn = view.findViewById(R.id.SundayToBtn);
        sundayChBx = view.findViewById(R.id.SundayCheckBox);
        saveBHBtn = view.findViewById(R.id.SaveBHBtn);
        sharedPreferences = getActivity().getSharedPreferences("userinfo", Context.MODE_PRIVATE);
        calendar = Calendar.getInstance();
        getData();
        getDayTime();
        checkBoxes();
        saveBH();
        return view;
    }

    public void getData(){
        DatabaseManager manager = new DatabaseManager(getContext(),
                "http://34.203.215.247/getBusinessHours.php",
                this);
        Map<String,String> params = new HashMap<>();
        params.put("provID",sharedPreferences.getString("userID","defaultvalue"));
        manager.getDataFromDatabase(params);
    }

    private void getDayTime(){
        mondayFromBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                 new TimePickerDialog(getActivity(), new TimePickerDialog.OnTimeSetListener() {

                    @Override
                    public void onTimeSet(TimePicker timePicker, int i, int i1) {
                        mondayFromTxt.setText(i+":"+i1);
                    }
                }, calendar.get(Calendar.HOUR_OF_DAY)
                        , calendar.get(Calendar.MINUTE), true).show();
            }
        });
        mondayToBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                new TimePickerDialog(getActivity(), new TimePickerDialog.OnTimeSetListener() {

                    @Override
                    public void onTimeSet(TimePicker timePicker, int i, int i1) {
                        mondayToTxt.setText(i+":"+i1);
                    }
                }, calendar.get(Calendar.HOUR_OF_DAY)
                        , calendar.get(Calendar.MINUTE), true).show();
            }
        });
        tuesdayFromBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                new TimePickerDialog(getActivity(), new TimePickerDialog.OnTimeSetListener() {

                    @Override
                    public void onTimeSet(TimePicker timePicker, int i, int i1) {
                        tuesdayFromTxt.setText(i+":"+i1);
                    }
                }, calendar.get(Calendar.HOUR_OF_DAY)
                        , calendar.get(Calendar.MINUTE), true).show();
            }
        });
        tuesdayToBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                new TimePickerDialog(getActivity(), new TimePickerDialog.OnTimeSetListener() {

                    @Override
                    public void onTimeSet(TimePicker timePicker, int i, int i1) {
                        tuesdayToTxt.setText(i+":"+i1);
                    }
                }, calendar.get(Calendar.HOUR_OF_DAY)
                        , calendar.get(Calendar.MINUTE), true).show();
            }
        });
        wednesdayFromBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                new TimePickerDialog(getActivity(), new TimePickerDialog.OnTimeSetListener() {

                    @Override
                    public void onTimeSet(TimePicker timePicker, int i, int i1) {
                        wednesdayFromTxt.setText(i+":"+i1);
                    }
                }, calendar.get(Calendar.HOUR_OF_DAY)
                        , calendar.get(Calendar.MINUTE), true).show();
            }
        });
        wednesdayToBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                new TimePickerDialog(getActivity(), new TimePickerDialog.OnTimeSetListener() {

                    @Override
                    public void onTimeSet(TimePicker timePicker, int i, int i1) {
                        wednesdayToTxt.setText(i+":"+i1);
                    }
                }, calendar.get(Calendar.HOUR_OF_DAY)
                        , calendar.get(Calendar.MINUTE), true).show();
            }
        });
        thursdayFromBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                new TimePickerDialog(getActivity(), new TimePickerDialog.OnTimeSetListener() {

                    @Override
                    public void onTimeSet(TimePicker timePicker, int i, int i1) {
                        thursdayFromTxt.setText(i+":"+i1);
                    }
                }, calendar.get(Calendar.HOUR_OF_DAY)
                        , calendar.get(Calendar.MINUTE), true).show();
            }
        });
        thursdayToBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                new TimePickerDialog(getActivity(), new TimePickerDialog.OnTimeSetListener() {

                    @Override
                    public void onTimeSet(TimePicker timePicker, int i, int i1) {
                        thursdayToTxt.setText(i+":"+i1);
                    }
                }, calendar.get(Calendar.HOUR_OF_DAY)
                        , calendar.get(Calendar.MINUTE), true).show();
            }
        });
        fridayFromBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                new TimePickerDialog(getActivity(), new TimePickerDialog.OnTimeSetListener() {

                    @Override
                    public void onTimeSet(TimePicker timePicker, int i, int i1) {
                        fridayFromTxt.setText(i+":"+i1);
                    }
                }, calendar.get(Calendar.HOUR_OF_DAY)
                        , calendar.get(Calendar.MINUTE), true).show();
            }
        });
        fridayToBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                new TimePickerDialog(getActivity(), new TimePickerDialog.OnTimeSetListener() {

                    @Override
                    public void onTimeSet(TimePicker timePicker, int i, int i1) {
                        fridayToTxt.setText(i+":"+i1);
                    }
                }, calendar.get(Calendar.HOUR_OF_DAY)
                        , calendar.get(Calendar.MINUTE), true).show();
            }
        });
        saturdayFromBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                new TimePickerDialog(getActivity(), new TimePickerDialog.OnTimeSetListener() {

                    @Override
                    public void onTimeSet(TimePicker timePicker, int i, int i1) {
                        saturdayFromTxt.setText(i+":"+i1);
                    }
                }, calendar.get(Calendar.HOUR_OF_DAY)
                        , calendar.get(Calendar.MINUTE), true).show();
            }
        });
        saturdayToBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                new TimePickerDialog(getActivity(), new TimePickerDialog.OnTimeSetListener() {

                    @Override
                    public void onTimeSet(TimePicker timePicker, int i, int i1) {
                        saturdayToTxt.setText(i+":"+i1);
                    }
                }, calendar.get(Calendar.HOUR_OF_DAY)
                        , calendar.get(Calendar.MINUTE), true).show();
            }
        });
        sundayFromBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                new TimePickerDialog(getActivity(), new TimePickerDialog.OnTimeSetListener() {

                    @Override
                    public void onTimeSet(TimePicker timePicker, int i, int i1) {
                        sundayFromTxt.setText(i+":"+i1);
                    }
                }, calendar.get(Calendar.HOUR_OF_DAY)
                        , calendar.get(Calendar.MINUTE), true).show();
            }
        });
        sundayToBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                new TimePickerDialog(getActivity(), new TimePickerDialog.OnTimeSetListener() {

                    @Override
                    public void onTimeSet(TimePicker timePicker, int i, int i1) {
                        sundayToTxt.setText(i+":"+i1);
                    }
                }, calendar.get(Calendar.HOUR_OF_DAY)
                        , calendar.get(Calendar.MINUTE), true).show();
            }
        });

    }

    private void checkBoxes(){
        mondayChBx.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                if(!b){
                    if (lastFromTime != null && !lastFromTime.equals(""))
                        mondayFromTxt.setText(lastFromTime);
                    mondayToTxt.setText(lastToTime);
                    mondayFromBtn.setClickable(true);
                    mondayToBtn.setClickable(true);
                }else{
                    lastFromTime = mondayFromTxt.getText().toString();
                    lastToTime = mondayToTxt.getText().toString();
                    mondayFromBtn.setClickable(false);
                    mondayToBtn.setClickable(false);
                    mondayFromTxt.setText("??");
                    mondayToTxt.setText("??");
                }
            }
        });
        tuesdayChBx.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                if(!b){
                    if (lastFromTime != null && !lastFromTime.equals(""))
                        tuesdayFromTxt.setText(lastFromTime);
                    tuesdayToTxt.setText(lastToTime);
                    tuesdayFromBtn.setClickable(true);
                    tuesdayToBtn.setClickable(true);
                }else{
                    lastFromTime = tuesdayFromTxt.getText().toString();
                    lastToTime = tuesdayToTxt.getText().toString();
                    tuesdayFromBtn.setClickable(false);
                    tuesdayToBtn.setClickable(false);
                    tuesdayFromTxt.setText("??");
                    tuesdayToTxt.setText("??");
                }
            }
        });
        wednesdayChBx.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                if(!b){
                    if (lastFromTime != null && !lastFromTime.equals(""))
                        wednesdayFromTxt.setText(lastFromTime);
                    wednesdayToTxt.setText(lastToTime);
                    wednesdayFromBtn.setClickable(true);
                    wednesdayToBtn.setClickable(true);
                }else{
                    lastFromTime = wednesdayFromTxt.getText().toString();
                    lastToTime = wednesdayToTxt.getText().toString();
                    wednesdayFromBtn.setClickable(false);
                    wednesdayToBtn.setClickable(false);
                    wednesdayFromTxt.setText("??");
                    wednesdayToTxt.setText("??");
                }
            }
        });
        thursdayChBx.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                if(!b){
                    if (lastFromTime != null && !lastFromTime.equals(""))
                        thursdayFromTxt.setText(lastFromTime);
                    thursdayToTxt.setText(lastToTime);
                    thursdayFromBtn.setClickable(true);
                    thursdayToBtn.setClickable(true);
                }else{
                    lastFromTime = thursdayFromTxt.getText().toString();
                    lastToTime = thursdayToTxt.getText().toString();
                    thursdayFromBtn.setClickable(false);
                    thursdayToBtn.setClickable(false);
                    thursdayFromTxt.setText("??");
                    thursdayToTxt.setText("??");
                }
            }
        });
        fridayChBx.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                if(!b){
                    if (lastFromTime != null && !lastFromTime.equals(""))
                        fridayFromTxt.setText(lastFromTime);
                    fridayToTxt.setText(lastToTime);
                    fridayFromBtn.setClickable(true);
                    fridayToBtn.setClickable(true);
                }else{
                    lastFromTime = fridayFromTxt.getText().toString();
                    lastToTime = fridayToTxt.getText().toString();
                    fridayFromBtn.setClickable(false);
                    fridayToBtn.setClickable(false);
                    fridayFromTxt.setText("??");
                    fridayToTxt.setText("??");
                }
            }
        });
        saturdayChBx.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                if(!b){
                    if (lastFromTime != null && !lastFromTime.equals(""))
                        mondayFromTxt.setText(lastFromTime);
                    saturdayToTxt.setText(lastToTime);
                    saturdayFromBtn.setClickable(true);
                    saturdayToBtn.setClickable(true);
                }else{
                    lastFromTime = saturdayFromTxt.getText().toString();
                    lastToTime = saturdayToTxt.getText().toString();
                    saturdayFromBtn.setClickable(false);
                    saturdayToBtn.setClickable(false);
                    saturdayFromTxt.setText("??");
                    saturdayToTxt.setText("??");
                }
            }
        });
        sundayChBx.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                if(!b){
                    if (lastFromTime != null && !lastFromTime.equals(""))
                        sundayFromTxt.setText(lastFromTime);
                    sundayToTxt.setText(lastToTime);
                    sundayFromBtn.setClickable(true);
                    sundayToBtn.setClickable(true);
                }else{
                    lastFromTime = sundayFromTxt.getText().toString();
                    lastToTime = sundayToTxt.getText().toString();
                    sundayFromBtn.setClickable(false);
                    sundayToBtn.setClickable(false);
                    sundayFromTxt.setText("??");
                    sundayToTxt.setText("??");
                }
            }
        });
    }

    private void saveBH(){
        saveBHBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                addOrUpdate();
            }
        });
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
        JSONObject parentObject = null;
        businessHoursAL = new ArrayList<>();
        if (response == "failed"){
            Toast.makeText(getContext(), "Sorry, something went wrong", Toast.LENGTH_SHORT).show();
            return;
        }else {
            try {
                parentObject = new JSONObject(response);
                JSONArray parentArray = parentObject.getJSONArray("businessHours");

                for (int i = 0; i < parentArray.length(); i++) {
                    DaysOfWeek businessHours = new DaysOfWeek();
                    JSONObject finalObject = parentArray.getJSONObject(i);
                    businessHours.day = finalObject.getString("DayOfWeek");
                    businessHours.fromTime =  finalObject.getString("FromTime");
                    businessHours.toTime = finalObject.getString("ToTime");
                    businessHours.isClosed = Boolean.valueOf(finalObject.getString("OpeningStatus"));
                    businessHoursAL.add(businessHours);
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
        display();
    }

    private void display(){
        for(DaysOfWeek businessHours : businessHoursAL){
            String day = businessHours.day;
            switch (day){
                case "Monday":
                    mondayFromTxt.setText(businessHours.fromTime);
                    mondayToTxt.setText(businessHours.toTime);
                    mondayChBx.setChecked(businessHours.isClosed);
                    break;
                case "Tuesday":
                    tuesdayFromTxt.setText(businessHours.fromTime);
                    tuesdayToTxt.setText(businessHours.toTime);
                    tuesdayChBx.setChecked(businessHours.isClosed);
                    break;
                case "Wednesday":
                    wednesdayFromTxt.setText(businessHours.fromTime);
                    wednesdayToTxt.setText(businessHours.toTime);
                    wednesdayChBx.setChecked(businessHours.isClosed);
                    break;
                case "Thursday":
                    thursdayFromTxt.setText(businessHours.fromTime);
                    thursdayToTxt.setText(businessHours.toTime);
                    thursdayChBx.setChecked(businessHours.isClosed);
                    break;
                case "Friday":
                    fridayFromTxt.setText(businessHours.fromTime);
                    fridayToTxt.setText(businessHours.toTime);
                    fridayChBx.setChecked(businessHours.isClosed);
                    break;
                case "Saturday":
                    saturdayFromTxt.setText(businessHours.fromTime);
                    saturdayToTxt.setText(businessHours.toTime);
                    saturdayChBx.setChecked(businessHours.isClosed);
                    break;
                case "Sunday":
                    sundayFromTxt.setText(businessHours.fromTime);
                    sundayToTxt.setText(businessHours.toTime);
                    sundayChBx.setChecked(businessHours.isClosed);
                    break;

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

    private void addOrUpdate(){
        String url = "http://34.203.215.247/addUpdateBusinessHours.php";
        StringRequest stringRequest = new StringRequest(Request.Method.POST,
                url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        JSONObject parentObject = null;
                        if (response.equals("Hours updated successfully") || response.equals("Added Successfully")){
                            Toast.makeText(getContext(), response, Toast.LENGTH_SHORT).show();

                        }else {
                            Toast.makeText(getContext(), "Oops, Something went wrong"+response, Toast.LENGTH_SHORT).show();
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        if (error != null){
                            Toast.makeText(getContext(), "Something went wrong", Toast.LENGTH_LONG).show();
                        }
                    }
                }
        ){
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String,String> params = new HashMap<>();

                params.put("provID",sharedPreferences.getString("userID","defaultvalue"));
                params.put("FromMonday",mondayFromTxt.getText().toString());
                params.put("FromTuesday",tuesdayFromTxt.getText().toString());
                params.put("FromWednesday",wednesdayFromTxt.getText().toString());
                params.put("FromThursday",thursdayFromTxt.getText().toString());
                params.put("FromFriday",fridayFromTxt.getText().toString());
                params.put("FromSaturday",saturdayFromTxt.getText().toString());
                params.put("FromSunday",sundayFromTxt.getText().toString());
                params.put("ToMonday",mondayToTxt.getText().toString());
                params.put("ToTuesday",tuesdayToTxt.getText().toString());
                params.put("ToWednesday",wednesdayToTxt.getText().toString());
                params.put("ToThursday",thursdayToTxt.getText().toString());
                params.put("ToFriday",fridayToTxt.getText().toString());
                params.put("ToSaturday",saturdayToTxt.getText().toString());
                params.put("ToSunday",sundayToTxt.getText().toString());
                params.put("StatusMonday",mondayChBx.isChecked()+"");
                params.put("StatusTuesday",tuesdayChBx.isChecked()+"");
                params.put("StatusWednesday",wednesdayChBx.isChecked()+"");
                params.put("StatusThursday",thursdayChBx.isChecked()+"");
                params.put("StatusFriday",fridayChBx.isChecked()+"");
                params.put("StatusSaturday",saturdayChBx.isChecked()+"");
                params.put("StatusSunday",sundayChBx.isChecked()+"");

                return params;
            }
        };
        MySingleton.getInstance(getActivity().getApplicationContext()).addToRequestQueue(stringRequest);
    }
}
