package com.assignment4.priti.knowyourgovernment;

import android.support.v7.widget.RecyclerView;
import android.view.View;

public class RecyclerItemClickListener {

    public interface OnRecyclerClickListener extends RecyclerView.OnItemTouchListener{
         void onItemClick(View view, int position);

         void onLongItemClick(View view, int position);
    }
}
