package com.example.waytowork;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Switch;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.net.URLEncoder;

public class JoinUser extends AppCompatActivity {
    private EditText editTextId,editTextPw,checkPw,editTextName,editTextEmail;
    Button join ,id_check,tos,reset;
    View.OnClickListener cl;
    String Id;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.joinuser);
        editTextId = (EditText) findViewById(R.id.input_id);
        editTextPw = (EditText) findViewById(R.id.input_pw);
        checkPw = (EditText) findViewById(R.id.input_pw2);
        editTextName = (EditText) findViewById(R.id.input_name);
        editTextEmail = (EditText) findViewById(R.id.input_email);


        join = (Button) findViewById(R.id.join);
        id_check = (Button) findViewById(R.id.id_check);
        tos = (Button) findViewById(R.id.tos);
        reset = (Button) findViewById(R.id.reset);
        cl = new View.OnClickListener(){
            @Override
            public void onClick(View view) {
                switch (view.getId()){
                    case R.id.join:
                         Id = editTextId.getText().toString();
                        String Pw = editTextPw.getText().toString();
                        String Chpw = checkPw.getText().toString();
                        String Name = editTextName.getText().toString();
                        String Email = editTextEmail.getText().toString();
                        if(Pw.equalsIgnoreCase(Chpw)) {

                            insertoToDatabase(Id, Pw, Name, Email);

                            Intent intent = new Intent(getApplicationContext(),Login.class);
                            startActivity(intent);
                            finish();
                        }
                        else checkPw.setText("");
                        break;
                    case R.id.id_check:
                        //리턴 셀렉트 할만한거
                        CheckId(Id);

                        break;
                    case R.id.tos:
                        Intent intent = new Intent(JoinUser.this, Tos.class);
                        startActivityForResult(intent,1);
                        break;
                    case R.id.reset:
                        editTextId.setText("");
                        editTextPw.setText("");
                        checkPw.setText("");
                        editTextName.setText("");
                        editTextEmail.setText("");

                }

            }
        };
        join.setOnClickListener(cl);
        id_check.setOnClickListener(cl);
        tos.setOnClickListener(cl);
        reset.setOnClickListener(cl);
    }


    private void CheckId(String Id ) {
        class CheckData extends AsyncTask<String, Void, String> {
            // ProgressDialog loading;
            @Override
            protected void onPreExecute() {
                super.onPreExecute();
                //   loading = ProgressDialog.show(SignupPage.this, "Please Wait", null, true, true);
            }
            @Override
            protected void onPostExecute(String s) {
                super.onPostExecute(s);
                //loading.dismiss();
                //Toast.makeText(getApplicationContext(), s, Toast.LENGTH_LONG).show();
            }
            @Override
            protected String doInBackground(String... params) {

                try {
                    String Id = (String) params[0];


                    String link = "http://shingu.freehost.kr/3_project/Select_id.php";
                    String data =  URLEncoder.encode(Id, "UTF-8");

                    URL url = new URL(link);
                    URLConnection conn = url.openConnection();

                    conn.setDoOutput(true);
                    OutputStreamWriter wr = new OutputStreamWriter(conn.getOutputStream());

                    wr.write(data);
                    wr.flush();

                    BufferedReader reader = new BufferedReader(new InputStreamReader(conn.getInputStream()));

                    StringBuilder sb = new StringBuilder();
                    String line = null;

                    // Read Server Response
                    while ((line = reader.readLine()) != null) {
                        sb.append(line);
                        break;
                    }
                    return sb.toString();
                } catch (Exception e) {
                    return new String("Exception: " + e.getMessage());
                }
            }
        }
        CheckData task = new CheckData();
        task.execute(Id);
    }

    private void insertoToDatabase(String Id, String Pw, String Name, String Email ) {
        class InsertData extends AsyncTask<String, Void, String> {
            // ProgressDialog loading;
            @Override
            protected void onPreExecute() {
                super.onPreExecute();
                //   loading = ProgressDialog.show(SignupPage.this, "Please Wait", null, true, true);
            }
            @Override
            protected void onPostExecute(String s) {
                super.onPostExecute(s);
                //loading.dismiss();
                Toast.makeText(getApplicationContext(), s, Toast.LENGTH_LONG).show();
            }
            @Override
            protected String doInBackground(String... params) {

                try {
                    String Id = (String) params[0];
                    String Pw = (String) params[1];
                    String Name = (String) params[2];
                    String Email = (String) params[3];

                    String link = "http://shingu.freehost.kr/3_project/03_member_insert.php";
                    String data = URLEncoder.encode("member_id", "UTF-8") + "=" + URLEncoder.encode(Id, "UTF-8");
                    data += "&" + URLEncoder.encode("member_pw", "UTF-8") + "=" + URLEncoder.encode(Pw, "UTF-8");
                    data += "&" + URLEncoder.encode("member_name", "UTF-8") + "=" + URLEncoder.encode(Name, "UTF-8");
                    data += "&" + URLEncoder.encode("member_email", "UTF-8") + "=" + URLEncoder.encode(Email, "UTF-8");

                    URL url = new URL(link);
                    URLConnection conn = url.openConnection();

                    conn.setDoOutput(true);
                    OutputStreamWriter wr = new OutputStreamWriter(conn.getOutputStream());

                    wr.write(data);
                    wr.flush();

                    BufferedReader reader = new BufferedReader(new InputStreamReader(conn.getInputStream()));

                    StringBuilder sb = new StringBuilder();
                    String line = null;

                    // Read Server Response
                    while ((line = reader.readLine()) != null) {
                        sb.append(line);
                        break;
                    }
                    return sb.toString();
                } catch (Exception e) {
                    return new String("Exception: " + e.getMessage());
                }
            }
        }
        InsertData task = new InsertData();
        task.execute(Id,Pw,Name,Email);
    }
}


