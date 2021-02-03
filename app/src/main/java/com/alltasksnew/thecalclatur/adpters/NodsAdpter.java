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
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;

import com.alltasksnew.thecalclatur.R;
import com.alltasksnew.thecalclatur.roomdatabase.AppDatabase;
import com.alltasksnew.thecalclatur.roomdatabase.BaseViewHolder;
import com.alltasksnew.thecalclatur.roomdatabase.ModelRoom;
import com.alltasksnew.thecalclatur.screens.UpdateNodsActivity;

import java.util.ArrayList;
import java.util.List;

public class NodsAdpter extends RecyclerView.Adapter<NodsAdpter.HolderPracticeAdpter> {

    ArrayList<ModelRoom> list;
    private final OnItemClickListener mlistener;
    public interface OnItemClickListener {
        void onItemClick(ModelRoom item);
    }
    Context mcontext;

    public ArrayList<ModelRoom> getList() {
        return list;
    }

    public void setList(ArrayList<ModelRoom> list) {
        this.list = list;
    }
    private Callback mCallback ;
    public void setmCallback(Callback mCallback) {
        this.mCallback = mCallback;
    }
    public Callback getmCallback() {
        return mCallback;
    }



    AppDatabase database = AppDatabase.getDatabaseInstance(mcontext);


    public NodsAdpter(ArrayList<ModelRoom> names, Context mcontext ,OnItemClickListener listener) {
        this.list = names;
        this.mcontext = mcontext;
        this.mlistener=listener;

    }

    @NonNull
    @Override
    public HolderPracticeAdpter onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_nods, parent, false);
        return new HolderPracticeAdpter(view);

    }

    public String[] mColors = {
            "#4c4e77", "#ffb624", "#9b653e", "#4c4e77", "#3ca59d", "#e84a5f", "#07031a",        //reds
            "#5a3d55"};
    @SuppressLint("SetTextI18n")
    @Override
    public void onBindViewHolder(@NonNull final HolderPracticeAdpter holder, final int position) {


        //  holder.tv_counter.setText(" المشاهدات : "+list.get(position).getCounter()+"");
        getColorbacrond(holder.cardView,list.get(position).getColor());
        holder.textView.setText(list.get(position).getInput());
        holder.tv_date.setText(list.get(position).getTimes()+" / "+list.get(position).getDates());


        holder.img_delet.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                mCallback.onDeleteClick(list.get(position));
                holder.onBind(position);

            }
        });
        holder.img_update.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent myintent=new Intent(mcontext, UpdateNodsActivity.class);
                myintent.putExtra("modelNods",list.get(position));
                mcontext.startActivity(myintent);

            }
        });
        holder.imglesen.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                mlistener.onItemClick(list.get(position));

            }
        });
        //firebase

    }

    private void getColorbacrond(CardView cardView, int color) {
        if(color==0){
            cardView.setCardBackgroundColor(ContextCompat.getColor(mcontext, R.color.violet));

        }else   if(color==1){
            cardView.setCardBackgroundColor(ContextCompat.getColor(mcontext, R.color.bluelighit));

        }else   if(color==2){
            cardView.setCardBackgroundColor(ContextCompat.getColor(mcontext, R.color.brawn));

        }
        else   if(color==3){
            cardView.setCardBackgroundColor(ContextCompat.getColor(mcontext, R.color.yallow));

        }
    }

    public interface Callback {
        void onDeleteClick(ModelRoom mUser);
    }

    public void addItems(List<ModelRoom> userList) {
        list = (ArrayList<ModelRoom>) userList;
        notifyDataSetChanged();

    }


    @Override
    public int getItemCount() {
        return list.size();
    }

    public static class HolderPracticeAdpter extends BaseViewHolder {
        TextView textView ,tv_date;
        ImageView img_update,imglesen,img_delet;
        CardView cardView;
        LinearLayout linearLayout;

        public HolderPracticeAdpter(@NonNull View itemView) {
            super(itemView);
            textView = itemView.findViewById(R.id.tv_item);
            img_update = itemView.findViewById(R.id.img_update);
            cardView = itemView.findViewById(R.id.carditem);
            tv_date = itemView.findViewById(R.id.tv_date);
            imglesen = itemView.findViewById(R.id.img_lestion);
            img_delet = itemView.findViewById(R.id.img_delete);


        }

        @Override
        protected void clear() {

        }


    }

}

