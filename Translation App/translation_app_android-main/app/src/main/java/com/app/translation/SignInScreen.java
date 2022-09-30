package com.app.translation;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

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

public class SignInScreen extends AppCompatActivity {
    Button mBtnSignIn,mBtnSignUp;
    EditText mEdtEmail,mEdtPassword;

    KProgressHUD progressHUD;
    MyPref myPref;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signin_screen);

        initialize();


        mBtnSignIn = findViewById(R.id.mBtnSignIn);
        mBtnSignUp = findViewById(R.id.mBtnSignUp);
        mEdtEmail = findViewById(R.id.mEdtEmail);
        mEdtPassword = findViewById(R.id.mEdtPassword);

        mBtnSignIn.setOnClickListener(v -> {
            login();
        });

        mBtnSignUp.setOnClickListener(v -> {
            startActivity(new Intent(SignInScreen.this,SignUpScreen.class));
        });
    }
    private void initialize(){
        progressHUD = KProgressHUD.create(this)
                .setCancellable(false)
                .setAnimationSpeed(2)
                .setDimAmount(0.1f);
        myPref = new MyPref(this);
    }
    void login(){
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

        if(Utils.isNetworkAvailable(this)){

            progressHUD.show();
            ApiInterface api = ApiClient.getClient().create(ApiInterface.class);

            Call<JsonObject> call = api.logIn(mEdtEmail.getText().toString(),mEdtPassword.getText().toString());
            call.enqueue(new Callback<JsonObject>() {
                @Override
                public void onResponse(Call<JsonObject> call, Response<JsonObject> response) {
                    Log.d("LOGIN", String.valueOf(response.body()));

                    if(response.body() != null){
                        Toast.makeText(SignInScreen.this,response.body().get("message").getAsString(),Toast.LENGTH_SHORT).show();
                        if(response.body().get("success").getAsBoolean()){
                            User userAccount = new Gson().fromJson(response.body().get("user").getAsJsonObject(), User.class);
                            myPref.setUser(new Gson().toJson(userAccount));
                            finish();
                            startActivity(new Intent(SignInScreen.this, TranslationScreen.class));
                        }
                    }else{
                        Toast.makeText(SignInScreen.this,"Something went wrong, please try again later.",Toast.LENGTH_SHORT).show();
                    }
                    progressHUD.dismiss();
                }

                @Override
                public void onFailure(Call<JsonObject> call, Throwable t) {
                    Log.d("LOGIN", String.valueOf(t));
                    Toast.makeText(SignInScreen.this,"Something went wrong, please try again later.",Toast.LENGTH_SHORT).show();
                    progressHUD.dismiss();
                }
            });
        }
        else{
            Toast.makeText(SignInScreen.this,"Please Check Internet Connectivity.",Toast.LENGTH_SHORT).show();
        }
    }
}