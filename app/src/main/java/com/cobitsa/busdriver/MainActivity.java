package com.cobitsa.busdriver;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.pm.ActivityInfo;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.util.JsonReader;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.cobitsa.busdriver.TTS;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.ProtocolException;
import java.net.URL;
import java.util.Timer;
import java.util.TimerTask;

public class MainActivity extends AppCompatActivity {

    private static Context context;
    private ImageView imageView9;

    String busId = "381205";
    String busNum = "752";
    public int flag;
    public int blind;
    Button button;
    Button button2;
    TTS tts;
    private TimerTask mTask;
    private Timer mTimer;

    @SuppressLint("SourceLockedOrientationActivity")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);
        MainActivity.context = getApplicationContext();
        setContentView(R.layout.activity_main2);
        tts = new TTS();

        imageView9 = (ImageView) findViewById(R.id.imageView9);
        imageView9.setVisibility(View.INVISIBLE);

        button = findViewById(R.id.button);
        button.setOnClickListener(new Button.OnClickListener() {
            @Override
            public void onClick(View view) {
                forDELETE();
                setflag(0);
                if(blind == 1) {
                    try {
                        tts.speech(busNum + "번 버스입니다.");
                        Thread.sleep(2500);
                        tts.speech(busNum + "번 버스입니다.");
                        Thread.sleep(2500);
                        tts.speech(busNum + "번 버스입니다.");
                        Thread.sleep(2500);
                        tts.speech(busNum + "번 버스입니다.");
                        Thread.sleep(2500);
                        tts.speech(busNum + "번 버스입니다.");

                        setBlind(0);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            }
        });

        button2 = findViewById(R.id.button2);
        button2.setOnClickListener(new Button.OnClickListener() {
            @Override
            public void onClick(View view) {

                try {
                    tts.speech(busNum+"번 버스입니다.");
                    Thread.sleep(2500);
                    tts.speech(busNum+"번 버스입니다.");
                    Thread.sleep(2500);
                    tts.speech(busNum+"번 버스입니다.");
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }

            }
        });

        mTask = new TimerTask() {
            @Override
            public void run() {
                try {
                    tracing(busId, busNum, flag);
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        };

        mTimer = new Timer();

        mTimer.schedule(mTask, 1000, 3000);

    }


    private void forDELETE() {

        Thread thread = new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    URL url = new URL("https://cobitsa.herokuapp.com/bus/" + busId);
                    HttpURLConnection conn = (HttpURLConnection) url.openConnection();
                    conn.setRequestMethod("DELETE");

                    OutputStreamWriter out = new OutputStreamWriter(conn.getOutputStream());
                    out.write("");
                    out.close();
                    conn.getInputStream();
                } catch (MalformedURLException e) {
                    e.printStackTrace();
                } catch (IOException e) {
                    e.printStackTrace();
                }

            }
        });
        thread.start();
    }

    public void tracing(String busId,String busNum, int flag) throws IOException {
        // final TTS tts = new TTS();
        URL url = new URL("https://cobitsa.herokuapp.com/bus/" + busId);
        HttpURLConnection conn = (HttpURLConnection) url.openConnection();

        try {
            conn.setRequestMethod("GET");
            InputStream responseBody =  conn.getInputStream();
            InputStreamReader responseBodyReader =
                    new InputStreamReader(responseBody, "UTF-8");

            JsonReader jsonReader = new JsonReader(responseBodyReader);
            jsonReader.beginObject();
            while (jsonReader.hasNext()) {
                String key = jsonReader.nextName();
                if (key.equals("willRide")) {
                    Boolean ride = jsonReader.nextBoolean();

                        if (ride == true) {
                            setBlind(1);
                            if(flag == 0) {
                                setflag(1);
                            imageView9.post(new Runnable() {
                                @Override
                                public void run() {
                                    imageView9.setVisibility(View.VISIBLE);
                                }
                            });
                        }
                    }
                        if (ride == false) {
                            imageView9.post(new Runnable() {
                                @Override
                                public void run() {
                                    imageView9.setVisibility(View.INVISIBLE);
                                }
                            });
                        }
                }
                else if (key.equals("willGetOff")) {
                    Boolean getOff = jsonReader.nextBoolean();
                        if (getOff == true) {
                            if(flag == 0) {
                            MediaPlayer mediaPlayer = MediaPlayer.create(getAppContext(), R.raw.buzzer);
                            mediaPlayer.start();

                            setflag(1);
                            Alert();
                        }
                    }
                }
            }
        } catch (ProtocolException e) {
            e.printStackTrace();
        }


    }

    private void Alert() {
        final AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
        builder.setTitle("하차 알림");
        builder.setMessage("시각장애인 승객이 하차합니다.");

        builder.setPositiveButton("확인",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                    }
                });
        Handler mHandler = new Handler(Looper.getMainLooper());
        mHandler.postDelayed(new Runnable() {
            @Override
            public void run() {
                AlertDialog dialog =  builder.show();
                TextView textView = (TextView) dialog.findViewById(android.R.id.message);
                textView.setTextSize(35.0f);
            }
        }, 0);

    }

    public void setflag(int iflag){
        this.flag = iflag;
    }

    public void setBlind(int iBlind){
        this.blind = iBlind;
    }

    public static Context getAppContext() {
        return MainActivity.context;
    }

}
