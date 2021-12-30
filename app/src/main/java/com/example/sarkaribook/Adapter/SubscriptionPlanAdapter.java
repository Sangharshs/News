package com.example.sarkaribook.Adapter;

import static com.example.sarkaribook.Retrofit.ApiUtils.BASE_URL;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.sarkaribook.AllActivities.OtpVerifyActivity;
import com.example.sarkaribook.AllActivities.SubscriptionsActivity;
import com.example.sarkaribook.Model.StorePlan;
import com.example.sarkaribook.Model.Subscription;
import com.example.sarkaribook.Model.UserLogin;
import com.example.sarkaribook.R;
import com.example.sarkaribook.Retrofit.ApiInterface;
import com.example.sarkaribook.TinyDB;
import com.gpfreetech.IndiUpi.IndiUpi;
import com.gpfreetech.IndiUpi.entity.TransactionResponse;
import com.shreyaspatil.EasyUpiPayment.EasyUpiPayment;
import com.shreyaspatil.EasyUpiPayment.listener.PaymentStatusListener;
import com.shreyaspatil.EasyUpiPayment.model.TransactionDetails;

import org.w3c.dom.Text;

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


public class SubscriptionPlanAdapter extends RecyclerView.Adapter<SubscriptionPlanAdapter.viewHolder>{

    List<Subscription> subscriptionList;
    Activity activity;
    String passThisForDuration;
    String passThisForAmount;

    ItemClickListener itemClickListener;

    public SubscriptionPlanAdapter(List<Subscription> subscriptionList, Activity context, ItemClickListener itemClickListener) {
        this.subscriptionList = subscriptionList;
        this.activity = context;
        this.itemClickListener = itemClickListener;
    }

