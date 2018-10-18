package agency.vavien.ekolife;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class MoodActivity extends AppCompatActivity implements View.OnClickListener {
    //https://ekolife.ekoccs.com/api/General/GetSystemParameter?value=MOODHELP

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mood);

        // findViewById
        ImageButton imageButton_neseli = findViewById(R.id.imageButton_neseli);
        ImageButton imageButton_uyku = findViewById(R.id.imageButton_uyku);
        ImageButton imageButton_kizgin = findViewById(R.id.imageButton_kizgin);
        ImageButton imageButton_oynatmaya = findViewById(R.id.imageButton_oynatmaya);
        ImageButton imageButton_hayatzor = findViewById(R.id.imageButton_hayatzor);
        ImageButton imageButton_gevsek = findViewById(R.id.imageButton_gevsek);

        TextView textView_neden_soruyoruz = findViewById(R.id.textView_neden_soruyoruz);

        // onClick
        imageButton_neseli.setOnClickListener(this);
        imageButton_uyku.setOnClickListener(this);
        imageButton_kizgin.setOnClickListener(this);
        imageButton_oynatmaya.setOnClickListener(this);
        imageButton_hayatzor.setOnClickListener(this);
        imageButton_gevsek.setOnClickListener(this);

        textView_neden_soruyoruz.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
                getRequest(preferences.getString("autoId", null));
            }
        });

    }

    @Override
    public void onClick(View v) {
        int moodId = -1;
        switch (v.getId()) {
            case R.id.imageButton_neseli:
                moodId = 0;
                break;
            case R.id.imageButton_uyku:
                moodId = 1;
                break;
            case R.id.imageButton_kizgin:
                moodId = 2;
                break;
            case R.id.imageButton_oynatmaya:
                moodId = 3;
                break;
            case R.id.imageButton_hayatzor:
                moodId = 4;
                break;
            case R.id.imageButton_gevsek:
                moodId = 5;
                break;
            default:
                break;
        }
        if (moodId != -1) {
            SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(this);
            postRequest(preferences.getString("userId", null), moodId, preferences.getString("autoId", null));
        } else
            Toast.makeText(this, "Mood seçiminde bir hata oluştu", Toast.LENGTH_SHORT).show();
    }

    private void postRequest(final String userId, final int moodId, final String autoId) {
        RequestQueue queue = Volley.newRequestQueue(this);

        JSONObject jsonBody = new JSONObject();
        try {
            jsonBody.put("InPersonId", userId);
            jsonBody.put("InMood", moodId + "");
        } catch (JSONException e) {
            e.printStackTrace();
        }

        JsonObjectRequest stringRequest = new JsonObjectRequest(
                Request.Method.POST,
                "https://ekolife.ekoccs.com/api/User/InsertMood",
                jsonBody,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        Log.wtf("MoodAct", "Response : " + response);
                        try {
                            if (response.getBoolean("response"))
                                Toast.makeText(MoodActivity.this, "Mood'unuz gönderildi", Toast.LENGTH_SHORT).show();
                            else
                                Toast.makeText(MoodActivity.this, "Bir hata oluştu daha sonra tekrar deneyiniz", Toast.LENGTH_SHORT).show();
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                        finish();
                        startActivity(new Intent(MoodActivity.this, MainActivity.class));
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Log.wtf("MoodAct", "Error : " + error);
                        Toast.makeText(MoodActivity.this, "Bir hata oluştu tekrar deneyiniz", Toast.LENGTH_SHORT).show();
                    }
                }) {
            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                Map<String, String> headers = new HashMap<String, String>();
                headers.put("Authorization", autoId);
                return headers;
            }
            /*@Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<String, String>();
                params.put("InPersonId", userId);
                params.put("InMood", moodId + "");
                return params;
            }*/
        };
        queue.add(stringRequest);
    }

    private void getRequest(final String autoId) {
        RequestQueue queue = Volley.newRequestQueue(getApplicationContext());
        StringRequest stringRequest = new StringRequest(
                Request.Method.GET,
                "https://ekolife.ekoccs.com/api/General/GetSystemParameter?value=MOODHELP",
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        Log.wtf("MoodAct", "Response : " + response);
                        Toast.makeText(MoodActivity.this, "" + response, Toast.LENGTH_LONG).show();
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Log.wtf("MoodAct", "Error : " + error);
                        Toast.makeText(MoodActivity.this, "Bir hata oluştu", Toast.LENGTH_SHORT).show();
                    }
                }) {
            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                Map<String, String> params = new HashMap<String, String>();
                params.put("Authorization", autoId);
                return params;
            }
        };
        stringRequest.setRetryPolicy(new DefaultRetryPolicy(
                4700,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        queue.add(stringRequest);
    }

}
