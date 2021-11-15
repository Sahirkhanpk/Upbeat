package activities;

import android.content.Intent;
import android.net.Uri;
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
import java.util.HashMap;
import java.util.List;

import adapter.taskdetail_adapter;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import helper.CustomDialog;
import helper.GlobalData;
import model.Addon_tests_model;
import model.Offer;
import model.contactUsResponse;
import model.task_details_response;
import network.ApiClient;
import network.ApiInterface;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class Task_details_Activity extends AppCompatActivity {

    @BindView(R.id.back_img)
    ImageView back_img;




    @BindView(R.id.new_task_date)
    TextView new_task_date;

    @BindView(R.id.test_rv)
    RecyclerView test_rv;

    @BindView(R.id.txt_name)
    TextView txt_name;

    @BindView(R.id.txt_age)
    TextView txt_age;

    @BindView(R.id.txt_Account)
    TextView txt_Account;

    @BindView(R.id.img_call)
    ImageView img_call;

    @BindView(R.id.address_txt)
    TextView address_txt;

    @BindView(R.id.total_price)
    TextView total_price;

    @BindView(R.id.layout_add_test)
    LinearLayout layout_add_test;

    @BindView(R.id.layout_proceed)
    LinearLayout layout_proceed;




taskdetail_adapter Taskdetail_adapter;

    CustomDialog customDialog;

    ApiInterface apiInterface = ApiClient.getRetrofit().create(ApiInterface.class);
    List<Offer> orders;
    final LinearLayoutManager mLayoutManager  = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_task_details_);
        ButterKnife.bind(this);


        customDialog = new CustomDialog(Task_details_Activity.this);









        orders = new ArrayList<>();


        getRequestedTests(GlobalData.currentTaskid);
    }

    @OnClick({R.id.back_img,R.id.layout_proceed,R.id.img_call,R.id.layout_add_test})
    public void onViewClicked(View view) {
        switch (view.getId()) {

            case R.id.back_img:
                finish();
                break;

            case R.id.layout_proceed:
                 if(GlobalData.testlistempty){
                      Toast.makeText(Task_details_Activity.this,"Test list is empty Please add test before proceed", Toast.LENGTH_SHORT).show();
                 }else
                 {startActivity(new Intent(Task_details_Activity.this, Stuff_sample_collection_Activity.class));
                finish();}
                break;
            case R.id.img_call:
                Intent intent = new Intent(Intent.ACTION_DIAL, Uri.parse("tel:" + "+92211234567"));
                startActivity(intent);
                break;
            case R.id.layout_add_test:
                startActivity(new Intent(Task_details_Activity.this,Test_name_Activity.class));
                break;







        }
    }


    private void getRequestedTests(String id){
        HashMap<String, String> map = new HashMap<>();

        map.put("_id", id);

        if (customDialog != null)
            customDialog.show();


        String header = GlobalData.accessToken; /*SharedHelper.getKey(this, "token_type") + " " + SharedHelper.getKey(this, "access_token");*/
        Call<task_details_response> call = apiInterface.getTaskDetails(header,map);
        call.enqueue(new Callback<task_details_response>() {
            @Override
            public void onResponse(@NonNull Call<task_details_response> call, @NonNull Response<task_details_response> response) {
                customDialog.cancel();
                if (response.isSuccessful()) {
                    if(response.body().getData()!=null) {
                        new_task_date.setText(response.body().getData().getOrderDetails().getTestDate());
                        txt_name.setText(response.body().getData().getPatientDetails().getFullname());
                        txt_age.setText(response.body().getData().getPatientDetails().getAge() + "years - " + response.body().getData().getPatientDetails().getGender());
                        total_price.setText("Rs. "+response.body().getData().getOrderDetails().getTotalAmount());
                        txt_Account.setText(response.body().getData().getOrderDetails().getOrder_number());
                        address_txt.setText(response.body().getData().getPatientDetails().getAddress());
                    }
                  //  txt_Account.setText(response.body().getData().get(0).getOrderDetails().get;
                        if(response.body().getData().getOrderDetails().getTestName()!=null&&response.body().getData().getOrderDetails().getTestName().size()>0) {
                           Taskdetail_adapter =new taskdetail_adapter(response.body().getData().getOrderDetails().getTestName(),Task_details_Activity.this, true);
                         GlobalData.TaskDetails=response.body();
                          //  offers_adapter = new Offers_adapter(response.body().getData().getOffers(), Task_details_Activity.this, true);
                       GlobalData.orderDetails=response.body().getData().getOrderDetails();
                        test_rv.setLayoutManager(mLayoutManager);
                        test_rv.setItemAnimator(new DefaultItemAnimator());
                        test_rv.setAdapter(Taskdetail_adapter);

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
                        Toast.makeText(Task_details_Activity.this, jObjError.optString("message"), Toast.LENGTH_LONG).show();



                    } catch (IOException | JSONException e) {
                        // handle failure to read error
                        Toast.makeText(Task_details_Activity.this, e.toString(), Toast.LENGTH_SHORT).show();
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
            public void onFailure(@NonNull Call<task_details_response> call, @NonNull Throwable t) {
                customDialog.cancel();
                Toast.makeText(Task_details_Activity.this, R.string.something_went_wrong, Toast.LENGTH_SHORT).show();
            }
        });


    }




    private void AddOnTest() {

        Addon_tests_model addon_tests_model = new Addon_tests_model();
        addon_tests_model.setOrder_id(GlobalData.orderDetails.getOrderId());
        addon_tests_model.setTestName(GlobalData.Testlist);
        HashMap<String, String> map = new HashMap<>();

       /* map.put("order_id", GlobalData.orderDetails.getOrderId());
        map.put("test_name_id",String.valueOf(id));*/

        if (customDialog != null)
            customDialog.show();


        String header = GlobalData.accessToken; /*SharedHelper.getKey(this, "token_type") + " " + SharedHelper.getKey(this, "access_token");*/
        Call<contactUsResponse> call = apiInterface.AddOnTest(GlobalData.accessToken,addon_tests_model);
        call.enqueue(new Callback< contactUsResponse >() {
            @Override
            public void onResponse(@NonNull Call< contactUsResponse > call, @NonNull Response< contactUsResponse > response) {
                if (customDialog != null)
                    customDialog.cancel();
                if (response.isSuccessful()) {
                    GlobalData.testlistempty=false;
                    GlobalData.Testlist=null;
                    getRequestedTests(GlobalData.currentTaskid);
                  //  Toast.makeText(context, response.body().getMessage(), Toast.LENGTH_LONG).show();
                  //  notifyItemRemoved(position);

                    // img_lock.setBackground(getResources().getDrawable(R.id.email));
                   /* if(response.body().getData()!=null&&response.body().getData().size()>0) {
                        layout_members.setVisibility(View.VISIBLE);
                        layout_add_family_member.setVisibility(View.GONE);
                        newTaskRv.setVisibility(View.VISIBLE);
                        orders.clear();
                        //   newTaskRv.getRecycledViewPool().clear();
                        orders.addAll(response.body().getData());
                        newTaskAdapter.notifyDataSetChanged();

                    }else {
                        layout_members.setVisibility(View.GONE);
                        layout_add_family_member.setVisibility(View.VISIBLE);
                        newTaskRv.setVisibility(View.GONE);

                    }*/

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
                    GlobalData.test_order_list=null;
                    // Toast.makeText(Forgot_password_Activity.this, response.body().getMessage(), Toast.LENGTH_SHORT).show();
                    Gson gson = new GsonBuilder().create();

                    try {
                        JSONObject jObjError = new JSONObject(response.errorBody().string());
                        //  if (jObjError.has("error")) {
                         /*   startActivity(new Intent(Login.this, OTP.class)
                                    .setFlags(Intent.FLAG_ACTIVITY_NEW_TASK |
                                            Intent.FLAG_ACTIVITY_CLEAR_TASK));*/
                        Toast.makeText(Task_details_Activity.this, jObjError.optString("message"), Toast.LENGTH_LONG).show();



                    } catch (IOException | JSONException e) {
                        // handle failure to read error
                        Toast.makeText(Task_details_Activity.this, e.toString(), Toast.LENGTH_SHORT).show();
                    }
                }
            }

            @Override
            public void onFailure(@NonNull Call< contactUsResponse > call, @NonNull Throwable t) {
                customDialog.cancel();
                GlobalData.Testlist=null;
                Toast.makeText(Task_details_Activity.this, R.string.something_went_wrong, Toast.LENGTH_SHORT).show();
            }
        });


    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode==2)
        {
            if(GlobalData.Testlist!=null){
                AddOnTest();
            }

        }


    }

    @Override
    protected void onResume() {
        super.onResume();
        if(GlobalData.Testlist!=null){
            AddOnTest();
        }



    }


}