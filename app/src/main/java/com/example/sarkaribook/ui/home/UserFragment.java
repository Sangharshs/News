package com.example.sarkaribook.ui.home;

import static com.example.sarkaribook.Retrofit.ApiUtils.BASE_URL;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.example.sarkaribook.Adapter.CategoryAdapter;
import com.example.sarkaribook.Adapter.PeopleAdapter;
import com.example.sarkaribook.Model.Category;
import com.example.sarkaribook.Model.User;
import com.example.sarkaribook.R;
import com.example.sarkaribook.Retrofit.ApiInterface;

import java.util.ArrayList;
import java.util.List;

import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;


public class UserFragment extends Fragment {

    public UserFragment() {
        // Required empty public constructor
    }

    View v;
    PeopleAdapter peopleAdapter;
    RecyclerView recyclerView;
    List<User> userList;
    ProgressBar progressBar;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        v = inflater.inflate(R.layout.fragment_user
                , container, false);

        recyclerView = v.findViewById(R.id.peopleitemsRecyclerView);
        progressBar = v.findViewById(R.id.progressBar);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(v.getContext());
        linearLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        recyclerView.setLayoutManager(linearLayoutManager);

        userList = new ArrayList<>();
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

        Call<List<User>> call = api.getUsers();
        Log.e("CALL",api.toString());
        call.enqueue(new Callback<List<User>>() {
            @Override
            public void onResponse(Call<List<User>> call, Response<List<User>> response) {
                progressBar.setVisibility(View.GONE);
                Log.e("RESPONSE",response.toString());
                if(response.isSuccessful()) {

                   userList = response.body();

                    peopleAdapter = new PeopleAdapter(userList);
                    recyclerView.setAdapter(peopleAdapter);
                }
            }

            @Override
            public void onFailure(Call<List<User>> call, Throwable t) {
                progressBar.setVisibility(View.GONE);
                Toast.makeText(getContext(), t.getMessage().toString(), Toast.LENGTH_SHORT).show();
            }
        });




        return v;
    }
}