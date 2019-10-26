package com.example.myapplication.unit;

import android.widget.Toast;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.nio.charset.Charset;

public class ProcessFile {
    private long pages;//总页数
    private final int SIZE = 600;//每一页固定的字节数
    private long bytecount;
    private long currentPage;
    private RandomAccessFile readFile;


//    public ProcessFile(String path) {
//        File file = new File(path);
//        try {
//            readFile = new RandomAccessFile(file, "r");
//            bytecount = readFile.length();
//            pages = bytecount / SIZE;
//        } catch (FileNotFoundException e) {
//            e.printStackTrace();
//        } catch (IOException e) {
//            e.printStackTrace();
//        }
//
//    }

    public ProcessFile(String path, int currentPage) {
        File file = new File(path);
        try {
            readFile = new RandomAccessFile(file, "r");
            bytecount = readFile.length();
            pages = bytecount / SIZE;
            this.currentPage = currentPage;
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    private void seek(long page) {
        try {
            readFile.seek(page*SIZE);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    //固定读取 SIZE+30个字节的内容 为什么+30 ？
    //读取的为字节 需要进行转码 转码不可能刚好转的就是文本内容,
    //一页的末尾 和开始出有可能乱码 每一次多读30个字节 是为了第一页乱码位置
    //在第二页就可以正常显示出内容 不影响阅读

    private String read(){
        byte[] ch=new byte[SIZE+30];
        try{
            readFile.read(ch);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return new String(ch, Charset.forName("utf-8"));
    }
    public String getPrevious(){
        String content=null;
        if (currentPage<=1){
            seek(currentPage-1);
            content=read();
        }else{
            seek(currentPage-2);
            content=read();
            currentPage--;
        }
        return  content;
    }
    public String getNext(){
        String content=null;
        if (currentPage>=pages){
            seek(currentPage-1);
            content=read();
        }else{
            seek(currentPage);
            content=read();
            currentPage++;
        }
        return  content;
    }


}
