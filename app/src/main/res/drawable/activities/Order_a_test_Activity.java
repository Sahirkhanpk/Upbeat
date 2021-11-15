package activities;

import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.Gravity;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.excellabs.R;
import com.github.jhonnyx2012.horizontalpicker.DatePickerListener;
import com.github.jhonnyx2012.horizontalpicker.HorizontalPicker;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import org.joda.time.DateTime;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import adapter.OrderAddonsAdapter;
import adapter.Others_adapter;
import adapter.Slots_adapter;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import helper.ConnectionHelper;
import helper.CustomDialog;
import helper.GlobalData;
import model.Data_family_members;
import model.TestModel;
import model.family_member_model;
import model.getDocter_response;
import model.get_branches_response;
import model.test;
import network.ApiClient;
import network.ApiInterface;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


public class Order_a_test_Activity extends AppCompatActivity implements DatePickerListener, Slots_adapter.SlotAdapterListener {


int docid=0;
int branchid=0;

    private List<Data_family_members> familydata = null;
    ConnectionHelper connectionHelper;
    CustomDialog customDialog;
    boolean isInternetAvailable;
    ApiInterface apiInterface = ApiClient.getRetrofit().create(ApiInterface.class);
    @BindView(R.id.back_img)
    ImageView backImg;




    @BindView(R.id.view_location)
    View view_location;

    @BindView(R.id.radiogroupLocation)
    RadioGroup radiogroupLocation;

    @BindView(R.id.radio_at_lab)
    RadioButton radio_at_lab;

    @BindView(R.id.select_location)
    RadioButton select_location;

    List<String> other;
    List<String> selected_docters;
    List<String> selected_branches;
    List<String> Docter_list;
    List<String> Branches_list;

    String testType="Home";
String patient="";
    @BindView(R.id.radio_home_collection)
    RadioButton radio_home_collection;

    @BindView(R.id.layout_continue)
    LinearLayout layout_continue;

    @BindView(R.id.self_layout)
    LinearLayout self_layout;

    @BindView(R.id.layout_add_address)
    LinearLayout layout_add_address;




    @BindView(R.id.other_layout)
    LinearLayout other_layout;



    @BindView(R.id.user_address)
    TextView user_address;

    @BindView(R.id.txt_price)
    TextView txt_price;

    @BindView(R.id.txt_self)
    TextView txt_self;

    @BindView(R.id.txt_other)
    TextView txt_other;

    @BindView(R.id.date_time)
  TextView date_time;


    @BindView(R.id.select_test)
    RelativeLayout select_test;

    @BindView(R.id.select_docter)
    RelativeLayout select_docter;


    @BindView(R.id.select_branch)
    RelativeLayout select_branch;



    @BindView(R.id.addons_rv)
    RecyclerView addons_rv;


    @BindView(R.id.others_rv)
    RecyclerView others_rv;

    @BindView(R.id.refer_rv)
    RecyclerView refer_rv;


    @BindView(R.id.branch_rv)
    RecyclerView branch_rv;





    @BindView(R.id.slots_rv)
    RecyclerView slots_rv;
OrderAddonsAdapter orderAddonsAdapter;
    Others_adapter others_adapter,selected_docters_adapter,selected_branches_adapter;
Slots_adapter slots_adapter;
    int LAUNCH_SECOND_ACTIVITY = 1;
    ArrayList<String> scheduledtimes= new ArrayList<String>();
    List<TestModel> list;
    List<TestModel> listslots;

    String time="";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_order_a_test_);
        ButterKnife.bind(this);

        if(GlobalData.profileNew!=null)
