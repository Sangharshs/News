package com.example.sarkaribook.AllActivities;

import static com.example.sarkaribook.Retrofit.ApiUtils.BASE_URL;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.res.ColorStateList;
import android.net.Uri;
import android.os.Bundle;
import android.text.Html;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.view.Menu;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.Toast;

import com.example.sarkaribook.Model.UserLogin;
import com.example.sarkaribook.R;
import com.example.sarkaribook.Retrofit.ApiInterface;
import com.example.sarkaribook.Retrofit.Res;
import com.example.sarkaribook.TinyDB;
import com.example.sarkaribook.ui.home.DownloadFragment;
import com.example.sarkaribook.ui.home.HomeFragment;
import com.example.sarkaribook.ui.home.PayFragment;
import com.example.sarkaribook.ui.home.UserFragment;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.navigation.NavigationBarView;
import com.google.android.material.snackbar.Snackbar;
import com.google.android.material.navigation.NavigationView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.appcompat.app.AppCompatActivity;

import com.example.sarkaribook.databinding.ActivityMainBinding;

import java.util.List;
import java.util.Locale;

import javax.sql.ConnectionEvent;
import javax.sql.ConnectionEventListener;

import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class MainActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener, ConnectionEventListener {

    private AppBarConfiguration mAppBarConfiguration;
    private ActivityMainBinding binding;
    Fragment fragment = null;
    FragmentTransaction fragmentTransaction;
    ImageView menuimageView;
    TinyDB tinyDB;
    BottomNavigationView navigation;

    @SuppressLint("ResourceAsColor")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        menuimageView = findViewById(R.id.menuIcon);

        tinyDB = new TinyDB(getApplicationContext());

        DrawerLayout drawer = binding.drawerLayout;
        NavigationView navigationView = binding.navView;
        navigationView.setNavigationItemSelectedListener(this);

        navigationView.setItemIconTintList(ColorStateList.valueOf(R.color.white));

        navigation = (BottomNavigationView) findViewById(R.id.bottomNavView);
//        if(tinyDB.getBoolean("isSubscribed")) {
            navigation.setOnItemSelectedListener(mOnItemSelectedListener);
//        }
        fragment = new HomeFragment();
        switchFragment(fragment);

        menuimageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(drawer.isOpen()){
                    drawer.close();
                }else{
                    drawer.open();
                }
            }
        });
        checkPermission(Manifest.permission.READ_EXTERNAL_STORAGE,100);

        HttpLoggingInterceptor LOG = new HttpLoggingInterceptor();
        LOG.level(HttpLoggingInterceptor.Level.BODY);
        OkHttpClient okHttpClient = new OkHttpClient.Builder().addInterceptor(LOG).build();


        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(BASE_URL) // Specify your api here
                .addConverterFactory(GsonConverterFactory.create())
                .client(okHttpClient)
                .build();


        HttpLoggingInterceptor LOG1 = new HttpLoggingInterceptor();
        LOG.level(HttpLoggingInterceptor.Level.BODY);
        OkHttpClient okHttpClient1 = new OkHttpClient.Builder().addInterceptor(LOG1).build();


        Retrofit retrofit1 = new Retrofit.Builder()
                .baseUrl(BASE_URL) // Specify your api here
                .addConverterFactory(GsonConverterFactory.create())
                .client(okHttpClient)
                .build();
        ApiInterface api1 = retrofit1.create(ApiInterface.class);
        int u_id = tinyDB.getInt("id");
        Call<Res> call1 = api1.getUserActiveOrNot(u_id);

        call1.enqueue(new Callback<Res>() {
            @Override
            public void onResponse(Call<Res> call, Response<Res> response) {

                if(response.isSuccessful()) {

                    if(response.body().getMessage().equals("active user")) {
                        ApiInterface api = retrofit.create(ApiInterface.class);


                        Call<Res> call2 = api.getSubscriptionStatus(u_id);

                        call2.enqueue(new Callback<Res>() {
                            @Override
                            public void onResponse(Call<Res> call, Response<Res> response) {
                                if (response.isSuccessful()) {
                                    Log.e("RES", response.body().getMessage());

                                    if (response.body().getMessage().equals("no subscription")) {
                                        startActivity(new Intent(MainActivity.this,SubscriptionsActivity.class));
                                        finish();

                                       // tinyDB.putBoolean("isSubscribed", false);
//                                        AlertDialog.Builder builder1 = new AlertDialog.Builder(MainActivity.this);
//                                        builder1.setMessage("Please Buy Subscription First");
//                                        builder1.setCancelable(true);
//
//                                        builder1.setPositiveButton(
//                                                "Buy Subscription",
//                                                new DialogInterface.OnClickListener() {
//                                                    public void onClick(DialogInterface dialog, int id) {
//                                                        navigation.setClickable(false);
//                                                        fragment = new PayFragment();
//                                                        switchFragment(fragment);
//                                                        Toast.makeText(MainActivity.this, "Buy Premium", Toast.LENGTH_SHORT).show();
//                                                        dialog.cancel();
//                                                    }
//                                                });
//
//                                        AlertDialog alert11 = builder1.create();
//                                        alert11.setCancelable(false);
//                                        alert11.show();
                                    }
                                }
                            }

                            @Override
                            public void onFailure(Call<Res> call, Throwable t) {
                                Log.e("ERROR", t.getMessage().toString());
                            }
                        });

                    }else{
                        AlertDialog.Builder builder1 = new AlertDialog.Builder(MainActivity.this);
                        builder1.setMessage("Your Account Is Blocked");
                        AlertDialog alert11 = builder1.create();
                        alert11.setCancelable(false);
                        alert11.show();

                    }
                }

            }

            @Override
            public void onFailure(Call<Res> call, Throwable t) {

            }
        });




    }
    private BottomNavigationView.OnItemSelectedListener mOnItemSelectedListener
            = new BottomNavigationView.OnItemSelectedListener() {
        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem item) {
            Fragment ft;
            switch (item.getItemId()) {
                case R.id.bottom_nav_home:
                    fragment = new HomeFragment();
                    switchFragment(fragment);
                    return true;
                case R.id.bottom_nav_pay:
                    fragment = new PayFragment();
                    switchFragment(fragment);
                    return true;
                case R.id.bottom_nav_download:
                    fragment = new DownloadFragment();
                    switchFragment(fragment);
                    return true;
                case R.id.bottom_nav_people:
                    fragment = new UserFragment();
                    switchFragment(fragment);

                    return true;
            }
            return false;
        }

    };
    private void switchFragment(Fragment fragment) {
         fragmentTransaction = getSupportFragmentManager().beginTransaction();
        fragmentTransaction.replace(R.id.nav_host_fragment_content_main, fragment);
        fragmentTransaction.commit();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onSupportNavigateUp() {
        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment_content_main);
        return NavigationUI.navigateUp(navController, mAppBarConfiguration)
                || super.onSupportNavigateUp();
    }

    public void checkPermission(String permission, int requestCode)
    {
        // Checking if permission is not granted
        if (ContextCompat.checkSelfPermission(this, permission) == PackageManager.PERMISSION_DENIED) {
            ActivityCompat.requestPermissions(this, new String[] { permission }, requestCode);
        }
        else {
          //  Toast.makeText(this, "Permission already granted", Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode,
                                           @NonNull String[] permissions,
                                           @NonNull int[] grantResults)
    {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        if (requestCode == 100) {
            // Checking whether user granted the permission or not.
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {

                // Showing the toast message
               // Toast.makeText(this, "Storage Permission Granted", Toast.LENGTH_SHORT).show();
            }
            else {
               // Toast.makeText(this, "Storage Permission Denied", Toast.LENGTH_SHORT).show();
            }
        }
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {
            case R.id.nav_home:
                 startActivity(new Intent(MainActivity.this,MainActivity.class));
                 finish();
                return true;
            case R.id.nav_privacy_policy:

                return true;
            case R.id.nav_share:
                try {
                    Intent shareIntent = new Intent(Intent.ACTION_SEND);
                    shareIntent.setType("text/plain");
                    shareIntent.putExtra(Intent.EXTRA_SUBJECT, getString(R.string.app_name));
                    String shareMessage= "\nLet me recommend you this application\n\n";
                    shareMessage = shareMessage + "https://play.google.com/store/apps/details?id=" + getPackageName() +"\n\n";
                    shareIntent.putExtra(Intent.EXTRA_TEXT, shareMessage);
                    startActivity(Intent.createChooser(shareIntent, "choose one"));
                } catch(Exception e) {
                    //e.toString();
                }
                return true;
            case R.id.nav_rateUs:
                Uri uriUrl = Uri.parse("https://play.google.com/");
                Intent launchBrowser = new Intent(Intent.ACTION_VIEW, uriUrl);
                startActivity(launchBrowser);
                return true;

            case R.id.nav_aboutUs:
                Uri uriUrl1 = Uri.parse("https://play.google.com/");
                Intent launchBrowser1 = new Intent(Intent.ACTION_VIEW, uriUrl1);
                startActivity(launchBrowser1);
                return true;

        }
        return false;
    }

    @Override
    public void connectionClosed(ConnectionEvent connectionEvent) {
        Toast.makeText(this, "Internet Connection Required", Toast.LENGTH_SHORT).show();
        finish();
    }

    @Override
    public void connectionErrorOccurred(ConnectionEvent connectionEvent) {
        Toast.makeText(this, "Internet Connection Required", Toast.LENGTH_SHORT).show();
       finish();
    }
}