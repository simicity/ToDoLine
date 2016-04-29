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
import android.util.Log;
import android.view.ContextMenu;
import android.view.KeyEvent;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ImageView;
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

    private TodoAdapter todoAdapter;
    private ListAdapter listAdapter;
    private TodoLineAdapter rowAdapter;
    private List<TodoItem> todoItems = new ArrayList<TodoItem>();
    private List<ListItem> listItems = new ArrayList<ListItem>();
    private ListView listview;
    private EditText edit_title;
    private ImageView image_tolist;
    InputMethodManager inputMethodManager;
    private CoordinatorLayout coordinatorLayout;
    private int tmp_list_id;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);
        setContentView(R.layout.todo_main);

        Intent intent = getIntent();
        tmp_list_id = intent.getIntExtra("_list_id", -1);

        inputMethodManager = (InputMethodManager)getSystemService(Context.INPUT_METHOD_SERVICE);
        coordinatorLayout = (CoordinatorLayout)findViewById(R.id.main_layout);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(TodoMain.this, TodoAdd.class);
                intent.putExtra("_list_id", tmp_list_id);
                startActivity(intent);
            }
        });

        todoAdapter = new TodoAdapter(this);
        listAdapter = new ListAdapter(this);
        rowAdapter = new TodoLineAdapter(this, 0, todoItems);

        listview = (ListView)findViewById(R.id.listView);
        listview.setAdapter(rowAdapter);
        listview.setChoiceMode(AbsListView.CHOICE_MODE_MULTIPLE);

        edit_title = (EditText)findViewById(R.id.edit_title);
        image_tolist = (ImageView)findViewById(R.id.img_list);
        image_tolist.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ListItem listItem = new ListItem();
                listAdapter.updateList(listItem.get_list_id(), listItem.getList_name());
                Intent intent = new Intent(TodoMain.this, ListMain.class);
                startActivity(intent);
            }
        });

        Cursor c = listAdapter.getListList(tmp_list_id);
        if(c.moveToFirst()) {
            do {
                edit_title.setText(c.getString(c.getColumnIndex("list_name")));
            } while(c.moveToNext());
        }

        edit_title.setOnKeyListener(new View.OnKeyListener() {
            @Override
            public boolean onKey(View v, int keyCode, KeyEvent event) {
                if ((event.getAction() == KeyEvent.ACTION_DOWN) && (keyCode == KeyEvent.KEYCODE_ENTER)) {
                    inputMethodManager.hideSoftInputFromWindow(edit_title.getWindowToken(), InputMethodManager.RESULT_UNCHANGED_SHOWN);
                    listAdapter.updateList(tmp_list_id, edit_title.getText().toString());
                    edit_title.setText(edit_title.getText().toString());
                    return true;
                }
                return false;
            }
        });

        loadTodo();

        setMenuDialog();
    }

    protected void loadTodo() {
        todoItems.clear();
        listItems.clear();

        Comparator<TodoItem> itemComparator = new Comparator<TodoItem>() {
            @Override
            public int compare(TodoItem todo1, TodoItem todo2) {
                Integer obj1 = todo1.getTime();
                Integer obj2 = todo2.getTime();
                return obj1.compareTo(obj2);
            }
        };

        Cursor c = listAdapter.getListList(tmp_list_id);
        if(c.moveToFirst()) {
            do {
                ListItem listItem = new ListItem();
                listItem.set_list_id(c.getInt(c.getColumnIndex("_list_id")));
                listItem.setList_name(c.getString(c.getColumnIndex("list_name")));
                listItems.add(listItem);
            } while(c.moveToNext());
        }

        c = todoAdapter.getAllList(tmp_list_id);
        if(c.moveToFirst()) {
            do {
                TodoItem todoItem = new TodoItem();
                todoItem.set_id(c.getInt(c.getColumnIndex("_id")));
                todoItem.setTask(c.getString(c.getColumnIndex("task")));
                todoItem.setMemo(c.getString(c.getColumnIndex("memo")));
                todoItem.setTime(c.getInt(c.getColumnIndex("time")));
                todoItem.setDone(c.getInt(c.getColumnIndex("done")));
                todoItem.setDone(c.getInt(c.getColumnIndex("list_id")));
                todoItems.add(todoItem);
            } while(c.moveToNext());
        }

        Collections.sort(todoItems, itemComparator);

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
        final TodoItem detail = todoItems.get(menuInfo.position);

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
                                        todoAdapter.delete(detail.get_id());
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
                                        todoAdapter.selectivedelete();
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
                                        todoAdapter.deleteAll();
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
        TodoLineAdapter rowAdapter = new TodoLineAdapter(this, 0, todoItems);
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
