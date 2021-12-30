package com.example.sarkaribook.Retrofit;

import android.app.DownloadManager;

import com.example.sarkaribook.Model.Category;
import com.example.sarkaribook.Model.Ebook;
import com.example.sarkaribook.Model.StorePlan;
import com.example.sarkaribook.Model.Subcategory;
import com.example.sarkaribook.Model.Subscription;
import com.example.sarkaribook.Model.User;
import com.example.sarkaribook.Model.UserLogin;

import java.util.ArrayList;
import java.util.List;

import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.Headers;
import retrofit2.http.POST;
import retrofit2.http.Path;
import retrofit2.http.Query;

public interface ApiInterface {



    @GET("Categories/index")
    Call<List<Category>> getCategories();

    @GET("Subcat/sub_cat/{id}")
    Call<List<Subcategory>> getSubcategories(@Path("id") String id);

    @GET("Categories/books/{id}")
    Call<List<Ebook>> getEbookList(@Path("id") int id);

    @GET("Categories/paid_books")
    Call<List<Ebook>> getAllPaidEbookList();

    @Headers("Content-Type: application/json;charset=utf-8")
    @POST("Users/verify/contact_number/{contact_number}")
    Call<UserLogin> registerUser(@Path("contact_number") String c_n,@Body UserLogin userLogin);

    @Headers("Content-Type: application/json;charset=utf-8")
    @POST("Users/store_subscription/")
    Call<StorePlan> storePlanDetails(@Body StorePlan storePlan);

    @GET("Users/index")
    Call<List<User>> getUsers();

    @GET("users/check_subscription/{id}")
    Call<Res> getSubscriptionStatus(@Path("id") int id);

    @GET("Users/check_user/{id}")
    Call<Res> getUserActiveOrNot(@Path("id") int id);

    @GET("Categories/allbooks/")
    Call<List<Ebook>> getAllEbookList();

    @GET("Categories/paid_books")
    Call<List<Subscription>> subscriptionPlansList();

    @GET("Categories/youtube/")
    Call<List<Ebook>> getYoutubeLinks();




// Google Doc Link :
// https://docs.google.com/document/d/11j5EzYd6W-FyyO_jnbCVPWiCwbrqfgsOUhVgnNVwh_A/edit?usp=sharing

// Drive Pdf Link
// https://drive.google.com/file/d/1npPfmFuglaOivm6LkCBwQMt5T1J2XFA1/view?usp=sharing

}
