package com.example.ratul.pocketlends;

import android.content.Context;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
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
                    String[] REQ_S = new String[3];
                    REQ_S[0] = REQUEST_URL;
                    REQ_S[1] = "POST";
                    REQ_S[2] = Utils.toJson(username.getText().toString(),password.getText().toString());
                    RegisterAsyncTask task = new RegisterAsyncTask();
                    task.execute(REQ_S);
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

    private class RegisterAsyncTask extends AsyncTask<String[],Void,String>{
        @Override
        protected String doInBackground(String[]... HTTPdata) {

            if (HTTPdata.length < 1 || HTTPdata[0] == null) {
                return null;
            }

            String[] REQ_S = new String[3];
            REQ_S = HTTPdata[0];
            String result = Utils.fetchData(REQ_S[0],REQ_S[1],REQ_S[2]);
            return result;
        }
        @Override
        protected void onPostExecute(String result){
            // If there is no result, do nothing.
            if (result == null) {
                return;
            }
            showToast(result);
        }
    }
}
