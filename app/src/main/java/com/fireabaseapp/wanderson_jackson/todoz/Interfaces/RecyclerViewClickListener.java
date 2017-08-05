package com.fireabaseapp.wanderson_jackson.todoz.Interfaces;

import android.view.View;

/**
 * Created by eduardo on 05/08/17.
 */

public interface RecyclerViewClickListener {
    public void OnClickListener(View view, int position);

    //You can use or not OnLongClick
    public void OnLongClickListener(View view, int position);
}
