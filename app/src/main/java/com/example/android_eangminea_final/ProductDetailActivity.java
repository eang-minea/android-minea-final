package com.example.android_eangminea_final;

import android.graphics.Paint;
import android.graphics.drawable.GradientDrawable;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.viewpager2.widget.ViewPager2;

import java.util.ArrayList;
import java.util.List;

public class ProductDetailActivity extends AppCompatActivity {

    private ImageView btnBack;
    private ViewPager2 viewPagerImageSlider;
    private LinearLayout layoutSliderDots;

    private ImageView imgFavoriteHeart;
    private FrameLayout cardFavorite;

    private TextView txtDetailTitle;
    private TextView txtDetailSubtitle;
    private TextView txtDetailDiscountPrice;
    private TextView txtDetailOriginalPrice;
    private TextView txtDetailDescription;
    private TextView txtStockStatus;

    private Button btnAddToCart, btnBuyNow;

    private boolean isFavorite = true;

    // For local drawable images
    private List<Integer> sliderImages = new ArrayList<>();
    // For remote URL images
    private List<String> sliderImageUrls = new ArrayList<>();
    private boolean isRemoteProduct = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_product_detail);

        setupWindowInsets();
        initViews();
        bindProductData();
        setupImageSlider();
        setupListeners();
    }

    private void setupWindowInsets() {
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.detailRoot), (v, windowInsets) -> {
            Insets insets = windowInsets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(insets.left, insets.top, insets.right, 0);

            float density = getResources().getDisplayMetrics().density;
            findViewById(R.id.layoutDetailActions).setPadding(
                (int)(20 * density),
                (int)(12 * density),
                (int)(20 * density),
                (int)(16 * density) + insets.bottom
            );
            return windowInsets;
        });
    }

    private void initViews() {
        btnBack = findViewById(R.id.btnBack);
        viewPagerImageSlider = findViewById(R.id.viewPagerImageSlider);
        layoutSliderDots = findViewById(R.id.layoutSliderDots);

        imgFavoriteHeart = findViewById(R.id.imgFavoriteHeart);
        cardFavorite = findViewById(R.id.cardFavorite);

        txtDetailTitle = findViewById(R.id.txtDetailTitle);
        txtDetailSubtitle = findViewById(R.id.txtDetailSubtitle);
        txtDetailDiscountPrice = findViewById(R.id.txtDetailDiscountPrice);
        txtDetailOriginalPrice = findViewById(R.id.txtDetailOriginalPrice);
        txtDetailDescription = findViewById(R.id.txtDetailDescription);
        txtStockStatus = findViewById(R.id.txtStockStatus);

        btnAddToCart = findViewById(R.id.btnAddToCart);
        btnBuyNow = findViewById(R.id.btnBuyNow);
    }

    private void bindProductData() {
        Product product = (Product) getIntent().getSerializableExtra("product");
        if (product != null) {
            // Set title and subtitle
            txtDetailTitle.setText(product.getTitle());
            txtDetailSubtitle.setText(product.getDescription());

            // Set prices
            txtDetailDiscountPrice.setText(product.getDiscountedPrice());
            txtDetailOriginalPrice.setText(product.getOriginalPrice());
            txtDetailOriginalPrice.setPaintFlags(
                    txtDetailOriginalPrice.getPaintFlags() | Paint.STRIKE_THRU_TEXT_FLAG
            );

            // Set description
            txtDetailDescription.setText(product.getDescription());

            // Determine if this is a remote (API) product or local product
            if (product.hasRemoteImages()) {
                isRemoteProduct = true;
                sliderImageUrls = product.getImageUrls();
                if (sliderImageUrls == null || sliderImageUrls.isEmpty()) {
                    // Fallback to thumbnail if no image URLs
                    sliderImageUrls = new ArrayList<>();
                    sliderImageUrls.add(product.getThumbnailUrl());
                }

                // Show stock status from API
                txtStockStatus.setText("In Stock");
                txtStockStatus.setVisibility(View.VISIBLE);
            } else {
                isRemoteProduct = false;
                sliderImages = product.getImages();
                txtStockStatus.setText("5 in a stock");
            }
        } else {
            // Fallback default
            sliderImages.add(R.drawable.shirt1);
            sliderImages.add(R.drawable.shirt2);
            sliderImages.add(R.drawable.watch1);
        }
    }

    private void setupImageSlider() {
        if (isRemoteProduct) {
            // Use URL-based adapter for API products
            ImageSliderAdapter urlAdapter = new ImageSliderAdapter(sliderImageUrls, true);
            viewPagerImageSlider.setAdapter(urlAdapter);
            setupDotsIndicator(0, sliderImageUrls.size());
        } else {
            // Use drawable-based adapter for local products
            ImageSliderAdapter resAdapter = new ImageSliderAdapter(sliderImages);
            viewPagerImageSlider.setAdapter(resAdapter);
            setupDotsIndicator(0, sliderImages.size());
        }

        viewPagerImageSlider.registerOnPageChangeCallback(new ViewPager2.OnPageChangeCallback() {
            @Override
            public void onPageSelected(int position) {
                super.onPageSelected(position);
                int total = isRemoteProduct
                        ? (sliderImageUrls != null ? sliderImageUrls.size() : 0)
                        : (sliderImages != null ? sliderImages.size() : 0);
                setupDotsIndicator(position, total);
            }
        });
    }

    private void setupDotsIndicator(int activePosition, int totalDots) {
        layoutSliderDots.removeAllViews();
        if (totalDots <= 1) return;

        int activeColor = ContextCompat.getColor(this, R.color.primary_coral);
        int inactiveColor = ContextCompat.getColor(this, R.color.border_light);

        float density = getResources().getDisplayMetrics().density;
        int heightPx = (int) (6 * density);
        int marginPx = (int) (3 * density);

        for (int i = 0; i < totalDots; i++) {
            View dot = new View(this);
            int widthPx = (i == activePosition) ? (int) (18 * density) : (int) (6 * density);

            LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(widthPx, heightPx);
            params.setMargins(marginPx, 0, marginPx, 0);
            dot.setLayoutParams(params);

            GradientDrawable shape = new GradientDrawable();
            shape.setShape(GradientDrawable.RECTANGLE);
            shape.setCornerRadius(3 * density);
            shape.setColor(i == activePosition ? activeColor : inactiveColor);
            dot.setBackground(shape);

            layoutSliderDots.addView(dot);
        }
    }

    private void setupListeners() {
        btnBack.setOnClickListener(v -> finish());

        cardFavorite.setOnClickListener(v -> {
            isFavorite = !isFavorite;
            imgFavoriteHeart.setImageResource(isFavorite ? R.drawable.ic_heart_filled : R.drawable.ic_heart_outline);
            Toast.makeText(this, isFavorite ? "Added to favorites" : "Removed from favorites", Toast.LENGTH_SHORT).show();
        });

        btnAddToCart.setOnClickListener(v -> {
            String title = txtDetailTitle.getText().toString();
            Toast.makeText(this, "Added " + title + " to cart", Toast.LENGTH_SHORT).show();
        });

        btnBuyNow.setOnClickListener(v -> {
            Toast.makeText(this, "Proceeding to checkout", Toast.LENGTH_SHORT).show();
        });
    }
}