package com.example.ratul.pocketlends;

import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONException;
import org.json.JSONObject;

public class Invest extends AppCompatActivity {

    RelativeLayout orderDetailsView;
    RelativeLayout getOrderView;
    TextView invest_request_amount;
    TextView OTPTextView;
    EditText AmountRequested;
    Button getOrder_button;
    Button ButtonCancel;

    String RequestUrl;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_invest);
        getOrderView = (RelativeLayout) findViewById(R.id.getOrderView);
        orderDetailsView = (RelativeLayout) findViewById(R.id.orderDetailsView);
        invest_request_amount = (TextView) findViewById(R.id.invest_request_amount);
        OTPTextView = (TextView) findViewById(R.id.order_id);
        getOrder_button=(Button) findViewById(R.id.getOrder_button);
        ButtonCancel = (Button) findViewById(R.id.cancel);
        AmountRequested = (EditText) findViewById(R.id.amount_requested);

        Intent intent = getIntent();
        String option = intent.getStringExtra("option");
        switch(option)
        {
            case "invest":
                RequestUrl = Utils._domain+Utils._urlInvest;
                break;

            case "withdraw":
                RequestUrl = Utils._domain+Utils._urlWithdraw;
                break;
        }

        invest();
    }
    private void showToast(String data)
    {
        final int duration = Toast.LENGTH_SHORT;
        final Context context = getApplicationContext();
        Toast toast = Toast.makeText(context, data, duration);
        toast.show();
    }

    private void invest()
    {
        Utils.refreshTokens();
        CheckInvestAsyncTask checkInvest = new CheckInvestAsyncTask();
        checkInvest.execute(RequestUrl,"GET",null,Utils.pref_file.getString("access_token",""));
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
                noRequest();
                return;
            }
            RequestExist(result);
        }
    }

    private void noRequest() {

        if(orderDetailsView.getVisibility() == View.VISIBLE)
        {
            orderDetailsView.setVisibility(View.INVISIBLE);
        }
        if(getOrderView.getVisibility() == View.INVISIBLE)
        {
            getOrderView.setVisibility(View.VISIBLE);
        }
        getOrder_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String amount;
                int amount_int;
                if(Utils.isEmpty(AmountRequested))
                {
                    showToast("plese enter amount");
                }

                else
                {
                    amount = AmountRequested.getText().toString();
                    amount_int = Integer.parseInt(amount);
                    JSONObject addJson = new JSONObject();
                    try {
                        addJson.put("amt",amount_int);
                        addJson.put("options","add");

                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                    InvestAsyncTask add = new InvestAsyncTask();
                    add.execute(RequestUrl,"POST",addJson.toString(),Utils.pref_file.getString("access_token",""));
                }
            }
        });
    }

    private void RequestExist(String result)
    {
        String orderAmount;
        String orderOTP;

        if (getOrderView.getVisibility() == View.VISIBLE)
        {
            getOrderView.setVisibility(View.INVISIBLE);
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
                InvestAsyncTask cancel = new InvestAsyncTask();
                cancel.execute(RequestUrl,"POST",cancelJson.toString(),Utils.pref_file.getString("access_token",""));
            }
        });
    }
    private class InvestAsyncTask extends AsyncTask<String,Void,String> {
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
            Intent i = new Intent(Invest.this,MainActivity.class);
            startActivity(i);
            finish();
        }
    }
}
