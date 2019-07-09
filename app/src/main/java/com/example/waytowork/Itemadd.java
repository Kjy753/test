package com.example.waytowork;

import android.content.Intent;
import android.location.Address;
import android.location.Geocoder;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
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

import org.w3c.dom.Text;

import java.util.List;

public class Itemadd extends AppCompatActivity implements OnMapReadyCallback {
    //LatLng latLng = new LatLng(37.448864,127.167844);
    GoogleMap map;
    Intent intent;
    Button tos,reset,regist;
    EditText item_detail,end_po;
    TextView start_po;
    View.OnClickListener cl;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.itemadd);
        ((SupportMapFragment)getSupportFragmentManager().findFragmentById(R.id.main_map)).getMapAsync(this);
        tos = (Button) findViewById(R.id.tos);
        reset = (Button) findViewById(R.id.reset);
        regist = (Button) findViewById(R.id.regist);

        start_po = (TextView) findViewById(R.id.start_po);
        end_po = (EditText) findViewById(R.id.end_po);
        item_detail = (EditText) findViewById(R.id.item_detail);

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
                        intent = new Intent(Itemadd.this,Map.class);
                        startActivity(intent);
                        intent = getIntent();
                        String start = intent.getStringExtra("start_po");
                        start_po.setText(start);
                }
            }
        };
        start_po.setOnClickListener(cl);
        tos.setOnClickListener(cl);
        reset.setOnClickListener(cl);
        regist.setOnClickListener(cl);
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        map = googleMap;
        if(map != null){
            LatLng latLng=new LatLng(37.566643, 126.978279);
            CameraPosition position=new CameraPosition.Builder()
                    .target(latLng).zoom(16f).build();
            map.moveCamera(CameraUpdateFactory.newCameraPosition(position));

            MarkerOptions markerOptions=new MarkerOptions();
            markerOptions.icon(BitmapDescriptorFactory.fromResource(R.drawable.ic_marker));
            markerOptions.position(latLng);
            markerOptions.title("서울시청");
            markerOptions.snippet("Tel:01-120");

            map.addMarker(markerOptions);

            MyGeocodingThread thread=new MyGeocodingThread(latLng);
            thread.start();

        }
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

    Handler handler=new Handler(){
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what){
                case 100:
                    Toast toast=Toast.makeText(Itemadd.this, (String)msg.obj, Toast.LENGTH_SHORT);
                    toast.show();
                    break;

            }
        }
    };
}
