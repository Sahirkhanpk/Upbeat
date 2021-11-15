package activities;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SearchView;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.view.Gravity;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.upbeat.R;

import java.util.ArrayList;
import java.util.List;

import adapters.all_categries_adapter;
import adapters.sub_categries_adapter;
import butterknife.BindView;
import butterknife.ButterKnife;
import helper.GlobalData;
import models.Categories_Datum;
import models.Categories_response;
import models.Subcategory;

public class MainActivity extends AppCompatActivity implements all_categries_adapter.ProductAdapterListener,sub_categries_adapter.CategoryAdapterListener {

    @BindView(R.id.all_categries_rv)
    RecyclerView all_categries_rv;
    all_categries_adapter All_categries_adapter;
    sub_categries_adapter Sub_categries_adapter;


    @BindView(R.id.idSearchView)
    SearchView searchView;

    @BindView(R.id.filter_cat)
    ImageView filter_cat;


    @BindView(R.id.test_orders_rv)
    RecyclerView test_orders_rv;

    @BindView(R.id.category_txt)
    TextView category_txt;

    List<Categories_Datum> subcategoriesList;
    int subCatcheck=0;
int mainCat=0;
int searched=0;
    List<Subcategory> subcategories;
 boolean doubleBackToExitPressedOnce=false;
  //  Categories_Datum categories_datum;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);
        subcategories = new ArrayList<>();
        subcategoriesList=new ArrayList<>();

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



        if(GlobalData.categories_response.getData()!=null&&GlobalData.categories_response.getData().size()>0) {

    All_categries_adapter = new all_categries_adapter(MainActivity.this, GlobalData.categories_response.getData());
    all_categries_rv.setLayoutManager(new LinearLayoutManager(MainActivity.this, LinearLayoutManager.VERTICAL, false));
    all_categries_rv.setHasFixedSize(true);
    All_categries_adapter.setProductAdapterListener(this);
    all_categries_rv.setAdapter(All_categries_adapter);

}else {
    Toast.makeText(MainActivity.this, "List Empty", Toast.LENGTH_SHORT).show();
}

        if(GlobalData.categories_response.getData()!=null&&GlobalData.categories_response.getData().size()>0) {

            Sub_categries_adapter = new sub_categries_adapter(MainActivity.this, subcategories);

            GridLayoutManager gridLayoutManager = new GridLayoutManager(MainActivity.this,3,GridLayoutManager.VERTICAL,false);
           test_orders_rv.setLayoutManager(gridLayoutManager);

           test_orders_rv.setHasFixedSize(true);
            Sub_categries_adapter.setCatAdapterListener(this);
            test_orders_rv.setAdapter(Sub_categries_adapter);
        }else {
            Toast.makeText(MainActivity.this, "List Empty", Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public void onProductClick(int category,List<Subcategory> list,String catName) {
        if(list!=null&&list.size()>0) {
            filter_cat.setVisibility(View.VISIBLE);
            subcategoriesList.clear();
            Categories_Datum categories_datum= new Categories_Datum();
                categories_datum.setSubcategories(list);
                categories_datum.setName(category_txt.getText().toString());
                subcategoriesList.add(categories_datum);
            subCatcheck=1;
            subcategories.clear();
            subcategories.addAll(list);
            category_txt.setText(catName);
            Sub_categries_adapter.notifyDataSetChanged();
            mainCat = category;
        }else {
            Toast.makeText(MainActivity.this, "List Empty", Toast.LENGTH_SHORT).show();
        }

    }

    @Override
    public void onCatClick(int category,List<Subcategory> list,String catName) {
        filter_cat.setVisibility(View.GONE);

        if(list!=null&&list.size()>0){
            Categories_Datum categories_datum= new Categories_Datum();
            categories_datum.setSubcategories(list);
            categories_datum.setName(category_txt.getText().toString());

            subcategoriesList.add(categories_datum);
            subCatcheck++;
            subcategories.clear();
            subcategories.addAll(list);
            Sub_categries_adapter.notifyDataSetChanged();
            category_txt.setText(catName);
        } else {
            Toast.makeText(MainActivity.this, "List Empty", Toast.LENGTH_SHORT).show();
        }


    }

    @Override
    public void onProductDeleteClick(String category) {

    }


    @Override
    public void onBackPressed() {
      /*  if(searched==1){
            Sub_categries_adapter.filterListreset(GlobalData.categories_response.getData().get(mainCat).getSubcategories());
            searched=0;
        }else {*/
            if (subcategoriesList != null)
                if (subcategoriesList.size() > 1) {

                    subcategories.clear();
                    category_txt.setText(subcategoriesList.get(subCatcheck - 1).getName());
                    subcategoriesList.remove(subCatcheck - 1);
                    subCatcheck--;
                    subcategories.addAll(subcategoriesList.get(subCatcheck - 1).getSubcategories());

                    Sub_categries_adapter.notifyDataSetChanged();



                } else {
                    if (doubleBackToExitPressedOnce) {
                        super.onBackPressed();
                        return;
                    }

                    this.doubleBackToExitPressedOnce = true;
                    Toast.makeText(this, "Please click BACK again to exit", Toast.LENGTH_SHORT).show();

                    new Handler(Looper.getMainLooper()).postDelayed(new Runnable() {

                        @Override
                        public void run() {
                            doubleBackToExitPressedOnce = false;
                        }
                    }, 2000);
                    //  startActivity(new Intent(MainActivity.this, LoginActivity.class).addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP));
                    // finish();
                }
       /* }*/
    }


    private void filter(String text) {
        // creating a new array list to filter our data.
        List<Subcategory> filteredlist = new ArrayList<>();

        // running a for loop to compare elements.
        for (Subcategory item : subcategories) {
            // checking if the entered string matched with any item of our recycler view.
            if (item.getName().toLowerCase().contains(text.toLowerCase())) {
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
          //  Sub_categries_adapter.filterList(filteredlist);
            Categories_Datum categories_datum= new Categories_Datum();
            categories_datum.setSubcategories(filteredlist);
            categories_datum.setName(category_txt.getText().toString());

            subcategoriesList.add(categories_datum);
            subCatcheck++;
            subcategories.clear();
            subcategories.addAll(filteredlist);
            Sub_categries_adapter.notifyDataSetChanged();
           // category_txt.setText(catName);

        searched=1;


        }
    }
}