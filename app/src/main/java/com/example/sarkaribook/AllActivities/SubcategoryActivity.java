package com.example.sarkaribook.AllActivities;

import static android.view.View.GONE;

import static com.example.sarkaribook.Retrofit.ApiUtils.BASE_URL;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager.widget.ViewPager;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.example.sarkaribook.Adapter.MyAdapter;
import com.example.sarkaribook.Adapter.SubctegoryAdapter;
import com.example.sarkaribook.Model.Subcategory;
import com.example.sarkaribook.R;
import com.example.sarkaribook.Retrofit.ApiInterface;
import com.google.android.material.tabs.TabLayout;
import com.google.gson.Gson;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class SubcategoryActivity extends AppCompatActivity {

    RecyclerView recyclerView;
    SubctegoryAdapter adapter;
    ImageView imageView;
    int cat_id;
    List<Subcategory> subcategoryList;
    ProgressBar progressBar;
    MyAdapter viewPagerAdapter;
    TabLayout tabLayout;
    ViewPager viewPager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_subcategory);

        recyclerView = findViewById(R.id.subcategoryRecyclerView);
        imageView = findViewById(R.id.gobackmenuIcon);
        progressBar = findViewById(R.id.progressBar);

        imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });


        cat_id = getIntent().getIntExtra("id",0);

        showToast(String.valueOf(cat_id));
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
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

            Call<List<Subcategory>> call = api.getSubcategories(String.valueOf(cat_id));

            Log.e("CALL", call.toString());

            call.enqueue(new Callback<List<Subcategory>>() {
                @Override
                public void onResponse(Call<List<Subcategory>> call, Response<List<Subcategory>> response) {
                    Log.d("RESULT", new Gson().toJson(response.body()));
                    progressBar.setVisibility(View.GONE);
                    if (response.isSuccessful()) {
                        subcategoryList = response.body();
                        Log.d("Under_SUCCESS", new Gson().toJson(response.body()));
                        adapter = new SubctegoryAdapter(subcategoryList);
                        recyclerView.setAdapter(adapter);
                    }
                }

                @Override
                public void onFailure(Call<List<Subcategory>> call, Throwable t) {
                    progressBar.setVisibility(View.GONE);
                    Log.e("CALL_RESULT", t.getMessage());
                    Toast.makeText(getApplicationContext(), t.getMessage().toString(), Toast.LENGTH_SHORT).show();
                }
            });




    }

    public void showToast(String valueOf) {

        Toast.makeText(this, valueOf, Toast.LENGTH_SHORT).show();
    }
}