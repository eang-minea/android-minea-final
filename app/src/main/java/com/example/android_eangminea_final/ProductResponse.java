package com.example.android_eangminea_final;

import com.google.gson.annotations.SerializedName;

import java.util.List;

/**
 * Wrapper class for the DummyJSON products API response.
 */
public class ProductResponse {

    @SerializedName("products")
    private List<ApiProduct> products;

    @SerializedName("total")
    private int total;

    @SerializedName("skip")
    private int skip;

    @SerializedName("limit")
    private int limit;

    public List<ApiProduct> getProducts() {
        return products;
    }

    public int getTotal() {
        return total;
    }

    public int getSkip() {
        return skip;
    }

    public int getLimit() {
        return limit;
    }
}
