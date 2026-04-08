package com.example.a0804.utils;

import android.util.Log;
import com.example.a0804.models.Movie;
import com.example.a0804.models.Showtime;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.ArrayList;
import java.util.List;

public class DataInitializer {

    public static void seedDataIfEmpty() {
        FirebaseFirestore db = FirebaseFirestore.getInstance();

        db.collection(Constants.COL_MOVIES).limit(1).get().addOnCompleteListener(task -> {
            if (task.isSuccessful() && task.getResult().isEmpty()) {
                Log.d("DataInitializer", "Database is empty, seeding with local images...");
                seedMoviesWithLocalDrawables(db);
            }
        });
    }

    private static void seedMoviesWithLocalDrawables(FirebaseFirestore db) {
        List<Movie> defaultMovies = new ArrayList<>();
        
        // Lưu ý: Chúng ta lưu tên file drawable vào field posterUrl để Adapter xử lý
        defaultMovies.add(new Movie(null, "Spider-Man: No Way Home", 
                "docter_strange", 
                "Peter yêu cầu Doctor Strange giúp đỡ sau khi danh tính bị tiết lộ.", 
                "Hành động", 4.8));
        
        defaultMovies.add(new Movie(null, "Oppenheimer", 
                "robert", 
                "Câu chuyện về 'cha đẻ' của bom nguyên tử J. Robert Oppenheimer.", 
                "Chính kịch", 4.9));

        defaultMovies.add(new Movie(null, "Nobita và Bản giao hưởng Địa cầu", 
                "nobita", 
                "Chuyến phiêu lưu âm nhạc mới của Nobita và nhóm bạn.", 
                "Hoạt hình", 4.5));

        for (Movie movie : defaultMovies) {
            db.collection(Constants.COL_MOVIES).add(movie).addOnSuccessListener(doc -> {
                createDefaultShowtimes(db, doc.getId(), movie.getTitle());
            });
        }
    }

    private static void createDefaultShowtimes(FirebaseFirestore db, String movieId, String movieTitle) {
        String[] theaters = {"CGV Vincom", "Lotte Cinema", "BHD Star"};
        for (int i = 0; i < 2; i++) {
            Showtime showtime = new Showtime(null, movieId, "th_" + i, theaters[i], "19:00 - Hôm nay", 85000.0);
            db.collection(Constants.COL_SHOWTIMES).add(showtime);
        }
    }
}
