package com.simicity.simi.todoline;

/**
 * Created by simi on 4/28/16.
 */
public class ListItem {
    private int _list_id;
    private String list_name;

    public int get_list_id() {
        return _list_id;
    }

    public void set_list_id(int _list_id) {
        this._list_id = _list_id;
    }

    public String getList_name() {
        return list_name;
    }

    public void setList_name(String list_name) {
        this.list_name = list_name;
    }
}
