package com.example.waytowork;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
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
        arrayList.add("문서");
        arrayList.add("음식");

        arrayAdapter = new ArrayAdapter<>(getApplicationContext(),android.R.layout.simple_spinner_dropdown_item,arrayList);
        spinner = (Spinner) findViewById(R.id.spinner);
        spinner.setAdapter(arrayAdapter);
        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                Toast.makeText(getApplicationContext(),arrayList.get(i)+"가 선택되었습니다.",Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });


        cl = new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                switch (view.getId()){
                    case R.id.tos:
                        intent = new Intent(Itemadd.this, Clause_Add.class);
                        startActivity(intent);
                        finish();
                        break;
                    case R.id.reset:
                        // 스피너 초기화
                        start_po.setText("");
                        end_po.setText("");
                        item_detail.setText("");
                        break;
                    case R.id.regist:
                        intent = new Intent(Itemadd.this,MainActivity.class);
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


            }
        }
    };

}
