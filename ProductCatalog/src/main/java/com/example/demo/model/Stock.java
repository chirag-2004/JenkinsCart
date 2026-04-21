package com.example.demo.model;

public class Stock {
    private Long pid;
    private int noOfItemsLeft;

    public Long getPid() {
        return pid;
    }

    public void setPid(Long pid) {
        this.pid = pid;
    }

    public int getNoOfItemsLeft() {
        return noOfItemsLeft;
    }

    public void setNoOfItemsLeft(int noOfItemsLeft) {
        this.noOfItemsLeft = noOfItemsLeft;
    }
}
