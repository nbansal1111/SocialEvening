package com.project.socialevening.models;

import com.parse.ParseObject;

/**
 * Created by nitin on 14/11/15.
 */
public class UserCart {
    private ParseObject product;
    private long price;

    public long getPrice() {
        return price;
    }

    public void setPrice(long price) {
        this.price = price;
    }

    public ParseObject getCart() {
        return cart;
    }

    public void setCart(ParseObject cart) {
        this.cart = cart;
    }

    private ParseObject cart;
    private long qty;

    public ParseObject getProduct() {
        return product;
    }

    public void setProduct(ParseObject product) {
        this.product = product;
    }

    public long getQty() {
        return qty;
    }

    public void setQty(long qty) {
        this.qty = qty;
    }
}
