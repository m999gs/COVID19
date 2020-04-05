package com.example.covid19;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import java.util.ArrayList;
import java.util.List;

public class customAdapter extends ArrayAdapter<D> {

    private Context mContext;
    private int mResource;
    public customAdapter(@NonNull Context context, int resource, @NonNull ArrayList<D> objects) {
        super(context, resource, objects);
        mContext=context;
        mResource=resource;
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        String cn,cc,rc,dc;

        cn= getItem(position).getCountry();
        cc= getItem(position).getConfirmed();
        rc= getItem(position).getRecovered();
        dc= getItem(position).getDeath();

        D d=new D(cn,cc,rc,dc);

        LayoutInflater layoutInflater = LayoutInflater.from(mContext);
        convertView = layoutInflater.inflate(mResource,parent,false);
        ImageView flag = convertView.findViewById(R.id.cFlag);
        TextView cname = convertView.findViewById(R.id.cname);
        TextView confirmedCases = convertView.findViewById(R.id.confirmedCases);
        TextView recoverdcases = convertView.findViewById(R.id.recoveredCases);
        TextView deaths = convertView.findViewById(R.id.deathCases);

        cname.setText(cn);
        confirmedCases.setText(cc);
        recoverdcases.setText(rc);
        deaths.setText(dc);

        return convertView;
    }
}
