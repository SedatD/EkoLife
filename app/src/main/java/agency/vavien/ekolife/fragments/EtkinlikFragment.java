package agency.vavien.ekolife.fragments;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.NetworkResponse;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.ServerError;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.HttpHeaderParser;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import agency.vavien.ekolife.R;
import agency.vavien.ekolife.adapters.EtkinliklerAdapter;
import agency.vavien.ekolife.pojo_classes.EtkinliklerPojo;

/**
 * Created by SD on 18.12.2017.
 * dilmacsedat@gmail.com
 * :)
 */

public class EtkinlikFragment extends Fragment {
    private RecyclerView mRecyclerView;
    private RecyclerView.Adapter mAdapter;
    private RecyclerView.LayoutManager mLayoutManager;
    private TextView textView_etkinlikler;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.layout_etkinlik, container, false);

        mRecyclerView = rootView.findViewById(R.id.my_recycler_view);
        mRecyclerView.setHasFixedSize(true);
        mLayoutManager = new LinearLayoutManager(getContext());
        mRecyclerView.setLayoutManager(mLayoutManager);
        //mAdapter = new EtkinliklerAdapter(getDataSet());
        //mRecyclerView.setAdapter(mAdapter);

        textView_etkinlikler = rootView.findViewById(R.id.textView_etkinlikler);

        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(getActivity().getApplicationContext());
        getRequest(preferences.getString("autoId", null));

        return rootView;
    }

    private ArrayList<EtkinliklerPojo> getDataSet() {
        ArrayList results = new ArrayList<EtkinliklerPojo>();
        for (int index = 0; index < 10; index++) {
            EtkinliklerPojo obj = new EtkinliklerPojo("Etkinlik " + index, "Kişisel Gelişim Semineri", "Konferans Salonu", "30.08.2016");
            results.add(index, obj);
        }
        return results;
    }

    private void getRequest(final String autoId) {
        RequestQueue queue = Volley.newRequestQueue(getActivity().getApplicationContext());
        StringRequest stringRequest = new StringRequest(Request.Method.GET, "https://ekolife.ekoccs.com/api/Etkinlik",
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        Log.wtf("EtkinlikFrag", "Response : " + response);
                        try {
                            JSONArray jsonArray = new JSONArray(response);

                            ArrayList results = new ArrayList<EtkinliklerPojo>();
                            EtkinliklerPojo obj;

                            for (int i = 0; i < jsonArray.length(); i++) {
                                JSONObject jsonObject = jsonArray.getJSONObject(i);

                                obj = new EtkinliklerPojo(
                                        jsonObject.getString("StEtkinlikBasligi"),
                                        jsonObject.getString("StEtkinlikOzet"),
                                        jsonObject.getString("StLokasyon"),
                                        jsonObject.getString("DtEtkinlikBaslangicTarihi"));

                                results.add(obj);
                            }

                            if (jsonArray.length() <1)
                                textView_etkinlikler.setVisibility(View.VISIBLE);

                            mAdapter = new EtkinliklerAdapter(results);
                            mRecyclerView.setAdapter(mAdapter);

                        } catch (JSONException e) {
                            e.printStackTrace();
                            textView_etkinlikler.setVisibility(View.VISIBLE);
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        NetworkResponse response = error.networkResponse;
                        if (error instanceof ServerError && response != null) {
                            try {
                                String res = new String(response.data, HttpHeaderParser.parseCharset(response.headers, "utf-8"));
                                Log.wtf("try", "res : " + res + " / response : " + response);
                                JSONObject obj = new JSONObject(res);
                                Log.wtf("try", "obj : " + obj);
                            } catch (UnsupportedEncodingException | JSONException e1) {
                                e1.printStackTrace();
                            }
                        }
                        Log.wtf("EtkinlikFrag", "onErrorResponse : " + error + " / volleyError.getMessage() : " + error.getMessage());

                        Toast.makeText(getActivity().getApplicationContext(), "Bir hata oluştu lütfen daha sonra tekrar deneyiniz", Toast.LENGTH_SHORT).show();
                        getActivity().getSupportFragmentManager().beginTransaction().replace(R.id.frame, new DashFragment()).commit();
                    }
                }) {
            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                Map<String, String> params = new HashMap<String, String>();
                params.put("Authorization", autoId);
                return params;
            }
        };
        queue.add(stringRequest);
    }

}

