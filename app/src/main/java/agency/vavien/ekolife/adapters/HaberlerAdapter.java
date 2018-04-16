package agency.vavien.ekolife.adapters;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import java.util.ArrayList;

import agency.vavien.ekolife.R;
import agency.vavien.ekolife.pojo_classes.HaberlerPojo;

/**
 * Created by SD on 19.12.2017.
 * dilmacsedat@gmail.com
 * :)
 */

public class HaberlerAdapter extends RecyclerView.Adapter<HaberlerAdapter.DataObjectHolder> {
    private static MyClickListener myClickListener;
    private ArrayList<HaberlerPojo> mDataset;
    private Context context;

    public HaberlerAdapter(Context context, ArrayList<HaberlerPojo> myDataset) {
        this.context = context;
        mDataset = myDataset;
    }

    public void setOnItemClickListener(MyClickListener myClickListener) {
        this.myClickListener = myClickListener;
    }

    @Override
    public DataObjectHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.recyclerview_item_haberler, parent, false);

        /*int height = parent.getMeasuredHeight() / 3;
        int width = parent.getMeasuredWidth();
        view.setLayoutParams(new RecyclerView.LayoutParams(width, height));*/

        DataObjectHolder dataObjectHolder = new DataObjectHolder(view);
        return dataObjectHolder;
    }

    @Override
    public void onBindViewHolder(DataObjectHolder holder, int position) {
        //holder.imageView_haberler_photo.setImageBitmap(mDataset.get(position).getBitmapPhoto());
        Picasso.with(context).load(mDataset.get(position).getBitmapPhoto()).resize(160, 80).into(holder.imageView_haberler_photo);
        holder.textView_haberler_text.setText(mDataset.get(position).getText());
        holder.textView_haberler_date.setText(mDataset.get(position).getDate());
    }

    public void addItem(HaberlerPojo dataObj, int index) {
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
        ImageView imageView_haberler_photo;
        TextView textView_haberler_text, textView_haberler_date;

        private DataObjectHolder(View itemView) {
            super(itemView);
            imageView_haberler_photo = (ImageView) itemView.findViewById(R.id.imageView_haberler_photo);
            textView_haberler_text = (TextView) itemView.findViewById(R.id.textView_haberler_text);
            textView_haberler_date = (TextView) itemView.findViewById(R.id.textView_haberler_date);

            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            myClickListener.onItemClick(getPosition(), v);
        }
    }
}