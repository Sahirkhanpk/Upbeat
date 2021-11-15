package activities;


import android.Manifest;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.drawable.Drawable;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.DrawableRes;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.widget.SearchView;
import androidx.appcompat.widget.Toolbar;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.FragmentActivity;

import com.example.excellabs.R;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.location.places.ui.PlaceAutocompleteFragment;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptor;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.Polygon;
import com.google.android.gms.maps.model.PolygonOptions;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import butterknife.BindView;
import butterknife.ButterKnife;
import helper.Config;
import helper.CustomDialog;
import helper.GlobalData;
import network.ApiClient;
import network.ApiInterface;

public class EditLocatoinActivity extends FragmentActivity implements OnMapReadyCallback, GoogleApiClient.ConnectionCallbacks,
        GoogleApiClient.OnConnectionFailedListener {


public Marker markerName;

private GoogleMap mMap;
        GoogleApiClient mGoogleApiClient;
        CustomDialog customDialog;
private Toolbar viewToolbar;
private PolygonOptions polygonOptions;
private Polygon polygon;
private Boolean markerClicked;
private List<List<List<Double>>> coordinates;
private List<List<Double>> coordinatesInner;
private double latOne, longOne;
private int counter;
        EditText et_latitude;


        @BindView(R.id.imgCurrentLoc)
        ImageView imgCurrentLoc;

        EditText et_longitude;
        ProgressDialog progressDialog;
        ApiInterface apiInterface;
        int currentBranchId;
        LinearLayout save;
        List<String> spinnerArray;
        ArrayAdapter<String> adapter;
        Spinner spinner;
        TextView txt_address;
        TextView et_location;
        StringBuilder location=null;


        SearchView searchView;

        ImageView backImg;
        Marker marker;
        LinearLayout branch_lay;
        PlaceAutocompleteFragment autocompleteFragment;
        double clat,clon;
@Override
protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_locatoin);
        ButterKnife.bind(this);
        initViews();
/*        autocompleteFragment = (PlaceAutocompleteFragment)
                getFragmentManager().findFragmentById(R.id.place_autocomplete);

   *//*     autocompleteFragment.setBoundsBias(new LatLngBounds(
                new LatLng(-33.880490, 151.184363),
                new LatLng(-33.858754, 151.229596)));

        AutocompleteFilter typeFilter = new AutocompleteFilter.Builder()
                .setTypeFilter(AutocompleteFilter.TYPE_FILTER_ADDRESS)
                .build();

        autocompleteFragment.setFilter(typeFilter);*//*


        autocompleteFragment.setOnPlaceSelectedListener(new PlaceSelectionListener() {
            @Override
            public void onPlaceSelected(Place place) {
                // TODO: Get info about the selected place.
                Toast.makeText(getApplicationContext(), "Place:" + place.getName(), Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onError(Status status) {
                // TODO: Handle the error.
                Toast.makeText(getApplicationContext(), "error" + status, Toast.LENGTH_SHORT).show();
            }
        });*/
        }


@Override
public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;

        mMap.setOnMapClickListener(new GoogleMap.OnMapClickListener() {
@Override
public void onMapClick(LatLng latLng) {



        longOne = latLng.longitude;
        latOne = latLng.latitude;
        et_latitude.getText().clear();
        et_longitude.getText().clear();
        et_location.setText("");
        txt_address.setText("");
        String Lat = null;
        String Long = null;
        Lat = Double.toString(latLng.latitude);
        Long = Double.toString(latLng.longitude);



        et_latitude.setText(Lat);
        et_longitude.setText(Long);
        String ads = getAddress(latOne, longOne);
        et_location.setText(location.toString());
        txt_address.setText(ads);
        searchView.setQuery(ads, false);
        try {
        mMap.setMyLocationEnabled(true);
        mMap.getUiSettings().setZoomControlsEnabled(false);
        mMap.setMapType(GoogleMap.MAP_TYPE_TERRAIN);
        } catch (SecurityException e) {
        e.printStackTrace();
        }
      //  mMap.addMarker(new MarkerOptions().position(latLng).title(location));
        if (markerName!=null){  // marker name is declared as a gloval variable.
                markerName.setVisible(false);
                markerName.remove();
                markerName=null;
        }
        markerName=   mMap.addMarker(new MarkerOptions().position(latLng));
        mMap.animateCamera(CameraUpdateFactory.newCameraPosition(new CameraPosition.Builder().
        target(new LatLng(latOne, longOne)).zoom(14).build()));
        LatLng current = new LatLng(latOne, longOne);
      //  mMap.addMarker(new MarkerOptions().position(latLng));
        // mMap.addMarker(new MarkerOptions().position(current).title(location.toString()).icon(bitmapDescriptorFromVector( R.drawable.marker)));






        }
        });


        }






