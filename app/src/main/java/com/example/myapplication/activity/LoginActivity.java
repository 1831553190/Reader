package com.example.myapplication.activity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.view.View;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.Toast;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.localbroadcastmanager.content.LocalBroadcastManager;

import com.example.myapplication.MainActivity;
import com.example.myapplication.R;
import com.example.myapplication.bean.AppData;
import com.example.myapplication.database.UserDataBase;
import com.example.myapplication.model.User;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;

public class LoginActivity extends AppCompatActivity {
    TextInputEditText edit_userId,edit_passWord;
    CheckBox storeCheck;
    SharedPreferences sharedPreferences;
    SharedPreferences.Editor save;
    TextInputLayout loginUserInputLayout,loginPasswordInputLayout;
    UserDataBase userDataBase;
    String usrId;
    User user;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.login_main);
        userDataBase=new UserDataBase(this);
        edit_userId=findViewById(R.id.userId);
        loginUserInputLayout=findViewById(R.id.loginTextLayout);
        loginPasswordInputLayout=findViewById(R.id.loginPasswordInputLayout);
        edit_passWord=findViewById(R.id.passWord);
        storeCheck=findViewById(R.id.store_password);
        sharedPreferences= PreferenceManager.getDefaultSharedPreferences(this);
        boolean isSave=sharedPreferences.getBoolean(AppData.SAVE_PASSWORD,false);
        storeCheck.setChecked(isSave);
        save = sharedPreferences.edit();
        if (isSave) {
            user=userDataBase.queryUser(sharedPreferences.getString(AppData.USER_ID,""));
            if (user!=null){
                edit_userId.setText(user.getUserId());
                edit_passWord.setText(user.getPassword());
            }
        }
        storeCheck.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                save.putBoolean(AppData.SAVE_PASSWORD, isChecked);
                save.apply();
            }
        });
        final UserDataBase userDataBase=new UserDataBase(this);
        findViewById(R.id.loginBtn).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                loginUserInputLayout.setErrorEnabled(false);
                loginPasswordInputLayout.setErrorEnabled(false);
                String user_id=edit_userId.getText().toString();
                String user_pass = edit_passWord.getText().toString();
                if (user_id.length()<5){
                    loginUserInputLayout.setError("输入的账号长度小于5");
                }else if(user_pass.length()<6){
                    loginPasswordInputLayout.setError("输入的密码长度小于6");
                } else {
                    User user = userDataBase.queryUser(user_id);
                    if (user == null) {
                        loginUserInputLayout.setError("用户不存在");
                    } else if (!user_pass.equals(user.getPassword())) {
                        loginPasswordInputLayout.setError("密码错误！");
                    } else {
                        save.putString(AppData.USER_ID, user_id);
                        save.putBoolean(AppData.IS_LOGIN, true);
                        save.putBoolean(AppData.SAVE_PASSWORD, storeCheck.isChecked());
                        save.apply();
                        startActivity(new Intent(LoginActivity.this, MainActivity.class));
                        Toast.makeText(LoginActivity.this, "登录成功！", Toast.LENGTH_SHORT).show();
                        Intent intent = new Intent("android.intent.action.CAST_BROADCAST");
                        intent.putExtra("code", 2);
                        LocalBroadcastManager.getInstance(LoginActivity.this).sendBroadcast(intent);
                        sendBroadcast(intent);
                        finish();
                    }
                }
            }
        });

        findViewById(R.id.signUpBtn).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(LoginActivity.this, SignUpActivity.class));
            }
        });
    }
}
