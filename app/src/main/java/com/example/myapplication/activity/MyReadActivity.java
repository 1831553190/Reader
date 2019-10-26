package com.example.myapplication.activity;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.example.myapplication.R;
import com.example.myapplication.test.ContentSwitchView;
import com.example.myapplication.view.CoverPageView;
import com.example.myapplication.view.PageTurnView;
import com.example.myapplication.view.PicturesPageFactory;
import com.google.android.material.bottomsheet.BottomSheetBehavior;
import com.google.android.material.bottomsheet.BottomSheetDialog;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

public class MyReadActivity extends AppCompatActivity {

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ContentSwitchView view=new ContentSwitchView(this);
        view.updateData("/storage/emulated/0/Download/1190个书源.txt");
        setContentView(view);
        

    }
}
