package agency.vavien.ekolife.fragments_katilanlar;

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

import agency.vavien.ekolife.R;
import agency.vavien.ekolife.adapters.FragmentKatilanlarAdapter;
import agency.vavien.ekolife.pojo_classes.KatilanlarPojo;

/**
 * Created by SD on 18.12.2017.
 * dilmacsedat@gmail.com
 * :)
 */

public class FragmentKatilanlarOne extends Fragment {
    private RecyclerView mRecyclerView;
    private RecyclerView.Adapter mAdapter;
    private RecyclerView.LayoutManager mLayoutManager;
    private JSONArray jsonArray;
    private TextView textView_nope;

    public FragmentKatilanlarOne() {
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_katilanlar_one, container, false);

        mRecyclerView = (RecyclerView) rootView.findViewById(R.id.recyclerView_fragment_katilanlar);
        textView_nope = (TextView) rootView.findViewById(R.id.textView_nope);

        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(getActivity().getApplicationContext());
        //userId = preferences.getString("userId", null);
        //autoId = preferences.getString("autoId", null);

        getRequest(preferences.getString("autoId", null));

        return rootView;
    }

    private ArrayList<KatilanlarPojo> getDataSet() {
        ArrayList results = new ArrayList<KatilanlarPojo>();
        try {
            for (int i = 0; i < jsonArray.length(); i++) {
                JSONObject jsonObject1 = (JSONObject) jsonArray.get(i);

                int katilanId = jsonObject1.getInt("InPersonelID");
                String katilanName = jsonObject1.getString("StFullName");
                String katilanMail = jsonObject1.getString("stFrmeMail");
                String katilanTel = jsonObject1.getString("StPhoneMobile");
                String katilanPhoto = jsonObject1.getString("StProfilePhoto");
                String katilanDep = jsonObject1.getString("StProjectName");

                KatilanlarPojo obj = new KatilanlarPojo(katilanId,katilanPhoto, katilanDep, katilanName, katilanMail);
                results.add(obj);
            }
        } catch (Exception e) {
            Log.wtf("asdasd", e);
        }
        return results;
    }

    private void recycInit() {
        mRecyclerView.setHasFixedSize(true);
        mLayoutManager = new LinearLayoutManager(getContext());
        mRecyclerView.setLayoutManager(mLayoutManager);
        mAdapter = new FragmentKatilanlarAdapter(getActivity().getApplicationContext(), getDataSet());
        mRecyclerView.setAdapter(mAdapter);
        RecyclerView.ItemDecoration itemDecoration = new DividerItemDecoration(getContext(), LinearLayoutManager.VERTICAL);
        mRecyclerView.addItemDecoration(itemDecoration);

        /*((FragmentKatilanlarAdapter) mAdapter).setOnItemClickListener(new FragmentKatilanlarAdapter.MyClickListener() {
            @Override
            public void onItemClick(int position, View v) {
                Log.wtf("FragKatilanlarOne", "ses");
            }
        });*/
    }

    private void getRequest(final String autoId) {
        RequestQueue queue = Volley.newRequestQueue(getActivity().getApplicationContext());
        StringRequest stringRequest = new StringRequest(Request.Method.GET, "https://ekolife.vodasoft.com.tr/api/General/GetJoins?req=1", new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Log.wtf("HttpClient", "success! response 1: " + response);
                try {
                    jsonArray = new JSONArray(response);
                    if (jsonArray.length() != 0)
                        recycInit();
                    else
                        textView_nope.setVisibility(View.VISIBLE);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.wtf("HttpClient", "error: " + error);
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