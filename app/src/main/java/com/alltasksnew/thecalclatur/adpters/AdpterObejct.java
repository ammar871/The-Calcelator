package com.alltasksnew.thecalclatur.adpters;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.recyclerview.widget.RecyclerView;

import com.alltasksnew.thecalclatur.R;
import com.alltasksnew.thecalclatur.roomdatabase.BaseViewHolder;
import com.alltasksnew.thecalclatur.roomdatabase.ModleObjct;
import com.alltasksnew.thecalclatur.screens.AddObjectActiviy;
import com.alltasksnew.thecalclatur.screens.ShowingObjectActivity;

import java.util.ArrayList;
import java.util.List;

public class AdpterObejct extends RecyclerView.Adapter<AdpterObejct.HolderPracticeAdpter> {

    ArrayList<ModleObjct> list;
    Context mcontext;
    private Callback mCallback;

    public void setmCallback(Callback mCallback) {
        this.mCallback = mCallback;
    }

    public Callback getmCallback() {
        return mCallback;
    }

    public AdpterObejct(ArrayList<ModleObjct> names, Context mcontext) {
        this.list = names;
        this.mcontext = mcontext;


    }

    @NonNull
    @Override
    public HolderPracticeAdpter onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_object, parent, false);
        return new HolderPracticeAdpter(view);

    }

    public String[] mColors = {
            "#4c4e77", "#ffb624", "#9b653e", "#4c4e77", "#3ca59d", "#e84a5f", "#07031a",        //reds
            "#5a3d55"};

    @SuppressLint("SetTextI18n")
    @Override
    public void onBindViewHolder(@NonNull final HolderPracticeAdpter holder, final int position) {




        holder.tv_name.setText(list.get(position).getNameObj() + "");

        holder.tv_date.setText(list.get(position).getDare() + "");


        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent intent = new Intent(mcontext, ShowingObjectActivity.class);
                intent.putExtra("content", list.get(position).getContentObj());
                intent.putExtra("name", list.get(position).getNameObj());
                intent.putExtra("filName",list.get(position).getRecordObj());
                mcontext.startActivity(intent);


            }


        });
        holder.delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mCallback.onDeleteClick(list.get(position));
                holder.onBind(position);
            }

        });
        holder.update.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final AlertDialog.Builder alert = new AlertDialog.Builder(mcontext);





                alert.setPositiveButton(R.string.edite, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        Intent intent = new Intent(mcontext, AddObjectActiviy.class);
                        intent.putExtra("content", list.get(position));

                        mcontext.startActivity(intent);

                    }
                });

                alert.setNegativeButton(R.string.no, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int whichButton) {
                        dialog.dismiss();
                    }
                });
                   alert.setMessage(R.string.are_you_sure);
                alert.create().show();
            }

        });

    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public interface Callback {
        void onDeleteClick(ModleObjct mUser);
    }

    public static class HolderPracticeAdpter extends BaseViewHolder {
        TextView tv_name,tv_date;
        ImageView delete, update;


        public HolderPracticeAdpter(@NonNull View itemView) {
            super(itemView);
            tv_name = itemView.findViewById(R.id.ed_tyb);
            delete = itemView.findViewById(R.id.img_delets);
            update = itemView.findViewById(R.id.img_update);
            tv_date = itemView.findViewById(R.id.tv_date);
        }

        @Override
        protected void clear() {

        }


    }

    public void addItems(List<ModleObjct> userList) {
        list = (ArrayList<ModleObjct>) userList;
        notifyDataSetChanged();

    }

}