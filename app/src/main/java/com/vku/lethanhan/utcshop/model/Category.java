package com.vku.lethanhan.utcshop.model;

public class Category {
    int id;
    String name;
    String image;
    int level;

    public Category(int id, String name, String image, int level) {
        this.id = id;
        this.name = name;
        this.image = image;
        this.level = level;
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

    public int getLevel() {
        return level;
    }

    public void setLevel(int level) {
        this.level = level;
    }
}
