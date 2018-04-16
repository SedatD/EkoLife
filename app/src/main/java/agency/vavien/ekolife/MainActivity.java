package agency.vavien.ekolife;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.preference.PreferenceManager;
import android.support.design.widget.NavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

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

import java.util.HashMap;
import java.util.Map;

import agency.vavien.ekolife.fragments.ContactFragment;
import agency.vavien.ekolife.fragments.DashFragment;
import agency.vavien.ekolife.fragments.DoganlarFragment;
import agency.vavien.ekolife.fragments.EtkinlikFragment;
import agency.vavien.ekolife.fragments.HaberlerFragment;
import agency.vavien.ekolife.fragments.KatilanlarFragment;
import agency.vavien.ekolife.fragments.OneriFragment;
import agency.vavien.ekolife.fragments.ProfilFragment;
import agency.vavien.ekolife.fragments.ServisFragment;
import agency.vavien.ekolife.fragments.YemekFragment;

public class MainActivity extends AppCompatActivity {
    private static final String TAG_DASH = "dash";
    private static final String TAG_DOGAN = "dogan";
    private static final String TAG_ETKINLIK = "etkinlik";
    private static final String TAG_CONTACT = "contact";
    private static final String TAG_HABER = "haber";
    private static final String TAG_KATILANLAR = "katilan";
    private static final String TAG_ONERI = "oneri";
    private static final String TAG_PROFIL = "profil";
    private static final String TAG_SERVIS = "servis";
    private static final String TAG_YEMEK = "yemek";
    private static final String TAG_CIKIS = "cikis";
    public static int navItemIndex = 0;
    public static String CURRENT_TAG = TAG_DASH;
    private static NavigationView navigationView;
    private DrawerLayout drawer;
    private boolean shouldLoadHomeFragOnBackPress = true;
    private Handler mHandler;
    private Menu menu;
    private JSONObject jsonObject;
    private ImageView imageView;
    private TextView userFullName;

    public static void selectNavMenu() {
        navigationView.getMenu().getItem(navItemIndex).setChecked(true);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle(null);

        mHandler = new Handler();
        drawer = findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();

        navigationView = findViewById(R.id.nav_view);
        View header = navigationView.getHeaderView(0);
        imageView = header.findViewById(R.id.profilpic);
        userFullName = header.findViewById(R.id.userFullName);

        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(this);
        getRequest(preferences.getString("userId", null), preferences.getString("autoId", null));

        userFullName.setText(preferences.getString("userFullName", null));

        loadHomeFragment();

        setUpNavigationView();

        //startActivity(new Intent(MainActivity.this, MoodActivity.class));
    }

    private void loadHomeFragment() {
        // selecting appropriate nav menu item
        selectNavMenu();

        Runnable mPendingRunnable = new Runnable() {
            @Override
            public void run() {
                Fragment fragment = getHomeFragment();
                FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
                //fragmentTransaction.setCustomAnimations(android.R.anim.fade_in, android.R.anim.fade_out);
                fragmentTransaction.replace(R.id.frame, fragment, CURRENT_TAG);
                if (CURRENT_TAG.equals(TAG_DASH)) {
                    //fragmentTransaction.disallowAddToBackStack();
                    //fragmentTransaction.addToBackStack(null);
                }
                fragmentTransaction.commit();
                //fragmentTransaction.commitAllowingStateLoss();
            }
        };

        // If mPendingRunnable is not null, then add to the message queue
        mHandler.post(mPendingRunnable);

        // Closing drawer on item click
        drawer.closeDrawers();

        // refresh toolbar menu
        invalidateOptionsMenu();
    }

