
package com.example.sarkaribook.AllActivities;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.sarkaribook.R;
import com.example.sarkaribook.TinyDB;

import java.util.Locale;

public class RegisterActivity extends AppCompatActivity {

    private Button btnSubmit;
    private EditText nameEditText,emailEditText,numberEditText;
    TinyDB tinyDB;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);
        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);

        tinyDB = new TinyDB(getApplicationContext());

        if(tinyDB.getBoolean("isLogin")){
            startActivity(new Intent(RegisterActivity.this,MainActivity.class));
            finish();
        }

        btnSubmit = findViewById(R.id.submitBtn);
        nameEditText = findViewById(R.id.nameEditText);
        emailEditText = findViewById(R.id.emailEditText);
        numberEditText = findViewById(R.id.numberEditText);



        btnSubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String name = nameEditText.getText().toString();
                String email = emailEditText.getText().toString();
                String number = numberEditText.getText().toString();
                if (name.isEmpty()) {
                    showToast("Name Is Required");
                }else if(email.isEmpty()){
                    showToast("Email Is Required");
                } else if(number.length() != 10 && !number.isEmpty()) {
                    showToast("10 Digits Number Is Required");
                }else if(!name.isEmpty()&&!email.isEmpty()&&!number.isEmpty()){
                    Intent intent = new Intent(RegisterActivity.this,OtpVerifyActivity.class);
                    intent.putExtra("name",name);
                    intent.putExtra("number",number);
                    intent.putExtra("email",email);
                    startActivity(intent);
                    finish();
                }
            }
        });

    }

    private void showToast(String name_is_required) {

        Toast.makeText(this, name_is_required, Toast.LENGTH_SHORT).show();
    }
}