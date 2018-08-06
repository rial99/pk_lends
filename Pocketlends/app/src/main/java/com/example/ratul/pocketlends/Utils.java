package com.example.ratul.pocketlends;

import android.widget.EditText;

public class Utils {
    public static boolean isEmpty(EditText etText) {
        if (etText.getText().toString().trim().length() > 0)
            return false;

        return true;
    }
}
