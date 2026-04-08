package com.example.a0804;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.a0804.activities.LoginActivity;
import com.example.a0804.adapters.MovieAdapter;
import com.example.a0804.models.Movie;
import com.example.a0804.utils.Constants;
import com.example.a0804.utils.DataInitializer;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    private RecyclerView rvMovies;
    private MovieAdapter movieAdapter;
    private List<Movie> movieList;
    private FirebaseFirestore db;
    private ProgressBar progressBar;
    private FirebaseAuth mAuth;
    private Toolbar toolbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mAuth = FirebaseAuth.getInstance();
        if (mAuth.getCurrentUser() == null) {
            goToLogin();
            return;
        }

        // Xin quyền thông báo cho Android 13+
        requestNotificationPermission();

        toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        DataInitializer.seedDataIfEmpty();

        db = FirebaseFirestore.getInstance();
        rvMovies = findViewById(R.id.rvMovies);
        progressBar = findViewById(R.id.progressBar);

        movieList = new ArrayList<>();
        movieAdapter = new MovieAdapter(this, movieList);
        rvMovies.setLayoutManager(new GridLayoutManager(this, 2));
        rvMovies.setAdapter(movieAdapter);

        fetchMovies();
    }

    private void requestNotificationPermission() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            if (ContextCompat.checkSelfPermission(this, Manifest.permission.POST_NOTIFICATIONS) 
                    != PackageManager.PERMISSION_GRANTED) {
                ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.POST_NOTIFICATIONS}, 101);
            }
        }
    }

    private void goToLogin() {
        startActivity(new Intent(this, LoginActivity.class));
        finish();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if (item.getItemId() == R.id.menu_logout) {
            mAuth.signOut();
            goToLogin();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    private void fetchMovies() {
        progressBar.setVisibility(View.VISIBLE);
        db.collection(Constants.COL_MOVIES).get().addOnCompleteListener(task -> {
            progressBar.setVisibility(View.GONE);
            if (task.isSuccessful()) {
                movieList.clear();
                for (QueryDocumentSnapshot document : task.getResult()) {
                    Movie movie = document.toObject(Movie.class);
                    movie.setId(document.getId());
                    movieList.add(movie);
                }
                movieAdapter.notifyDataSetChanged();
            }
        });
    }
}
