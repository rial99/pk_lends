package com.example.ratul.pocketlends;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //Sign in button handling
        final TextView Sign_in_button = (TextView) findViewById(R.id.button_sign_in);

        Sign_in_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent sign_in_intent = new Intent(MainActivity.this,Register.class);
                startActivity(sign_in_intent);
            }
        });

        //Login button handling
        final TextView Login_in_button = (TextView) findViewById(R.id.button_log_in);

        Login_in_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent Login_intent = new Intent(MainActivity.this,Login.class);
                startActivity(Login_intent);
            }
        });
    }
}
