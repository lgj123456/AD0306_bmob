package com.example.yhdj.ad0306bmob;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import cn.bmob.v3.Bmob;
import cn.bmob.v3.BmobUser;
import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.SaveListener;

public class Login extends AppCompatActivity {
    private EditText username;
    private EditText password;
    private Button btn_login;
    private Button btn_reg;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        initViews();
    }

    private void initViews() {
        Bmob.initialize(this, "88550df99426c362c26d3ee1151a6bc6");
        username = (EditText) findViewById(R.id.username);
        password = (EditText) findViewById(R.id.password);
        btn_reg = (Button) findViewById(R.id.btn_reg);
        btn_reg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Login.this, Register_Activity.class);
                startActivity(intent);
            }
        });

        btn_login = (Button) findViewById(R.id.btn_login);
        btn_login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String name = username.getText().toString().trim();
                String psd = password.getText().toString().trim();

                if (name.isEmpty() || psd.isEmpty()) {
                    Toast.makeText(Login.this, "用户名和密码不能为空！！！", Toast.LENGTH_SHORT).show();
                    return;
                }
                BmobUser user = new BmobUser();
                user.setUsername(name);
                user.setPassword(psd);
                if (user == null) {
                    Toast.makeText(Login.this, "error", Toast.LENGTH_SHORT).show();
                    return;
                }
                    user.login(new SaveListener<BmobUser>() {

                        @Override
                        public void done(BmobUser bmobUser, BmobException e) {
                            if (e == null) {
                                Toast.makeText(Login.this, "登录成功！！！", Toast.LENGTH_SHORT).show();
                                Intent intent = new Intent(Login.this, MainActivity.class);

                                startActivity(intent);
                            } else {
                                //登录失败
                            }
                        }
                    });


            }
        });
    }
}