private void setGoogleApiClient() {

        if (mGoogleApiClient == null) {
        mGoogleApiClient = new GoogleApiClient.Builder(this)
        .addConnectionCallbacks(this)
        .addOnConnectionFailedListener(this)
        .addApi(LocationServices.API)
        .build();

        mGoogleApiClient.connect();
        }
        }

@Override
public void onConnected(@Nullable Bundle bundle) {

        try {

        Location mLastLocation = LocationServices.FusedLocationApi.getLastLocation(mGoogleApiClient);

        if (mLastLocation != null) {

        double latitude = mLastLocation.getLatitude();
        double longitude = mLastLocation.getLongitude();
        clat=latitude;
        clon=longitude;
        try {
        mMap.setMyLocationEnabled(true);

        mMap.getUiSettings().setZoomControlsEnabled(false);
        mMap.setMapType(GoogleMap.MAP_TYPE_TERRAIN);
        } catch (SecurityException e) {
        e.printStackTrace();
        }
        mMap.animateCamera(CameraUpdateFactory.newCameraPosition(new CameraPosition.Builder().
        target(new LatLng(clat, clon)).zoom(14).build()));
        LatLng current = new LatLng(clat, clon);
       // mMap.addMarker(new MarkerOptions().position(current).title(location.toString()).icon(bitmapDescriptorFromVector( R.drawable.marker)));

        }

        } catch (SecurityException ex) {
        ex.printStackTrace();
        }
        }

@Override
public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {

        }

@Override
public void onConnectionSuspended(int i) {

        }
private BitmapDescriptor bitmapDescriptorFromVector(@DrawableRes int vectorDrawableResourceId) {
        Drawable background = ContextCompat.getDrawable(this, R.drawable.marker);
        background.setBounds(0, 0, background.getIntrinsicWidth(), background.getIntrinsicHeight());
        Drawable vectorDrawable = ContextCompat.getDrawable(this, vectorDrawableResourceId);
        vectorDrawable.setBounds(40, 20, vectorDrawable.getIntrinsicWidth() + 40, vectorDrawable.getIntrinsicHeight() + 20);
        Bitmap bitmap = Bitmap.createBitmap(background.getIntrinsicWidth(), background.getIntrinsicHeight(), Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(bitmap);
        background.draw(canvas);
        vectorDrawable.draw(canvas);
        return BitmapDescriptorFactory.fromBitmap(bitmap);
        }

//********************** Checking google map permissions *********************
public void checkPermission() {

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
        if (ContextCompat.checkSelfPermission(this,
        Manifest.permission.ACCESS_FINE_LOCATION)
        == PackageManager.PERMISSION_GRANTED) {
        //Location Permission already granted
        setGoogleApiClient();
        } else {
        //Request Location Permission
        checkLocationPermission();
        }
        } else {
        setGoogleApiClient();
        }
        }

private void checkLocationPermission() {
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION)
        != PackageManager.PERMISSION_GRANTED) {

        // Should we show an explanation?
        if (ActivityCompat.shouldShowRequestPermissionRationale(this,
        Manifest.permission.ACCESS_FINE_LOCATION)) {

        // Show an explanation to the user *asynchronously* -- don't block
        // this thread waiting for the user's response! After the user
        // sees the explanation, try again to request the permission.
        new AlertDialog.Builder(this)
        .setTitle("Location Permission Needed")
        .setMessage("This app needs the Location permission, please accept to use location functionality")
        .setPositiveButton("OK", new DialogInterface.OnClickListener() {
@Override
public void onClick(DialogInterface dialogInterface, int i) {
        //Prompt the user once explanation has been shown
        ActivityCompat.requestPermissions(EditLocatoinActivity.this,
        new String[]{Manifest.permission.ACCESS_FINE_LOCATION},
        Config.MY_PERMISSIONS_REQUEST_LOCATION);
        }
        })
        .create()
        .show();


        } else {
        // No explanation needed, we can request the permission.

        ActivityCompat.requestPermissions(this,
        new String[]{Manifest.permission.ACCESS_FINE_LOCATION},
        Config.MY_PERMISSIONS_REQUEST_LOCATION);
        }
        }
        }

