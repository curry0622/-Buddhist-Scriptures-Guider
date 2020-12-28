package com.example.medical_final_project;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.speech.RecognitionListener;
import android.speech.RecognizerIntent;
import android.speech.SpeechRecognizer;
import android.util.Log;
import android.util.Pair;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;

public class MainMenu extends AppCompatActivity {

    private Button nameRecBtn, contentRecBtn, searchBtn, enterBtn;
    private ImageButton resetBtn;
    private TextView nameResTxt, contentResTxt, searchResTxt;
    private SpeechRecognizer recognizer;

    private String recType = "";
    private String scriptureName = "";
    private int index = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_menu);

        // my functions
        findObjects();
        initialSetup();
        btnOnClick();
    }

    public void findObjects() {
        nameRecBtn = findViewById(R.id.nameRecBtn);
        contentRecBtn = findViewById(R.id.contentRecBtn);
        searchBtn = findViewById(R.id.searchBtn);
        enterBtn = findViewById(R.id.enterBtn);
        resetBtn = findViewById(R.id.resetBtn);
        nameResTxt = findViewById(R.id.nameResTxt);
        contentResTxt = findViewById(R.id.contentResTxt);
        searchResTxt = findViewById(R.id.searchResTxt);
    }

    public void initialSetup() {
        enterBtn.setVisibility(View.INVISIBLE);
    }

    public void btnOnClick() {
        nameRecBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                recType = "name";
                contentRecBtn.setVisibility(View.INVISIBLE);
                nameResTxt.setText("辨識中...");
                checkPermission();
                startListening();
                recognize("name");
            }
        });

        contentRecBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                recType = "content";
                nameRecBtn.setVisibility(View.INVISIBLE);
                contentResTxt.setText("辨識中...");
                checkPermission();
                startListening();
                recognize("content");
            }
        });

        searchBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String strToSearch = "";
                if(recType == "name") {
                    strToSearch = nameResTxt.getText().toString();
                } else if(recType == "content") {
                    strToSearch = contentResTxt.getText().toString();
                }
                searchResTxt.setText("搜尋中...");
                Pair<String, Integer> result = search(strToSearch);
                if(!result.first.equals("None")) {
                    scriptureName = result.first;
                    index = result.second;
                    searchResTxt.setText(result.first + "\n第" + String.valueOf(result.second + 1) + "句");
                    enterBtn.setVisibility(View.VISIBLE);
                } else {
                    searchResTxt.setText("搜尋失敗");
                    Toast.makeText(MainMenu.this, recType, Toast.LENGTH_SHORT).show();
                }
            }
        });

        enterBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent();
                intent.putExtra("scripture name", scriptureName);
                intent.putExtra("index", index);
                intent.setClass(MainMenu.this, GuideReadingPage.class);
                startActivity(intent);
            }
        });

        resetBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                recreate();
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
                if(type.equals("name")) {
                    nameResTxt.setText("辨識失敗");
                } else {
                    contentResTxt.setText("辨識失敗");
                }
                Toast.makeText(MainMenu.this, "辨識失敗，請檢查網路連線後再試一次", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onResults(Bundle results) {
                ArrayList resList = results.getStringArrayList(SpeechRecognizer.RESULTS_RECOGNITION);
                String result = resList.get(0).toString();
                if(type.equals("name")) {
                    nameResTxt.setText(result);
                } else {
                    contentResTxt.setText(result);
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

    public Pair<String, Integer> search(String str) {
        Pair<String, Integer> result = new Pair<>("None", 0);
        if(recType == "name") {
            String scriptureNames[] = {"心經", "阿彌陀經", "普門品", "金剛經", "四十二章經", "圓覺經"};
            for(String scripture: scriptureNames) {
                if(scripture.equals(str)) {
                    result = new Pair<>(str, 0);
                    break;
                }
            }
        } else if(recType == "content") {
            checkPermission();
            Boolean found = false;
            for(int i = 0; i < 6; i++) {
                if(found) {
                    break;
                }
                switch(i) {
                    case 0:
                        try {
                            BufferedReader reader = new BufferedReader(new InputStreamReader(getAssets().open("o_42.txt")));
                            String lineText = "";
                            int index = 0;
                            while((lineText = reader.readLine()) != null) {
                                if(lineText.equals(str)) {
                                    found = true;
                                    result = new Pair<>("四十二章經", index);
                                    break;
                                }
                                index += 1;
                            }
                        } catch (IOException e) {
                            e.printStackTrace();
                            Toast.makeText(MainMenu.this, "IOExecption", Toast.LENGTH_SHORT).show();
                        }
                        break;
                    case 1:
                        try {
                            BufferedReader reader = new BufferedReader(new InputStreamReader(getAssets().open("o_amito.txt")));
                            String lineText = "";
                            int index = 0;
                            while((lineText = reader.readLine()) != null) {
                                if(lineText.equals(str)) {
                                    found = true;
                                    result = new Pair<>("阿彌陀經", index);
                                    break;
                                }
                                index += 1;
                            }
                        } catch (IOException e) {
                            e.printStackTrace();
                            Toast.makeText(MainMenu.this, "IOExecption", Toast.LENGTH_SHORT).show();
                        }
                        break;
                    case 2:
                        try {
                            BufferedReader reader = new BufferedReader(new InputStreamReader(getAssets().open("o_circle.txt")));
                            String lineText = "";
                            int index = 0;
                            while((lineText = reader.readLine()) != null) {
                                if(lineText.equals(str)) {
                                    found = true;
                                    result = new Pair<>("圓覺經", index);
                                    break;
                                }
                                index += 1;
                            }
                        } catch (IOException e) {
                            e.printStackTrace();
                            Toast.makeText(MainMenu.this, "IOExecption", Toast.LENGTH_SHORT).show();
                        }
                        break;
                    case 3:
                        try {
                            BufferedReader reader = new BufferedReader(new InputStreamReader(getAssets().open("o_heart.txt")));
                            String lineText = "";
                            int index = 0;
                            while((lineText = reader.readLine()) != null) {
                                if(lineText.equals(str)) {
                                    found = true;
                                    result = new Pair<>("心經", index);
                                    break;
                                }
                                index += 1;
                            }
                        } catch (IOException e) {
                            e.printStackTrace();
                            Toast.makeText(MainMenu.this, "IOExecption", Toast.LENGTH_SHORT).show();
                        }
                        break;
                    case 4:
                        try {
                            BufferedReader reader = new BufferedReader(new InputStreamReader(getAssets().open("o_kingkong.txt")));
                            String lineText = "";
                            int index = 0;
                            while((lineText = reader.readLine()) != null) {
                                if(lineText.equals(str)) {
                                    found = true;
                                    result = new Pair<>("金剛經", index);
                                    break;
                                }
                                index += 1;
                            }
                        } catch (IOException e) {
                            e.printStackTrace();
                            Toast.makeText(MainMenu.this, "IOExecption", Toast.LENGTH_SHORT).show();
                        }
                        break;
                    case 5:
                        try {
                            BufferedReader reader = new BufferedReader(new InputStreamReader(getAssets().open("o_pumen.txt")));
                            String lineText = "";
                            int index = 0;
                            while((lineText = reader.readLine()) != null) {
                                if(lineText.equals(str)) {
                                    found = true;
                                    result = new Pair<>("普門品", index);
                                    break;
                                }
                                index += 1;
                            }
                        } catch (IOException e) {
                            e.printStackTrace();
                            Toast.makeText(MainMenu.this, "IOExecption", Toast.LENGTH_SHORT).show();
                        }
                        break;
                }
            }
        }
        return result;
    }
}