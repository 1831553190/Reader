package com.example.myapplication.test;

import android.content.Context;
import android.graphics.Color;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.AppCompatTextView;

import com.example.myapplication.R;
import com.example.myapplication.bookopen.BookFactory;

public class TestFameView extends FrameLayout {
    AppCompatTextView textContent;


    public TestFameView(@NonNull Context context) {
        super(context);
        init();
    }

    public TestFameView(@NonNull Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    private void init(){
        View view = LayoutInflater.from(getContext()).inflate(R.layout.frame_testview,this,false);
        addView(view);
        textContent=view.findViewById(R.id.text_novel);
        textContent.setBackgroundColor(Color.WHITE);
    }
    public TextView getTextContent(){
        return textContent;
    }
 

    public void updateData(String content){
        textContent.setText(content);
    }

}
