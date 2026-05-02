package com.example.srp;

@kotlin.Metadata(mv = {2, 2, 0}, k = 1, xi = 48, d1 = {"\u0000Z\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\b\u0003\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0003\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010 \n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0010\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0005\n\u0002\u0010\u000e\n\u0002\b\u0005\u0018\u00002\u00020\u0001B\u0007\u00a2\u0006\u0004\b\u0002\u0010\u0003J\u0012\u0010\u0018\u001a\u00020\u00192\b\u0010\u001a\u001a\u0004\u0018\u00010\u001bH\u0014J\b\u0010\u001c\u001a\u00020\u0019H\u0002J\b\u0010\u001d\u001a\u00020\u0019H\u0002J\b\u0010\u001e\u001a\u00020\u0019H\u0002J\u0010\u0010\u001f\u001a\u00020\u00192\u0006\u0010 \u001a\u00020!H\u0002J\b\u0010\"\u001a\u00020\u0019H\u0002J\b\u0010#\u001a\u00020\u0019H\u0002J\u0010\u0010$\u001a\u00020\u00192\u0006\u0010%\u001a\u00020\tH\u0002R\u000e\u0010\u0004\u001a\u00020\u0005X\u0082.\u00a2\u0006\u0002\n\u0000R\u000e\u0010\u0006\u001a\u00020\u0007X\u0082.\u00a2\u0006\u0002\n\u0000R\u000e\u0010\b\u001a\u00020\tX\u0082.\u00a2\u0006\u0002\n\u0000R\u000e\u0010\n\u001a\u00020\u000bX\u0082.\u00a2\u0006\u0002\n\u0000R\u000e\u0010\f\u001a\u00020\rX\u0082.\u00a2\u0006\u0002\n\u0000R\u000e\u0010\u000e\u001a\u00020\u000fX\u0082.\u00a2\u0006\u0002\n\u0000R\u000e\u0010\u0010\u001a\u00020\u000fX\u0082.\u00a2\u0006\u0002\n\u0000R\u000e\u0010\u0011\u001a\u00020\u000fX\u0082.\u00a2\u0006\u0002\n\u0000R\u000e\u0010\u0012\u001a\u00020\u0013X\u0082.\u00a2\u0006\u0002\n\u0000R\u0014\u0010\u0014\u001a\b\u0012\u0004\u0012\u00020\u00160\u0015X\u0082\u000e\u00a2\u0006\u0002\n\u0000R\u0014\u0010\u0017\u001a\b\u0012\u0004\u0012\u00020\u00160\u0015X\u0082\u000e\u00a2\u0006\u0002\n\u0000\u00a8\u0006&"}, d2 = {"Lcom/example/srp/DayActivity;", "Landroidx/appcompat/app/AppCompatActivity;", "<init>", "()V", "rvStocks", "Landroidx/recyclerview/widget/RecyclerView;", "lineChart", "Lcom/github/mikephil/charting/charts/LineChart;", "detailsContainer", "Landroid/view/View;", "etSearch", "Landroid/widget/EditText;", "btnSearch", "Landroid/widget/ImageButton;", "tvTopStockName", "Landroid/widget/TextView;", "tvTopStockChange", "tvTopStockAccuracy", "api", "Lcom/example/srp/ApiService;", "allStocks", "", "Lcom/example/srp/Stock;", "filteredStocks", "onCreate", "", "savedInstanceState", "Landroid/os/Bundle;", "fetchData", "setupRecyclerView", "setupSearch", "filterStocks", "query", "", "setupChart", "setupTabs", "showView", "view", "app_debug"})
public final class DayActivity extends androidx.appcompat.app.AppCompatActivity {
    private androidx.recyclerview.widget.RecyclerView rvStocks;
    private com.github.mikephil.charting.charts.LineChart lineChart;
    private android.view.View detailsContainer;
    private android.widget.EditText etSearch;
    private android.widget.ImageButton btnSearch;
    private android.widget.TextView tvTopStockName;
    private android.widget.TextView tvTopStockChange;
    private android.widget.TextView tvTopStockAccuracy;
    private com.example.srp.ApiService api;
    @org.jetbrains.annotations.NotNull()
    private java.util.List<com.example.srp.Stock> allStocks;
    @org.jetbrains.annotations.NotNull()
    private java.util.List<com.example.srp.Stock> filteredStocks;
    
    public DayActivity() {
        super();
    }
    
    @java.lang.Override()
    protected void onCreate(@org.jetbrains.annotations.Nullable()
    android.os.Bundle savedInstanceState) {
    }
    
    private final void fetchData() {
    }
    
    private final void setupRecyclerView() {
    }
    
    private final void setupSearch() {
    }
    
    private final void filterStocks(java.lang.String query) {
    }
    
    private final void setupChart() {
    }
    
    private final void setupTabs() {
    }
    
    private final void showView(android.view.View view) {
    }
}