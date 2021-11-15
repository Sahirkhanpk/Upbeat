package activities;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.excellabs.R;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import adapter.promoAdapter;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import helper.CustomDialog;
import helper.GlobalData;
import model.CurrentPromoCodeModel;
import model.CurrentPromoCodeModelItem;
import network.ApiClient;
import network.ApiInterface;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class PromoCodesActivity extends AppCompatActivity {


    @BindView(R.id.back_img)
    ImageView back_img;


    @BindView(R.id.txt_current)
    TextView txt_current;

    @BindView(R.id.txt_past)
    TextView txt_past;


    @BindView(R.id.btn_current)
    LinearLayout btn_current;

    @BindView(R.id.btn_past)
    LinearLayout  btn_past;


    @BindView(R.id.promo_rv)
    RecyclerView promo_rv;

    promoAdapter promo_Adapter;

    List<CurrentPromoCodeModelItem> orders;

/*    @BindView(R.id.fabb)
    FloatingActionButton fabb;

    @BindView(R.id.txt_add)
    TextView txt_add;*/
    CustomDialog customDialog;

    ApiInterface apiInterface = ApiClient.getRetrofit().create(ApiInterface.class);
    final LinearLayoutManager mLayoutManager  = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_promo_codes);
        ButterKnife.bind(this);

        customDialog = new CustomDialog(PromoCodesActivity.this);


        orders = new ArrayList<>();
        promo_Adapter= new promoAdapter(orders, PromoCodesActivity.this, true);
        //  newTaskRv.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false));
        promo_rv.setLayoutManager(mLayoutManager);
        promo_rv.setItemAnimator(new DefaultItemAnimator());
        promo_rv.setAdapter(promo_Adapter);
        getCurrent();
    }


    @OnClick({R.id.back_img,R.id.btn_past,R.id.btn_current})
    public void onViewClicked(View view) {
        switch (view.getId()) {

            case R.id.back_img:
                finish();
                break;

            case R.id.btn_past:
                getExpiredPromos();

                break;
            case R.id.btn_current:
                getCurrent();

                break;




        }
    }




    @Override
    public void onResume(){
        super.onResume();

        getCurrent();
        // put your code here...

    }



    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        getCurrent();
    }
    private void getCurrent() {


        if (customDialog != null)
            customDialog.show();


        String header = GlobalData.accessToken; /*SharedHelper.getKey(this, "token_type") + " " + SharedHelper.getKey(this, "access_token");*/
        Call<CurrentPromoCodeModel> call = apiInterface.getCurrentPromo(header);
        call.enqueue(new Callback<CurrentPromoCodeModel>() {
            @Override
            public void onResponse(@NonNull Call<CurrentPromoCodeModel> call, @NonNull Response<CurrentPromoCodeModel> response) {
                customDialog.cancel();
                if (response.isSuccessful()) {
                    btn_past.setBackground(getResources().getDrawable(R.drawable.churus_button));
                    btn_current.setBackground(getResources().getDrawable(R.drawable.churus_button_blue));
                    txt_past.setTextColor(getResources().getColor(R.color.colorPrimaryText));
                    txt_current.setTextColor(getResources().getColor(R.color.colorWhite));
                    if(response.body().getData()!=null&&response.body().getData().size()>0) {
                        //layout_members.setVisibility(View.VISIBLE);
                      //  layout_add_family_member.setVisibility(View.GONE);


                        promo_rv.setVisibility(View.VISIBLE);
                        orders.clear();
                        //   newTaskRv.getRecycledViewPool().clear();
                        orders.addAll(response.body().getData());
                        promo_Adapter.notifyDataSetChanged();

                    }else {
                       // layout_members.setVisibility(View.GONE);
                     //   layout_add_family_member.setVisibility(View.VISIBLE);
                      promo_rv.setVisibility(View.GONE);
                        Toast.makeText(PromoCodesActivity.this, "List is Empty", Toast.LENGTH_LONG).show();

                    }

                   /* //  initView();
                    LayoutInflater inflater = getLayoutInflater();
                    View layout = inflater.inflate(R.layout.custom_toarst,
                            (ViewGroup) findViewById(R.id.layout_toarst));


                 *//*   TextView text = (TextView) layout.findViewById(R.id.text);
                    text.setText("Hello! This is a custom toast!");*//*

                    Toast toast = new Toast(getApplicationContext());
                    toast.setGravity(Gravity.BOTTOM, 0, 0);
                    toast.setDuration(Toast.LENGTH_LONG);
                    toast.setView(layout);
                    toast.show();
                    //Toast.makeText(Create_family_profile_Activity.this, getResources().getString(R.string.success_member), Toast.LENGTH_SHORT).show();

                    finish();*/
                } else {
                  //  Toast.makeText(PromoCodesActivity.this, getString(R.string.something_went_wrong), Toast.LENGTH_SHORT).show();
                    Gson gson = new GsonBuilder().create();

                    try {
                        JSONObject jObjError = new JSONObject(response.errorBody().string());
                        //  if (jObjError.has("error")) {
                         /*   startActivity(new Intent(Login.this, OTP.class)
                                    .setFlags(Intent.FLAG_ACTIVITY_NEW_TASK |
                                            Intent.FLAG_ACTIVITY_CLEAR_TASK));*/
                        Toast.makeText(PromoCodesActivity.this, jObjError.optString("message"), Toast.LENGTH_LONG).show();



                    } catch (IOException | JSONException e) {
                        // handle failure to read error
                        Toast.makeText(PromoCodesActivity.this, e.toString(), Toast.LENGTH_SHORT).show();
                    }
                   /* Gson gson = new GsonBuilder().create();
                    try {
                        ProfileError error = gson.fromJson(response.errorBody().string(),ProfileError.class);
                        System.out.println("error "+error.toString());
                        if (error.getName() != null){
                            Toast.makeText(ProfileActivity.this, error.getName().get(0), Toast.LENGTH_SHORT).show();
                        }else if(error.getEmail() != null){
                            Toast.makeText(ProfileActivity.this, error.getEmail().get(0), Toast.LENGTH_SHORT).show();
                        }else if(error.getAvatar() != null){
                            Toast.makeText(ProfileActivity.this, error.getAvatar().get(0), Toast.LENGTH_SHORT).show();
                            initView();
                        }
                    } catch (IOException e) {
                        // handle failure to read error
                        Toast.makeText(ProfileActivity.this, e.toString(), Toast.LENGTH_SHORT).show();
                    }*/
                }
            }

            @Override
            public void onFailure(@NonNull Call<CurrentPromoCodeModel> call, @NonNull Throwable t) {
                customDialog.cancel();
                Toast.makeText(PromoCodesActivity.this, R.string.something_went_wrong, Toast.LENGTH_SHORT).show();
            }
        });


    }

    private void getExpiredPromos() {


        if (customDialog != null)
            customDialog.show();


        String header = GlobalData.accessToken; /*SharedHelper.getKey(this, "token_type") + " " + SharedHelper.getKey(this, "access_token");*/
        Call<CurrentPromoCodeModel> call = apiInterface.getExpiredPromo(header);
        call.enqueue(new Callback<CurrentPromoCodeModel>() {
            @Override
            public void onResponse(@NonNull Call<CurrentPromoCodeModel> call, @NonNull Response<CurrentPromoCodeModel> response) {
                customDialog.cancel();
                if (response.isSuccessful()) {
                    btn_current.setBackground(getResources().getDrawable(R.drawable.churus_button));
                    btn_past.setBackground(getResources().getDrawable(R.drawable.churus_button_blue));
                    txt_current.setTextColor(getResources().getColor(R.color.colorPrimaryText));
                    txt_past.setTextColor(getResources().getColor(R.color.colorWhite));

                    if(response.body().getData()!=null&&response.body().getData().size()>0) {
                        //layout_members.setVisibility(View.VISIBLE);
                        //  layout_add_family_member.setVisibility(View.GONE);
                        promo_rv.setVisibility(View.VISIBLE);
                        orders.clear();
                        //   newTaskRv.getRecycledViewPool().clear();
                        orders.addAll(response.body().getData());
                        promo_Adapter.notifyDataSetChanged();


                    }else {
                        // layout_members.setVisibility(View.GONE);
                        //   layout_add_family_member.setVisibility(View.VISIBLE);
                           promo_rv.setVisibility(View.GONE);
                        Toast.makeText(PromoCodesActivity.this, "List is Empty", Toast.LENGTH_LONG).show();

                    }

                   /* //  initView();
                    LayoutInflater inflater = getLayoutInflater();
                    View layout = inflater.inflate(R.layout.custom_toarst,
                            (ViewGroup) findViewById(R.id.layout_toarst));


                 *//*   TextView text = (TextView) layout.findViewById(R.id.text);
                    text.setText("Hello! This is a custom toast!");*//*

                    Toast toast = new Toast(getApplicationContext());
                    toast.setGravity(Gravity.BOTTOM, 0, 0);
                    toast.setDuration(Toast.LENGTH_LONG);
                    toast.setView(layout);
                    toast.show();
                    //Toast.makeText(Create_family_profile_Activity.this, getResources().getString(R.string.success_member), Toast.LENGTH_SHORT).show();

                    finish();*/
                } else {
                    //  Toast.makeText(PromoCodesActivity.this, getString(R.string.something_went_wrong), Toast.LENGTH_SHORT).show();
                    Gson gson = new GsonBuilder().create();

                    try {
                        JSONObject jObjError = new JSONObject(response.errorBody().string());
                        //  if (jObjError.has("error")) {
                         /*   startActivity(new Intent(Login.this, OTP.class)
                                    .setFlags(Intent.FLAG_ACTIVITY_NEW_TASK |
                                            Intent.FLAG_ACTIVITY_CLEAR_TASK));*/
                        Toast.makeText(PromoCodesActivity.this, jObjError.optString("message"), Toast.LENGTH_LONG).show();



                    } catch (IOException | JSONException e) {
                        // handle failure to read error
                        Toast.makeText(PromoCodesActivity.this, e.toString(), Toast.LENGTH_SHORT).show();
                    }
                   /* Gson gson = new GsonBuilder().create();
                    try {
                        ProfileError error = gson.fromJson(response.errorBody().string(),ProfileError.class);
                        System.out.println("error "+error.toString());
                        if (error.getName() != null){
                            Toast.makeText(ProfileActivity.this, error.getName().get(0), Toast.LENGTH_SHORT).show();
                        }else if(error.getEmail() != null){
                            Toast.makeText(ProfileActivity.this, error.getEmail().get(0), Toast.LENGTH_SHORT).show();
                        }else if(error.getAvatar() != null){
                            Toast.makeText(ProfileActivity.this, error.getAvatar().get(0), Toast.LENGTH_SHORT).show();
                            initView();
                        }
                    } catch (IOException e) {
                        // handle failure to read error
                        Toast.makeText(ProfileActivity.this, e.toString(), Toast.LENGTH_SHORT).show();
                    }*/
                }
            }

            @Override
            public void onFailure(@NonNull Call<CurrentPromoCodeModel> call, @NonNull Throwable t) {
                customDialog.cancel();
                Toast.makeText(PromoCodesActivity.this, R.string.something_went_wrong, Toast.LENGTH_SHORT).show();
            }
        });


    }

}