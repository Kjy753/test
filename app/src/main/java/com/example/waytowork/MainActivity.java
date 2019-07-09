package com.example.waytowork;

import android.content.Intent;
import android.os.Bundle;

import android.text.Editable;
import android.text.TextWatcher;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.navigation.NavigationView;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {

private ArrayList<MainData> arrayList;
private MainAdapter mainAdapter;
private RecyclerView recyclerView;
private LinearLayoutManager linearLayoutManager; //리사이클 뷰에서 사용하는거

    


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

//팝업창 구현부
        Button b1 = (Button) findViewById(R.id.button);
        b1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                    //데이터 담아서 팝업(액티비티) 호출
                    Intent intent = new Intent(MainActivity.this, Pop.class);
                    intent.putExtra("data", "Test Popup");
                    startActivity(intent);
            }
        });


//네비게이션 드로얼,스와이프 구현부
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);//여기있어야 가는김에라고 뜨는데 왜지..?
//        FloatingActionButton fab = findViewById(R.id.fab);  //fab는 오른쪽 아래에 떠있는 버튼 필요없는
//        fab.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
//                        .setAction("Action", null).show();
//            }
//        });
        DrawerLayout drawer = findViewById(R.id.drawer_layout);  //액티비티 메인에 전체레이아웃을 drawerlayout으로 잡아놨음
        NavigationView navigationView = findViewById(R.id.nav_view);  //스와이프 화면 = nav_view
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(   //스와이프되게 버튼이 생기면서 열었다가 닫았다가 를 제어하는 부분인것같음
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();
        navigationView.setNavigationItemSelectedListener(this);


//리사이클러뷰 구현부
        recyclerView = (RecyclerView) findViewById(R.id.rv);
        linearLayoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(linearLayoutManager); //위에만든 매니저를 리사이클 뷰에 해줘라

        arrayList = new ArrayList<>();
        mainAdapter = new MainAdapter(arrayList); // MainAdapter에서 가져와서 arrylist값을 넣어준것
        recyclerView.setAdapter(mainAdapter);//mainAdapter에 담겨져있는걸 recyclerView에가  다시 담는다

//검색기능 구현부
        EditText e1;
        e1 = (EditText) findViewById(R.id.editText);
        e1.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                filter(s.toString());
            }
        });

        Button btn_add = (Button) findViewById(R.id.btn_add);
        btn_add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                MainData mainData = new MainData(R.mipmap.ic_launcher,"aa","recyclerView");
                MainData mainData2 = new MainData(R.mipmap.ic_launcher,"bb","recyclerView");
                MainData mainData3 = new MainData(R.mipmap.ic_launcher,"cc","recyclerView");
                MainData mainData4 = new MainData(R.mipmap.ic_launcher,"dd","recyclerView");
                arrayList.add(mainData);
                arrayList.add(mainData2);
                arrayList.add(mainData3);
                arrayList.add(mainData4);
                mainAdapter.notifyDataSetChanged();//추가 삭제 등등 행동을 하면 꼭 노티파이를써서 새로고침!
            }
            });

    }

//검색기능 구현부
    private void filter(String text) {
        ArrayList<MainData> filteredList = new ArrayList<>();

        for (MainData item : arrayList) {
            if (item.getTv_name().toLowerCase().contains(text.toLowerCase())) {
                filteredList.add(item);
            }
        }

        mainAdapter.filterList(filteredList);
    }


    //메인엑티비티에 드로우 레이아웃을 펼치고 접고를 처리하는곳
    @Override
    public void onBackPressed() {
        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

// 이 부분은 기본으로 만들어 질때 오른쪽 구석에 있는 또다른 메뉴
//    @Override
//    public boolean onCreateOptionsMenu(Menu menu) {          d
//        // Inflate the menu; this adds items to the action bar if it is present.
//        getMenuInflater().inflate(R.menu.main, menu);
//        return true;
//    }


    //여기부분은 뭐인지 잘몰라서 주석 처리했더니 오류사라짐
//    @Override
//    public boolean onOptionsItemSelected(MenuItem item) {
//         Handle action bar item clicks here. The action bar will
//         automatically handle clicks on the Home/Up button, so long
//         as you specify a parent activity in AndroidManifest.xml.
//        int id = item.getItemId();
//
//        noinspection SimplifiableIfStatement
//        if (id == R.id.action_settings) {
//            return true;
//        }
//
//        return super.onOptionsItemSelected(item);
//    }


//스와이프화면 메뉴들
    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();
        Intent i;
        if (id == R.id.profile) {
            //i = new Intent(getApplicationContext(), Profile.class);
           // startActivity(i);
        } else if (id == R.id.itemadd) {
            i = new Intent(MainActivity.this,Itemadd.class);
            startActivity(i);
        } else if (id == R.id.pointshop) {
            i = new Intent(getApplicationContext(), PointShop.class);
            startActivity(i);
        } else if (id == R.id.setting) {
            i = new Intent(getApplicationContext(), Setting.class);
            startActivity(i);

        } else if (id == R.id.nav_share) {

        } else if (id == R.id.nav_send) {

        }

        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

}