    @NonNull
    @Override
    public viewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_buy_subscription,parent,false);
        return new viewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull viewHolder holder, @SuppressLint("RecyclerView") int position) {

        holder.priceTextView.setText(subscriptionList.get(position).getAmountText());
        holder.durationTextView.setText(subscriptionList.get(position).getMonthText());
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                passThisForAmount = subscriptionList.get(position).getAmountText();
                passThisForDuration = subscriptionList.get(position).getMonthText();
                extractInt(passThisForAmount);
                //makePayment(extractInt(passThisForDuration)+".00", "SAIHOTEL1@icici", "SarkariLibrary", "desc", "0");
               payUsingUpi(extractInt(passThisForDuration), "SAIHOTEL1@icici", "SarkariLibrary", "desc");
               // payUsingUpi(extractInt(passThisForDuration)+".00", "SAIHOTEL1@icici","SarkariLibrary"," note");
            }
        });
    }

    void payUsingUpi(String amount, String upiId, String name, String note) {

        Uri uri = Uri.parse("upi://pay").buildUpon()
                .appendQueryParameter("pa", upiId)
                .appendQueryParameter("pn", name)
                .appendQueryParameter("tn", note)
                .appendQueryParameter("am", amount)
                .appendQueryParameter("cu", "INR")
                .build();
        itemClickListener.onItemClick(uri,extractInt(passThisForDuration),extractInt(passThisForAmount));
    }





    private void makePayment(String amount, String upi, String name, String desc, String transactionId) {
        //amount = format of amount should be in decimal format x.x (eg 530.00)

// note: always create new instance of UpiPayment for every new payment/order


        Toast.makeText(activity, amount, Toast.LENGTH_SHORT).show();

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

    @Override
    public int getItemCount() {
        return subscriptionList.size();
    }

    public class viewHolder extends RecyclerView.ViewHolder {
        TextView durationTextView,priceTextView;
        public viewHolder(@NonNull View itemView) {
            super(itemView);
            durationTextView = itemView.findViewById(R.id.getDurationTextView);
            priceTextView = itemView.findViewById(R.id.getSubscriptionPrice);
        }
    }

//    @Override
//    public void onTransactionCompleted(TransactionDetails transactionDetails) {
//        // on below line we are getting details about transaction when completed.
//        String transcDetails = transactionDetails.getStatus().toString() + "\n" + "Transaction ID : " + transactionDetails.getTransactionId();
//        // on below line we are setting details to our text view.
//
//    }
//
//    @Override
//    public void onTransactionSuccess() {
//        HttpLoggingInterceptor LOG = new HttpLoggingInterceptor();
//        LOG.level(HttpLoggingInterceptor.Level.BODY);
//        OkHttpClient okHttpClient = new OkHttpClient.Builder().addInterceptor(LOG).build();
//
//        Retrofit retrofit = new Retrofit.Builder()
//                .baseUrl(BASE_URL) // Specify your api here
//                .addConverterFactory(GsonConverterFactory.create())
//                .client(okHttpClient)
//                .build();
//
//        ApiInterface api = retrofit.create(ApiInterface.class);
//
//        TinyDB tinyDB = new TinyDB(activity.getApplicationContext());
//        int user_id = tinyDB.getInt("id");
//
//        SimpleDateFormat sdf = new SimpleDateFormat("yyyy/MM/dd");
//        Calendar c = Calendar.getInstance();
//        c.setTime(new Date()); // Using today's date
//        c.add(Calendar.DATE, Integer.parseInt(extractInt(passThisForDuration))); // Adding 5 days
//        String expiredDate = sdf.format(c.getTime());
//        System.out.println(expiredDate);
//
//        StorePlan userLogin = new StorePlan(Integer.parseInt(extractInt(passThisForDuration)),user_id,extractInt(passThisForAmount),expiredDate);
//        Call<StorePlan> call = api.storePlanDetails(userLogin);
//
//        call.enqueue(new Callback<StorePlan>() {
//            @Override
//            public void onResponse(Call<StorePlan> call, Response<StorePlan> response) {
//                if(response.isSuccessful()){
//                    tinyDB.putBoolean("isSubscribed",true);
//                    Toast.makeText(activity,"Stored Successfully", Toast.LENGTH_SHORT).show();
//                }
//            }
//
//            @Override
//            public void onFailure(Call<StorePlan> call, Throwable t) {
//
//            }
//        });
//
//        // this method is called when transaction is successful and we are displaying a toast message.
//        Toast.makeText(activity, "Transaction successfully completed..", Toast.LENGTH_SHORT).show();
//    }
//
//    @Override
//    public void onTransactionSubmitted() {
//        // this method is called when transaction is done
//        // but it may be successful or failure.
//        Log.e("TAG", "TRANSACTION SUBMIT");
//    }
//
//    @Override
//    public void onTransactionFailed() {
//        // this method is called when transaction is failure.
//        Toast.makeText(activity, "Failed to complete transaction", Toast.LENGTH_SHORT).show();
//    }
//    @Override
//    public void onTransactionCancelled() {
//
//        extractInt(passThisForDuration);
//
////        HttpLoggingInterceptor LOG = new HttpLoggingInterceptor();
////        LOG.level(HttpLoggingInterceptor.Level.BODY);
////        OkHttpClient okHttpClient = new OkHttpClient.Builder().addInterceptor(LOG).build();
////
////        Retrofit retrofit = new Retrofit.Builder()
////                .baseUrl(BASE_URL) // Specify your api here
////                .addConverterFactory(GsonConverterFactory.create())
////                .client(okHttpClient)
////                .build();
////
////        ApiInterface api = retrofit.create(ApiInterface.class);
////
////        TinyDB tinyDB = new TinyDB(activity.getApplicationContext());
////        int user_id = tinyDB.getInt("id");
////
////        SimpleDateFormat sdf = new SimpleDateFormat("yyyy/MM/dd");
////        Calendar c = Calendar.getInstance();
////        c.setTime(new Date()); // Using today's date
////        c.add(Calendar.DATE, Integer.parseInt(extractInt(passThisForDuration))); // Adding 5 days
////        String expiredDate = sdf.format(c.getTime());
////        System.out.println(expiredDate);
////
////        StorePlan userLogin = new StorePlan(Integer.parseInt(extractInt(passThisForDuration)),user_id,extractInt(passThisForAmount),expiredDate);
////        Call<StorePlan> call = api.storePlanDetails(userLogin);
////
////        call.enqueue(new Callback<StorePlan>() {
////            @Override
////            public void onResponse(Call<StorePlan> call, Response<StorePlan> response) {
////                if(response.isSuccessful()){
////                    tinyDB.putBoolean("isSubscribed",true);
////                    Toast.makeText(activity,"Stored Successfully", Toast.LENGTH_SHORT).show();
////                }
////            }
////
////            @Override
////            public void onFailure(Call<StorePlan> call, Throwable t) {
////
////            }
////        });
//        // this method is called when transaction is cancelled.
//        Toast.makeText(activity, "Transaction cancelled..", Toast.LENGTH_SHORT).show();
//    }
//
//    @Override
//    public void onAppNotFound() {
//        // this method is called when the users device is not having any app installed for making payment.
//        Toast.makeText(activity, "No app found for making transaction..", Toast.LENGTH_SHORT).show();
//    }
//
    public interface ItemClickListener {
        public void onItemClick(Uri product,String passthisforamount,String passthisforduration);
    }
}
