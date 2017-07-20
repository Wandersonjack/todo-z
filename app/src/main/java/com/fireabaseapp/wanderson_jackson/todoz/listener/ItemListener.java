package com.fireabaseapp.wanderson_jackson.todoz.listener;

import com.fireabaseapp.wanderson_jackson.todoz.Objeto.TaskObject;

/**
 * Created by wjackson on 7/19/2017.
 */

public interface ItemListener {
    void onDelete(TaskObject object);
    void onOpenDialog(TaskObject taskObject);
}