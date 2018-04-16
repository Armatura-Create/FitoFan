package com.example.alex.fitofan.client;

import android.util.Log;

import com.example.alex.fitofan.interfaces.ILoadingStatus;
import com.example.alex.fitofan.models.AuthenotificationKey;
import com.example.alex.fitofan.models.RegisterModel;
import com.example.alex.fitofan.models.SingInModel;
import com.example.alex.fitofan.models.TokenAtRegistration;

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

    public void singUp(RegisterModel model, final ILoadingStatus loader) {
        Call<TokenAtRegistration> call;
        call = RetrofitClient.getAPI().registration(model);

        call.enqueue(new Callback<TokenAtRegistration>() {
            @Override
            public void onResponse(Call<TokenAtRegistration> call, Response<TokenAtRegistration> response) {

                if (response.isSuccessful()) {
                    loader.onSuccess(response.message());

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
            public void onFailure(Call<TokenAtRegistration> call, Throwable t) {
                loader.onFailure(CONNECTION_ERROR);// don't change this string
            }
        });

    }

    public void singIn(SingInModel model, final ILoadingStatus loader) {
        Call<AuthenotificationKey> call;
        call = RetrofitClient.getAPI().loginUser(model);

        call.enqueue(new Callback<AuthenotificationKey>() {
            @Override
            public void onResponse(Call<AuthenotificationKey> call, Response<AuthenotificationKey> response) {

                if (response.isSuccessful()) {
                    loader.onSuccess(response.message());

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
            public void onFailure(Call<AuthenotificationKey> call, Throwable t) {
                loader.onFailure(CONNECTION_ERROR);// don't change this string
            }
        });

    }

//    public void uploadAvatar(String pathAvatar, UploadUserAvatar<String> uploadUserAvatar) {
//        String token = MSharedPreferences.getInstance().getKey();
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
