package com.example.ratul.pocketlends;

import android.content.Context;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class Login extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        final Button logIn_button = (Button) findViewById(R.id.login_button);
        final int duration = Toast.LENGTH_SHORT;
        final Context context = getApplicationContext();


        logIn_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                EditText username = (EditText) findViewById(R.id.username);
                EditText password = (EditText) findViewById(R.id.password);
                if (Utils.isEmpty(username) || Utils.isEmpty(password))
                {
                    Toast toast = Toast.makeText(context, "provide username and password", duration);
                    toast.show();

                }
                else
                {

                }
            }
        });
    }


}
