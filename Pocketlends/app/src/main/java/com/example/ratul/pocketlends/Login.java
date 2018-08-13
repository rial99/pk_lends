package com.example.ratul.pocketlends;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import org.json.JSONException;
import org.json.JSONObject;

public class Login extends AppCompatActivity {
    private final String _URL ="/login";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        String domain = getString(R.string.domain_name);
        final String REQUEST_URL = domain+_URL;

        final Button logIn_button = (Button) findViewById(R.id.login_button);

        logIn_button.setOnClickListener(new View.OnClickListener() {
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
                    LoginAsyncTask task = new LoginAsyncTask();
                    task.execute(REQUEST_URL,"POST",payload_json);
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

    private class LoginAsyncTask extends AsyncTask<String,Void,String> {
        @Override
        protected String doInBackground(String... HTTPdata) {

            if (HTTPdata.length < 1 || HTTPdata[0] == null) {
                return null;
            }
            String result = Utils.fetchData(HTTPdata[0],HTTPdata[1],HTTPdata[2],null);
            return result;
        }

        @Override
        protected void onPostExecute(String result) {
            // If there is no result, do nothing.
            String access_token="";
            String refresh_token="";
            if (result == null) {
                showToast("credentials invalid");
                return;
            }
            //showToast(result);
            try {
                JSONObject JWTtoken = new JSONObject(result);
                access_token = JWTtoken.getString("access_token");
                refresh_token = JWTtoken.getString("refresh_token");

            }catch (JSONException e)
            {

            }
            SharedPreferences pref_file = getSharedPreferences(getString(R.string.pocketlends_preferenceFileKey),Context.MODE_PRIVATE);
            SharedPreferences.Editor editor = pref_file.edit();

            editor.putString("access_token",access_token);
            editor.putString("refresh_token",refresh_token);
            editor.apply();
            showToast("Login succesful !");
            Intent MainActivity_intent = new Intent(Login.this,MainActivity.class);
            startActivity(MainActivity_intent);
        }

    }
}
