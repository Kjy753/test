package com.example.waytowork;

import android.content.Intent;
import android.location.Address;
import android.location.Geocoder;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.URL;
import java.net.URLConnection;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.List;

public class Itemadd extends AppCompatActivity implements OnMapReadyCallback {
    Spinner spinner;
    ArrayList<String> arrayList;
    ArrayAdapter<String> arrayAdapter;

    GoogleMap map;
    Intent intent;
    Button tos,reset,regist;
    EditText item_detail;
    TextView start_po,end_po;
    View.OnClickListener cl;
    String kat = null;
    String getTos = null;

    String Id ;
    String Item_Kat ;
    String  Start_po ;
    String  End_po ;
    String content;
    String Tos ;
    String Regdt ;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.itemadd);

        tos = (Button) findViewById(R.id.tos);
        reset = (Button) findViewById(R.id.reset);
        regist = (Button) findViewById(R.id.regist);

        start_po = (TextView) findViewById(R.id.start_po);
        end_po = (TextView) findViewById(R.id.end_po);
        item_detail = (EditText) findViewById(R.id.item_detail);



        ((SupportMapFragment)getSupportFragmentManager().findFragmentById(R.id.main_map)).getMapAsync(this);

         arrayList = new ArrayList<>();
        arrayList.add("분류");
        arrayList.add("문자");
        arrayList.add("음식");

        arrayAdapter = new ArrayAdapter<>(getApplicationContext(),android.R.layout.simple_spinner_dropdown_item,arrayList);
        spinner = (Spinner) findViewById(R.id.spinner);
        spinner.setAdapter(arrayAdapter);
        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                kat = arrayList.get(i);
                Toast.makeText(getApplicationContext(),kat+"가 선택되었습니다.",Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {
                Toast.makeText(getApplicationContext(),"분류를 선택해주세요.",Toast.LENGTH_SHORT).show();
            }
        });


        cl = new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                switch (view.getId()){
                    case R.id.tos:
                        intent = new Intent(Itemadd.this, Clause_Add.class);
                        startActivityForResult(intent,1);

                        break;
                    case R.id.reset:
                        // 스피너 초기화
                        arrayList.set(0,"분류");
                        start_po.setText("");
                        end_po.setText("");
                        item_detail.setText("");
                        break;
                    case R.id.regist:
                         Id = "a1";// 쉐어드나 파싱으로 아이디 값 들고와야함..
                         Item_Kat = kat ;

                         Start_po = start_po.getText().toString();
                         End_po = end_po.getText().toString();
                         content = item_detail.getText().toString();
                         Tos =getTos;
                         Regdt = "";
                        insertitem(Id,Item_Kat,Start_po,End_po,content,Tos,Regdt);

                        Intent intent = new Intent(getApplicationContext(),MainActivity.class);
                        startActivity(intent);
                        finish();
                        break;
                    case R.id.start_po:
                        Toast toast = Toast.makeText(getApplicationContext(),"클릭",Toast.LENGTH_SHORT);
                        toast.show();

                            map.setOnMapClickListener(new GoogleMap.OnMapClickListener() {
                                @Override
                                public void onMapClick(LatLng latLng) {
                                    MarkerOptions markerOptions = new MarkerOptions();
                                    markerOptions.icon(BitmapDescriptorFactory.fromResource(R.drawable.ic_start));
                                    markerOptions.position(latLng);
                                        map.addMarker(markerOptions);
                                    MyGeocodingThread thread=new MyGeocodingThread(latLng);
                                    thread.start();

                                    map.setOnMapClickListener(null);
                                }
                            });
                        break;
                    case R.id.end_po:
                        Toast toast1 = Toast.makeText(getApplicationContext(),"클릭",Toast.LENGTH_SHORT);
                        toast1.show();

                        map.setOnMapClickListener(new GoogleMap.OnMapClickListener() {
                            @Override
                            public void onMapClick(LatLng latLng) {
                                MarkerOptions markerOptions = new MarkerOptions();
                                markerOptions.icon(BitmapDescriptorFactory.fromResource(R.drawable.ic_marker));
                                markerOptions.position(latLng);
                                map.addMarker(markerOptions);
                                MyGeocodingThread2 thread=new MyGeocodingThread2(latLng);
                                thread.start();
                                map.setOnMapClickListener(null);
                            }
                        });
                        break;

                }
            }
        };
        start_po.setOnClickListener(cl);
        end_po.setOnClickListener(cl);
        tos.setOnClickListener(cl);
        reset.setOnClickListener(cl);
        regist.setOnClickListener(cl);


    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if(requestCode == 1){
            if(resultCode==RESULT_OK){
                //데이터 받기
                String result = data.getStringExtra("Tos");
                getTos = result;
            }
        }
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        map = googleMap;

            LatLng latLng = new LatLng(37.448864,127.167844);
            CameraPosition position=new CameraPosition.Builder()
                    .target(latLng).zoom(16f).build();
            map.moveCamera(CameraUpdateFactory.newCameraPosition(position));
    }

    class MyGeocodingThread extends Thread {
        LatLng latLng;
        public MyGeocodingThread(LatLng latLng){
            this.latLng=latLng;
        }

        @Override
        public void run() {
            Geocoder geocoder=new Geocoder(Itemadd.this);
            List<Address> addresses=null;
            String addressText="";
            try{
                addresses=geocoder.getFromLocation(latLng.latitude, latLng.longitude, 1);
                Thread.sleep(500);
                if(addresses != null && addresses.size()>0){
                    Address address=addresses.get(0);
                    addressText=address.getAdminArea()+" "+(address.getMaxAddressLineIndex()>0 ?
                            address.getAddressLine(0) : address.getLocality())+" ";
                    String txt=address.getSubLocality();
                    if(txt != null)
                        addressText += txt+" ";
                    addressText += address.getThoroughfare()+ " "+address.getSubThoroughfare();

                    Message msg=new Message();
                    msg.what=100;
                    msg.obj=addressText;
                    handler.sendMessage(msg);
                }
            }catch (Exception e){
                e.printStackTrace();
            }
        }
    }

    class MyGeocodingThread2 extends Thread {
        LatLng latLng;
        public MyGeocodingThread2(LatLng latLng){
            this.latLng=latLng;
        }

        @Override
        public void run() {
            Geocoder geocoder=new Geocoder(Itemadd.this);
            List<Address> addresses=null;
            String addressText="";
            try{
                addresses=geocoder.getFromLocation(latLng.latitude, latLng.longitude, 1);
                Thread.sleep(500);
                if(addresses != null && addresses.size()>0){
                    Address address=addresses.get(0);
                    addressText=address.getAdminArea()+" "+(address.getMaxAddressLineIndex()>0 ?
                            address.getAddressLine(0) : address.getLocality())+" ";
                    String txt=address.getSubLocality();
                    if(txt != null)
                        addressText += txt+" ";
                    addressText += address.getThoroughfare()+ " "+address.getSubThoroughfare();

                    Message msg=new Message();
                    msg.what=200;
                    msg.obj=addressText;
                    handler.sendMessage(msg);


                }
            }catch (Exception e){
                e.printStackTrace();
            }
        }
    }


    Handler handler=new Handler(){
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what){
                case 100:
                    start_po.setText((String)msg.obj);
                    start_po.setEnabled(false);
                    /*Toast toast=Toast.makeText(Itemadd.this, (String)msg.obj, Toast.LENGTH_SHORT);
                    toast.show();*/
                    break;
                case 200:
                    end_po.setText((String)msg.obj);
                    end_po.setEnabled(false);
                    break;


            }
        }
    };

    private void insertitem(String Id, String Item_Kat, String Start_po, String End_po,String content,String Tos, String Regdt) {
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
                    String Item_Kat = (String) params[1];
                    String Start_po = (String) params[2];
                    String End_po = (String) params[3];
                    String content = (String) params[4];
                    String Tos = (String) params[5];
                    String Regdt = (String) params[6];

                    String link = "http://shingu.freehost.kr/3_project/22_item_insert.php";
                    String data = URLEncoder.encode("member_id", "UTF-8") + "=" + URLEncoder.encode(Id, "UTF-8");
                    data += "&" + URLEncoder.encode("item_kat", "UTF-8") + "=" + URLEncoder.encode(Item_Kat, "UTF-8");
                    data += "&" + URLEncoder.encode("start_po", "UTF-8") + "=" + URLEncoder.encode(Start_po.trim(), "UTF-8");
                    data += "&" + URLEncoder.encode("end_po", "UTF-8") + "=" + URLEncoder.encode(End_po.trim(), "UTF-8");
                    data += "&" + URLEncoder.encode("content", "UTF-8") + "=" + URLEncoder.encode(content, "UTF-8");
                    data += "&" + URLEncoder.encode("tos", "UTF-8") + "=" + URLEncoder.encode(Tos, "UTF-8");
                    data += "&" + URLEncoder.encode("regdt","UTF-8")+ "=" + URLEncoder.encode(Regdt,"UTF-8");

                    // 포인트,물품등록상태,는 아직 안넣음.



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
        task.execute(Id,Item_Kat,Start_po,End_po,content,Tos,Regdt);
    }
}