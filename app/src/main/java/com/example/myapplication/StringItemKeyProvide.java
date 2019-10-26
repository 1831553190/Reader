package com.example.myapplication;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.selection.ItemKeyProvider;

import java.util.List;

public class StringItemKeyProvide extends ItemKeyProvider<String> {

    List<String> items;



    protected StringItemKeyProvide(int scope,List<String> items) {
        super(scope);
        this.items=items;
    }


    @Nullable
    @Override
    public String getKey(int position) {
        return items.get(position);
    }

    @Override
    public int getPosition(@NonNull String key) {
        return items.indexOf(key);
    }
}
