package network;

import models.AuthToken;
import models.Categories_response;
import models.ProfileNew;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;


import models.filter_api_request;
import models.filter_api_response;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.DELETE;
import retrofit2.http.Field;
import retrofit2.http.FieldMap;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.Header;
import retrofit2.http.Headers;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.Part;
import retrofit2.http.Path;

public interface ApiInterface {


  /*  @FormUrlEncoded
    @POST("endpoints/v1/misc/filter")
    Call<filter_api_response> getFilter(@Field("server_key") String server_key, @Field("access_token") String access_token, @Field("categories") ArrayList<Integer> categories, @Field("user_id") int user_id , @Field("attributes") ArrayList<Integer> attributes, @Field("min_price") Double min_price, @Field("max_price") Double max_price);
    //  @Headers("Content-Type: application/json")*/


    @FormUrlEncoded
    @POST("endpoints/v1/misc/filter")
    Call<filter_api_response> getFilter(@FieldMap HashMap<String, String> params);


    @FormUrlEncoded
    @POST("endpoints/v1/auth/login")
    Call<AuthToken> login(@FieldMap HashMap<String, String> params, @Header("server_key") String basic);


    @FormUrlEncoded
    @POST("endpoints/v1/misc/get_categories")
    Call<Categories_response> getProfile(@FieldMap HashMap<String, String> params);

  //  @Headers({"Content-Type: multipart/form-data","Content-Type: text/plain"})
    @FormUrlEncoded
    @POST("endpoints/v1/misc/get_attributes")
    Call<Categories_response> getAttributes(@Field("server_key") String server_key, @Field("access_token") String access_token, @Field("category_id") int category_id);










}
