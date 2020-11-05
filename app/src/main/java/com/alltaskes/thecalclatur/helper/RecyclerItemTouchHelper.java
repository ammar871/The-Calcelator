package com.alltaskes.thecalclatur.helper;

import android.graphics.Canvas;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.RecyclerView;

import com.alltaskes.thecalclatur.adpters.AdpterOutalyOfMounth;


public class RecyclerItemTouchHelper{}
//extends ItemTouchHelper.SimpleCallback {
//    private RecyclrItemTachHelperLisner lisner;
/*
    public RecyclerItemTouchHelper(int dragDirs, int swipeDirs, RecyclrItemTachHelperLisner lisner) {
        super(dragDirs, swipeDirs);
        this.lisner = lisner;
    }

    @Override
    public boolean onMove(@NonNull RecyclerView recyclerView, @NonNull RecyclerView.ViewHolder viewHolder, @NonNull RecyclerView.ViewHolder target) {
        return false;
    }

    @Override
    public void onSwiped(@NonNull RecyclerView.ViewHolder viewHolder, int direction) {
        if (lisner != null)
            lisner.onswieped(viewHolder, direction, viewHolder.getAdapterPosition());

    }

    @Override
    public int convertToAbsoluteDirection(int flags, int layoutDirection) {
        return super.convertToAbsoluteDirection(flags, layoutDirection);
    }

    @Override
    public void clearView(@NonNull RecyclerView recyclerView, @NonNull RecyclerView.ViewHolder viewHolder) {
        if (viewHolder instanceof AdpterOutalyOfMounth.HolderPracticeAdpter) {
            View forbackround = ((AdpterOutalyOfMounth.HolderPracticeAdpter) viewHolder).view_forbackround;
            getDefaultUIUtil().clearView(forbackround);
        } else if (viewHolder instanceof FavoraiteViewHolder) {
            View forbackround = ((FavoraiteViewHolder) viewHolder).view_forbackround;
            getDefaultUIUtil().clearView(forbackround);

        }


    }

    @Override

    public void onChildDraw(@NonNull Canvas c,
                            @NonNull RecyclerView recyclerView, @NonNull RecyclerView.ViewHolder viewHolder,
                            float dX, float dY, int actionState, boolean isCurrentlyActive) {
        if (viewHolder instanceof CardViewHolder){

        View forbackround = ((CardViewHolder) viewHolder).view_forbackround;
        getDefaultUIUtil().onDraw(c, recyclerView, forbackround, dX, dY, actionState, isCurrentlyActive);
    }else if (viewHolder instanceof FavoraiteViewHolder){

            View forbackround = ((FavoraiteViewHolder) viewHolder).view_forbackround;
            getDefaultUIUtil().onDraw(c, recyclerView, forbackround, dX, dY, actionState, isCurrentlyActive);
        }

    }

    @Override
    public void onSelectedChanged(@Nullable RecyclerView.ViewHolder viewHolder, int actionState) {
        if (viewHolder != null) {
            if (viewHolder instanceof CardViewHolder) {
                View forbackround = ((CardViewHolder) viewHolder).view_forbackround;
                getDefaultUIUtil().onSelected(forbackround);
            }else if (viewHolder instanceof FavoraiteViewHolder){

                View forbackround = ((FavoraiteViewHolder) viewHolder).view_forbackround;
                getDefaultUIUtil().onSelected(forbackround);
            }
        }
    }

    @Override
    public void onChildDrawOver(@NonNull Canvas c, @NonNull RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder, float dX, float dY, int actionState, boolean isCurrentlyActive) {
      if (viewHolder instanceof CardViewHolder){

          View forbackround = ((CardViewHolder) viewHolder).view_forbackround;
          getDefaultUIUtil().onDrawOver(c, recyclerView, forbackround, dX, dY, actionState, isCurrentlyActive);
      }else if (viewHolder instanceof FavoraiteViewHolder){
          View forbackround = ((FavoraiteViewHolder) viewHolder).view_forbackround;
          getDefaultUIUtil().onDrawOver(c, recyclerView, forbackround, dX, dY, actionState, isCurrentlyActive);
      }


    }
}*/

