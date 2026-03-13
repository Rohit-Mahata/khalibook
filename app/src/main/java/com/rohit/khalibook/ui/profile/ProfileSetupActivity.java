package com.rohit.khalibook.ui.profile;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import com.rohit.khalibook.R;
import com.rohit.khalibook.data.local.db.AppDatabase;
import com.rohit.khalibook.data.local.entity.UserProfileEntity;
import com.rohit.khalibook.session.SessionManager;
import com.rohit.khalibook.MainActivity;

public class ProfileSetupActivity extends AppCompatActivity {

    private EditText etFirstName, etLastName, etPhone, etAddress;
    private TextView tvEmoji;
    private Button btnSave;

    private SessionManager sessionManager;
    private AppDatabase db;
    private final String[] emojis = {"😡", "😭", "😤", "🥺", "🤣", "🥵", "😈", "💀"};

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile_setup);

        sessionManager = new SessionManager(this);
        db = AppDatabase.getInstance(this);

        etFirstName = findViewById(R.id.etFirstName);
        etLastName  = findViewById(R.id.etLastName);
        etPhone     = findViewById(R.id.etPhone);
        etAddress   = findViewById(R.id.etAddress);
        tvEmoji     = findViewById(R.id.tvEmoji);
        btnSave     = findViewById(R.id.btnSaveProfile);

        tvEmoji.setOnClickListener(v -> showEmojiPicker());
        btnSave.setOnClickListener(v -> saveProfile());
        
        loadExistingProfile();
    }

    private void loadExistingProfile() {
        int userId = sessionManager.getUserId();
        UserProfileEntity profile = db.userProfileDao().getByUserId(userId);
        if (profile != null) {
            etFirstName.setText(profile.firstName);
            etLastName.setText(profile.lastName);
            etAddress.setText(profile.address);
            if (profile.emoji != null && !profile.emoji.isEmpty()) {
                tvEmoji.setText(profile.emoji);
            }
        }
    }

    private void showEmojiPicker() {
        new AlertDialog.Builder(this)
                .setTitle("Choose your mood")
                .setItems(emojis, (dialog, which) -> {
                    tvEmoji.setText(emojis[which]);
                })
                .show();
    }

    private void saveProfile() {
        String firstName = etFirstName.getText().toString().trim();
        String lastName = etLastName.getText().toString().trim();
        String phone = etPhone.getText().toString().trim();
        String address = etAddress.getText().toString().trim();
        String emoji = tvEmoji.getText().toString();

        if (firstName.isEmpty() || lastName.isEmpty()) {
            Toast.makeText(this, "Please fill required fields", Toast.LENGTH_SHORT).show();
            return;
        }

        int userId = sessionManager.getUserId();
        if (userId == -1) {
            Toast.makeText(this, "Session error", Toast.LENGTH_SHORT).show();
            return;
        }

        UserProfileEntity profile = new UserProfileEntity();
        profile.userId = userId;
        profile.firstName = firstName;
        profile.lastName = lastName;
        profile.address = address;
        profile.emoji = emoji;

        db.userProfileDao().insert(profile);
        db.userDao().markProfileCompleted(userId);

        Toast.makeText(this, "Profile updated!", Toast.LENGTH_SHORT).show();
        startActivity(new Intent(this, MainActivity.class));
        finish();
    }
}
