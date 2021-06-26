package com.vku.lethanhan.utcshop.model;

public class Filter {
    String name;
    String value;
    String type;
    boolean check;

    public Filter(String name, String value, String type, boolean check) {
        this.name = name;
        this.value = value;
        this.type = type;
        this.check = check;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public boolean isCheck() {
        return check;
    }

    public void setCheck(boolean check) {
        this.check = check;
    }
}