@Override
public void onRequestPermissionsResult(int requestCode,
        String permissions[], int[] grantResults) {
        switch (requestCode) {
        case Config.MY_PERMISSIONS_REQUEST_LOCATION: {
        // If request is cancelled, the result arrays are empty.
        if (grantResults.length > 0
        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {

        // permission was granted, yay! Do the
        // location-related task you need to do.
        if (ContextCompat.checkSelfPermission(this,
        Manifest.permission.ACCESS_FINE_LOCATION)
        == PackageManager.PERMISSION_GRANTED) {

        if (mGoogleApiClient == null) {
        setGoogleApiClient();
        }
        }

        } else {

        // permission denied, boo! Disable the
        // functionality that depends on this permission.
        Toast.makeText(this, "permission denied", Toast.LENGTH_LONG).show();
        }
        return;
        }

        // other 'case' lines to check for other
        // permissions this app might request
        }
        }






private String getAddress(double latitude, double longitude) {
        Geocoder geocoder;
        List<Address> addresses;
        StringBuilder result = new StringBuilder();
        geocoder = new Geocoder(EditLocatoinActivity.this, Locale.getDefault());

        try {

        addresses = geocoder.getFromLocation(latitude, longitude, 1);
        if ( addresses != null&&addresses.size()!=0) {// Here 1 represent max location result to returned, by documents it recommended 1 to 5
        String address = addresses.get(0).getAddressLine(0); // If any additional address line present than only, check with max available address lines by getMaxAddressLineIndex()
        String city = addresses.get(0).getLocality();
        String state = addresses.get(0).getAdminArea();
        String country = addresses.get(0).getCountryName();
        String postalCode = addresses.get(0).getPostalCode();
        String knownName = addresses.get(0).getFeatureName();
        String street = addresses.get(0).getAddressLine(0);
                GlobalData.Latitude= Double.toString(latitude);
                GlobalData.Longitude= Double.toString(longitude);
                GlobalData.City= city;
                GlobalData.Landmark= knownName;

        result.append(street).append(",").append(city).append(",").append(state).append(",").append(country).append(",");
        location = new StringBuilder();
        location.append(city).append(",").append(state).append(",").append(country).append(".");
        }


        } catch (IOException e) {
        e.printStackTrace();
        }

        return result.toString();
        }

/*private void saveLocation() {
        if (customDialog != null && !isDestroyed())
        customDialog.show();
        Call<GetBranch> call = null;
        double lat = Double.parseDouble(et_latitude.getText().toString());
        double longi = Double.parseDouble(et_longitude.getText().toString());
        call = apiInterface.updateLocation(currentBranchId, lat, longi, txt_address.getText().toString(), et_location.getText().toString(), SharedHelper.getKey(MyApplication.getInstance(), "access_token"));


        call.enqueue(new Callback<GetBranch>() {
@Override
public void onResponse(Call<GetBranch> call, Response<GetBranch> response) {
        customDialog.dismiss();
        if (response.body() != null) {
        Utils.displayMessage(MapsActivity.this, "Address updated successful");


        } else {
        if (response.code() == 401) {
        startActivity(new Intent(MapsActivity.this, LoginActivity.class).setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK));
        finish();
        }
        Utils.displayMessage(MapsActivity.this, getString(R.string.something_went_wrong));
        }
        }

@Override
public void onFailure(Call<GetBranch> call, Throwable t) {
        customDialog.dismiss();
        Utils.displayMessage(MapsActivity.this, getString(R.string.something_went_wrong));
        }
        });
        }*/

//*****************************************************************************
private void initViews() {
        et_latitude = findViewById(R.id.et_latitude);
        et_longitude = findViewById(R.id.et_longitude);
        searchView = findViewById(R.id.idSearchView);
        txt_address = findViewById(R.id.txt_address);
        et_location = findViewById(R.id.et_location);
        customDialog = new CustomDialog(this);

        location = new StringBuilder();
        save = findViewById(R.id.save);
        backImg = findViewById(R.id.back_img);
        backImg.setVisibility(View.VISIBLE);
        backImg.setOnClickListener(new View.OnClickListener() {
public void onClick(View v) {
        // TODO Auto-generated method stub
        onBackPressed();
        }
        });


        save.setOnClickListener(new View.OnClickListener() {
public void onClick(View v) {
        // your handler code here

      // saveLocation();
        GlobalData.location =txt_address.getText().toString();
        finish();

        }
        });

        checkPermission();
        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
        .findFragmentById(R.id.map);
        /////////
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
                @Override
                public boolean onQueryTextSubmit(String query) {
                        // on below line we are getting the
                        // location name from search view.
                        String location = searchView.getQuery().toString();

                        // below line is to create a list of address
                        // where we will store the list of all address.
                        List<Address> addressList = null;

                        // checking if the entered location is null or not.
                        if (location != null || !location.equals("")) {
                                // on below line we are creating and initializing a geo coder.
                                Geocoder geocoder = new Geocoder(EditLocatoinActivity.this);
                                try {
                                        // on below line we are getting location from the
                                        // location name and adding that location to address list.
                                        addressList = geocoder.getFromLocationName(location, 1);
                                } catch (IOException e) {
                                        e.printStackTrace();
                                }
                                // on below line we are getting the location
                                // from our list a first position.
                                Address address=null;
                                if(addressList!=null&&addressList.size()>0) {
                                        address = addressList.get(0);

                                        // on below line we are creating a variable for our location
                                        // where we will add our locations latitude and longitude.
                                        LatLng latLng = new LatLng(address.getLatitude(), address.getLongitude());
                                        String ads = getAddress(address.getLatitude(), address.getLongitude());
                                        et_location.setText(location.toString());
                                        txt_address.setText(ads);

                                        searchView.setQuery(ads, false);
                                        // on below line we are adding marker to that position.
                                        mMap.addMarker(new MarkerOptions().position(latLng).title(location));

                                        // below line is to animate camera to that position.
                                        mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(latLng, 14));

                                }
                        }
                        return false;
                }

                @Override
                public boolean onQueryTextChange(String newText) {
                        return false;
                }
        });
        // at last we calling our map fragment to update.
        mapFragment.getMapAsync(this);
        imgCurrentLoc.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                        markerClicked = true;

                                LatLng loc = new LatLng(clat, clon);
                                CameraPosition cameraPosition = new CameraPosition.Builder().target(loc).zoom(17).build();
                                mMap.moveCamera(CameraUpdateFactory.newCameraPosition(cameraPosition));

                }
        });

       /////////
      //  mapFragment.getMapAsync(this);

        //code, added
        markerClicked = false;
        coordinates = new ArrayList<>();
        coordinatesInner = new ArrayList<>();
        counter = 0;
        apiInterface = ApiClient.getRetrofit().create(ApiInterface.class);
        spinnerArray = new ArrayList<String>();
        adapter = new ArrayAdapter<String>(
        this, android.R.layout.simple_spinner_item, spinnerArray);


        }

