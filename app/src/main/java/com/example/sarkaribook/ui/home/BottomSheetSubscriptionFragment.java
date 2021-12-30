package com.example.sarkaribook.ui.home;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;

import com.example.sarkaribook.Adapter.SubscriptionPlanAdapter;
import com.example.sarkaribook.Model.Subscription;
import com.example.sarkaribook.R;
import com.google.android.material.bottomsheet.BottomSheetDialogFragment;

import java.util.ArrayList;
import java.util.List;


public class BottomSheetSubscriptionFragment extends BottomSheetDialogFragment {

    View v;
    RecyclerView  subscriptionRecyclerView;
    List<Subscription> subscriptionList;
    public BottomSheetSubscriptionFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
       v = inflater.inflate(R.layout.fragment_bottom_sheet_subscription, container, false);

        subscriptionRecyclerView = v.findViewById(R.id.subscriptionsPlansRecyclerView);

        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(v.getContext());
        subscriptionRecyclerView.setLayoutManager(linearLayoutManager);

        subscriptionList = new ArrayList<>();

        subscriptionList.add(new Subscription("Buy For 30 Days","In Just Rs 99"));
        subscriptionList.add(new Subscription("Buy For 60 Days", "In Just Rs 199"));
        subscriptionList.add(new Subscription("Buy For 120 Days","In Just Rs 299"));
        subscriptionList.add(new Subscription("Buy For 180 Days","In Just Rs 399"));

//        SubscriptionPlanAdapter adapter = new SubscriptionPlanAdapter(subscriptionList,getActivity(),this);
//        subscriptionRecyclerView.setAdapter(adapter);


        return v;
    }
}