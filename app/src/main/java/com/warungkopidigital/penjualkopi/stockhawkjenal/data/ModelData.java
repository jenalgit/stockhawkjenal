package com.warungkopidigital.penjualkopi.stockhawkjenal.data;

/**
 * Created by penjualkopi on 17/12/17.
 */

public class ModelData {
    private int id;
    private String symbol;
    private Double price;
    private Double absolute_change;
    private Double percentage_change;
    private String history;

    public ModelData() {
    }

    public ModelData(int id, String symbol, Double price, Double absolute_change, Double percentage_change, String history) {
        this.id = id;
        this.symbol = symbol;
        this.price = price;
        this.absolute_change = absolute_change;
        this.percentage_change = percentage_change;
        this.history = history;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getSymbol() {
        return symbol;
    }

    public void setSymbol(String symbol) {
        this.symbol = symbol;
    }

    public Double getPrice() {
        return price;
    }

    public void setPrice(Double price) {
        this.price = price;
    }

    public Double getAbsolute_change() {
        return absolute_change;
    }

    public void setAbsolute_change(Double absolute_change) {
        this.absolute_change = absolute_change;
    }

    public Double getPercentage_change() {
        return percentage_change;
    }

    public void setPercentage_change(Double percentage_change) {
        this.percentage_change = percentage_change;
    }

    public String getHistory() {
        return history;
    }

    public void setHistory(String history) {
        this.history = history;
    }
}
