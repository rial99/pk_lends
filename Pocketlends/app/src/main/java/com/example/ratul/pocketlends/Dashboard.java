package com.example.ratul.pocketlends;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class Dashboard extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dashboard);

        //Sign in button handling
        final Button Sign_in_button = (Button) findViewById(R.id.button_sign_in);

        Sign_in_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent sign_in_intent = new Intent(Dashboard.this,Register.class);
                startActivity(sign_in_intent);
            }
        });

        //Login button handling
        final Button Login_in_button = (Button) findViewById(R.id.button_log_in);

        Login_in_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent Login_intent = new Intent(Dashboard.this,Login.class);
                startActivity(Login_intent);
            }
        });
    }
}
