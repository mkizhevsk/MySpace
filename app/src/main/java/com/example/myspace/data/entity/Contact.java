package com.example.myspace.data.entity;

public class Contact {

    private int id;

    private String phone;

    private String email;

    private int groupId;

    public Contact() {
    }

    public Contact(int id, String phone, String email, int groupId) {
        this.id = id;
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
}
