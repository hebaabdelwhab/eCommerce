package com.example.ecommerce;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class MainActivity extends AppCompatActivity {
    //Declaration
    private Button joinNowButton , LogInButton;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
      //-----------------------------------------------------------
      joinNowButton = (Button)findViewById(R.id.main_join_now_btn);
      LogInButton   =  (Button)findViewById(R.id.main_login_btn);
      //--------------------------------------------------------------------------------------------
        LogInButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent TheIntent  = new Intent(MainActivity.this,LoginActivity.class);
                startActivity(TheIntent);
            }
        });
        joinNowButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent TheIntent  = new Intent(MainActivity.this,RegisterActivity.class);
                startActivity(TheIntent);
            }
        });

    }
}