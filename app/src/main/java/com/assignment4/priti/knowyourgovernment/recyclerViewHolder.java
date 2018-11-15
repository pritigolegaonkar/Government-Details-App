package com.assignment4.priti.knowyourgovernment;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

/**
 * Created by  priti on 11/3/2018
 */
public class recyclerViewHolder extends RecyclerView.ViewHolder {
    public TextView personName;
    public TextView officeName;
    public TextView partyName;
    public View item;


    public recyclerViewHolder(View itemView) {
        super(itemView);
        item = itemView;
        personName=itemView.findViewById(R.id.personName);
        officeName=itemView.findViewById(R.id .officeName);
        partyName=itemView.findViewById(R.id.partyName);
    }


}

