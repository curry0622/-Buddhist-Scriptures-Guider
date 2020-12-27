package com.example.medical_final_project;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.opengl.Visibility;
import android.os.Bundle;
import android.speech.RecognitionListener;
import android.speech.RecognizerIntent;
import android.speech.SpeechRecognizer;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;

public class MainMenu extends AppCompatActivity {

    private Button nameRecBtn, contentRecBtn, searchBtn, enterBtn;
    private TextView nameResTxt, contentRecTxt, searchResTxt;
    private SpeechRecognizer recognizer;

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
                checkPermission();
                startListening();
                recognize("name");
            }
        });

        contentRecBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                checkPermission();
                startListening();
                recognize("content");
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

    public void checkPermission() {
        int recordPermission = ActivityCompat.checkSelfPermission(this, Manifest.permission.RECORD_AUDIO);
        if(recordPermission != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(MainMenu.this, new String[]{Manifest.permission.RECORD_AUDIO}, 1);
        }
    }

    public void startListening() {
        Intent intent = new Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH);
        intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE_MODEL, RecognizerIntent.LANGUAGE_MODEL_FREE_FORM);
        recognizer = SpeechRecognizer.createSpeechRecognizer(this);
        recognizer.startListening(intent);
    }

    public void recognize(String type) {
        recognizer.setRecognitionListener(new RecognitionListener() {
            @Override
            public void onReadyForSpeech(Bundle bundle) {

            }

            @Override
            public void onBeginningOfSpeech() {

            }

            @Override
            public void onRmsChanged(float v) {

            }

            @Override
            public void onBufferReceived(byte[] bytes) {

            }

            @Override
            public void onEndOfSpeech() {

            }

            @Override
            public void onError(int i) {
                Toast.makeText(MainMenu.this, "辨識失敗，請檢查網路連線後再試一次", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onResults(Bundle results) {
                ArrayList resList = results.getStringArrayList(SpeechRecognizer.RESULTS_RECOGNITION);
                String result = resList.get(0).toString();
                if(type.equals("name")) {
                    nameResTxt.setText(result);
                } else {
                    contentRecTxt.setText(result);
                }
            }

            @Override
            public void onPartialResults(Bundle bundle) {

            }

            @Override
            public void onEvent(int i, Bundle bundle) {

            }
        });
    }
}