package agency.vavien.ekolife;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.provider.MediaStore;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;

import org.json.JSONException;
import org.json.JSONObject;
import org.xml.sax.InputSource;
import org.xml.sax.XMLReader;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;

import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;

public class HaberDetayActivity extends AppCompatActivity implements View.OnClickListener {
    private ImageView imageView_haber_detay_gorsel, imageButton_share;
    private TextView textView_haber_detay_title, textView_haber_detay_content;
    private Uri imguri;
    private String haberResimUrlString = "";
    private String subject, text;

    public static Bitmap getBitmapFromURL(String src) {
        try {
            URL url = new URL(src);
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setDoInput(true);
            connection.connect();
            InputStream input = connection.getInputStream();
            return BitmapFactory.decodeStream(input);
        } catch (IOException e) {
            Log.wtf("HaberDetay", "" + e);
            return null;
        }
    }

    class ClassgetBitmapFromURL extends AsyncTask<String, Void, Bitmap> {

        protected Bitmap doInBackground(String... urls) {
            try {
                URL url = new URL(urls[0]);
                HttpURLConnection connection = (HttpURLConnection) url.openConnection();
                connection.setDoInput(true);
                connection.connect();
                InputStream input = connection.getInputStream();
                return BitmapFactory.decodeStream(input);
            } catch (IOException e) {
                Log.wtf("HaberDetay", "" + e);
                return null;
            }
        }

        protected void onPostExecute(Bitmap bitmap) {
            imguri = getImageUri(getApplicationContext(), bitmap);
            if (imguri != null) {
                if (Build.VERSION.SDK_INT >= 23) {
                    if (checkPermission()) {
                        Intent share = new Intent(Intent.ACTION_SEND);
                        share.setType("text/plain");
                        share.putExtra(Intent.EXTRA_SUBJECT, subject);
                        share.putExtra(Intent.EXTRA_TEXT, text);
                        share.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
                        share.setType("image/*");
                        share.putExtra(Intent.EXTRA_STREAM, imguri);
                        startActivity(Intent.createChooser(share, "Bu haberi paylaşın"));
                    } else {
                        requestPermission();
                    }
                } else {
                    requestPermission();
                }
            } else {
                //Toast.makeText(this, "Bir hata oluştu", Toast.LENGTH_SHORT).show();
            }
        }

    }

    public Uri getImageUri(Context inContext, Bitmap inImage) {
        if (inImage != null) {
            ByteArrayOutputStream bytes = new ByteArrayOutputStream();
            inImage.compress(Bitmap.CompressFormat.JPEG, 100, bytes);
            String path = "";
            if (checkPermission2()) {
                path = MediaStore.Images.Media.insertImage(inContext.getContentResolver(), inImage, "Title", null);
                return Uri.parse(path);
            } else {
                requestPermission2();
                return null;
            }
        } else
            return null;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_haber_detay);

        imageView_haber_detay_gorsel = findViewById(R.id.imageView_haber_detay_gorsel);
        textView_haber_detay_title = findViewById(R.id.textView_haber_detay_title);
        textView_haber_detay_content = findViewById(R.id.textView_haber_detay_content);
        imageButton_share = findViewById(R.id.imageButton_share);

        imageButton_share.setOnClickListener(this);

