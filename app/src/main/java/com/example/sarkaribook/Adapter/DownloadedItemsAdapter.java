package com.example.sarkaribook.Adapter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.sarkaribook.AllActivities.PDFViewActivity;
import com.example.sarkaribook.Model.Category;
import com.example.sarkaribook.R;

import java.io.File;
import java.util.List;

public class DownloadedItemsAdapter extends RecyclerView.Adapter<DownloadedItemsAdapter.viewHOLDER> {
    private List<File> downloadItemsList;
    private Context context;

    public DownloadedItemsAdapter(List<File> downloadItemsList, Context context) {
        this.downloadItemsList = downloadItemsList;
        this.context = context;
    }

    @NonNull
    @Override
    public viewHOLDER onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_download,parent,false);
        return new viewHOLDER(v);
    }

    @Override
    public void onBindViewHolder(@NonNull viewHOLDER holder, @SuppressLint("RecyclerView") int position) {
        holder.cTitle.setText(downloadItemsList.get(position).getName());
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(view.getContext(), PDFViewActivity.class);
                intent.putExtra("path",downloadItemsList.get(position).getPath());
                view.getContext().startActivity(intent);
            }
        });

    }

    @Override
    public int getItemCount() {
        return downloadItemsList.size();
    }

    public class viewHOLDER extends RecyclerView.ViewHolder {
        TextView cTitle;
        public viewHOLDER(@NonNull View itemView) {
            super(itemView);

            cTitle = itemView.findViewById(R.id.cTitle);

        }
    }
}
