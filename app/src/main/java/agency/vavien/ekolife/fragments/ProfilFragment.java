package agency.vavien.ekolife.fragments;

import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.support.media.ExifInterface;
import android.net.Uri;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.provider.MediaStore;
import android.support.v4.app.Fragment;
import android.util.Base64;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
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
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

import agency.vavien.ekolife.MainActivity;
import agency.vavien.ekolife.R;

import static android.app.Activity.RESULT_OK;

/**
 * Created by SD on 18.12.2017.
 * dilmacsedat@gmail.com
 * :)
 */

public class ProfilFragment extends Fragment implements View.OnClickListener {
    private ProgressDialog progressDialog;
    private Bitmap mbitmap = null;
    private int PICK_IMAGE_REQUEST = 111;
    private TextView textView_profil_ad_soyad, textView_profil_mail, textView_profil_departman;
    private EditText editText_profil_facebook, editText_profil_linkedin, editText_profil_instagram;
    private ImageButton imageButton_profil_resmi;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.layout_profil, container, false);

        // findViewById
        imageButton_profil_resmi = rootView.findViewById(R.id.imageButton_profil_resmi);
        Button button_profil_gonder = rootView.findViewById(R.id.button_profil_gonder);


        textView_profil_ad_soyad = rootView.findViewById(R.id.textView_profil_ad_soyad);
        textView_profil_mail = rootView.findViewById(R.id.textView_profil_mail);
        textView_profil_departman = rootView.findViewById(R.id.textView_profil_departman);

        editText_profil_facebook = rootView.findViewById(R.id.editText_profil_facebook);
        editText_profil_linkedin = rootView.findViewById(R.id.editText_profil_linkedin);
        editText_profil_instagram = rootView.findViewById(R.id.editText_profil_instagram);

        //imageButton_profil_resmi.setImageDrawable(getResources().getDrawable(R.drawable.fotoekle));

        // onClick
        button_profil_gonder.setOnClickListener(this);
        imageButton_profil_resmi.setOnClickListener(this);

        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(getActivity().getApplicationContext());
        getRequest(preferences.getString("userId", null), preferences.getString("autoId", null));

        return rootView;
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.imageButton_profil_resmi:
                Intent intent = new Intent();
                intent.setType("image/*");
                intent.setAction(Intent.ACTION_GET_CONTENT);
                intent.addCategory(Intent.CATEGORY_OPENABLE);
                startActivityForResult(intent, 1);
                break;
            case R.id.button_profil_gonder:
                progressDialog = new ProgressDialog(getActivity());
                progressDialog.setMessage("Gönderiliyor lütfen bekleyiniz.");
                progressDialog.setCancelable(false);
                progressDialog.show();
                SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(getActivity().getApplicationContext());
                sendWorkPostRequest(preferences.getString("userId", null), preferences.getString("autoId", null));
                //postRequest(preferences.getString("userId", null), preferences.getString("autoId", null));
                //hiamina(preferences.getString("userId", null), preferences.getString("autoId", null));
                break;
            default:
                break;
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 1) {
            InputStream stream;

            Bitmap bitmap;
            try {
                if (data != null) {
                    if (data.getData() != null) {
                        stream = getActivity().getContentResolver().openInputStream(data.getData());
                        bitmap = BitmapFactory.decodeStream(stream);
                        stream.close();
                        mbitmap = bitmap;
                        /*ExifInterface exif = new ExifInterface(stream);
                        int orientation = exif.getAttributeInt(ExifInterface.TAG_ORIENTATION, 1);
                        Matrix matrix = new Matrix();
                        if (orientation == 3)
                            matrix.postRotate(180);
                        if (orientation == 6)
                            matrix.postRotate(90);
                        if (orientation == 8)
                            matrix.postRotate(270);
                        mbitmap = Bitmap.createBitmap(mbitmap, 0, 0, mbitmap.getWidth(), mbitmap.getHeight(), matrix, true);*/
                        imageButton_profil_resmi.setImageBitmap(bitmap);
                    }
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        } else {
            super.onActivityResult(requestCode, resultCode, data);
            if (requestCode == PICK_IMAGE_REQUEST && resultCode == RESULT_OK && data != null && data.getData() != null) {
                Uri filePath = data.getData();
                try {
                    mbitmap = MediaStore.Images.Media.getBitmap(getActivity().getContentResolver(), filePath);
                    imageButton_profil_resmi.setImageBitmap(mbitmap);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }
    }

    private void getRequest(String newsId, final String autoId) {
        RequestQueue queue = Volley.newRequestQueue(getActivity().getApplicationContext());
        StringRequest stringRequest = new StringRequest(Request.Method.GET, "https://ekolife.vodasoft.com.tr/api/User/GetUserProfile?userId=" + newsId,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        Log.wtf("ProfilFrag", "Response : " + response);
                        try {
                            JSONObject jsonObject = new JSONObject(response);

                            textView_profil_ad_soyad.setText(jsonObject.getString("StFullName"));
                            textView_profil_mail.setText(jsonObject.getString("stFrmeMail"));
                            textView_profil_departman.setText(jsonObject.getString("StProjectName"));

                            //editText_profil_facebook.setText(jsonObject.getString("StFacebook"));
                            //editText_profil_linkedin.setText(jsonObject.getString("StLinkedin"));
                            //editText_profil_instagram.setText(jsonObject.getString("StInstagram"));

                            if (!Objects.equals(jsonObject.getString("StFacebook"), "null"))
                                editText_profil_facebook.setText(jsonObject.getString("StFacebook"));
                            else
                                editText_profil_facebook.setHint("facebook adresiniz");

                            if (!Objects.equals(jsonObject.getString("StLinkedin"), "null"))
                                editText_profil_linkedin.setText(jsonObject.getString("StLinkedin"));
                            else
                                editText_profil_linkedin.setHint("linkedin adresiniz");

                            if (!Objects.equals(jsonObject.getString("StInstagram"), "null"))
                                editText_profil_instagram.setText(jsonObject.getString("StInstagram"));
                            else
                                editText_profil_instagram.setHint("instagram adresiniz");

                            if (jsonObject.getString("StUserImage") == null)
                                imageButton_profil_resmi.setImageDrawable(getResources().getDrawable(R.drawable.fotoekle));
                            if (Objects.equals(jsonObject.getString("StUserImage"), "null"))
                                imageButton_profil_resmi.setImageDrawable(getResources().getDrawable(R.drawable.fotoekle));

                            Glide.with(getActivity().getApplicationContext())
                                    .load(jsonObject.getString("StUserImage"))
                                    .diskCacheStrategy(DiskCacheStrategy.NONE)
                                    //.centerCrop()
                                    //.fitCenter()
                                    .skipMemoryCache(true)
                                    //.apply(RequestOptions.circleCropTransform())
                                    .into(imageButton_profil_resmi);
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Log.wtf("ProfilFrag", "Error : " + error);
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

    private void sendWorkPostRequest(final String userId, final String autoId) {
        try {
            String imageString = "";
            if (mbitmap != null) {
                int quality = 90;
                ByteArrayOutputStream baos = new ByteArrayOutputStream();
                mbitmap.compress(Bitmap.CompressFormat.JPEG, quality, baos);
                int bs = baos.size();
                while (bs > 999999) {
                    Log.wtf("ProfilFrag", "baos.size : " + bs + " / quality : " + quality);
                    baos = new ByteArrayOutputStream();
                    quality = quality - 5;
                    mbitmap.compress(Bitmap.CompressFormat.JPEG, quality, baos);
                    bs = baos.size();
                }
                Log.wtf("ProfilFrag", "baos.size : " + bs + " / quality : " + quality);
                byte[] imageBytes = baos.toByteArray();
                imageString = Base64.encodeToString(imageBytes, Base64.DEFAULT);
            }

            String URL = "https://ekolife.vodasoft.com.tr/api/User/UpdateUser";
            JSONObject jsonBody = new JSONObject();
            jsonBody.put("InUserID", userId);
            jsonBody.put("StFBUserName", editText_profil_facebook.getText());
            jsonBody.put("StInsUserName", editText_profil_instagram.getText());
            jsonBody.put("StLnkdnUserName", editText_profil_linkedin.getText());
            jsonBody.put("StProfileImage", imageString.trim());

            JsonObjectRequest jsonOblect = new JsonObjectRequest(
                    Request.Method.POST,
                    URL,
                    jsonBody,
                    new Response.Listener<JSONObject>() {
                        @Override
                        public void onResponse(JSONObject response) {
                            Log.wtf("ProfilFrag", "Response : " + response);
                            progressDialog.dismiss();

                            try {
                                if (response.getBoolean("response"))
                                    Toast.makeText(getActivity().getApplicationContext(), "Profiliniz güncellenmiştir", Toast.LENGTH_SHORT).show();
                                else
                                    Toast.makeText(getActivity().getApplicationContext(), "Bir hata oluştu lütfen daha sonra tekrar deneyiniz", Toast.LENGTH_SHORT).show();
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }

                            MainActivity.navItemIndex = 0;
                            getActivity().finish();
                            startActivity(new Intent(getActivity(), MainActivity.class));

                            /*DashFragment dashFragment = new DashFragment();
                            getActivity().getSupportFragmentManager().beginTransaction()
                                    .replace(R.id.frame, dashFragment, "dash")
                                    .addToBackStack(null)
                                    .commit();
                            MainActivity.navItemIndex = 0;
                            selectNavMenu();*/
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
                            Log.wtf("ProfilFrag", "onErrorResponse : " + error + " / volleyError.getMessage() : " + error.getMessage());
                            Toast.makeText(getActivity().getApplicationContext(), "Bir hata oluştu lütfen daha sonra tekrar deneyiniz", Toast.LENGTH_SHORT).show();
                            progressDialog.dismiss();
                            error.printStackTrace();
                        }
                    }) {
                @Override
                public Map<String, String> getHeaders() throws AuthFailureError {
                    final Map<String, String> headers = new HashMap<>();
                    headers.put("Content-Type", "application/json; charset=utf-8");
                    headers.put("Authorization", autoId);
                    return headers;
                }
            };
            RequestQueue rQueue = Volley.newRequestQueue(getActivity());
            rQueue.add(jsonOblect);
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    private void postRequest(final String userId, final String autoId) {
        String imageString = "";
        if (mbitmap != null) {
            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            mbitmap.compress(Bitmap.CompressFormat.JPEG, 100, baos);
            byte[] imageBytes = baos.toByteArray();
            imageString = Base64.encodeToString(imageBytes, Base64.DEFAULT);
        }
        final String finalImageString = imageString;

        RequestQueue rQueue = Volley.newRequestQueue(getActivity());
        StringRequest request = new StringRequest(Request.Method.POST, "https://ekolife.vodasoft.com.tr/api/User/UpdateUser",
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        Log.wtf("ProfilFrag", "Response : " + response);
                        progressDialog.dismiss();
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError volleyError) {
                        NetworkResponse response = volleyError.networkResponse;
                        if (volleyError instanceof ServerError && response != null) {
                            try {
                                String res = new String(response.data, HttpHeaderParser.parseCharset(response.headers, "utf-8"));
                                Log.wtf("try", "res : " + res + " / response : " + response);
                                JSONObject obj = new JSONObject(res);
                                Log.wtf("try", "obj : " + obj);
                            } catch (UnsupportedEncodingException | JSONException e1) {
                                e1.printStackTrace();
                            }
                        }
                        Log.wtf("ProfilFrag", "onErrorResponse : " + volleyError + " / volleyError.getMessage() : " + volleyError.getMessage());
                        progressDialog.dismiss();
                        volleyError.printStackTrace();
                    }
                }) {
            @Override
            protected Map<String, String> getParams() {
                Map<String, String> parameters = new HashMap<String, String>();
                parameters.put("InUserID", userId);
                parameters.put("StFBUserName", "fb");
                parameters.put("StInsUserName", "ins");
                parameters.put("StLnkdnUserName ", "linlin");
                parameters.put("StProfileImage", finalImageString);
                Log.wtf("ProfilFrag", "Params : " + parameters);
                return parameters;
            }

            /*@Override
            public String getBodyContentType() {
                return "application/json; charset=utf-8";
            }*/
            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                Map<String, String> headers = new HashMap<String, String>();
                headers.put("Content-Type", "application/json; charset=utf-8");
                headers.put("Authorization", autoId);
                //headers.put("Accept", "application/json");
                //headers.put("Content-Type", "application/json");
                //headers.put("Content-Type", "application/x-www-form-urlencoded");

                //return (headers != null || headers.isEmpty()) ? headers : super.getHeaders();
                return headers;
            }
        };
        rQueue.add(request);
    }

    private void hiamina(final String userId, final String autoId) {
        Map<String, String> jsonParams = new HashMap<String, String>();
        jsonParams.put("InUserID", userId);
        jsonParams.put("StFBUserName", "qwe");
        jsonParams.put("StInsUserName", "asd");
        jsonParams.put("StLnkdnUserName ", "zxc");
        jsonParams.put("StProfileImage", "");
        JsonObjectRequest myRequest = new JsonObjectRequest(
                Request.Method.POST,
                "https://ekolife.vodasoft.com.tr/api/User/UpdateUser",
                new JSONObject(jsonParams),
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        Log.wtf("ProfilFrag", "Response : " + response);
                        progressDialog.dismiss();
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Log.wtf("ProfilFrag", "onErrorResponse : " + error + " / volleyError.getMessage() : " + error.getMessage());
                        progressDialog.dismiss();
                        error.printStackTrace();
                        error.getMessage();
                    }
                }) {
            @Override
            public String getBodyContentType() {
                return "application/json; charset=utf-8";
            }

            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                HashMap<String, String> headers = new HashMap<String, String>();
                headers.put("Authorization", autoId);
                return headers;
            }
        };
        RequestQueue rQueue = Volley.newRequestQueue(getActivity());
        rQueue.add(myRequest);
    }
}