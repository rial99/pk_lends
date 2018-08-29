package com.example.ratul.pocketlends;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONException;
import org.json.JSONObject;


public class MainActivity extends AppCompatActivity {

    String urlUser;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);


        Utils.pref_file = getSharedPreferences(getString(R.string.pocketlends_preferenceFileKey), Context.MODE_PRIVATE);
        Utils._domain = getString(R.string.domain_name);
        if (Utils.pref_file.getString("access_token","") != "" && Utils.pref_file.getString("refresh_token","") != "")
        {
            setContentView(R.layout.activity_main);


            urlUser = Utils._domain+Utils._urlUser;
            final String urlLogout = Utils._domain+Utils._urlLogout;

            refreshData();

            Button LogOutButton =(Button) findViewById(R.id.Log_out_B);
            Button InvestButton = (Button) findViewById(R.id.add);
            Button WithdrawButton = (Button) findViewById(R.id.withdraw);
            Button BorrowButton = (Button) findViewById(R.id.borrow);
            Button RepayButton = (Button) findViewById(R.id.repay);

            InvestButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent i = new Intent(MainActivity.this,Invest.class);
                    i.putExtra("option","invest");
                    startActivity(i);
                }
            });
            WithdrawButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent i = new Intent(MainActivity.this,Invest.class);
                    i.putExtra("option","withdraw");
                    startActivity(i);

                }
            });
            BorrowButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent i = new Intent(MainActivity.this,Invest.class);
                    i.putExtra("option","borrow");
                    startActivity(i);
                }
            });
            RepayButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    RepayAsyncTask task = new RepayAsyncTask();
                    task.execute(Utils._domain+Utils._urlRepay,"GET",null,Utils.pref_file.getString("access_token",""));
                }
            });
            LogOutButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {

                    Utils.refreshTokens();

                    LogOutAsyncTask logoutTask = new LogOutAsyncTask();
                    logoutTask.execute(urlLogout,"GET",null,Utils.pref_file.getString("access_token",""));
                }
            });

        }
        else
        {
            Intent i = new Intent(MainActivity.this,Register.class);
            startActivity(i);
        }


    }

    private void refreshData() {
        Utils.refreshTokens();

        UserDataAsyncTask UserTask = new UserDataAsyncTask();
        UserTask.execute(urlUser,"GET",null,Utils.pref_file.getString("access_token",""));
    }
    private void showToast(String data)
    {
        final int duration = Toast.LENGTH_SHORT;
        final Context context = getApplicationContext();
        Toast toast = Toast.makeText(context, data, duration);
        toast.show();
    }
    private void updateui(User u)
    {
        TextView username = (TextView) findViewById(R.id.username);
        username.setText(u.Username);

        TextView invest_amt = (TextView) findViewById(R.id.invest_amt);
        invest_amt.setText(u.Invest_amt);

        TextView lend_amt = (TextView) findViewById(R.id.lend_amt);
        lend_amt.setText(u.Lend_amt);

        TextView Lend_interest = (TextView) findViewById(R.id.lend_interest);
        Lend_interest.setText(u.Interest_amt_L);

        TextView borrow_amt = (TextView) findViewById(R.id.borrow_amt);
        borrow_amt.setText(u.Borrow_amt);

        TextView Borrow_interest = (TextView) findViewById(R.id.borrow_interest);
        Borrow_interest.setText(u.Interest_amt_B);
    }

    public class RefreshAsyncTask extends AsyncTask<String,Void,String> {
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
            String access_token="";
            if (result == null) {
                return;
            }

            try {
                JSONObject JWTtoken = new JSONObject(result);
                access_token = JWTtoken.getString("access_token");
            }catch (JSONException e)
            {

            }
            SharedPreferences.Editor editor = Utils.pref_file.edit();
            editor.putString("access_token",access_token);
            editor.apply();

        }
    }

    private class UserDataAsyncTask extends AsyncTask<String,Void,String> {
        @Override
        protected String doInBackground(String... HTTPdata) {

            if (HTTPdata.length < 1 || HTTPdata[0] == null) {
                return null;
            }
            String result = Utils.fetchData(HTTPdata[0], HTTPdata[1], HTTPdata[2], HTTPdata[3]);
            return result;
        }

        @Override
        protected void onPostExecute(String result) {
            // If there is no result, do nothing.
            if (result == null) {
                return;
            }
            User u = Utils.extractUserFeatureFromJson(result);
            updateui(u);
        }
    }

    private class LogOutAsyncTask extends AsyncTask<String,Void,String>{
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
            SharedPreferences.Editor editor = Utils.pref_file.edit();
            editor.putString("access_token","");
            editor.putString("refresh_token","");
            editor.apply();

            showToast(result);

            Intent Login_intent = new Intent(MainActivity.this,Login.class);
            startActivity(Login_intent);
            finish();
        }
    }

    private class RepayAsyncTask extends AsyncTask<String,Void,String> {
        @Override
        protected String doInBackground(String... HTTPdata) {

            if (HTTPdata.length < 1 || HTTPdata[0] == null) {
                return null;
            }
            String result = Utils.fetchData(HTTPdata[0], HTTPdata[1], HTTPdata[2], HTTPdata[3]);
            return result;
        }

        @Override
        protected void onPostExecute(String result) {
            // If there is no result, do nothing.
            if (result == null) {
                return;
            }
            showToast(result);
            Intent i = new Intent(MainActivity.this,MainActivity.class);
            startActivity(i);
            finish();
        }
    }

}
