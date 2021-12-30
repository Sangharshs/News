package com.example.sarkaribook.AllActivities;

import static com.example.sarkaribook.Retrofit.ApiUtils.BASE_URL;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import com.example.sarkaribook.Adapter.SubscriptionPlanAdapter;
import com.example.sarkaribook.Model.StorePlan;
import com.example.sarkaribook.Model.Subscription;
import com.example.sarkaribook.R;
import com.example.sarkaribook.Retrofit.ApiInterface;
import com.example.sarkaribook.TinyDB;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class SubscriptionsActivity extends AppCompatActivity implements SubscriptionPlanAdapter.ItemClickListener {

    RecyclerView subscriptionRecyclerView;
    List<Subscription> subscriptionList;
    final int UPI_PAYMENT = 0;
    private ItemClickListener itemClickListener;
    String duration ,amount;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_subscriptions);


        subscriptionRecyclerView = findViewById(R.id.subscriptionsPlansRecyclerView);

        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        subscriptionRecyclerView.setLayoutManager(linearLayoutManager);

        subscriptionList = new ArrayList<>();

        subscriptionList.add(new Subscription("Buy For 30 Days","In Just Rs 99"));
        subscriptionList.add(new Subscription("Buy For 60 Days", "In Just Rs 199"));
        subscriptionList.add(new Subscription("Buy For 120 Days","In Just Rs 299"));
        subscriptionList.add(new Subscription("Buy For 180 Days","In Just Rs 399"));

        SubscriptionPlanAdapter adapter = new SubscriptionPlanAdapter(subscriptionList,this,this);
        subscriptionRecyclerView.setAdapter(adapter);



    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        switch (requestCode) {
            case UPI_PAYMENT:
                if ((RESULT_OK == resultCode) || (resultCode == 11)) {
                    if (data != null) {
                        String trxt = data.getStringExtra("response");
                        Log.d("UPI", "onActivityResult: " + trxt);
                        ArrayList<String> dataList = new ArrayList<>();
                        dataList.add(trxt);
                        upiPaymentDataOperation(dataList);
                    } else {
                        Log.d("UPI", "onActivityResult: " + "Return data is null");
                        ArrayList<String> dataList = new ArrayList<>();
                        dataList.add("nothing");
                        upiPaymentDataOperation(dataList);
                    }
                } else {
                    Log.d("UPI", "onActivityResult: " + "Return data is null"); //when user simply back without payment
                    ArrayList<String> dataList = new ArrayList<>();
                    dataList.add("nothing");
                    upiPaymentDataOperation(dataList);
                }
                break;
        }
    }

    private void upiPaymentDataOperation(ArrayList<String> data) {
        if (isConnectionAvailable(this)) {
            String str = data.get(0);
            Log.d("UPIPAY", "upiPaymentDataOperation: "+str);
            String paymentCancel = "";
            if(str == null) str = "discard";
            String status = "";
            String approvalRefNo = "";
            String response[] = str.split("&");
            for (int i = 0; i < response.length; i++) {
                String equalStr[] = response[i].split("=");
                if(equalStr.length >= 2) {
                    if (equalStr[0].toLowerCase().equals("Status".toLowerCase())) {
                        status = equalStr[1].toLowerCase();
                    }
                    else if (equalStr[0].toLowerCase().equals("ApprovalRefNo".toLowerCase()) || equalStr[0].toLowerCase().equals("txnRef".toLowerCase())) {
                        approvalRefNo = equalStr[1];
                    }
                }
                else {
                    paymentCancel = "Payment cancelled by user.";
                }
            }

            if (status.equals("success")) {
                HttpLoggingInterceptor LOG = new HttpLoggingInterceptor();
                LOG.level(HttpLoggingInterceptor.Level.BODY);
                OkHttpClient okHttpClient = new OkHttpClient.Builder().addInterceptor(LOG).build();

                Retrofit retrofit = new Retrofit.Builder()
                        .baseUrl(BASE_URL) // Specify your api here
                        .addConverterFactory(GsonConverterFactory.create())
                        .client(okHttpClient)
                        .build();

                ApiInterface api = retrofit.create(ApiInterface.class);

                TinyDB tinyDB = new TinyDB(getApplicationContext());
                int user_id = tinyDB.getInt("id");

                SimpleDateFormat sdf = new SimpleDateFormat("yyyy/MM/dd");
                Calendar c = Calendar.getInstance();
                c.setTime(new Date()); // Using today's date
                c.add(Calendar.DATE, Integer.parseInt(extractInt(duration))); // Adding 5 days
                String expiredDate = sdf.format(c.getTime());
                System.out.println(expiredDate);

                StorePlan userLogin = new StorePlan(Integer.parseInt(duration),user_id,extractInt(amount),expiredDate);
                Call<StorePlan> call = api.storePlanDetails(userLogin);

                call.enqueue(new Callback<StorePlan>() {
                    @Override
                    public void onResponse(Call<StorePlan> call, Response<StorePlan> response) {
                        if(response.isSuccessful()){
                            tinyDB.putBoolean("isSubscribed",true);
                            Toast.makeText(SubscriptionsActivity.this,"Stored Successfully", Toast.LENGTH_SHORT).show();
                        }
                    }

                    @Override
                    public void onFailure(Call<StorePlan> call, Throwable t) {

                    }
                });

                // this method is called when transaction is successful and we are displaying a toast message.
                Toast.makeText(SubscriptionsActivity.this, "Transaction successfully completed..", Toast.LENGTH_SHORT).show();

                //Code to handle successful transaction here.
                Toast.makeText(this, "Transaction successful.", Toast.LENGTH_SHORT).show();
                Log.d("UPI", "responseStr: "+approvalRefNo);
            }
            else if("Payment cancelled by user.".equals(paymentCancel)) {
                Toast.makeText(this, "Payment cancelled by user.", Toast.LENGTH_SHORT).show();
                HttpLoggingInterceptor LOG = new HttpLoggingInterceptor();
                LOG.level(HttpLoggingInterceptor.Level.BODY);
                OkHttpClient okHttpClient = new OkHttpClient.Builder().addInterceptor(LOG).build();

                Retrofit retrofit = new Retrofit.Builder()
                        .baseUrl(BASE_URL) // Specify your api here
                        .addConverterFactory(GsonConverterFactory.create())
                        .client(okHttpClient)
                        .build();

                ApiInterface api = retrofit.create(ApiInterface.class);

                TinyDB tinyDB = new TinyDB(getApplicationContext());
                int user_id = tinyDB.getInt("id");

                SimpleDateFormat sdf = new SimpleDateFormat("yyyy/MM/dd");
                Calendar c = Calendar.getInstance();
                c.setTime(new Date()); // Using today's date


                c.add(Calendar.DATE, Integer.parseInt(duration)); // Adding 5 days

                String expiredDate = sdf.format(c.getTime());
                System.out.println(expiredDate);


                    StorePlan userLogin = new StorePlan(Integer.parseInt(extractInt(duration)), user_id, amount, expiredDate);
                    Call<StorePlan> call = api.storePlanDetails(userLogin);

                    call.enqueue(new Callback<StorePlan>() {
                        @Override
                        public void onResponse(Call<StorePlan> call, Response<StorePlan> response) {
                            if (response.isSuccessful()) {
                                tinyDB.putBoolean("isSubscribed", true);
                                Toast.makeText(SubscriptionsActivity.this, "Stored Successfully", Toast.LENGTH_SHORT).show();
                            }
                        }

                        @Override
                        public void onFailure(Call<StorePlan> call, Throwable t) {

                        }
                    });


                // this method is called when transaction is successful and we are displaying a toast message.
                Toast.makeText(SubscriptionsActivity.this, "Transaction successfully completed..", Toast.LENGTH_SHORT).show();

                //Code to handle successful transaction here.
                Toast.makeText(this, "Transaction successful.", Toast.LENGTH_SHORT).show();
                Log.d("UPI", "responseStr: "+approvalRefNo);

            }
            else {
                Toast.makeText(this, "Transaction failed.Please try again", Toast.LENGTH_SHORT).show();
            }
        } else {
            Toast.makeText(this, "Internet connection is not available. Please check and try again", Toast.LENGTH_SHORT).show();
        }
    }

    public static boolean isConnectionAvailable(Context context) {
        ConnectivityManager connectivityManager = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        if (connectivityManager != null) {
            NetworkInfo netInfo = connectivityManager.getActiveNetworkInfo();
            if (netInfo != null && netInfo.isConnected()
                    && netInfo.isConnectedOrConnecting()
                    && netInfo.isAvailable()) {
                return true;
            }
        }
        return false;
    }

    @Override
    public void onItemClick(Uri product,String duration1,String amount1) {

        duration = duration1;
        amount = amount1;
        Log.e("DUR",duration1);
        Log.e("AMO",amount1+".00");

        Intent upiPayIntent = new Intent(Intent.ACTION_VIEW);
        upiPayIntent.setData(product);

        // will always show a dialog to user to choose an app
        Intent chooser = Intent.createChooser(upiPayIntent, "Pay with");

        // check if intent resolves
        if(null != chooser.resolveActivity(getPackageManager())) {
            startActivityForResult(chooser, UPI_PAYMENT);
        } else {
            Toast.makeText(this,"No UPI app found, please install one to continue",Toast.LENGTH_SHORT).show();
        }
    }


    public interface ItemClickListener {
        public void onItemClick(Uri uri);
    }

    static String extractInt(String str)
    {
        // Replacing every non-digit number
        // with a space(" ")
        str = str.replaceAll("[^\\d]", " ");

        // Remove extra spaces from the beginning
        // and the ending of the string
        str = str.trim();

        // Replace all the consecutive white
        // spaces with a single space
        str = str.replaceAll(" +", " ");

        if (str.equals(""))
            return "-1";

        return str;
    }



}