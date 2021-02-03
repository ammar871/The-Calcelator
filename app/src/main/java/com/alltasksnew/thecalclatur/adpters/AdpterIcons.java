package com.alltasksnew.thecalclatur.adpters;

import android.annotation.SuppressLint;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.alltasksnew.thecalclatur.R;
import com.alltasksnew.thecalclatur.modls.Outaly;
import com.alltasksnew.thecalclatur.roomdatabase.BaseViewHolder;

import java.util.ArrayList;

public class AdpterIcons extends RecyclerView.Adapter<AdpterIcons.HolderPracticeAdpter> {

    ArrayList<Outaly> list;
    Context mcontext;
    private  final OnClickListener mlistener;
    public interface OnClickListener {
        void onClick(Outaly item);
    }

    public AdpterIcons(ArrayList<Outaly> names, Context mcontext, OnClickListener mlistener) {
        this.list = names;
        this.mcontext = mcontext;
        this.mlistener = mlistener;



    }

    @NonNull
    @Override
    public HolderPracticeAdpter onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_imge, parent, false);
        return new HolderPracticeAdpter(view);

    }

    @SuppressLint("SetTextI18n")
    @Override
    public void onBindViewHolder(@NonNull final HolderPracticeAdpter holder, final int position) {


        //  holder.tv_counter.setText(" المشاهدات : "+list.get(position).getCounter()+"");

        holder.img_icon.setImageResource(list.get(position).getIcon());

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mlistener.onClick(list.get(position));



            }

        });


    }
    @Override
    public int getItemCount() {
        return list.size();
    }

    public static class HolderPracticeAdpter extends BaseViewHolder {
        ImageView img_icon;


        public HolderPracticeAdpter(@NonNull View itemView) {
            super(itemView);
            img_icon = itemView.findViewById(R.id.img_icon_item);

        }

        @Override
        protected void clear() {

        }


    }

}

