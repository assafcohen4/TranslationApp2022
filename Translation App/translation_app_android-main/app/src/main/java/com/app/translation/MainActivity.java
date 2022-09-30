package com.app.translation;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.animation.AnimationUtils;
import android.widget.Toast;

import com.app.translation.API.YandexTranslate;
import com.kaopiz.kprogresshud.KProgressHUD;

import java.util.concurrent.ExecutionException;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        YandexTranslate yandexTranslate = new YandexTranslate();
        try {
            String result = yandexTranslate.execute("the text to be translated", "en", "de").get();
        } catch (ExecutionException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

}