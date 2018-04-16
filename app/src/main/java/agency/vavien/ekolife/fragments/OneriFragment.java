package agency.vavien.ekolife.fragments;

import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v4.app.Fragment;
import android.util.Base64;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
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
import com.android.volley.toolbox.Volley;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.util.HashMap;
import java.util.Map;

import agency.vavien.ekolife.R;

/**
 * Created by SD on 18.12.2017.
 * dilmacsedat@gmail.com
 * :)
 */

public class OneriFragment extends Fragment implements View.OnClickListener {
    private EditText editText_oneri_title, editText_oneri_content;
    private ImageView imageView;
    private Bitmap mbitmap;
    private ProgressDialog progressDialog;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.layout_oneri, container, false);

        // findViewById
        editText_oneri_title = rootView.findViewById(R.id.editText_oneri_title);
        editText_oneri_content = rootView.findViewById(R.id.editText_oneri_content);
        ImageButton imageButton_oneri_foto = rootView.findViewById(R.id.imageButton_oneri_foto);
        Button button_oneri_gonder = rootView.findViewById(R.id.button_oneri_gonder);
        imageView = rootView.findViewById(R.id.imageView);

        // onClick
        imageButton_oneri_foto.setOnClickListener(this);
        button_oneri_gonder.setOnClickListener(this);

        return rootView;
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.imageButton_oneri_foto:
                //Intent cam_intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                //startActivityForResult(cam_intent, 1);
                Intent intent = new Intent();
                intent.setType("image/*");
                intent.setAction(Intent.ACTION_GET_CONTENT);
                intent.addCategory(Intent.CATEGORY_OPENABLE);
                startActivityForResult(intent, 1);
                break;
            case R.id.button_oneri_gonder:

                if (!editText_oneri_title.getText().toString().equals("") && !editText_oneri_content.getText().toString().equals("")) {
                    progressDialog = new ProgressDialog(getActivity());
                    progressDialog.setMessage("Gönderiliyor lütfen bekleyiniz.");
                    progressDialog.setCancelable(false);
                    progressDialog.show();
                    SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(getActivity().getApplicationContext());
                    postRequest(preferences.getString("userId", null), preferences.getString("autoId", null));
                } else
                    Toast.makeText(getActivity().getApplicationContext(), "Başlık ve not kısmı boş bırakılamaz", Toast.LENGTH_SHORT).show();

                //Intent intent = new Intent(getActivity().getApplicationContext(), DashFragment.class);
                //startActivity(new Intent(getActivity().getApplicationContext(),DashFragment.class));
                //getActivity().getSupportFragmentManager().beginTransaction().replace(R.id.frame, new DashFragment()).commit();
                break;
            default:
                break;
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        /*if (requestCode == 1) {
            Bitmap bitmap = (Bitmap) data.getExtras().get("data");
            imageView.setImageBitmap(bitmap);
        }*/
        Bitmap bitmap = null;
        if (bitmap != null)
            bitmap.recycle();

        InputStream stream;
        try {
            if (data != null) {
                if (data.getData() != null) {
                    stream = getActivity().getContentResolver().openInputStream(data.getData());
                    bitmap = BitmapFactory.decodeStream(stream);
                    stream.close();
                    mbitmap = bitmap;
                    imageView.setImageBitmap(bitmap);
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void postRequest(final String userId, final String autoId) {
        try {
            String imageString = "";
            if (mbitmap != null) {
                int quality = 90;
                ByteArrayOutputStream baos = new ByteArrayOutputStream();
                mbitmap.compress(Bitmap.CompressFormat.JPEG, quality, baos);
                int bs = baos.size();
                while (bs > 999999) {
                    Log.wtf("OneriFrag", "baos.size : " + bs + " / quality : " + quality);
                    baos = new ByteArrayOutputStream();
                    quality = quality - 5;
                    mbitmap.compress(Bitmap.CompressFormat.JPEG, quality, baos);
                    bs = baos.size();
                }
                Log.wtf("OneriFrag", "baos.size : " + bs + " / quality : " + quality);
                byte[] imageBytes = baos.toByteArray();
                imageString = Base64.encodeToString(imageBytes, Base64.DEFAULT);
            }

            String URL = "https://ekolife.vodasoft.com.tr/api/General/InsertFikirSikayet";
            JSONObject jsonBody = new JSONObject();
            jsonBody.put("InUserID", userId);
            jsonBody.put("StKonu", editText_oneri_title.getText());
            jsonBody.put("StNot", editText_oneri_content.getText());
            jsonBody.put("StImage", imageString.trim());

            JsonObjectRequest jsonOblect = new JsonObjectRequest(
                    Request.Method.POST,
                    URL,
                    jsonBody,
                    new Response.Listener<JSONObject>() {
                        @Override
                        public void onResponse(JSONObject response) {
                            Log.wtf("OneriFrag", "Response : " + response);
                            progressDialog.dismiss();

                            try {
                                if (response.getBoolean("response"))
                                    Toast.makeText(getActivity().getApplicationContext(), "Notunuz iletilmiştir", Toast.LENGTH_SHORT).show();
                                else
                                    Toast.makeText(getActivity().getApplicationContext(), "Bir hata oluştu lütfen daha sonra tekrar deneyiniz", Toast.LENGTH_SHORT).show();
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }

                            getActivity().getSupportFragmentManager().beginTransaction().replace(R.id.frame, new DashFragment()).commit();

                            /*MainActivity.navItemIndex = 0;
                            getActivity().finish();
                            startActivity(new Intent(getActivity(), MainActivity.class));*/

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
                            Log.wtf("OneriFrag", "onErrorResponse : " + error + " / volleyError.getMessage() : " + error.getMessage());
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

}

