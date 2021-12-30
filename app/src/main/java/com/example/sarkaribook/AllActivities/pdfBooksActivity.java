package com.example.sarkaribook.AllActivities;

import static com.example.sarkaribook.Retrofit.ApiUtils.BASE_URL;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager.widget.ViewPager;

import android.Manifest;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.example.sarkaribook.Adapter.EbookAdapter;
import com.example.sarkaribook.Adapter.MyAdapter;
import com.example.sarkaribook.Adapter.SubctegoryAdapter;
import com.example.sarkaribook.Model.Ebook;
import com.example.sarkaribook.Model.Subcategory;
import com.example.sarkaribook.R;
import com.example.sarkaribook.Retrofit.ApiInterface;
import com.google.android.material.tabs.TabLayout;
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

public class pdfBooksActivity extends AppCompatActivity {

    MyAdapter viewPagerAdapter;
    TabLayout tabLayout;
    ViewPager viewPager;
    RecyclerView recyclerView;
    EbookAdapter adapter;
    ImageView imageView;
    int cat_id;
    List<Ebook> subcategoryList;
    ProgressBar progressBar;
    int READ_STORAGE_PERMISSION = 100;
    int WRITE_STORAGE_PERMISSION = 101;
    int tabPosition;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pdf_books);


        tabLayout=(TabLayout)findViewById(R.id.tabLayout);
        viewPager=(ViewPager)findViewById(R.id.viewPager);

        tabLayout.addTab(tabLayout.newTab().setText("PDF"));
        tabLayout.addTab(tabLayout.newTab().setText("Google Doc"));
        tabLayout.addTab(tabLayout.newTab().setText("Youtube"));

        tabLayout.setTabGravity(TabLayout.GRAVITY_FILL);
        MyAdapter adapter1 = new MyAdapter(this,getSupportFragmentManager(), tabLayout.getTabCount());

        viewPager.setAdapter(adapter1);
        viewPager.addOnPageChangeListener(new TabLayout.TabLayoutOnPageChangeListener(tabLayout));
        tabLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                viewPager.setCurrentItem(tab.getPosition());
                tabPosition = tab.getPosition() ;
                reload_Data();
                Log.e("TP",String.valueOf(tab.getPosition()));
            }
            @Override
            public void onTabUnselected(TabLayout.Tab tab) {
                Log.e("TP",String.valueOf(tab.getPosition()));
            }
            @Override
            public void onTabReselected(TabLayout.Tab tab) {
                Log.e("TP",String.valueOf(tab.getPosition()));
            }
        });

        recyclerView = findViewById(R.id.pdfRecyclerView);
        imageView = findViewById(R.id.gobackmenuIcon);
        progressBar = findViewById(R.id.progressBar);



        imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });


        cat_id = getIntent().getIntExtra("id",0);


        reload_Data();


        if(ContextCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE)
                != PackageManager.PERMISSION_GRANTED)
        {
            // Permission is not granted
            checkPermission(Manifest.permission.WRITE_EXTERNAL_STORAGE,101);
        }



    }

    private void reload_Data() {
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

        Call<List<Ebook>> call = api.getEbookList(cat_id);

        Log.e("CALL", call.toString());

        call.enqueue(new Callback<List<Ebook>>() {
            @Override
            public void onResponse(Call<List<Ebook>> call, Response<List<Ebook>> response) {
                Log.d("RESULT", new Gson().toJson(response.body()));
                progressBar.setVisibility(View.GONE);
                if (response.isSuccessful()){
                 //   try {
//                        for (int i = 0; i <= subcategoryList.size(); i++) {
//                            if (response.body().get(i).getFile_url().endsWith(".pdf")) {
                                subcategoryList = response.body();
                                Log.d("Under_SUCCESS", new Gson().toJson(response.body()));
//                            }
//                        }
//                    }catch (IndexOutOfBoundsException e){
//                        e.printStackTrace();
//                    }

                    adapter = new EbookAdapter(subcategoryList,getApplicationContext());
                    recyclerView.setAdapter(adapter);
                }
            }

            @Override
            public void onFailure(Call<List<Ebook>> call, Throwable t) {
                progressBar.setVisibility(View.GONE);
                Log.e("CALL_RESULT", t.getMessage());
                Toast.makeText(getApplicationContext(), t.getMessage().toString(), Toast.LENGTH_SHORT).show();
            }
        });

    }

    public void checkPermission(String permission, int requestCode)
    {
        // Checking if permission is not granted
        if (ContextCompat.checkSelfPermission(this, permission) == PackageManager.PERMISSION_DENIED) {
            ActivityCompat.requestPermissions(this, new String[] { permission }, requestCode);
        }
        else {
            Toast.makeText(this, "Permission already granted", Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode,
                                           @NonNull String[] permissions,
                                           @NonNull int[] grantResults)
    {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        if (requestCode == READ_STORAGE_PERMISSION) {

            // Checking whether user granted the permission or not.
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {

                // Showing the toast message
                Toast.makeText(this, "Storage Permission Granted", Toast.LENGTH_SHORT).show();
            }
            else {
                Toast.makeText(this, "Storage Permission Denied", Toast.LENGTH_SHORT).show();
            }
        }
        else if (requestCode == WRITE_STORAGE_PERMISSION) {
            if (grantResults.length > 0
                    && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                Toast.makeText(this, "Storage Permission Granted", Toast.LENGTH_SHORT).show();
            }
            else {
                Toast.makeText(this, "Storage Permission Denied", Toast.LENGTH_SHORT).show();
            }
        }
    }
}