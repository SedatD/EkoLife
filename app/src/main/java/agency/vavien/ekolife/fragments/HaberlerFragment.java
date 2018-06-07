package agency.vavien.ekolife.fragments;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v4.app.Fragment;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import agency.vavien.ekolife.HaberDetayActivity;
import agency.vavien.ekolife.R;
import agency.vavien.ekolife.adapters.HaberlerAdapter;
import agency.vavien.ekolife.pojo_classes.HaberlerPojo;

/**
 * Created by SD on 18.12.2017.
 * dilmacsedat@gmail.com
 * :)
 */

public class HaberlerFragment extends Fragment {
    private JSONArray jsonArray;
    private RecyclerView mRecyclerView;
    private RecyclerView.Adapter mAdapter;
    private RecyclerView.LayoutManager mLayoutManager;
    private TextView textView_haberler;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.layout_haberler, container, false);

        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(getActivity().getApplicationContext());
        getRequest(preferences.getString("autoId", null));

        mRecyclerView = rootView.findViewById(R.id.recyclerView_haberler);
        textView_haberler = rootView.findViewById(R.id.textView_haberler);

        return rootView;
    }

    private void recycInit() {
        mRecyclerView.setHasFixedSize(true);
        mLayoutManager = new LinearLayoutManager(getContext());
        mRecyclerView.setLayoutManager(mLayoutManager);
        mAdapter = new HaberlerAdapter(getActivity().getApplicationContext(), getDataSet());
        mRecyclerView.setAdapter(mAdapter);
        RecyclerView.ItemDecoration itemDecoration = new DividerItemDecoration(getActivity().getApplicationContext(), LinearLayoutManager.VERTICAL);
        mRecyclerView.addItemDecoration(itemDecoration);

        ((HaberlerAdapter) mAdapter).setOnItemClickListener(new HaberlerAdapter.MyClickListener() {
            @Override
            public void onItemClick(int position, View v) {
                Intent intent = new Intent(getActivity(), HaberDetayActivity.class);
                try {
                    intent.putExtra("InHaberId", jsonArray.getJSONObject(position).getInt("InHaberId"));
                    startActivity(intent);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        });
    }

    private ArrayList<HaberlerPojo> getDataSet() {
        final ArrayList results = new ArrayList<HaberlerPojo>();
        try {
            for (int i = 0; i < jsonArray.length(); i++) {
                JSONObject jsonObject1 = (JSONObject) jsonArray.get(i);
                final String newsTitle = jsonObject1.getString("StHaberBasligi");
                final String newsDate = jsonObject1.getString("DtInsertedDate");
                final String newsPhoto = jsonObject1.getString("StResimUrl");

                HaberlerPojo obj = new HaberlerPojo(newsPhoto, newsTitle, newsDate);
                results.add(obj);
            }
        } catch (Exception e) {
            Log.wtf("HaberlerFrag", e);
        }
        return results;
    }

    private void getRequest(final String autoId) {
        RequestQueue queue = Volley.newRequestQueue(getContext());
        StringRequest stringRequest = new StringRequest(Request.Method.GET, "https://ekolife.ekoccs.com/api/News/GetNewsTitles", new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Log.wtf("HaberlerFrag", "Response : " + response);
                try {
                    jsonArray = new JSONArray(response);
                    if (jsonArray.length() != 0)
                        recycInit();
                    else
                        textView_haberler.setVisibility(View.VISIBLE);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.wtf("HaberlerFrag", "Error : " + error);
                textView_haberler.setVisibility(View.VISIBLE);
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

