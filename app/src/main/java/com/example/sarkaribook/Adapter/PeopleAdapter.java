package com.example.sarkaribook.Adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.sarkaribook.Model.User;
import com.example.sarkaribook.R;

import org.w3c.dom.Text;

import java.util.List;

public class PeopleAdapter extends RecyclerView.Adapter<PeopleAdapter.viewholder> {
     private List<User> userList;

    public PeopleAdapter(List<User> userList) {
        this.userList = userList;
    }

    @NonNull
    @Override
    public viewholder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_user,parent,false);
        return new viewholder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull viewholder holder, int position) {
        holder.userName.setText(userList.get(position).getUser_name());
    }

    @Override
    public int getItemCount() {
        return userList.size();
    }

    public class viewholder extends RecyclerView.ViewHolder {

       TextView userName;
        public viewholder(@NonNull View itemView) {
            super(itemView);
            userName = itemView.findViewById(R.id.userName);
        }
    }
}
