package com.example.medical_final_project;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import android.Manifest;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;

public class GuideReadingPage extends AppCompatActivity {

    private String scriptureName, fileName;
    private ArrayList<String> content = new ArrayList<>();
    private int index = 0;

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
        determineFileName();
        if(!fileName.equals("failed")) {
            checkPermission();
            loadContent();
            contentTxt.setText(content.get(index));
        } else {
            Toast.makeText(GuideReadingPage.this, "無法開啟佛經內容", Toast.LENGTH_SHORT).show();
            finish();
        }
    }

    public void btnOnClick() {
        backBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

        prevBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(index > 0) {
                    index -= 1;
                    contentTxt.setText(content.get(index));
                }
            }
        });

        nextBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(index < content.size() - 1) {
                    index += 1;
                    contentTxt.setText(content.get(index));
                }
            }
        });
    }

    public void checkPermission() {
        int readPermission = ActivityCompat.checkSelfPermission(this, Manifest.permission.READ_EXTERNAL_STORAGE);
        if(readPermission != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(GuideReadingPage.this, new String[]{Manifest.permission.READ_EXTERNAL_STORAGE}, 1);
        }
    }

    public void determineFileName() {
        fileName = "failed";
        switch (scriptureName) {
            case "心經":
                fileName = "o_heart.txt";
                break;
            case "阿彌陀經":
                fileName = "o_amito.txt";
                break;
            case "普門品":
                fileName = "o_pumen.txt";
                break;
            case "金剛經":
                fileName = "o_kingkong.txt";
                break;
            case "四十二章經":
                fileName = "o_42.txt";
                break;
            case "圓覺經":
                fileName = "o_circle.txt";
                break;
        }
    }

    public void loadContent() {
        try {
            BufferedReader reader = new BufferedReader(new InputStreamReader(getAssets().open(fileName)));
            String lineText = "";
            while ((lineText = reader.readLine()) != null) {
                content.add(lineText);
            }
        } catch (IOException e) {
            e.printStackTrace();
            Toast.makeText(GuideReadingPage.this, "無法開啟" + fileName, Toast.LENGTH_SHORT).show();
        }
    }
}