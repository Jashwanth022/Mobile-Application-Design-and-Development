from flask import Flask, jsonify
from model import get_predictions

app = Flask(__name__)

@app.route("/")
def home():
    return "Stock Prediction API Running 🚀"

@app.route("/predict")
def predict():
    data = get_predictions()
    return jsonify(data)

if __name__ == "__main__":
    app.run(host="0.0.0.0", port=5000, debug=True)
