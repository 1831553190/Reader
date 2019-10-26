package com.example.myapplication.file;

import java.io.*;

public class OpenFile {
    public StringBuffer openFile(String path){
        try {
            File file=new File(path);
            FileInputStream fis=new FileInputStream(file);
            InputStreamReader isr =new InputStreamReader(fis);
            StringBuffer sb=new StringBuffer();
            char[] input=new char[100];
            while (isr.read(input)!=-1){
                sb.append(input);
            }
            isr.close();
            fis.close();
            return sb;
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }
}
