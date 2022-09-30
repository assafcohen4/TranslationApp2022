package com.app.translation;


import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.app.translation.API.ApiClient;
import com.app.translation.API.ApiInterface;
import com.app.translation.Constant.Utils;
import com.app.translation.Database.MyPref;
import com.app.translation.Model.User;
import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.kaopiz.kprogresshud.KProgressHUD;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class SignUpScreen extends AppCompatActivity {
    Button mBtnSignUp;
    ImageView mBtnBack;
    EditText mEdtEmail,mEdtPassword,mEdtCnfPassword;

    KProgressHUD progressHUD;
    MyPref myPref;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up_screen);
        initialize();
        mBtnBack = findViewById(R.id.mBtnBack);
        mBtnSignUp = findViewById(R.id.mBtnSignUp);
        mEdtEmail = findViewById(R.id.mEdtEmail);
        mEdtPassword = findViewById(R.id.mEdtPassword);
        mEdtCnfPassword = findViewById(R.id.mEdtCnfPassword);

        mBtnBack.setOnClickListener(v -> {
            onBackPressed();
        });
        mBtnSignUp.setOnClickListener(v -> {
            signUp();
        });
    }
    private void initialize(){
        progressHUD = KProgressHUD.create(this)
                .setCancellable(false)
                .setAnimationSpeed(2)
                .setDimAmount(0.1f);
        myPref = new MyPref(this);
    }
    void signUp(){
        if(mEdtEmail.getText().length() == 0 ){
            Toast.makeText(this,"Please enter email address.",Toast.LENGTH_SHORT).show();
            return;
        }
        if(mEdtPassword.getText().length() == 0){
            Toast.makeText(this,"Please enter password.",Toast.LENGTH_SHORT).show();
            return;
        }
        if(!mEdtEmail.getText().toString().trim().matches("[a-zA-Z0-9._-]+@[a-z]+\\.+[a-z]+")){
            Toast.makeText(this,"Please enter a valid email address.",Toast.LENGTH_SHORT).show();
            return;
        }

        if(!mEdtPassword.getText().toString().equals(mEdtCnfPassword.getText().toString())){
            Toast.makeText(this,"Password and confirm password must be same.",Toast.LENGTH_SHORT).show();
            return;
        }

        if(Utils.isNetworkAvailable(this)) {

            progressHUD.show();

            ApiInterface api = ApiClient.getClient().create(ApiInterface.class);

            Call<JsonObject> call = api.signUp(mEdtEmail.getText().toString(), mEdtPassword.getText().toString());
            call.enqueue(new Callback<JsonObject>() {
                @Override
                public void onResponse(Call<JsonObject> call, Response<JsonObject> response) {
                    Log.d("SIGNUP", String.valueOf(response.body()));

                    if (response.body() != null) {
                        Toast.makeText(SignUpScreen.this, response.body().get("message").getAsString(), Toast.LENGTH_SHORT).show();
                        if (response.body().get("success").getAsBoolean()) {
                            User userAccount = new Gson().fromJson(response.body().get("user").getAsJsonObject(), User.class);
                            myPref.setUser(new Gson().toJson(userAccount));
                            finish();
                            startActivity(new Intent(SignUpScreen.this, TranslationScreen.class));
                        }
                    } else {
                        Toast.makeText(SignUpScreen.this, "Something went wrong, please try again later.", Toast.LENGTH_SHORT).show();
                    }
                    progressHUD.dismiss();
                }

                @Override
                public void onFailure(Call<JsonObject> call, Throwable t) {
                    Log.d("SIGNUP", String.valueOf(t));
                    Toast.makeText(SignUpScreen.this, "Something went wrong, please try again later.", Toast.LENGTH_SHORT).show();
                    progressHUD.dismiss();
                }
            });
        }
        else{
            Toast.makeText(SignUpScreen.this,"Please Check Internet Connectivity.",Toast.LENGTH_SHORT).show();

        }
    }


}