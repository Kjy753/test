package com.example.waytowork;

import android.content.ClipData;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

public class Clause_Add extends AppCompatActivity {
    CheckBox option1, option2;
    Button check;
    View.OnClickListener cl;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.clause_add);
        option1 = (CheckBox) findViewById(R.id.checkBox1);
        option2 = (CheckBox) findViewById(R.id.checkBox2);
        check = (Button) findViewById(R.id.check);

        cl = new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(option1.isChecked()){
                    Intent intent = new Intent(getApplicationContext(),Itemadd.class);
                    intent.putExtra("tos","1");
                    startActivity(intent);

                }else{
                    Toast toast = Toast.makeText(getApplicationContext(),"동의가 필요합니다",Toast.LENGTH_SHORT);
                    toast.show();
                }
            }
        };
        check.setOnClickListener(cl);


    }
}
