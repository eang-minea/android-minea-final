package com.example.android_eangminea_final;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions;

import java.util.List;

/**
 * Slider adapter that supports both local drawable resource IDs
 * and remote image URLs (loaded via Glide).
 */
public class ImageSliderAdapter extends RecyclerView.Adapter<ImageSliderAdapter.SliderViewHolder> {

    private List<Integer> imageResList;
    private List<String> imageUrlList;
    private final boolean isUrlMode;

    // Constructor for local drawable resources
    public ImageSliderAdapter(List<Integer> imageList) {
        this.imageResList = imageList;
        this.isUrlMode = false;
    }

    // Constructor for remote image URLs
    public ImageSliderAdapter(List<String> imageUrls, boolean isUrl) {
        this.imageUrlList = imageUrls;
        this.isUrlMode = true;
    }

    @NonNull
    @Override
    public SliderViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_slider_image, parent, false);
        return new SliderViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull SliderViewHolder holder, int position) {
        if (isUrlMode) {
            String imageUrl = imageUrlList.get(position);
            Glide.with(holder.itemView.getContext())
                    .load(imageUrl)
                    .transition(DrawableTransitionOptions.withCrossFade())
                    .centerCrop()
                    .into(holder.imgSlider);
        } else {
            int imageRes = imageResList.get(position);
            holder.imgSlider.setImageResource(imageRes);
        }
    }

    @Override
    public int getItemCount() {
        if (isUrlMode) {
            return imageUrlList == null ? 0 : imageUrlList.size();
        }
        return imageResList == null ? 0 : imageResList.size();
    }

    public static class SliderViewHolder extends RecyclerView.ViewHolder {
        ImageView imgSlider;

        public SliderViewHolder(@NonNull View itemView) {
            super(itemView);
            imgSlider = itemView.findViewById(R.id.imgSlider);
        }
    }
}