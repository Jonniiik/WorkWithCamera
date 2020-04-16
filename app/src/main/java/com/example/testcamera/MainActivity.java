package com.example.testcamera;

import androidx.appcompat.app.AppCompatActivity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.example.testcamera.Java.JavaCameraActivity;
import com.example.testcamera.Kotlin.KotlinCameraActivity;

public class MainActivity extends AppCompatActivity {

    Button btnJava;
    Button btnKotlin;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        btnJava = findViewById(R.id.btnJava);
        btnKotlin = findViewById(R.id.btnKotlin);

        btnJava.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, JavaCameraActivity.class);
                startActivity(intent);
            }
        });

        btnKotlin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, KotlinCameraActivity.class);
                startActivity(intent);
            }
        });
    }
}
