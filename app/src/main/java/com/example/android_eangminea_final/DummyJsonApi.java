package com.example.android_eangminea_final;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

/**
 * Retrofit interface for the DummyJSON Products API.
 */
public interface DummyJsonApi {

    @GET("products")
    Call<ProductResponse> getProducts();

    @GET("products")
    Call<ProductResponse> getProducts(@Query("limit") int limit, @Query("skip") int skip);

    @GET("products/search")
    Call<ProductResponse> searchProducts(@Query("q") String query);
}
