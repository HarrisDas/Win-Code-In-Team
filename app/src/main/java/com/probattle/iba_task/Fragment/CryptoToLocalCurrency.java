package com.probattle.iba_task.Fragment;


import android.app.Activity;
import android.app.ProgressDialog;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.TextView;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.probattle.iba_task.CryptoModel;
import com.probattle.iba_task.R;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 */
public class CryptoToLocalCurrency extends Fragment {

    Activity activity;
    String url="https://api.coinmarketcap.com/v1/ticker/?limit=";
    int limit=100;
    List<CryptoModel> list=new ArrayList<CryptoModel>();
    Spinner spinner;
    Spinner spinner_forex;
    TextView tv_local;
    List<String> currencies=new ArrayList<>();
    List<String> currencyForex=new ArrayList<>();
    List<String> currencyForexPrice=new ArrayList<>();
    String ratesLink="http://data.fixer.io/api/latest?access_key=6103746d27542605220b38a25ee50687";

    String value="";

    String c="" , selected_currency_name="";
    Button btn_convert;


    public CryptoToLocalCurrency() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
       View view=  inflater.inflate(R.layout.fragment_crypto_to_local_currency, container, false);
        activity=getActivity();
        spinner=(Spinner)view.findViewById(R.id.spinner_crypto_currency);
        tv_local=(TextView)view.findViewById(R.id.tv_local);
        spinner_forex=(Spinner)view.findViewById(R.id.spinner_currency);
        btn_convert=(Button)view.findViewById(R.id.btn_convert);



        btn_convert.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.e("currency_rate_euro" , value);
                double v1=Double.parseDouble(value)* 0.8130081;
                value=String.valueOf(v1);
                Log.e("currency_rate_dollar" , value);

                Log.e("crypto_rate" , c);

                double ans=Double.parseDouble(value)* Double.parseDouble(c);
                Log.e("ans" , ans+"");
                tv_local.setText(ans+" "+ selected_currency_name);
            }
        });




        loadJson();
        loadCurrencyRates();

        return view;
    }
    public void loadCurrencyRates()
    {

        // Initialize a new StringRequest
        final ProgressDialog dialog=new ProgressDialog(activity);
        dialog.setTitle("Loading");
        dialog.setMessage("Wait...");
        dialog.show();
        StringRequest stringRequest = new StringRequest(
                Request.Method.GET,
                ratesLink,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        // Do something with response string
                        try {

                            JSONObject obj=new JSONObject(response);
                            JSONObject rates=obj.getJSONObject("rates");



                            Iterator<String> iter = rates.keys();

                            while (iter.hasNext()) {

                                String key = iter.next();
                                currencyForex.add(key);
                                try {
                                    currencyForexPrice.add(String.valueOf(rates.get(key)));
                                } catch (JSONException e) {
                                    // Something went wrong!
                                }
                            }




                            ArrayAdapter<String> adapter = new ArrayAdapter<String>(activity,
                                    android.R.layout.simple_spinner_item,currencyForex);
                            spinner_forex.setAdapter(adapter);

                            spinner_forex.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                                @Override
                                public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

                                    value=currencyForexPrice.get(position);
                                    selected_currency_name=currencyForex.get(position);

                                }

                                @Override
                                public void onNothingSelected(AdapterView<?> parent) {

                                }
                            });

                            dialog.dismiss();

                        } catch (JSONException e) {

                            e.printStackTrace();

                            dialog.dismiss();
                        }


                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        dialog.dismiss();
                    }
                }
        );

        // Add StringRequest to the RequestQueue
        Volley.newRequestQueue(activity).add(stringRequest);
    }

    public void loadJson() {


        // Initialize a new StringRequest
        final ProgressDialog dialog = new ProgressDialog(activity);
        dialog.setTitle("Loading");
        dialog.setMessage("Wait...");
        dialog.show();
        StringRequest stringRequest = new StringRequest(
                Request.Method.GET,
                url + limit,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        // Do something with response string
                        try {
                            JSONArray jsonArray = new JSONArray(response);

                            for (int i = 0; i < jsonArray.length(); i++) {
                                JSONObject obj = jsonArray.getJSONObject(i);

                                String id = obj.getString("id");
                                Log.e("name", id);
                                String name = obj.getString("name");
                                currencies.add(name);
                                String price_usd = obj.getString("price_usd");
                                String price_btc = obj.getString("price_btc");

                                CryptoModel model = new CryptoModel();
                                model.setId(id);
                                model.setName(name);
                                model.setPrice_usd(price_usd);
                                model.setPrice_btc(price_btc);

                                list.add(model);

                            }
                            ArrayAdapter<String> adapter = new ArrayAdapter<String>(activity,
                                    android.R.layout.simple_spinner_item, currencies);
                            spinner.setAdapter(adapter);

                            spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                                @Override
                                public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                                    double price = Double.parseDouble(list.get(position).getPrice_usd());

                                    c = String.valueOf(price);

                                }

                                @Override
                                public void onNothingSelected(AdapterView<?> parent) {

                                }
                            });
                            dialog.dismiss();
                        } catch (JSONException e) {
                            e.printStackTrace();
                            dialog.dismiss();
                        }


                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        dialog.dismiss();
                    }
                }
        );

        // Add StringRequest to the RequestQueue
        Volley.newRequestQueue(activity).add(stringRequest);
    }
}
