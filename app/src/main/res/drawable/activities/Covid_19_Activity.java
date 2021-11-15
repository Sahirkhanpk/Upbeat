package activities;

import android.app.Activity;
import android.app.DatePickerDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.excellabs.R;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Locale;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import helper.ConnectionHelper;
import helper.CustomDialog;
import helper.GlobalData;
import model.Covid;

public class Covid_19_Activity extends AppCompatActivity {


    @BindView(R.id.first_name)
    EditText first_name;

    @BindView(R.id.last_name)
    EditText last_name;

    @BindView(R.id.select_gender)
    Spinner select_gender;

    @BindView(R.id.et_date_of_birth)
    EditText date_of_birth;

    @BindView(R.id.et_father_name)
    EditText father_name;

    @BindView(R.id.et_husband_name)
    EditText husband_name;

    @BindView(R.id.et_address)
    EditText address;

    @BindView(R.id.et_mobile_number)
    EditText mobile_number;

    @BindView(R.id.et_email)
    EditText email;

    @BindView(R.id.et_select_airline)
    Spinner select_airline;

    @BindView(R.id.et_from_to)
    EditText from_to;

    @BindView(R.id.et_flight_number)
    TextView flight_number;

    @BindView(R.id.et_flight_time)
    EditText flight_time;

    @BindView(R.id.et_ticket_number)
    EditText ticket_number;

    @BindView(R.id.et_pnr)
    EditText pnr;

    @BindView(R.id.et_nationality)
    EditText nationality;

    @BindView(R.id.et_passport_number)
    EditText passport_number;

    @BindView(R.id.et_cnic)
    EditText cnic;

    @BindView(R.id.et_select_sample_city)
    Spinner select_sample_city;




    @BindView(R.id.layout_continue)
    LinearLayout layout_continue;

    @BindView(R.id.back_img)
    ImageView backImg;

    @BindView(R.id.lebel_personal)
    TextView lebel_personal;




    @BindView(R.id.lebel_flight)
    TextView lebel_flight;


    @BindView(R.id.personal_information)
    LinearLayout personal_information;


    @BindView(R.id.layout_personal_information)
   LinearLayout layout_personal_information;


    @BindView(R.id.layout_flight_information)
    LinearLayout layout_flight_information;

    @BindView(R.id.flight_information)
    LinearLayout flight_information;

    @BindView(R.id.view_personal_info)
    View view_personal_info;

    @BindView(R.id.view_flight_info)
    View view_flight_info;


