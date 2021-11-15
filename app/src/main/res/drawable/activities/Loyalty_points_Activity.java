package activities;

import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
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

import adapter.Offers_adapter;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import helper.CustomDialog;
import helper.GlobalData;
import model.Loyalty_points_response;
import model.Offer;
import network.ApiClient;
import network.ApiInterface;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class Loyalty_points_Activity extends AppCompatActivity {

    @BindView(R.id.back_img)
    ImageView back_img;

    @BindView(R.id.offers_rv)
    RecyclerView offers_rv;

    @BindView(R.id.tv_total_points)
    TextView tv_total_points;

    @BindView(R.id.tv_exiting_offers)
    TextView tv_exiting_offers;

    @BindView(R.id.tv_message)
    TextView tv_message;

    Offers_adapter offers_adapter;
    CustomDialog customDialog;

    ApiInterface apiInterface = ApiClient.getRetrofit().create(ApiInterface.class);
    List<Offer> orders;
    final LinearLayoutManager mLayoutManager  = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_loyalty_points_);
        ButterKnife.bind(this);


        customDialog = new CustomDialog(Loyalty_points_Activity.this);









        orders = new ArrayList<>();

        /*offers_response offers_response= new offers_response();
        offers_response.setMessage("Get PKR 1000 in your wallet for 30,000 points.");

        orders.add(offers_response);
        offers_response offers_response1= new offers_response();
        offers_response1.setMessage("Get PKR 2000 in your wallet for 60,000 points.");
        orders.add(offers_response1);

       offers_adapter = new Offers_adapter(orders, Loyalty_points_Activity.this, true);
        //  newTaskRv.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false));
        offers_rv.setLayoutManager(mLayoutManager);
        offers_rv.setItemAnimator(new DefaultItemAnimator());
        offers_rv.setAdapter(offers_adapter);*/
        getcurrentOffers();
    }

    @OnClick({R.id.back_img})
    public void onViewClicked(View view) {
        switch (view.getId()) {

            case R.id.back_img:
                finish();
                break;






        }
    }

    private void getcurrentOffers() {


        if (customDialog != null)
            customDialog.show();


        String header = GlobalData.accessToken; /*SharedHelper.getKey(this, "token_type") + " " + SharedHelper.getKey(this, "access_token");*/
        Call<Loyalty_points_response> call = apiInterface.getoffers(header);
        call.enqueue(new Callback<Loyalty_points_response>() {
            @Override
            public void onResponse(@NonNull Call<Loyalty_points_response> call, @NonNull Response<Loyalty_points_response> response) {
                customDialog.cancel();
                if (response.isSuccessful()) {
                    if(response.body().getData().getTotalpoints()!=null&&!response.body().getData().getTotalpoints().equals("null"))
                    {  GlobalData.Totalpoints=response.body().getData().getTotalpoints();
                    tv_total_points.setText(Integer.toString(response.body().getData().getTotalpoints())+" points");
                    }
                    if(response.body().getData().getTotalpoints()==0)
                    { tv_message.setText("You have redeemed all of your loyalty points.Currently you have no points to radeem");
                    tv_exiting_offers.setVisibility(View.GONE);
                    offers_rv.setVisibility(View.GONE);}
                    else
                        if(response.body().getData().getOffers()!=null&&response.body().getData().getOffers().size()>0) {

                        offers_adapter = new Offers_adapter(response.body().getData().getOffers(), Loyalty_points_Activity.this, true);
                        offers_rv.setLayoutManager(mLayoutManager);
                        offers_rv.setItemAnimator(new DefaultItemAnimator());
                        offers_rv.setAdapter(offers_adapter);




                    }


                } else {
                    //  Toast.makeText(PromoCodesActivity.this, getString(R.string.something_went_wrong), Toast.LENGTH_SHORT).show();
                    Gson gson = new GsonBuilder().create();

                    try {
                        JSONObject jObjError = new JSONObject(response.errorBody().string());
                        //  if (jObjError.has("error")) {
                         /*   startActivity(new Intent(Login.this, OTP.class)
                                    .setFlags(Intent.FLAG_ACTIVITY_NEW_TASK |
                                            Intent.FLAG_ACTIVITY_CLEAR_TASK));*/
                        Toast.makeText(Loyalty_points_Activity.this, jObjError.optString("message"), Toast.LENGTH_LONG).show();



                    } catch (IOException | JSONException e) {
                        // handle failure to read error
                        Toast.makeText(Loyalty_points_Activity.this, e.toString(), Toast.LENGTH_SHORT).show();
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
            public void onFailure(@NonNull Call<Loyalty_points_response> call, @NonNull Throwable t) {
                customDialog.cancel();
                Toast.makeText(Loyalty_points_Activity.this, R.string.something_went_wrong, Toast.LENGTH_SHORT).show();
            }
        });


    }



}