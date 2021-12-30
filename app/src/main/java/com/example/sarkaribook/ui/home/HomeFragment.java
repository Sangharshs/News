package com.example.sarkaribook.ui.home;

import static com.example.sarkaribook.Retrofit.ApiUtils.BASE_URL;

import android.os.Bundle;

import androidx.appcompat.widget.LinearLayoutCompat;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.provider.CalendarContract;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.example.sarkaribook.Adapter.CategoryAdapter;
import com.example.sarkaribook.R;
import com.example.sarkaribook.Model.Category;
import com.example.sarkaribook.Retrofit.ApiInterface;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;


public class HomeFragment extends Fragment {

    public HomeFragment() {
        // Required empty public constructor
    }
    View v;
    private RecyclerView categoryRecyclerView;
    private List<Category> categoryList;
    ProgressBar progressBar;
    private EditText searchBar;
    CategoryAdapter adapter;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        v = inflater.inflate(R.layout.fragment_home, container, false);


        searchBar = v.findViewById(R.id.searchEditext);
        categoryRecyclerView = v.findViewById(R.id.categoryRecyclerView);
        progressBar = v.findViewById(R.id.progressBar);

        categoryList = new ArrayList<>();

        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(v.getContext());
        linearLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        categoryRecyclerView.setLayoutManager(linearLayoutManager);

        HttpLoggingInterceptor loggingInterceptor = new HttpLoggingInterceptor();
        HttpLoggingInterceptor LOG = new HttpLoggingInterceptor();
        LOG.level(HttpLoggingInterceptor.Level.BODY);
        OkHttpClient okHttpClient = new OkHttpClient.Builder().addInterceptor(LOG).build();

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(BASE_URL) // Specify your api here
                .addConverterFactory(GsonConverterFactory.create())
                .client(okHttpClient)
                .build();

        ApiInterface api = retrofit.create(ApiInterface.class);

        Call<List<Category>> call = api.getCategories();
        Log.e("CALL",api.toString());
        call.enqueue(new Callback<List<Category>>() {
            @Override
            public void onResponse(Call<List<Category>> call, Response<List<Category>> response) {
                progressBar.setVisibility(View.GONE);
                Log.e("RESPONSE",response.toString());
                if(response.isSuccessful()) {

                    categoryList = response.body();

                     adapter = new CategoryAdapter(categoryList);
                    categoryRecyclerView.setAdapter(adapter);
                    categoryRecyclerView.scheduleLayoutAnimation();
                }
            }

            @Override
            public void onFailure(Call<List<Category>> call, Throwable t) {
                progressBar.setVisibility(View.GONE);
                //Toast.makeText(getContext(), t.getMessage().toString(), Toast.LENGTH_SHORT).show();
            }
        });

        searchBar.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                filter(s.toString());
            }
        });


        return v;


    }

    private void filter(String text) {

        List<Category> filterlist1 = new ArrayList<>();

        for(Category product: categoryList){
            if(product.getCategoryName().toLowerCase().contains(text.toString())){
                filterlist1.add(product);
            }
        }
        adapter.filterList(filterlist1);

    }

}