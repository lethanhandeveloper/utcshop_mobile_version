package com.vku.lethanhan.utcshop.model;

import android.os.Parcel;
import android.os.Parcelable;

import java.io.Serializable;

public class Cart implements Serializable {
    int id;
    int product_id;
    String name;
    int price;
    int quantity;
    String image;
    int discount;
    boolean checked;

    public Cart(int id, int product_id, String name, int price, int quantity, String image, int discount, boolean checked) {
        this.id = id;
        this.product_id = product_id;
        this.name = name;
        this.price = price;
        this.quantity = quantity;
        this.image = image;
        this.discount = discount;
        this.checked = checked;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getProduct_id() {
        return product_id;
    }

    public void setProduct_id(int product_id) {
        this.product_id = product_id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getPrice() {
        return price;
    }

    public void setPrice(int price) {
        this.price = price;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public int getDiscount() {
        return discount;
    }

    public void setDiscount(int discount) {
        this.discount = discount;
    }

    public boolean isChecked() {
        return checked;
    }

    public void setChecked(boolean checked) {
        this.checked = checked;
    }

    @Override
    public String toString() {
        return "Cart{" +
                "id=" + id +
                ", product_id=" + product_id +
                ", name='" + name + '\'' +
                ", price=" + price +
                ", quantity=" + quantity +
                ", image='" + image + '\'' +
                ", discount=" + discount +
                ", checked=" + checked +
                '}';
    }

    public int getcurrentPrice(){
        if (this.discount > 0){
            return (this.price * (100 - this.discount)) /100;
        }

        return this.price;
    }
}
