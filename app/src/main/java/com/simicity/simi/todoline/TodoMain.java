package com.simicity.simi.todoline;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.view.ContextMenu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ListView;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import uk.co.chrisjenx.calligraphy.CalligraphyContextWrapper;

public class TodoMain extends AppCompatActivity {
    static final int MENU_EDIT = 0;
    static final int MENU_DELETE = 1;
    static final int MENU_SELECTIVEDELETE = 2;
    static final int MENU_DELETEALL = 3;

    private TodoAdapter todo;
    private TodoLineAdapter rowAdapter;
    private List<TodoItem> list = new ArrayList<TodoItem>();
    private ListView listview;
    private EditText edit_title;
    InputMethodManager inputMethodManager;
    private CoordinatorLayout coordinatorLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);
        setContentView(R.layout.todo_main);

        inputMethodManager = (InputMethodManager)getSystemService(Context.INPUT_METHOD_SERVICE);
        coordinatorLayout = (CoordinatorLayout)findViewById(R.id.main_layout);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(TodoMain.this, TodoAdd.class);
                startActivity(intent);
            }
        });

        edit_title = (EditText)findViewById(R.id.edit_title);
        Calendar calendar = Calendar.getInstance();
        int month = calendar.get(Calendar.MONTH);
        int day = calendar.get(Calendar.DAY_OF_MONTH);
        edit_title.setText(String.format("%d/%d To Do List", month, day));

        todo = new TodoAdapter(this);
        rowAdapter = new TodoLineAdapter(this, 0, list);
        listview = (ListView)findViewById(R.id.listView);

        listview.setAdapter(rowAdapter);
        listview.setChoiceMode(AbsListView.CHOICE_MODE_MULTIPLE);
        loadTodo();

        setMenuDialog();
    }

    protected void loadTodo() {
        list.clear();

        Comparator<TodoItem> itemComparator = new Comparator<TodoItem>() {
            @Override
            public int compare(TodoItem todo1, TodoItem todo2) {
                Integer obj1 = todo1.getTime();
                Integer obj2 = todo2.getTime();
                return obj1.compareTo(obj2);
            }
        };

        Cursor c = todo.getAllList();
        if(c.moveToFirst()) {
            do {
                TodoItem item = new TodoItem();
                item.set_id(c.getInt(c.getColumnIndex("_id")));
                item.setTask(c.getString(c.getColumnIndex("task")));
                item.setMemo(c.getString(c.getColumnIndex("memo")));
                item.setTime(c.getInt(c.getColumnIndex("time")));
                item.setDone(c.getInt(c.getColumnIndex("done")));
                list.add(item);
            } while(c.moveToNext());
        }

        Collections.sort(list, itemComparator);

        rowAdapter.notifyDataSetChanged();
        listview.invalidateViews();
    }

    protected void setMenuDialog() {
        listview.setOnCreateContextMenuListener(new View.OnCreateContextMenuListener() {
            @Override
            public void onCreateContextMenu(ContextMenu menu, View v, ContextMenu.ContextMenuInfo menuInfo) {
                menu.add(0, MENU_EDIT, 0, "Edit");
                menu.add(0, MENU_DELETE, 0, "Delete");
                menu.add(0, MENU_SELECTIVEDELETE, 0, "Delete Selected Task");
                menu.add(0, MENU_DELETEALL, 0, "Delete All");
            }
        });
    }

    @Override
    public boolean onContextItemSelected(MenuItem item) {
        AdapterView.AdapterContextMenuInfo menuInfo = (AdapterView.AdapterContextMenuInfo)item.getMenuInfo();
        final TodoItem detail = list.get(menuInfo.position);

        switch(item.getItemId()) {
            case MENU_EDIT:
                Intent intent = new Intent(TodoMain.this, TodoAdd.class);
                intent.putExtra("_id", detail.get_id());
                startActivity(intent);
                break;

            case MENU_DELETE:
                new AlertDialog.Builder(this)
                        .setMessage("Sure?")
                        .setPositiveButton("Yes",
                                new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {
                                        todo.delete(detail.get_id());
                                        loadTodo();
                                    }
                                })
                        .setNegativeButton("No", null)
                        .show();
                break;
            case MENU_SELECTIVEDELETE:
                new AlertDialog.Builder(this)
                        .setMessage("Sure?")
                        .setPositiveButton("Yes",
                                new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {
                                        todo.selectivedelete();
                                        loadTodo();
                                    }
                                })
                        .setNegativeButton("No", null)
                        .show();
                break;

            case MENU_DELETEALL:
                new AlertDialog.Builder(this)
                        .setMessage("Sure?")
                        .setPositiveButton("Yes",
                                new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {
                                        todo.deleteAll();
                                        loadTodo();
                                    }
                                })
                        .setNegativeButton("No", null)
                        .show();
                break;
        }

        return super.onContextItemSelected(item);
    }

    @Override
    protected void onRestart() {
        loadTodo();

        listview = (ListView)findViewById(R.id.listView);
        TodoLineAdapter rowAdapter = new TodoLineAdapter(this, 0, list);
        listview.setAdapter(rowAdapter);

        super.onRestart();
    }

    @Override
    protected void attachBaseContext(Context newBase) {
        super.attachBaseContext(CalligraphyContextWrapper.wrap(newBase));
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        inputMethodManager.hideSoftInputFromWindow(coordinatorLayout.getWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);
        coordinatorLayout.requestFocus();
        return true;
    }
}
