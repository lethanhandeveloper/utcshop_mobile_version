package com.vku.lethanhan.utcshop.model;

import java.io.Serializable;

public class Account implements Serializable {
    String email;
    String password;
    String name;
    String date_of_birth;
    int gender;
    String phone_number;
    String address;

    public Account(String email, String password, String name, String date_of_birth, int gender, String phone_number, String address) {
        this.email = email;
        this.password = password;
        this.name = name;
        this.date_of_birth = date_of_birth;
        this.gender = gender;
        this.phone_number = phone_number;
        this.address = address;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDate_of_birth() {
        return date_of_birth;
    }

    public void setDate_of_birth(String date_of_birth) {
        this.date_of_birth = date_of_birth;
    }

    public int getGender() {
        return gender;
    }

    public void setGender(int gender) {
        this.gender = gender;
    }

    public String getPhone_number() {
        return phone_number;
    }

    public void setPhone_number(String phone_number) {
        this.phone_number = phone_number;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }
}
