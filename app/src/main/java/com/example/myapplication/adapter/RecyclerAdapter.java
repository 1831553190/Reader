package com.example.myapplication.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.selection.SelectionTracker;
import androidx.recyclerview.widget.RecyclerView;

import com.example.myapplication.R;
import com.example.myapplication.model.Data;

import java.util.ArrayList;
import java.util.List;

public class RecyclerAdapter extends RecyclerView.Adapter<RecyclerAdapter.ViewHolder> implements Filterable {

    List<Data> list;
    Context context;
    OnItemClickListener onItemClickListener;
    OnItemLongClickListener onItemLongClickListener;
    SelectionTracker selectionTracker;
    List<Data> listSource=new ArrayList<>();;

    public RecyclerAdapter(List<Data> list, Context context) {
        this.list = list;
        listSource.addAll(list);
        this.context = context;
    }

    @Override
    public Filter getFilter() {
        return new Filter() {
            @Override
            protected FilterResults performFiltering(CharSequence constraint) {
                if (constraint.length()==0){
                    list.clear();
                    list.addAll(listSource);
                }else{
//                    List<Data> listFilter=new ArrayList<>();
                    list.clear();
                    for (Data data:listSource){
                        if (data.getTitle().toUpperCase().contains(constraint.toString().toUpperCase())){
                            list.add(data);
                        }
                    }
                }

                FilterResults filterResults = new FilterResults();
                filterResults.values = list;
                return filterResults;
            }

            @Override
            protected void publishResults(CharSequence constraint, FilterResults results) {
                notifyDataSetChanged();
            }
        };
    }


    public interface OnItemLongClickListener{
        void onItemLongClick(View view,int position);
    }

    public interface OnItemClickListener{
        void onItemClick(View view,int position);
    }

    public void setOnItemClickListener(OnItemClickListener onItemClickListener){
        this.onItemClickListener=onItemClickListener;
    }
    public void setOnItemLongClickListener(OnItemLongClickListener onItemLongClickListener){
        this.onItemLongClickListener=onItemLongClickListener;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view= LayoutInflater.from(context).inflate(R.layout.main_item,null);
        ViewHolder viewHolder=new ViewHolder(view);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, final int position) {
        holder.title.setText(list.get(position).getTitle());
        String item=String.valueOf(position);
//        selectionTracker.select(item);
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onItemClickListener.onItemClick(v,position);
            }
        });
        holder.itemView.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                onItemLongClickListener.onItemLongClick(v,position);
                return true;
            }
        });
        holder.cardView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onItemClickListener.onItemClick(v,position);

            }
        });
        holder.cardView.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                onItemLongClickListener.onItemLongClick(v,position);
                return true;
            }
        });
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

//    @Override
//    public long getItemId(int position) {
//        return list.get(position).hashCode();
//    }


    private void bind(int position,boolean isSelect){

    }


    public class ViewHolder extends RecyclerView.ViewHolder{

        TextView title;
        CardView cardView;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            cardView=itemView.findViewById(R.id.card);
            title=itemView.findViewById(R.id.mainTitle);
        }
    }
}
