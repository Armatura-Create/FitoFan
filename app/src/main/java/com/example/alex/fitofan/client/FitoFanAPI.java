package com.example.alex.fitofan.client;

import com.example.alex.fitofan.models.AuthenotificationKey;
import com.example.alex.fitofan.models.RegisterModel;
import com.example.alex.fitofan.models.SingInModel;
import com.example.alex.fitofan.models.TokenAtRegistration;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.POST;

interface FitoFanAPI {

//    WARNING! Use POST or GET annotation as requested by server restAPI documentation
//    WARNING! используйте POST или GET аннотации в соответствии с указаниями сервера

    @POST("/v1/registration")
    Call<TokenAtRegistration> registration(@Body RegisterModel model);

//    @PATCH("/authorization/my_user")
//    Call<UserInfoModel> updatePhone(@Header("Authorization") String token, @Body PhoneModel info);

    @POST("/v1/login")
    Call<AuthenotificationKey> loginUser(@Body SingInModel model);

}
