package com.example.medical_final_project;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.opengl.Visibility;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

public class MainMenu extends AppCompatActivity {

    private Button nameRecBtn, contentRecBtn, searchBtn, enterBtn;
    private TextView nameResTxt, contentRecTxt, searchResTxt;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_menu);

        // my functions
        findObjects();
        initialSetUp();
        btnOnClick();
    }

    public void findObjects() {
        nameRecBtn = findViewById(R.id.nameRecBtn);
        contentRecBtn = findViewById(R.id.contentRecBtn);
        searchBtn = findViewById(R.id.searchBtn);
        enterBtn = findViewById(R.id.enterBtn);
        nameResTxt = findViewById(R.id.nameResTxt);
        contentRecTxt = findViewById(R.id.contentResTxt);
        searchResTxt = findViewById(R.id.searchResTxt);
    }

    public void initialSetUp() {
        enterBtn.setVisibility(View.INVISIBLE);
    }

    public void btnOnClick() {
        nameRecBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

            }
        });

        contentRecBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

            }
        });

        searchBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                enterBtn.setVisibility(View.VISIBLE);
            }
        });

        enterBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent();
                intent.setClass(MainMenu.this, GuideReadingPage.class);
                startActivity(intent);
            }
        });
    }
}