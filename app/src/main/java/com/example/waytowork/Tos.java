package com.example.waytowork;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageButton;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

public class Tos extends AppCompatActivity{
    ImageButton ib_Back;
    CheckBox ch_First,ch_Second,ch_Third;
    EditText ed_First,ed_Second,ed_Third;
    Button bt_Ok,bt_Cancel;
    View.OnClickListener cl;
    Intent intent;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.tos);
        ib_Back = (ImageButton) findViewById(R.id.ib_Back);
        ch_First = (CheckBox) findViewById(R.id.ch_First);
        ch_Second = (CheckBox) findViewById(R.id.ch_Second);
        ch_Third = (CheckBox) findViewById(R.id.ch_Third);
        ed_First = (EditText) findViewById(R.id.ed_First);
        ed_Second = (EditText) findViewById(R.id.ed_Second);
        ed_Third = (EditText) findViewById(R.id.ed_Third);
        bt_Ok = (Button) findViewById(R.id.bt_Ok);
        bt_Cancel = (Button) findViewById(R.id.bt_Cancel);
        cl = new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                switch (view.getId()){
                    case R.id.ib_Back:
                        break;
                    case R.id.ch_First:
                        break;
                    case R.id.ch_Second:
                        break;
                    case R.id.ch_Third:
                        break;
                    case R.id.ed_First:
                        intent = new Intent(Tos.this,clause_account1.class);
                        break;
                    case R.id.ed_Second:
                        break;
                    case R.id.ed_Third:
                        break;
                    case R.id.bt_Ok:
                        break;
                    case R.id.bt_Cancel:
                        break;
                }
            }
        };
        ib_Back.setOnClickListener(cl);
        ch_First.setOnClickListener(cl);
        ch_Second.setOnClickListener(cl);
        ch_Third.setOnClickListener(cl);
        ed_First.setOnClickListener(cl);
        ed_Second.setOnClickListener(cl);
        ed_Third.setOnClickListener(cl);
        bt_Ok.setOnClickListener(cl);
        bt_Cancel.setOnClickListener(cl);

    }


}
