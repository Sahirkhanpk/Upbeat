package activities;

import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.excellabs.R;

import java.util.ArrayList;
import java.util.List;

import adapter.MyTestOrderAdapter;
import adapter.Task_status_adapter;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import helper.GlobalData;
import model.TestModel;

public class MyTestOrderActivity extends AppCompatActivity implements  Task_status_adapter.SlotAdapterListener{

    Task_status_adapter slots_adapter;
    MyTestOrderAdapter myTestOrderAdapter;

    @BindView(R.id.status_rv)
    RecyclerView slots_rv;


    @BindView(R.id.test_orders_rv)
    RecyclerView test_orders_rv;


    @BindView(R.id.back_img)
    ImageView backImg;

    List<TestModel> listslots;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_test_order);
        ButterKnife.bind(this);


        listslots = new ArrayList<>();
        TestModel model= new TestModel();
        model.setPrice("50");
        model.setName(" All ");
        listslots.add(model);
        TestModel modell= new TestModel();
        modell.setPrice("500");
        modell.setName("In progress");
        listslots.add(modell);
        TestModel modelll= new TestModel();
        modelll.setPrice("500");
        modelll.setName("Scheduled");
        listslots.add(modelll);


        TestModel modellll= new TestModel();
        modellll.setPrice("14500");
        modellll.setName("Cancelled");
        listslots.add(modellll);

        TestModel modelllll= new TestModel();
        modelllll.setPrice("17000");
        modelllll.setName("Completed");
        listslots.add(modelllll);

        slots_adapter = new Task_status_adapter(listslots, MyTestOrderActivity.this);
        slots_rv.setLayoutManager(new LinearLayoutManager(MyTestOrderActivity.this, LinearLayoutManager.HORIZONTAL, false));
        // slots_rv.setLayoutManager(new GridLayoutManager(this, 3));

        slots_rv.setHasFixedSize(true);
        slots_rv.setAdapter(slots_adapter);

        slots_adapter.setCategoryAdapterListener(this);




       myTestOrderAdapter = new MyTestOrderAdapter(MyTestOrderActivity.this, GlobalData.patient_home_response.getData().getMytestorders());
        test_orders_rv.setLayoutManager(new LinearLayoutManager(MyTestOrderActivity.this, LinearLayoutManager.VERTICAL, false));
        test_orders_rv.setHasFixedSize(true);
        test_orders_rv.setAdapter(myTestOrderAdapter);
    }

    @Override
    public void onCategoryClick(TestModel category) {

    }

    @OnClick({R.id.back_img})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.back_img:
                onBackPressed();
                break;
        }


    }

}