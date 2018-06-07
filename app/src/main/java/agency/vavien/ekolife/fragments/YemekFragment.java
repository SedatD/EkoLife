package agency.vavien.ekolife.fragments;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.CalendarView;
import android.widget.ListView;

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
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import agency.vavien.ekolife.R;

/**
 * Created by SD on 18.12.2017.
 * dilmacsedat@gmail.com
 * :)
 */

public class YemekFragment extends Fragment {
    ListView listView_yemek;
    CalendarView calendarView_yemek;
    JSONObject jsonObject;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.layout_yemek, container, false);

        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(getActivity().getApplicationContext());
        //userId = preferences.getString("userId", null);
        //autoId = preferences.getString("autoId", null);
        getRequest(preferences.getString("autoId", null));

        listView_yemek = rootView.findViewById(R.id.listView_yemek);
        listView_yemek.setClickable(false);
        calendarView_yemek = rootView.findViewById(R.id.calendarView_yemek);
        calendarView_yemek.setOnDateChangeListener(new CalendarView.OnDateChangeListener() {
            @Override
            public void onSelectedDayChange(CalendarView calendarView, int year, int mounth, int day) {
                //Toast.makeText(getApplicationContext(), "Selected Date:\n" + "Day = " + day + "\n" + "Month = " + mounth + "\n" + "Year = " + year, Toast.LENGTH_SHORT).show();
                try {
                    if (jsonObject == null)
                        return;
                    JSONArray jsonArray = jsonObject.getJSONArray(String.valueOf(day));
                    final ArrayList<String> list = new ArrayList<String>();
                    for (int i = 0; i < jsonArray.length(); i++) {
                        list.add(String.valueOf(jsonArray.get(i)));
                    }
                    final StableArrayAdapter adapter = new StableArrayAdapter(getActivity().getApplicationContext(), R.layout.custom_list_item, list);
                    listView_yemek.setAdapter(adapter);
                } catch (JSONException e) {
                    final ArrayList<String> list = new ArrayList<String>();
                    list.add("Yemek listesi güncellenemedi");
                    final StableArrayAdapter adapter = new StableArrayAdapter(getActivity().getApplicationContext(), R.layout.custom_list_item, list);
                    listView_yemek.setAdapter(adapter);
                    e.printStackTrace();
                }
            }
        });

        // other programmatically css
        Calendar calendar = Calendar.getInstance();
        calendar.set(Calendar.DATE, calendar.getActualMaximum(Calendar.DATE));
        calendar.set(Calendar.HOUR_OF_DAY, 23);//not sure this is needed
        long endOfMonth = calendar.getTimeInMillis();

        //may need to reinitialize calendar, not sure
        calendar = Calendar.getInstance();
        calendar.set(Calendar.DATE, 1);
        calendar.set(Calendar.HOUR_OF_DAY, 0);
        long startOfMonth = calendar.getTimeInMillis();

        calendarView_yemek.setFirstDayOfWeek(2);
        calendarView_yemek.setShowWeekNumber(false);
        calendarView_yemek.setMaxDate(endOfMonth);
        calendarView_yemek.setMinDate(startOfMonth);

        // adapter
        /*listView_yemek.setClickable(false);
        String[] values = new String[]{"Tavuk Suyu Çorba", "Cızbız Köfte", "Yufka Kebabı",
                "Sade Pilav", "Peynirli Makarna", "Revani", "Muzlu Puding", "Meyve",
                "Salata"};
        final ArrayList<String> list = new ArrayList<String>();
        for (int i = 0; i < values.length; ++i) {
            list.add(values[i]);
        }
        //Collections.addAll(list, values);//ustteki forla aynı
        final StableArrayAdapter adapter = new StableArrayAdapter(getActivity().getApplicationContext(), R.layout.custom_list_item, list);
        listView_yemek.setAdapter(adapter);*/

        return rootView;
    }

    private void getRequest(final String autoId) {
        RequestQueue queue = Volley.newRequestQueue(getActivity().getApplicationContext());
        StringRequest stringRequest = new StringRequest(Request.Method.GET, "https://ekolife.ekoccs.com/api/General/GetMealMenus", new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Log.wtf("YemekFrag", "Response : " + response);
                try {
                    jsonObject = new JSONObject(response);
                    Calendar calendar = Calendar.getInstance();
                    calendar.get(Calendar.DAY_OF_WEEK_IN_MONTH);
                    JSONArray jsonArray = jsonObject.getJSONArray(String.valueOf(calendar.get(Calendar.DAY_OF_MONTH)));
                    final ArrayList<String> list = new ArrayList<String>();
                    for (int i = 0; i < jsonArray.length(); i++) {
                        list.add(String.valueOf(jsonArray.get(i)));
                    }
                    final StableArrayAdapter adapter = new StableArrayAdapter(getActivity().getApplicationContext(), R.layout.custom_list_item, list);
                    listView_yemek.setAdapter(adapter);
                } catch (JSONException e) {
                    final ArrayList<String> list = new ArrayList<String>();
                    list.add("Yemek listesi kaydı bulunmamaktadır");
                    final StableArrayAdapter adapter = new StableArrayAdapter(getActivity().getApplicationContext(), R.layout.custom_list_item, list);
                    listView_yemek.setAdapter(adapter);
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.wtf("YemekFrag", "Error : " + error);
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

    private class StableArrayAdapter extends ArrayAdapter<String> {
        HashMap<String, Integer> mIdMap = new HashMap<String, Integer>();

        StableArrayAdapter(Context context, int textViewResourceId, List<String> objects) {
            super(context, textViewResourceId, objects);
            for (int i = 0; i < objects.size(); ++i) {
                mIdMap.put(objects.get(i), i);
            }
        }

        @Override
        public long getItemId(int position) {
            String item = getItem(position);
            return mIdMap.get(item);
        }

        @Override
        public boolean hasStableIds() {
            return true;
        }

    }

}


