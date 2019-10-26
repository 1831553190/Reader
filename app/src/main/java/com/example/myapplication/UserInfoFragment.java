package com.example.myapplication;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.AppCompatImageView;
import androidx.fragment.app.Fragment;
import androidx.localbroadcastmanager.content.LocalBroadcastManager;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.example.myapplication.activity.UserDetailActivity;
import com.example.myapplication.bean.AppData;
import com.example.myapplication.model.User;
import com.example.myapplication.unit.GlideCircleBorderTransform;
import com.example.myapplication.database.UserDataBase;
import com.example.myapplication.activity.LoginActivity;


public class UserInfoFragment extends Fragment implements View.OnClickListener {
    View userLayout,view;
    SharedPreferences sharedPreferences;
    Button btnLogin;
    TextView nickName;
    boolean isLogin=false;
    int[] headImg={R.mipmap.head1,R.mipmap.head2,R.mipmap.head3};

    int headId=0;
    UserDataBase userDataBase;
    AppCompatImageView headerImg;
    User user;
    View settings;



    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = LayoutInflater.from(getContext()).inflate(R.layout.fragment_user, null);
        userDataBase=new UserDataBase(getContext());
        
        userLayout=view.findViewById(R.id.userLayout);
        btnLogin=view.findViewById(R.id.user_login);
        headerImg = view.findViewById(R.id.headerImg);
        nickName=view.findViewById(R.id.userNickName);
        settings=view.findViewById(R.id.layout_settings);
        settings.setOnClickListener(this);
//        testView=view.findViewById(R.id.myReadView);
        userLayout.setOnClickListener(this);
        btnLogin.setOnClickListener(this);
//        testView.setOnClickListener(this);
        
        sharedPreferences= PreferenceManager.getDefaultSharedPreferences(getContext());
        init();
        LocalBroadcastManager broadcastManager=LocalBroadcastManager.getInstance(getContext());
        IntentFilter intentFilter=new IntentFilter();
        intentFilter.addAction("android.intent.action.CAST_BROADCAST");
        BroadcastReceiver broadcastReceiver=new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {
                int code=intent.getIntExtra("code",0);
                if(code==2){
                    init();
                }
            }
        };
        Glide.with(view).load(isLogin?headImg[headId]:R.drawable.ic_person_black_48dp)
                .apply(RequestOptions.circleCropTransform())
                .placeholder(R.drawable.ic_person_black_48dp)
//                .centerCrop()
//                .diskCacheStrategy(DiskCacheStrategy.ALL)
//                .transform(new RoundedCorners(90))
                .transform(new GlideCircleBorderTransform(2, Color.argb(255,255,255,255)))
                .into(headerImg);
        broadcastManager.registerReceiver(broadcastReceiver,intentFilter);
        return view;

    }


    public void init(){
        isLogin=sharedPreferences.getBoolean(AppData.IS_LOGIN,false);
        if(isLogin){
            user=userDataBase.queryUser(sharedPreferences.getString(AppData.USER_ID,""));
            if (user!=null){
                btnLogin.setText("切换用户");
            }
            headId=Integer.parseInt(user.getHeadImg());
            nickName.setText(user.getNickName().equals("")?user.getUserId():user.getNickName());
        }else{
            btnLogin.setText("登录");
            nickName.setText("未登录");
            headId=0;
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.user_login:
                startActivity(new Intent(getContext(), LoginActivity.class));
                break;
            case R.id.userLayout:
                if (isLogin){
                    Intent intent=new Intent(getContext(), UserDetailActivity.class);
                    startActivity(intent);
                }else{
                    startActivity(new Intent(getContext(), LoginActivity.class));
                }
                break;
            case R.id.layout_settings:
                startActivity(new Intent(getContext(), SettingPreference.class));
                break;
//            case R.id.myReadView:
//                startActivity(new Intent(getContext(),MyReadActivity.class));
//                break;

        }
    }


}
