package com.example.android.locationtracker;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.IntentSender;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationManager;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;

import java.io.IOException;
import java.util.List;
import java.util.Locale;

public class MainActivity extends AppCompatActivity {
    Button location_btn;
    TextView long_text, lat_text, country_text, locality_text;
    FusedLocationProviderClient fusedLocationProviderClient;

    public interface onGpsListener {
        void gpsStatus(boolean isGPSEnable);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

//        Assign Variable
        location_btn = findViewById(R.id.location_btn);
        long_text = findViewById(R.id.long_text);
        lat_text = findViewById(R.id.lat_text);
        country_text = findViewById(R.id.country_text);
        locality_text = findViewById(R.id.locality_text);

//        initialise fusedLocationProviderClient
        fusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(this);

        location_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (ActivityCompat.checkSelfPermission(MainActivity.this, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
//                    When permission is granted
                    fusedLocationProviderClient.getLastLocation().addOnCompleteListener(new OnCompleteListener<Location>() {
                        @Override
                        public void onComplete(@NonNull Task<Location> task) {
                            Location location = task.getResult();
                            if (location != null) {
                                try {
//                    Initialise Geocoder
                                    Geocoder geocoder = new Geocoder(MainActivity.this, Locale.getDefault());
//                    Initialise Address List
                                    List<Address> addresses = geocoder.getFromLocation(
                                            location.getLatitude(), location.getLongitude(), 1
                                    );
//                        Set latitude on text View
                                    String lat = "Longitude:";
                                    lat_text.setText(lat + addresses.get(0).getLatitude());
//                        Set longitude on text View
                                    String lon = "Latitude:";
                                    long_text.setText(lon + addresses.get(0).getLongitude());
//                        Set country on text View
                                    String con = "Country:";
                                    country_text.setText(con + addresses.get(0).getCountryName());
//                        Set locality on text View
                                    String loc = "Locality:";
                                    locality_text.setText(loc + addresses.get(0).getLocality());
                                } catch (IOException e) {
                                    e.printStackTrace();
                                }
                            } else {
                                Toast.makeText(MainActivity.this, "Please turn on location.", Toast.LENGTH_SHORT).show();
                            }
                        }
                    });
                } else {
                    ActivityCompat.requestPermissions(MainActivity.this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, 44);
                }
            }
        });
    }
}