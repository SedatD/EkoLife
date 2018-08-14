package agency.vavien.ekolife;

import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.onesignal.OneSignal;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class LoginActivity extends AppCompatActivity implements View.OnClickListener {
    private EditText editText_name, editText_password;
    private ProgressDialog progress;
    private Boolean myBool = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        //MyApplication mApplication = (MyApplication)getApplicationContext();

        if (getIntent() != null)
            if (getIntent().getExtras() != null)
                myBool = getIntent().getExtras().getBoolean("isMoodBool");


        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
        if (preferences.getString("userPassword", null) != null) {
            progress = ProgressDialog.show(this, "Giriş Yapılıyor", "Lütfen Bekleyiniz", true);
            postRequest(preferences.getString("userName", null), preferences.getString("userPassword", null));
        }

        initView();
    }

    private void initView() {
        // init components
        TextView textView_login_sifremi_unuttum;
        Button button_login;

        // findViewById
        editText_name = (EditText) findViewById(R.id.editText_login_name);
        editText_password = (EditText) findViewById(R.id.editText_login_password);
        textView_login_sifremi_unuttum = (TextView) findViewById(R.id.textView_login_sifremi_unuttum);
        button_login = (Button) findViewById(R.id.button_login);

        // onClick
        textView_login_sifremi_unuttum.setOnClickListener(this);
        button_login.setOnClickListener(this);

        // other css
        editText_name.getBackground().setColorFilter(Color.rgb(97, 5, 234), PorterDuff.Mode.SRC_IN);
        editText_password.getBackground().setColorFilter(Color.rgb(97, 5, 234), PorterDuff.Mode.SRC_IN);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.textView_login_sifremi_unuttum:
                SifremiUnuttumRequest();
                break;
            case R.id.button_login:
                progress = ProgressDialog.show(this, "Giriş Yapılıyor", "Lütfen Bekleyiniz", true);
                /*if (editText_name.getText().toString().equals("") && editText_password.getText().toString().equals(""))
                    postRequest("okoc", "Ok124578!");
                else*/
                    postRequest(editText_name.getText().toString(), editText_password.getText().toString());
                break;
            default:
                break;
        }
    }

    private void SifremiUnuttumRequest() {
        RequestQueue queue = Volley.newRequestQueue(this);
        StringRequest stringRequest = new StringRequest(Request.Method.GET, "https://ekolife.ekoccs.com/api/General/GetSystemParameter?value=FORGOTPWD ", new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Log.wtf("LoginAct", "Response : " + response);
                Toast.makeText(LoginActivity.this, response, Toast.LENGTH_LONG).show();
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.wtf("LoginAct", "Error: " + error);
            }
        });
        queue.add(stringRequest);
    }

    private void postRequest(final String name, final String password) {
        RequestQueue queue = Volley.newRequestQueue(this);
        StringRequest stringRequest = new StringRequest(Request.Method.POST, "https://ekolife.ekoccs.com/token", new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Log.wtf("LoginAct", "Response : " + response);
                try {
                    JSONObject jsonObject = new JSONObject(response);

                    SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
                    SharedPreferences.Editor editor = preferences.edit();
                    editor.putString("userName", name);
                    editor.putString("userPassword", password);
                    editor.putString("userFullName", jsonObject.getString("userFullName"));
                    editor.putString("userId", jsonObject.getString("userid"));
                    editor.putString("autoId", jsonObject.getString("token_type") + " " + jsonObject.getString("access_token"));
                    editor.apply();

                    if (myBool)
                        startActivity(new Intent(LoginActivity.this, MoodActivity.class));
                    else
                        startActivity(new Intent(LoginActivity.this, MainActivity.class));

                    finish();
                    progress.dismiss();
                } catch (JSONException e) {
                    e.printStackTrace();
                    progress.dismiss();
                    Toast.makeText(LoginActivity.this, "Bir hata oluştu", Toast.LENGTH_SHORT).show();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.wtf("LoginAct", "Error : " + error);
                //postRequest(name, password);
                progress.dismiss();
                Toast.makeText(LoginActivity.this, "Bağlantı Hatası", Toast.LENGTH_SHORT).show();
            }
        }) {
            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<String, String>();
                params.put("grant_type", "password");
                params.put("username", name);
                params.put("password", password);
                String osi = OneSignal.getPermissionSubscriptionState().getSubscriptionStatus().getUserId();
                if (osi != null)
                    params.put("cloudMessagingToken", osi);
                else
                    params.put("cloudMessagingToken", "123");
                Log.wtf("LoginAct", "params : " + params);
                return params;
            }
        };
        queue.add(stringRequest);
    }

}
