package com.demo.example.sss.capstone.validation;

import android.graphics.Color;
import android.widget.EditText;

/**
 * Created by SSS on 10/1/17.
 */

public class Validation {

    public boolean validateEditTexts(EditText editText){

        if (editText.getText().toString().equals("") || editText.getText() == null ||
                editText.getText().length() < 1){
            editText.setError("please enter your "+editText.getHint());
            editText.setBackgroundColor(Color.RED);
            return false;

        }else{
            editText.setBackgroundColor(Color.WHITE);
            return true;
        }
    }
}