    private void getRequest(final String userId, final String autoId) {
        RequestQueue queue = Volley.newRequestQueue(this);
        StringRequest stringRequest = new StringRequest(Request.Method.GET, "https://ekolife.vodasoft.com.tr/api/General/GetHomePage?userId=" + userId, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Log.wtf("MainAct", "Response : " + response);
                try {
                    jsonObject = new JSONObject(response);

                    SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
                    SharedPreferences.Editor editor = preferences.edit();
                    editor.putString("jsonObject", jsonObject + "");
                    editor.apply();

                    if (jsonObject.getInt("birthdaylikes") > -1)
                        userFullName.setText("İyi ki doğdun " + userFullName.getText());
                    if (jsonObject.getInt("joinlikes") > -1)
                        userFullName.setText("Aramıza hoşgeldin " + userFullName.getText());

                    String userImage = jsonObject.getString("userImage");

                    Glide.with(getApplicationContext())
                            .load(userImage)
                            .diskCacheStrategy(DiskCacheStrategy.NONE)
                            .skipMemoryCache(true)
                            // .apply(RequestOptions.circleCropTransform())
                            .into(imageView);

                    /*Picasso.with(getApplicationContext()).load(userImage).into(new Target() {
                        @Override
                        public void onBitmapLoaded(Bitmap bitmap, Picasso.LoadedFrom from) {
                            imageView.setImageBitmap(bitmap);
                        }

                        @Override
                        public void onBitmapFailed(Drawable errorDrawable) {
                        }

                        @Override
                        public void onPrepareLoad(Drawable placeHolderDrawable) {

                        }
                    });*/

                    loadHomeFragment();

                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.wtf("MainAct", "Error : " + error);
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

    private Fragment getHomeFragment() {
        switch (navItemIndex) {
            case 0:
                /*Bundle bundle = new Bundle();
                bundle.putString("jsonObject", jsonObject + "");
                DashFragment dashFragment = new DashFragment();
                dashFragment.setArguments(bundle);*/
                return new DashFragment();
            case 1:
                return new DoganlarFragment();
            case 2:
                return new EtkinlikFragment();
            case 3:
                return new ContactFragment();
            case 4:
                return new HaberlerFragment();
            case 5:
                return new KatilanlarFragment();
            case 6:
                return new OneriFragment();
            case 7:
                return new ProfilFragment();
            case 8:
                return new ServisFragment();
            case 9:
                return new YemekFragment();
            default:
                return new DashFragment();
        }
    }

    private void setUpNavigationView() {
        navigationView.setNavigationItemSelectedListener(
                new NavigationView.OnNavigationItemSelectedListener() {
                    // This method will trigger on item Click of navigation menu
                    @Override
                    public boolean onNavigationItemSelected(MenuItem menuItem) {
                        //Check to see which item was being clicked and perform appropriate action
                        switch (menuItem.getItemId()) {
                            //Replacing the main content with ContentFragment Which is our Inbox View;
                            case R.id.nav_dash:
                                navItemIndex = 0;
                                CURRENT_TAG = TAG_DASH;
                                break;
                            case R.id.nav_doganlar:
                                navItemIndex = 1;
                                CURRENT_TAG = TAG_DOGAN;
                                break;
                            case R.id.nav_etkinlikler:
                                navItemIndex = 2;
                                CURRENT_TAG = TAG_ETKINLIK;
                                break;
                            case R.id.nav_contactList:
                                navItemIndex = 3;
                                CURRENT_TAG = TAG_CONTACT;
                                break;
                            case R.id.nav_haberler:
                                navItemIndex = 4;
                                CURRENT_TAG = TAG_HABER;
                                break;
                            case R.id.nav_katilanlar:
                                navItemIndex = 5;
                                CURRENT_TAG = TAG_KATILANLAR;
                                break;
                            case R.id.nav_oneri:
                                navItemIndex = 6;
                                CURRENT_TAG = TAG_ONERI;
                                break;
                            case R.id.nav_profil:
                                navItemIndex = 7;
                                CURRENT_TAG = TAG_PROFIL;
                                break;
                            case R.id.nav_servis:
                                navItemIndex = 8;
                                CURRENT_TAG = TAG_SERVIS;
                                break;
                            case R.id.nav_yemek:
                                navItemIndex = 9;
                                CURRENT_TAG = TAG_YEMEK;
                                break;
                            case R.id.nav_logout:
                                navItemIndex = 10;
                                CURRENT_TAG = TAG_CIKIS;
                                SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
                                SharedPreferences.Editor editor = preferences.edit();
                                editor.remove("userPassword");
                                editor.apply();
                                finish();
                                break;
                            default:
                                navItemIndex = 0;
                        }

                        getHomeFragment();

                        //Checking if the item is in checked state or not, if not make it in checked state
                        if (menuItem.isChecked()) {
                            menuItem.setChecked(false);
                        } else {
                            menuItem.setChecked(true);
                        }
                        //menuItem.setChecked(true);

                        loadHomeFragment();

                        return true;
                    }
                });
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            if (shouldLoadHomeFragOnBackPress) {
                if (getSupportFragmentManager().getBackStackEntryCount() == 1)
                    getSupportFragmentManager().beginTransaction().addToBackStack(null);
                else
                // checking if user is on other navigation menu
                // rather than home
                if (navItemIndex != 0) {
                    navItemIndex = 0;
                    CURRENT_TAG = TAG_DASH;
                    loadHomeFragment();
                    return;
                }
            }
        }
        super.onBackPressed();
    }

}
