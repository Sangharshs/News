package com.example.sarkaribook.ui.home;

import static com.example.sarkaribook.R.layout.fragment_pay;
import static com.example.sarkaribook.Retrofit.ApiUtils.BASE_URL;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager.widget.ViewPager;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.example.sarkaribook.Adapter.EbookAdapter;
import com.example.sarkaribook.Adapter.PaidEbooksAdapter;
import com.example.sarkaribook.Adapter.MyAdapter;
import com.example.sarkaribook.Model.Ebook;
import com.example.sarkaribook.Model.Subscription;
import com.example.sarkaribook.R;
import com.example.sarkaribook.Retrofit.ApiInterface;
import com.example.sarkaribook.TinyDB;
import com.google.android.material.tabs.TabLayout;
import com.google.gson.Gson;

import java.util.Collections;
import java.util.List;

import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;


public class PayFragment extends Fragment {

    public PayFragment() {
        // Required empty public constructor
    }
    View v;
    RecyclerView ebookListRecyclerView;
    List<Subscription> subscriptionList;
    List<Ebook> ebookList;
    ProgressBar progressBar;
    TinyDB tinyDB;
    EbookAdapter adapter;

    TabLayout tabLayout;
    ViewPager viewPager;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        v =  inflater.inflate(fragment_pay, container, false);

        tinyDB = new TinyDB(v.getContext());
        //ebookListRecyclerView = v.findViewById(R.id.ebooksPaidRecyclerView);

    //    progressBar = v.findViewById(R.id.progressBar);

       // showPaidPdfList();

        tabLayout=(TabLayout)v.findViewById(R.id.tabLayout);
        viewPager=(ViewPager)v.findViewById(R.id.viewPager);

        tabLayout.addTab(tabLayout.newTab().setText("PDF"));
        tabLayout.addTab(tabLayout.newTab().setText("Google Doc"));
        tabLayout.addTab(tabLayout.newTab().setText("Youtube"));

        tabLayout.setTabGravity(TabLayout.GRAVITY_FILL);
        MyAdapter adapter = new MyAdapter(getActivity(),getParentFragmentManager(), tabLayout.getTabCount());

        viewPager.setAdapter(adapter);
        viewPager.setCurrentItem(0);

        viewPager.addOnPageChangeListener(new TabLayout.TabLayoutOnPageChangeListener(tabLayout));
        tabLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                viewPager.setCurrentItem(tab.getPosition());
            }
            @Override
            public void onTabUnselected(TabLayout.Tab tab) {
            }
            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }
        });

//
//        HttpLoggingInterceptor LOG = new HttpLoggingInterceptor();
//        LOG.level(HttpLoggingInterceptor.Level.BODY);
//        OkHttpClient okHttpClient = new OkHttpClient.Builder().addInterceptor(LOG).build();
//
//
//        Retrofit retrofit = new Retrofit.Builder()
//                .baseUrl(BASE_URL) // Specify your api here
//                .addConverterFactory(GsonConverterFactory.create())
//                .client(okHttpClient)
//                .build();
//
//
//        HttpLoggingInterceptor LOG1 = new HttpLoggingInterceptor();
//        LOG.level(HttpLoggingInterceptor.Level.BODY);
//        OkHttpClient okHttpClient1 = new OkHttpClient.Builder().addInterceptor(LOG1).build();
//
//
//        ApiInterface api = retrofit.create(ApiInterface.class);
//
//
//        Call<List<Ebook>> call2 = api.getYoutubeLinks();
//
//
//        call2.enqueue(new Callback<List<Ebook>>() {
//            @Override
//            public void onResponse(Call<List<Ebook>> call, Response<List<Ebook>> response) {
//                if(response.isSuccessful()){
//                    progressBar.setVisibility(View.GONE);
//                    if (response.isSuccessful()) {
//                        ebookList = response.body();
//                        Log.d("Under_SUCCESS", new Gson().toJson(response.body()));
//                        Collections.reverse(ebookList);
//                        EbookAdapter adapter = new EbookAdapter(ebookList,getContext(),0);
//                        ebookListRecyclerView.setAdapter(adapter);
//                    }
//
//                }
//            }
//
//            @Override
//            public void onFailure(Call<List<Ebook>> call, Throwable t) {
//
//            }
//        });




//        call2.enqueue(new Callback<Res>() {
//            @Override
//            public void onResponse(Call<Res> call, Response<Res> response) {
//                if (response.isSuccessful()) {
//                    Log.e("RES", response.body().getMessage());
//
//                    if (response.body().getMessage().equals("no subscription")) {
//                        tinyDB.putBoolean("isSubscribed", false);
////                        BottomSheetSubscriptionFragment bottomSheet = new BottomSheetSubscriptionFragment();
////                        bottomSheet.show(getFragmentManager(),
//                            //    "ModalBottomSheet");
//
////                        AlertDialog.Builder builder1 = new AlertDialog.Builder(v.getContext());
////                        builder1.setMessage("Please Buy Subscription First");
////                        builder1.setCancelable(true);
////
////                        builder1.setPositiveButton(
////                                "Buy Subscription",
////                                new DialogInterface.OnClickListener() {
////                                    public void onClick(DialogInterface dialog, int id) {
////
////                                    }
////                                });
////
////                        AlertDialog alert11 = builder1.create();
////                        alert11.setCancelable(false);
////                        alert11.show();
//                    }
//                }
//            }
//
//            @Override
//            public void onFailure(Call<Res> call, Throwable t) {
//                Log.e("ERROR", t.getMessage().toString());
//            }
//        });
        // Inflate the layout for this fragment
        return v;
    }

    private void showPaidPdfList() {


        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(v.getContext());
        ebookListRecyclerView.setLayoutManager(linearLayoutManager);

        HttpLoggingInterceptor LOG = new HttpLoggingInterceptor();
        LOG.level(HttpLoggingInterceptor.Level.BODY);
        OkHttpClient okHttpClient = new OkHttpClient.Builder().addInterceptor(LOG).build();

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(BASE_URL) // Specify your api here
                .addConverterFactory(GsonConverterFactory.create())
                .client(okHttpClient)
                .build();

        ApiInterface api = retrofit.create(ApiInterface.class);

        Call<List<Ebook>> call = api.getAllPaidEbookList();

        Log.e("CALL", call.toString());

        call.enqueue(new Callback<List<Ebook>>() {
            @Override
            public void onResponse(Call<List<Ebook>> call, Response<List<Ebook>> response) {
                Log.d("RESULT", new Gson().toJson(response.body()));
                progressBar.setVisibility(View.GONE);
                if (response.isSuccessful()) {
                    ebookList = response.body();
                    Log.d("Under_SUCCESS", new Gson().toJson(response.body()));
                    PaidEbooksAdapter adapter = new PaidEbooksAdapter(ebookList,v.getContext());
                    ebookListRecyclerView.setAdapter(adapter);
                }
            }

            @Override
            public void onFailure(Call<List<Ebook>> call, Throwable t) {
                progressBar.setVisibility(View.GONE);
                Log.e("CALL_RESULT", t.getMessage());
                Toast.makeText(v.getContext(), t.getMessage().toString(), Toast.LENGTH_SHORT).show();
            }
        });


    }
}