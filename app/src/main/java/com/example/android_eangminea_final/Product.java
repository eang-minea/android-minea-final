package com.example.android_eangminea_final;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class Product implements Serializable {
    private List<Integer> images;
    private String title;
    private String description;
    private String originalPrice;
    private String discountedPrice;

    public Product(List<Integer> images, String title, String description, String originalPrice, String discountedPrice) {
        this.images = images != null ? images : new ArrayList<>();
        this.title = title;
        this.description = description;
        this.originalPrice = originalPrice;
        this.discountedPrice = discountedPrice;
    }

    public Product(int singleImage, String title, String description, String originalPrice, String discountedPrice) {
        this.images = new ArrayList<>();
        this.images.add(singleImage);
        this.title = title;
        this.description = description;
        this.originalPrice = originalPrice;
        this.discountedPrice = discountedPrice;
    }

    public int getMainImage() {
        if (images != null && !images.isEmpty()) {
            return images.get(0);
        }
        return R.drawable.shirt1;
    }

    public List<Integer> getImages() {
        return images;
    }

    public String getTitle() {
        return title;
    }

    public String getDescription() {
        return description;
    }

    public String getOriginalPrice() {
        return originalPrice;
    }

    public String getDiscountedPrice() {
        return discountedPrice;
    }
}