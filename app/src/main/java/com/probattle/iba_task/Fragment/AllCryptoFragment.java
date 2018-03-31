package com.probattle.iba_task.Fragment;


import android.app.ProgressDialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.probattle.iba_task.Adapters.CryptoListAdapter;
import com.probattle.iba_task.CryptoModel;
import com.probattle.iba_task.DashBoardActivity;
import com.probattle.iba_task.R;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 */
public class AllCryptoFragment extends Fragment {

    String url = "https://api.coinmarketcap.com/v1/ticker/?limit=";
    int limit = 100;
    List<CryptoModel> list = new ArrayList<CryptoModel>();
    CryptoListAdapter adapter;
    RecyclerView recyclerView;
    ProgressDialog dialog;
    SharedPreferences pref;

    public AllCryptoFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_all_crypto, container, false);
        init(view);
        pref = getActivity().getApplicationContext().getSharedPreferences("MyPref", 0);
        loadJson();



        DashBoardActivity.etSearch.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
               // Log.e(TAG , charSequence.toString());
                adapter.getFilter().filter(charSequence);
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });
        return view;

    }

    private void init(View view) {

        dialog = new ProgressDialog(getActivity());
        dialog.setMessage("Loading currencies");
        dialog.setCancelable(false);

        recyclerView = view.findViewById(R.id.all_crypto_recycler);
        adapter = new CryptoListAdapter(list, getActivity(), new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
//                Toast.makeText(getActivity(), ""+list.get(i).getName(), Toast.LENGTH_SHORT).show();

                CryptoModel cryptoModel = list.get(i);
                SharedPreferences.Editor editor = pref.edit();
                editor.putBoolean(cryptoModel.getId(), true);
                editor.commit();
                boolean aBoolean = pref.getBoolean(cryptoModel.getId(), false);
                    Toast.makeText(getActivity(), "" + list.get(i).getName(), Toast.LENGTH_SHORT).show();

//                SharedPreferences sharedPref = getActivity().getPreferences(Context.MODE_PRIVATE);

            }
        });
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        recyclerView.setAdapter(adapter);
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

                                CryptoModel model = new CryptoModel();
                                model.setId(id);
                                model.setName(name);
                                model.setPrice_usd(price_usd);
                                model.setPrice_btc(price_btc);
                                boolean aBoolean = pref.getBoolean(id, false);


                                if (aBoolean){
                                    model.setLike(true);
                                }else{
                                    model.setLike(false);
                                }

                                list.add(model);


                            }
                            adapter.notifyDataSetChanged();

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
}
