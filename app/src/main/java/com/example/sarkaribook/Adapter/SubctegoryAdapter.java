package com.example.sarkaribook.Adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.sarkaribook.Model.Subcategory;
import com.example.sarkaribook.R;

import java.util.List;

public class SubctegoryAdapter extends RecyclerView.Adapter<SubctegoryAdapter.viewHolder> {

    List<Subcategory> subcategoryList;

    public SubctegoryAdapter(List<Subcategory> subcategoryList) {
        this.subcategoryList = subcategoryList;
    }

    @NonNull
    @Override
    public viewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_subcategory,parent,false);
        return new viewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull viewHolder holder, int position) {

        holder.numTextView.setText(String.valueOf(subcategoryList.get(position)));
        holder.subcategoryName.setText(subcategoryList.get(position).getsubcategoryName());

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

            }
        });

    }

    @Override
    public int getItemCount() {
        return subcategoryList.size();
    }

    public class viewHolder extends RecyclerView.ViewHolder {
        TextView subcategoryName,numTextView;

        public viewHolder(@NonNull View itemView) {
            super(itemView);

            subcategoryName = itemView.findViewById(R.id.subcategoryName);
            numTextView = itemView.findViewById(R.id.indexNumTextView);
        }
    }
}
