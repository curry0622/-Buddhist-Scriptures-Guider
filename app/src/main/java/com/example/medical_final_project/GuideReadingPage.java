package com.example.medical_final_project;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import android.Manifest;
import android.content.pm.PackageManager;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.os.Handler;
import android.speech.tts.TextToSpeech;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Locale;
import java.util.concurrent.ExecutionException;

public class GuideReadingPage extends AppCompatActivity {

    private String scriptureName, scriptureFileName, vernacularFileName;
    private ArrayList<String> scriptureContent = new ArrayList<>();
    private ArrayList<String> vernacularContent = new ArrayList<>();
    private int index = 0;
    private TextToSpeech chiTTS;

    // Taiwanese synthesis
    private MediaPlayer mediaPlayer;
    private TaiwaneseSynthesis taiwaneseSynthesis;
    private String wav_path;

    private TextView titleTxt, contentTxt, vernacularTxt, pageTxt;
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
        pageTxt = findViewById(R.id.pageTxt);
        prevBtn = findViewById(R.id.prevBtn);
        nextBtn = findViewById(R.id.nextBtn);
        chiSynBtn = findViewById(R.id.chiSynBtn);
        taiSynBtn = findViewById(R.id.taiSynBtn);
        backBtn = findViewById(R.id.backBtn);
    }

    public void initialSetup() {
        titleTxt.setText(scriptureName);
        determineFileName();
        if(!scriptureFileName.equals("failed") && !vernacularFileName.equals("failed")) {
            checkPermission();
            loadContent();
            contentTxt.setText(scriptureContent.get(index));
            vernacularTxt.setText(vernacularContent.get(index));
            setPage();
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
                    contentTxt.setText(scriptureContent.get(index));
                    vernacularTxt.setText(vernacularContent.get(index));
                    setPage();
                }
            }
        });

        nextBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(index < scriptureContent.size() - 1) {
                    index += 1;
                    contentTxt.setText(scriptureContent.get(index));
                    vernacularTxt.setText(vernacularContent.get(index));
                    setPage();
                }
            }
        });

        chiSynBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                chiTTS = initChiTTS();
                talk(scriptureContent.get(index));
            }
        });

        taiSynBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                taiwaneseSynthesis = new TaiwaneseSynthesis();
                try {
                    wav_path = taiwaneseSynthesis.execute(scriptureContent.get(index)).get();
                    mediaPlayer = new MediaPlayer();
                    mediaPlayer.setDataSource(wav_path);
                    mediaPlayer.prepare();
                    mediaPlayer.start();
                    mediaPlayer.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
                        @Override
                        public void onCompletion(MediaPlayer mediaPlayer) {
                            mediaPlayer.release();
                        }
                    });
                } catch (InterruptedException e) {
                    e.printStackTrace();
                    Toast.makeText(GuideReadingPage.this, "InterruptedException", Toast.LENGTH_SHORT).show();
                } catch (ExecutionException e) {
                    e.printStackTrace();
                    Toast.makeText(GuideReadingPage.this, "ExecutionException", Toast.LENGTH_SHORT).show();
                } catch (IOException e) {
                    e.printStackTrace();
                    Toast.makeText(GuideReadingPage.this, "IOException", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    public void checkPermission() {
        int readPermission = ActivityCompat.checkSelfPermission(this, Manifest.permission.READ_EXTERNAL_STORAGE);
        int writePermission = ActivityCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE);
        if(readPermission != PackageManager.PERMISSION_GRANTED ||writePermission != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(GuideReadingPage.this, new String[]{Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.WRITE_EXTERNAL_STORAGE}, 1);
        }
    }

    public void determineFileName() {
        scriptureFileName = "failed";
        vernacularFileName = "failed";
        switch (scriptureName) {
            case "心經":
                scriptureFileName = "o_heart.txt";
                vernacularFileName = "v_heart.txt";
                break;
            case "阿彌陀經":
                scriptureFileName = "o_amito.txt";
                break;
            case "普門品":
                scriptureFileName = "o_pumen.txt";
                break;
            case "金剛經":
                scriptureFileName = "o_kingkong.txt";
                break;
            case "四十二章經":
                scriptureFileName = "o_42.txt";
                break;
            case "圓覺經":
                scriptureFileName = "o_circle.txt";
                break;
        }
    }

    public void loadContent() {
        // read scripture file
        try {
            BufferedReader reader = new BufferedReader(new InputStreamReader(getAssets().open(scriptureFileName)));
            String lineText = "";
            while((lineText = reader.readLine()) != null) {
                scriptureContent.add(lineText);
            }
        } catch (IOException e) {
            e.printStackTrace();
            Toast.makeText(GuideReadingPage.this, "無法開啟" + scriptureFileName, Toast.LENGTH_SHORT).show();
        }
        // read vernacular file
        try {
            BufferedReader reader = new BufferedReader(new InputStreamReader(getAssets().open(vernacularFileName)));
            String lineText = "";
            while((lineText = reader.readLine()) != null) {
                vernacularContent.add(lineText);
            }
        } catch (IOException e) {
            e.printStackTrace();
            Toast.makeText(GuideReadingPage.this, "無法開啟" + vernacularFileName, Toast.LENGTH_SHORT).show();
        }
    }

    public void setPage() {
        pageTxt.setText(String.valueOf(index + 1) + " / " + String.valueOf(scriptureContent.size()));
    }

    public TextToSpeech initChiTTS() {
        chiTTS = new TextToSpeech(this, new TextToSpeech.OnInitListener() {
            @Override
            public void onInit(int status) {
                if(status == TextToSpeech.SUCCESS) {
                    chiTTS.setPitch((float)1.0);
                    chiTTS.setSpeechRate((float)0.7);
                    Locale locale = Locale.TAIWAN;
                    if(chiTTS.isLanguageAvailable(locale) >= TextToSpeech.LANG_AVAILABLE) {
                        chiTTS.setLanguage(locale);
                    }
                }
            }
        });
        return chiTTS;
    }

    public void talk(String statement) {
        Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                chiTTS.speak(statement, TextToSpeech.QUEUE_FLUSH, null);
                while(chiTTS.isSpeaking()){}
            }
        }, 1000);
    }
}