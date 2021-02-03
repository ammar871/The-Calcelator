package com.alltasksnew.thecalclatur.adpters;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.alltasksnew.thecalclatur.R;
import com.alltasksnew.thecalclatur.roomdatabase.BaseViewHolder;
import com.alltasksnew.thecalclatur.roomdatabase.Modle_itemMunth;
import com.alltasksnew.thecalclatur.screens.MenuOutalyActivity;

import java.util.ArrayList;

public class AdpterMunths extends RecyclerView.Adapter<AdpterMunths.HolderPracticeAdpter> {

    ArrayList<Modle_itemMunth> list;
    Context mcontext;


    public AdpterMunths(ArrayList<Modle_itemMunth> names, Context mcontext ) {
        this.list = names;
        this.mcontext = mcontext;


    }

    @NonNull
    @Override
    public HolderPracticeAdpter onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_manth, parent, false);
        return new HolderPracticeAdpter(view);

    }

    public String[] mColors = {
            "#4c4e77", "#ffb624", "#9b653e", "#4c4e77", "#3ca59d", "#e84a5f", "#07031a",        //reds
            "#5a3d55"};
    @SuppressLint("SetTextI18n")
    @Override
    public void onBindViewHolder(@NonNull final HolderPracticeAdpter holder, final int position) {


        //  holder.tv_counter.setText(" المشاهدات : "+list.get(position).getCounter()+"");

        holder.tv_name.setText(list.get(position).getNameMunth());
        holder.tv_number.setText(list.get(position).getNumberMunth());
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent intent =new Intent(mcontext, MenuOutalyActivity.class);
                intent.putExtra("numberMunth",list.get(position).getNumberMunth());
                intent.putExtra("key",list.get(position).getKey());
                mcontext.startActivity(intent);


            }

        });


    }
    @Override
    public int getItemCount() {
        return list.size();
    }

    public static class HolderPracticeAdpter extends BaseViewHolder {
        TextView tv_name, tv_number;
        ImageView img_update,imglesen,img_delet;
        CardView cardView;
        LinearLayout linearLayout;

        public HolderPracticeAdpter(@NonNull View itemView) {
            super(itemView);
            tv_name = itemView.findViewById(R.id.name_m);
//            img_update = itemView.findViewById(R.id.img_update);
//            cardView = itemView.findViewById(R.id.carditem);
            tv_number = itemView.findViewById(R.id.number_m);
//            imglesen = itemView.findViewById(R.id.img_lestion);
//            img_delet = itemView.findViewById(R.id.img_delete);


        }

        @Override
        protected void clear() {

        }


    }

}

