package com.example.ecommerce;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.provider.Settings;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.ecommerce.Prevalent.Prevalent;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import org.jetbrains.annotations.NotNull;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;

public class ConfirmFinalOrderActivity extends AppCompatActivity implements LocationListener {

    private  EditText nameEditText , PhoneEditText , AddressEditText , CityEditText;
    private Button ConfirmBtn,GeogleMapBtn;
    private  String TotalPrice ="";
    TextView mapaddress;
    LocationManager locationManager;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_confirm_final_order);
        //Step[0]
        if (ContextCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED
                && ActivityCompat.checkSelfPermission(getApplicationContext(),
                android.Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]{android.Manifest.permission.ACCESS_FINE_LOCATION,
                    android.Manifest.permission.ACCESS_COARSE_LOCATION}, 101);
        }
        //Step[1]
         TotalPrice = getIntent().getStringExtra("Total Price");
         nameEditText = findViewById(R.id.shippment_name);
         PhoneEditText = findViewById(R.id.shippment_PhoneNumber);
         AddressEditText = findViewById(R.id.shippment_Address);
         CityEditText = findViewById(R.id.shippment_City_name);
         ConfirmBtn = findViewById(R.id.Confirm_final_order_btn);
         GeogleMapBtn = findViewById(R.id.GetLocation_btn);
         mapaddress = findViewById(R.id.mapaddress);
         //-----------------------------------------------------
         //Step[2]
         ConfirmBtn.setOnClickListener(new View.OnClickListener() {
             @Override
             public void onClick(View v) {
                 check();
             }
         });
         GeogleMapBtn.setOnClickListener(new View.OnClickListener() {
             @SuppressLint("MissingPermission")
             @Override
             public void onClick(View v) {
                 locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
                 locationEnabled();
                 getLocation();
             }
         });
    }
    private void locationEnabled() {
        LocationManager lm = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
        boolean gps_enabled = false;
        boolean network_enabled = false;
        try {
            gps_enabled = lm.isProviderEnabled(LocationManager.GPS_PROVIDER);
        } catch (Exception e) {
            e.printStackTrace();
        }
        try {
            network_enabled = lm.isProviderEnabled(LocationManager.NETWORK_PROVIDER);
        } catch (Exception e) {
            e.printStackTrace();
        }
        if (!gps_enabled && !network_enabled) {
            new AlertDialog.Builder(ConfirmFinalOrderActivity.this)
                    .setTitle("Enable GPS Service")
                    .setMessage("We need your GPS location to show Near Places around you.")
                    .setCancelable(false)
                    .setPositiveButton("Enable", new
                            DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface paramDialogInterface, int paramInt) {
                                    startActivity(new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS));
                                }
                            })
                    .setNegativeButton("Cancel", null)
                    .show();
        }
    }
    void getLocation() {
        try {
            locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
            locationManager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, 500, 5, (LocationListener) this);
        } catch (SecurityException e) {
            e.printStackTrace();
        }
    }
    private void check() {
         if(TextUtils.isEmpty(nameEditText.getText().toString()))
         {
             Toast.makeText(this, "Please provide your full name .", Toast.LENGTH_SHORT).show();
         }
         else if(TextUtils.isEmpty(PhoneEditText.getText().toString()))
         {
             Toast.makeText(this, "Please provide your Phone number.", Toast.LENGTH_SHORT).show();
         }
         else if(TextUtils.isEmpty(AddressEditText.getText().toString()))
         {
             Toast.makeText(this, "Please provide your Address.", Toast.LENGTH_SHORT).show();
         }
         else if(TextUtils.isEmpty(CityEditText.getText().toString()))
         {
             Toast.makeText(this, "Please provide your City.", Toast.LENGTH_SHORT).show();
         }
         else 
         {
             confirm();
         }

    }
    private void confirm() {
        final String saveCurrentTime , saveCurrentDate;
        Calendar calForDate = Calendar.getInstance();
        SimpleDateFormat currentDate = new SimpleDateFormat("MMM dd, yyyy");
        saveCurrentDate = currentDate.format(calForDate.getTime());
        SimpleDateFormat currentTime = new SimpleDateFormat("HH:mm:ss a");
        saveCurrentTime = currentDate.format(calForDate.getTime());
        final  DatabaseReference cartListRef = FirebaseDatabase
             .getInstance().getReference().child("Orders").child(Prevalent.currentOnlineUser.getPhone());
        HashMap<String,Object> OrderMap = new HashMap<>();
        OrderMap.put("totalAmount",TotalPrice);
        OrderMap.put("name", nameEditText.getText().toString());
        OrderMap.put("phone", PhoneEditText.getText().toString());
        OrderMap.put("address", AddressEditText.getText().toString());
        OrderMap.put("city",CityEditText.getText().toString());
        OrderMap.put("date",saveCurrentDate);
        OrderMap.put("time",saveCurrentTime);
        OrderMap.put("state","notshipped");
        cartListRef.updateChildren(OrderMap).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull @NotNull Task<Void> task) {
                if(task.isSuccessful())
                {
                    FirebaseDatabase.getInstance().getReference().child("cart List").child("User View")
                    .child(Prevalent.currentOnlineUser.getPhone())
                    .removeValue()
                     .addOnCompleteListener(new OnCompleteListener<Void>() {
                         @Override
                         public void onComplete(@NonNull @NotNull Task<Void> task) {
                             if(task.isSuccessful())
                             {
                                 Toast.makeText(ConfirmFinalOrderActivity.this,"Your final order has been has been placed successfully",Toast.LENGTH_SHORT).show();
                                 Intent intent = new Intent(ConfirmFinalOrderActivity.this,HomeActivity.class);
                                 intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK|Intent.FLAG_ACTIVITY_CLEAR_TASK);
                                 startActivity(intent);
                                 finish();
                             }
                         }
                     });

                }
            }
        });

    }
    @Override
    public void onLocationChanged(@NonNull Location location) {
        try {
            Geocoder geocoder = new Geocoder(getApplicationContext(), Locale.getDefault());
            List<Address> addresses = geocoder.getFromLocation(location.getLatitude(), location.getLongitude(), 1);

            //CityEditText.setText(addresses.get(0).getLocality());
            CityEditText.setText(addresses.get(0).getCountryName());
            AddressEditText.setText(addresses.get(0).getAddressLine(0));

        } catch (Exception e) {
        }
    }

    @Override
    public void onStatusChanged(String provider, int status, Bundle extras) {

    }

    @Override
    public void onProviderEnabled(@NonNull String provider) {

    }

    @Override
    public void onProviderDisabled(@NonNull String provider) {

    }
}