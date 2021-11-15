package activities;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;

import androidx.appcompat.app.AppCompatActivity;

import com.example.excellabs.R;

import butterknife.BindView;
import butterknife.ButterKnife;

public class Stuff_Password_Updated_successfull extends AppCompatActivity {


    @BindView(R.id.img_pass_changed)
    ImageView img_pass_changed;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_stuff__password__updated_successfull);
        ButterKnife.bind(this);
        img_pass_changed.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(Stuff_Password_Updated_successfull.this,LoginActivity.class).addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP));
                finish();
            }
        });
    }
}