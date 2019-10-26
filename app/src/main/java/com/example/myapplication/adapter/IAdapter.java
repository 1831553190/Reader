package com.example.myapplication.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.RelativeLayout;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.selection.ItemDetailsLookup;
import androidx.recyclerview.selection.SelectionTracker;
import androidx.recyclerview.widget.RecyclerView;
import com.example.myapplication.model.Data;
import com.example.myapplication.R;

import java.util.ArrayList;
import java.util.List;

public class IAdapter extends RecyclerView.Adapter<IAdapter.ViewHolder> implements Filterable {

    List<Data> list;
    Context context;
    OnItemClickListener onItemClickListener;
    boolean actionMode=false;
//    FilterText filterText;
    private List<Data> mSourceList = new ArrayList<>();
    SelectionTracker tracker;

    public void setTracker(SelectionTracker tracker){
        this.tracker=tracker;
    }

    public void setAddAll(){
        mSourceList.clear();
        mSourceList.addAll(list);
    }


    public IAdapter(List<Data> list, Context context) {
        this.list = list;
        //这里需要初始化filterList
        this.context = context;

//        setHasStableIds(true);
    }
    public void setActionMode(boolean actionMode){
        this.actionMode=actionMode;

    }


    @Override
    public Filter getFilter() {
        return new Filter() {
            @Override
            protected FilterResults performFiltering(CharSequence constraint) {
                if (constraint.length()==0){
                    list.clear();
                    list.addAll(mSourceList);
                }else{
//                   List<Data> listFilter=new ArrayList<>();
                    list.clear();
                    for (Data data:mSourceList){
                       if (data.getTitle().toUpperCase().contains(constraint.toString().toUpperCase())){
                           list.add(data);
                       }
                   }
//                   list.addAll(listFilter);
                }

                FilterResults filterResults = new FilterResults();
                filterResults.values = list;
                return filterResults;
            }

            @Override
            protected void publishResults(CharSequence constraint, FilterResults results) {
//                list.clear();
//                list.addAll((ArrayList<Data>) results.values);
                notifyDataSetChanged();
            }
        };
    }


    public interface OnItemClickListener{
        void onItemClick(View view,int position);
    }

    public void setOnItemClickListener(OnItemClickListener onItemClickListener){
        this.onItemClickListener=onItemClickListener;
    }
    @NonNull
    @Override
    public IAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view= LayoutInflater.from(context).inflate(R.layout.newbook_item,parent,false);
        IAdapter.ViewHolder viewHolder=new IAdapter.ViewHolder(view);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull IAdapter.ViewHolder holder, final int position) {
        holder.title.setText(list.get(position).getTitle());
        holder.filepath.setText(list.get(position).getFilePath());
        if (tracker!=null){
            if (tracker.isSelected(list.get(position))){
                holder.checkBox.setChecked(true);
                holder.itemView.setActivated(true);
            }else{
                holder.checkBox.setChecked(false);
                holder.itemView.setActivated(false);
            }
        }
        holder.checkBox.setVisibility(actionMode?View.VISIBLE:View.GONE);

        holder.itemV.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!actionMode){
                    onItemClickListener.onItemClick(v,position);
                }
            }
        });
    }

    @Override
    public int getItemCount() {
        return list==null?0:list.size();
    }


    @Override
    public long getItemId(int position) {
        return position;
    }

    public class ViewHolder extends RecyclerView.ViewHolder{
        TextView title,filepath;
        CheckBox checkBox;
        RelativeLayout itemV;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            title=itemView.findViewById(R.id.item_newbook);
            filepath=itemView.findViewById(R.id.filepath);
            itemV=itemView.findViewById(R.id.item);
            checkBox=itemView.findViewById(R.id.importbook_checkbox);
        }
        public ItemDetailsLookup.ItemDetails getViewHolderItemDetails(){
            return new ItemDetailsLookup.ItemDetails() {
                @Override
                public int getPosition() {
                    return getAdapterPosition();
                }
                @Nullable
                @Override
                public Object getSelectionKey() {
                    return list.get(getAdapterPosition());
                }
            };
        }
    }
}
