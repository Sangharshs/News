package com.example.sarkaribook.Adapter;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.sarkaribook.AllActivities.pdfBooksActivity;
import com.example.sarkaribook.Model.Category;
import com.example.sarkaribook.R;
import com.example.sarkaribook.AllActivities.SubcategoryActivity;

import java.util.List;


public class CategoryAdapter extends RecyclerView.Adapter<CategoryAdapter.viewHolder> {

    private List<Category> categoryList;

    public CategoryAdapter(List<Category> categoryList) {
        this.categoryList = categoryList;
    }

    @NonNull
    @Override
    public viewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_category,parent,false);
        return new viewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull viewHolder holder, @SuppressLint("RecyclerView") int position) {

        holder.categoryName.setText(categoryList.get(position).getCategoryName());
        holder.postTime.setText(categoryList.get(position).time);

       holder.itemView.setOnClickListener(new View.OnClickListener() {
    @Override
    public void onClick(View view) {
        Intent intent = new Intent(view.getContext(), pdfBooksActivity.class);
        intent.putExtra("id",categoryList.get(position).id);
        Log.e("id",String.valueOf(categoryList.get(position).id));
        view.getContext().startActivity(intent);
    }
});
    }

    @Override
    public int getItemCount() {
        return categoryList.size();
    }

    public class viewHolder extends RecyclerView.ViewHolder {
        TextView categoryName,postTime;
        public viewHolder(@NonNull View itemView) {
            super(itemView);
            categoryName = itemView.findViewById(R.id.cTitle);
            postTime = itemView.findViewById(R.id.postTimeView);
        }
    }

    public void filterList(List<Category> productfilterList1){
         categoryList = productfilterList1;
        notifyDataSetChanged();
    }
}
