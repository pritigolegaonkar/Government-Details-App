package com.assignment4.priti.knowyourgovernment;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;

/**
 * Created by  priti on 11/3/2018
 */
public class OfficialDetailsAdapter extends RecyclerView.Adapter<recyclerViewHolder> {

    private MainActivity mainActivity;
    private ArrayList<OfficialDetails> officialDetailsArrayList;
    private static final String TAG = "Adapter";


    public OfficialDetailsAdapter(MainActivity activity, ArrayList<OfficialDetails> details){
        mainActivity = activity;
        officialDetailsArrayList = details;
    }


    @Override
    public int getItemCount() {
        return officialDetailsArrayList.size();
    }

    @NonNull
    @Override
    public recyclerViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {

        Log.d(TAG, "onCreateViewHolder: " + i + " " +viewGroup);

        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.recycler_view_layout, viewGroup, false);
        return new recyclerViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull recyclerViewHolder holder, final int i) {

        OfficialDetails officialDetails = officialDetailsArrayList.get(i);
        Log.d(TAG, "onBindViewHolder: " + i + " " +officialDetails.getPersonName());
        holder.personName.setText(officialDetails.getPersonName());
        holder.officeName.setText(officialDetails.getOfficeName());
        holder.partyName.setText("(" + officialDetails.getPartyName() + ")");
        holder.item.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mainActivity.onItemClick(v,i);
            }
        });
        holder.item.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                mainActivity.onLongItemClick(v,i);
                return false;
            }
        });

    }

    @Override
    public int getItemViewType(int position) {
        return R.layout.recycler_view_layout;
    }
}
