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

public class FragmentServisServisler extends Fragment {
    public int last_position = -1;
    private ExpandableListView expandableListView_servis_servisler;
    private ExpAdapterServisler ExpAdapter;

    public FragmentServisServisler() {
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_servis_servisler, container, false);

        expandableListView_servis_servisler = rootView.findViewById(R.id.expandableListView_servis_servisler);
        expandableListView_servis_servisler.setClickable(true);

        expandableListView_servis_servisler.setOnChildClickListener(new ExpandableListView.OnChildClickListener() {
            @Override
            public boolean onChildClick(ExpandableListView parent, View v, int groupPosition, int childPosition, long id) {
                //String child_name = (String) ExpAdapter.getChild(groupPosition, childPosition);
                //Toast.makeText(getActivity(), child_name, Toast.LENGTH_SHORT).show();
                return false;
            }
        });

        expandableListView_servis_servisler.setOnGroupExpandListener(new ExpandableListView.OnGroupExpandListener() {
            @Override
            public void onGroupExpand(int groupPosition) {
                /*if (last_position != -1 && last_position != groupPosition)
                    expandableListView_servis_servisler.collapseGroup(last_position);*/
                last_position = groupPosition;
            }
        });

        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(getContext());
        getRequest(preferences.getString("autoId", null));

        return rootView;
    }

    private void getRequest(final String autoId) {
        RequestQueue queue = Volley.newRequestQueue(getContext());
        StringRequest stringRequest = new StringRequest(
                Request.Method.GET,
                "https://ekolife.ekoccs.com/api/General/GetServisListesi",
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        Log.wtf("FragServisServisler", "Response : " + response);
                        try {
                            ArrayList<ExpGroupServisler> list = new ArrayList<ExpGroupServisler>();
                            ArrayList<ExpChildServisler> ch_list = null;

                            JSONArray jsonArray = new JSONArray(response);
                            for (int i = 0; i < jsonArray.length(); i++) {
                                JSONObject jsonObject = jsonArray.getJSONObject(i);
                                ExpGroupServisler expGroupServisler = new ExpGroupServisler();
                                expGroupServisler.setName(jsonObject.getString("servis_saati"));
                                ch_list = new ArrayList<ExpChildServisler>();
                                JSONArray jsonArray1 = jsonObject.getJSONArray("servisler");
                                for (int j = 0; j < jsonArray1.length(); j++) {
                                    JSONObject jsonObject1 = jsonArray1.getJSONObject(j);
                                    ExpChildServisler expChildServisler = new ExpChildServisler();
                                    expChildServisler.setStServis(jsonObject1.getString("StServis"));
                                    expChildServisler.setStGuzergahLink(jsonObject1.getString("StGuzergahLink"));
                                    expChildServisler.setStSurucuAdi(jsonObject1.getString("StSurucuAdi"));
                                    expChildServisler.setStSurucuGSM(jsonObject1.getString("StSurucuGSM"));
                                    expChildServisler.setStServisPlaka(jsonObject1.getString("StServisPlaka"));

                                    ch_list.add(expChildServisler);
                                }
                                expGroupServisler.setItems(ch_list);
                                list.add(expGroupServisler);
                            }

                            /*//JSONArray jsonArray = jsonObject.getJSONArray("08:00 - 17:00");

                            ExpGroupServisler expGroupServisler = new ExpGroupServisler();
                            expGroupServisler.setName("08:00 - 17:00");

                            ch_list = new ArrayList<ExpChildServisler>();
                            for (int i = 0; i < jsonArray.length(); i++) {
                                JSONObject jsonObject1 = (JSONObject) jsonArray.get(i);

                                ExpChildServisler expChildServisler = new ExpChildServisler();
                                expChildServisler.setStServis(jsonObject1.getString("StServis"));
                                expChildServisler.setStGuzergahLink(jsonObject1.getString("StGuzergahLink"));
                                expChildServisler.setStSurucuAdi(jsonObject1.getString("StSurucuAdi"));
                                expChildServisler.setStSurucuGSM(jsonObject1.getString("StSurucuGSM"));
                                expChildServisler.setStServisPlaka(jsonObject1.getString("StServisPlaka"));

                                ch_list.add(expChildServisler);
                            }
                            expGroupServisler.setItems(ch_list);
                            list.add(expGroupServisler);*/

                            //ExpListItems = SetStandardGroups();
                            ExpAdapter = new ExpAdapterServisler(getContext(), list);
                            expandableListView_servis_servisler.setAdapter(ExpAdapter);

                        } catch (JSONException e) {
                            Log.wtf("FragServisServisler", "catch : " + e);
                            e.printStackTrace();
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Log.wtf("FragServisServisler", "Error : " + error);
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
