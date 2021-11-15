package network;

import models.AuthToken;
import models.Categories_response;
import models.ProfileNew;

import java.util.HashMap;


import okhttp3.MultipartBody;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.DELETE;
import retrofit2.http.FieldMap;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.Header;
import retrofit2.http.Headers;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.Part;
import retrofit2.http.Path;

public interface ApiInterface {


    //  @Headers("Content-Type: application/json")
    @FormUrlEncoded
    @POST("endpoints/v1/auth/login")
    Call<AuthToken> login(@FieldMap HashMap<String, String> params, @Header("server_key") String basic);


    @FormUrlEncoded
    @POST("endpoints/v1/misc/get_categories")
    Call<Categories_response> getProfile(@FieldMap HashMap<String, String> params);


}
