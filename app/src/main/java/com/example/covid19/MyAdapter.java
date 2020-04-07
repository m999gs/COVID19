package com.example.covid19;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class MyAdapter extends RecyclerView.Adapter<MyAdapter.ViewHolder> {

    private ArrayList<D> list;
    private Context context;

    MyAdapter(ArrayList<D> list, Context context) {
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
        final D temp = list.get(position);
        String s=temp.getCode().toLowerCase();
        if(s.equals("do"))s="doo";
        final int id = context.getResources().getIdentifier(s,"drawable",context.getPackageName());
        holder.CountryFlag.setImageResource(id);
        holder.CountryName.setText(temp.getCountry());
        holder.ConfirmedCases.setText(temp.getConfirmed()+"");
        holder.RecoveredCases.setText(temp.getRecovered()+"");
        holder.DeathCases.setText(temp.getDeath()+"");

        holder.linearLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Put the intent to the other activity
                Intent i = new Intent(v.getContext(),CountryWise.class);
                i.putExtra("flag",id);
                i.putExtra("countryname",temp.getCountry());
                i.putExtra("confirmed",temp.getConfirmed());
                i.putExtra("recovered",temp.getRecovered());
                i.putExtra("Death",temp.getDeath());
                i.putExtra("longitude",temp.getLongitude());
                i.putExtra("latitude",temp.getLatitude());
                v.getContext().startActivity(i);
                Toast.makeText(context,"You clicked "+temp.getCountry(),Toast.LENGTH_SHORT).show();
            }
        });

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
        LinearLayout linearLayout;



        ViewHolder(@NonNull View itemView) {
            super(itemView);
            CountryFlag = itemView.findViewById(R.id.cFlag);
            CountryName = itemView.findViewById(R.id.cname);
            ConfirmedCases = itemView.findViewById(R.id.confirmedCases);
            RecoveredCases = itemView.findViewById(R.id.recoveredCases);
            DeathCases = itemView.findViewById(R.id.deathCases);
            linearLayout = itemView.findViewById(R.id.recycleList);
        }
    }
}
