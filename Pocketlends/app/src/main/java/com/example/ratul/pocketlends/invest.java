package com.example.ratul.pocketlends;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONException;
import org.json.JSONObject;


public class invest extends AppCompatActivity {

    SharedPreferences pref_file=null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_invest);
        pref_file = getSharedPreferences(getString(R.string.pocketlends_preferenceFileKey), Context.MODE_PRIVATE);

        String domain = getString(R.string.domain_name);
        final String REQUEST_URL = domain+Utils._urlInvest;
        Button order_id_button = (Button) findViewById(R.id.order_id_button);
        final EditText amount = (EditText) findViewById(R.id.amount);
        order_id_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(Utils.isEmpty(amount))
                {
                    showToast("please enter amount");
                }
                else {
                    try {
                        JSONObject payload = new JSONObject();
                        payload.put("invest_amt", Integer.parseInt(amount.getText().toString()));
                        InvestAsyncTask invest =new InvestAsyncTask();
                        invest.execute(REQUEST_URL,"POST",payload.toString(),pref_file.getString("access_token",""));
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
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

    private class InvestAsyncTask extends AsyncTask<String,Void,String> {
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
            TextView order_id_TextView=(TextView)findViewById(R.id.order_id);
            String orderId;
            if (result == null) {
                return;
            }
            showToast(result);
            try{
                JSONObject order_id_json = new JSONObject(result);
                orderId = order_id_json.getString("order_id");
                order_id_TextView.setText(orderId);
            }catch (JSONException e)
            {
                e.printStackTrace();
            }
        }
    }



}
