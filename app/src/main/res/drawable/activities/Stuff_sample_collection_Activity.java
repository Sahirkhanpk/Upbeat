package activities;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Typeface;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.excellabs.R;
import com.github.angads25.toggle.LabeledSwitch;
import com.github.angads25.toggle.interfaces.OnToggledListener;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import adapter.simple_collection_adapter;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import helper.CustomDialog;
import helper.GlobalData;
import model.Complete_task_response;
import model.Offer;
import model.Stuff_simpling_model;
import network.ApiClient;
import network.ApiInterface;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class Stuff_sample_collection_Activity extends AppCompatActivity {

    @BindView(R.id.back_img)
    ImageView back_img;

    @BindView(R.id.test_simples)
    RecyclerView test_rv;

    @BindView(R.id.txt_account_collected)
    TextView txt_account_collected;

    @BindView(R.id.txt_slip_number)
    TextView txt_slip_number;



    simple_collection_adapter Simple_collection_adapter;

    CustomDialog customDialog;

    ApiInterface apiInterface = ApiClient.getRetrofit().create(ApiInterface.class);
    List<Offer> orders;
    final LinearLayoutManager mLayoutManager  = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_stuff_sample_collection_);
        ButterKnife.bind(this);


        customDialog = new CustomDialog(Stuff_sample_collection_Activity.this);
        getPatenttasts();
        LabeledSwitch labeledSwitch = findViewById(R.id.switch1);
        LabeledSwitch labeledSwitch2 = findViewById(R.id.switch2);

        if(GlobalData.TaskDetails.getData().getOrderDetails()!=null)
        {
            txt_account_collected.setText(GlobalData.TaskDetails.getData().getOrderDetails().getTotalAmount());
            if(GlobalData.TaskDetails.getData().getOrderDetails().getPaymentType().equals("Cash"))
        {
            labeledSwitch.setEnabled(true);
            labeledSwitch2.setEnabled(false);
        }else {
            labeledSwitch.setEnabled(false);
            labeledSwitch2.setEnabled(true);
        }}
     //   txt_slip_number.setText(GlobalData.TaskDetails.getData().getOrderDetails().get);
        labeledSwitch.setOnToggledListener(new OnToggledListener() {
            @Override
            public void onSwitched(LabeledSwitch labeledSwitch, boolean isOn) {
                // Implement your switching logic here
            }
        });









        orders = new ArrayList<>();
      /*  if(GlobalData.orderDetails!=null&&GlobalData.orderDetails.getTestName().size()>0) {
            Simple_collection_adapter = new simple_collection_adapter(GlobalData.orderDetails.getTestName(), Stuff_sample_collection_Activity.this, true);
            //  offers_adapter = new Offers_adapter(response.body().getData().getOffers(), Stuff_sample_collection_Activity.this, true);
            test_rv.setLayoutManager(mLayoutManager);
            test_rv.setItemAnimator(new DefaultItemAnimator());
            test_rv.setAdapter(Simple_collection_adapter);
        }*/




    }
    private void getPatenttasts() {
        HashMap<String, String> map = new HashMap<>();

        map.put("patient_number", GlobalData.TaskDetails.getData().getPatientDetails().getPatientNbr());
        if (customDialog != null)
            customDialog.show();
        // map.put("keywords", RequestBody.create(MediaType.parse("text/plain"), "test"));

        String header = GlobalData.accessToken; /*SharedHelper.getKey(this, "token_type") + " " + SharedHelper.getKey(this, "access_token");*/
        Call<Stuff_simpling_model> call = apiInterface.getPatientTests(header,map);
        call.enqueue(new Callback<Stuff_simpling_model>() {
            @Override
            public void onResponse(@NonNull Call<Stuff_simpling_model> call, @NonNull Response<Stuff_simpling_model> response) {
                customDialog.cancel();
                if (response.isSuccessful()) {
                    if(response.body().getData() !=null&&response.body().getData().size()>0) {
                        Simple_collection_adapter = new simple_collection_adapter(response.body().getData(), Stuff_sample_collection_Activity.this, true);
                        //  offers_adapter = new Offers_adapter(response.body().getData().getOffers(), Stuff_sample_collection_Activity.this, true);
                        test_rv.setLayoutManager(mLayoutManager);
                        test_rv.setItemAnimator(new DefaultItemAnimator());
                        test_rv.setAdapter(Simple_collection_adapter);
                    }
                }
                else{
                    Toast.makeText(Stuff_sample_collection_Activity.this, "List Empty", Toast.LENGTH_SHORT).show();

                }

            }

            @Override
            public void onFailure(@NonNull Call<Stuff_simpling_model> call, @NonNull Throwable t) {
                customDialog.cancel();
                Toast.makeText(Stuff_sample_collection_Activity.this, R.string.something_went_wrong, Toast.LENGTH_SHORT).show();
            }
        });


    }


    private void getTaskCompleted(String id){
        HashMap<String, String> map = new HashMap<>();

        map.put("_id", id);
        map.put("status", "Completed");
        if (customDialog != null)
            customDialog.show();


        String header = GlobalData.accessToken; /*SharedHelper.getKey(this, "token_type") + " " + SharedHelper.getKey(this, "access_token");*/
        Call<Complete_task_response> call = apiInterface.getTaskStatusCompleted(header,map);
        call.enqueue(new Callback<Complete_task_response>() {
            @Override
            public void onResponse(@NonNull Call<Complete_task_response> call, @NonNull Response<Complete_task_response> response) {
                customDialog.cancel();
                if (response.isSuccessful()) {

                    alertTaskCompleted();

                } else {
                    //  Toast.makeText(PromoCodesActivity.this, getString(R.string.something_went_wrong), Toast.LENGTH_SHORT).show();
                    Gson gson = new GsonBuilder().create();

                    try {
                        JSONObject jObjError = new JSONObject(response.errorBody().string());
                        //  if (jObjError.has("error")) {
                         /*   startActivity(new Intent(Login.this, OTP.class)
                                    .setFlags(Intent.FLAG_ACTIVITY_NEW_TASK |
                                            Intent.FLAG_ACTIVITY_CLEAR_TASK));*/
                        Toast.makeText(Stuff_sample_collection_Activity.this, jObjError.optString("message"), Toast.LENGTH_LONG).show();



                    } catch (IOException | JSONException e) {
                        // handle failure to read error
                        Toast.makeText(Stuff_sample_collection_Activity.this, e.toString(), Toast.LENGTH_SHORT).show();
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
            public void onFailure(@NonNull Call<Complete_task_response> call, @NonNull Throwable t) {
                customDialog.cancel();
                Toast.makeText(Stuff_sample_collection_Activity.this, R.string.something_went_wrong, Toast.LENGTH_SHORT).show();
            }
        });


    }
    @OnClick({R.id.back_img,R.id.layout_conform_payment})
    public void onViewClicked(View view) {
        switch (view.getId()) {

            case R.id.back_img:
                finish();
                break;

           /* case R.id.layout_proceed:
                startActivity(new Intent(Stuff_sample_collection_Activity.this, Stuff_sample_collection_Activity.class));
                finish();
                break;*/
            case R.id.layout_conform_payment:
               getTaskCompleted(GlobalData.currentTaskid);
                break;






        }
    }

    public void alertTaskCompleted() {
        final AlertDialog.Builder alertDialog = new AlertDialog.Builder(this);
        LayoutInflater inflater = (LayoutInflater) this.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View convertView = inflater.inflate(R.layout.dialog_task_completed, null);
        alertDialog.setView(convertView);


        alertDialog
                .setPositiveButton(getResources().getString(R.string.ok), new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        // continue with delete
//                        handler.removeCallbacks(runnable);
                        dialog.dismiss();
                        finish();


                    }
                });

        AlertDialog alert = alertDialog.create();
        alert.setCancelable(false);
        alert.show();
        Button pbutton = alert.getButton(DialogInterface.BUTTON_POSITIVE);
        pbutton.setTextColor(ContextCompat.getColor(Stuff_sample_collection_Activity.this, R.color.colorAccent));
        pbutton.setTypeface(pbutton.getTypeface(), Typeface.BOLD);

        /*if (!((ServiceFlow) this).isFinishing()) {
            alert.show();
        }*/
    }

}