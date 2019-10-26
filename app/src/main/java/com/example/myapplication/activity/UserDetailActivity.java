package com.example.myapplication.activity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;
import androidx.localbroadcastmanager.content.LocalBroadcastManager;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.example.myapplication.R;
import com.example.myapplication.bean.AppData;
import com.example.myapplication.unit.GlideCircleBorderTransform;
import com.example.myapplication.database.UserDataBase;
import com.example.myapplication.model.User;

public class UserDetailActivity extends AppCompatActivity implements View.OnClickListener {

    UserDataBase userDataBase;
    String userId;
    TextView textview_userId,textView_nickName,textView_userSignature,textView_userAge,textview_userEmail,textView_userCity;
    int[] headImg={R.mipmap.head1,R.mipmap.head2,R.mipmap.head3};
    boolean isLogin=false,isSaveCheck;
    int headId=0;
    SharedPreferences save;
    User user;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.layout_userdetail);
        userDataBase=new UserDataBase(this);
        save=PreferenceManager.getDefaultSharedPreferences(this);
        
        textView_nickName=findViewById(R.id.detail_nickname);
        textview_userId=findViewById(R.id.detail_userid);
        textView_userSignature=findViewById(R.id.detail_signature);
        
        textView_userAge=findViewById(R.id.detail_userAge);
        textview_userEmail=findViewById(R.id.detail_userEmail);
        textView_userCity=findViewById(R.id.detail_userCity);
        
        AppCompatButton btnEdit=findViewById(R.id.detail_edit);
        AppCompatButton btnLogout =findViewById(R.id.logout);
        ImageView imageView=findViewById(R.id.detail_head);
        
        isSaveCheck=save.getBoolean(AppData.SAVE_PASSWORD,false);
        userId=save.getString(AppData.USER_ID,"");
        init();
        
        
        headId=Integer.parseInt(user.getHeadImg());
        isLogin=save.getBoolean(AppData.IS_LOGIN,false);
        
        Glide.with(this).load(isLogin?headImg[headId]:R.drawable.ic_person_black_48dp)
                .apply(RequestOptions.circleCropTransform())
                .placeholder(R.drawable.ic_person_black_48dp)
//                .centerCrop()
//                .diskCacheStrategy(DiskCacheStrategy.ALL)
//                .transform(new RoundedCorners(90))
                .transform(new GlideCircleBorderTransform(2, Color.argb(255,255,255,255)))
                .into(imageView);
        btnEdit.setOnClickListener(this);
        btnLogout.setOnClickListener(this);
    }

    public void init(){
        user=userDataBase.queryUser(userId);
    
        textview_userId.setText(user.getUserId());
        textView_nickName.setText(user.getNickName()==null?"未设置":user.getNickName());
        textView_userSignature.setText(user.getUserSignature()==null?"这个家伙很懒，什么都没留下！":user.getUserSignature());
    
        textView_userAge.setText(""+user.getAge());
        textview_userEmail.setText(user.getEmail());
        textView_userCity.setText(user.getCity());
    }
    
    
    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        Log.d("TAG", "onActivityResult: "+resultCode+"res"+requestCode);
//        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode==1){
            init();
//            Toast.makeText(this, "Update", Toast.LENGTH_SHORT).show();
        }
    }
    
    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.logout:
                SharedPreferences.Editor editor=save.edit();
                if (!isSaveCheck){
                    editor.putString(AppData.USER_ID,null);
                }
                editor.putBoolean(AppData.IS_LOGIN, false);
                editor.apply();
                Intent intent=new Intent("android.intent.action.CAST_BROADCAST");
                intent.putExtra("code",2);
                LocalBroadcastManager.getInstance(UserDetailActivity.this).sendBroadcast(intent);
                sendBroadcast(intent);
                Toast.makeText(UserDetailActivity.this, "已注销", Toast.LENGTH_SHORT).show();
                finish();
                break;
            case R.id.detail_edit:
                startActivityForResult(new Intent(UserDetailActivity.this,UserDetailEditActivity.class),0);
    
                break;
        }
        
    }
}
