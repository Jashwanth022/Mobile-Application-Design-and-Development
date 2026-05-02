# =========================================
# MODEL FILE (FAST VERSION)
# =========================================

import yfinance as yf
import pandas as pd
import numpy as np
import time
import warnings
import random

from sklearn.ensemble import RandomForestRegressor, RandomForestClassifier
from sklearn.preprocessing import StandardScaler
from sklearn.metrics import accuracy_score

import ta

warnings.filterwarnings("ignore")

np.random.seed(42)
random.seed(42)


# =========================================
# STOCK LIST
# =========================================
large_cap = [
    "RELIANCE.NS","TCS.NS","HDFCBANK.NS","INFY.NS",
    "ICICIBANK.NS","HINDUNILVR.NS","ITC.NS","LT.NS",
    "SBIN.NS","BHARTIARTL.NS"
]

mid_cap = [
    "PIDILITIND.NS","BERGEPAINT.NS","TRENT.NS","MUTHOOTFIN.NS",
    "LUPIN.NS","MPHASIS.NS","COFORGE.NS","ASTRAL.NS",
    "ALKEM.NS","POLYCAB.NS"
]

stocks = large_cap + mid_cap


# =========================================
# DOWNLOAD
# =========================================
def download_data(symbol):

    time.sleep(0.1)

    df = yf.download(
        symbol,
        period="1y",  # FAST
        interval="1d",
        auto_adjust=True,
        progress=False,
        threads=False
    )

    if df.empty:
        return None

    df.columns = [c[0] if isinstance(c, tuple) else c for c in df.columns]
    return df


# =========================================
# FEATURES
# =========================================
def add_features(df):

    df["Return"] = df["Close"].pct_change()
    df["Volatility"] = df["Return"].rolling(10).std()

    df["SMA10"] = df["Close"].rolling(10).mean()
    df["SMA20"] = df["Close"].rolling(20).mean()
    df["SMA50"] = df["Close"].rolling(50).mean()

    df["Momentum"] = df["Close"] / df["Close"].shift(10) - 1

    df["RSI"] = ta.momentum.rsi(df["Close"])
    df["MACD"] = ta.trend.macd(df["Close"])

    df["Lag1"] = df["Return"].shift(1)
    df["Lag2"] = df["Return"].shift(2)

    df.dropna(inplace=True)
    return df


# =========================================
# PROCESS
# =========================================
def process_stock(symbol):

    df = download_data(symbol)
    if df is None:
        return None

    df = add_features(df)

    df["Target_1D"] = df["Close"].pct_change(1).shift(-1)
    df["Target_1W"] = df["Close"].pct_change(5).shift(-5)
    df["Target_1M"] = df["Close"].pct_change(21).shift(-21)

    df["Direction"] = (df["Close"].pct_change().shift(-1) > 0).astype(int)
    df.dropna(inplace=True)

    features = [
        "Return","Volatility","SMA10","SMA20","SMA50",
        "Momentum","RSI","MACD","Lag1","Lag2","Volume"
    ]

    X = df[features]

    scaler = StandardScaler()
    X_scaled = scaler.fit_transform(X)

    preds = []

    # Regression predictions
    for target in ["Target_1D","Target_1W","Target_1M"]:

        y = df[target]
        split = int(len(X_scaled) * 0.7)

        model = RandomForestRegressor(
            n_estimators=150,   # FAST
            max_depth=10,
            random_state=42,
            n_jobs=1
        )

        model.fit(X_scaled[:split], y[:split])

        pred = model.predict(X_scaled[-1].reshape(1, -1))[0]
        preds.append(round(pred * 100, 2))

    # Classification accuracy
    split = int(len(X_scaled) * 0.7)

    clf = RandomForestClassifier(
        n_estimators=200,
        max_depth=10,
        random_state=42
    )

    clf.fit(X_scaled[:split], df["Direction"][:split])

    y_pred = clf.predict(X_scaled[split:])
    acc = accuracy_score(df["Direction"][split:], y_pred)

    return preds[0], preds[1], preds[2], round(acc * 100, 2)


# =========================================
# FINAL FUNCTION
# =========================================
def get_predictions():

    results = []

    for stock in stocks:
        try:
            result = process_stock(stock)

            if result is None:
                continue

            d, w, m, acc = result

            cap = "Largecap" if stock in large_cap else "Midcap"

            results.append({
                "stock": stock,
                "cap": cap,
                "next_day": d,
                "next_week": w,
                "next_month": m,
                "accuracy": acc
            })

        except:
            continue

    return results
