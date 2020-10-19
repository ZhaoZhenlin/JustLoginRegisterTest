package com.example.justloginregistertest;

import android.app.Activity;
import android.app.Fragment;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.RadioGroup;
import android.widget.RadioButton;
import android.widget.RadioGroup.OnCheckedChangeListener;


import androidx.appcompat.app.AppCompatActivity;
/**
 * Created by littlecurl 2018/6/24
 */
/**
 * 此类 implements View.OnClickListener 之后，
 * 就可以把onClick事件写到onCreate()方法之外
 * 这样，onCreate()方法中的代码就不会显得很冗余
 */
public class RegisterActivity extends AppCompatActivity implements View.OnClickListener {

    private String realCode;
    private DBOpenHelper mDBOpenHelper;
    private Button mBtRegisteractivityRegister;
    private RelativeLayout mRlRegisteractivityTop;
    private ImageView mIvRegisteractivityBack;
    private LinearLayout mLlRegisteractivityBody;
    private EditText mEtRegisteractivityUsername;
    private EditText mEtRegisteractivityPassword1;
    private EditText mEtRegisteractivityPassword2;
    private EditText mEtRegisteractivityPhonecodes;
//    private TextView show = null;
    private RadioGroup rgroup = null;
//    private RadioButton ch1 = null;
//    private RadioButton ch2 = null;



    private ImageView mIvRegisteractivityShowcode;
    private String generateRealCode;
    private RelativeLayout mRlRegisteractivityBottom;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        initView(); //把register页面的空间初始化一下。然后把他们提到外面(25-36)，方便其他方法引用

        mDBOpenHelper = new DBOpenHelper(this); //获取数据哭操作类

        //将验证码用图片的形式显示出来
        mIvRegisteractivityShowcode.setImageBitmap(Code.getInstance().createBitmap());
        generateRealCode = Code.getInstance().getCode().toLowerCase();
    }

    private void initView(){
        //初始化组件
        mBtRegisteractivityRegister = findViewById(R.id.bt_registeractivity_register);
        mRlRegisteractivityTop = findViewById(R.id.rl_registeractivity_top);
        mIvRegisteractivityBack = findViewById(R.id.iv_registeractivity_back);
        mLlRegisteractivityBody = findViewById(R.id.ll_registeractivity_body);
        mEtRegisteractivityUsername = findViewById(R.id.et_registeractivity_username);
        mEtRegisteractivityPassword1 = findViewById(R.id.et_registeractivity_password1);
        mEtRegisteractivityPassword2 = findViewById(R.id.et_registeractivity_password2);
        mEtRegisteractivityPhonecodes = findViewById(R.id.et_registeractivity_phoneCodes);
//        mEtRegisteractivityIdentity = findViewById(R.id.et_registeractivity_identity);
//        show = (TextView)findViewById(R.id.show);
        rgroup = (RadioGroup)findViewById(R.id.rgroup);
//        ch1 = (RadioButton)findViewById(R.id.ch1);
//        ch2 = (RadioButton)findViewById(R.id.ch2);
        mIvRegisteractivityShowcode = findViewById(R.id.iv_registeractivity_showCode);
        mRlRegisteractivityBottom = findViewById(R.id.rl_registeractivity_bottom);


//        //通过setOnCheckedChangeListener( )来响应按钮的事
//        rgroup.setOnCheckedChangeListener(new OnCheckedChangeListener(){
//            @Override
//            public void onCheckedChanged(RadioGroup rg,int checkedId)
//            {
//                switch(checkedId){
//                    case R.id.ch1:show.setText("Your identity is: teacher staff");break;
//                    case R.id.ch2:show.setText("Your identity is: student");break;
//                }
//            }
//        });
//        if(ch1.getId() == rgroup.getCheckedRadioButtonId()){
//            show.setText(ch1.getText().toString());
//        }else if(ch2.getId() == rgroup.getCheckedRadioButtonId()){
//            show.setText(ch2.getText().toString());
//        }
    /**
     * 可以点击的地方，注册上点击事件
     * 注册页面能点击的就三个地方
     * top处返回箭头、刷新验证码图片、注册按钮
     */
        mIvRegisteractivityBack.setOnClickListener(this);
        mIvRegisteractivityShowcode.setOnClickListener(this);
        mBtRegisteractivityRegister.setOnClickListener(this);

    }

    //为RadioGroup设置监听事件
//		rgroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
//
//        @Override
//        public void onCheckedChanged(RadioGroup rgroup, int checkedId) {
//            // TODO Auto-generated method stub
//            String sexTmp = "";
//            if(checkedId == ch1.getId()){
//                sexTmp = ch1.getText().toString();
//            }else if(checkedId == ch2.getId()){
//                sexTmp = ch2.getText().toString();
//            }
//            show.setText(sexTmp);
//        }
//    });

    //点击事件的监听事件
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.iv_registeractivity_back: //返回登录页面
                Intent intent1 = new Intent(this, LoginActivity.class); // 离开register页面到login页面
                startActivity(intent1); //开始跳转
                finish(); //跳转完之后，销毁此Activity
                break;
            case R.id.iv_registeractivity_showCode:    //改变/刷新 随机验证码的生成
                // 下面两行代码记住就行
                mIvRegisteractivityShowcode.setImageBitmap(Code.getInstance().createBitmap());
                generateRealCode = Code.getInstance().getCode().toLowerCase();
                break;
            case R.id.bt_registeractivity_register:    //注册按钮
                //获取用户输入的用户名、密码、验证码
                String username = mEtRegisteractivityUsername.getText().toString().trim();
                String password = mEtRegisteractivityPassword2.getText().toString().trim();
                String userInputCode = mEtRegisteractivityPhonecodes.getText().toString().toLowerCase();


                String identity = null;
                for (int i = 0; i < rgroup.getChildCount(); i++) {
                    RadioButton rd = (RadioButton) rgroup.getChildAt(i);
                    if (rd.isChecked()) {
                        Toast.makeText(getApplicationContext(), "Your choice is" + rd.getText(), Toast.LENGTH_LONG).show();
                        identity = rd.getText().toString();
                        break;
                    }
                }


                //注册验证
                if (!TextUtils.isEmpty(username) && !TextUtils.isEmpty(password) && !TextUtils.isEmpty(identity) && !TextUtils.isEmpty(userInputCode)) {
//                    if (userInputCode.equals(generateRealCode)) { //string类里面没有判断realcode等不等于null的操作
                    if (TextUtils.equals(userInputCode, generateRealCode)) { //TextUtils, 这个工具类可以判断realcode是否为null
                        //将用户名和密码加入到数据库中
                        mDBOpenHelper.add(username, password, identity);
                        Intent intent2 = new Intent(this, MainActivity.class);
                        startActivity(intent2);
                        finish();
                        Toast.makeText(this, "The verification is passed and the registration is successful", Toast.LENGTH_SHORT).show();
                    } else {
                        Toast.makeText(this, "Verification code error, registration failed", Toast.LENGTH_SHORT).show();
                    }
                } else {
                    // 如果usrname，password，phoneCode是空的话
                    Toast.makeText(this, "\n" +
                            "Incomplete information, registration failed", Toast.LENGTH_SHORT).show();
                }

                break;
        }
    }
}

