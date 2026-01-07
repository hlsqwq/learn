package com.hls;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class Demo2 extends Thread {

    private Money money;



    @Override
    public void run() {
        money.get(100);
        money.save(100);
    }
}
