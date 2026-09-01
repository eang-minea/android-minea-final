package com.example.android_eangminea_final;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

public class SignUpActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);

        Button btnCreateAccount = findViewById(R.id.btn_create_account);
        TextView tvLogin = findViewById(R.id.tv_login);

        btnCreateAccount.setOnClickListener(v -> {
            // For now, go straight to main activity
            startActivity(new Intent(SignUpActivity.this, MainActivity.class));
            finishAffinity(); // Close all activities and go to Main
        });

        tvLogin.setOnClickListener(v -> {
            finish(); // Go back to Login Activity
        });
    }
}
