package com.hls.template;

public interface CallBack {

    /**
     * It is called when data arrivals
     * @param count  serial id
     * @param data   receive data
     */
    void read(int count,String data);

}
