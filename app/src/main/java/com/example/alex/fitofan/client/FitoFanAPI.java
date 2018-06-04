package com.example.alex.fitofan.client;

import com.example.alex.fitofan.models.GetCommentsModel;
import com.example.alex.fitofan.models.GetPlanModel;
import com.example.alex.fitofan.models.GetPlansModel;
import com.example.alex.fitofan.models.GetRatingModel;
import com.example.alex.fitofan.models.GetSearchPlansModel;
import com.example.alex.fitofan.models.GetSearchUsersModel;
import com.example.alex.fitofan.models.GetUserModel;
import com.example.alex.fitofan.models.GetWallModel;
import com.example.alex.fitofan.models.LikeModel;
import com.example.alex.fitofan.models.StatusModel;
import com.example.alex.fitofan.models.SubModel;
import com.example.alex.fitofan.settings.MApplication;

import java.util.HashMap;

import retrofit2.Call;
import retrofit2.http.FieldMap;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.POST;

interface FitoFanAPI {

//    WARNING! Use POST or GET annotation as requested by server restAPI documentation
//    WARNING! используйте POST или GET аннотации в соответствии с указаниями сервера

    @POST("/v1/registration")
    @FormUrlEncoded
    Call<GetUserModel> registration(@FieldMap(encoded = true) HashMap<String, String> params);

//    @PATCH("/authorization/my_user")
//    Call<UserInfoModel> updatePhone(@Header("Authorization") String token, @Body PhoneModel info);

    @POST("/v1/login")
    @FormUrlEncoded
    Call<GetUserModel> loginUser(@FieldMap(encoded = true) HashMap<String, String> params);

    @POST("/v1/addPlan")
    @FormUrlEncoded
    Call<GetPlanModel> sendPlan(@FieldMap(encoded = true) HashMap<String, String> params);

    @POST("/v1/deletePlan")
    @FormUrlEncoded
    Call<LikeModel> deletePlan(@FieldMap(encoded = true) HashMap<String, String> params);

    @POST("/v1/getPlan")
    @FormUrlEncoded
    Call<GetPlanModel> getPlan(@FieldMap(encoded = true) HashMap<String, String> params);

    @POST("/v1/getLatestPlans")
    @FormUrlEncoded
    Call<GetWallModel> getWall(@FieldMap(encoded = true) HashMap<String, String> params);

    @POST("/v1/getUserPlans")
    @FormUrlEncoded
    Call<GetPlansModel> getUserPlans(@FieldMap(encoded = true) HashMap<String, String> params);

    @POST("/v1/getRating")
    @FormUrlEncoded
    Call<GetRatingModel> getRating(@FieldMap(encoded = true) HashMap<String, String> params);

    @POST("/v1/getUser")
    @FormUrlEncoded
    Call<GetUserModel> getUserData(@FieldMap(encoded = true) HashMap<String, String> params);

    @POST("/v1/likePlan")
    @FormUrlEncoded
    Call<LikeModel> likePlan(@FieldMap(encoded = true) HashMap<String, String> params);

    @POST("/v1/dislikePlan")
    @FormUrlEncoded
    Call<LikeModel> dislikePlan(@FieldMap(encoded = true) HashMap<String, String> params);

    @POST("/v1/getSavedPlans")
    @FormUrlEncoded
    Call<GetPlansModel> getSavedPlans(@FieldMap(encoded = true) HashMap<String, String> params);

    @POST("/v1/savePlan")
    @FormUrlEncoded
    Call<LikeModel> savePlan(@FieldMap(encoded = true) HashMap<String, String> params);

    @POST("/v1/unsavePlan")
    @FormUrlEncoded
    Call<LikeModel> unsavePlan(@FieldMap(encoded = true) HashMap<String, String> params);

    @POST("/v1/subscribeUser")
    @FormUrlEncoded
    Call<SubModel> subscribeUser(@FieldMap(encoded = true) HashMap<String, String> params);

    @POST("/v1/unsubscribeUser")
    @FormUrlEncoded
    Call<SubModel> unSubscribeUser(@FieldMap(encoded = true) HashMap<String, String> params);

    @POST("/v1/getSubscribers")
    @FormUrlEncoded
    Call<GetRatingModel> getSubscribers(@FieldMap(encoded = true) HashMap<String, String> params);

    @POST("/v1/getSubscriptions")
    @FormUrlEncoded
    Call<GetRatingModel> getSubscriptions(@FieldMap(encoded = true) HashMap<String, String> params);

    @POST("/v1/addTrainingPlanComment")
    @FormUrlEncoded
    Call<SubModel> addTrainingPlanComment(@FieldMap(encoded = true) HashMap<String, String> params);

    @POST("/v1/deleteTrainingPlanComment")
    @FormUrlEncoded
    Call<SubModel> deleteTrainingPlanComment(@FieldMap(encoded = true) HashMap<String, String> params);

    @POST("/v1/getPlanComments")
    @FormUrlEncoded
    Call<GetCommentsModel> getPlanComments(@FieldMap(encoded = true) HashMap<String, String> params);

    @POST("/v1/searchPlans")
    @FormUrlEncoded
    Call<GetSearchPlansModel> searchPlans(@FieldMap(encoded = true) HashMap<String, String> params);

    @POST("/v1/changeUserInfo")
    @FormUrlEncoded
    Call<StatusModel> changeUserInfo(@FieldMap(encoded = true) HashMap<String, String> params);

    @POST("/v1/changeUserPhoto")
    @FormUrlEncoded
    Call<StatusModel> changeUserPhoto(@FieldMap(encoded = true) HashMap<String, String> params);

    @POST("/v1/changeUserPassword")
    @FormUrlEncoded
    Call<StatusModel> changeUserPassword(@FieldMap(encoded = true) HashMap<String, String> params);

    @POST("/v1/changeUserEmail")
    @FormUrlEncoded
    Call<StatusModel> changeUserEmail(@FieldMap(encoded = true) HashMap<String, String> params);

    @POST("/v1/likeUserAvatar")
    @FormUrlEncoded
    Call<LikeModel> likeUserAvatar(@FieldMap(encoded = true) HashMap<String, String> params);

    @POST("/v1/dislikeUserAvatar")
    @FormUrlEncoded
    Call<LikeModel> dislikeUserAvatar(@FieldMap(encoded = true) HashMap<String, String> params);

    @POST("/v1/editPlan")
    @FormUrlEncoded
    Call<LikeModel> editPlan(@FieldMap(encoded = true) HashMap<String, String> params);

    @POST("/v1/getMyUnpublishedPlans")
    @FormUrlEncoded
    Call<GetPlansModel> getMyUnpublishedPlans(@FieldMap(encoded = true) HashMap<String, String> params);

    @POST("/v1/changePlanStatus")
    @FormUrlEncoded
    Call<LikeModel> changePlanStatus(@FieldMap(encoded = true) HashMap<String, String> params);

    @POST("/v1/searchUsers")
    @FormUrlEncoded
    Call<GetSearchUsersModel> searchUsers(@FieldMap(encoded = true) HashMap<String, String> params);

}
