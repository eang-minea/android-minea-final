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

    // Fields for API-sourced products (URL-based images)
    private String thumbnailUrl;
    private List<String> imageUrls;
    private int id;

    // Constructor for local drawable images (existing, backward compatible)
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

    // Constructor for API products (URL-based images)
    public Product(int id, String title, String description, String originalPrice, String discountedPrice,
                   String thumbnailUrl, List<String> imageUrls) {
        this.id = id;
        this.title = title;
        this.description = description;
        this.originalPrice = originalPrice;
        this.discountedPrice = discountedPrice;
        this.thumbnailUrl = thumbnailUrl;
        this.imageUrls = imageUrls;
        this.images = new ArrayList<>(); // empty, since we use URLs
    }

    public int getId() {
        return id;
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

    public String getThumbnailUrl() {
        return thumbnailUrl;
    }

    public List<String> getImageUrls() {
        return imageUrls;
    }

    public boolean hasRemoteImages() {
        return thumbnailUrl != null && !thumbnailUrl.isEmpty();
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