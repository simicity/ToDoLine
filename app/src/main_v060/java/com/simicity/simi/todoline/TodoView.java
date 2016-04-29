package com.simicity.simi.todoline;

import android.content.Context;
import android.graphics.Color;
import android.util.Log;
import android.widget.Checkable;
import android.widget.GridLayout;
import android.widget.RadioButton;
import android.widget.TextView;

/**
 * Created by simi on 4/23/16.
 */
public class TodoView extends GridLayout implements Checkable{
    private TodoAdapter todo;
    private TodoItem item;
    private TextView[] textView = new TextView[3];
    private RadioButton radioButton;
    private boolean tmp_done, check_pattern;

    public TodoView(Context context) {
        super(context);
        addView(inflate(getContext(), R.layout.row, null));
        todo = new TodoAdapter(getContext());
        radioButton = (RadioButton) findViewById(R.id.todo_radio);
        textView[0] = (TextView) findViewById(R.id.todo_task);
        textView[1] = (TextView) findViewById(R.id.todo_memo);
        textView[2] = (TextView) findViewById(R.id.todo_time);
    }

    public void setTodoView(TodoItem item) {
        this.item = item;
        int hour = item.getTime()/100;
        int minute = item.getTime()%100;

        textView[0].setText(String.valueOf(item.getTask()));
        textView[1].setText(String.valueOf(item.getMemo()));
        textView[2].setText(String.format("%02d:%02d", hour, minute));

        if(item.getDone() == 0) {
            tmp_done = check_pattern = false;
        } else {
            tmp_done = check_pattern = true;
        }
    }

    @Override
    public void setChecked(boolean checked) {
        if((check_pattern && tmp_done != checked) || (!check_pattern && tmp_done == checked)) {
            radioButton.setChecked(tmp_done);

            if (tmp_done) {
                item.setDone(1);
                textView[0].setTextColor(Color.parseColor("#c0c0c0"));
                textView[1].setTextColor(Color.parseColor("#c0c0c0"));
                textView[2].setTextColor(Color.parseColor("#c0c0c0"));
            } else {
                item.setDone(0);
                textView[0].setTextColor(Color.parseColor("#181619"));
                textView[1].setTextColor(Color.parseColor("#181619"));
                textView[2].setTextColor(Color.parseColor("#181619"));
            }

            todo.update(item.get_id(), item.getTask(), item.getMemo(), item.getTime(), item.getDone());
            tmp_done = !tmp_done;
        }
    }

    @Override
    public boolean isChecked() {
        return tmp_done;
    }

    @Override
    public void toggle() {
    }
}
