package agency.vavien.ekolife.adapters;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.preference.PreferenceManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
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
import com.onesignal.OneSignal;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import agency.vavien.ekolife.R;
import agency.vavien.ekolife.pojo_classes.DoganlarPojo;

/**
 * Created by SD on 18.12.2017.
 * dilmacsedat@gmail.com
 * :)
 */

public class FragmentDoganlarAdapter extends RecyclerView.Adapter<FragmentDoganlarAdapter.DataObjectHolder> {
    private static MyClickListener myClickListener;
    public Context context;
    private ArrayList<DoganlarPojo> mDataset;

    public FragmentDoganlarAdapter(Context context, ArrayList<DoganlarPojo> myDataset) {
        this.context = context;
        mDataset = myDataset;
    }

    public void setOnItemClickListener(MyClickListener myClickListener) {
        this.myClickListener = myClickListener;
    }

    @Override
    public DataObjectHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.recyclerview_item_doganlar, parent, false);
        DataObjectHolder dataObjectHolder = new DataObjectHolder(view);
        return dataObjectHolder;
    }

    @Override
    public void onBindViewHolder(final DataObjectHolder holder, int position) {

        if (mDataset.get(position).isBoIsLiked()) {
            holder.button_doganlar_tebrik.setVisibility(View.GONE);
            holder.textYourself.setText("Bu kişiyi zaten tebrik ettiniz");
            holder.textYourself.setVisibility(View.VISIBLE);
        }

        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(context);
        if (Integer.valueOf(preferences.getString("userId", null)) == mDataset.get(position).getInPersonelID()) {
            holder.button_doganlar_tebrik.setVisibility(View.GONE);
            holder.textYourself.setText(mDataset.get(position).getInLikeAdet() + " kişi tebrik etti.");
            holder.textYourself.setVisibility(View.VISIBLE);
        }

        if (position % 2 == 0)
            holder.linearLayout_recyclerView_doganlar_genel.setBackgroundResource(R.drawable.konfetituruncu);
        else
            holder.linearLayout_recyclerView_doganlar_genel.setBackgroundResource(R.drawable.konfetiyesil);

        //Picasso.with(context).load(mDataset.get(position).getBitmapPhoto()).resize(160, 80).into(holder.imageView_doganlar_foto);
        Glide.with(context)
                .load(mDataset.get(position).getStProfilePhoto())
                //.fitCenter()
                //.centerCrop()
                .into(holder.imageView_doganlar_foto);
        holder.textView_doganlar_text.setText(mDataset.get(position).getStFullName());

        holder.imageButton_doganlar_instagram.setTag(mDataset.get(position).getStInstagram());
        holder.imageButton_doganlar_facebook.setTag(mDataset.get(position).getStFacebook());

        if (mDataset.get(position).getStInstagram().equals("null"))
            holder.imageButton_doganlar_instagram.setVisibility(View.INVISIBLE);
        if (mDataset.get(position).getStFacebook().equals("null"))
            holder.imageButton_doganlar_facebook.setVisibility(View.INVISIBLE);

        holder.button_doganlar_tebrik.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                holder.progressBar.setVisibility(View.VISIBLE);
                SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(context);
                getRequest(preferences.getString("userId", null), mDataset.get(holder.getAdapterPosition()).getInPersonelID(), preferences.getString("autoId", null), holder.progressBar, holder.button_doganlar_tebrik, mDataset.get(holder.getAdapterPosition()).getStOneSignalId());
            }
        });
    }

    private void getRequest(String userId, int id, final String autoId, final ProgressBar progressBar, final Button button_doganlar_tebrik, final String osi) {
        RequestQueue queue = Volley.newRequestQueue(context);
        StringRequest stringRequest = new StringRequest(
                Request.Method.GET,
                "https://ekolife.ekoccs.com/api/General/InsertLike?userId=" + userId + "&likedPersonId=" + id + "&typeId=1",
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        Log.wtf("DoganlarAdapter", "response : " + response);

                        try {
                            OneSignal.postNotification(new JSONObject("{'contents': {'en':'Biri seni tebrik etti :)'}, 'include_player_ids': ['" + osi + "'],'data':{'banaOzel':" + true + "}}"), null);
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }

                        progressBar.setVisibility(View.GONE);
                        //button_doganlar_tebrik.setVisibility(View.INVISIBLE);
                        button_doganlar_tebrik.setBackgroundColor(R.color.contact_number);
                        button_doganlar_tebrik.setText("tebrİk edİldİ");
                        button_doganlar_tebrik.setClickable(false);
                        Toast.makeText(context, "Tebrik ettiniz.", Toast.LENGTH_SHORT).show();
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        progressBar.setVisibility(View.GONE);
                        Log.wtf("DoganlarAdapter", "error : " + error);
                        Toast.makeText(context, "Bir hata oluştu.", Toast.LENGTH_SHORT).show();
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

    public void addItem(DoganlarPojo dataObj, int index) {
        mDataset.add(dataObj);
        notifyItemInserted(index);
    }

    public void deleteItem(int index) {
        mDataset.remove(index);
        notifyItemRemoved(index);
    }

    @Override
    public int getItemCount() {
        return mDataset.size();
    }

    public interface MyClickListener {
        public void onItemClick(int position, View v);
    }

    public class DataObjectHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        LinearLayout linearLayout_recyclerView_doganlar_genel;
        ImageView imageView_doganlar_foto;
        TextView textView_doganlar_text, textYourself;
        Button button_doganlar_tebrik;
        ImageButton imageButton_doganlar_instagram, imageButton_doganlar_facebook;
        ProgressBar progressBar;

        private DataObjectHolder(View itemView) {
            super(itemView);
            linearLayout_recyclerView_doganlar_genel = itemView.findViewById(R.id.linearLayout_recyclerView_doganlar_genel);
            imageView_doganlar_foto = itemView.findViewById(R.id.imageView_doganlar_foto);
            textView_doganlar_text = itemView.findViewById(R.id.textView_doganlar_text);
            button_doganlar_tebrik = itemView.findViewById(R.id.button_doganlar_tebrik);
            imageButton_doganlar_instagram = itemView.findViewById(R.id.imageButton_doganlar_instagram);
            imageButton_doganlar_facebook = itemView.findViewById(R.id.imageButton_doganlar_facebook);
            textYourself = itemView.findViewById(R.id.textYourself);
            progressBar = itemView.findViewById(R.id.progressBar);
            //itemView.setOnClickListener(this);

            imageButton_doganlar_instagram.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if (!imageButton_doganlar_instagram.getTag().equals("null")) {
                        try {
                            context.getPackageManager().getPackageInfo("com.instagram.android", 0);
                            context.startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse(imageButton_doganlar_instagram.getTag().toString())));
                        } catch (Exception e) {
                            context.startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse(imageButton_doganlar_instagram.getTag().toString())));
                        }
                    }
                }
            });

            imageButton_doganlar_facebook.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if (!imageButton_doganlar_facebook.getTag().equals("null")) {
                        try {
                            context.getPackageManager().getPackageInfo("com.facebook.katana", 0);
                            context.startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse(imageButton_doganlar_facebook.getTag().toString())));
                        } catch (Exception e) {
                            context.startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse(imageButton_doganlar_facebook.getTag().toString())));
                        }

                        /*Intent intent = new Intent(android.content.Intent.ACTION_VIEW, Uri.parse(imageButton_doganlar_facebook.getTag().toString()));
                        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                        intent.setClassName("com.google.android.apps.maps", "com.google.android.maps.MapsActivity");
                        context.startActivity(intent);*/
                    }
                }
            });

        }

        @Override
        public void onClick(View v) {
            myClickListener.onItemClick(getPosition(), v);
        }
    }

}
