package agency.vavien.ekolife.adapters;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.TextView;

import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.util.ArrayList;

import agency.vavien.ekolife.R;
import agency.vavien.ekolife.pojo_classes.ExpChildServisler;
import agency.vavien.ekolife.pojo_classes.ExpGroupServisler;

/**
 * Created by SD on 5.02.2018.
 * dilmacsedat@gmail.com
 * :)
 */

public class ExpAdapterServisler extends BaseExpandableListAdapter {
    private Context context;
    private ArrayList<ExpGroupServisler> groups;

    public ExpAdapterServisler(Context context, ArrayList<ExpGroupServisler> groups) {
        this.context = context;
        this.groups = groups;
    }

    @Override
    public Object getChild(int groupPosition, int childPosition) {
        ArrayList<ExpChildServisler> chList = groups.get(groupPosition).getItems();
        return chList.get(childPosition);
    }

    @Override
    public long getChildId(int groupPosition, int childPosition) {
        return childPosition;
    }

    @Override
    public View getChildView(int groupPosition, int childPosition, boolean isLastChild, View convertView, ViewGroup parent) {
        ExpChildServisler expChildServisler = (ExpChildServisler) getChild(groupPosition, childPosition);

        if (convertView == null) {
            LayoutInflater infalInflater = (LayoutInflater) context.getSystemService(context.LAYOUT_INFLATER_SERVICE);
            convertView = infalInflater.inflate(R.layout.exp_servisler_child_item, null);
        }

        TextView textView_StServis = convertView.findViewById(R.id.textView_StServis);
        final TextView textView_StGuzergahLink = convertView.findViewById(R.id.textView_StGuzergahLink);
        TextView textView_StSurucuAdi = convertView.findViewById(R.id.textView_StSurucuAdi);
        TextView textView_StSurucuGSM = convertView.findViewById(R.id.textView_StSurucuGSM);
        TextView textView_StServisPlaka = convertView.findViewById(R.id.textView_StServisPlaka);

        textView_StServis.setText(expChildServisler.getStServis());
        textView_StGuzergahLink.setText(expChildServisler.getStGuzergahLink());
        textView_StSurucuAdi.setText(expChildServisler.getStSurucuAdi());
        textView_StSurucuGSM.setText(expChildServisler.getStSurucuGSM());
        textView_StServisPlaka.setText(expChildServisler.getStServisPlaka());

        textView_StGuzergahLink.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //Log.wtf("ExpAdapterServisler", Uri.parse("https://www.google.com/maps/dir/?api=1&" + textView_StGuzergahLink.getText().toString()).toString());
                Log.wtf("ExpAdapterServisler", textView_StGuzergahLink.getText().toString());

                Intent i = new Intent(Intent.ACTION_VIEW);
                i.setData(Uri.parse(textView_StGuzergahLink.getText().toString()));
                i.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                context.startActivity(i);

                /*Intent intent = new Intent(android.content.Intent.ACTION_VIEW);
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                intent.setData(Uri.parse("https://www.google.com/maps/dir/?api=1&" + textView_StGuzergahLink.getText().toString()));
                intent.setClassName("com.google.android.apps.maps", "com.google.android.maps.MapsActivity");
                context.startActivity(intent);*/

                /*Intent intent = context.getPackageManager().getLaunchIntentForPackage("com.google.android.apps.maps");
                intent.setAction(Intent.ACTION_VIEW);
                intent.setData(Uri.parse(textView_StGuzergahLink.getText().toString()));
                context.startActivity(intent);*/
            }
        });

        return convertView;
    }

    @Override
    public int getChildrenCount(int groupPosition) {
        ArrayList<ExpChildServisler> chList = groups.get(groupPosition).getItems();
        return chList.size();
    }

    @Override
    public Object getGroup(int groupPosition) {
        return groups.get(groupPosition);
    }

    @Override
    public int getGroupCount() {
        return groups.size();
    }

    @Override
    public long getGroupId(int groupPosition) {
        return groupPosition;
    }

    @Override
    public View getGroupView(int groupPosition, boolean isExpanded, View convertView, ViewGroup parent) {
        ExpGroupServisler group = (ExpGroupServisler) getGroup(groupPosition);

        if (convertView == null) {
            LayoutInflater inf = (LayoutInflater) context.getSystemService(context.LAYOUT_INFLATER_SERVICE);
            convertView = inf.inflate(R.layout.exp_servisler_group_item, null);
        }

        TextView textView_exp_servisler_group = convertView.findViewById(R.id.textView_exp_servisler_group);

        textView_exp_servisler_group.setText(group.getName());

        return convertView;
    }

    @Override
    public boolean hasStableIds() {
        return true;
    }

    @Override
    public boolean isChildSelectable(int groupPosition, int childPosition) {
        return true;
    }

}