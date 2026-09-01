package com.example.android_eangminea_final;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.viewpager2.widget.ViewPager2;

import java.util.ArrayList;
import java.util.List;

public class OnboardingActivity extends AppCompatActivity {

    private OnboardingAdapter onboardingAdapter;
    private LinearLayout layoutOnboardingIndicators;
    private TextView tvSkip, tvPrev, tvNext, tvPageNumber;
    private ViewPager2 onboardingViewPager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_onboarding);

        layoutOnboardingIndicators = findViewById(R.id.dots_layout);
        tvSkip = findViewById(R.id.tv_skip);
        tvPrev = findViewById(R.id.tv_prev);
        tvNext = findViewById(R.id.tv_next);
        tvPageNumber = findViewById(R.id.tv_page_number);
        onboardingViewPager = findViewById(R.id.viewPager);

        setupOnboardingItems();
        setupOnboardingIndicators();
        setCurrentOnboardingIndicator(0);

        onboardingViewPager.setAdapter(onboardingAdapter);
        onboardingViewPager.registerOnPageChangeCallback(new ViewPager2.OnPageChangeCallback() {
            @Override
            public void onPageSelected(int position) {
                super.onPageSelected(position);
                setCurrentOnboardingIndicator(position);
                tvPageNumber.setText((position + 1) + "/3");
                
                if (position == 0) {
                    tvPrev.setVisibility(View.INVISIBLE);
                } else {
                    tvPrev.setVisibility(View.VISIBLE);
                }
                
                if (position == onboardingAdapter.getItemCount() - 1) {
                    tvNext.setText("Get Started");
                } else {
                    tvNext.setText("Next");
                }
            }
        });

        tvNext.setOnClickListener(v -> {
            if (onboardingViewPager.getCurrentItem() + 1 < onboardingAdapter.getItemCount()) {
                onboardingViewPager.setCurrentItem(onboardingViewPager.getCurrentItem() + 1);
            } else {
                startActivity(new Intent(getApplicationContext(), LoginActivity.class));
                finish();
            }
        });

        tvPrev.setOnClickListener(v -> {
            if (onboardingViewPager.getCurrentItem() > 0) {
                onboardingViewPager.setCurrentItem(onboardingViewPager.getCurrentItem() - 1);
            }
        });

        tvSkip.setOnClickListener(v -> {
            startActivity(new Intent(getApplicationContext(), LoginActivity.class));
            finish();
        });
    }

    private void setupOnboardingItems() {
        List<OnboardingAdapter.OnboardingItem> onboardingItems = new ArrayList<>();
        onboardingItems.add(new OnboardingAdapter.OnboardingItem(
                R.drawable.ic_onboarding_1, // Updated image
                "Choose Products",
                "Amet minim mollit non deserunt ullamco est sit aliqua dolor do amet sint. Velit officia consequat duis enim velit mollit."
        ));
        onboardingItems.add(new OnboardingAdapter.OnboardingItem(
                R.drawable.ic_onboarding_2, // Updated image
                "Make Payment",
                "Amet minim mollit non deserunt ullamco est sit aliqua dolor do amet sint. Velit officia consequat duis enim velit mollit."
        ));
        onboardingItems.add(new OnboardingAdapter.OnboardingItem(
                R.drawable.ic_onboarding_3, // Updated image
                "Get Your Order",
                "Amet minim mollit non deserunt ullamco est sit aliqua dolor do amet sint. Velit officia consequat duis enim velit mollit."
        ));
        onboardingAdapter = new OnboardingAdapter(onboardingItems);
    }

    private void setupOnboardingIndicators() {
        ImageView[] indicators = new ImageView[onboardingAdapter.getItemCount()];
        LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(
                ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT
        );
        layoutParams.setMargins(8, 0, 8, 0);
        for (int i = 0; i < indicators.length; i++) {
            indicators[i] = new ImageView(getApplicationContext());
            indicators[i].setImageDrawable(ContextCompat.getDrawable(
                    getApplicationContext(),
                    R.drawable.ic_dot_inactive
            ));
            indicators[i].setLayoutParams(layoutParams);
            layoutOnboardingIndicators.addView(indicators[i]);
        }
    }

    private void setCurrentOnboardingIndicator(int index) {
        int childCount = layoutOnboardingIndicators.getChildCount();
        for (int i = 0; i < childCount; i++) {
            ImageView imageView = (ImageView) layoutOnboardingIndicators.getChildAt(i);
            if (i == index) {
                imageView.setImageDrawable(ContextCompat.getDrawable(getApplicationContext(), R.drawable.ic_dot_active));
            } else {
                imageView.setImageDrawable(ContextCompat.getDrawable(getApplicationContext(), R.drawable.ic_dot_inactive));
            }
        }
    }
}
