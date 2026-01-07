package com.hls;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;


public class Money {

    private int amount=1000;

    public int get(int value) {
        synchronized (this) {
            return amount -= value;
        }
    }

    public void save(int value) {
        synchronized (this) {
            amount += value;
        }

    }

}