if(GlobalData.profileNew.getData().getAddress()!=null)
        user_address.setText(GlobalData.profileNew.getData().getAddress());

        connectionHelper = new ConnectionHelper(Order_a_test_Activity.this);
        isInternetAvailable = connectionHelper.isConnectingToInternet();
        customDialog = new CustomDialog(Order_a_test_Activity.this);
        list = new ArrayList<>();

        GlobalData.ordertest=null;

        GlobalData.Testlist=null;
        GlobalData.totalAmmount=null;

        other= new ArrayList<>();
       selected_docters =new ArrayList<>();
       selected_branches= new ArrayList<>();
        Docter_list= new ArrayList<>();
        Branches_list= new ArrayList<>();
        getDocters();

        TestModel model= new TestModel();
        model.setPrice("50");
        model.setName("Blood");
        list.add(model);
        TestModel modell= new TestModel();
        modell.setPrice("500");
        modell.setName("Xray");
        list.add(modell);


        scheduledtimes.add("16:50");
      scheduledtimes.add("16:20");
        scheduledtimes.add("15:50");

        radio_home_collection.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener()
        {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
               if(isChecked==true) {
                   testType = "Home";
                   select_branch.setVisibility(View.GONE);
                   radiogroupLocation.setVisibility(View.VISIBLE);
                   layout_add_address.setVisibility(View.VISIBLE);
                   view_location.setVisibility(View.VISIBLE);
               }

            }

        });
        radio_at_lab.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener()
        {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if(isChecked==true) {
                    testType = "Lab";
                    select_branch.setVisibility(View.VISIBLE);
                    radiogroupLocation.setVisibility(View.GONE);
                    layout_add_address.setVisibility(View.GONE);
                    view_location.setVisibility(View.GONE);


                }
            }

        });

        HorizontalPicker picker = (HorizontalPicker) findViewById(R.id.datePicker);



        picker
                .setListener(this)
                .setDays(30)
                .setOffset(10)
