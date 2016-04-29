package com.simicity.simi.todoline;

import android.util.Log;

/**
 * Created by simi on 2/11/16.
 */
public class TodoItem {
    private int _id;
    private String task;
    private String memo;
    private int time;
    private int done;

    public void set_id(int _id) {
        this._id = _id;
    }

    public int get_id() {
        return _id;
    }

    public void setTask(String task) {
        this.task = task;
    }

    public String getTask() {
        return task;
    }

    public void setMemo(String memo) {
        this.memo = memo;
    }

    public String getMemo() {
        return memo;
    }

    public void setTime(int time) {
        this.time = time;
    }

    public int getTime() {
        return time;
    }

    public void setDone(int done) {
        this.done = done;
    }

    public int getDone() {
        return done;
    }
}
