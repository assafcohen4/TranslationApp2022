package com.app.translation;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.DialogInterface;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.app.translation.API.ApiClient;
import com.app.translation.API.ApiInterface;
import com.app.translation.Adapter.TranslationAdapter;
import com.app.translation.Constant.Utils;
import com.app.translation.Database.DatabaseHandler;
import com.app.translation.Database.MyPref;
import com.app.translation.Model.TranslateWord;
import com.app.translation.Model.User;
import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.google.gson.reflect.TypeToken;
import com.kaopiz.kprogresshud.KProgressHUD;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class TranslationList extends AppCompatActivity {

    RecyclerView translationListRecycler;
    LinearLayoutManager linearLayoutManager;
    LinearLayout mllEmptyMessage;
    List<TranslateWord> translateWordList;
    TranslationAdapter translationAdapter;
    ImageView mImgBack;
    KProgressHUD progressHUD;
    MyPref myPref;
    DatabaseHandler databaseHandler;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_translation_list);
        initData();
        setUpUIViews();
        setData();
        setUpAdapter();

        mImgBack.setOnClickListener(view -> {
            onBackPressed();
        });
    }




    private void initData() {
        translateWordList = new ArrayList<>();
        progressHUD = KProgressHUD.create(this)
                .setCancellable(false)
                .setAnimationSpeed(2)
                .setDimAmount(0.1f);
        myPref = new MyPref(this);
        databaseHandler = new DatabaseHandler(this);
    }

    private void setUpUIViews() {
        translationListRecycler = (RecyclerView) findViewById(R.id.mRecyclerTranslationList);
        mImgBack = findViewById(R.id.mImgBack);
        mllEmptyMessage = findViewById(R.id.mLLEmpty);
        linearLayoutManager=new LinearLayoutManager(this);
        translationListRecycler.setLayoutManager(linearLayoutManager);
        translationListRecycler.setHasFixedSize(true);
        translationListRecycler.addItemDecoration(new DividerItemDecoration(this, DividerItemDecoration.VERTICAL));
        translationAdapter = new TranslationAdapter(translateWordList,this);
        mllEmptyMessage.setVisibility(View.GONE);
    }

    private void setData() {

        getSavedTranslationList();
    }

    private void setUpAdapter() {

        translationListRecycler.setAdapter(translationAdapter);
    }

    public void deleteConfirmation(String translationId, int position){
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage("Are you sure you want to delete this word?")
                .setTitle("Delete !!!");

        builder.setMessage("Are you sure you want to delete this word?")
                .setCancelable(false)
                .setPositiveButton("Yes", (dialog, id) -> {
                    dialog.cancel();
                    deleteTranslation(translationId,position);

                })
                .setNegativeButton("No", (dialog, id) -> {
                    dialog.cancel();
                });

        AlertDialog alert = builder.create();
        alert.setTitle("Delete !!!");
        alert.show();
    }
    public void deleteTranslation(String translationId,int position){
        if(Utils.isNetworkAvailable(this)){
            progressHUD.show();
            ApiInterface api = ApiClient.getClient().create(ApiInterface.class);
            User loginUser = new Gson().fromJson(myPref.getUser(), User.class);

            Call<JsonObject> call = api.deleteTranslation(translationId);
            call.enqueue(new Callback<JsonObject>() {
                @Override
                public void onResponse(Call<JsonObject> call, Response<JsonObject> response) {
                    Log.d("DELETE_TRANSLATION", String.valueOf(response.body()));

                    if(response.body() != null){
                        if(response.body().get("success").getAsBoolean()){
                            databaseHandler.deleteSavedWord(translateWordList.get(position));
                            translateWordList.remove(position);
                            translationAdapter.notifyDataSetChanged();
                            if(translateWordList.isEmpty()){
                                mllEmptyMessage.setVisibility(View.VISIBLE);
                            }
                        }
                        Toast.makeText(TranslationList.this,response.body().get("message").getAsString(),Toast.LENGTH_SHORT).show();
                    }else{
                        Toast.makeText(TranslationList.this,"Something went wrong, please try again later.",Toast.LENGTH_SHORT).show();
                    }
                    progressHUD.dismiss();
                }

                @Override
                public void onFailure(Call<JsonObject> call, Throwable t) {
                    Log.d("DELETE_TRANSLATION", String.valueOf(t));
                    Toast.makeText(TranslationList.this,"Something went wrong, please try again later.",Toast.LENGTH_SHORT).show();
                    progressHUD.dismiss();
                }
            });
        }
        else{
            Toast.makeText(TranslationList.this,"Please Check Internet Connectivity.",Toast.LENGTH_SHORT).show();
        }
    }

    public void saveTranslationLocal(TranslateWord translation,int position){
        databaseHandler.addTranslatedWord(translation);
        translateWordList.get(position).setSaved(databaseHandler.isSavedWord(translateWordList.get(position).getTranslationID()));
        translationAdapter.notifyDataSetChanged();
    }

    public void getSavedTranslationList(){
        if(Utils.isNetworkAvailable(this)){
            progressHUD.show();
            ApiInterface api = ApiClient.getClient().create(ApiInterface.class);
            User loginUser = new Gson().fromJson(myPref.getUser(), User.class);

            Call<JsonObject> call = api.getAllTranslation(loginUser.getID());
            call.enqueue(new Callback<JsonObject>() {
                @Override
                public void onResponse(Call<JsonObject> call, Response<JsonObject> response) {
                    Log.d("GET_ALL_TRANSLATION", String.valueOf(response.body()));

                    if(response.body() != null){
                        if(response.body().get("success").getAsBoolean()){
                            translateWordList.clear();
                            if(!response.body().get("translation_list").getAsJsonArray().isEmpty()){
                                translateWordList.addAll((Collection<? extends TranslateWord>)
                                        new Gson().fromJson(response.body().get("translation_list").getAsJsonArray(), new TypeToken<List<TranslateWord>>(){}.getType())) ;

                                for (int i=0;i<translateWordList.size();i++){
                                    translateWordList.get(i).setSaved(databaseHandler.isSavedWord(translateWordList.get(i).getTranslationID()));
                                }
                                translationAdapter.notifyDataSetChanged();
                            }
                            else{
                               mllEmptyMessage.setVisibility(View.VISIBLE);
                            }
                        }else{
                            Toast.makeText(TranslationList.this,response.body().get("message").getAsString(),Toast.LENGTH_SHORT).show();
                        }
                    }else{
                        Toast.makeText(TranslationList.this,"Something went wrong, please try again later.",Toast.LENGTH_SHORT).show();
                    }
                    progressHUD.dismiss();
                }

                @Override
                public void onFailure(Call<JsonObject> call, Throwable t) {
                    Log.d("GET_ALL_TRANSLATION", String.valueOf(t));
                    Toast.makeText(TranslationList.this,"Something went wrong, please try again later.",Toast.LENGTH_SHORT).show();
                    progressHUD.dismiss();
                }
            });
        }
        else{
            translateWordList.addAll(databaseHandler.getAllSavedWords());

            for (int i=0;i<translateWordList.size();i++){
                translateWordList.get(i).setSaved(databaseHandler.isSavedWord(translateWordList.get(i).getTranslationID()));
            }
            translationAdapter.notifyDataSetChanged();

            if(translateWordList.isEmpty()){
                mllEmptyMessage.setVisibility(View.VISIBLE);
            }

            Toast.makeText(TranslationList.this,"Please Check Internet Connectivity.",Toast.LENGTH_SHORT).show();
        }
    }

    public void removeTranslationLocal(int position){
        databaseHandler.deleteSavedWord(translateWordList.get(position));
        translateWordList.get(position).setSaved(databaseHandler.isSavedWord(translateWordList.get(position).getTranslationID()));
        translationAdapter.notifyDataSetChanged();
        if(translateWordList.isEmpty()){
            mllEmptyMessage.setVisibility(View.VISIBLE);
        }
    }
}