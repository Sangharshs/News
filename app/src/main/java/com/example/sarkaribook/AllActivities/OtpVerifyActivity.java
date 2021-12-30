package com.example.sarkaribook.AllActivities;

import static com.example.sarkaribook.Retrofit.ApiUtils.BASE_URL;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.example.sarkaribook.Model.User;
import com.example.sarkaribook.Model.UserLogin;
import com.example.sarkaribook.R;
import com.example.sarkaribook.Retrofit.ApiInterface;
import com.example.sarkaribook.Retrofit.Res;
import com.example.sarkaribook.TinyDB;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.FirebaseException;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.PhoneAuthCredential;
import com.google.firebase.auth.PhoneAuthProvider;

import java.util.concurrent.TimeUnit;

import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class OtpVerifyActivity extends AppCompatActivity {

    Button verifiOtpBtn;
    private FirebaseAuth mAuth;

    // variable for our text input
    // field for phone and OTP.
    private EditText  edtOTP;

    // buttons for generating OTP and verifying OTP
    private Button verifyOTPBtn, generateOTPBtn;

    // string for storing our verification ID
    private String verificationId;

    private String number,name,email,phone;

    TinyDB tinyDB;
    ProgressBar progressBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_otp_verify);

        verifiOtpBtn = findViewById(R.id.verifyBtn);
        edtOTP = findViewById(R.id.otpEditText);
        mAuth = FirebaseAuth.getInstance();
        progressBar = findViewById(R.id.progressBar);

        tinyDB = new TinyDB(getApplicationContext());

        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,WindowManager.LayoutParams.FLAG_FULLSCREEN);

        number = getIntent().getStringExtra("number");
        name = getIntent().getStringExtra("name");
        email = getIntent().getStringExtra("email");
        phone = "+91" + number;
        progressBar.setVisibility(View.GONE);

        sendVerificationCode(phone);
        Log.e("NUM",phone);
        verifiOtpBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if(TextUtils.isEmpty(edtOTP.getText().toString())){
                    Toast.makeText(OtpVerifyActivity.this, "Enter Otp", Toast.LENGTH_SHORT).show();
                }else{
                    progressBar.setVisibility(View.VISIBLE);
                    verifyCode(edtOTP.getText().toString());
                }
            }
        });

    }

    private void signInWithCredential(PhoneAuthCredential credential) {
        // inside this method we are checking if
        // the code entered is correct or not.
        mAuth.signInWithCredential(credential)
                .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            // if the code is correct and the task is successful
                            // we are sending our user to new activity.
                            Intent i = new Intent(OtpVerifyActivity.this, MainActivity.class);
                            tinyDB.putBoolean("isLogin",true);
                            tinyDB.putString("name",name);
                            tinyDB.putString("number",number);
                            tinyDB.putString("email",email);
                            progressBar.setVisibility(View.GONE);
                            HttpLoggingInterceptor LOG = new HttpLoggingInterceptor();
                            LOG.level(HttpLoggingInterceptor.Level.BODY);
                            OkHttpClient okHttpClient = new OkHttpClient.Builder().addInterceptor(LOG).build();

                            Retrofit retrofit = new Retrofit.Builder()
                                    .baseUrl(BASE_URL) // Specify your api here
                                    .addConverterFactory(GsonConverterFactory.create())
                                    .client(okHttpClient)
                                    .build();

                            ApiInterface api = retrofit.create(ApiInterface.class);

                            String user_name = tinyDB.getString("name");
                            String email = tinyDB.getString("email");
                            String number = tinyDB.getString("number");

                            UserLogin userLogin = new UserLogin(user_name,email,number,"1");
                            Call<UserLogin> call = api.registerUser(number,userLogin);

                            call.enqueue(new Callback<UserLogin>() {
                                @Override
                                public void onResponse(Call<UserLogin> call, Response<UserLogin> response) {
                                    if(response.isSuccessful()){
                                        response.body().getId();
                                        tinyDB.putInt("id",response.body().getId());
                                        startActivity(i);
                                        finish();
                                    }
                                }

                                @Override
                                public void onFailure(Call<UserLogin> call, Throwable t) {
                                    progressBar.setVisibility(View.GONE);
                                }
                            });
                        } else {
                            // if the code is not correct then we are
                            // displaying an error message to the user.
                            Toast.makeText(OtpVerifyActivity.this, task.getException().getMessage(), Toast.LENGTH_LONG).show();
                        }
                    }
                });
    }


    private void sendVerificationCode(String number) {
        // this method is used for getting
        // OTP on user phone number.
        PhoneAuthProvider.getInstance().verifyPhoneNumber(number,
                60,
                TimeUnit.SECONDS,
                this,
                mCallBack);
    }

    // callback method is called on Phone auth provider.
    private PhoneAuthProvider.OnVerificationStateChangedCallbacks
            // initializing our callbacks for on
            // verification callback method.
            mCallBack = new PhoneAuthProvider.OnVerificationStateChangedCallbacks() {
        // below method is used when
        // OTP is sent from Firebase
        @Override
        public void onCodeSent(String s, PhoneAuthProvider.ForceResendingToken forceResendingToken) {
            super.onCodeSent(s, forceResendingToken);
            // when we receive the OTP it
            // contains a unique id which
            // we are storing in our string
            // which we have already created.
            verificationId = s;
        }

        // this method is called when user
        // receive OTP from Firebase.
        @Override
        public void onVerificationCompleted(PhoneAuthCredential phoneAuthCredential) {
            // below line is used for getting OTP code
            // which is sent in phone auth credentials.
            final String code = phoneAuthCredential.getSmsCode();
            // checking if the code
            // is null or not.
            if (code != null) {
                // if the code is not null then
                // we are setting that code to
                // our OTP edittext field.
                edtOTP.setText(code);
                // after setting this code
                // to OTP edittext field we
                // are calling our verifycode method.
                verifyCode(code);

            }
        }

        // this method is called when firebase doesn't
        // sends our OTP code due to any error or issue.
        @Override
        public void onVerificationFailed(FirebaseException e) {
            // displaying error message with firebase exception.
            Toast.makeText(OtpVerifyActivity.this, e.getMessage(), Toast.LENGTH_LONG).show();
        }
    };

    // below method is use to verify code from Firebase.
    private void verifyCode(String code) {
        // below line is used for getting getting
        // credentials from our verification id and code.
        PhoneAuthCredential credential = PhoneAuthProvider.getCredential(verificationId, code);
        // after getting credential we are
        // calling sign in method.
        signInWithCredential(credential);

    }
}