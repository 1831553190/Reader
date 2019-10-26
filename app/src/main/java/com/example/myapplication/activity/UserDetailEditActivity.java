package com.example.myapplication.activity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.example.myapplication.R;
import com.example.myapplication.bean.AppData;
import com.example.myapplication.database.UserDataBase;
import com.example.myapplication.model.User;
import com.example.myapplication.unit.GlideCircleBorderTransform;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;

public class UserDetailEditActivity extends AppCompatActivity implements View.OnClickListener {
    ImageView headImg;
    LinearLayout imgLayout;
    TextInputEditText nicknameEdit,ageEdit,cityEdit,emailEdit,signatureEdit;
    TextInputLayout ageEditLayout,emailAddrEditLayout;
    AppCompatButton saveEdit;
    
    private boolean isLogin=false;
    private UserDataBase userDataBase;
    int[] imgIds={R.mipmap.head1,R.mipmap.head2,R.mipmap.head3};
    private int headId;
    SharedPreferences sharedPreferences;
    String userId;
    User user;
    
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.layout_userdetailedit);
        init();
        
        
        userDataBase=new UserDataBase(this);
        sharedPreferences=PreferenceManager.getDefaultSharedPreferences(this);
        isLogin= sharedPreferences.getBoolean(AppData.IS_LOGIN,false);
        userId=sharedPreferences.getString(AppData.USER_ID,"");
        user=userDataBase.queryUser(userId);
        headId=Integer.parseInt(user.getHeadImg());
        Glide.with(this).load(isLogin?imgIds[headId]:R.drawable.ic_person_black_48dp)
                .apply(RequestOptions.circleCropTransform())
                .placeholder(R.drawable.ic_person_black_48dp)
//                .centerCrop()
//                .diskCacheStrategy(DiskCacheStrategy.ALL)
//                .transform(new RoundedCorners(90))
                .transform(new GlideCircleBorderTransform(2, Color.argb(255,255,255,255)))
                .into(headImg);
    }
    
    
    public void init(){
        headImg=findViewById(R.id.headImageEdit);
        imgLayout=findViewById(R.id.layout_headEdit);
        nicknameEdit=findViewById(R.id.nickname_edit);
        ageEdit=findViewById(R.id.age_edit);
        cityEdit=findViewById(R.id.city_edit);
        emailEdit=findViewById(R.id.email_edit);
        signatureEdit=findViewById(R.id.signature_edit);
        saveEdit=findViewById(R.id.save_edit);
        
        ageEditLayout=findViewById(R.id.age_editLayout);
        emailAddrEditLayout=findViewById(R.id.email_addr_editLayout);
        imgLayout.setOnClickListener(this);
        saveEdit.setOnClickListener(this);

    }
    
    @Override
    public void onClick(View v) {
       switch (v.getId()){
           case R.id.layout_headEdit:
               Toast.makeText(this, "头像更换功能尚未开放，敬请期待！", Toast.LENGTH_SHORT).show();
               break;
           case R.id.save_edit:
               String strAge=ageEdit.getText().toString();
               int age=0;
               if (!strAge.equals("")){
                   age=Integer.valueOf(strAge);
               }
               String emailAddr=emailEdit.getText().toString();
               if(!emailAddr.equals("")){
                   if (!emailAddr.contains("@")&&!emailAddr.contains(".")){
                       emailAddrEditLayout.setErrorEnabled(false);
                       emailAddrEditLayout.setError("邮箱地址错误，请重新输入！");
                   }else{
                       user.setEmail(emailEdit.getText().toString());
                   }
               }
               if (age>120||age<0){
                   ageEditLayout.setErrorEnabled(false);
                   ageEditLayout.setError("年龄输入有误，请重新输入！");
               }else{
                   if (!strAge.equals("")){
                       user.setAge(age);
                   }
                   if (!cityEdit.getText().toString().equals("")){
                       user.setCity(cityEdit.getText().toString());
                   }
                   if (!nicknameEdit.getText().toString().equals("")){
                       user.setNickName(nicknameEdit.getText().toString());
                   }
                   if (!signatureEdit.getText().toString().equals("")){
                       user.setUserSignature(signatureEdit.getText().toString());
                   }
                   userDataBase.modefyUserInfo(user);
                   Toast.makeText(this, "用户信息更新成功", Toast.LENGTH_SHORT).show();
                   setResult(1);
                   finish();
               }
               break;
       }
    }
}
