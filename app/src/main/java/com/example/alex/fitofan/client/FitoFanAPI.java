package com.example.alex.fitofan.client;

import com.example.alex.fitofan.models.GetUserModel;

import java.util.HashMap;

import retrofit2.Call;
import retrofit2.http.FieldMap;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.POST;

interface FitoFanAPI {

//    WARNING! Use POST or GET annotation as requested by server restAPI documentation
//    WARNING! используйте POST или GET аннотации в соответствии с указаниями сервера

    @POST("/v1/registration")
    @FormUrlEncoded
    Call<GetUserModel> registration(@FieldMap (encoded = true) HashMap<String,String> params);

//    @PATCH("/authorization/my_user")
//    Call<UserInfoModel> updatePhone(@Header("Authorization") String token, @Body PhoneModel info);

    @POST("/v1/login")
    @FormUrlEncoded
    Call<GetUserModel> loginUser(@FieldMap (encoded = true) HashMap<String, String> params);

    @POST("/v1/registration")
    @FormUrlEncoded
    Call<GetUserModel> loginUser2(@FieldMap (encoded = true) HashMap<String, String> model);

    @GET("/v1/getuser")
    @FormUrlEncoded
    Call<GetUserModel> getUserData(@FieldMap (encoded = true) HashMap<String,String> params);

}
