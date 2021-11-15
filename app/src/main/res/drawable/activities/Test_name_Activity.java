package activities;

import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SearchView;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.excellabs.R;

import java.util.ArrayList;
import java.util.List;

import adapter.TestAdapter;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import helper.GlobalData;
import model.TestName;
import model.excelLabTestmodel;

public class Test_name_Activity extends AppCompatActivity {


    @BindView(R.id.back_pointer)
    ImageView backImg;
TestAdapter testAdapter;

    @BindView(R.id.tests_rv)
    RecyclerView tests_rv;


    @BindView(R.id.idSearchView)
    SearchView searchView;

    @BindView(R.id.layout_add)
    LinearLayout layout_add;

    List<excelLabTestmodel> list;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_test_name_);
        ButterKnife.bind(this);
if(GlobalData.test_order_list!=null)

       /* list = new ArrayList<>();
        TestModel model= new TestModel();
        model.setPrice("800");
        model.setName("Blood CP");
        list.add(model);
        TestModel modell= new TestModel();
        modell.setPrice("1200");
        modell.setName("Blood Culture");
        list.add(modell);

        TestModel modelll= new TestModel();
        modelll.setPrice("1450");
        modelll.setName("Glucose");
        list.add(modelll);
        TestModel modellll= new TestModel();
        modellll.setPrice("14500");
        modellll.setName("Renal Functional Test");
        list.add(modellll);

        TestModel modelllll= new TestModel();
        modelllll.setPrice("17000");
        modelllll.setName("Liver Functional Test");
        list.add(modelllll);*/

    searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
        @Override
        public boolean onQueryTextSubmit(String query) {
            return false;
        }

        @Override
        public boolean onQueryTextChange(String newText) {
            // inside on query text change method we are
            // calling a method to filter our recycler view.
            filter(newText);
            return false;
        }
    });
        prepareAdapter();
    }

    @OnClick({ R.id.back_pointer,R.id.layout_add})
    public void onViewClicked(View view) {
        switch (view.getId()) {

            case R.id.back_pointer:
                onBackPressed();
                break;
            case R.id.layout_add:
             finish();
                break;


        }
    }

    private void prepareAdapter() {
        if (tests_rv != null) {



            testAdapter = new TestAdapter(Test_name_Activity.this,GlobalData.test_order_list);

            tests_rv.setLayoutManager(new LinearLayoutManager(Test_name_Activity.this, LinearLayoutManager.VERTICAL, false));}
            tests_rv.setHasFixedSize(true);
            tests_rv.setAdapter(testAdapter);
        //  productsAdapter.setProductAdapterListener(context);


    }




    private void filter(String text) {
        // creating a new array list to filter our data.
        ArrayList<TestName> filteredlist = new ArrayList<>();

        // running a for loop to compare elements.
        for (TestName item : GlobalData.test_order_list) {
            // checking if the entered string matched with any item of our recycler view.
            if (item.getDesc().toLowerCase().contains(text.toLowerCase())) {
                // if the item is matched we are
                // adding it to our filtered list.
                filteredlist.add(item);
            }
        }
        if (filteredlist.isEmpty()) {
            // if no item is added in filtered list we are
            // displaying a toast message as no data found.
            Toast.makeText(this, "No Data Found..", Toast.LENGTH_SHORT).show();
        } else {
            // at last we are passing that filtered
            // list to our adapter class.
            testAdapter.filterList(filteredlist);
        }
    }
}