package com.example.myspace.data.dto;

import com.example.myspace.data.Helper;
import com.example.myspace.data.entity.Card;

public class CardDto {

    private String internalCode;
    private String editDateTime;
    private String front;
    private String back;
    private String example;
    private int status;

    public CardDto(Card card) {
        this.internalCode = card.getInternalCode();
        this.editDateTime = Helper.getStringDateTime(card.getEditDateTime());
        this.front = card.getFront();
        this.back = card.getBack();
        this.example = card.getExample();
        this.status = card.getStatus();
    }

    public String getInternalCode() {
        return internalCode;
    }

    public void setInternalCode(String internalCode) {
        this.internalCode = internalCode;
    }

    public String getEditDateTime() {
        return editDateTime;
    }

    public void setEditDateTime(String editDateTime) {
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

    @Override
    public String toString() {
        return "CardDto{" +
                "internalCode='" + internalCode + '\'' +
                ", editDateTime='" + editDateTime + '\'' +
                ", front='" + front + '\'' +
                ", back='" + back + '\'' +
                ", example='" + example + '\'' +
                ", status=" + status +
                '}';
    }
}