/*extends FragmentActivity implements OnMapReadyCallback,
        LocationListener, GoogleMap.OnCameraMoveListener, GoogleMap.OnCameraIdleListener,
        GoogleApiClient.ConnectionCallbacks,
        GoogleApiClient.OnConnectionFailedListener {

    @BindView(R.id.txt_address)
    TextView addressEdit;

    @BindView(R.id.et_location)
    EditText landmark;
    @BindView(R.id.dummy_image_view)
    ImageView dummyImageView;
    @BindView(R.id.imgCurrentLoc)
    ImageView imgCurrentLoc;
    @BindView(R.id.save)
    Button save;


    boolean mapClicked = true;
    private String TAG = "SaveDelivery";
    private String addressHeader = "";
    private BottomSheetBehavior behavior;
    private GoogleMap mMap;
    private GoogleApiClient mGoogleApiClient;
    private LocationRequest mLocationRequest;
    private int value = 0;
    private Double crtLat;
    private Double crtLng;
    private Double srcLat;
    private Double srcLng;
    AnimatedVectorDrawableCompat avdProgress;
    FusedLocationProviderClient mFusedLocationClient;
    private int PLACE_AUTOCOMPLETE_REQUEST_CODE = 1;
    private int SET_COMPLETE = 1;


Adress address = null;
    ApiInterface apiInterface = ApiClient.getRetrofit().create(ApiInterface.class);
    Animation slide_down, slide_up;
    boolean isAddressSave = false;
    boolean isSkipVisible = false;
    Context context;
    LatLng latlng;
    CustomDialog customDialog;
    Retrofit retrofit;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_locatoin);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        ButterKnife.bind(this);
        context = EditLocatoinActivity.this;
        address = new Adress();
        customDialog = new CustomDialog(context);
        checkPermission();
        // address.setType("other");
        //Intialize Animation line
        // initializeAvd();


        //Load animation
        mFusedLocationClient = LocationServices.getFusedLocationProviderClient(this);
        slide_down = AnimationUtils.loadAnimation(context,
                R.anim.slide_down);
        slide_up = AnimationUtils.loadAnimation(context,
                R.anim.slide_up);

        isAddressSave = getIntent().getBooleanExtra("get_address", false);
        isSkipVisible = getIntent().getBooleanExtra("skip_visible", false);
      *//*  if (isSkipVisible)
            skipTxt.setVisibility(View.VISIBLE);
        else
            skipTxt.setVisibility(View.GONE);*//*


        // Initialize Places.
        Places.initialize(getApplicationContext(), getResources().getString(R.string.google_maps_key));

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
                buildGoogleApiClient();
                mFusedLocationClient.getLastLocation()
                        .addOnSuccessListener(this, new OnSuccessListener<Location>() {
                            @Override
                            public void onSuccess(Location location) {
                                // Got last known location. In some rare situations this can be null.
                                if (location != null) {
                                    // Logic to handle location object
                                    Bundle extras = getIntent().getExtras();
                                    if (extras != null) {
                                        String isEdit = extras.getString("edit");
                                        String my_address = extras.getString("place_address");
                                        if (isEdit == null && my_address == null) {
                                            getAddress(location.getLatitude(), location.getLongitude());
                                        }
                                    }
//                                    getAddress(context, location.getLatitude(), location.getLongitude());
                                }
                            }
                        });
            } else {
                //Request Location Permission
            }
        } else {
            buildGoogleApiClient();
            mFusedLocationClient.getLastLocation()
                    .addOnSuccessListener(this, new OnSuccessListener<Location>() {
                        @Override
                        public void onSuccess(Location location) {
                            // Got last known location. In some rare situations this can be null.
                            if (location != null) {
                                // Logic to handle location object
                                Bundle extras = getIntent().getExtras();
                                if (extras != null) {
                                    String isEdit = extras.getString("edit");
                                    String my_address = extras.getString("place_address");
                                    if (isEdit == null && my_address == null) {
                                        getAddress(location.getLatitude(), location.getLongitude());
                                    }
                                }
//                                getAddress(context, location.getLatitude(), location.getLongitude());
                            }
                        }
                    });
        }
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);

        //  View bottomSheet = findViewById(R.id.bottom_sheet);
        // behavior = BottomSheetBehavior.from(bottomSheet);

        //set state is expanded
        *//* behavior.setState(BottomSheetBehavior.STATE_EXPANDED);*//*
      *//*  dummyImageView.setVisibility(View.VISIBLE);

        behavior.setBottomSheetCallback(new BottomSheetBehavior.BottomSheetCallback() {
            @Override
            public void onStateChanged(@NonNull View bottomSheet, int newState) {
                // React to state change
                // to collapse to show
                if (BottomSheetBehavior.STATE_COLLAPSED == newState) {
                    dummyImageView.startAnimation(slide_down);
                    dummyImageView.setVisibility(View.GONE);

                } else if (BottomSheetBehavior.STATE_EXPANDED == newState) {
                    dummyImageView.setVisibility(View.VISIBLE);
                    dummyImageView.startAnimation(slide_up);

                }
            }

            @Override
            public void onSlide(@NonNull View bottomSheet, float slideOffset) {
                // React to dragging events
                Log.e("Slide :", "" + slideOffset);
                if (slideOffset < 0.9) {
                    dummyImageView.setVisibility(View.GONE);
                    dummyImageView.startAnimation(slide_down);
                }

            }

        });
*//*
    *//*    otherRadio.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                currentLocImg.setBackgroundResource(R.drawable.ic_other_marker);
                otherAddressTitleLayout.setVisibility(View.VISIBLE);
                Animation animation = AnimationUtils.loadAnimation(context, R.anim.slide_in_right);
                animation.setDuration(500);
                Animation animation2 = AnimationUtils.loadAnimation(context, R.anim.slide_out_left);
                animation2.setDuration(500);
                typeRadiogroup.startAnimation(animation2);
                otherAddressTitleLayout.startAnimation(animation);
                typeRadiogroup.setVisibility(View.GONE);
            }
        });

        typeRadiogroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup radioGroup, int i) {
                RadioButton radioButton = (RadioButton) radioGroup.findViewById(i);
                if (radioButton.getText().toString().toLowerCase().equals("home"))
                    currentLocImg.setBackgroundResource(R.drawable.ic_hoem_marker);
                else if (radioButton.getText().toString().toLowerCase().equals("work"))
                    currentLocImg.setBackgroundResource(R.drawable.ic_work_marker);
                else if (radioButton.getText().toString().equalsIgnoreCase(getResources().getString(R.string.other))) {
                    currentLocImg.setBackgroundResource(R.drawable.ic_other_marker);
                    otherAddressTitleLayout.setVisibility(View.VISIBLE);
                    Animation animation = AnimationUtils.loadAnimation(context, R.anim.slide_in_right);
                    animation.setDuration(500);
                    Animation animation2 = AnimationUtils.loadAnimation(context, R.anim.slide_out_left);
                    animation2.setDuration(500);
                    typeRadiogroup.startAnimation(animation2);
                    otherAddressTitleLayout.startAnimation(animation);
                    typeRadiogroup.setVisibility(View.GONE);
                }

                System.out.println("typeRadiogroup " + radioButton.getText().toString().toLowerCase());
                address.setType(radioButton.getText().toString().toLowerCase());

            }
        });*//*

    }

*//*    Runnable action = new Runnable() {
        @Override
        public void run() {
            avdProgress.stop();
            if (animationLineCartAdd != null)
                animationLineCartAdd.setVisibility(View.INVISIBLE);
        }
    };*//*

   *//* private void initializeAvd() {
        avdProgress = AnimatedVectorDrawableCompat.create(getApplicationContext(), R.drawable.avd_line);
        animationLineCartAdd.setBackground(avdProgress);
        repeatAnimation();
    }

    private void repeatAnimation() {
        animationLineCartAdd.setVisibility(View.VISIBLE);
        avdProgress.start();
        animationLineCartAdd.postDelayed(action, 1700); // Will repeat animation in every 1 second
    }*//*

    @Override
    public void onMapReady(GoogleMap googleMap) {

        try {
            boolean success = googleMap.setMapStyle(MapStyleOptions.loadRawResourceStyle(this, R.raw.keep));
            if (!success) {
                Log.i("Map:Style", "Style parsing failed.");
            } else {
                Log.i("Map:Style", "Style Applied.");
            }
        } catch (Resources.NotFoundException e) {
            Log.i("Map:Style", "Can't find style. Error: ");
        }

        mMap = googleMap;

        if (mMap != null) {
            mMap.getUiSettings().setCompassEnabled(false);
            mMap.setBuildingsEnabled(true);
            mMap.setOnCameraMoveListener(this);
            mMap.setOnCameraIdleListener(this);
            mMap.getUiSettings().setRotateGesturesEnabled(false);
            mMap.getUiSettings().setTiltGesturesEnabled(false);
        }

        Bundle extras = getIntent().getExtras();
        if (extras != null) {
            String isEdit = extras.getString("edit");
            String id = extras.getString("place_id");
            String latitude = extras.getString("latitude");
            String longitude = extras.getString("longitude");
            String my_address = extras.getString("place_address");

            if (my_address != null && latitude != null && longitude != null) {
                value = 1;
                mapClicked = false;
                address.setLatitude(Double.parseDouble(latitude));
                address.setLongitude(Double.parseDouble(longitude));
                address.setMapAddress(my_address);
                addressEdit.setText(my_address);
                LatLng latLng = new LatLng(Double.parseDouble(latitude), Double.parseDouble(longitude));
                CameraPosition cameraPosition = new CameraPosition.Builder().target(latLng).zoom(17).build();
                mMap.moveCamera(CameraUpdateFactory.newCameraPosition(cameraPosition));
            }


        }

    }


    protected synchronized void buildGoogleApiClient() {
        mGoogleApiClient = new GoogleApiClient.Builder(this)
                .addConnectionCallbacks(this)
                .addOnConnectionFailedListener(this)
//                .addApi(Places.GEO_DATA_API)
                .addApi(LocationServices.API)
                .build();
        mGoogleApiClient.connect();
    }

    @Override
    public void onLocationChanged(Location location) {
        System.out.println("onLocationChanged ");
        if (value == 0) {
            value = 1;
            if (address != null) {
                LatLng loc = new LatLng(location.getLatitude(), location.getLongitude());
                CameraPosition cameraPosition = new CameraPosition.Builder().target(loc).zoom(17).build();
                mMap.moveCamera(CameraUpdateFactory.newCameraPosition(cameraPosition));
            } else {
                LatLng loc = new LatLng(address.getLatitude(), address.getLongitude());
                CameraPosition cameraPosition = new CameraPosition.Builder().target(loc).zoom(17).build();
                mMap.moveCamera(CameraUpdateFactory.newCameraPosition(cameraPosition));
            }
            getAddress(location.getLatitude(), location.getLongitude());
        }
        crtLat = location.getLatitude();
        crtLng = location.getLongitude();
    }

    private void getAddress(double latitude, double longitude) {
        System.out.println("GetAddress " + latitude + " | " + longitude);
        try {
            Geocoder geocoder = new Geocoder(this, Locale.getDefault());
            List<Address> addresses = geocoder.getFromLocation(latitude, longitude, 1);
            if (addresses != null && addresses.size() > 0) {
                Address returnedAddress = addresses.get(0);
                StringBuilder strReturnedAddress = new StringBuilder();
                if (returnedAddress.getMaxAddressLineIndex() > 0) {
                    for (int j = 0; j < returnedAddress.getMaxAddressLineIndex(); j++) {
                        strReturnedAddress.append(returnedAddress.getAddressLine(j)).append("");
                    }
                } else {
                    strReturnedAddress.append(returnedAddress.getAddressLine(0)).append("");
                }
                if (mapClicked) {
                    addressEdit.setText(strReturnedAddress.toString());
                } else {
                    CameraPosition cameraPosition = new CameraPosition.Builder().target(latlng).zoom(17).build();
                    mMap.moveCamera(CameraUpdateFactory.newCameraPosition(cameraPosition));
                }

                Address obj = addresses.get(0);
                address.setCity(obj.getLocality());
                address.setState(obj.getAdminArea());
                address.setCountry(obj.getCountryName());
                address.setLatitude(obj.getLatitude());
                address.setLongitude(obj.getLongitude());
                address.setPincode(obj.getPostalCode());
                address.setStreet(obj.getThoroughfare());
                addressHeader = obj.getFeatureName();
                //SharedHelper.putKey(context, "pickup_address", strReturnedAddress.toString());
            } else {
                //getAddress(context, latitude, longitude);
            }
        } catch (Exception e) {
            e.printStackTrace();
            // getAddress(context, latitude, longitude);
        }
    }


    @Override
    public void onCameraIdle() {
        try {
            CameraPosition cameraPosition = mMap.getCameraPosition();
            srcLat = cameraPosition.target.latitude;
            srcLng = cameraPosition.target.longitude;
            // initializeAvd();
            if (mapClicked) {
                getAddress(srcLat, srcLng);
            }
         *//*   skipTxt.setAlpha(1);
            skipTxt.setClickable(true);
            skipTxt.setEnabled(true);*//*
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onCameraMove() {
        behavior.setState(BottomSheetBehavior.STATE_COLLAPSED);
        dummyImageView.setVisibility(View.GONE);
        addressEdit.setText(address.getLandmark());
        mapClicked = true;
    }

    @Override
    public void onConnected(@Nullable Bundle bundle) {
        mLocationRequest = new LocationRequest();
        mLocationRequest.setInterval(1000);
        mLocationRequest.setFastestInterval(1000);
        mLocationRequest.setPriority(LocationRequest.PRIORITY_BALANCED_POWER_ACCURACY);
        if (ContextCompat.checkSelfPermission(this,
                Manifest.permission.ACCESS_FINE_LOCATION)
                == PackageManager.PERMISSION_GRANTED) {
            LocationServices.FusedLocationApi.requestLocationUpdates(mGoogleApiClient, mLocationRequest, this);
        }
    }

    @Override
    public void onConnectionSuspended(int i) {

    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {

    }

    @Override
    protected void attachBaseContext(Context newBase) {
        super.attachBaseContext(CalligraphyContextWrapper.wrap(newBase));
    }


    @Override
    public void onBackPressed() {
        super.onBackPressed();
        if (behavior.getState() == BottomSheetBehavior.STATE_EXPANDED) {
            behavior.setState(BottomSheetBehavior.STATE_COLLAPSED);
        }
        finish();
        overridePendingTransition(R.anim.anim_nothing, R.anim.slide_out_right);
    }

    @OnClick({*//*R.id.backArrow,*//* R.id.save, R.id.imgCurrentLoc,*//* R.id.cancel_txt,*//**//* R.id.skip_txt*//**//*,*//**//* R.id.address*//*})
    public void onViewClicked(View view) {
        switch (view.getId()) {

            case R.id.imgCurrentLoc:
                mapClicked = true;
                if (crtLat != null && crtLng != null) {
                    LatLng loc = new LatLng(crtLat, crtLng);
                    CameraPosition cameraPosition = new CameraPosition.Builder().target(loc).zoom(17).build();
                    mMap.moveCamera(CameraUpdateFactory.newCameraPosition(cameraPosition));
                }
                break;


        }
    }

    private void findPlace() {
        try {
            List<Place.Field> fields = Arrays.asList(Place.Field.ID, Place.Field.NAME, Place.Field.ADDRESS, Place.Field.LAT_LNG);
            Intent intent = new Autocomplete.IntentBuilder(AutocompleteActivityMode.FULLSCREEN, fields).build(this);
            startActivityForResult(intent, PLACE_AUTOCOMPLETE_REQUEST_CODE);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == PLACE_AUTOCOMPLETE_REQUEST_CODE) {
//            if (resultCode == RESULT_OK) {
//                Place place = PlaceAutocomplete.getPlace(this, data);
//                LatLng loc = new LatLng(place.getLatLng().latitude, place.getLatLng().longitude);
//                mapClicked = false;
//                CameraPosition cameraPosition = new CameraPosition.Builder().target(loc).zoom(17).build();
//                mMap.moveCamera(CameraUpdateFactory.newCameraPosition(cameraPosition));
//
//                srcLat = loc.latitude;
//                srcLng = loc.longitude;
//
//                if (place.getAddress().toString().contains(place.getName().toString())){
//                    addressEdit.setText(place.getAddress());
//                }else{
//                    addressEdit.setText(place.getName()+", "+place.getAddress());
//                }
//
//                getAddress(srcLat, srcLng);
//            }

            if (resultCode == RESULT_OK) {
                Place place = Autocomplete.getPlaceFromIntent(data);
                Log.i(TAG, "Place: " + place.getName() + ", " + place.getId() + ", " + place.getLatLng());

                latlng = new LatLng(place.getLatLng().latitude, place.getLatLng().longitude);
                mapClicked = false;

                srcLat = latlng.latitude;
                srcLng = latlng.longitude;

                if (place.getAddress().toString().contains(place.getName().toString())) {
                    addressEdit.setText(place.getAddress());
                } else {
                    addressEdit.setText(place.getName() + ", " + place.getAddress());
                }

                getAddress(srcLat, srcLng);
            } else if (resultCode == AutocompleteActivity.RESULT_ERROR) {
                // TODO: Handle the error.
                Status status = Autocomplete.getStatusFromIntent(data);
                Log.i(TAG, status.getStatusMessage());
            } else if (resultCode == RESULT_CANCELED) {
                // The user canceled the operation.
            }
        }
    }

    public void checkPermission() {

        if (android.os.Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (ContextCompat.checkSelfPermission(this,
                    Manifest.permission.ACCESS_FINE_LOCATION)
                    == PackageManager.PERMISSION_GRANTED) {
                //Location Permission already granted
                setGoogleApiClient();
            } else {
                //Request Location Permission
                checkLocationPermission();
            }
        } else {
            setGoogleApiClient();
        }
    }

    private void setGoogleApiClient() {

        if (mGoogleApiClient == null) {
            mGoogleApiClient = new GoogleApiClient.Builder(this)
                    .addConnectionCallbacks(this)
                    .addOnConnectionFailedListener(this)
                    .addApi(LocationServices.API)
                    .build();

            mGoogleApiClient.connect();
        }
    }


    private void checkLocationPermission() {
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION)
                != PackageManager.PERMISSION_GRANTED) {

            // Should we show an explanation?
            if (ActivityCompat.shouldShowRequestPermissionRationale(this,
                    Manifest.permission.ACCESS_FINE_LOCATION)) {

                // Show an explanation to the user *asynchronously* -- don't block
                // this thread waiting for the user's response! After the user
                // sees the explanation, try again to request the permission.
                new AlertDialog.Builder(this)
                        .setTitle("Location Permission Needed")
                        .setMessage("This app needs the Location permission, please accept to use location functionality")
                        .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                //Prompt the user once explanation has been shown
                                ActivityCompat.requestPermissions(EditLocatoinActivity.this,
                                        new String[]{Manifest.permission.ACCESS_FINE_LOCATION},
                                        Config.MY_PERMISSIONS_REQUEST_LOCATION);
                            }
                        })
                        .create()
                        .show();


            } else {
                // No explanation needed, we can request the permission.

                ActivityCompat.requestPermissions(this,
                        new String[]{Manifest.permission.ACCESS_FINE_LOCATION},
                        Config.MY_PERMISSIONS_REQUEST_LOCATION);
            }
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode,
                                           String permissions[], int[] grantResults) {
        switch (requestCode) {
            case Config.MY_PERMISSIONS_REQUEST_LOCATION: {
                // If request is cancelled, the result arrays are empty.
                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {

                    // permission was granted, yay! Do the
                    // location-related task you need to do.
                    if (ContextCompat.checkSelfPermission(this,
                            Manifest.permission.ACCESS_FINE_LOCATION)
                            == PackageManager.PERMISSION_GRANTED) {

                        if (mGoogleApiClient == null) {
                            setGoogleApiClient();
                        }
                    }

                } else {

                    // permission denied, boo! Disable the
                    // functionality that depends on this permission.
                    Toast.makeText(this, "permission denied", Toast.LENGTH_LONG).show();
                }
                return;
            }

            // other 'case' lines to check for other
            // permissions this app might request
        }
    }*/
}
