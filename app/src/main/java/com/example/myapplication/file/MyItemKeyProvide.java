package com.example.myapplication.file;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.selection.ItemKeyProvider;

import com.example.myapplication.model.Data;

import java.util.List;

public class MyItemKeyProvide extends ItemKeyProvider {

    List<Data> list;
     /**
     * Creates a new provider with the given scope.
     *
     * @param scope Scope can't be changed at runtime.
     */
     public MyItemKeyProvide(int scope, List<Data> list) {
        super(scope);
        this.list=list;
    }

    @Nullable
    @Override
    public Object getKey(int position) {
        return list.get(position);
    }

    @Override
    public int getPosition(@NonNull Object key) {
        return list.indexOf(key);
    }
}
