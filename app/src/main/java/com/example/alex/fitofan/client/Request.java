package com.example.alex.fitofan.client;

import android.util.Log;

import com.example.alex.fitofan.interfaces.ILoadingStatus;
import com.example.alex.fitofan.models.GetUserModel;
import com.example.alex.fitofan.settings.MSharedPreferences;
import com.google.gson.Gson;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Singleton class for passing your requests
 * TODO: Extend it if necessary and divide on subclasses
 * TODO: Наследуйте класс если посчитаете нужным и разбейте на группы наследников,
 * TODO: и в дальнейшем можно будет исп. pattern FabricMethod
 */
public class Request {

    private final static String CONNECTION_ERROR = "Connection error";

    private static Request request;

    public static Request getInstance() {
        if (request == null) request = new Request();
        return request;
    }

    public void singUp(HashMap<String, String> params, final ILoadingStatus loader) {
        Call<GetUserModel> call;
        call = RetrofitClient.getAPI().registration(params);

        call.enqueue(new Callback<GetUserModel>() {
            @Override
            public void onResponse(Call<GetUserModel> call, Response<GetUserModel> response) {

                Log.e("onKKK ", new Gson().toJson(response.body(), GetUserModel.class));

                if (response.isSuccessful()) {

                    loader.onSuccess(response.message());
                    Log.e("onResponseOk: ", response.headers().toString());
                    Log.e("onResponseOk: ", response.message());
                    Log.e("onResponseOk: ", String.valueOf(response.code()));

                } else {
                    loader.onFailure(response.message());
                    Log.e("onResponse: ", response.headers().toString());
                    Log.e("onResponse: ", response.message());
                    Log.e("onResponse: ", String.valueOf(response.code()));
                }
            }

            @Override
            public void onFailure(Call<GetUserModel> call, Throwable t) {
                Log.e("onResponseOk: ", t.toString());
                loader.onFailure(CONNECTION_ERROR);// don't change this string
            }
        });

    }

