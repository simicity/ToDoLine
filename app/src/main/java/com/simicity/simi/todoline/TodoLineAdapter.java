package com.simicity.simi.todoline;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.util.AttributeSet;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Checkable;
import android.widget.CompoundButton;
import android.widget.RadioButton;
import android.widget.TextView;

import java.util.List;

/**
 * Created by simi on 2/11/16.
 */
public class TodoLineAdapter extends ArrayAdapter<TodoItem>{
    public TodoLineAdapter(Context context, int textViewResourceId, List<TodoItem> objects) {
        super(context, textViewResourceId, objects);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if (null == convertView) {
            convertView = new TodoView(getContext());
        }

        TodoItem todoItem = getItem(position);

        TodoView todoView = (TodoView) convertView;
        todoView.setTodoView(todoItem);

        return  convertView;
    }
}
