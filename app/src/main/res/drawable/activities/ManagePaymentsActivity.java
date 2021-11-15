package activities;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.LinearLayout;

import androidx.appcompat.app.AppCompatActivity;

import com.example.excellabs.R;

import butterknife.BindView;
import butterknife.OnClick;

public class ManagePaymentsActivity extends AppCompatActivity {


    @BindView(R.id.layout_add_card)
    LinearLayout layout_add_card;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_manage_payments);
    }

    @OnClick({R.id.layout_add_card})
    public void onViewClicked(View view) {
        switch (view.getId()) {

            case R.id.layout_add_card:
                startActivity(new Intent(ManagePaymentsActivity.this, AddCardActivity.class));
                break;


        }
    }
}