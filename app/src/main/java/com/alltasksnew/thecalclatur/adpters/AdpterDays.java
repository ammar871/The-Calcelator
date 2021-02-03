package com.alltasksnew.thecalclatur.adpters;

import android.annotation.SuppressLint;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.alltasksnew.thecalclatur.R;
import com.alltasksnew.thecalclatur.modls.Days;
import com.alltasksnew.thecalclatur.roomdatabase.BaseViewHolder;

import java.util.ArrayList;

public class AdpterDays extends RecyclerView.Adapter<AdpterDays.HolderPracticeAdpter> {

    ArrayList<Days> list;
    Context mcontext;
    private  final OnItemClickListener mlistener;
    public interface OnItemClickListener {
        void onItemClick(int item);
    }

    public AdpterDays(ArrayList<Days> names, Context mcontext, OnItemClickListener mlistener) {
        this.list = names;
        this.mcontext = mcontext;
        this.mlistener = mlistener;



    }

    @NonNull
    @Override
    public HolderPracticeAdpter onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_days, parent, false);
        return new HolderPracticeAdpter(view);

    }

    public String[] mColors = {
            "#4c4e77", "#ffb624", "#9b653e", "#4c4e77", "#3ca59d", "#e84a5f", "#07031a",        //reds
            "#5a3d55"};
    @SuppressLint("SetTextI18n")
    @Override
    public void onBindViewHolder(@NonNull final HolderPracticeAdpter holder, final int position) {


        //  holder.tv_counter.setText(" المشاهدات : "+list.get(position).getCounter()+"");

        holder.tv_name.setText(list.get(position).getNumberDay()+"");

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mlistener.onItemClick(list.get(position).getNumberDay());



            }

        });


    }
    @Override
    public int getItemCount() {
        return list.size();
    }

    public static class HolderPracticeAdpter extends BaseViewHolder {
        TextView tv_name;


        public HolderPracticeAdpter(@NonNull View itemView) {
            super(itemView);
            tv_name = itemView.findViewById(R.id.number_day);

        }

        @Override
        protected void clear() {

        }


    }

}

