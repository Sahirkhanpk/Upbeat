package controller;

import android.util.Log;

import androidx.annotation.NonNull;

import com.android.volley.ServerError;
import helper.GlobalData;
import models.Categories_response;
import models.ProfileNew;
import network.ApiInterface;

import com.google.firebase.iid.FirebaseInstanceId;
import com.google.gson.Gson;
import com.google.gson.JsonSyntaxException;

import java.util.HashMap;


import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


public class GetProfile {

    private static final String TAG = "GetProfile";

    public GetProfile(ApiInterface apiInterface, final ProfileListener profileListener) {
     //   String device_id = Settings.Secure.getString(MyApplication.getInstance().getContentResolver(), Settings.Secure.ANDROID_ID);
        String device_type = "Android";
        String device_token = FirebaseInstanceId.getInstance().getToken();

        HashMap<String, String> params = new HashMap<>();
       // params.put("device_id", device_id);
      /*  params.put("device_type", device_type);
        params.put("device_token", device_token);*/

        params.put("server_key", "1539874186");
        params.put("access_token", GlobalData.accessToken);
        Call<Categories_response> call = apiInterface.getProfile(params);
        call.enqueue(new Callback<Categories_response>() {
            @Override
            public void onResponse(@NonNull Call<Categories_response> call, @NonNull Response<Categories_response> response) {
                Log.d(TAG, "onResponse: " + response.isSuccessful());
                if (response.isSuccessful()) {
               //     SharedHelper.putKey(MyApplication.getInstance(), Constants.PREF.PROFILE_ID, "" + "1");
                 //   SharedHelper.putKey(MyApplication.getInstance(), Constants.PREF.CURRENCY, "" + response.body().getCurrency());

                   GlobalData.categories_response=response.body();
                    profileListener.onSuccess(response.body());
                 //   String s=response.body().getData().getRole();
                //    GlobalData.ROLE = response.body().getData().getRole();
                   // SharedHelper.putKey(MyApplication.getInstance(), "role", response.body().getData().getRoles().get(0).getName());
                } else try {
                    ServerError serverError = new Gson().fromJson(response.errorBody().charStream(), ServerError.class);
                    profileListener.onFailure(serverError.getMessage());
                } catch (JsonSyntaxException e) {
                    profileListener.onFailure("");
                }
            }

            @Override
            public void onFailure(@NonNull Call<Categories_response> call, @NonNull Throwable t) {
                Log.d(TAG, "onFailure: " + t.getMessage());
                profileListener.onFailure("");
            }
        });
    }
}
