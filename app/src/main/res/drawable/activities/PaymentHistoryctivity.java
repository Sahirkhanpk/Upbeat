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

import adapter.ProductsAdapter;
import adapter.paymentHistoryAdapter;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class PaymentHistoryctivity extends AppCompatActivity {

    paymentHistoryAdapter PaymentHistoryAdapter;
    ProductsAdapter productsAdapter;

    @BindView(R.id.back_img)
    ImageView back;

    @BindView(R.id.payment_rv)
    RecyclerView payment_rv;


    List<String> orderList;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_payment_historyctivity);
        ButterKnife.bind(this);

        orderList = new ArrayList<>();
        orderList.add("All Reports");
        orderList.add("My Test Orders");
        orderList.add("My wallet");

        prepareAdapter();

    }

    @OnClick({R.id.back_img})
    public void onViewClicked(View view) {
        switch (view.getId()) {

            case R.id.back_img:
                finish();
                break;






        }
    }

    private void prepareAdapter() {


            PaymentHistoryAdapter = new paymentHistoryAdapter(PaymentHistoryctivity.this, orderList);
        payment_rv.setLayoutManager(new LinearLayoutManager(PaymentHistoryctivity.this, LinearLayoutManager.VERTICAL, false));
        payment_rv.setHasFixedSize(true);
    payment_rv.setAdapter(PaymentHistoryAdapter);
         // productsAdapter.setProductAdapterListener(PaymentHistoryctivity.this);

    }

}
