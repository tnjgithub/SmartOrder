package com.hanibey.smartordermodel;

import android.graphics.Bitmap;

/**
 * Created by Tanju on 24.09.2017.
 */

public class SelectedOrderItem {

    public String productKey;
    public String Title;
    public double Price;
    public int Quantity;
    public boolean IsServed;

    public String getProductKey() {
        return productKey;
    }

    public void setProductKey(String productKey) {
        this.productKey = productKey;
    }

    public String getTitle() {
        return Title;
    }

    public void setTitle(String title) {
        Title = title;
    }

    public double getPrice() {
        return Price;
    }

    public void setPrice(double price) {
        Price = price;
    }

    public int getQuantity() {
        return Quantity;
    }

    public void setQuantity(int quantity) {
        Quantity = quantity;
    }

    public boolean isServed() {
        return IsServed;
    }

    public void setServed(boolean served) {
        IsServed = served;
    }
}
