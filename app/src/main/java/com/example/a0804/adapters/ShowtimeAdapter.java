package com.example.a0804.adapters;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.a0804.R;
import com.example.a0804.activities.BookingSuccessActivity;
import com.example.a0804.models.Showtime;
import com.example.a0804.models.Ticket;
import com.example.a0804.utils.Constants;
import com.example.a0804.utils.NotificationHelper;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.List;

public class ShowtimeAdapter extends RecyclerView.Adapter<ShowtimeAdapter.ShowtimeViewHolder> {

    private Context context;
    private List<Showtime> showtimeList;
    private FirebaseFirestore db;
    private FirebaseAuth mAuth;

    public ShowtimeAdapter(Context context, List<Showtime> showtimeList) {
        this.context = context;
        this.showtimeList = showtimeList;
        this.db = FirebaseFirestore.getInstance();
        this.mAuth = FirebaseAuth.getInstance();
    }

    @NonNull
    @Override
    public ShowtimeViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_showtime, parent, false);
        return new ShowtimeViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ShowtimeViewHolder holder, int position) {
        Showtime showtime = showtimeList.get(position);
        holder.tvTheaterName.setText(showtime.getTheaterName());
        holder.tvTime.setText(showtime.getTime());
        holder.tvPrice.setText(String.format("%.0f VNĐ", showtime.getPrice()));

        holder.btnBook.setOnClickListener(v -> bookTicket(showtime));
    }

    private void bookTicket(Showtime showtime) {
        if (mAuth.getCurrentUser() == null) return;
        
        String userId = mAuth.getCurrentUser().getUid();
        String seatNumber = "A" + (int)(Math.random() * 50 + 1);

        Ticket ticket = new Ticket(null, userId, showtime.getId(), seatNumber, System.currentTimeMillis());

        db.collection(Constants.COL_TICKETS).add(ticket)
                .addOnSuccessListener(documentReference -> {
                    // 1. Nổ Notification ngay lập tức
                    NotificationHelper.sendImmediateNotification(context, "Phim tại " + showtime.getTheaterName(), seatNumber);

                    // 2. Chuyển sang màn hình thành công
                    Intent intent = new Intent(context, BookingSuccessActivity.class);
                    intent.putExtra("details", "Bạn đã đặt thành công ghế " + seatNumber + " tại " + showtime.getTheaterName() + ".\nSuất chiếu: " + showtime.getTime());
                    context.startActivity(intent);
                })
                .addOnFailureListener(e -> {
                    Toast.makeText(context, "Lỗi đặt vé: " + e.getMessage(), Toast.LENGTH_SHORT).show();
                });
    }

    @Override
    public int getItemCount() {
        return showtimeList.size();
    }

    public static class ShowtimeViewHolder extends RecyclerView.ViewHolder {
        TextView tvTheaterName, tvTime, tvPrice;
        Button btnBook;

        public ShowtimeViewHolder(@NonNull View itemView) {
            super(itemView);
            tvTheaterName = itemView.findViewById(R.id.tvTheaterName);
            tvTime = itemView.findViewById(R.id.tvShowtimeTime);
            tvPrice = itemView.findViewById(R.id.tvShowtimePrice);
            btnBook = itemView.findViewById(R.id.btnBook);
        }
    }
}
