package com.app.translation;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.app.translation.API.ApiClient;
import com.app.translation.API.ApiInterface;
import com.app.translation.Constant.Utils;
import com.app.translation.Database.DatabaseHandler;
import com.app.translation.Database.MyPref;
import com.app.translation.Model.User;
import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.kaopiz.kprogresshud.KProgressHUD;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class TranslationScreen extends AppCompatActivity {

    private Spinner fromLanguage, toLanguage;
    List<String> languageList;
    ArrayAdapter<String> dataAdapter;

    KProgressHUD progressHUD;
    MyPref myPref;
    EditText mEdtTranslate;
    TextView mTxtTranslatedText,mTxtGoToSavedWord;
    Button mBtnTranslate,mBtnSave;
    ImageButton mButtonQuit;
    Boolean isTranslate = false;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_translation_screen);
        initData();
        setUpUIViews();

        mBtnTranslate.setOnClickListener(v -> {
//            onBackPressed();
            translatingAPI();
        });
        mBtnSave.setOnClickListener(v -> {
            saveTranslateWord();
        });
        mButtonQuit.setOnClickListener(view ->{

            AlertDialog.Builder builder = new AlertDialog.Builder(this);

            builder.setMessage("Are you sure you want to log out?")
                    .setCancelable(false)
                    .setPositiveButton("Yes", (dialog, id) -> {
                        dialog.cancel();
                        myPref.remove();
                        new DatabaseHandler(this).deleteTables();
                        startActivity(new Intent(TranslationScreen.this,SignInScreen.class).addFlags(
                                Intent.FLAG_ACTIVITY_CLEAR_TASK|Intent.FLAG_ACTIVITY_NEW_TASK));

                    })
                    .setNegativeButton("No", (dialog, id) -> {
                        dialog.cancel();
                    });

            AlertDialog alert = builder.create();
            alert.setTitle("Log out !!!");
            alert.show();


        });
        mTxtGoToSavedWord.setOnClickListener(v -> {
            Intent goToSavedWordScreen = new Intent(this,TranslationList.class);
            startActivity(goToSavedWordScreen);
        });
        mEdtTranslate.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) { }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                isTranslate = false;
            }

            @Override
            public void afterTextChanged(Editable editable) { }
        });
    }

    private void initData() {
        progressHUD = KProgressHUD.create(this)
                .setCancellable(false)
                .setAnimationSpeed(2)
                .setDimAmount(0.1f);
        myPref = new MyPref(this);

        languageList = new ArrayList<String>();
        languageList.add("Select language");
        languageList.addAll(Utils.getLanguageCodeList());
//        languageList.add("Arabic");
//        languageList.add("Bengali");
//        languageList.add("Chinese");
//        languageList.add("English");
//        languageList.add("French");
//        languageList.add("Gujarati");
//        languageList.add("Spanish");
    }

    private void setUpUIViews() {
        fromLanguage = findViewById(R.id.mSpinnerSource);
        toLanguage = findViewById(R.id.mSpinnerDestination);
        mEdtTranslate = findViewById(R.id.mEdtTranslate);
        mTxtTranslatedText = findViewById(R.id.mTxtTranslatedText);
        mTxtGoToSavedWord = findViewById(R.id.mTxtGoToSavedWord);
        mBtnTranslate = findViewById(R.id.mBtnTranslate);
        mBtnSave = findViewById(R.id.mBtnSave);
        mButtonQuit = findViewById(R.id.mButtonQuit);
        setUpAdapter();
    }

    private void setUpAdapter() {
        dataAdapter= new ArrayAdapter<String>(this,
                android.R.layout.simple_spinner_dropdown_item, languageList);
        dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        fromLanguage.setAdapter(dataAdapter);
        toLanguage.setAdapter(dataAdapter);
    }

    void saveTranslateWord(){
        if(mEdtTranslate.getText().length() == 0 ){
            Toast.makeText(this,"Please enter word.",Toast.LENGTH_SHORT).show();
            return;
        }


        if(fromLanguage.getSelectedItemPosition() == 0 ||
                toLanguage.getSelectedItemPosition() == 0){
            Toast.makeText(this,"Please select languages.",Toast.LENGTH_SHORT).show();
            return;
        }
        if(mTxtTranslatedText.getText().toString().trim().isEmpty() || isTranslate == false){
            Toast.makeText(this,"Please translate the word",Toast.LENGTH_SHORT).show();
            return;
        }

        if(Utils.isNetworkAvailable(this)){
            progressHUD.show();
            ApiInterface api = ApiClient.getClient().create(ApiInterface.class);
            User loginUser = new Gson().fromJson(myPref.getUser(), User.class);

            Call<JsonObject> call = api.saveTranslation(loginUser.getID(),fromLanguage.getSelectedItem().toString(),mEdtTranslate.getText().toString(),toLanguage.getSelectedItem().toString(),mTxtTranslatedText.getText().toString());
            call.enqueue(new Callback<JsonObject>() {
                @Override
                public void onResponse(Call<JsonObject> call, Response<JsonObject> response) {
                    Log.d("SAVE_TRANSLATION", String.valueOf(response.body()));

                    if(response.body() != null){
                        Toast.makeText(TranslationScreen.this,response.body().get("message").getAsString(),Toast.LENGTH_SHORT).show();
//                    if(response.body().get("success").getAsBoolean()){
//
//                    }
                    }else{
                        Toast.makeText(TranslationScreen.this,"Something went wrong, please try again later.",Toast.LENGTH_SHORT).show();
                    }
                    progressHUD.dismiss();
                }

                @Override
                public void onFailure(Call<JsonObject> call, Throwable t) {
                    Log.d("SAVE_TRANSLATION", String.valueOf(t));
                    Toast.makeText(TranslationScreen.this,"Something went wrong, please try again later.",Toast.LENGTH_SHORT).show();
                    progressHUD.dismiss();
                }
            });
        }
        else{
            Toast.makeText(TranslationScreen.this,"Please Check Internet Connectivity.",Toast.LENGTH_SHORT).show();

        }
    }

    void translatingAPI(){
        if(mEdtTranslate.getText().length() == 0 ){
            Toast.makeText(this,"Please enter word.",Toast.LENGTH_SHORT).show();
            return;
        }


        if(fromLanguage.getSelectedItemPosition() == 0 ||
                toLanguage.getSelectedItemPosition() == 0){
            Toast.makeText(this,"Please select languages.",Toast.LENGTH_SHORT).show();
            return;
        }
        if(Utils.isNetworkAvailable(this)){
            progressHUD.show();

            Retrofit retrofit = new Retrofit.Builder()
                    .baseUrl("https://api-free.deepl.com/v2/")
                    .addConverterFactory(GsonConverterFactory.create())
                    .build();

                String fromLanguageCode = fromLanguage.getSelectedItem().toString();
                String toLanguageCode = toLanguage.getSelectedItem().toString();


            ApiInterface api = retrofit.create(ApiInterface.class);
            Call<JsonObject> call = api.translation("57bb11a0-b8fc-d802-f85f-86a574727b81:fx",
                    fromLanguageCode.substring(fromLanguageCode.length()-2),
                    mEdtTranslate.getText().toString(),
                    toLanguageCode.substring(toLanguageCode.length()-2)
            );

            call.enqueue(new Callback<JsonObject>() {
                @Override
                public void onResponse(Call<JsonObject> call, Response<JsonObject> response) {
                    if(response.body() != null && response.body().get("translations") != null &&
                            !response.body().get("translations")
                                    .getAsJsonArray().isEmpty()
                    ){
//                        Toast.makeText(TranslationScreen.this,response.body().get("message").getAsString(),Toast.LENGTH_SHORT).show();
//                    if(response.body().get("success").getAsBoolean()){
//
//                    }
                       // Log.e("Translated text",response.body().getAsJsonArray("translations").get(0).toString());
                        isTranslate = true;
                        String translatedText = response.body().get("translations")
                                .getAsJsonArray().get(0).getAsJsonObject().get("text").getAsString();
                        mTxtTranslatedText.setText(translatedText);

                    }else{
                        Toast.makeText(TranslationScreen.this,"Something went wrong, please try again later.",Toast.LENGTH_SHORT).show();
                    }
                    progressHUD.dismiss();
                }

                @Override
                public void onFailure(Call<JsonObject> call, Throwable t) {
                    Log.d("SAVE_TRANSLATION", String.valueOf(t));
                    Toast.makeText(TranslationScreen.this,"Something went wrong, please try again later.",Toast.LENGTH_SHORT).show();
                    progressHUD.dismiss();
                }
            });
        }
        else{
            Toast.makeText(TranslationScreen.this,"Please Check Internet Connectivity.",Toast.LENGTH_SHORT).show();

        }
    }

}