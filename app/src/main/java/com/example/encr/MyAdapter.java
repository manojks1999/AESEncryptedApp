package com.example.encr;


import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;



class MyViewHolder extends RecyclerView.ViewHolder{

    TextView textName,textMsg;
    public MyViewHolder(@NonNull View itemView){
        super(itemView);
        textName=itemView.findViewById(R.id.textViewName);
        textMsg=itemView.findViewById(R.id.textViewMsg);
    }
}