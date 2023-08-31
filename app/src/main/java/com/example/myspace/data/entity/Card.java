package com.example.myspace.data.entity;

import com.google.gson.annotations.SerializedName;

import java.time.LocalDate;
import java.time.LocalDateTime;

public class Card {

    private  int id;

    @SerializedName("internalCode")
    private String internalCode;

    @SerializedName("editDateTime")
    private LocalDateTime editDateTime;

    @SerializedName("front")
    private String front;

    @SerializedName("back")
    private String back;

    @SerializedName("example")
    private String example;

    @SerializedName("status")
    private int status;

    public Card() {
    }

    public Card(LocalDateTime editDateTime, String front, String back, String example, int status) {
        this.editDateTime = editDateTime;
        this.front = front;
        this.back = back;
        this.example = example;
        this.status = status;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getInternalCode() {
        return internalCode;
    }

    public void setInternalCode(String internalCode) {
        this.internalCode = internalCode;
    }

    public LocalDateTime getEditDateTime() {
        return editDateTime;
    }

    public void setEditDateTime(LocalDateTime editDateTime) {
        this.editDateTime = editDateTime;
    }

    public String getFront() {
        return front;
    }

    public void setFront(String front) {
        this.front = front;
    }

    public String getBack() {
        return back;
    }

    public void setBack(String back) {
        this.back = back;
    }

    public String getExample() {
        return example;
    }

    public void setExample(String example) {
        this.example = example;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public String print() {
        return this.id + " " + this.getEditDateTime().toString() + " " + this.getFront() + " | " + this.getBack() + " | " + this.getExample() + " " + this.getStatus();
    }

    @Override
    public int hashCode() {
        int hashCode = 1;

        hashCode = hashCode * 37 + this.id;
        hashCode = hashCode * 37 + this.front.hashCode();

        return hashCode;
    }
}
