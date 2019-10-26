package com.example.myapplication;

import android.view.MotionEvent;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.selection.ItemDetailsLookup;
import androidx.recyclerview.widget.RecyclerView;

public class StringItemDetailsLookup extends ItemDetailsLookup {

    RecyclerView recyclerView;


    public StringItemDetailsLookup(RecyclerView recyclerView){
        this.recyclerView=recyclerView;
    }

    @Nullable
    @Override
    public ItemDetails getItemDetails(@NonNull MotionEvent e) {
        View view=recyclerView.findChildViewUnder(e.getX(),e.getY());
        if (view!=null){
            RecyclerView.ViewHolder holder=recyclerView.getChildViewHolder(view);

        }
        return null;
    }
}
