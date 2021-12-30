package com.example.sarkaribook;

import static com.example.sarkaribook.Retrofit.ApiUtils.BASE_URL;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.example.sarkaribook.Adapter.EbookAdapter;
import com.example.sarkaribook.Adapter.SimplePdfAdapter;
import com.example.sarkaribook.Adapter.YtlinkAdapter;
import com.example.sarkaribook.Model.Ebook;
import com.example.sarkaribook.Retrofit.ApiInterface;
import com.google.gson.Gson;

import java.util.ArrayList;
import java.util.List;

import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class SimplePDFFragment extends Fragment {


    public SimplePDFFragment() {
        // Required empty public constructor
    }

    RecyclerView recyclerView;
    List<Ebook> subcategoryList;
    ProgressBar progressBar;

    View v;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        v = inflater.inflate(R.layout.fragment_simple_p_d_f, container, false);




        recyclerView = v.findViewById(R.id.ytlinkRecyclerView);
        progressBar = v.findViewById(R.id.progressBar);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(v.getContext());
        recyclerView.setLayoutManager(linearLayoutManager);

        subcategoryList = new ArrayList<>();

        HttpLoggingInterceptor LOG = new HttpLoggingInterceptor();
        LOG.level(HttpLoggingInterceptor.Level.BODY);
        OkHttpClient okHttpClient = new OkHttpClient.Builder().addInterceptor(LOG).build();


        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(BASE_URL) // Specify your api here
                .addConverterFactory(GsonConverterFactory.create())
                .client(okHttpClient)
                .build();


        ApiInterface api = retrofit.create(ApiInterface.class);

        Call<List<Ebook>> call = api.getAllEbookList();

        Log.e("CALL", call.toString());

        call.enqueue(new Callback<List<Ebook>>() {
            @Override
            public void onResponse(Call<List<Ebook>> call, Response<List<Ebook>> response) {
                Log.d("RESULT", new Gson().toJson(response.body()));
                progressBar.setVisibility(View.GONE);
                if (response.isSuccessful()){


                    for(int i=0;i<=subcategoryList.size();i++) {
                        try {
                            if (response.body().get(i).getFile_url().endsWith(".pdf")) {
                                subcategoryList = response.body();
                            }
                        }catch (IndexOutOfBoundsException e){
                            e.printStackTrace();
                        }
                    }
                    Log.d("Under_SUCCESS", new Gson().toJson(response.body()));
                    SimplePdfAdapter adapter = new SimplePdfAdapter(subcategoryList,v.getContext());
                    recyclerView.setAdapter(adapter);
                }
            }

            @Override
            public void onFailure(Call<List<Ebook>> call, Throwable t) {
                progressBar.setVisibility(View.GONE);
                Log.e("CALL_RESULT", t.getMessage());
                Toast.makeText(v.getContext(), t.getMessage().toString(), Toast.LENGTH_SHORT).show();
            }
        });

        return v;
    }
}