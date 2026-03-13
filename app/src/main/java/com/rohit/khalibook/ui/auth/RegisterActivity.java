package com.rohit.khalibook.ui.auth;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.rohit.khalibook.R;
import com.rohit.khalibook.data.local.db.AppDatabase;
import com.rohit.khalibook.data.local.entity.UserEntity;

public class RegisterActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle b) {
        super.onCreate(b);
        setContentView(R.layout.activity_register);

        EditText etUsername = findViewById(R.id.etUsername);
        EditText etPhone = findViewById(R.id.etPhone);
        EditText etPassword = findViewById(R.id.etPassword);
        Button btnRegister = findViewById(R.id.btnRegister);

        btnRegister.setOnClickListener(v -> {
            if (AppDatabase.getInstance(this)
                    .userDao()
                    .getByPhone(etPhone.getText().toString()) != null) {
                Toast.makeText(this, "Phone already registered", Toast.LENGTH_SHORT).show();
                return;
            }

            UserEntity u = new UserEntity();
            u.username = etUsername.getText().toString();
            u.phone = etPhone.getText().toString();
            u.password = etPassword.getText().toString();
            u.profileCompleted = 0;

            AppDatabase.getInstance(this).userDao().insert(u);

            startActivity(new Intent(this, LoginActivity.class));
            finish();
        });
    }
}
