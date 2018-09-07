package com.snippetTech.ratul.pocketlends;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONException;
import org.json.JSONObject;


public class MainActivity extends AppCompatActivity {

    String urlUser;
    String RequestUrl;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Utils.pref_file = getSharedPreferences(getString(R.string.pocketlends_preferenceFileKey), Context.MODE_PRIVATE);
        Utils._domain = getString(R.string.domain_name);
        //code here if no connection to server show an alert box and force close the app
        statusAsyncTask statusTask = new statusAsyncTask();
        statusTask.execute(Utils._domain+Utils._urlStatus,"GET",null,null);

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
            ImageButton RefreshButton = (ImageButton) findViewById(R.id.refresh_button);

            InvestButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    RequestUrl = Utils._domain+Utils._urlInvest;
                   checkInvest();
                }
            });
            WithdrawButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    RequestUrl =Utils._domain+Utils._urlWithdraw;
                    checkInvest();
                }
            });
            BorrowButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    RequestUrl = Utils._domain+Utils._urlBorrow;
                    checkInvest();
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
            RefreshButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    refreshData();
                }
            });
        }
        else
        {
            Intent i = new Intent(MainActivity.this,Register.class);
            startActivity(i);
        }
    }
    private void checkInvest()
    {
        Utils.refreshTokens();
        CheckInvestAsyncTask checkInvest = new CheckInvestAsyncTask();
        checkInvest.execute(RequestUrl,"GET",null,Utils.pref_file.getString("access_token",""));
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

    private class CheckInvestAsyncTask extends AsyncTask<String,Void,String> {
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
                AlertDialog.Builder mBuilder = new AlertDialog.Builder(MainActivity.this);
                View mView = getLayoutInflater().inflate(R.layout.pop_up,null);
                final EditText amount = (EditText) mView.findViewById(R.id.user_input_amount);
                final Button btn = (Button) mView.findViewById(R.id.submit_btn);
                mBuilder.setView(mView);
                final AlertDialog dialog = mBuilder.create();
                dialog.show();
                btn.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        int amount_int;
                        if (Utils.isEmpty(amount))
                        {
                            showToast("enter amount");
                        }
                        else
                        {
                            amount_int = Integer.parseInt(amount.getText().toString());
                            JSONObject investJSON = new JSONObject();
                            try {
                                investJSON.put("amt",amount_int);
                                investJSON.put("options","add");

                            } catch (JSONException e) {
                                // TODO Auto-generated catch block
                                e.printStackTrace();
                            }
                            invest(investJSON.toString());
                            dialog.dismiss();
                        }
                    }
                });


            }
            else
            {
                Intent i = new Intent(MainActivity.this,Invest.class);
                i.putExtra("request_url",RequestUrl);
                startActivity(i);
            }
        }
    }

    private void invest(String payload) {
        InvestAsyncTask invest = new InvestAsyncTask();
        invest.execute(RequestUrl,"POST",payload,Utils.pref_file.getString("access_token",""));
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
            JSONObject error = null;
            try {
                error = new JSONObject(result);
                if (error.has("error"))
                {
                    showToast("user operation not allowed");
                }
                else{
                    Intent i = new Intent(MainActivity.this,MainActivity.class);
                    startActivity(i);
                    finish();
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
    }
    private class statusAsyncTask extends AsyncTask<String,Void,String> {
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
                AlertDialog.Builder altdial = new AlertDialog.Builder(MainActivity.this);
                altdial.setMessage("Do you want to quit the app ???").setCancelable(false)
                        .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                finish();
                            }
                        });
                AlertDialog alert = altdial.create();
                alert.setTitle("No connection to Server");
                alert.show();
            }
        }
    }
    public class InvestAsyncTask extends AsyncTask<String,Void,String> {
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
            try {
                JSONObject error = new JSONObject(result);
                if (error.has("error"))
                {
                    showToast("user operation not allowed");
                }
                else
                {
                    Intent i = new Intent(MainActivity.this,Invest.class);
                    i.putExtra("request_url",RequestUrl);
                    startActivity(i);
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }

        }
    }

}
