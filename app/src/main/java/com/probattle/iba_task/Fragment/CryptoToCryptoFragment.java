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
import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 */
public class CryptoToCryptoFragment extends Fragment {
    Activity activity;
    String url = "https://api.coinmarketcap.com/v1/ticker/?limit=";
    int limit = 100;
    List<CryptoModel> list = new ArrayList<CryptoModel>();
    Spinner spinner1;
    Spinner spinner2;
    TextView tv_local;
    Button btn_convert;
    List<String> currencies1 = new ArrayList<>();
    List<String> currencies2 = new ArrayList<>();

    String c1 = "";
    String c2 = "";


    public CryptoToCryptoFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_crypto_to_crypto, container, false);

        init(view);

        loadJson();
        return view;
    }

    private void init(View view) {
        activity = getActivity();
        spinner1 = (Spinner) view.findViewById(R.id.spinner_crypto_currency1);
        spinner2 = (Spinner) view.findViewById(R.id.spinner_crypto_currency2);


        tv_local = (TextView) view.findViewById(R.id.tv_local);
        btn_convert = (Button) view.findViewById(R.id.btn_convert);

        btn_convert.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                double v1 = Double.parseDouble(c1);
                double v2 = Double.parseDouble(c2);
                double inDollar = 1 / (double) v1;
                double ans = inDollar * v2;
                tv_local.setText(ans + "");

            }
        });


    }

    public void loadJson() {


        // Initialize a new StringRequest
        final ProgressDialog dialog = new ProgressDialog(getActivity());
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
                                currencies1.add(name);
                                currencies2.add(name);
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
                                    android.R.layout.simple_spinner_item, currencies1);
                            spinner1.setAdapter(adapter);

                            spinner1.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                                @Override
                                public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                                    //  double price= Double.parseDouble(list.get(position).getPrice_usd());

                                    c1 = list.get(position).getPrice_usd();

                                }

                                @Override
                                public void onNothingSelected(AdapterView<?> parent) {

                                }
                            });

                            ArrayAdapter<String> adapter2 = new ArrayAdapter<String>(activity,
                                    android.R.layout.simple_spinner_item, currencies2);
                            spinner2.setAdapter(adapter);

                            spinner2.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                                @Override
                                public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                                    // double price= Double.parseDouble(list.get(position).getPrice_usd());

                                    c2 = list.get(position).getPrice_usd();

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