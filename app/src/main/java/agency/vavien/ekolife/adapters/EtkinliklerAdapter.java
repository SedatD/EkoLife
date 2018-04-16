package agency.vavien.ekolife.adapters;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.ArrayList;

import agency.vavien.ekolife.R;
import agency.vavien.ekolife.pojo_classes.EtkinliklerPojo;

/**
 * Created by SD on 19.12.2017.
 * dilmacsedat@gmail.com
 * :)
 */

public class EtkinliklerAdapter extends RecyclerView.Adapter<EtkinliklerAdapter.DataObjectHolder> {
    private static String LOG_TAG = "EtkinliklerAdapter";
    private static MyClickListener myClickListener;
    private ArrayList<EtkinliklerPojo> mDataset;

    public EtkinliklerAdapter(ArrayList<EtkinliklerPojo> myDataset) {
        mDataset = myDataset;
    }

    public void setOnItemClickListener(MyClickListener myClickListener) {
        this.myClickListener = myClickListener;
    }

    @Override
    public DataObjectHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.cardview_row, parent, false);

        DataObjectHolder dataObjectHolder = new DataObjectHolder(view);

        return dataObjectHolder;
    }

    @Override
    public void onBindViewHolder(DataObjectHolder holder, int position) {
        holder.textView_etkinlik_title.setText(mDataset.get(position).getTitle());
        holder.textView_etkinlik_text.setText(mDataset.get(position).getText());
        holder.textView_etkinlik_yer.setText(mDataset.get(position).getYer());
        holder.textView_etkinlik_date.setText(mDataset.get(position).getDate());
    }

    public void addItem(EtkinliklerPojo dataObj, int index) {
        mDataset.add(index, dataObj);
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
        TextView textView_etkinlik_title, textView_etkinlik_text, textView_etkinlik_yer, textView_etkinlik_date;

        public DataObjectHolder(View itemView) {
            super(itemView);
            textView_etkinlik_title = (TextView) itemView.findViewById(R.id.textView_etkinlik_title);
            textView_etkinlik_text = (TextView) itemView.findViewById(R.id.textView_etkinlik_text);
            textView_etkinlik_yer = (TextView) itemView.findViewById(R.id.textView_etkinlik_yer);
            textView_etkinlik_date = (TextView) itemView.findViewById(R.id.textView_etkinlik_date);
        }

        @Override
        public void onClick(View v) {
            myClickListener.onItemClick(getAdapterPosition(), v);
        }
    }
}