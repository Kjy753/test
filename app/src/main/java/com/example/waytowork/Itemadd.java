package com.example.waytowork;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.os.AsyncTask;
import android.os.Build;
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

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.FusedLocationProviderApi;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

import org.w3c.dom.Text;

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
    String a = null;

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
        arrayList.add("select");
        arrayList.add("문자");
        arrayList.add("food");

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
                        a = "1";
                        /*intent = new Intent(Itemadd.this, Clause_Add.class);
                        startActivity(intent);
                        finish();*/
                        break;
                    case R.id.reset:
                        // 스피너 초기화
                        arrayList.set(0,"select");
                        start_po.setText("");
                        end_po.setText("");
                        item_detail.setText("");
                        break;
                    case R.id.regist:
                        String Id = "a1";// 쉐어드나 파싱으로 아이디 값 들고와야함..
                        String Item_Kat = kat ;

                        String Start_po = start_po.getText().toString();
                        String End_po = end_po.getText().toString();
                        String Contatint = item_detail.getText().toString();
                        String Tos = a;
                        insertitem(Id,Item_Kat);

                        /*intent = new Intent(Itemadd.this,MainActivity.class);
                        startActivity(intent);
                        finish();*/
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
                    Toast toast=Toast.makeText(Itemadd.this, (String)msg.obj, Toast.LENGTH_SHORT);
                    toast.show();
                    break;
                case 200:
                    end_po.setText((String)msg.obj);
                    end_po.setEnabled(false);
                    break;
                case 300:
                    Toast to=Toast.makeText(Itemadd.this, (String)msg.obj, Toast.LENGTH_SHORT);
                    to.show();
                    break;


            }
        }
    };

    private void insertitem(String Id, String Item_Kat) {
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
               // Toast.makeText(getApplicationContext(), s, Toast.LENGTH_LONG).show();
            }
            @Override
            protected String doInBackground(String... params) {

                try {
                    String Id = (String) params[0];
                    String Item_Kat = (String) params[1];


                    String link = "http://shingu.freehost.kr/3_project/03_member_insert.php";
                    String data = URLEncoder.encode("member_id", "UTF-8") + "=" + URLEncoder.encode(Id, "UTF-8");
                    data += "&" + URLEncoder.encode("item_kat", "UTF-8") + "=" + URLEncoder.encode(Item_Kat, "CP949");

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
        task.execute(Id,Item_Kat);
    }
}
