package com.example.a0804.utils;

import android.content.Context;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.example.a0804.R;

public class ImageLoader {
    public static void loadImage(Context context, String posterPath, ImageView imageView) {
        if (posterPath == null) return;

        // Kiểm tra xem posterPath có phải là tên drawable không
        int drawableId = context.getResources().getIdentifier(posterPath, "drawable", context.getPackageName());

        if (drawableId != 0) {
            // Nếu tìm thấy trong drawable
            Glide.with(context)
                    .load(drawableId)
                    .placeholder(android.R.color.darker_gray)
                    .into(imageView);
        } else {
            // Nếu không tìm thấy (giả định là URL)
            Glide.with(context)
                    .load(posterPath)
                    .placeholder(android.R.color.darker_gray)
                    .into(imageView);
        }
    }
}
