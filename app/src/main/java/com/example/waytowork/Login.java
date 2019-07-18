package com.example.waytowork;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Looper;
import android.os.StrictMode;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.HttpClient;
import org.apache.http.client.ResponseHandler;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.BasicResponseHandler;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;

import java.util.ArrayList;
import java.util.List;

public class Login extends AppCompatActivity {

    EditText id,pw;
    Button sign_up;
    ImageButton login;
    View.OnClickListener cl;
    ProgressDialog dialog = null;

    TextView vvv;

    // 이것들 사용하기 위해 모듈에 compileOnly 'org.jbundle.util.osgi.wrapped:org.jbundle.util.osgi.wrapped.org.apache.http.client:4.1.2' 적용
    HttpPost httppost;
    HttpResponse response;
    HttpClient httpclient;
    List<NameValuePair> nameValuePairs;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.login);
        // 개발자 문제점 발견에 도움을 주는 친구
        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(policy);


        id = (EditText) findViewById(R.id.id);
        pw = (EditText) findViewById(R.id.pw);
        vvv = (TextView)findViewById(R.id.vvv);

        login = (ImageButton) findViewById(R.id.login);
        sign_up = (Button) findViewById(R.id.sign_up);

        cl = new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                switch (view.getId()){
                    case R.id.login:
                        if( id.getText().toString().equals("")) {
                            Toast.makeText(Login.this, "아이디를 입력해주세요.", Toast.LENGTH_LONG).show();
                        } else{
                            new Thread(new Runnable(){
                                public void run(){
                                    Looper.prepare();
                                    login();
                                    Looper.loop();
                                }
                            }).start();
                        }
                        break;
                    case R.id.sign_up:
                        Intent intent = new Intent(getApplicationContext(),JoinUser.class);
                        startActivity(intent);
                        finish();
                        break;
                }
            }
        };
        login.setOnClickListener(cl);
        sign_up.setOnClickListener(cl);
    }
    void login(){
        try{
            httpclient = new DefaultHttpClient();  //안드로이드에서 접속할때 클라이언트
            httppost = new HttpPost("http://shingu.freehost.kr/3_project/02_login_check.php");  //접속할주소
            nameValuePairs = new ArrayList<NameValuePair>(2);  //배열에 ip,비번 배열에 추가하기
            nameValuePairs.add(new BasicNameValuePair("member_id",id.getText().toString()));
            nameValuePairs.add(new BasicNameValuePair("member_pw",pw.getText().toString()));
            httppost.setEntity(new UrlEncodedFormEntity(nameValuePairs));
            response = httpclient.execute(httppost);//실행된값
            ResponseHandler<String> responseHandler = new BasicResponseHandler();  //받아온 값들을 string으로 반환해준다,핸들러 역할도 한다
            final String response = httpclient.execute(httppost,responseHandler);


            if(response.contains(id.getText().toString())){
                dialog = ProgressDialog.show(Login.this, "", // 여기 에러존재
                        "로그인 중...", true);
                Intent in = new Intent(Login.this, MainActivity.class);
                startActivity(in);
                finish();
            }else{
                Toast.makeText(getApplicationContext(),"실패",Toast.LENGTH_SHORT).show();
            }
        } catch (Exception e){

        }
    }



}