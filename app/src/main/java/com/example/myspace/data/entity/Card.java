package com.example.myspace.data.entity;

import android.util.Log;

import java.time.LocalDate;

public class Card {

    private  int id;

    private LocalDate date;

    private String front;

    private String back;

    private String example;

    private int status;

    public Card() {
    }

    public Card(LocalDate date, String front, String back, String example, int status) {
        this.date = date;
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

    public LocalDate getDate() {
        return date;
    }

    public void setDate(LocalDate date) {
        this.date = date;
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
        return this.getDate().toString() + " " + this.getFront() + " | " + this.getBack() + " | " + this.getExample() + " " + this.getStatus();
    }
}
