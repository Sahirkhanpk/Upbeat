package activities;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Typeface;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import com.android.volley.ServerError;
import com.example.excellabs.R;
import com.google.gson.Gson;
import com.google.gson.JsonSyntaxException;

import java.util.HashMap;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import helper.ConnectionHelper;
import helper.CustomDialog;
import helper.GlobalData;
import model.Covid_response;
import model.add_order_model;
import model.place_order_response;
import network.ApiClient;
import network.ApiInterface;
import okhttp3.MediaType;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import utils.Utils;

public class ConfirmPaymentActivity extends AppCompatActivity {


    Context context;
    Activity activity;
    ConnectionHelper connectionHelper;
    CustomDialog customDialog;
    boolean isInternetAvailable;
    ApiInterface apiInterface = ApiClient.getRetrofit().create(ApiInterface.class);
    String TAG = "ConfirmPaymentActivity";
    add_order_model add_order_model1= new add_order_model();

    @BindView(R.id.lebel_price)
    TextView lebel_price;


    @BindView(R.id.back_img)
    ImageView back_img;

    @BindView(R.id.et_cash)
    EditText et_cash;

    @BindView(R.id.et_visa_card)
    EditText et_visa_card;

    @BindView(R.id.layout_add_test)
    LinearLayout layout_add_test;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_confirm_payment);
        ButterKnife.bind(this);


        context = ConfirmPaymentActivity.this;
        activity = ConfirmPaymentActivity.this;
        connectionHelper = new ConnectionHelper(context);
        isInternetAvailable = connectionHelper.isConnectingToInternet();
        customDialog = new CustomDialog(context);
        if(GlobalData.ordertest!=null)
            lebel_price.setText(GlobalData.ordertest.getTotal_price());
        else{
            lebel_price.setText(GlobalData.totalAmmount);
        }
    }

    @OnClick({R.id.layout_add_test,R.id.et_visa_card,R.id.et_cash,R.id.back_img})
    public void onViewClicked(View view) {
        switch (view.getId()) {

            case R.id.layout_add_test:
                addTest();
                break;
            case R.id.et_visa_card:
                et_visa_card.setBackground(getResources().getDrawable(R.drawable.rounded_corners_blue));
                et_cash.setBackground(getResources().getDrawable(R.drawable.rounded_corner_edittext));

                break;
            case R.id.et_cash:
                et_cash.setBackground(getResources().getDrawable(R.drawable.rounded_corners_blue));
                et_visa_card.setBackground(getResources().getDrawable(R.drawable.rounded_corner_edittext));

                break;
            case R.id.back_img:
                finish();
                break;

        }
    }



    private void addTest() {
        HashMap<String, RequestBody> map = new HashMap<>();
        if(GlobalData.covid!=null) {
            map.put("first_name", RequestBody.create(MediaType.parse("text/plain"), GlobalData.covid.getFirst_name()));
            map.put("last_name", RequestBody.create(MediaType.parse("text/plain"), GlobalData.covid.getLast_name()));
            map.put("gender", RequestBody.create(MediaType.parse("text/plain"), GlobalData.covid.getGender()));
            map.put("date_of_birth", RequestBody.create(MediaType.parse("text/plain"), GlobalData.covid.getDate_of_birth()));
            map.put("father_name", RequestBody.create(MediaType.parse("text/plain"), GlobalData.covid.getFather_name()));
            map.put("husband_name", RequestBody.create(MediaType.parse("text/plain"), GlobalData.covid.getHusband_name()));
            map.put("address", RequestBody.create(MediaType.parse("text/plain"), GlobalData.covid.getAddress()));
            map.put("phone_number", RequestBody.create(MediaType.parse("text/plain"), GlobalData.covid.getMobile_number()));
            map.put("email", RequestBody.create(MediaType.parse("text/plain"), GlobalData.covid.getEmail()));
            map.put("airline", RequestBody.create(MediaType.parse("text/plain"), GlobalData.covid.getAirline()));
            map.put("flight_information_from", RequestBody.create(MediaType.parse("text/plain"), GlobalData.covid.getFrom_to()));
            map.put("flight_number", RequestBody.create(MediaType.parse("text/plain"), GlobalData.covid.getFlight_number()));
            map.put("booking_number", RequestBody.create(MediaType.parse("text/plain"), GlobalData.covid.getPnr()));
            map.put("flight_time", RequestBody.create(MediaType.parse("text/plain"), GlobalData.covid.getFlight_time()));
            map.put("ticket_number", RequestBody.create(MediaType.parse("text/plain"), GlobalData.covid.getTicket_number()));
            map.put("nationality", RequestBody.create(MediaType.parse("text/plain"), GlobalData.covid.getNationality()));
            map.put("passport_number", RequestBody.create(MediaType.parse("text/plain"), GlobalData.covid.getPassport_number()));
            map.put("cnic", RequestBody.create(MediaType.parse("text/plain"), GlobalData.covid.getCnic()));
            map.put("sample_collection_city", RequestBody.create(MediaType.parse("text/plain"), GlobalData.covid.getSample_city()));
            map.put("total_amount", RequestBody.create(MediaType.parse("text/plain"), GlobalData.totalAmmount));
            map.put("payment_type", RequestBody.create(MediaType.parse("text/plain"), "cash"));
            map.put("isPaid", RequestBody.create(MediaType.parse("text/plain"), "true"));
            map.put("isPromocode", RequestBody.create(MediaType.parse("text/plain"), "true"));
            map.put("promocode", RequestBody.create(MediaType.parse("text/plain"), "123"));
        }
        HashMap<String, RequestBody> mapp = new HashMap<>();
if(GlobalData.ordertest!=null) {

  /*  mapp.put("test_type", RequestBody.create(MediaType.parse("text/plain"), GlobalData.ordertest.getFor_whom()));
    mapp.put("test_time", RequestBody.create(MediaType.parse("text/plain"), GlobalData.ordertest.getDate_time()));
    mapp.put("test_name", RequestBody.create(MediaType.parse("text/plain"), GlobalData.ordertest.getTest_name()));

    mapp.put("test_charges", RequestBody.create(MediaType.parse("text/plain"), GlobalData.ordertest.getTotal_price()));
    mapp.put("description", RequestBody.create(MediaType.parse("text/plain"), GlobalData.ordertest.getCollection_place()));
    mapp.put("address", RequestBody.create(MediaType.parse("text/plain"), GlobalData.ordertest.getAddress()));*/

   /* mapp.put("test_by", RequestBody.create(MediaType.parse("text/plain"), GlobalData.ordertest.getFor_whom()));
    mapp.put("schedule_date_time", RequestBody.create(MediaType.parse("text/plain"), GlobalData.ordertest.getDate_time()));
    mapp. put("payment_type", RequestBody.create(MediaType.parse("text/plain"), "cash"));
    map.put("isPaid", RequestBody.create(MediaType.parse("text/plain"), "true"));
    map.put("isPromocode", RequestBody.create(MediaType.parse("text/plain"), "false"));
    map.put("promocode", RequestBody.create(MediaType.parse("text/plain"), ""));
    map.put("total_amount", RequestBody.create(MediaType.parse("text/plain"), "1540"));
    mapp.put("test_charges", RequestBody.create(MediaType.parse("text/plain"), GlobalData.ordertest.getTotal_price()));
    mapp.put("test_name", RequestBody.create(MediaType.parse(""), GlobalData.test_order_list));*/
add_order_model1.setSampleCollection(GlobalData.ordertest.getCollection_place());
    add_order_model1.setIsPaid(true);
    add_order_model1.setPaymentType("Cash");
    add_order_model1.setTestBy(GlobalData.ordertest.getFor_whom());
    add_order_model1.setScheduleDateTime(GlobalData.ordertest.getDate_time());
  //  add_order_model1.setScheduleDateTime( "2021-10-27 05:00:00");

    add_order_model1.setIsPromocode(false);
    add_order_model1.setPromocode("");
    add_order_model1.setTotalAmount(GlobalData.totalAmmount);
    add_order_model1.setIsPaid(true);
    add_order_model1.setTestName(GlobalData.Testlist);
    add_order_model1.setOthers( GlobalData.ordertest.getOthers());
    add_order_model1.setAddress(GlobalData.ordertest.getAddress());


    add_order_model1.setDoctorId(GlobalData.ordertest.getDocterid());
    add_order_model1.setBranchId(GlobalData.ordertest.getBranchid());




    add_order_model1.setLocationLat(GlobalData.Latitude);
    add_order_model1.setLocationLong(GlobalData.Longitude);
    add_order_model1.setCity(GlobalData.City);
    add_order_model1.setNearByLandMark(GlobalData.Landmark);


     /*   "schedule_date_time":"2021-10-27 05:00:00",
             "payment_type":"Cash",
             "total_amount":"9700",
             "isPaid":true,
             "isPromocode":true,
             "promocode":"x6sfgat",
             "address":"pvffffffffffff",
             "doctor_id":123,
             "branch_id":123,
             "card_number":"5123456789012346",
             "expiry_date":"12/2025",
             "cvv":"123"*/




}
        customDialog.show();
        String header =GlobalData.accessToken;
        Call<place_order_response> call;
        if(GlobalData.ordertest==null)
        {
            addCovidtest(map);
        }
        else {
            call = apiInterface.addOrder(header, add_order_model1);
            call.enqueue(new Callback<place_order_response>() {
                @Override
                public void onResponse(@NonNull Call<place_order_response> call, @NonNull Response<place_order_response> response) {
                    Log.d(TAG, "onResponse: " + response.isSuccessful());
                    if (response.isSuccessful()) {
                        GlobalData.ordertest = null;
                        GlobalData.covid = null;
                        GlobalData.Testlist = null;
                        GlobalData.totalAmmount = null;
                        //  Log.d(TAG, "onResponse: access_token: " + response.body().getTokenType() + " "+response.body().getAccessToken());
                        customDialog.dismiss();
                        Utils.displayMessage(activity, "Booking confirmed");
                        alertDialogBookingConfirm(response.body().getData().getPaymentID().toString());


                    } else {
                        try {
                            ServerError serverError = new Gson().fromJson(response.errorBody().charStream(), ServerError.class);
                            if (serverError != null) {
                                Utils.displayMessage(activity, getString(R.string.invalid_credentials));
                            } else {
                                Utils.displayMessage(activity, "User Do not Exists ");
                            }

                        } catch (JsonSyntaxException e) {
                            Utils.displayMessage(activity, getString(R.string.something_went_wrong));
                        }

                        customDialog.dismiss();
                    }
                    //customDialog.dismiss();
                }

                @Override
                public void onFailure(@NonNull Call<place_order_response> call, @NonNull Throwable t) {
                    customDialog.dismiss();
                    Log.d(TAG, "onFailure: " + t.getMessage());
                    Utils.displayMessage(activity, getString(R.string.something_went_wrong));
                }
            });
        }

    }

    public void alertDialogBookingConfirm(String code) {
        final AlertDialog.Builder alertDialog = new AlertDialog.Builder(context);
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View convertView = inflater.inflate(R.layout.dialog_booking, null);
        TextView textView = convertView.findViewById(R.id.code);
        textView.setText(code);
        alertDialog.setView(convertView);


        alertDialog
                .setPositiveButton(getResources().getString(R.string.okay), new DialogInterface.OnClickListener() {
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
       // TextView textView =alert.getButton(DialogInterface.code );
        pbutton.setTextColor(ContextCompat.getColor(ConfirmPaymentActivity.this, R.color.colorAccent));
        pbutton.setTypeface(pbutton.getTypeface(), Typeface.BOLD);

        /*if (!((ServiceFlow) this).isFinishing()) {
            alert.show();
        }*/
    }

    public  void addCovidtest(HashMap<String, RequestBody> map){
        customDialog.show();
        String header =GlobalData.accessToken;
        Call<Covid_response> call;
        call = apiInterface.addPreTest(header,map);
            call.enqueue(new Callback<Covid_response>() {
            @Override
            public void onResponse(@NonNull Call<Covid_response> call, @NonNull Response<Covid_response> response) {
                Log.d(TAG, "onResponse: " + response.isSuccessful());
                if (response.isSuccessful()) {
                    GlobalData.ordertest=null;
                    GlobalData.covid=null;
                    GlobalData.Testlist=null;
                    GlobalData.totalAmmount=null;
                    //  Log.d(TAG, "onResponse: access_token: " + response.body().getTokenType() + " "+response.body().getAccessToken());
                    customDialog.dismiss();
                    Utils.displayMessage(activity, "Booking confirmed");
                    alertDialogBookingConfirm(response.body().getData().getOrderid());


                } else {
                    try {
                        ServerError serverError = new Gson().fromJson(response.errorBody().charStream(), ServerError.class);
                        if (serverError !=null) {
                            Utils.displayMessage(activity, getString(R.string.invalid_credentials));
                        } else {
                            Utils.displayMessage(activity, "User Do not Exists ");
                        }

                    } catch (JsonSyntaxException e) {
                        Utils.displayMessage(activity, getString(R.string.something_went_wrong));
                    }

                    customDialog.dismiss();
                }
                //customDialog.dismiss();
            }

            @Override
            public void onFailure(@NonNull Call<Covid_response> call, @NonNull Throwable t) {
                customDialog.dismiss();
                Log.d(TAG, "onFailure: " + t.getMessage());
                Utils.displayMessage(activity, getString(R.string.something_went_wrong));
            }
        });

    }
}