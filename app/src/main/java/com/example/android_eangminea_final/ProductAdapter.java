package com.example.android_eangminea_final;

import android.content.Intent;
import android.graphics.Paint;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions;

import java.util.List;

public class ProductAdapter extends RecyclerView.Adapter<ProductAdapter.ProductViewHolder> {

    private final List<Product> productList;
    private OnProductClickListener listener;

    public interface OnProductClickListener {
        void onProductClick(Product product);
    }

    public ProductAdapter(List<Product> productList) {
        this.productList = productList;
    }

    public ProductAdapter(List<Product> productList, OnProductClickListener listener) {
        this.productList = productList;
        this.listener = listener;
    }

    public void setOnProductClickListener(OnProductClickListener listener) {
        this.listener = listener;
    }

    @NonNull
    @Override
    public ProductViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_product, parent, false);
        return new ProductViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ProductViewHolder holder, int position) {
        Product product = productList.get(position);
        holder.bind(product, listener);
    }

    @Override
    public int getItemCount() {
        return productList == null ? 0 : productList.size();
    }

    public static class ProductViewHolder extends RecyclerView.ViewHolder {

        private final ImageView imgProduct;
        private final TextView txtProductTitle;
        private final TextView txtProductDescription;
        private final TextView txtDiscountedPrice;
        private final TextView txtOriginalPrice;

        public ProductViewHolder(@NonNull View itemView) {
            super(itemView);
            imgProduct = itemView.findViewById(R.id.imgProduct);
            txtProductTitle = itemView.findViewById(R.id.txtProductTitle);
            txtProductDescription = itemView.findViewById(R.id.txtProductDescription);
            txtDiscountedPrice = itemView.findViewById(R.id.txtDiscountedPrice);
            txtOriginalPrice = itemView.findViewById(R.id.txtOriginalPrice);
        }

        public void bind(Product product, OnProductClickListener listener) {
            // Load image: use Glide for remote URLs, fallback to local drawable
            if (product.hasRemoteImages()) {
                Glide.with(itemView.getContext())
                        .load(product.getThumbnailUrl())
                        .transition(DrawableTransitionOptions.withCrossFade())
                        .centerCrop()
                        .into(imgProduct);
            } else {
                imgProduct.setImageResource(product.getMainImage());
            }

            txtProductTitle.setText(product.getTitle());
            txtProductDescription.setText(product.getDescription());
            txtDiscountedPrice.setText(product.getDiscountedPrice());

            txtOriginalPrice.setText(product.getOriginalPrice());
            txtOriginalPrice.setPaintFlags(txtOriginalPrice.getPaintFlags() | Paint.STRIKE_THRU_TEXT_FLAG);

            itemView.setOnClickListener(v -> {
                if (listener != null) {
                    listener.onProductClick(product);
                } else {
                    Intent intent = new Intent(v.getContext(), ProductDetailActivity.class);
                    intent.putExtra("product", product);
                    v.getContext().startActivity(intent);
                }
            });
        }
    }
}