    Context context;
    Activity activity;
    ConnectionHelper connectionHelper;
    CustomDialog customDialog;
    boolean isInternetAvailable;
    final Calendar myCalendar = Calendar.getInstance();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_covid_19_);
        ButterKnife.bind(this);
        context = Covid_19_Activity.this;
        activity =Covid_19_Activity.this;
        connectionHelper = new ConnectionHelper(context);
        isInternetAvailable = connectionHelper.isConnectingToInternet();
        customDialog = new CustomDialog(context);




        DatePickerDialog.OnDateSetListener date = new DatePickerDialog.OnDateSetListener() {

            @Override
            public void onDateSet(DatePicker view, int year, int monthOfYear,
                                  int dayOfMonth) {
                // TODO Auto-generated method stub
                myCalendar.set(Calendar.YEAR, year);
                myCalendar.set(Calendar.MONTH, monthOfYear);
                myCalendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);
                updateLabel();
            }

        };

        date_of_birth.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub
                new DatePickerDialog(Covid_19_Activity.this, date, myCalendar
                        .get(Calendar.YEAR), myCalendar.get(Calendar.MONTH),
                        myCalendar.get(Calendar.DAY_OF_MONTH)).show();
            }
        });
    }



    @OnClick({ R.id.back_img,R.id.flight_information,R.id.personal_information,R.id.layout_continue})
    public void onViewClicked(View view) {
        switch (view.getId()) {

            case R.id.back_img:
                onBackPressed();
                break;
            case R.id.et_address:
                startActivity(new Intent(Covid_19_Activity.this,EditLocatoinActivity.class));
                break;

            case R.id.flight_information:
                if (first_name.getText().toString().isEmpty()){
                    Toast.makeText(this, "The First name field is required.", Toast.LENGTH_SHORT).show();
                    return;
                } else if(email.getText().toString().isEmpty()) {
                    Toast.makeText(this, "The email field is required.", Toast.LENGTH_SHORT).show();
                    return;
                }
                else if(last_name.getText().toString().isEmpty()) {
                    Toast.makeText(this, "The Last name field is required.", Toast.LENGTH_SHORT).show();
                    return;
                }
                else if(mobile_number.getText().toString().isEmpty()) {
                    Toast.makeText(this, "The Password field is required.", Toast.LENGTH_SHORT).show();
                    return;
                }
                else if(date_of_birth.getText().toString().isEmpty()) {
                    Toast.makeText(this, "The DOB field is required.", Toast.LENGTH_SHORT).show();
                    return;

                }
                else if(husband_name.getText().toString().isEmpty()) {
                    Toast.makeText(this, "The DOB field is required.", Toast.LENGTH_SHORT).show();
                    return;

                }
                else if(father_name.getText().toString().isEmpty()) {
                    Toast.makeText(this, "The DOB field is required.", Toast.LENGTH_SHORT).show();
                    return;

                }
                else if(address.getText().toString().isEmpty()) {
                    Toast.makeText(this, "The DOB field is required.", Toast.LENGTH_SHORT).show();
                    return;

                }
             view_flight_info.setBackgroundColor(getResources().getColor(R.color.colorPrimary));
                view_personal_info.setBackgroundColor(getResources().getColor(R.color.light_grey));
                layout_personal_information.setVisibility(View.GONE);
                layout_flight_information.setVisibility(View.VISIBLE);
                lebel_personal.setTextColor(getResources().getColor(R.color.light_grey));
                lebel_flight.setTextColor(getResources().getColor(R.color.colorPrimary));
                break;

            case R.id.personal_information:
                view_flight_info.setBackgroundColor(getResources().getColor(R.color.light_grey));
                view_personal_info.setBackgroundColor(getResources().getColor(R.color.colorPrimary));
                layout_personal_information.setVisibility(View.VISIBLE);
                lebel_personal.setTextColor(getResources().getColor(R.color.colorPrimary));
                lebel_flight.setTextColor(getResources().getColor(R.color.light_grey));
                layout_personal_information.setVisibility(View.VISIBLE);
                layout_flight_information.setVisibility(View.GONE);
                break;
            case R.id.layout_continue:

                if (from_to.getText().toString().isEmpty()){
                    Toast.makeText(this, "The Flight location from to  field is required.", Toast.LENGTH_SHORT).show();
                    return;
                } else if(flight_time.getText().toString().isEmpty()) {
                    Toast.makeText(this, "The flight time field is required.", Toast.LENGTH_SHORT).show();
                    return;
                }
                else if(ticket_number.getText().toString().isEmpty()) {
                    Toast.makeText(this, "The ticket number field is required.", Toast.LENGTH_SHORT).show();
                    return;
                }
                else if(pnr.getText().toString().isEmpty()) {
                    Toast.makeText(this, "The PNR field is required.", Toast.LENGTH_SHORT).show();
                    return;
                }
                else if(nationality.getText().toString().isEmpty()) {
                    Toast.makeText(this, "The nationality field is required.", Toast.LENGTH_SHORT).show();
                    return;

                }
                else if(passport_number.getText().toString().isEmpty()) {
                    Toast.makeText(this, "The passport number field is required.", Toast.LENGTH_SHORT).show();
                    return;

                }
                else if(cnic.getText().toString().isEmpty()) {
                    Toast.makeText(this, "The CNIC field is required.", Toast.LENGTH_SHORT).show();
                    return;

                }

                Covid covid= new Covid();
            covid.setFirst_name(first_name.getText().toString());
               covid.setLast_name(last_name.getText().toString());
              covid.setGender(select_gender.getSelectedItem().toString());
              covid.setDate_of_birth(date_of_birth.getText().toString());
              covid.setFather_name(father_name.getText().toString());
              covid.setHusband_name(husband_name.getText().toString());
              covid.setAddress(address.getText().toString());
               covid.setMobile_number(mobile_number.getText().toString());
               covid.setEmail(email.getText().toString());


              covid.setAirline(select_airline.getSelectedItem().toString());
        covid.setFrom_to(from_to.getText().toString());
        covid.setFlight_number(flight_number.getText().toString());
              covid.setFlight_time(flight_time.getText().toString());
               covid.setTicket_number(ticket_number.getText().toString());
               covid.setPnr(pnr.getText().toString());
              covid.setNationality(nationality.getText().toString());
               covid.setPassport_number(passport_number.getText().toString());
               covid.setCnic(cnic.getText().toString());
              covid.setSample_city(select_sample_city.getSelectedItem().toString());
              GlobalData.covid=covid;
                startActivity(new Intent(Covid_19_Activity.this, ConfirmPaymentActivity.class));
                finish();

                break;
        }
    }


    private void updateLabel() {
        String myFormat = "MM/dd/yy"; //In which you need put here
        SimpleDateFormat sdf = new SimpleDateFormat(myFormat, Locale.US);

        date_of_birth.setText(sdf.format(myCalendar.getTime()));
    }


    @Override
    protected void onResume() {
        super.onResume();
        if (GlobalData.location!= null) {
            address.setText(GlobalData.location);
        }

    }

}