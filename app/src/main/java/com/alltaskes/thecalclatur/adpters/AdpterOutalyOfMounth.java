package com.alltaskes.thecalclatur.adpters;

import android.annotation.SuppressLint;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.alltaskes.thecalclatur.R;
import com.alltaskes.thecalclatur.roomdatabase.AppDatabase;
import com.alltaskes.thecalclatur.roomdatabase.BaseViewHolder;
import com.alltaskes.thecalclatur.roomdatabase.DaysModle;

import java.util.ArrayList;
import java.util.List;

public class AdpterOutalyOfMounth extends RecyclerView.Adapter<AdpterOutalyOfMounth.HolderPracticeAdpter> {

    ArrayList<DaysModle> list;
    Context mcontext;
    private AppDatabase database;
    private Callback mCallback ;
    public void setmCallback(Callback mCallback) {
        this.mCallback = mCallback;
    }
    public Callback getmCallback() {
        return mCallback;
    }

    public AdpterOutalyOfMounth(ArrayList<DaysModle> names, Context mcontext) {
        this.list = names;
        this.mcontext = mcontext;
        database = AppDatabase.getDatabaseInstance(mcontext);


    }

    @NonNull
    @Override
    public HolderPracticeAdpter onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_outaly, parent, false);
        return new HolderPracticeAdpter(view);

    }
    public interface Callback {
        void onDeleteClick(DaysModle mUser);
    }
    public void addItems(List<DaysModle> userList) {
        list = (ArrayList<DaysModle>) userList;
        notifyDataSetChanged();

    }
    public String[] mColors = {
            "#4c4e77", "#ffb624", "#9b653e", "#4c4e77", "#3ca59d", "#e84a5f", "#07031a",        //reds
            "#5a3d55"};

    @SuppressLint("SetTextI18n")
    @Override
    public void onBindViewHolder(@NonNull final HolderPracticeAdpter holder, final int position) {


        //  holder.tv_counter.setText(" المشاهدات : "+list.get(position).getCounter()+"");

        holder.tv_numb.setText(list.get(position).getNubDay() + "");
        holder.tv_outaly.setText(list.get(position).getOutalymony() + "");

        setIcon(list.get(position).getKindOutlay(), holder.icon);
        holder.itemView.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View view) {

                mCallback.onDeleteClick(list.get(position));
                holder.onBind(position);

                return true;
            }
        });


    }

    private void setIcon(int kindOutlay, ImageView icon) {
        switch (kindOutlay) {
            case 0:
                icon.setImageResource(R.drawable.one);
                break;
            case 1:
                icon.setImageResource(R.drawable.two);
                break;
            case 2:
                icon.setImageResource(R.drawable.three);
                break;
            case 3:
                icon.setImageResource(R.drawable.four);
                break;
            case 4:
                icon.setImageResource(R.drawable.five);
                break;
            case 5:
                icon.setImageResource(R.drawable.sex);
                break;
            case 6:
                icon.setImageResource(R.drawable.seven);
                break;
            case 7:
                icon.setImageResource(R.drawable.eatht);
                break;
            case 8:
                icon.setImageResource(R.drawable.nine);
                break;
            case 9:
                icon.setImageResource(R.drawable.ten);
                break;
            case 10:
                icon.setImageResource(R.drawable.elevan);
                break;
            case 11:
                icon.setImageResource(R.drawable.twalf);
                break;


        }
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public static class HolderPracticeAdpter extends BaseViewHolder {
        TextView tv_numb,
                tv_outaly;
        ImageView icon;


        public HolderPracticeAdpter(@NonNull View itemView) {
            super(itemView);
            tv_numb = itemView.findViewById(R.id.tv_numDays);
            tv_outaly = itemView.findViewById(R.id.tv_outaly_item);
            icon = itemView.findViewById(R.id.img_icon_item);

        }

        @Override
        protected void clear() {

        }


    }

}

