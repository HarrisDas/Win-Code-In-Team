package com.probattle.iba_task.Fragment;


import android.app.ProgressDialog;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.jjoe64.graphview.GraphView;
import com.jjoe64.graphview.series.DataPoint;
import com.jjoe64.graphview.series.LineGraphSeries;
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
public class GraphFragment extends Fragment {


    ProgressDialog dialog;
    String url = "https://api.coinmarketcap.com/v1/ticker/?limit=";
    int limit = 10;
    List<CryptoModel> list = new ArrayList<CryptoModel>();
    List<String> currencies1 = new ArrayList<>();
    Spinner spinner1;
    String c1 = "";
    int count = 0;
    GraphView graph;
    LineGraphSeries<DataPoint> series;

    public GraphFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment

        View view = inflater.inflate(R.layout.fragment_graph, container, false);
        dialog = new ProgressDialog(getActivity());
        dialog.setCancelable(false);
        spinner1 = (Spinner) view.findViewById(R.id.spinner_crypto_currency1);

        graph = (GraphView) view.findViewById(R.id.graph);
//        ArrayList<DataPoint> dataPoints =new ArrayList<>();
//        dataPoints.add(new DataPoint(0,1));
//                {
//                new DataPoint(0, 1),
//                new DataPoint(1, 5),
//                new DataPoint(2, 3),
//                new DataPoint(3, 2),
//                new DataPoint(4, 6)
//        };

        series = new LineGraphSeries<DataPoint>(new DataPoint[]{
//                new DataPoint(0, 1),
//                new DataPoint(1, 5),
//                new DataPoint(2, 3),
//                new DataPoint(3, 2),
//                new DataPoint(4, 6)
        });
//        series.appendData(new DataPoint(5,1),true,100);
////        series.appendData(dataPoints,true,100);
//
////        series.;
        graph.addSeries(series);
//
//        series.appendData(new DataPoint(6,10),true,100);
//
//        series.appendData(new DataPoint(7,7),true,100);
//
//        series.appendData(new DataPoint(8,1),true,100);
        loadJson();

        return view;
    }

    public void loadJson() {


        dialog.show();
        // Initialize a new StringRequest
        StringRequest stringRequest = new StringRequest(
                Request.Method.GET,
                url + limit,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        // Do something with response string

                        dialog.dismiss();
                        try {
                            JSONArray jsonArray = new JSONArray(response);

                            for (int i = 0; i < jsonArray.length(); i++) {
                                JSONObject obj = jsonArray.getJSONObject(i);

                                String id = obj.getString("id");

                                Log.e("name", id);
                                String name = obj.getString("name");
                                String price_usd = obj.getString("price_usd");
                                String price_btc = obj.getString("price_btc");
                                currencies1.add(name);
                                CryptoModel model = new CryptoModel();
                                model.setId(id);
                                model.setName(name);
                                model.setPrice_usd(price_usd);
                                model.setPrice_btc(price_btc);
//                                boolean aBoolean = pref.getBoolean(id, false);


//                                if (aBoolean){
//                                    model.setLike(true);
//                                }else{
//                                    model.setLike(false);
//                                }

                                list.add(model);


                            }

                            ArrayAdapter<String> adapter = new ArrayAdapter<String>(getActivity(),
                                    android.R.layout.simple_spinner_item, currencies1);
                            spinner1.setAdapter(adapter);

                            spinner1.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                                @Override
                                public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                                    //  double price= Double.parseDouble(list.get(position).getPrice_usd());


                                    c1 = list.get(position).getPrice_usd();
                                    getCustomCrypto(list.get(position).getId());

                                }

                                @Override
                                public void onNothingSelected(AdapterView<?> parent) {

                                }
                            });

//                            adapter.notifyDataSetChanged();

                        } catch (JSONException e) {
                            e.printStackTrace();
                        }

                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        dialog.dismiss();
                        Toast.makeText(getActivity(), "unable to fetch list", Toast.LENGTH_SHORT).show();

                    }

                }
        );

        // Add StringRequest to the RequestQueue
        Volley.newRequestQueue(getActivity()).add(stringRequest);
    }

    private void getCustomCrypto(final String CryptoId) {
//        dialog.show();
        // Initialize a new StringRequest

//        if (count > 10) {
////            count = 0;
//            return;
//        }
        StringRequest stringRequest = new StringRequest(
                Request.Method.GET,
                url + limit,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        // Do something with response string

//                        dialog.dismiss();
                        try {
                            JSONArray jsonArray = new JSONArray(response);

                            for (int i = 0; i < jsonArray.length(); i++) {
                                JSONObject obj = jsonArray.getJSONObject(i);

                                String id = obj.getString("id");
                                if (id.equals(CryptoId)) {

                                    Log.e("name", id);
                                    String name = obj.getString("name");
                                    String price_usd = obj.getString("price_usd");
                                    String price_btc = obj.getString("price_btc");
//                                    currencies1.add(name);
                                    CryptoModel model = new CryptoModel();
                                    model.setId(id);
                                    model.setName(name);
                                    model.setPrice_usd(price_usd);
                                    model.setPrice_btc(price_btc);
                                    count++;
                                    if (price_usd != null) {
                                        System.out.println("value :" + Double.valueOf(price_usd));

                                        Double val = Double.valueOf(price_usd);
//                                        int i1 = Integer.parseInt(String.valueOf());


                                        series.appendData(new DataPoint(++count, val%1000), true, 15);

                                    }

                                    getCustomCrypto(id);


                                }

//                                boolean aBoolean = pref.getBoolean(id, false);


//                                if (aBoolean){
//                                    model.setLike(true);
//                                }else{
//                                    model.setLike(false);
//                                }

//                                list.add(model);


                            }
//                            adapter.notifyDataSetChanged();

                        } catch (JSONException e) {
                            e.printStackTrace();
                        }

                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        dialog.dismiss();
                        Toast.makeText(getActivity(), "unable to fetch list", Toast.LENGTH_SHORT).show();

                    }

                }
        );

        // Add StringRequest to the RequestQueue
        Volley.newRequestQueue(getActivity()).add(stringRequest);

        graph.addSeries(series);
    }

}
