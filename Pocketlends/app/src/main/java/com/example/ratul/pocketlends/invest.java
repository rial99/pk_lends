package com.example.ratul.pocketlends;

import android.app.Activity;
import android.content.Context;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.razorpay.Checkout;
import com.razorpay.PaymentResultListener;

import org.json.JSONException;
import org.json.JSONObject;

public class invest extends AppCompatActivity implements PaymentResultListener {

    EditText value;

    Button Razorpay_via;
    int payamount;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_invest);

        value = (EditText) findViewById(R.id.amount);
        Razorpay_via = (Button) findViewById(R.id.razorpay_via);

        Razorpay_via.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startPayment();
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

    private void startPayment() {
        payamount = Integer.parseInt(value.getText().toString());
        Checkout checkout = new Checkout();
        checkout.setImage(R.mipmap.ic_launcher);

        final Activity activity=this;

        try {
            JSONObject options = new JSONObject();
            options.put("description","Order #123456");
            options.put("currency","INR");
            options.put("amount",payamount*100);
            checkout.open(activity,options);

        }catch (JSONException e)
        {
            e.printStackTrace();
        }


    }


    @Override
    public void onPaymentSuccess(String s) {
        showToast("your payment is succesful");

    }

    @Override
    public void onPaymentError(int i, String s) {
        showToast("payment failed error");
    }
}
