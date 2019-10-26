package com.example.myapplication.activity;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.ClipData;
import android.content.ClipboardManager;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.core.widget.NestedScrollView;

import com.example.myapplication.R;
import com.example.myapplication.database.UserDataBase;
import com.example.myapplication.model.User;
import com.google.android.material.snackbar.Snackbar;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;

import java.util.Random;

public class SignUpActivity extends Activity implements View.OnClickListener {

    Button btn_generate_id;
    TextView textID_show,alert_id_text,alert_idDone;
    TextInputEditText user_password,user_password_confirm,user_nickname,user_email;
    TextInputLayout user_password_confirmLayout,passLayout;
    AlertDialog.Builder alert;
    UserDataBase userDataBase;
    AlertDialog dialog;

    Random random;
    int count=5,max=1000000000,min=10000,interval=max+min+1;
    long userId;
    View alertView,doneLayout;
    Button alert_btnBack,alert_btnCancel,alert_btnConfirm,alert_btnCopy,alert_btnDone,btn_idSelect;

    NestedScrollView scrollView;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.layout_signin);
        userDataBase=new UserDataBase(this);
        btn_generate_id=findViewById(R.id.signin_button_confirm);
        btn_idSelect=findViewById(R.id.signin_choose);
        textID_show=findViewById(R.id.signin_text_generateid);
        btn_generate_id.setOnClickListener(this);
        btn_idSelect.setOnClickListener(this);
        dialog=dialogInit();
        random=new Random();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.signin_button_confirm:
                count--;
                if (count>0){
                    userId= random.nextInt(interval)+min;
                    while(userDataBase.queryUser(String.valueOf(userId))!=null){
                        userId= random.nextInt(interval)+min;
                    }
                    textID_show.setText("你的id:"+userId);
                    alert_idDone.setText("ID:"+userId);
                    btn_idSelect.setVisibility(View.VISIBLE);
                }else{
                    Snackbar.make(v,"操作太频繁了！晚点再试吧。",Snackbar.LENGTH_SHORT)
                            .setAction("注册该id", new View.OnClickListener() {
                            
                                @Override
                                    public void onClick(View v) {
                                    alert_id_text.setText("id:"+userId);
                                    passLayout.setErrorEnabled(false);
                                    user_password_confirmLayout.setErrorEnabled(false);
                                    dialog.setCancelable(false);
                                    dialog.show();
                                }
                            }).setActionTextColor(getResources().getColor(R.color.colorBlue))
                            .show();
                }
               // Waiting to send non-key event because the touched window has not finished processing certain input events that were delivered to it over 500.0ms ago.  Wait queue length: 15.  Wait queue head age: 298830.0ms.

                break;
            case R.id.signin_choose:
                alert_id_text.setText("你的id:"+userId);
                alert_idDone.setText("ID:"+userId);
                passLayout.setErrorEnabled(false);
                user_password_confirmLayout.setErrorEnabled(false);
                dialog.setCancelable(false);
                dialog.show();
                break;
            case R.id.alert_back:
                finish();
                break;
            case R.id.alert_cancel:
                dialog.setCancelable(true);
                dialog.dismiss();
                break;
                
            case R.id.alert_confirmBtn:
                String user_pass=user_password.getText().toString();
                String user_pass_confirm=user_password_confirm.getText().toString();
                if (user_pass.equals(user_pass_confirm)&&user_pass.length()>=6){
                    User user=new User();
                    user.setUserId(String.valueOf(userId));
                    user.setPassword(user_pass);
                    user.setNickName(user_nickname.getText().toString());
                    user.setEmail(user_email.getText().toString());
                    user.setAge(0);
                    userDataBase.addUser(user);
                    alert_id_text.setVisibility(View.GONE);
                    doneLayout.setVisibility(View.VISIBLE);
                    scrollView.setVisibility(View.GONE);
                }else if(user_pass.length()<6){
                    passLayout.setErrorEnabled(false);
                    passLayout.setError("密码过短");
                } else{
                    user_password_confirmLayout.setErrorEnabled(false);
                    user_password_confirmLayout.setError("密码不一致");
                }
                break;
            case R.id.alert_idcopy:
                ClipboardManager clipboardManager= (ClipboardManager) getSystemService(CLIPBOARD_SERVICE);
                clipboardManager.setPrimaryClip(ClipData.newPlainText("id:",String.valueOf(userId)));
                Snackbar.make(v,"id:"+userId+"已复制",Snackbar.LENGTH_SHORT).show();
                break;
            case R.id.alert_done:
                finish();
                break;
        }

    }

    private AlertDialog dialogInit(){
        alert=new AlertDialog.Builder(this);
        alertView= LayoutInflater.from(this).inflate(R.layout.alert_modify_info,null);
        alert.setTitle("完善信息")
                .setView(alertView);
        user_password=alertView.findViewById(R.id.alert_password);
        user_password_confirm=alertView.findViewById(R.id.alert_password_confirm);
        user_password_confirmLayout=alertView.findViewById(R.id.alert_confirmLayout);
        passLayout=alertView.findViewById(R.id.alert_passwordLayout);
        user_nickname=alertView.findViewById(R.id.alert_nickname);
        user_email=alertView.findViewById(R.id.alert_email);
        alert_btnBack=alertView.findViewById(R.id.alert_back);
        alert_btnCancel=alertView.findViewById(R.id.alert_cancel);
        alert_btnConfirm=alertView.findViewById(R.id.alert_confirmBtn);
        alert_id_text=alertView.findViewById(R.id.alert_id);
        scrollView=alertView.findViewById(R.id.scrollLayout);
        
        doneLayout=alertView.findViewById(R.id.alert_layout_done);
        alert_idDone=alertView.findViewById(R.id.alert_done_id);
        alert_btnCopy=alertView.findViewById(R.id.alert_idcopy);
        alert_btnDone=alertView.findViewById(R.id.alert_done);
        
        
        
        alert_btnBack.setOnClickListener(this);
        alert_btnCancel.setOnClickListener(this);
        alert_btnConfirm.setOnClickListener(this);
        
        
        alert_btnCopy.setOnClickListener(this);
        alert_btnDone.setOnClickListener(this);
        
        alert.setCancelable(false);
        return alert.create();
    }
}
