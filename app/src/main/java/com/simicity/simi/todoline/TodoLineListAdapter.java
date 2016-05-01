package com.simicity.simi.todoline;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.List;

/**
 * Created by simi on 4/28/16.
 */
public class TodoLineListAdapter extends ArrayAdapter<ListItem> {
    private LayoutInflater layoutinflater;

    public TodoLineListAdapter(Context context, int textViewResourceId, List<ListItem> objects) {
        super(context, textViewResourceId, objects);
        layoutinflater = (LayoutInflater)context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if (null == convertView) {
            convertView = layoutinflater.inflate(R.layout.listrow, null);
        }

        ListItem listItem = getItem(position);

        TextView textView = (TextView)convertView.findViewById(R.id.list_listname);
        textView.setText(listItem.getList_name());

        return  convertView;
    }
}
