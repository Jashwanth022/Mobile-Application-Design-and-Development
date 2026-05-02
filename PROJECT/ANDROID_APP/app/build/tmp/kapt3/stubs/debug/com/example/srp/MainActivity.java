package com.example.srp;

@kotlin.Metadata(mv = {2, 2, 0}, k = 1, xi = 48, d1 = {"\u0000P\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\b\u0003\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0002\b\u0005\n\u0002\u0010\u000e\n\u0002\b\u0005\n\u0002\u0010 \n\u0002\u0018\u0002\n\u0002\b\u0003\u0018\u00002\u00020\u0001B\u0007\u00a2\u0006\u0004\b\u0002\u0010\u0003J\u0012\u0010\n\u001a\u00020\u000b2\b\u0010\f\u001a\u0004\u0018\u00010\rH\u0014J\u0010\u0010\u000e\u001a\u00020\u000b2\u0006\u0010\u000f\u001a\u00020\u0010H\u0002J\u0010\u0010\u0011\u001a\u00020\u000b2\u0006\u0010\u0012\u001a\u00020\u0013H\u0002J\u0010\u0010\u0014\u001a\u00020\u000b2\u0006\u0010\u000f\u001a\u00020\u0010H\u0002J\u0010\u0010\u0015\u001a\u00020\u000b2\u0006\u0010\u000f\u001a\u00020\u0010H\u0002J\b\u0010\u0016\u001a\u00020\u000bH\u0002J \u0010\u0017\u001a\u00020\u000b2\u0006\u0010\u0018\u001a\u00020\u00192\u0006\u0010\u001a\u001a\u00020\u00192\u0006\u0010\u001b\u001a\u00020\u0019H\u0002J\b\u0010\u001c\u001a\u00020\u000bH\u0002J\u0016\u0010\u001d\u001a\u00020\u000b2\f\u0010\u001e\u001a\b\u0012\u0004\u0012\u00020 0\u001fH\u0002J\b\u0010!\u001a\u00020\u000bH\u0002J\b\u0010\"\u001a\u00020\u000bH\u0002R\u000e\u0010\u0004\u001a\u00020\u0005X\u0082.\u00a2\u0006\u0002\n\u0000R\u000e\u0010\u0006\u001a\u00020\u0007X\u0082.\u00a2\u0006\u0002\n\u0000R\u000e\u0010\b\u001a\u00020\tX\u0082.\u00a2\u0006\u0002\n\u0000\u00a8\u0006#"}, d2 = {"Lcom/example/srp/MainActivity;", "Landroidx/appcompat/app/AppCompatActivity;", "<init>", "()V", "db", "Lcom/example/srp/AppDatabase;", "watchlistAdapter", "Lcom/example/srp/WatchlistAdapter;", "api", "Lcom/example/srp/ApiService;", "onCreate", "", "savedInstanceState", "Landroid/os/Bundle;", "loadDashboard", "container", "Landroid/widget/FrameLayout;", "setupDashboardChart", "chart", "Lcom/github/mikephil/charting/charts/LineChart;", "loadSettings", "loadWatchlist", "setupTrendingClicks", "openDetail", "symbol", "", "change", "accuracy", "fetchData", "setupTopPick", "data", "", "Lcom/example/srp/StockResponse;", "setupMiniChart", "setupNavigation", "app_debug"})
public final class MainActivity extends androidx.appcompat.app.AppCompatActivity {
    private com.example.srp.AppDatabase db;
    private com.example.srp.WatchlistAdapter watchlistAdapter;
    private com.example.srp.ApiService api;
    
    public MainActivity() {
        super();
    }
    
    @java.lang.Override()
    protected void onCreate(@org.jetbrains.annotations.Nullable()
    android.os.Bundle savedInstanceState) {
    }
    
    private final void loadDashboard(android.widget.FrameLayout container) {
    }
    
    private final void setupDashboardChart(com.github.mikephil.charting.charts.LineChart chart) {
    }
    
    private final void loadSettings(android.widget.FrameLayout container) {
    }
    
    private final void loadWatchlist(android.widget.FrameLayout container) {
    }
    
    private final void setupTrendingClicks() {
    }
    
    private final void openDetail(java.lang.String symbol, java.lang.String change, java.lang.String accuracy) {
    }
    
    private final void fetchData() {
    }
    
    private final void setupTopPick(java.util.List<com.example.srp.StockResponse> data) {
    }
    
    private final void setupMiniChart() {
    }
    
    private final void setupNavigation() {
    }
}