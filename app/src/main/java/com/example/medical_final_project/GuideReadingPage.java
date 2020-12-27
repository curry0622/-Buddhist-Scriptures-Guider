package com.example.medical_final_project;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

public class GuideReadingPage extends AppCompatActivity {

    private String scriptureName;

    private TextView titleTxt, contentTxt, vernacularTxt;
    private Button prevBtn, nextBtn, chiSynBtn, taiSynBtn, backBtn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_guide_reading_page);

        // get intent extra
        scriptureName = getIntent().getStringExtra("scripture name");

        // my functions
        findObjects();
        initialSetup();
        btnOnClick();
    }

    public void findObjects() {
        titleTxt = findViewById(R.id.titleTxt);
        contentTxt = findViewById(R.id.contentTxt);
        vernacularTxt = findViewById(R.id.vernacularTxt);
        prevBtn = findViewById(R.id.prevBtn);
        nextBtn = findViewById(R.id.nextBtn);
        chiSynBtn = findViewById(R.id.chiSynBtn);
        taiSynBtn = findViewById(R.id.taiSynBtn);
        backBtn = findViewById(R.id.backBtn);
    }

    public void initialSetup() {
        titleTxt.setText(scriptureName);
    }

    public void btnOnClick() {
        backBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
    }
}