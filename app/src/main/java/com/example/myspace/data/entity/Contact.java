package com.example.myspace.data.entity;

import android.content.Intent;

public class Contact implements Comparable<Contact> {

    private int id;

    private String name;

    private String phone;

    private String email;

    private int groupId;

    public Contact() {
    }

    public Contact(String name, String phone, String email) {
        this.name = name;
        this.phone = phone;
        this.email = email;
    }

    public Contact(int id, String name, String phone, String email, int groupId) {
        this.id = id;
        this.name = name;
        this.phone = phone;
        this.email = email;
        this.groupId = groupId;
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

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public int getGroupId() {
        return groupId;
    }

    public void setGroupId(int groupId) {
        this.groupId = groupId;
    }

    @Override
    public String toString() {
        return "Contact{" +
                "id=" + id +
                ", phone='" + phone + '\'' +
                ", email='" + email + '\'' +
                ", groupId=" + groupId +
                '}';
    }

    @Override
    public int compareTo(Contact c) {
        return this.getName().compareTo(c.getName());
    }
}
