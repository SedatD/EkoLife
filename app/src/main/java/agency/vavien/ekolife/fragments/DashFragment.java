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
import android.widget.ImageButton;
import android.widget.TextView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import agency.vavien.ekolife.HaberDetayActivity;
import agency.vavien.ekolife.MainActivity;
import agency.vavien.ekolife.R;
import agency.vavien.ekolife.adapters.DashHaberAdapter;
import agency.vavien.ekolife.pojo_classes.DashHaberPojo;

import static agency.vavien.ekolife.MainActivity.selectNavMenu;

/**
 * Created by SD on 18.12.2017.
 * dilmacsedat@gmail.com
 * :)
 */

public class DashFragment extends Fragment implements View.OnClickListener {
    private static String LOG_TAG = "RecyclerViewActivity";
    private RecyclerView.Adapter mAdapter;
    private RecyclerView.LayoutManager mLayoutManager;
    private JSONObject jsonObject;
    private String userId, autoId;
    private RecyclerView mRecyclerView;
    private ImageButton imageButton_oneri, imageButton_katilanlar, imageButton_doganlar;
    private ImageButton imageButton_etkinlikler, imageButton_haberler, imageButton_yemek, imageButton_servis;
    private TextView textView_doganlar, textView_katilanlar, textView_dash;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.layout_dash, container, false);

        mRecyclerView = rootView.findViewById(R.id.recyclerView_dash);

        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(getActivity().getApplicationContext());
        userId = preferences.getString("userId", null);
        autoId = preferences.getString("autoId", null);
        String jsonObjectString = preferences.getString("jsonObject", null);

        // findViewById
        imageButton_oneri = rootView.findViewById(R.id.imageButton_oneri);
        imageButton_katilanlar = rootView.findViewById(R.id.imageButton_katilanlar);
        imageButton_doganlar = rootView.findViewById(R.id.imageButton_doganlar);

        imageButton_etkinlikler = rootView.findViewById(R.id.imageButton_etkinlikler);
        imageButton_haberler = rootView.findViewById(R.id.imageButton_haberler);
        imageButton_yemek = rootView.findViewById(R.id.imageButton_yemek);
        imageButton_servis = rootView.findViewById(R.id.imageButton_servis);

        textView_katilanlar = rootView.findViewById(R.id.textView_katilanlar);
        textView_doganlar = rootView.findViewById(R.id.textView_doganlar);
        textView_dash = rootView.findViewById(R.id.textView_dash);

        // onClick
        imageButton_oneri.setOnClickListener(this);
        imageButton_katilanlar.setOnClickListener(this);
        imageButton_doganlar.setOnClickListener(this);

        imageButton_etkinlikler.setOnClickListener(this);
        imageButton_haberler.setOnClickListener(this);
        imageButton_yemek.setOnClickListener(this);
        imageButton_servis.setOnClickListener(this);

        if (jsonObjectString != null) {
            try {
                jsonObject = new JSONObject(jsonObjectString);
                textView_katilanlar.setText(String.valueOf(jsonObject.getInt("joiners")));
                textView_doganlar.setText(String.valueOf(jsonObject.getInt("birthdays")));
                recycInit();
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }

        /*Bundle bundle = this.getArguments();
        try {
            jsonObject = new JSONObject(bundle.getString("jsonObject"));
            textView_katilanlar.setText(String.valueOf(jsonObject.getInt("joiners")));
            textView_doganlar.setText(String.valueOf(jsonObject.getInt("birthdays")));
            recycInit();
        } catch (JSONException e) {
            jsonObject = null;
            e.printStackTrace();
        }*/

        return rootView;
    }

    private void recycInit() {
        mRecyclerView.setHasFixedSize(true);
        mLayoutManager = new LinearLayoutManager(getActivity().getApplicationContext()) {
            @Override
            public boolean canScrollVertically() {
                return false;
            }
        };
        mRecyclerView.setLayoutManager(mLayoutManager);
        mAdapter = new DashHaberAdapter(getDataSet());
        mRecyclerView.setAdapter(mAdapter);
        RecyclerView.ItemDecoration itemDecoration = new DividerItemDecoration(getActivity().getApplicationContext(), LinearLayoutManager.VERTICAL);
        mRecyclerView.addItemDecoration(itemDecoration);

        ((DashHaberAdapter) mAdapter).setOnItemClickListener(new DashHaberAdapter.MyClickListener() {
            @Override
            public void onItemClick(int position, View v) {
                Intent intent = new Intent(getActivity(), HaberDetayActivity.class);
                try {
                    JSONObject jo = (JSONObject) jsonObject.getJSONArray("lastnews").get(position);
                    intent.putExtra("InHaberId", jo.getInt("InHaberId"));
                    startActivity(intent);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        });
    }

    private ArrayList<DashHaberPojo> getDataSet() {
        ArrayList results = new ArrayList<DashHaberPojo>();
        try {
            JSONArray jsonArray = jsonObject.getJSONArray("lastnews");
            if (jsonArray.length() == 0)
                textView_dash.setVisibility(View.VISIBLE);
            for (int i = 0; i < jsonArray.length(); i++) {
                JSONObject jsonObject1 = (JSONObject) jsonArray.get(i);
                int newsId = jsonObject1.getInt("InHaberId");
                String newsTitle = jsonObject1.getString("StHaberBasligi");
                String newsSummary = jsonObject1.getString("StHaberSumm");
                String newsDate = jsonObject1.getString("DtInsertedDate");

                DashHaberPojo obj = new DashHaberPojo(newsTitle, newsSummary, newsId);
                results.add(obj);
            }
        } catch (Exception e) {
            Log.wtf("DashFrag", e);
        }
        return results;
    }

    @Override
    public void onResume() {
        super.onResume();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.imageButton_oneri:
                OneriFragment oneriFragment = new OneriFragment();
                getActivity().getSupportFragmentManager().beginTransaction()
                        .replace(R.id.frame, oneriFragment, "katilan")
                        .addToBackStack(null)
                        .commit();
                MainActivity.navItemIndex = 6;
                selectNavMenu();
                break;
            case R.id.imageButton_katilanlar:
                KatilanlarFragment katilanlarFragment = new KatilanlarFragment();
                getActivity().getSupportFragmentManager().beginTransaction()
                        .replace(R.id.frame, katilanlarFragment, "katilan")
                        .addToBackStack(null)
                        .commit();
                MainActivity.navItemIndex = 5;
                selectNavMenu();
                break;
            case R.id.imageButton_doganlar:
                DoganlarFragment doganlarFragment = new DoganlarFragment();
                getActivity().getSupportFragmentManager().beginTransaction()
                        .replace(R.id.frame, doganlarFragment, "dogan")
                        .addToBackStack(null)
                        .commit();
                MainActivity.navItemIndex = 1;
                selectNavMenu();
                break;
            case R.id.imageButton_etkinlikler:
                EtkinlikFragment etkinlikFragment = new EtkinlikFragment();
                getActivity().getSupportFragmentManager().beginTransaction()
                        .replace(R.id.frame, etkinlikFragment, "etkinlik")
                        .addToBackStack(null)
                        .commit();
                MainActivity.navItemIndex = 2;
                selectNavMenu();
                break;
            case R.id.imageButton_haberler:
                HaberlerFragment haberlerFragmentt = new HaberlerFragment();
                getActivity().getSupportFragmentManager().beginTransaction()
                        .replace(R.id.frame, haberlerFragmentt, "haber")
                        .addToBackStack(null)
                        .commit();
                MainActivity.navItemIndex = 4;
                selectNavMenu();
                break;
            case R.id.imageButton_yemek:
                YemekFragment yemekFragment = new YemekFragment();
                getActivity().getSupportFragmentManager().beginTransaction()
                        .replace(R.id.frame, yemekFragment, "yemek")
                        .addToBackStack(null)
                        .commit();
                MainActivity.navItemIndex = 9;
                selectNavMenu();
                break;
            case R.id.imageButton_servis:
                ServisFragment servisFragment = new ServisFragment();
                getActivity().getSupportFragmentManager().beginTransaction()
                        .replace(R.id.frame, servisFragment, "servis")
                        .addToBackStack(null)
                        .commit();
                MainActivity.navItemIndex = 8;
                selectNavMenu();
                break;
            default:
                break;
        }
    }

}

