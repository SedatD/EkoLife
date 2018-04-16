package agency.vavien.ekolife.adapters;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.TextView;

import java.util.HashMap;
import java.util.List;

import agency.vavien.ekolife.R;

/**
 * Created by SD on 20.12.2017.
 * dilmacsedat@gmail.com
 * :)
 */

public class FragmentServisSizAdapter extends BaseExpandableListAdapter {
    private List<String> list_parent;
    private HashMap<String, List<String>> list_child;
    private Context context;
    private TextView textView_exp_servisler_group;
    private TextView textView_vardiya,textView_servis,textView_surucu,textView_surucuGSM,textView_servisPlaka;
    private LayoutInflater inflater;

    public FragmentServisSizAdapter(Context context, List<String> list_parent, HashMap<String, List<String>> list_child) {
        this.context = context;
        this.list_parent = list_parent;
        this.list_child = list_child;
    }

    @Override
    public int getGroupCount() {
        return list_parent.size();
    }

    @Override
    public int getChildrenCount(int groupPosition) {
        return list_child.get(list_parent.get(groupPosition)).size();
    }

    @Override
    public Object getGroup(int groupPosition) {
        return list_parent.get(groupPosition);
    }

    @Override
    public Object getChild(int groupPosition, int childPosition) {
        return list_child.get(list_parent.get(groupPosition)).get(childPosition);

    }

    @Override
    public long getGroupId(int groupPosition) {
        return groupPosition;
    }

    @Override
    public long getChildId(int groupPosition, int childPosition) {
        return childPosition;
    }

    @Override
    public boolean hasStableIds() {
        return false;
    }

    @Override
    public boolean isChildSelectable(int groupPosition, int childPosition) {
        return true;
    }

    @Override
    public View getGroupView(int groupPosition, boolean isExpanded, View view, ViewGroup parent) {
        String title_name = (String) getGroup(groupPosition);
        if (view == null) {
            inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            view = inflater.inflate(R.layout.exp_siz_group_item, null);
        }
        textView_exp_servisler_group = view.findViewById(R.id.textView_exp_siz_group);
        textView_exp_servisler_group.setText(title_name);
        return view;
    }

    @Override
    public View getChildView(int groupPosition, int childPosition, boolean isLastChild, View view, ViewGroup parent) {

        if (view == null) {
            inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            view = inflater.inflate(R.layout.exp_siz_child_item, null);
        }

        String txt_child_name = (String) getChild(groupPosition, childPosition);
        Log.wtf("asdasd","" + getChild(groupPosition, childPosition));

        textView_vardiya = view.findViewById(R.id.textView_vardiya);
        textView_vardiya.setText(txt_child_name);

        /*if (getGroup(groupPosition).toString().equals("GALATASARAY")) {
            view.setBackgroundColor(Color.RED);
        } else if (getGroup(groupPosition).toString().equals("FENERBAHCE")) {
            view.setBackgroundColor(Color.BLUE);
        }*/

        return view;
    }

}