.setTodayDateBackgroundColor(getResources().getColor(R.color.colorPrimaryBlueText))
                .setDateSelectedColor(getResources().getColor(R.color.colorPrimary))
                .setDateSelectedTextColor(Color.WHITE)
                .setMonthAndYearTextColor(Color.DKGRAY)
                .setTodayButtonTextColor(getResources().getColor(R.color.colorPrimaryBlueText))
                .setTodayDateTextColor(Color.WHITE)
                .setTodayDateBackgroundColor(getResources().getColor(R.color.colorPrimaryBlueText))
                .setUnselectedDayTextColor(getResources().getColor(R.color.colorPrimaryText))
                .setDayOfWeekTextColor(getResources().getColor(R.color.colorPrimaryText))
                .setUnselectedDayTextColor(getResources().getColor(R.color.colorPrimaryText))
                .showTodayButton(false)


                .init();


        // or on the View directly after init was completed
    picker.setBackground(getResources().getDrawable(R.drawable.rounded_corners_day));
        picker.setBackgroundColor(getResources().getColor(R.color.colorWhite));
        picker.setDate(new DateTime());


    }


    @OnClick({ R.id.back_img,R.id.other_layout,R.id.self_layout,R.id.select_test,R.id.layout_continue,R.id.layout_add_address,R.id.select_docter,R.id.select_branch})
    public void onViewClicked(View view) {
        switch (view.getId()) {

            case R.id.back_img:
                onBackPressed();
                break;
            case R.id.select_docter:
                showPopupEventTest("Select Docter",Docter_list);
                break;

            case R.id.select_branch:
                showPopupEventTest("Select Branch",Branches_list);
                break;


            case R.id.select_test:
                startActivity(new Intent(Order_a_test_Activity.this,Test_name_Activity.class));
                break;
            case R.id.other_layout:
                other_layout.setBackground(getResources().getDrawable(R.drawable.rounded_corners_blue));
                self_layout.setBackground(getResources().getDrawable(R.drawable.rounded_corners_whitle_small));
                txt_other.setTextColor(getResources().getColor(R.color.colorPrimaryBlueText));
                txt_self.setTextColor(getResources().getColor(R.color.colorPrimaryText));
                patient ="other";
                if(familydata!=null&&familydata.size()>0)
                {
                      if(other.size()==familydata.size())
                {
                    Toast.makeText(Order_a_test_Activity.this, "You have already selected all family List", Toast.LENGTH_LONG).show();

                }else
                      {Dialog_show_family_member();}
                }
               else {
                Toast.makeText(Order_a_test_Activity.this, "List is Empty", Toast.LENGTH_LONG).show();
            }
                break;
            case R.id.self_layout:
                other_layout.setBackground(getResources().getDrawable(R.drawable.rounded_corners_whitle_small));
                self_layout.setBackground(getResources().getDrawable(R.drawable.rounded_corners_blue));
                txt_self.setTextColor(getResources().getColor(R.color.colorPrimaryBlueText));
                txt_other.setTextColor(getResources().getColor(R.color.colorPrimaryText));
                patient ="self";
                break;
                case R.id.layout_continue:

                if (patient.equals("")){
                    Toast.makeText(this, "want to book for your self or other ?.", Toast.LENGTH_SHORT).show();
                    return;
                } else if(time.equals("")) {
                    Toast.makeText(this, "Select schedule date", Toast.LENGTH_SHORT).show();
                    return;
                }
                else if(txt_price.getText().toString().isEmpty()) {
                    Toast.makeText(this, "Please select the test", Toast.LENGTH_SHORT).show();
                    return;
                }
                else if(!select_location.isChecked()||user_address.getText().equals("")||user_address.getText().equals("null")||user_address.getText().equals(null)) {
                    Toast.makeText(this, "Please check selected location ", Toast.LENGTH_SHORT).show();
                    return;
                }

                String Test_name="";

                for(int i=0;i<GlobalData.test_order_list.size()-1;i++)
                {
                    Test_name = Test_name+GlobalData.test_order_list.get(i).getDesc();
                }



             /*   other.add("ali");
                    other.add("raza");
                    other.add("ahmahad");*/
                    test order_test = new test();
                order_test.setCollection_place(testType);
                order_test.setFor_whom(patient);
                order_test.setDate_time(time);
                order_test.setTotal_price(txt_price.getText().toString());
                order_test.setTest_name(Test_name);
                    order_test.setAddress(user_address.getText().toString());
                    order_test.setOthers(other);
                    order_test.setDocterid(docid);
                    order_test.setBranchid(branchid);

         GlobalData.ordertest =order_test;




                startActivity(new Intent(Order_a_test_Activity.this, ConfirmPaymentActivity.class));
                finish();

                break;

            case R.id.layout_add_address:
                startActivity(new Intent(Order_a_test_Activity.this,EditLocatoinActivity.class));
                break;
        }
    }

    @Override
    public void onDateSelected(DateTime dateSelected) {
        if(dateSelected!=null) {
           /* DateFormat dateFormat = android.text.format.DateFormat.getDateFormat(getApplicationContext());
            date_time.setText("Time: " + dateFormat.format(dateSelected.toDate()).toString());*/
            date_time.setText( dateSelected.toString("dd MMM"));
            time=dateSelected.toString("dd MMM");
          /*  date_time.setText( dateSelected.toString("dd MMM yyyy'T'HH:mm"));*/
           // SlotPicker.pickTime(Order_a_test_Activity.this, dateSelected.toString(),scheduledtimes,0);
        }

    }



    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode==2)
        {
            if(GlobalData.test_order_list!=null)
                if (orderAddonsAdapter== null) {
                    orderAddonsAdapter = new OrderAddonsAdapter(GlobalData.test_order_list, Order_a_test_Activity.this);
                    addons_rv.setLayoutManager(new LinearLayoutManager(Order_a_test_Activity.this, LinearLayoutManager.HORIZONTAL, false));


                    addons_rv.setHasFixedSize(true);
                    addons_rv.setAdapter(orderAddonsAdapter);}
        }


    }

    @Override
    protected void onResume() {
        super.onResume();

        if (GlobalData.location!= null) {
            user_address.setText(GlobalData.location);
        }


        listslots = new ArrayList<>();
        TestModel model= new TestModel();
        model.setPrice("50");
        model.setName("09:30 am");
        listslots.add(model);
        TestModel modell= new TestModel();
        modell.setPrice("500");
        modell.setName("10:30 am");
        listslots.add(modell);
        TestModel modelll= new TestModel();
        modelll.setPrice("500");
        modelll.setName("11:30 am");
        listslots.add(modelll);


        TestModel modellll= new TestModel();
        modellll.setPrice("14500");
        modellll.setName("12:30 pm");
        listslots.add(modellll);

        TestModel modelllll= new TestModel();
        modelllll.setPrice("17000");
        modelllll.setName("01:30 pm");
        listslots.add(modelllll);

        TestModel modellllll= new TestModel();
        modellllll.setPrice("17000");
        modellllll.setName("02:30 pm");
        listslots.add(modellllll);

        TestModel modelllllll= new TestModel();
        modelllllll.setPrice("17000");
        modelllllll.setName("03:30 pm");
        listslots.add(modelllllll);

        TestModel modellllllll= new TestModel();
        modellllllll.setPrice("17000");
        modellllllll.setName("04:30 pm");
        listslots.add(modelllllll);

        TestModel modelllllllll= new TestModel();
        modelllllllll.setPrice("17000");
        modelllllllll.setName("05:30 pm");
        listslots.add(modellllllll);

        TestModel modellllllllll= new TestModel();
        modellllllllll.setPrice("17000");
        modellllllllll.setName("06:30 pm");
        listslots.add(modelllllllll);

        TestModel modelllllllllll= new TestModel();
        modelllllllllll.setPrice("17000");
        modelllllllllll.setName("07:30 pm");
        listslots.add(modellllllllll);
        TestModel modellllllllllll= new TestModel();
        modellllllllllll.setPrice("17000");
        modellllllllllll.setName("08:30 pm");
        listslots.add(modelllllllllll);




        slots_adapter = new Slots_adapter(listslots, Order_a_test_Activity.this);
       slots_rv.setLayoutManager(new LinearLayoutManager(Order_a_test_Activity.this, LinearLayoutManager.HORIZONTAL, false));
       // slots_rv.setLayoutManager(new GridLayoutManager(this, 3));

        slots_rv.setHasFixedSize(true);
        slots_rv.setAdapter(slots_adapter);

        slots_adapter.setCategoryAdapterListener(this);
        orderAddonsAdapter = new OrderAddonsAdapter(GlobalData.Testlist, Order_a_test_Activity.this);
        others_adapter = new Others_adapter(other,Order_a_test_Activity.this);
        others_rv.setLayoutManager(new LinearLayoutManager(Order_a_test_Activity.this, LinearLayoutManager.HORIZONTAL, false));
        others_rv.setHasFixedSize(true);
        others_rv.setAdapter(others_adapter);



        selected_docters_adapter =  new Others_adapter(selected_docters,Order_a_test_Activity.this);

        refer_rv.setLayoutManager(new LinearLayoutManager(Order_a_test_Activity.this, LinearLayoutManager.HORIZONTAL, false));
        refer_rv.setHasFixedSize(true);
        refer_rv.setAdapter(selected_docters_adapter);




        selected_branches_adapter =  new Others_adapter(selected_branches,Order_a_test_Activity.this);

        branch_rv.setLayoutManager(new LinearLayoutManager(Order_a_test_Activity.this, LinearLayoutManager.HORIZONTAL, false));
        branch_rv.setHasFixedSize(true);
        branch_rv.setAdapter(selected_branches_adapter);






        if(GlobalData.Testlist!=null) {
            int  test_Price=0;
            for(int i=0;i<GlobalData.Testlist.size();i++)
            {
                test_Price=test_Price+Integer.valueOf(GlobalData.Testlist.get(i).getCost());
               // test_Price=test_Price+Integer.valueOf(100);

            }
            txt_price.setText("PKR "+String.valueOf(test_Price));
            GlobalData.totalAmmount=String.valueOf(test_Price);

            addons_rv.setLayoutManager(new LinearLayoutManager(Order_a_test_Activity.this, LinearLayoutManager.HORIZONTAL, false));


            addons_rv.setHasFixedSize(true);
            addons_rv.setAdapter(orderAddonsAdapter);
        }

    }

    @Override
    public void onCategoryClick(TestModel testModel) {
        if (testModel != null) {
            String date_= date_time.getText().toString();

            date_time.setText(time+", "+testModel.getName());
        }
    }



    private void getDocters(){


        if (customDialog != null)
            customDialog.show();


        String header = GlobalData.accessToken; /*SharedHelper.getKey(this, "token_type") + " " + SharedHelper.getKey(this, "access_token");*/
        Call<getDocter_response> call = apiInterface.getDocters(header);
        call.enqueue(new Callback<getDocter_response>() {
            @Override
            public void onResponse(@NonNull Call<getDocter_response> call, @NonNull Response<getDocter_response> response) {

                if (response.isSuccessful()) {
                    if(response.body().getData()!=null&&response.body().getData().size()>0) {


                       GlobalData.Docter_data =response.body().getData();
                        if(response.body().getData()!=null&&response.body().getData().size()>0)
                        {
                            for(int i=0;i<response.body().getData().size();i++){
                                Docter_list.add(response.body().getData().get(i).getDoctorName());
                            }
                        }
                        customDialog.cancel();
                        getBranches();

                        getFamilyMembers();



                    }else {
                        customDialog.cancel();
                        Toast.makeText(Order_a_test_Activity.this, "List is Empty", Toast.LENGTH_LONG).show();

                    }


                } else {
                    customDialog.cancel();
                    Gson gson = new GsonBuilder().create();

                    try {
                        JSONObject jObjError = new JSONObject(response.errorBody().string());

                        Toast.makeText(Order_a_test_Activity.this, jObjError.optString("message"), Toast.LENGTH_LONG).show();



                    } catch (IOException | JSONException e) {
                        // handle failure to read error
                        Toast.makeText(Order_a_test_Activity.this, e.toString(), Toast.LENGTH_SHORT).show();
                    }

                }
            }

            @Override
            public void onFailure(@NonNull Call<getDocter_response> call, @NonNull Throwable t) {
                customDialog.cancel();
                Toast.makeText(Order_a_test_Activity.this, R.string.something_went_wrong, Toast.LENGTH_SHORT).show();
            }
        });


    }


    private void getBranches(){


        if (customDialog != null)
            customDialog.show();


        String header = GlobalData.accessToken; /*SharedHelper.getKey(this, "token_type") + " " + SharedHelper.getKey(this, "access_token");*/
        Call<get_branches_response> call = apiInterface.getBranches(header);
        call.enqueue(new Callback<get_branches_response>() {
            @Override
            public void onResponse(@NonNull Call<get_branches_response> call, @NonNull Response<get_branches_response> response) {

                if (response.isSuccessful()) {
                    if(response.body().getData()!=null&&response.body().getData().size()>0) {


                          GlobalData.Branches_data=response.body().getData();
                        if(response.body().getData()!=null&&response.body().getData().size()>0)
                        {
                            for(int i=0;i<response.body().getData().size();i++){
                               Branches_list.add(response.body().getData().get(i).getName());
                            }
                        }
                        customDialog.cancel();



                    }else {
                        customDialog.cancel();

                        Toast.makeText(Order_a_test_Activity.this, "List is Empty", Toast.LENGTH_LONG).show();

                    }


                } else {
                    customDialog.cancel();
                    Gson gson = new GsonBuilder().create();

                    try {
                        JSONObject jObjError = new JSONObject(response.errorBody().string());

                        Toast.makeText(Order_a_test_Activity.this, jObjError.optString("message"), Toast.LENGTH_LONG).show();



                    } catch (IOException | JSONException e) {
                        // handle failure to read error
                        Toast.makeText(Order_a_test_Activity.this, e.toString(), Toast.LENGTH_SHORT).show();
                    }

                }
            }

            @Override
            public void onFailure(@NonNull Call<get_branches_response> call, @NonNull Throwable t) {
                customDialog.cancel();
                Toast.makeText(Order_a_test_Activity.this, R.string.something_went_wrong, Toast.LENGTH_SHORT).show();
            }
        });


    }



    private void getFamilyMembers() {


        if (customDialog != null)
            customDialog.show();


        String header =GlobalData.accessToken; /*SharedHelper.getKey(this, "token_type") + " " + SharedHelper.getKey(this, "access_token");*/
        Call<family_member_model> call = apiInterface.getFamilyMembers(header);
        call.enqueue(new Callback<family_member_model>() {
            @Override
            public void onResponse(@NonNull Call<family_member_model> call, @NonNull Response<family_member_model> response) {
                customDialog.cancel();
                if (response.isSuccessful()) {
                    if(response.body().getData()!=null&&response.body().getData().size()>0) {
                     familydata=response.body().getData();

                    }else {
                    /*    layout_members.setVisibility(View.GONE);
                        layout_add_family_member.setVisibility(View.VISIBLE);
                        newTaskRv.setVisibility(View.GONE);*/

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
                    Toast.makeText(Order_a_test_Activity.this, getString(R.string.something_went_wrong), Toast.LENGTH_SHORT).show();
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
            public void onFailure(@NonNull Call<family_member_model> call, @NonNull Throwable t) {
                customDialog.cancel();
                Toast.makeText(Order_a_test_Activity.this, R.string.something_went_wrong, Toast.LENGTH_SHORT).show();
            }
        });


    }


    public void Dialog_show_family_member(){
        // setup the alert builder
        AlertDialog.Builder builder = new AlertDialog.Builder(Order_a_test_Activity.this);

        String[] animals;
        animals=new String[familydata.size()];
      /*  if(familydata!=null&&familydata.size()>0)
        { animals=new String[familydata.size()];
      animals[0]="Tej";}else {
            animals=new String[2];
        }*/
// add a list
      /*  if(familydata!=null&&familydata.size()>0)
            for(int i=0;i<familydata.size();i++){
                if(other.size()==0)
          animals[i]=familydata.get(i).getFullname();
                else {
                    for(int s=0;s<other.size();s++)
                        for(int k=0;k<familydata.size();k++){
                            if(!other.get(s).equals(familydata.get(k).getFullname()))
                            {
                                animals[i]=familydata.get(i).getFullname();
                            }
                        }
                }
        }*/
        if(familydata!=null&&familydata.size()>0)
            for(int i=0;i<familydata.size();i++){

                    animals[i]=familydata.get(i).getFullname();

            }


        builder.setItems(animals, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                other.add(familydata.get(which).getFullname());
                others_adapter.notifyDataSetChanged();
              //  familydata.remove(which);




               /* switch (which) {
                    case 0: // horse
                    case 1: // cow
                    case 2: // camel
                    case 3: // sheep
                    case 4: // goat
                }*/
            }
        });

// create and show the alert dialog
        AlertDialog dialog = builder.create();
        dialog.show();
    }
    //////////
    private void showPopupEventTest(String txt,  List<String> list) {


// add a list
       /* if(GlobalData.Docter_data!=null&&GlobalData.Docter_data.size()>0)
            for(int i=0;i<GlobalData.Docter_data.size();i++){
               Docter_list.add(GlobalData.Docter_data.get(i).getDoctorName());
            }*/

        final Dialog dialog_data = new Dialog(Order_a_test_Activity.this);

        dialog_data.requestWindowFeature(Window.FEATURE_NO_TITLE);

        dialog_data.getWindow().setGravity(Gravity.CENTER);

        dialog_data.setContentView(R.layout.dialog_event);

        WindowManager.LayoutParams lp_number_picker = new WindowManager.LayoutParams();
        Window window = dialog_data.getWindow();
        lp_number_picker.copyFrom(window.getAttributes());

        lp_number_picker.width = WindowManager.LayoutParams.MATCH_PARENT;
        lp_number_picker.height = WindowManager.LayoutParams.WRAP_CONTENT;

        window.setGravity(Gravity.CENTER);
        window.setAttributes(lp_number_picker);

        TextView alertdialog_textview = (TextView) dialog_data.findViewById(R.id.alertdialog_textview);
        alertdialog_textview.setText(txt);
        alertdialog_textview.setHint(txt);

        Button dialog_cancel_btn = (Button) dialog_data.findViewById(R.id.dialog_cancel_btn);
        dialog_cancel_btn.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                if(dialog_data != null)
                {
                    dialog_data.dismiss();
                    dialog_data.cancel();
                }

            }
        });

        EditText filterText = (EditText) dialog_data.findViewById(R.id.alertdialog_edittext);
        ListView alertdialog_Listview = (ListView) dialog_data.findViewById(R.id.alertdialog_Listview);
        alertdialog_Listview.setChoiceMode(ListView.CHOICE_MODE_SINGLE);
        final ArrayAdapter<String>  adapter = new ArrayAdapter<String>(Order_a_test_Activity.this, android.R.layout.simple_list_item_1,list);
        alertdialog_Listview.setAdapter(adapter);
        alertdialog_Listview.setOnItemClickListener(new AdapterView.OnItemClickListener()
        {
            @Override
            public void onItemClick(AdapterView<?> a, View v, int position, long id)
            {
              //  Utility.hideKeyboard(Order_a_test_Activity.this,v);


                //data.setText(String.valueOf(a.getItemAtPosition(position)));


                if(dialog_data != null)
                { dialog_data.dismiss();
                    dialog_data.cancel();
                    if(txt!=null&&txt.equals("Select Docter")){
                        if(selected_docters!=null&&selected_docters.size()>0)
                        selected_docters.remove(0);
                        selected_docters.add(list.get(position));
                        selected_docters_adapter.notifyDataSetChanged();
                        docid=GlobalData.Docter_data.get(position).getId();
                    }else {
                        if(selected_branches!=null&&selected_branches.size()>0)
                        selected_branches.remove(0);
                        selected_branches.add(list.get(position));
                        selected_branches_adapter.notifyDataSetChanged();
                        branchid=GlobalData.Branches_data.get(position).getBranchid();
                    }
                }
            }
        });

        filterText.addTextChangedListener(new TextWatcher()
        {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after)
            {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count)
            {

            }

            @Override
            public void afterTextChanged(Editable s)
            {
                adapter.getFilter().filter(s);
            }
        });

        dialog_data.show();

    }

}