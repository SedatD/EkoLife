package agency.vavien.ekolife.adapters;

import android.support.v4.content.ContextCompat;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.ArrayList;

import agency.vavien.ekolife.R;
import agency.vavien.ekolife.pojo_classes.DashHaberPojo;

/**
 * Created by SD on 18.12.2017.
 * dilmacsedat@gmail.com
 * :)
 */

public class DashHaberAdapter extends RecyclerView.Adapter<DashHaberAdapter.DataObjectHolder> {
    private static MyClickListener myClickListener;
    private ArrayList<DashHaberPojo> mDataset;
    private int back;

    public DashHaberAdapter(ArrayList<DashHaberPojo> myDataset) {
        mDataset = myDataset;
    }

    public void setOnItemClickListener(MyClickListener myClickListener) {
        DashHaberAdapter.myClickListener = myClickListener;
    }

    @Override
    public DataObjectHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.recyclerview_item_dash_haber, parent, false);

        int height = parent.getMeasuredHeight() / 3;
        int width = parent.getMeasuredWidth();

        view.setLayoutParams(new RecyclerView.LayoutParams(width, height));
        back = ContextCompat.getColor(parent.getContext(), R.color.haberGrisi);
        DataObjectHolder dataObjectHolder = new DataObjectHolder(view);
        return dataObjectHolder;
    }

    @Override
    public void onBindViewHolder(DataObjectHolder holder, int position) {
        if (position % 2 == 0)
            holder.linearLayout_recylerView_dash_haber.setBackgroundColor(back);

        holder.textView_dash_haber_title.setText(mDataset.get(position).getTitle());
        holder.textView_dash_haber_summary.setText(mDataset.get(position).getSummary());
        holder.textView_dash_haber_date.setText(mDataset.get(position).getNewsDate());
    }

    public void addItem(DashHaberPojo dataObj, int index) {
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
        void onItemClick(int position, View v);
    }

    public static class DataObjectHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        LinearLayout linearLayout_recylerView_dash_haber;
        TextView textView_dash_haber_title, textView_dash_haber_summary, textView_dash_haber_date;

        private DataObjectHolder(View itemView) {
            super(itemView);
            linearLayout_recylerView_dash_haber = itemView.findViewById(R.id.linearLayout_recylerView_dash_haber);
            textView_dash_haber_title = itemView.findViewById(R.id.textView_dash_haber_title);
            textView_dash_haber_summary = itemView.findViewById(R.id.textView_dash_haber_summary);
            textView_dash_haber_date = itemView.findViewById(R.id.textView_dash_haber_date);
            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            myClickListener.onItemClick(getPosition(), v);
        }
    }
}
