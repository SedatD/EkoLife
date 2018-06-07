package agency.vavien.ekolife.fragments_servis;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ExpandableListView;
import android.widget.TextView;
import android.widget.Toast;

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
import agency.vavien.ekolife.adapters.ExpAdapterServisler;
import agency.vavien.ekolife.pojo_classes.ExpChildServisler;
import agency.vavien.ekolife.pojo_classes.ExpGroupServisler;

/**
 * Created by SD on 18.12.2017.
 * dilmacsedat@gmail.com
 * :)
 */

public class FragmentServisSiz extends Fragment {
    public int last_position = -1;
    private TextView textView_servis_siz_adres;
    private ExpandableListView expandableListView_servis_siz;
    private ExpAdapterServisler ExpAdapter;

    public FragmentServisSiz() {}

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_servis_siz, container, false);

        textView_servis_siz_adres = rootView.findViewById(R.id.textView_servis_siz_adres);
        expandableListView_servis_siz = rootView.findViewById(R.id.expandableListView_servis_siz);
        expandableListView_servis_siz.setClickable(true);


        expandableListView_servis_siz.setOnChildClickListener(new ExpandableListView.OnChildClickListener() {
            @Override
            public boolean onChildClick(ExpandableListView parent, View v, int groupPosition, int childPosition, long id) {
                String child_name = (String) ExpAdapter.getChild(groupPosition, childPosition);
                Toast.makeText(getActivity(), child_name, Toast.LENGTH_SHORT).show();
                return false;
            }
        });


        expandableListView_servis_siz.setOnGroupExpandListener(new ExpandableListView.OnGroupExpandListener() {
            @Override
            public void onGroupExpand(int groupPosition) {
                /*if (last_position != -1 && last_position != groupPosition)
                    expandableListView_servis_servisler.collapseGroup(last_position);*/
                last_position = groupPosition;
            }
        });

        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(getContext());
        getRequest(preferences.getString("autoId", null), preferences.getString("userId", null));

        return rootView;
    }

    private void getRequest(final String autoId, String userId) {
        RequestQueue queue = Volley.newRequestQueue(getContext());
        StringRequest stringRequest = new StringRequest(
                Request.Method.GET,
                "https://ekolife.ekoccs.com/api/User/GetTransportation?userId=" + userId,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        Log.wtf("FragServisSiz", "Response : " + response);
                        try {
                            JSONObject jsonObject = new JSONObject(response);

                            String adres = jsonObject.getString("StSokak")
                                    + " " + jsonObject.getString("StCadde")
                                    + " " + jsonObject.getString("stMahalle")
                                    + " " + jsonObject.getString("StIlce");

                            textView_servis_siz_adres.setText(adres);

                            JSONArray jsonArray = jsonObject.getJSONArray("Servisler");

                            ArrayList<ExpGroupServisler> list = new ArrayList<ExpGroupServisler>();
                            ArrayList<ExpChildServisler> ch_list;

                            for (int i = 0; i < jsonArray.length(); i++) {
                                JSONObject jsonObject1 = jsonArray.getJSONObject(i);

                                ExpGroupServisler expGroupServisler = new ExpGroupServisler();
                                expGroupServisler.setName(jsonObject1.getString("StSaat"));

                                ch_list = new ArrayList<ExpChildServisler>();

                                ExpChildServisler expChildServisler = new ExpChildServisler();
                                expChildServisler.setStServis(jsonObject1.getString("StServis"));
                                expChildServisler.setStGuzergahLink(jsonObject1.getString("StGuzergahLink"));
                                expChildServisler.setStSurucuAdi(jsonObject1.getString("StSurucuAdi"));
                                expChildServisler.setStSurucuGSM(jsonObject1.getString("StSurucuGSM"));
                                expChildServisler.setStServisPlaka(jsonObject1.getString("StServisPlaka"));

                                ch_list.add(expChildServisler);

                                expGroupServisler.setItems(ch_list);
                                list.add(expGroupServisler);

                            }

                            ExpAdapter = new ExpAdapterServisler(getContext(), list);
                            expandableListView_servis_siz.setAdapter(ExpAdapter);

                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Log.wtf("FragServisSiz", "Error : " + error);
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
