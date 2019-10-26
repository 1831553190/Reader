package com.example.myapplication.bookopen;

import android.app.Activity;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.os.Build;
import android.util.DisplayMetrics;
import android.util.Log;
import android.widget.TextView;
import android.widget.Toast;


import androidx.annotation.RequiresApi;

import com.example.myapplication.R;
import com.example.myapplication.unit.ScreenUtils;

import org.mozilla.universalchardet.UniversalDetector;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.io.UnsupportedEncodingException;
import java.nio.MappedByteBuffer;
import java.nio.channels.FileChannel;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class BookFactory {

    private static int screenHeight, screenWidth;//实际屏幕尺寸
    private static int pageHeight,pageWidth;//文字排版页面尺寸
    private static int lineNumber;//行数
//    private int lineSpace = Util.getPXWithDP(5);
    private static int fontSize ;
//    private static final int margin = Util.getPXWithDP(5);//文字显示距离屏幕实际尺寸的偏移量
    private Paint mPaint;
    private int begin;//当前阅读的字节数_开始
    private int end;//当前阅读的字节数_结束

    private static int mPageSize=0;


    private Context mContext;

    private Canvas mCanvas;
    private ArrayList<String> content = new ArrayList<>();
    boolean isFirst=true;
    String currentContent;




    private long fileLength;//文件长度

    RandomAccessFile randomAccessFile;
    MappedByteBuffer mappedByteBuffer;
    private int margin=0;
    private static int lineSpace;
    String encoding = null;
    int contentIndex=0;





    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN)
    public BookFactory(Context context, TextView textView) {
        DisplayMetrics metrics = new DisplayMetrics();
        ((Activity)context).getWindowManager().getDefaultDisplay().getMetrics(metrics);
        screenHeight = metrics.heightPixels; //textView.getBottom()-textView.getTop();
        screenWidth = metrics.widthPixels;
        fontSize = ScreenUtils.sp2px(context,18);//(int) textView.getTextSize();

        pageHeight = screenHeight  - fontSize;//- margin*2
        pageWidth = screenWidth; //-margin*2;
        lineSpace= 21;
        lineNumber = pageHeight/(fontSize+lineSpace);
        mPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        mPaint.setTextSize(fontSize);
    }


    public static String getFileIncode(File file) {

        if (!file.exists()) {
            System.err.println("getFileIncode: file not exists!");
            return null;
        }
        byte[] buf = new byte[4096];
        FileInputStream fis = null;
        try {
            fis = new FileInputStream(file);
            UniversalDetector detector = new UniversalDetector(null);
            int nread;
            while ((nread = fis.read(buf)) > 0 && !detector.isDone()) {
                detector.handleData(buf, 0, nread);
            }
            detector.dataEnd();
            String encoding = detector.getDetectedCharset();
            if (encoding != null) {
                System.out.println("Detected encoding = " + encoding);
            } else {
                System.out.println("No encoding detected.");
            }

            detector.reset();
            fis.close();
            return encoding;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }


    public void openBook(String path){
        begin=end=0;
        File file=new File(path);
        String code=getFileIncode(file);
        encoding=code==null?"UTF-8":code;
        fileLength=file.length();
        try {
            randomAccessFile=new RandomAccessFile(file,"r");
            mappedByteBuffer=randomAccessFile.getChannel().map(FileChannel.MapMode.READ_ONLY,0,fileLength);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }



        //向后读取一个段落，返回bytes

    public String currentContent(){
        if(end >= fileLength){
            return "没有下一页";
        }else{
            content.clear();
            pageDown();
        }
        return printPage();
    }

    public String nextContent(){
        if(end >= fileLength){
            return "没有下一页";
        }else{
            content.clear();
            contentIndex=end;
            nextPageContent(contentIndex);
        }
        return printPage();
    }

    //上一页
    public String preContent(){
        if(begin <= 0){
            return "没有上一页";
        }else{
            content.clear();
            contentIndex=begin;
            contentIndex=prePageContent(contentIndex);
            content.clear();
            nextPageContent(contentIndex);
        }
        return printPage();
    }



    //获取后一页的内容
    private int nextPageContent(int index){
        String strParagraph = "";
        while((content.size()<lineNumber) && (index< fileLength)){
            byte[] byteTemp = readParagraphForward(index);
            index += byteTemp.length;
            try{
                strParagraph = new String(byteTemp, encoding);
            }catch(Exception e){
                e.printStackTrace();
            }
            //计算每行需要的字数，切断string放入list中
            while(strParagraph.length() >  0){
                int size = mPaint.breakText(strParagraph,true,pageWidth,null);
                content.add(strParagraph.substring(0,size));
                strParagraph = strParagraph.substring(size);
                if(content.size() >= lineNumber){
                    break;
                }
            }
            //如有剩余，则将指针回退
            if(strParagraph.length()>0){
                try{
                    index -= (strParagraph).getBytes(encoding).length;
                }catch(Exception e){
                    e.printStackTrace();
                }
            }

        }
        return index;
    }

    //读取前一页的内容
    private  int prePageContent(int index) {
        String strParagraph = "";
        while (content.size() < lineNumber && index > 0) {
            byte[] byteTemp = readParagraphBack(index);
            index -= byteTemp.length;
            try {
                strParagraph = new String(byteTemp, encoding);
            } catch (UnsupportedEncodingException e) {
                e.printStackTrace();
            }
            while (strParagraph.length() > 0) {
                int size = mPaint.breakText(strParagraph, true, pageWidth, null);
                content.add(strParagraph.substring(0,size));
                strParagraph = strParagraph.substring(size);
                if (content.size() >= lineNumber) {
                    break;
                }
            }
            if (strParagraph.length() > 0) {
                try {
                    index += strParagraph.getBytes(encoding).length;
                } catch (UnsupportedEncodingException u) {
                    u.printStackTrace();
                }
            }
        }

        return index;
    }















        //下一页
    public String nextPage(){
        if(end >= fileLength){
            return "没有下一页";
        }else{
            content.clear();
            begin = end;
            pageDown();
        }
        return printPage();
    }




    //上一页
    public String prePage(){
        if(begin <= 0){
            return "没有上一页";
        }else{
            content.clear();
            end = begin;
            pageUp();
            contentIndex=begin;
            content.clear();
            nextPageContent(contentIndex);
        }
        return printPage();
    }


    //向后读取一个段落，返回bytes
    private byte[] readParagraphForward(int end){
        byte b0;
        int i = end;
        while(i < fileLength){
            b0 = mappedByteBuffer.get(i);
            if(b0 == 10) {
                break;
            }
            i++;
        }
        i = (int) Math.min(fileLength-1,i);

        int nParaSize = i - end+1  ;
        byte[] buf = new byte[nParaSize];
        for (i = 0; i < nParaSize; i++) {
            buf[i] =  mappedByteBuffer.get(end + i);
        }
        return buf;
    }



    //向前读取一个段落
    private byte[] readParagraphBack(int begin){
        byte bytes ;
        int i = begin -1 ;
        while(i > 0){
            bytes = mappedByteBuffer.get(i);
            if(bytes == 0x0a && i != begin -1 ){
                i++;
                break;
            }
            i--;
        }
        int nParaSize = begin-i  ;
        byte[] buf = new byte[nParaSize];
        for (int j = 0; j < nParaSize; j++) {
            buf[j] = mappedByteBuffer.get(i + j);
        }
        return buf;
    }
    //获取后一页的内容
    private void pageDown(){
        String strParagraph = "";
        while((content.size()<lineNumber) && (end< fileLength)){
            byte[] byteTemp = readParagraphForward(end);
            end += byteTemp.length;
            try{
                strParagraph = new String(byteTemp, encoding);
            }catch(Exception e){
                e.printStackTrace();
            }
//            strParagraph = strParagraph.replaceAll("\r\n","  ");
//            strParagraph = strParagraph.replaceAll("\n", " ");
            //计算每行需要的字数，切断string放入list中
            while(strParagraph.length() >  0){
                int size = mPaint.breakText(strParagraph,true,pageWidth,null);
                content.add(strParagraph.substring(0,size));
                strParagraph = strParagraph.substring(size);
                if(content.size() >= lineNumber){
                    break;
                }
            }
            //如有剩余，则将指针回退
            if(strParagraph.length()>0){
                try{
                    end -= (strParagraph).getBytes(encoding).length;
                }catch(Exception e){
                    e.printStackTrace();
                }
            }

        }
    }
    //读取前一页的内容
    private  void pageUp(){
        String strParagraph = "";
        while(content.size()<lineNumber && begin>0){
            byte[] byteTemp = readParagraphBack(begin);
            begin -= byteTemp.length;
            try{
                strParagraph = new String(byteTemp, encoding);
            }catch(UnsupportedEncodingException e){
                e.printStackTrace();
            }
//            strParagraph = strParagraph.replaceAll("\r\n","  ");
//            strParagraph = strParagraph.replaceAll("\n","  ");
            while(strParagraph.length() > 0){
                int size = mPaint.breakText(strParagraph,true,pageWidth,null);
                content.add(strParagraph.substring(0,size));
                strParagraph = strParagraph.substring(size);
                if(content.size() >= lineNumber){
                    break;
                }
            }
            if(strParagraph.length() > 0){
                try{
                    begin+= strParagraph.getBytes(encoding).length;
                }catch (UnsupportedEncodingException u){
                    u.printStackTrace();
                }
            }
        }
    }
    private String printPage(){
        StringBuilder buffer=new StringBuilder();
        if(content.size()>0){
//            int y = margin;

//                mCanvas.drawColor(mContext.getResources().getColor(R.color.dayModeBackgroundColor));

            for(String line : content){
//                y += fontSize+lineSpace;
//                mCanvas.drawText(line,margin,y, mPaint);
                buffer.append(line);
            }
            float percent = (float) begin / fileLength *100;
            DecimalFormat format = new DecimalFormat("#0.00");
            String readingProgress = format.format(percent)+"%";
            int length = (int ) mPaint.measureText(readingProgress);
//            mCanvas.drawText(readingProgress, (screenWidth - length) / 2, screenHeight - margin, mPaint);

            //显示时间

//            mView.invalidate();
        }
        return buffer.toString();
    }
}
