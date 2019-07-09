package com.example.waytowork;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;

import androidx.appcompat.app.AppCompatActivity;

public class Setting extends AppCompatActivity {

    ImageButton ib;
    Button b,b1,b2,b3,b4;
    View.OnClickListener cl;
    Intent i;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.setting);

        ib = (ImageButton) findViewById(R.id.imageButton);
        b = (Button) findViewById(R.id.button);
        b1 = (Button) findViewById(R.id.button1);
        b2 = (Button) findViewById(R.id.button2);
        b3 = (Button) findViewById(R.id.button3);
        b4 = (Button) findViewById(R.id.button4);

        cl = new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                    switch (v.getId()){
                        case R.id.imageButton:
                            onBackPressed();
                            break;
                        case R.id.button:
                        break;
                        case R.id.button1:
                            i = new Intent(getApplicationContext(),Version.class);
                            startActivity(i);
                            break;
                        case R.id.button2:
                            i = new Intent(getApplicationContext(),Push.class);
                            startActivity(i);
                            break;
                        case R.id.button3:
                            i = new Intent(getApplicationContext(), Setting_Select_Clause.class);
                            startActivity(i);
                            break;
                        case R.id.button4:
                            i = new Intent(getApplicationContext(),Out.class);
                            startActivity(i);
                            break;

                    }

            }
        };
        ib.setOnClickListener(cl);
        b.setOnClickListener(cl);
        b1.setOnClickListener(cl);
        b2.setOnClickListener(cl);
        b3.setOnClickListener(cl);
        b4.setOnClickListener(cl);
    }
}