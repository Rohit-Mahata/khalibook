package com.rohit.khalibook;

import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.rohit.khalibook.data.local.db.AppDatabase;
import com.rohit.khalibook.data.local.entity.CommentEntity;
import com.rohit.khalibook.data.local.entity.PostEntity;
import com.rohit.khalibook.data.local.entity.UserProfileEntity;
import com.rohit.khalibook.session.SessionManager;
import com.rohit.khalibook.ui.PostAdapter;
import com.rohit.khalibook.ui.auth.LoginActivity;
import com.rohit.khalibook.ui.profile.ProfileSetupActivity;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity implements PostAdapter.OnPostActionListener {

    private AppDatabase db;
    private SessionManager session;
    private PostAdapter adapter;
    private int userId;
    private String username;

    private EditText etSearch, etPostContent;
    private Button btnPost, btnLogout;
    private ImageButton btnProfile;
    private RecyclerView rvPosts;
    private TextView tvWelcome;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        db = AppDatabase.getInstance(this);
        session = new SessionManager(this);
        userId = session.getUserId();

        if (userId == -1) {
            startActivity(new Intent(this, LoginActivity.class));
            finish();
            return;
        }

        UserProfileEntity profile = db.userProfileDao().getByUserId(userId);
        username = (profile != null) ? profile.firstName + " " + profile.lastName : "User " + userId;

        initViews();
        setupRecyclerView();
        loadPosts();
    }

    private void initViews() {
        etSearch = findViewById(R.id.etSearch);
        etPostContent = findViewById(R.id.etPostContent);
        btnPost = findViewById(R.id.btnPost);
        btnProfile = findViewById(R.id.btnProfile);
        btnLogout = findViewById(R.id.btnLogout);
        rvPosts = findViewById(R.id.rvPosts);
        tvWelcome = findViewById(R.id.tvWelcome);

        btnPost.setOnClickListener(v -> createPost());
        btnProfile.setOnClickListener(v -> {
            Intent intent = new Intent(MainActivity.this, ProfileSetupActivity.class);
            startActivity(intent);
        });

        btnLogout.setOnClickListener(v -> {
            session.logout();
            Intent intent = new Intent(MainActivity.this, LoginActivity.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
            startActivity(intent);
            finish();
        });

        etSearch.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {}

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                searchPosts(s.toString());
            }

            @Override
            public void afterTextChanged(Editable s) {}
        });
    }

    private void setupRecyclerView() {
        adapter = new PostAdapter(new ArrayList<>(), userId, this);
        rvPosts.setLayoutManager(new LinearLayoutManager(this));
        rvPosts.setAdapter(adapter);
    }

    private void loadPosts() {
        List<PostEntity> posts = db.postDao().getAllPosts();
        adapter.setPosts(posts);
    }

    private void createPost() {
        String content = etPostContent.getText().toString().trim();
        if (content.isEmpty()) return;

        PostEntity post = new PostEntity();
        post.userId = userId;
        post.username = username;
        post.content = content;
        post.timestamp = System.currentTimeMillis();

        db.postDao().insert(post);
        etPostContent.setText("");
        loadPosts();
        Toast.makeText(this, "Posted!", Toast.LENGTH_SHORT).show();
    }

    private void searchPosts(String query) {
        if (query.isEmpty()) {
            loadPosts();
        } else {
            List<PostEntity> filteredPosts = db.postDao().searchPosts(query);
            adapter.setPosts(filteredPosts);
        }
    }

    @Override
    public void onLikeClick(PostEntity post) {
        db.postDao().incrementLikeCount(post.id);
        loadPosts();
    }

    @Override
    public void onCommentClick(PostEntity post) {
        showCommentDialog(post);
    }

    @Override
    public void onDeleteClick(PostEntity post) {
        new AlertDialog.Builder(this)
                .setTitle("Delete Post")
                .setMessage("Are you sure you want to delete this post?")
                .setPositiveButton("Delete", (dialog, which) -> {
                    db.postDao().deleteById(post.id);
                    loadPosts();
                })
                .setNegativeButton("Cancel", null)
                .show();
    }

    private void showCommentDialog(PostEntity post) {
        EditText etComment = new EditText(this);
        etComment.setHint("Write a comment...");

        List<CommentEntity> comments = db.commentDao().getCommentsForPost(post.id);
        StringBuilder sb = new StringBuilder();
        for (CommentEntity c : comments) {
            sb.append(c.username).append(": ").append(c.content).append("\n");
        }

        new AlertDialog.Builder(this)
                .setTitle("Comments")
                .setMessage(sb.toString())
                .setView(etComment)
                .setPositiveButton("Comment", (dialog, which) -> {
                    String content = etComment.getText().toString().trim();
                    if (!content.isEmpty()) {
                        CommentEntity comment = new CommentEntity();
                        comment.postId = post.id;
                        comment.userId = userId;
                        comment.username = username;
                        comment.content = content;
                        comment.timestamp = System.currentTimeMillis();
                        db.commentDao().insert(comment);
                        db.postDao().incrementCommentCount(post.id);
                        loadPosts();
                        Toast.makeText(MainActivity.this, "Commented!", Toast.LENGTH_SHORT).show();
                    }
                })
                .setNegativeButton("Close", null)
                .show();
    }
}
