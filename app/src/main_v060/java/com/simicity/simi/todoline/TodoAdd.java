package com.simicity.simi.todoline;

import android.app.TimePickerDialog;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.TimePicker;

import java.util.Calendar;

import uk.co.chrisjenx.calligraphy.CalligraphyContextWrapper;

/**
 * Created by simi on 2/11/16.
 */
public class TodoAdd extends AppCompatActivity {
    private TodoAdapter todo;
    private AlertDialog.Builder alrt;
    private Button btn_back;
    private Button btn_save;
    private EditText edit_task;
    private EditText edit_memo;
    private TextView edit_time;
    private int tmp_id, tmp_time, tmp_done;
    InputMethodManager inputMethodManager;
    private RelativeLayout relativeLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.todo_add);

        Intent intent = getIntent();
        tmp_id = intent.getIntExtra("_id", -1);

        alrt = new AlertDialog.Builder(this);
        todo = new TodoAdapter(this);

        btn_back = (Button)findViewById(R.id.btn_back);
        btn_save = (Button)findViewById(R.id.btn_save);
        edit_task = (EditText)findViewById(R.id.edit_task);
        edit_memo = (EditText)findViewById(R.id.edit_memo);
        edit_time = (TextView)findViewById(R.id.edit_time);

        inputMethodManager = (InputMethodManager)getSystemService(Context.INPUT_METHOD_SERVICE);
        relativeLayout = (RelativeLayout)findViewById(R.id.add_layout);

        if(tmp_id != -1) {
            Cursor c = todo.getList(tmp_id);
            if(c.moveToFirst()) {
                edit_task.setText(c.getString(c.getColumnIndex("task")));
                edit_memo.setText(c.getString(c.getColumnIndex("memo")));
                tmp_time = c.getInt(c.getColumnIndex("time"));
                edit_time.setText(String.format("%02d", tmp_time / 100) + " : " + String.format("%02d", tmp_time%100));
                tmp_done = c.getInt(c.getColumnIndex("done"));
            }
        }else{
            Calendar calendar = Calendar.getInstance();
            int hour = calendar.get(Calendar.HOUR_OF_DAY);
            int minute = calendar.get(Calendar.MINUTE);
            tmp_time = hour * 100 + minute;
            edit_time.setText(String.format("%02d", hour) + " : " + String.format("%02d", minute));
        }

        edit_time.setOnClickListener(new View.OnClickListener() {
            Calendar calendar = Calendar.getInstance();
            int hour = calendar.get(Calendar.HOUR_OF_DAY);
            int minute = calendar.get(Calendar.MINUTE);

            @Override
            public void onClick(View v) {
                TimePickerDialog dialog = new TimePickerDialog(
                        TodoAdd.this,
                        R.style.MyPickerDialogTheme,
                        new TimePickerDialog.OnTimeSetListener(){
                            @Override
                            public void onTimeSet(TimePicker view, int picked_hour,int picked_minute) {
                                edit_time.setText(String.format("%02d", picked_hour) + " : " + String.format("%02d", picked_minute));
                                tmp_time = picked_hour * 100 + picked_minute;
                            }
                        },
                        hour,minute,true);
                dialog.setTitle("Select Time");
                dialog.show();
            }
        });

        btn_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        btn_save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
               if(edit_task.getText().toString().matches("")) {
                   alrt.setMessage("A value is required for \"Task\" field.")
                           .setPositiveButton("OK", null)
                           .show();
               } else {
                   if(tmp_id == -1) {
                       todo.insert(edit_task.getText().toString(), edit_memo.getText().toString(), tmp_time);
                       finish();
                   } else {
                       todo.update(tmp_id, edit_task.getText().toString(), edit_memo.getText().toString(), tmp_time, tmp_done);
                       finish();
                   }
               }
            }
        });
    }

    @Override
    protected void attachBaseContext(Context newBase) {
        super.attachBaseContext(CalligraphyContextWrapper.wrap(newBase));
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        inputMethodManager.hideSoftInputFromWindow(relativeLayout.getWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);
        relativeLayout.requestFocus();
        return true;
    }
}
