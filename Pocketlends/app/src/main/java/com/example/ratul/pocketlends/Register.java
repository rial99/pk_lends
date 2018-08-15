package com.example.ratul.pocketlends;

import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;


public class Register extends AppCompatActivity {
    private final String _URL ="/register";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        String domain = getString(R.string.domain_name);
        final String REQUEST_URL = domain+_URL;

        final Button signIn_button = (Button) findViewById(R.id.signIn_button);
        final TextView Log_in_button = (TextView) findViewById(R.id.Log_in_button);

        Log_in_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent LogI = new Intent(Register.this,Login.class);
                startActivity(LogI);
            }
        });

        signIn_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                EditText username = (EditText) findViewById(R.id.username);
                EditText password = (EditText) findViewById(R.id.password);
                if (Utils.isEmpty(username) || Utils.isEmpty(password))
                {
                    showToast("please provide username and password");
                }
                else
                {
                    String payload_json = Utils.toJson(username.getText().toString(),password.getText().toString());
                    RegisterAsyncTask task = new RegisterAsyncTask();
                    task.execute(REQUEST_URL,"POST",payload_json,null);
                }
            }
        });
    }

    private void showToast(String data)
    {
        final int duration = Toast.LENGTH_SHORT;
        final Context context = getApplicationContext();
        Toast toast = Toast.makeText(context, data, duration);
        toast.show();
    }

    private class RegisterAsyncTask extends AsyncTask<String,Void,String>{
        @Override
        protected String doInBackground(String... HTTPdata) {

            if (HTTPdata.length < 1 || HTTPdata[0] == null) {
                return null;
            }
            String result = Utils.fetchData(HTTPdata[0],HTTPdata[1],HTTPdata[2],HTTPdata[3]);
            return result;
        }
        @Override
        protected void onPostExecute(String result){
            // If there is no result, do nothing.
            if (result == null) {
                return;
            }
            showToast(result);
            Intent Login_intent = new Intent(Register.this,Login.class);
            startActivity(Login_intent);
        }
    }
}
