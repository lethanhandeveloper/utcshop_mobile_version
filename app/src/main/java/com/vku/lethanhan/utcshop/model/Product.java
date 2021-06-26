package com.vku.lethanhan.utcshop.model;

import java.io.Serializable;

public class Product implements Serializable {
    int id;
    String name;
    String image;
    String description;
    int quantity;
    int sold;
    int price;
    int discount;
    String promotion;
    int category_id;
    String updated_date;

    public Product() {
    }

    public Product(int id, String name, String image, String description, int quantity, int sold, int price, int discount, String promotion, int category_id, String updated_date) {
        this.id = id;
        this.name = name;
        this.image = image;
        this.description = description;
        this.quantity = quantity;
        this.sold = sold;
        this.price = price;
        this.discount = discount;
        this.promotion = promotion;
        this.category_id = category_id;
        this.updated_date = updated_date;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    public int getSold() {
        return sold;
    }

    public void setSold(int sold) {
        this.sold = sold;
    }

    public int getPrice() {
        return price;
    }

    public void setPrice(int price) {
        this.price = price;
    }

    public int getDiscount() {
        return discount;
    }

    public void setDiscount(int discount) {
        this.discount = discount;
    }

    public String getPromotion() {
        return promotion;
    }

    public void setPromotion(String promotion) {
        this.promotion = promotion;
    }

    public int getCategory_id() {
        return category_id;
    }

    public void setCategory_id(int category_id) {
        this.category_id = category_id;
    }

    public String getUpdated_date() {
        return updated_date;
    }

    public void setUpdated_date(String updated_date) {
        this.updated_date = updated_date;
    }

    public boolean isStock(){
        if (this.getQuantity() - this.getSold() >0) return true;
        return false;
    }



    @Override
    public String toString() {
        return "Product{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", image='" + image + '\'' +
                ", description='" + description + '\'' +
                ", quantity=" + quantity +
                ", sold=" + sold +
                ", price=" + price +
                ", discount=" + discount +
                ", promotion='" + promotion + '\'' +
                ", category_id=" + category_id +
                ", updated_date='" + updated_date + '\'' +
                '}';
    }
}
