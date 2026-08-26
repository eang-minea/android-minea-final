package com.example.android_eangminea_final;

import android.content.res.ColorStateList;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.core.widget.ImageViewCompat;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class CategoryAdapter extends RecyclerView.Adapter<CategoryAdapter.CategoryViewHolder> {

    private final List<Category> categoryList;
    private int selectedPosition = 0;
    private OnCategoryClickListener listener;

    public interface OnCategoryClickListener {
        void onCategoryClick(Category category, int position);
    }

    public CategoryAdapter(List<Category> categoryList, OnCategoryClickListener listener) {
        this.categoryList = categoryList;
        this.listener = listener;
        for (int i = 0; i < categoryList.size(); i++) {
            if (categoryList.get(i).isSelected()) {
                selectedPosition = i;
                break;
            }
        }
    }

    @NonNull
    @Override
    public CategoryViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_category, parent, false);
        return new CategoryViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull CategoryViewHolder holder, int position) {
        Category category = categoryList.get(position);
        boolean isSelected = position == selectedPosition;

        holder.txtCategoryName.setText(category.getName());
        holder.imgCategoryIcon.setImageResource(category.getIconRes());

        if (isSelected) {
            holder.frameCategoryIcon.setBackgroundResource(R.drawable.bg_category_selected);
            ImageViewCompat.setImageTintList(holder.imgCategoryIcon, ColorStateList.valueOf(Color.WHITE));
            holder.txtCategoryName.setTextColor(ContextCompat.getColor(holder.itemView.getContext(), R.color.text_dark));
            holder.txtCategoryName.setTypeface(null, android.graphics.Typeface.BOLD);
        } else {
            holder.frameCategoryIcon.setBackgroundResource(R.drawable.bg_category_unselected);
            ImageViewCompat.setImageTintList(holder.imgCategoryIcon, ColorStateList.valueOf(ContextCompat.getColor(holder.itemView.getContext(), R.color.text_dark)));
            holder.txtCategoryName.setTextColor(ContextCompat.getColor(holder.itemView.getContext(), R.color.text_subtitle));
            holder.txtCategoryName.setTypeface(null, android.graphics.Typeface.NORMAL);
        }

        holder.itemView.setOnClickListener(v -> {
            int prev = selectedPosition;
            selectedPosition = holder.getAdapterPosition();
            notifyItemChanged(prev);
            notifyItemChanged(selectedPosition);
            if (listener != null) {
                listener.onCategoryClick(category, selectedPosition);
            }
        });
    }

    @Override
    public int getItemCount() {
        return categoryList == null ? 0 : categoryList.size();
    }

    public static class CategoryViewHolder extends RecyclerView.ViewHolder {
        FrameLayout frameCategoryIcon;
        ImageView imgCategoryIcon;
        TextView txtCategoryName;

        public CategoryViewHolder(@NonNull View itemView) {
            super(itemView);
            frameCategoryIcon = itemView.findViewById(R.id.frameCategoryIcon);
            imgCategoryIcon = itemView.findViewById(R.id.imgCategoryIcon);
            txtCategoryName = itemView.findViewById(R.id.txtCategoryName);
        }
    }
}