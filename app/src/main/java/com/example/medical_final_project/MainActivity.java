package com.example.medical_final_project;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class MainActivity extends AppCompatActivity {

    private Button startBtn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // my functions
        findObjects();
        btnOnClick();
    }

    public void findObjects() {
        startBtn = findViewById(R.id.startBtn);
    }

    public void btnOnClick() {
        startBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent();
                intent.setClass(MainActivity.this, MainMenu.class);
                startActivity(intent);
            }
        });
    }
}