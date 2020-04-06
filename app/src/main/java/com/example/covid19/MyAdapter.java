package com.example.covid19;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
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
        String s=temp.getCode().toLowerCase();
        if(s.equals("do"))s="doo";
        int id = context.getResources().getIdentifier(s,"drawable",context.getPackageName());
        holder.CountryFlag.setImageResource(id);
        holder.CountryName.setText(temp.getCountry());
        holder.ConfirmedCases.setText(temp.getConfirmed()+"");
        holder.RecoveredCases.setText(temp.getRecovered()+"");
        holder.DeathCases.setText(temp.getDeath()+"");
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder{

        TextView CountryName;
        TextView ConfirmedCases;
        TextView RecoveredCases;
        TextView DeathCases;
        ImageView CountryFlag;



        ViewHolder(@NonNull View itemView) {
            super(itemView);
            CountryFlag = itemView.findViewById(R.id.cFlag);
            CountryName = itemView.findViewById(R.id.cname);
            ConfirmedCases = itemView.findViewById(R.id.confirmedCases);
            RecoveredCases = itemView.findViewById(R.id.recoveredCases);
            DeathCases = itemView.findViewById(R.id.deathCases);
        }
    }
}
