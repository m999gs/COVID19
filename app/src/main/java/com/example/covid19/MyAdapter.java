package com.example.covid19;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class MyAdapter extends RecyclerView.Adapter<MyAdapter.ViewHolder> {

    private ArrayList<D> list;
    private Context context;

    public MyAdapter(ArrayList<D> list, Context context) {
        this.list = list;
        this.context = context;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.covidcases,parent,false);

        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        D temp = list.get(position);

        holder.CountryName.setText(temp.getCountry());
        holder.ConfirmedCases.setText(temp.getConfirmed());
        holder.RecoveredCases.setText(temp.getRecovered());
        holder.DeathCases.setText(temp.getDeath());
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder{

        public TextView CountryName;
        public TextView ConfirmedCases;
        public TextView RecoveredCases;
        public TextView DeathCases;


        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            CountryName = itemView.findViewById(R.id.cname);
            ConfirmedCases = itemView.findViewById(R.id.confirmedCases);
            RecoveredCases = itemView.findViewById(R.id.recoveredCases);
            DeathCases = itemView.findViewById(R.id.deathCases);
        }
    }
}
