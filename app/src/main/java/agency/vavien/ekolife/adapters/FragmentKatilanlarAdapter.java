package agency.vavien.ekolife.adapters;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
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

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import agency.vavien.ekolife.R;
import agency.vavien.ekolife.pojo_classes.KatilanlarPojo;

/**
 * Created by SD on 18.12.2017.
 * dilmacsedat@gmail.com
 * :)
 */

public class FragmentKatilanlarAdapter extends RecyclerView.Adapter<FragmentKatilanlarAdapter.DataObjectHolder> {
    private static MyClickListener myClickListener;
    private ArrayList<KatilanlarPojo> mDataset;
    private Context context;

    public FragmentKatilanlarAdapter(Context context, ArrayList<KatilanlarPojo> myDataset) {
        this.context = context;
        mDataset = myDataset;
    }

    public void setOnItemClickListener(MyClickListener myClickListener) {
        this.myClickListener = myClickListener;
    }

    @Override
    public DataObjectHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.recyclerview_item_katilanlar, parent, false);
        DataObjectHolder dataObjectHolder = new DataObjectHolder(view);
        return dataObjectHolder;
    }

    @Override
    public void onBindViewHolder(final DataObjectHolder holder, int position) {
        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(context);
        if (Integer.valueOf(preferences.getString("userId", null)) == mDataset.get(position).getId()) {
            holder.button_katilanlar_tebrik.setVisibility(View.GONE);
            holder.textYourself.setText(123 + " kişi tebrik etti.");
            holder.textYourself.setVisibility(View.VISIBLE);
        }

        if (position % 2 == 0)
            holder.relativeLayout_recyclerView_katilanlar_genel.setBackgroundResource(R.drawable.konfetisari);
        else
            holder.relativeLayout_recyclerView_katilanlar_genel.setBackgroundResource(R.drawable.konfetimavi);

        //Picasso.with(context).load(mDataset.get(position).getBitmapPhoto()).resize(160, 80).into(holder.imageView_katilanlar_foto);
        Glide.with(context)
                .load(mDataset.get(position).getBitmapPhoto())
                //.fitCenter()
                //.centerCrop()
                .into(holder.imageView_katilanlar_foto);
        holder.textView_katilanlar_departman.setText(mDataset.get(position).getDepartman());
        holder.textView_katilanlar_isim.setText(mDataset.get(position).getName());
        holder.textView_katilanlar_mail.setText(mDataset.get(position).getMail());

        holder.button_katilanlar_tebrik.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                holder.progressBar.setVisibility(View.VISIBLE);
                SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(context);
                getRequest(preferences.getString("userId", null), mDataset.get(holder.getAdapterPosition()).getId(), preferences.getString("autoId", null), holder.progressBar);
            }
        });
    }

    private void getRequest(String userId, int id, final String autoId, final ProgressBar progressBar) {
        RequestQueue queue = Volley.newRequestQueue(context);
        StringRequest stringRequest = new StringRequest(
                Request.Method.GET,
                "https://ekolife.vodasoft.com.tr/api/General/InsertLike?userId=" + userId + "&likedPersonId=" + id + "&typeId=2",
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        progressBar.setVisibility(View.GONE);
                        Log.wtf("KatilanAdapter", "response : " + response);
                        Toast.makeText(context, "Tebrik ettiniz.", Toast.LENGTH_SHORT).show();
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        progressBar.setVisibility(View.GONE);
                        Log.wtf("KatilanAdapter", "error : " + error);
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

    public void addItem(KatilanlarPojo dataObj, int index) {
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

    public static class DataObjectHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        RelativeLayout relativeLayout_recyclerView_katilanlar_genel;
        ImageView imageView_katilanlar_foto;
        TextView textView_katilanlar_departman, textView_katilanlar_isim, textView_katilanlar_mail, textYourself;
        Button button_katilanlar_tebrik;
        ProgressBar progressBar;

        private DataObjectHolder(View itemView) {
            super(itemView);
            relativeLayout_recyclerView_katilanlar_genel = itemView.findViewById(R.id.relativeLayout_recyclerView_katilanlar_genel);
            imageView_katilanlar_foto = itemView.findViewById(R.id.imageView_katilanlar_foto);
            textView_katilanlar_departman = itemView.findViewById(R.id.textView_katilanlar_departman);
            textView_katilanlar_isim = itemView.findViewById(R.id.textView_katilanlar_isim);
            textView_katilanlar_mail = itemView.findViewById(R.id.textView_katilanlar_mail);
            button_katilanlar_tebrik = itemView.findViewById(R.id.button_katilanlar_tebrik);
            textYourself = itemView.findViewById(R.id.textYourself);
            progressBar = itemView.findViewById(R.id.progressBar);

            //itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            myClickListener.onItemClick(getPosition(), v);
        }
    }

}

