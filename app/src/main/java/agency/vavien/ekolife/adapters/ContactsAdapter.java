package agency.vavien.ekolife.adapters;

/**
 * Created by belfu on 15.01.2018.
 */


import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;

import java.util.ArrayList;
import java.util.List;

import agency.vavien.ekolife.R;
import agency.vavien.ekolife.pojo_classes.Contact;


public class ContactsAdapter extends RecyclerView.Adapter<ContactsAdapter.MyViewHolder> implements Filterable {
    private Context context;
    private List<Contact> contactList;
    private List<Contact> contactListFiltered;
    private ContactsAdapterListener listener;

    public ContactsAdapter(Context context, List<Contact> contactList, ContactsAdapterListener listener) {
        this.context = context;
        this.listener = listener;
        this.contactList = contactList;
        this.contactListFiltered = contactList;
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.user_row_item, parent, false);
        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, final int position) {
        final Contact contact = contactListFiltered.get(position);
        holder.isim.setText(contact.getName());
        holder.mail.setText(contact.getMail());
        holder.departman.setText(contact.getPName());

        holder.imageButton_insta.setTag(contact.getStInstagram());
        holder.imageButton_face.setTag(contact.getStFacebook());

        if (contact.getStInstagram() == null)
            holder.imageButton_insta.setVisibility(View.GONE);

        int a = 5;
        if (contact.getStInstagram() != null)
            a = 6;

        if (contact.getStFacebook() == null)
            holder.imageButton_face.setVisibility(View.GONE);


        Glide.with(context)
                .load(contact.getPhoto())
                // .apply(RequestOptions.circleCropTransform())
                .into(holder.thumbnail);
    }

    @Override
    public int getItemCount() {
        return contactListFiltered.size();
    }

    @Override
    public Filter getFilter() {
        return new Filter() {
            @Override
            protected FilterResults performFiltering(CharSequence charSequence) {
                String charString = charSequence.toString();
                if (charString.isEmpty()) {
                    contactListFiltered = contactList;
                } else {
                    List<Contact> filteredList = new ArrayList<>();
                    for (Contact row : contactList) {
                        String asd = row.getName().toLowerCase().substring(0, charString.length());
                        if (asd.equals(charString.toLowerCase()))
                            filteredList.add(row);
                        /*if (row.getName().toLowerCase().contains(charString.toLowerCase())) {
                            filteredList.add(row);
                        }*/
                    }
                    contactListFiltered = filteredList;
                }

                FilterResults filterResults = new FilterResults();
                filterResults.values = contactListFiltered;
                return filterResults;
            }

            @Override
            protected void publishResults(CharSequence charSequence, FilterResults filterResults) {
                contactListFiltered = (ArrayList<Contact>) filterResults.values;
                notifyDataSetChanged();
            }
        };
    }

    public interface ContactsAdapterListener {
        void onContactSelected(Contact contact);
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {
        public TextView isim, mail, departman;
        public ImageView thumbnail;
        public ImageButton imageButton_insta, imageButton_face;

        public MyViewHolder(View view) {
            super(view);
            isim = view.findViewById(R.id.isim);
            mail = view.findViewById(R.id.mail);
            departman = view.findViewById(R.id.departman);
            thumbnail = view.findViewById(R.id.thumbnail);
            imageButton_insta = view.findViewById(R.id.imageButton_insta);
            imageButton_face = view.findViewById(R.id.imageButton_face);

            imageButton_insta.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if (imageButton_insta.getTag() != null) {
                        try {
                            context.getPackageManager().getPackageInfo("com.instagram.android", 0);
                            context.startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse(imageButton_insta.getTag().toString())));
                        } catch (Exception e) {
                            Toast.makeText(context, "Bu kullanıcının instagram adresi hatalıdır.", Toast.LENGTH_SHORT).show();
                        }
                    } else
                        Toast.makeText(context, "Bu kullanıcının instagram adresi bulunmamaktadır.", Toast.LENGTH_SHORT).show();
                }
            });

            imageButton_face.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if (imageButton_face.getTag() != null) {
                        try {
                            context.getPackageManager().getPackageInfo("com.facebook.katana", 0);
                            context.startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse(imageButton_face.getTag().toString())));
                        } catch (Exception e) {
                            Toast.makeText(context, "Bu kullanıcının facebook adresi hatalıdır.", Toast.LENGTH_SHORT).show();
                        }
                    } else
                        Toast.makeText(context, "Bu kullanıcının facebook adresi bulunmamaktadır.", Toast.LENGTH_SHORT).show();
                }
            });

            view.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    listener.onContactSelected(contactListFiltered.get(getAdapterPosition()));
                }
            });
        }
    }

}
