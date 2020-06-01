package com.cobitsa.busdriver;

import android.app.Activity;
import android.speech.tts.TextToSpeech;
import android.widget.Toast;
import com.cobitsa.busdriver.MainActivity;

import java.util.Locale;

public class TTS extends Activity implements TextToSpeech.OnInitListener {
    private TextToSpeech tts;

    public TTS() {
        tts = new TextToSpeech(MainActivity.getAppContext(), this);
    }

    @Override
    public void onInit(int status) {
        if (status == TextToSpeech.SUCCESS) {
            // 작업 성공
            int language = tts.setLanguage(Locale.KOREAN);  // 언어 설정
            if (language == TextToSpeech.LANG_MISSING_DATA || language == TextToSpeech.LANG_NOT_SUPPORTED)
                Toast.makeText(this, "지원하지 않는 언어입니다.", Toast.LENGTH_SHORT).show();
        } else {
            Toast.makeText(this, "TTS 작업에 실패하였습니다.", Toast.LENGTH_SHORT).show();
        }
    }


    public void shutdownTTS() {
        if (tts != null) {
            tts.stop();
            tts.shutdown();
        }
    }

    public void speech(String text) {
        tts.speak(text, TextToSpeech.QUEUE_FLUSH, null, null);
    }

}