    public void singIn(HashMap<String, String> params, final ILoadingStatus loader) {
        Call<GetUserModel> call;
        call = RetrofitClient.getAPI().loginUser(params);

        call.enqueue(new Callback<GetUserModel>() {
            @Override
            public void onResponse(Call<GetUserModel> call, Response<GetUserModel> response) {
                Log.e("onLLL1", new Gson().toJson(response.body(), GetUserModel.class));
                if (response.isSuccessful()) {
                    try {
                        JSONObject jsonObject = new JSONObject(new Gson().toJson(response.body(), GetUserModel.class));
                        if (jsonObject.getInt("status") == 1){
                            loader.onSuccess("Success");
                            MSharedPreferences.getInstance().setUserInfo(new Gson().toJson(response.body(), GetUserModel.class));
                            Log.e("onLLL2", new Gson().toJson(response.body(), GetUserModel.class));
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                        Log.e("onSSS ", e.toString());
                    }

                } else {
                    loader.onFailure(response.message());
                    Log.e("onResponse: ", response.headers().toString());
                    Log.e("onResponse: ", response.message());
                    Log.e("onResponse: ", String.valueOf(response.code()));
                }
            }

            @Override
            public void onFailure(Call<GetUserModel> call, Throwable t) {
                loader.onFailure(CONNECTION_ERROR);// don't change this string
                Log.e("onFailure: ", t.toString());
            }
        });

    }

    public void sendPlan(HashMap<String, String> model, final ILoadingStatus loader) {
        Call<GetUserModel> call;
        call = RetrofitClient.getAPI().loginUser2(model);

        call.enqueue(new Callback<GetUserModel>() {
            @Override
            public void onResponse(Call<GetUserModel> call, Response<GetUserModel> response) {

                Log.e("onRRR ", new Gson().toJson(response.body(), GetUserModel.class));

                if (response.isSuccessful()) {

                    loader.onSuccess(response.message());
                    Log.e("onResponseOk: ", response.headers().toString());
                    Log.e("onResponseOk: ", response.message());
                    Log.e("onResponseOk: ", String.valueOf(response.code()));

                } else {
                    //if responce is failed we send message with error body to alertDialog
//                    String errMessage = "";
//                    try {
//                        errMessage = response.errorBody().string();
//                        loader.onFailure(errMessage);
//                    } catch (IOException e) {
//                        e.printStackTrace();
//                    }
                    loader.onFailure(response.message());
                    Log.e("onResponse: ", response.headers().toString());
                    Log.e("onResponse: ", response.message());
                    Log.e("onResponse: ", String.valueOf(response.code()));
                }
            }

            @Override
            public void onFailure(Call<GetUserModel> call, Throwable t) {
                Log.e("onResponseOk: ", t.toString());
                loader.onFailure(CONNECTION_ERROR);// don't change this string
            }
        });

    }

    public void singInWithFB(HashMap<String, String> params, final ILoadingStatus loader) {
        Call<GetUserModel> call;
        call = RetrofitClient.getAPI().loginUser(params);

        call.enqueue(new Callback<GetUserModel>() {
            @Override
            public void onResponse(Call<GetUserModel> call, Response<GetUserModel> response) {
                Log.e("onLLL1", new Gson().toJson(response.body(), GetUserModel.class));
                if (response.isSuccessful()) {
                    try {
                        JSONObject jsonObject = new JSONObject(new Gson().toJson(response.body(), GetUserModel.class));
                        if (jsonObject.getInt("status") == 1){
                            loader.onSuccess("Success");
                            Log.e("onLLL2", new Gson().toJson(response.body(), GetUserModel.class));
                            MSharedPreferences.getInstance().setUserInfo(new Gson().toJson(response.body(), GetUserModel.class));
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                        Log.e("onSSS ", e.toString());
                    }

                } else {
                    loader.onFailure(response.message());
                    Log.e("onResponse: ", response.headers().toString());
                    Log.e("onResponse: ", response.message());
                    Log.e("onResponse: ", String.valueOf(response.code()));
                }
            }

            @Override
            public void onFailure(Call<GetUserModel> call, Throwable t) {
                loader.onFailure(CONNECTION_ERROR);// don't change this string
                Log.e("onFailureFB: ", t.toString());
            }
        });
    }

    public void getUserData(HashMap<String, String> params, final ILoadingStatus loader) {
        Call<GetUserModel> call;
        call = RetrofitClient.getAPI().loginUser(params);

        call.enqueue(new Callback<GetUserModel>() {
            @Override
            public void onResponse(Call<GetUserModel> call, Response<GetUserModel> response) {
                Log.e("onLLL1", new Gson().toJson(response.body(), GetUserModel.class));
                if (response.isSuccessful()) {
                    try {
                        JSONObject jsonObject = new JSONObject(new Gson().toJson(response.body(), GetUserModel.class));
                        if (jsonObject.getInt("status") == 1){
                            loader.onSuccess("Success");
                            Log.e("onLLL2", new Gson().toJson(response.body(), GetUserModel.class));
                            MSharedPreferences.getInstance().setUserInfo(new Gson().toJson(response.body(), GetUserModel.class));
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                        Log.e("onSSS ", e.toString());
                    }

                } else {
                    loader.onFailure(response.message());
                    Log.e("onResponse: ", response.headers().toString());
                    Log.e("onResponse: ", response.message());
                    Log.e("onResponse: ", String.valueOf(response.code()));
                }
            }

            @Override
            public void onFailure(Call<GetUserModel> call, Throwable t) {
                loader.onFailure(CONNECTION_ERROR);// don't change this string
                Log.e("onFailureFB: ", t.toString());
            }
        });
    }

//    public void uploadAvatar(String pathAvatar, UploadUserAvatar<String> uploadUserAvatar) {
//        String token = MSharedPreferences.getInstance().getStatus();
//
//        Log.e("0x008800", "uploadAvatar: string pathAvatar" + pathAvatar);
//        File file = new File(pathAvatar);
//        Log.e("0x008800", "uploadAvatar: path avatar " + file.getPath());
//        RequestBody requestBody = RequestBody.create(MediaType.parse("image/*"), file);
//        Log.e("0x008800", "uploadAvatar: requesBody " + requestBody);
//        MultipartBody.Part body = MultipartBody.Part.createFormData("file", file.getName(), requestBody);
//        Log.e("0x008800", "uploadAvatar: body " + body);
////        RequestBody name = RequestBody.create(MediaType.parse("text/plain"), file.getName());
////        Log.e("0x008800", "uploadAvatar: file get name " + file.getName() );
////        String desc = "jpg";
//
//
//        // Log.e("0x008800", "uploadAvatar: name " + name );
////        UploadAvatar model = new UploadAvatar();
////        model.setAvatar(file);
//
//        Call<ResponseBody> call = RetrofitClient.getAPI().uploadAvatar(token, body);
//        Log.e("0x008800", "uploadAvatar: token " + token);
//        call.enqueue(new Callback<ResponseBody>() {
//            @Override
//            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
//                if (response.code() == 205) {
//
//                    Log.e("0x008800", "onResponse: sucessful upload file");
//                    uploadUserAvatar.onSuccessUploadUserAvatar("successful upload avatar");
//
//                } else {
//
//                    Log.e("0x008800", "onResponse: dont upload file");
//                    Log.e("0x008800", "onResponse: code " + response.code());
//                    try {
//                        Log.e("0x008800", "onResponse: errobody " + response.errorBody().string());
//                    } catch (IOException e) {
//                        e.printStackTrace();
//                    }
//                    uploadUserAvatar.onFailureUploadUserAvatar("fail upload avatar");
//                }
//            }
//
//            @Override
//            public void onFailure(Call<ResponseBody> call, Throwable t) {
//
//                Log.e("0x008800", "onFailure: upload file" + t.getMessage());
//            }
//        });
//    }
}
