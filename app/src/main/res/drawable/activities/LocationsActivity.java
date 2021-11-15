package activities;

import android.os.Bundle;
import android.view.View;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ImageView;

import androidx.appcompat.app.AppCompatActivity;

import com.example.excellabs.R;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class LocationsActivity extends AppCompatActivity {

    @BindView(R.id.locations)
    WebView locations;


    @BindView(R.id.back_img)
    ImageView back_img;




    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_locations);
        ButterKnife.bind(this);


        locations.getSettings().setLoadsImagesAutomatically(true);
        locations.getSettings().setJavaScriptEnabled(true);
        locations.setScrollBarStyle(View.SCROLLBARS_INSIDE_OVERLAY);


        locations.getSettings().setSupportZoom(true);
        locations.getSettings().setBuiltInZoomControls(true); // allow pinch to zooom
        locations.getSettings().setDisplayZoomControls(false); // disable the default zoom controls on the page


        // Configure the client to use when opening URLs
        locations.setWebViewClient(new WebViewClient());
        // Load the initial URL
        locations.loadUrl("https://excel-labs.com/Home/DynamicView?ViewName=Location");
    }

    @OnClick({R.id.back_img})
    public void onViewClicked(View view) {
        switch (view.getId()) {

            case R.id.back_img:
                finish();
                break;






        }
    }
}