        int newsId = getIntent().getExtras().getInt("InHaberId");

        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());

        getRequest(newsId, preferences.getString("autoId", null));
    }

    private void getRequest(int newsId, final String autoId) {
        RequestQueue queue = Volley.newRequestQueue(getApplicationContext());
        StringRequest stringRequest = new StringRequest(Request.Method.GET, "https://ekolife.ekoccs.com/api/News?id=" + newsId, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Log.wtf("HaberDetayAct", "Response : " + response);
                try {
                    JSONObject jsonObject = new JSONObject(response);
                    if (!jsonObject.getBoolean("BoYayinla"))
                        imageButton_share.setVisibility(View.GONE);

                    subject = jsonObject.getString("StHaberBasligi");
                    text = jsonObject.getString("StHaber");

                    textView_haber_detay_title.setText(jsonObject.getString("StHaberBasligi"));
                    textView_haber_detay_content.setText(jsonObject.getString("StHaber"));

                    haberResimUrlString = jsonObject.getString("StResimUrl");

                    Glide.with(getApplicationContext())
                            .load(jsonObject.getString("StResimUrl"))
                            .diskCacheStrategy(DiskCacheStrategy.NONE)
                            .skipMemoryCache(true)
                            .fitCenter()
                            .centerCrop()
                            //.apply(RequestOptions.circleCropTransform())
                            .into(imageView_haber_detay_gorsel);

                    if (jsonObject.getString("StResimUrl").equals("null")) {
                        imageView_haber_detay_gorsel.getLayoutParams().height = 1;
                        imageView_haber_detay_gorsel.requestLayout();
                    }

                    /*Picasso.with(getApplicationContext()).load(jsonObject.getString("StResimUrl")).into(new Target() {
                        @Override
                        public void onBitmapLoaded(Bitmap bitmap, Picasso.LoadedFrom from) {
                            imageView_haber_detay_gorsel.setImageBitmap(bitmap);
                        }

                        @Override
                        public void onBitmapFailed(Drawable errorDrawable) {

                        }

                        @Override
                        public void onPrepareLoad(Drawable placeHolderDrawable) {

                        }
                    });*/
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.wtf("HaberDetayAct", "Error : " + error);
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

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.imageButton_share:

                new ClassgetBitmapFromURL().execute(haberResimUrlString);

                /*imguri = getImageUri(this, getBitmapFromURL(haberResimUrlString));

                if (imguri != null) {
                    if (Build.VERSION.SDK_INT >= 23) {
                        if (checkPermission()) {
                            Intent share = new Intent(Intent.ACTION_SEND);
                            share.setType("text/plain");
                            share.putExtra(Intent.EXTRA_SUBJECT, subject);
                            share.putExtra(Intent.EXTRA_TEXT, text);
                            share.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
                            share.setType("image/*");
                            share.putExtra(Intent.EXTRA_STREAM, imguri);
                            startActivity(Intent.createChooser(share, "Bu haberi paylaşın"));
                        } else {
                            requestPermission();
                        }
                    } else {
                        requestPermission();
                    }
                } else {
                    Toast.makeText(this, "Bir hata oluştu", Toast.LENGTH_SHORT).show();
                }*/

                break;
        }
    }

    private boolean checkPermission() {
        int result = ContextCompat.checkSelfPermission(this, android.Manifest.permission.READ_EXTERNAL_STORAGE);
        return result == PackageManager.PERMISSION_GRANTED;
    }

    private boolean checkPermission2() {
        int result = ContextCompat.checkSelfPermission(this, android.Manifest.permission.WRITE_EXTERNAL_STORAGE);
        return result == PackageManager.PERMISSION_GRANTED;
    }

    private void requestPermission() {
        ActivityCompat.requestPermissions(this, new String[]{android.Manifest.permission.READ_EXTERNAL_STORAGE}, 1);
    }

    private void requestPermission2() {
        ActivityCompat.requestPermissions(this, new String[]{android.Manifest.permission.WRITE_EXTERNAL_STORAGE}, 2);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String permissions[], int[] grantResults) {
        switch (requestCode) {
            case 1:
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    Intent share = new Intent(Intent.ACTION_SEND);
                    share.setType("text/plain");
                    share.putExtra(Intent.EXTRA_SUBJECT, subject);
                    share.putExtra(Intent.EXTRA_TEXT, text);
                    share.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
                    share.setType("image/*");
                    share.putExtra(Intent.EXTRA_STREAM, imguri);
                    startActivity(Intent.createChooser(share, "Bu haberi paylaşın"));
                } else {
                    Toast.makeText(this, "Paylaşım yapabilmek için izin vermeniz lazım", Toast.LENGTH_SHORT).show();
                }
                break;

            case 2:
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    Toast.makeText(this, "Lütfen tekrar deneyiniz", Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(this, "Paylaşım yapabilmek için izin vermeniz lazım", Toast.LENGTH_SHORT).show();
                }
                break;
        }
    }

}
