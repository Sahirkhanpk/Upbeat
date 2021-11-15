package activities;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.LinearLayout;

import androidx.appcompat.app.AppCompatActivity;

import com.example.excellabs.R;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class Password_chnaged_Activity extends AppCompatActivity {


    @BindView(R.id.layout_sign_in)
    LinearLayout sign_in;


    Context context;
    Activity activity;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_password_chnaged_);
        ButterKnife.bind(this);

        context = Password_chnaged_Activity.this;
        activity = Password_chnaged_Activity.this;
    }

    @OnClick({  R.id.layout_sign_in})
    public void onViewClicked(View view) {
        switch (view.getId()) {


            case R.id.layout_sign_in:
                startActivity(new Intent(context, LoginActivity.class).addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP));
                finish();
                break;



        }
    }

    @Override
    public void onBackPressed() {
        startActivity(new Intent(context, LoginActivity.class).addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP));
        finish();
    }
}