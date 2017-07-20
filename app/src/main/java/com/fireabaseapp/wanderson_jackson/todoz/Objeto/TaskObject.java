package com.fireabaseapp.wanderson_jackson.todoz.Objeto;

/**
 * Created by wjackson on 7/15/2017.
 */

public class TaskObject {
    private String key;
    private String task;
    //private String date;                                                                          //TODO: 7/20/2017 add date to content


    public TaskObject() {
    }

    public TaskObject(String key, String task) {
        this.key = key;
        this.task = task;
    }

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }

    public String getTask() {
        return task;
    }

    public void setTask(String task) {
        this.task = task;
    }
}
