package com.example.ecommerce;

import androidx.appcompat.app.AppCompatActivity;

import android.Manifest;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.common.api.internal.BaseImplementation;
import com.google.zxing.Result;
import com.karumi.dexter.Dexter;
import com.karumi.dexter.PermissionToken;
import com.karumi.dexter.listener.PermissionDeniedResponse;
import com.karumi.dexter.listener.PermissionGrantedResponse;
import com.karumi.dexter.listener.PermissionRequest;
import com.karumi.dexter.listener.single.PermissionListener;

import me.dm7.barcodescanner.zxing.ZXingScannerView;

public class CameraScannerActivity extends AppCompatActivity implements ZXingScannerView.ResultHandler {
     ZXingScannerView zXingScannerView ;
     public String ProductName ="";
     TextView textView;
     Button DoneBtn ;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_camera_scanner);
        //Step[1]
        zXingScannerView = findViewById(R.id.ZXScan);
        textView = findViewById(R.id.tes);
        DoneBtn = findViewById(R.id.DoneBtn);
        DoneBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent TheIntent  = new Intent(CameraScannerActivity.this , SearchActivity.class);
                TheIntent.putExtra("heba" , ProductName);
                setResult(RESULT_OK,TheIntent);
                finish();
            }
        });
        Dexter.withActivity(this).withPermission(Manifest.permission.CAMERA)
                .withListener(new PermissionListener() {
                    @Override
                    public void onPermissionGranted(PermissionGrantedResponse permissionGrantedResponse) {
                        zXingScannerView.setResultHandler(CameraScannerActivity.this);
                        zXingScannerView.startCamera();
                    }
                    @Override
                    public void onPermissionDenied(PermissionDeniedResponse permissionDeniedResponse) {
                        Toast.makeText(CameraScannerActivity.this, "Please Accept this permission", Toast.LENGTH_SHORT).show();
                    }

                    @Override
                    public void onPermissionRationaleShouldBeShown(PermissionRequest permissionRequest, PermissionToken permissionToken) {

                    }
                }).check();
    }

    @Override
    public void handleResult(Result result) {
        ProductName = result.getText().toString();
        textView.setText(result.getText());
        zXingScannerView.startCamera();
    }

    @Override
    protected void onResume() {
        super.onResume();
        zXingScannerView.setResultHandler(CameraScannerActivity.this);
        zXingScannerView.startCamera();
    }

    @Override
    protected void onDestroy() {
        zXingScannerView.stopCamera();
        super.onDestroy();
    }
}