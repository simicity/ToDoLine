package com.simicity.simi.todoline;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.ContextMenu;
import android.view.MenuItem;
import android.view.View;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ListView;

import java.util.ArrayList;
import java.util.List;

import uk.co.chrisjenx.calligraphy.CalligraphyContextWrapper;

/**
 * Created by simi on 4/28/16.
 */
public class ListMain extends AppCompatActivity {
    static final int MENU_DELETE = 0;
    static final int MENU_DELETEALL = 1;

    private ListAdapter list;
    private TodoAdapter todo;
    private TodoLineListAdapter listRowAdapter;
    private List<ListItem> listItems = new ArrayList<ListItem>();
    private List<TodoItem> todoItems = new ArrayList<TodoItem>();
    private ListView lineListView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);
        setContentView(R.layout.list_main);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.listfab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final EditText editView = new EditText(ListMain.this);
                new AlertDialog.Builder(ListMain.this)
                        .setTitle("Input the list name")
                        .setView(editView, 10, 20, 10, 20)
                        .setPositiveButton("Add", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int whichButton) {
                                list.insertList(editView.getText().toString());
                                loadList();
                            }
                        })
                        .setNegativeButton("Cancel", null)
                        .show();
            }
        });

        list = new ListAdapter(this);
        todo = new TodoAdapter(this);
        listRowAdapter = new TodoLineListAdapter(this, 0, listItems);
        lineListView = (ListView)findViewById(R.id.listListView);
        lineListView.setAdapter(listRowAdapter);
        lineListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                ListItem list_detail = listItems.get(position);
                Intent intent = new Intent(ListMain.this, TodoMain.class);
                intent.putExtra("_list_id", list_detail.get_list_id());
                startActivity(intent);
            }
        });

        loadList();

        setMenuDialog();
    }

    protected void loadList() {
        listItems.clear();
        todoItems.clear();

        Cursor c = list.getAllListList();
        if(c.moveToFirst()) {
            do {
                ListItem listItem = new ListItem();
                listItem.set_list_id(c.getInt(c.getColumnIndex("_list_id")));
                listItem.setList_name(c.getString(c.getColumnIndex("list_name")));
                listItems.add(listItem);
            } while(c.moveToNext());
        }

        c = list.getAllList();
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

        listRowAdapter.notifyDataSetChanged();
        lineListView.invalidateViews();
    }

    protected void setMenuDialog() {
        lineListView.setOnCreateContextMenuListener(new View.OnCreateContextMenuListener() {
            @Override
            public void onCreateContextMenu(ContextMenu menu, View v, ContextMenu.ContextMenuInfo menuInfo) {
                menu.add(0, MENU_DELETE, 0, "Delete");
                menu.add(0, MENU_DELETEALL, 0, "Delete All");
            }
        });
    }

    @Override
    public boolean onContextItemSelected(MenuItem item) {
        AdapterView.AdapterContextMenuInfo menuInfo = (AdapterView.AdapterContextMenuInfo)item.getMenuInfo();
        final ListItem list_detail = listItems.get(menuInfo.position);

        switch(item.getItemId()) {
            case MENU_DELETE:
                new AlertDialog.Builder(this)
                        .setMessage("Sure?")
                        .setPositiveButton("Yes",
                                new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {
                                        list.deleteList(list_detail.get_list_id());
                                        todo.selectivedeleteList(list_detail.get_list_id());
                                        loadList();
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
                                        list.deleteAllList();
                                        todo.deleteAll();
                                        loadList();
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
        loadList();

        lineListView = (ListView)findViewById(R.id.listListView);
        TodoLineListAdapter listRowAdapter = new TodoLineListAdapter(this, 0, listItems);
        lineListView.setAdapter(listRowAdapter);

        super.onRestart();
    }

    @Override
    protected void attachBaseContext(Context newBase) {
        super.attachBaseContext(CalligraphyContextWrapper.wrap(newBase));
    }
}
