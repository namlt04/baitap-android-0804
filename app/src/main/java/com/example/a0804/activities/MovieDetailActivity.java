package com.example.a0804.activities;

import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.a0804.R;
import com.example.a0804.adapters.ShowtimeAdapter;
import com.example.a0804.models.Movie;
import com.example.a0804.models.Showtime;
import com.example.a0804.utils.Constants;
import com.example.a0804.utils.ImageLoader;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;

import java.util.ArrayList;
import java.util.List;

public class MovieDetailActivity extends AppCompatActivity {

    private ImageView ivPosterDetail;
    private TextView tvTitleDetail, tvGenreDetail, tvDescription;
    private RecyclerView rvShowtimes;
    private ShowtimeAdapter showtimeAdapter;
    private List<Showtime> showtimeList;
    private FirebaseFirestore db;
    private Movie movie;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_movie_detail);

        movie = (Movie) getIntent().getSerializableExtra("movie");
        if (movie == null) {
            finish();
            return;
        }

        db = FirebaseFirestore.getInstance();

        ivPosterDetail = findViewById(R.id.ivPosterDetail);
        tvTitleDetail = findViewById(R.id.tvTitleDetail);
        tvGenreDetail = findViewById(R.id.tvGenreDetail);
        tvDescription = findViewById(R.id.tvDescription);
        rvShowtimes = findViewById(R.id.rvShowtimes);

        tvTitleDetail.setText(movie.getTitle());
        tvGenreDetail.setText(movie.getGenre());
        tvDescription.setText(movie.getDescription());

        // Sử dụng ImageLoader cho màn hình chi tiết
        ImageLoader.loadImage(this, movie.getPosterUrl(), ivPosterDetail);

        showtimeList = new ArrayList<>();
        showtimeAdapter = new ShowtimeAdapter(this, showtimeList);
        rvShowtimes.setLayoutManager(new LinearLayoutManager(this));
        rvShowtimes.setAdapter(showtimeAdapter);

        fetchShowtimes();
    }

    private void fetchShowtimes() {
        db.collection(Constants.COL_SHOWTIMES)
                .whereEqualTo("movieId", movie.getId())
                .get()
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        showtimeList.clear();
                        for (QueryDocumentSnapshot document : task.getResult()) {
                            Showtime showtime = document.toObject(Showtime.class);
                            showtime.setId(document.getId());
                            showtimeList.add(showtime);
                        }
                        showtimeAdapter.notifyDataSetChanged();
                    } else {
                        Toast.makeText(MovieDetailActivity.this, "Error fetching showtimes", Toast.LENGTH_SHORT).show();
                    }
                });
    }
}
