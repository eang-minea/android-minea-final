package com.example.android_eangminea_final;

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
    private ImageSliderAdapter sliderAdapter;
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
    private List<Integer> sliderImages = new ArrayList<>();

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
            sliderImages = product.getImages();
            txtDetailTitle.setText(product.getTitle());
            txtDetailSubtitle.setText("This is 100% " + product.getTitle().toLowerCase());
            txtDetailDiscountPrice.setText(product.getDiscountedPrice());
            txtDetailOriginalPrice.setText(product.getOriginalPrice());
            txtDetailDescription.setText(product.getDescription() + " wear shirt which is made by Bangladesh is made by this by Bangladesh dummy text");
        } else {
            sliderImages.add(R.drawable.shirt1);
            sliderImages.add(R.drawable.shirt2);
            sliderImages.add(R.drawable.watch1);
        }
    }

    private void setupImageSlider() {
        sliderAdapter = new ImageSliderAdapter(sliderImages);
        viewPagerImageSlider.setAdapter(sliderAdapter);

        setupDotsIndicator(0);

        viewPagerImageSlider.registerOnPageChangeCallback(new ViewPager2.OnPageChangeCallback() {
            @Override
            public void onPageSelected(int position) {
                super.onPageSelected(position);
                setupDotsIndicator(position);
            }
        });
    }

    private void setupDotsIndicator(int activePosition) {
        layoutSliderDots.removeAllViews();
        if (sliderImages == null || sliderImages.size() <= 1) return;

        int activeColor = ContextCompat.getColor(this, R.color.primary_coral);
        int inactiveColor = ContextCompat.getColor(this, R.color.border_light);

        float density = getResources().getDisplayMetrics().density;
        int heightPx = (int) (6 * density);
        int marginPx = (int) (3 * density);

        for (int i = 0; i < sliderImages.size(); i++) {
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