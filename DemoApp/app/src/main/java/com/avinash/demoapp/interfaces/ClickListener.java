package com.avinash.demoapp.interfaces;

import android.view.View;


/**
 * Created by avinash.padalkar on 25/07/2017.
 */
public interface ClickListener {
    public void onClick(View view, int position);

    public void onLongClick(View view, int position);
}
