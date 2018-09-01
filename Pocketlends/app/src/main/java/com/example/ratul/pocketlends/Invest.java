package com.example.ratul.pocketlends;

import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONException;
import org.json.JSONObject;

public class Invest extends AppCompatActivity {

    RelativeLayout orderDetailsView;
    static TextView invest_request_amount;
    static TextView R_type;
    static TextView OTPTextView;
    static Button ButtonCancel;
    static String request_url;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_invest);
        orderDetailsView = (RelativeLayout) findViewById(R.id.orderDetailsView);
        invest_request_amount = (TextView) findViewById(R.id.invest_request_amount);
        OTPTextView = (TextView) findViewById(R.id.order_id);
        R_type = (TextView) findViewById(R.id.request_type);
        ButtonCancel = (Button) findViewById(R.id.cancel);

        Intent intent = getIntent();
        request_url = intent.getStringExtra("request_url");

        RequestExist(request_url);

        ButtonCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                JSONObject cancelJson = new JSONObject();
                try {
                    cancelJson.put("amt",0);
                    cancelJson.put("options","cancel");

                } catch (JSONException e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                }
                Invest.InvestAsyncTask cancel = new Invest().new InvestAsyncTask();
                cancel.execute(request_url,"POST",cancelJson.toString(),Utils.pref_file.getString("access_token",""));
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


    public static void RequestExist(String result)
    {
        Invest.FetchAsyncTask fetch = new Invest().new FetchAsyncTask();
        fetch.execute(request_url,"GET",null,Utils.pref_file.getString("access_token",""));
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
            showToast(result);
        }
    }
    public class FetchAsyncTask extends AsyncTask<String,Void,String> {
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
            String orderAmount;
            String orderOTP;
            // If there is no result, do nothing.
            if (result == null) {
                return;
            }
            try{
                JSONObject orderDetails = new JSONObject(result);
                orderAmount = orderDetails.getString("amount");
                orderOTP = orderDetails.getString("order_id");
                invest_request_amount.setText(orderAmount);
                OTPTextView.setText(orderOTP);

            }catch (JSONException e)
            {
                e.printStackTrace();
            }
        }
    }
}
