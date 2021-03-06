package com.snippetTech.ratul.pocketlends;

import android.content.Context;
import android.content.SharedPreferences;
import android.text.TextUtils;
import android.util.Log;
import android.widget.EditText;


import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.nio.charset.Charset;

public class Utils {
    static SharedPreferences pref_file=null;
    static String _domain="";
    static String _urlUser = "/user_info";
    static String _urlRefresh = "/refresh_token";
    static String _urlLogout = "/logout";
    static String _urlRegister ="/register";
    static String _urlLogin ="/login";
    static String _urlInvest = "/invest";
    static String _urlWithdraw = "/withdraw";
    static String _urlBorrow = "/borrow";
    static String _urlRepay = "/repay";
    static String _urlStatus = "/status";

    public static String fetchData(String requestUrl,String method,String payload,String acess_token)
    {
        URL url = createUrl(requestUrl);
        String jsonResponse = null;
        try {
            jsonResponse = makeHttpRequest(url,method,payload,acess_token);
        } catch (IOException e) {
            Log.e(LOG_TAG, "Error closing input stream", e);
        }
        return jsonResponse;
    }

    public static void refreshTokens()
    {
        String urlRefresh = _domain+_urlRefresh;
        MainActivity.RefreshAsyncTask task = new MainActivity().new RefreshAsyncTask();
        task.execute(urlRefresh,"GET",null,pref_file.getString("refresh_token",""));
    }

    public static String toJson(String username,String password)
    {
        JSONObject json = new JSONObject();
        try {
            json.put("username",username);
            json.put("password",password);

        } catch (JSONException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }

        return json.toString();
    }

    /** Tag for the log messages */
    public static final String LOG_TAG = Utils.class.getSimpleName();

    public static boolean isEmpty(EditText etText) {
        if (etText.getText().toString().trim().length() > 0)
            return false;

        return true;
    }
    /**
     * Returns new URL object from the given string URL.
     */

    public static User extractUserFeatureFromJson(String userJson)
    {
        String username="";
        String invest_amt = "";
        String lend_amt = "";
        String borrow_amt = "";
        String interest_amt_L = "";
        String interest_amt_B = "";
        if (TextUtils.isEmpty(userJson)) {
            return null;
        }
        try {
            JSONObject user = new JSONObject(userJson);
            username = user.getString("username");
            invest_amt = user.getString("invest_amt");
            lend_amt = user.getString("lend_amt");
            borrow_amt = user.getString("borrow_amt");
            interest_amt_L = user.getString("interest_amt_L");
            interest_amt_B = user.getString("interest_amt_B");
            return new User(username,invest_amt,lend_amt,borrow_amt,interest_amt_L,interest_amt_B);
        }catch (JSONException e)
        {
            Log.e(LOG_TAG, "Problem parsing the earthquake JSON results", e);

        }
        return null;
    }

    private static URL createUrl(String stringUrl) {
        URL url = null;
        try {
            url = new URL(stringUrl);
        } catch (MalformedURLException e) {
            Log.e(LOG_TAG, "Error with creating URL ", e);
        }
        return url;
    }

    private static String makeHttpRequest(URL url,String method,String payload,String acess_token)throws IOException
    {
        String jsonResponse = null;
        // If the URL is null, then return early.
        if (url == null) {
            return jsonResponse;
        }
        HttpURLConnection urlConnection = null;
        InputStream inputStream = null;

        try {
            //preliminary header file configurations
            urlConnection = (HttpURLConnection) url.openConnection();
            urlConnection.setRequestMethod(method);
            urlConnection.setReadTimeout(2000 /* milliseconds */);
            urlConnection.setConnectTimeout(2500 /* milliseconds */);
            if (acess_token != null)
            {
                urlConnection.setRequestProperty("Authorization","Bearer "+acess_token);
            }

            if(payload != null)
            {
                urlConnection.setRequestProperty("Content-Type", "application/json; charset=UTF-8");
                //for writing the post request
                OutputStreamWriter writer = new OutputStreamWriter(urlConnection.getOutputStream(), "UTF-8");
                writer.write(payload);
                writer.close();
            }

            // If the request was successful (response code 200),
            // then read the input stream and parse the response.
            if (urlConnection.getResponseCode() == 200) {
                inputStream = urlConnection.getInputStream();
                jsonResponse = readFromStream(inputStream);
            }
            else {
                Log.e(LOG_TAG, "Error response code: " + urlConnection.getResponseCode());
            }
            if (inputStream != null) {
                inputStream.close();
            }
        } catch (IOException e) {
            Log.e(LOG_TAG, "Problem retrieving the earthquake JSON results.", e);
        } finally {
            if (urlConnection != null) {
                urlConnection.disconnect();
            }
        }
        return jsonResponse;
    }

    private static String readFromStream(InputStream inputStream) throws IOException {
        StringBuilder output = new StringBuilder();
        if (inputStream != null) {
            InputStreamReader inputStreamReader = new InputStreamReader(inputStream, Charset.forName("UTF-8"));
            BufferedReader reader = new BufferedReader(inputStreamReader);
            String line = reader.readLine();
            while (line != null) {
                output.append(line);
                line = reader.readLine();
            }
        }
        return output.toString();
    }


}
