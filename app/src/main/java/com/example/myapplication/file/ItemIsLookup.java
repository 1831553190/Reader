package com.example.myapplication.file;

import android.view.MotionEvent;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.selection.ItemDetailsLookup;
import androidx.recyclerview.widget.RecyclerView;

import com.example.myapplication.adapter.IAdapter;

public class ItemIsLookup extends ItemDetailsLookup{
    RecyclerView recyclerView;

    public ItemIsLookup(RecyclerView recyclerView) {
        this.recyclerView = recyclerView;
    }

    @Nullable
    @Override
    public ItemDetails getItemDetails(@NonNull MotionEvent e) {
        View view = recyclerView.findChildViewUnder(e.getX(), e.getY());
        if (view != null) {

            RecyclerView.ViewHolder holder = recyclerView.getChildViewHolder(view);
            if (holder instanceof IAdapter.ViewHolder) {
                return ((IAdapter.ViewHolder) holder).getViewHolderItemDetails();
            }
        }
        return null;
    }
}






//        ItemDetailsLookup<Long> {
//
//    RecyclerView recyclerView;
//
//    public ItemIsLookup(RecyclerView recyclerView) {
//        this.recyclerView = recyclerView;
//    }
//
//
//
//    @Nullable
//    @Override
//    public ItemDetails<Long> getItemDetails(@NonNull MotionEvent e) {
//        View view=recyclerView.findChildViewUnder(e.getX(),e.getY());
//        if (view!=null){
//            return ((IAdapter.ViewHolder)recyclerView.getChildViewHolder(view)).getViewHolderItemDetails();
//        }
//        return null;
//    }
//}
