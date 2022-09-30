package com.app.translation.API;

import com.google.gson.JsonObject;

import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.Part;
import retrofit2.http.Query;

public interface ApiInterface {

    @FormUrlEncoded
    @POST("logIn")
    Call<JsonObject> logIn(@Field("email") String email,
                            @Field("password") String password);
    @FormUrlEncoded
    @POST("signUp")
    Call<JsonObject> signUp(@Field("email") String email,
                            @Field("password") String password);

    @FormUrlEncoded
    @POST("savedTranslation")
    Call<JsonObject> saveTranslation(@Field("user_id") String user_id,
                                     @Field("from_type") String from_type,
                                     @Field("from_text") String from_text,
                                     @Field("to_type") String to_type,
                                     @Field("to_text") String to_text);

    @FormUrlEncoded
    @POST("deleteTranslation")
    Call<JsonObject> deleteTranslation(@Field("translation_id") String translationID);

    @GET("getAllTranslation")
    Call<JsonObject> getAllTranslation(@Query("user_id") String user_id);

    @FormUrlEncoded
    @POST("translate")
    Call<JsonObject> translation(@Field("auth_key") String auth_key,
                                     @Field("source_lang") String source_lang,
                                     @Field("text") String text,
                                     @Field("target_lang") String to_type);
}


