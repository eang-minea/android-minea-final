package com.example.android_eangminea_final;

import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.core.widget.ImageViewCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MainActivity extends AppCompatActivity {

    private static final int PAGE_SIZE = 20;

    private RecyclerView recyclerViewCategories;
    private CategoryAdapter categoryAdapter;
    private List<Category> categoryList;

    private RecyclerView recyclerViewProducts;
    private ProductAdapter productAdapter;
    private List<Product> allProducts;
    private List<Product> displayedProducts;

    private EditText edtSearch;
    private ImageView btnMenu, btnFilter;
    private TextView btnSeeAllCategories;
    private ProgressBar progressBar;
    private TextView txtError;
    private SwipeRefreshLayout swipeRefreshLayout;

    // Pagination state
    private int currentSkip = 0;
    private int totalProducts = 0;
    private boolean isLoading = false;

    // Bottom Navigation items
    private ImageView imgNavHome, imgNavCart, imgNavFavorite, imgNavMore;
    private TextView txtNavHome, txtNavCart, txtNavFavorite, txtNavMore;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        setupWindowInsets();
        initViews();
        setupCategories();
        setupProducts();
        setupSearch();
        setupBottomNav();
        setupSwipeRefresh();
    }

    private void setupWindowInsets() {
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.mainRoot), (v, windowInsets) -> {
            Insets insets = windowInsets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(insets.left, insets.top, insets.right, 0);
            findViewById(R.id.layoutBottomNav).setPadding(0, 0, 0, insets.bottom);
            return windowInsets;
        });
    }

    private void initViews() {
        recyclerViewCategories = findViewById(R.id.recyclerViewCategories);
        recyclerViewProducts = findViewById(R.id.recyclerViewProducts);
        edtSearch = findViewById(R.id.edtSearch);
        btnMenu = findViewById(R.id.btnMenu);
        btnFilter = findViewById(R.id.btnFilter);
        btnSeeAllCategories = findViewById(R.id.btnSeeAllCategories);
        progressBar = findViewById(R.id.progressBar);
        txtError = findViewById(R.id.txtError);
        swipeRefreshLayout = findViewById(R.id.swipeRefreshLayout);

        imgNavHome = findViewById(R.id.imgNavHome);
        imgNavCart = findViewById(R.id.imgNavCart);
        imgNavFavorite = findViewById(R.id.imgNavFavorite);
        imgNavMore = findViewById(R.id.imgNavMore);

        txtNavHome = findViewById(R.id.txtNavHome);
        txtNavCart = findViewById(R.id.txtNavCart);
        txtNavFavorite = findViewById(R.id.txtNavFavorite);
        txtNavMore = findViewById(R.id.txtNavMore);

        btnMenu.setOnClickListener(v -> Toast.makeText(this, "Menu clicked", Toast.LENGTH_SHORT).show());
        btnFilter.setOnClickListener(v -> Toast.makeText(this, "Filter clicked", Toast.LENGTH_SHORT).show());
        btnSeeAllCategories.setOnClickListener(v -> Toast.makeText(this, "All Categories", Toast.LENGTH_SHORT).show());
    }

    private void setupCategories() {
        categoryList = new ArrayList<>();
        categoryList.add(new Category("Apparel", R.drawable.ic_cat_apparel, true));
        categoryList.add(new Category("Shoe", R.drawable.ic_cat_shoe, false));
        categoryList.add(new Category("Beauty", R.drawable.ic_cat_beauty, false));
        categoryList.add(new Category("Electric", R.drawable.ic_cat_electric, false));

        recyclerViewCategories.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false));
        categoryAdapter = new CategoryAdapter(categoryList, (category, position) -> {
            Toast.makeText(MainActivity.this, category.getName() + " selected", Toast.LENGTH_SHORT).show();
        });
        recyclerViewCategories.setAdapter(categoryAdapter);
    }

    private void setupProducts() {
        allProducts = new ArrayList<>();
        displayedProducts = new ArrayList<>();

        recyclerViewProducts.setLayoutManager(new LinearLayoutManager(this));
        productAdapter = new ProductAdapter(displayedProducts, product -> {
            Intent intent = new Intent(MainActivity.this, ProductDetailActivity.class);
            intent.putExtra("product", product);
            startActivity(intent);
        });
        recyclerViewProducts.setAdapter(productAdapter);

        // Fetch first page of products from DummyJSON API
        fetchProductsFromApi(false);
    }

    private void setupSwipeRefresh() {
        swipeRefreshLayout.setColorSchemeResources(
                R.color.primary_coral
        );

        swipeRefreshLayout.setOnRefreshListener(() -> {
            if (isLoading) {
                swipeRefreshLayout.setRefreshing(false);
                return;
            }

            if (currentSkip >= totalProducts && totalProducts > 0) {
                // All products have been loaded
                swipeRefreshLayout.setRefreshing(false);
                Toast.makeText(this, "All products loaded (" + totalProducts + " total)", Toast.LENGTH_SHORT).show();
                return;
            }

            // Load next 20 products
            fetchProductsFromApi(true);
        });
    }

    /**
     * Fetch products from the DummyJSON API.
     * @param isLoadMore true = append next page; false = initial load (clear existing)
     */
    private void fetchProductsFromApi(boolean isLoadMore) {
        if (isLoading) return;
        isLoading = true;

        if (!isLoadMore) {
            // Initial load: show progress bar, reset pagination
            progressBar.setVisibility(View.VISIBLE);
            recyclerViewProducts.setVisibility(View.GONE);
            txtError.setVisibility(View.GONE);
            currentSkip = 0;
            allProducts.clear();
            displayedProducts.clear();
            productAdapter.notifyDataSetChanged();
        }

        DummyJsonApi api = RetrofitClient.getApi();
        api.getProducts(PAGE_SIZE, currentSkip).enqueue(new Callback<ProductResponse>() {
            @Override
            public void onResponse(Call<ProductResponse> call, Response<ProductResponse> response) {
                isLoading = false;
                progressBar.setVisibility(View.GONE);
                swipeRefreshLayout.setRefreshing(false);

                if (response.isSuccessful() && response.body() != null) {
                    ProductResponse body = response.body();
                    totalProducts = body.getTotal();
                    List<ApiProduct> apiProducts = body.getProducts();

                    int insertStart = allProducts.size();

                    for (ApiProduct apiProduct : apiProducts) {
                        double originalPrice = apiProduct.getPrice();
                        double discountedPrice = apiProduct.getDiscountedPrice();

                        String originalPriceStr = String.format(Locale.US, "$%.2f", originalPrice);
                        String discountedPriceStr = String.format(Locale.US, "$%.2f", discountedPrice);

                        Product product = new Product(
                                apiProduct.getId(),
                                apiProduct.getTitle(),
                                apiProduct.getDescription(),
                                originalPriceStr,
                                discountedPriceStr,
                                apiProduct.getThumbnail(),
                                apiProduct.getImages()
                        );
                        allProducts.add(product);
                    }

                    // Update skip for next page
                    currentSkip = allProducts.size();

                    // Update displayed list based on current search
                    String searchQuery = edtSearch.getText().toString().trim();
                    if (searchQuery.isEmpty()) {
                        displayedProducts.clear();
                        displayedProducts.addAll(allProducts);
                    } else {
                        filterProducts(searchQuery);
                    }

                    productAdapter.notifyDataSetChanged();
                    recyclerViewProducts.setVisibility(View.VISIBLE);

                    if (isLoadMore) {
                        int loaded = apiProducts.size();
                        Toast.makeText(MainActivity.this,
                                "Loaded " + loaded + " more products (" + allProducts.size() + "/" + totalProducts + ")",
                                Toast.LENGTH_SHORT).show();
                    }
                } else {
                    if (!isLoadMore) {
                        showError("Failed to load products. Pull down to retry.");
                    } else {
                        Toast.makeText(MainActivity.this, "Failed to load more products", Toast.LENGTH_SHORT).show();
                    }
                }
            }

            @Override
            public void onFailure(Call<ProductResponse> call, Throwable t) {
                isLoading = false;
                progressBar.setVisibility(View.GONE);
                swipeRefreshLayout.setRefreshing(false);

                if (!isLoadMore) {
                    showError("Network error: " + t.getMessage());
                } else {
                    Toast.makeText(MainActivity.this, "Network error: " + t.getMessage(), Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    private void showError(String message) {
        txtError.setText(message);
        txtError.setVisibility(View.VISIBLE);
        recyclerViewProducts.setVisibility(View.GONE);
        Toast.makeText(this, message, Toast.LENGTH_LONG).show();
    }

    private void setupSearch() {
        edtSearch.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {}

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                filterProducts(s.toString());
            }

            @Override
            public void afterTextChanged(Editable s) {}
        });
    }

    private void filterProducts(String query) {
        displayedProducts.clear();
        if (query.trim().isEmpty()) {
            displayedProducts.addAll(allProducts);
        } else {
            String lower = query.toLowerCase();
            for (Product p : allProducts) {
                if (p.getTitle().toLowerCase().contains(lower) || p.getDescription().toLowerCase().contains(lower)) {
                    displayedProducts.add(p);
                }
            }
        }
        productAdapter.notifyDataSetChanged();
    }

    private void setupBottomNav() {
        findViewById(R.id.navHome).setOnClickListener(v -> selectNavTab(0));
        findViewById(R.id.navCart).setOnClickListener(v -> selectNavTab(1));
        findViewById(R.id.navFavorite).setOnClickListener(v -> selectNavTab(2));
        findViewById(R.id.navMore).setOnClickListener(v -> selectNavTab(3));
    }

    private void selectNavTab(int index) {
        int colorSelected = ContextCompat.getColor(this, R.color.primary_coral);
        int colorUnselected = ContextCompat.getColor(this, R.color.icon_unselected);

        ImageViewCompat.setImageTintList(imgNavHome, android.content.res.ColorStateList.valueOf(index == 0 ? colorSelected : colorUnselected));
        txtNavHome.setTextColor(index == 0 ? colorSelected : colorUnselected);
        txtNavHome.setTypeface(null, index == 0 ? android.graphics.Typeface.BOLD : android.graphics.Typeface.NORMAL);

        ImageViewCompat.setImageTintList(imgNavCart, android.content.res.ColorStateList.valueOf(index == 1 ? colorSelected : colorUnselected));
        txtNavCart.setTextColor(index == 1 ? colorSelected : colorUnselected);
        txtNavCart.setTypeface(null, index == 1 ? android.graphics.Typeface.BOLD : android.graphics.Typeface.NORMAL);

        ImageViewCompat.setImageTintList(imgNavFavorite, android.content.res.ColorStateList.valueOf(index == 2 ? colorSelected : colorUnselected));
        txtNavFavorite.setTextColor(index == 2 ? colorSelected : colorUnselected);
        txtNavFavorite.setTypeface(null, index == 2 ? android.graphics.Typeface.BOLD : android.graphics.Typeface.NORMAL);

        ImageViewCompat.setImageTintList(imgNavMore, android.content.res.ColorStateList.valueOf(index == 3 ? colorSelected : colorUnselected));
        txtNavMore.setTextColor(index == 3 ? colorSelected : colorUnselected);
        txtNavMore.setTypeface(null, index == 3 ? android.graphics.Typeface.BOLD : android.graphics.Typeface.NORMAL);
    }
}