package com.example.sarkaribook.Adapter;

import android.annotation.SuppressLint;
import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.sarkaribook.Model.Ebook;
import com.example.sarkaribook.R;

import java.util.List;

public class YtlinkAdapter extends  RecyclerView.Adapter<YtlinkAdapter.viewHolder>{
    List<Ebook> ebookList;
    Context context;

    public YtlinkAdapter(List<Ebook> ebookList, Context context) {
        this.ebookList = ebookList;
        this.context = context;
    }

    @NonNull
    @Override
    public viewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_ebook,parent,false);
        return new viewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull viewHolder holder, @SuppressLint("RecyclerView") int position) {
        holder.pdfDesc.setText(ebookList.get(position).description);
        holder.textView.setText(ebookList.get(position).title);

       holder.imageView.setImageResource(R.drawable.images);

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Uri uri = Uri.parse(ebookList.get(position).getImage_url()); // missing 'http://' will cause crashed
                Intent intent = new Intent(Intent.ACTION_VIEW, uri);
                try {
                    view.getContext().startActivity(intent);
                }catch (ActivityNotFoundException e){
                    e.printStackTrace();
                }
            }
        });
    }

    @Override
    public int getItemCount() {
        return ebookList.size();
    }

    public class viewHolder extends RecyclerView.ViewHolder {
        TextView textView,pdfDesc;
        ImageView imageView;
        public viewHolder(@NonNull View itemView) {
            super(itemView);
            textView = itemView.findViewById(R.id.pdfTitle);
            pdfDesc = itemView.findViewById(R.id.pdfDescription);
            imageView = itemView.findViewById(R.id.downloadBtn);

        }
    }
}
