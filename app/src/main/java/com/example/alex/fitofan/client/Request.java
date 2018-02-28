package com.example.alex.fitofan.client;

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

//    public void getAllStudios(final GetBannerCode getBannerCode) {
//        String token = MSharedPreferences.getInstance().getKey();
//        Call<List<AllStudios>> call = RetrofitClient.getAPI().getAllStudios(token);
//        call.enqueue(new Callback<List<AllStudios>>() {
//            @Override
//            public void onResponse(Call<List<AllStudios>> call, Response<List<AllStudios>> response) {
//                if (response.isSuccessful()) {
////                    List<AllStudios> allStudios = new ArrayList<>();
////                    allStudios = response.body();
//                    getBannerCode.getBannerCode(response.body().get(0).getBanner().toString());
//                    //videoCode = response.body().get(0).getBanner().toString();
//
////                    if (allStudios != null && allStudios.get(0).getTeachers().length > 0) {
////
////                    }
////                    MSharedPreferences.getInstance().setTeachers(new Gson().toJson(teachers));
//
//                    Log.e("getAllStudios", response.body().get(0).getBanner().toString());
//                } else {
//                    Log.e("getAllStudios", response.code() + " " + response.message());
//                }
//            }
//
//            @Override
//            public void onFailure(Call<List<AllStudios>> call, Throwable t) {
//                Log.e("getAllStudios", t.getMessage());
//            }
//        });
//
//
//    }
//
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
