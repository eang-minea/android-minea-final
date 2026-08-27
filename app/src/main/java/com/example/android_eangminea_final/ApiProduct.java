package com.example.android_eangminea_final;

import com.google.gson.annotations.SerializedName;

import java.util.List;

/**
 * POJO model matching the DummyJSON product JSON structure.
 * Gson deserializes the API response directly into this class.
 */
public class ApiProduct {

    @SerializedName("id")
    private int id;

    @SerializedName("title")
    private String title;

    @SerializedName("description")
    private String description;

    @SerializedName("price")
    private double price;

    @SerializedName("discountPercentage")
    private double discountPercentage;

    @SerializedName("rating")
    private double rating;

    @SerializedName("stock")
    private int stock;

    @SerializedName("brand")
    private String brand;

    @SerializedName("category")
    private String category;

    @SerializedName("thumbnail")
    private String thumbnail;

    @SerializedName("images")
    private List<String> images;

    // Getters

    public int getId() {
        return id;
    }

    public String getTitle() {
        return title;
    }

    public String getDescription() {
        return description;
    }

    public double getPrice() {
        return price;
    }

    public double getDiscountPercentage() {
        return discountPercentage;
    }

    public double getRating() {
        return rating;
    }

    public int getStock() {
        return stock;
    }

    public String getBrand() {
        return brand;
    }

    public String getCategory() {
        return category;
    }

    public String getThumbnail() {
        return thumbnail;
    }

    public List<String> getImages() {
        return images;
    }

    /**
     * Calculate the discounted price based on price and discountPercentage.
     */
    public double getDiscountedPrice() {
        return price - (price * discountPercentage / 100.0);
    }
}
