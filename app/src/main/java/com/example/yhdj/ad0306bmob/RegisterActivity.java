package com.example.yhdj.ad0306bmob;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import cn.bmob.v3.BmobSMS;
import cn.bmob.v3.BmobUser;
import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.QueryListener;
import cn.bmob.v3.listener.SaveListener;
import cn.bmob.v3.listener.UpdateListener;

public class RegisterActivity extends AppCompatActivity {

    private EditText edt_code, edt_username, edt_password, edt_phone, edt_comfirm;
    private Button btn_code;
    private Button btn_reg;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        initViews();
    }


    private void initViews() {
        edt_code = (EditText) findViewById(R.id.edt_code);
        edt_username = (EditText) findViewById(R.id.edt_username);
        edt_password = (EditText) findViewById(R.id.edt_password);
        edt_phone = (EditText) findViewById(R.id.edt_phone);
        edt_comfirm = (EditText) findViewById(R.id.edt_comfirm);
        btn_code = (Button) findViewById(R.id.btn_code);
        btn_reg = (Button) findViewById(R.id.btn_reg);
        btn_code.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String num = edt_phone.getText().toString().trim();
                BmobSMS.requestSMSCode(num, "ADbmob", new QueryListener<Integer>() {
                    @Override
                    public void done(Integer integer, BmobException e) {
                        if (e == null) {
                            Toast.makeText(RegisterActivity.this, "验证码已发送！！！", Toast.LENGTH_SHORT).show();
                            btn_code.setClickable(false);
                            btn_code.setBackgroundColor(Color.GRAY);
                            new CountDownTimer(100000, 1000) {

                                @Override
                                public void onTick(long millisUntilFinished) {

                                    btn_code.setText(millisUntilFinished / 1000 + "秒");
                                }

                                @Override
                                public void onFinish() {
                                    btn_code.setClickable(true);
                                    btn_code.setText("重新发送");
                                }
                            }.start();
                        } else {
                            Toast.makeText(RegisterActivity.this, "验证码发送失败！！！", Toast.LENGTH_SHORT).show();

                        }
                    }
                });
            }
        });

        btn_reg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final String num = edt_phone.getText().toString().trim();
                final String username = edt_username.getText().toString().trim();
                final String password = edt_password.getText().toString().trim();
                String code = edt_code.getText().toString().trim();
                final String comfirmNum = edt_comfirm.getText().toString().trim();

                BmobSMS.verifySmsCode(num, code, new UpdateListener() {
                    @Override
                    public void done(BmobException e) {
                        if (e == null) {
                            Toast.makeText(RegisterActivity.this, "验证成功！！！", Toast.LENGTH_SHORT).show();
                            BmobUser user = new BmobUser();
                            if (StringUtils.isValidUserName(username)) {
                                if (StringUtils.isValidPassword(password)) {
                                    if (password.equals(comfirmNum)) {
                                        user.setMobilePhoneNumber(num);
                                        user.setUsername(username);
                                        user.setPassword(password);
                                        user.setMobilePhoneNumberVerified(true);
                                        user.signUp(new SaveListener<Person>() {

                                            @Override
                                            public void done(Person person, BmobException e) {
                                                if (e == null) {
                                                    Toast.makeText(RegisterActivity.this, "注册成功！！！", Toast.LENGTH_SHORT).show();
                                                    Intent intent = new Intent(RegisterActivity.this, Login.class);
                                                    startActivity(intent);
                                                } else {
                                                    Toast.makeText(RegisterActivity.this, "注册失败！！！", Toast.LENGTH_SHORT).show();
                                                }
                                            }
                                        });

                                    } else {
                                        edt_comfirm.setError("确认密码不一致！！！");

                                    }
                                } else {
                                    edt_password.setError("密码不合法，必须为3到20位数字！！！");

                                }
                            } else {
                                edt_username.setError("用户名不合法，3到20位，首字符为字母！！！");

                            }
                        } else {

                            Toast.makeText(RegisterActivity.this, "验证失败！！！", Toast.LENGTH_SHORT).show();
                        }


                    }
                });
            }
        });

    }
}

