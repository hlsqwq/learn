package com.hls;

import cn.hutool.core.io.file.FileWriter;

import java.io.File;
import java.io.IOException;

public class demo {

    public void add_file() throws IOException {
        File file = new File("C:\\Users\\hls\\OneDrive\\桌面\\123");
        file.mkdirs();
        for (int i = 0; i < 10; i++) {
            String path = file.getAbsolutePath() + "\\" + i + ".txt";
            new File(path).createNewFile();
        }
    }


    public void find(String directory,String dest){
        File file = new File(directory);
        File[] files = file.listFiles();
        if (files!=null && files.length>0) {
            for (File file1 : files) {
                if (file1.isDirectory()) {
                    find(file1.getAbsolutePath(),dest);
                    return;
                }else if(file1.getName().substring(0,file1.getName().lastIndexOf(".")).equals(dest)){
                    System.out.println("we are finding this");
                    return;
                }
            }
        } else {
            System.out.println("this is not a directory or empty");
        }
        System.out.println("we are not finding this");
    }


    void write(String data) throws IOException {
        File file = new File("q.txt");
        file.createNewFile();
        FileWriter fw = new FileWriter(file.getAbsoluteFile());
        fw.write(data);
    }

}
