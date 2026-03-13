package com.rohit.khalibook.ui.auth;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.rohit.khalibook.MainActivity;
import com.rohit.khalibook.R;
import com.rohit.khalibook.data.local.db.AppDatabase;
import com.rohit.khalibook.data.local.entity.UserEntity;
import com.rohit.khalibook.ui.profile.ProfileSetupActivity;
import com.rohit.khalibook.session.SessionManager;

public class LoginActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle b) {
        super.onCreate(b);
        setContentView(R.layout.activity_login);

        EditText etPhone = findViewById(R.id.etPhone);
        EditText etPassword = findViewById(R.id.etPassword);
        Button btnLogin = findViewById(R.id.btnLogin);
        TextView tvRegister = findViewById(R.id.tvRegister);

        btnLogin.setOnClickListener(v -> {
            UserEntity user = AppDatabase.getInstance(this)
                    .userDao()
                    .login(etPhone.getText().toString(), etPassword.getText().toString());

            if (user == null) {
                Toast.makeText(this, "Invalid login", Toast.LENGTH_SHORT).show();
                return;
            }

            SessionManager sm = new SessionManager(this);
            sm.login(user.id);

            if (user.profileCompleted == 0) {
                startActivity(new Intent(this, ProfileSetupActivity.class));
            } else {
                startActivity(new Intent(this, MainActivity.class));
            }
            finish();
        });

        tvRegister.setOnClickListener(v -> {
            startActivity(new Intent(this, RegisterActivity.class));
        });
    }
}
