package com.yogi.gardulistrik.view;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.drawable.Drawable;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationListener;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.Snackbar;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapFragment;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.BitmapDescriptor;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.yogi.gardulistrik.R;
import com.yogi.gardulistrik.api.mdl.CoordinateMdl;

import java.util.List;
import java.util.Locale;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class MapsActivity extends AppCompatActivity implements OnMapReadyCallback, GoogleApiClient.ConnectionCallbacks, GoogleApiClient.OnConnectionFailedListener, com.google.android.gms.location.LocationListener {

    private static final String TAG_LATLG ="ltlng" ;
    public static final String TAG_DATA_LOCATION ="loc" ;
    public static final int TAG_RESULT_LOCATION =12 ;
    protected GoogleApiClient mGoogleApiClient;
    private GoogleMap mMaps;
    @BindView(R.id.imgMarker)
    ImageView imgMarker;
    @BindView(R.id.container)
    RelativeLayout mContainer;
    MapFragment mapFragment;

    LatLng myLatlng;
    private Location mLocation;
    Snackbar mSnackbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps);
        ButterKnife.bind(this);

        mapFragment = (MapFragment) getFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
        mGoogleApiClient = new GoogleApiClient.Builder(this)
                .addApi(LocationServices.API)
                .addConnectionCallbacks(this)
                .addOnConnectionFailedListener(this)
                .build();

    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {

    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMaps = googleMap;
        if (mLocation!=null){

            mMaps.moveCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(mLocation.getLatitude(),mLocation.getLongitude()), 10));

            CameraPosition myPosition = new CameraPosition.Builder()
                    .target(new LatLng(mLocation.getLatitude(),mLocation.getLongitude())).zoom(15).
                            bearing(mMaps.getCameraPosition().bearing).build();
            mMaps.animateCamera(CameraUpdateFactory.newCameraPosition(myPosition), null);

            mMaps.setOnCameraIdleListener(new GoogleMap.OnCameraIdleListener() {
                @Override
                public void onCameraIdle() {

                }
            });
            mMaps.setOnCameraMoveStartedListener(new GoogleMap.OnCameraMoveStartedListener() {
                @Override
                public void onCameraMoveStarted(int reason) {
                    if (reason == GoogleMap.OnCameraMoveStartedListener.REASON_GESTURE) {

                        mMaps.clear();
                        imgMarker.setVisibility(View.VISIBLE);


                    }
                }
            });

        }

        mMaps.setMyLocationEnabled(true);
        mMaps.getUiSettings().setMyLocationButtonEnabled(true);
        mMaps.getUiSettings().setCompassEnabled(true);
    }

    @Override
    public void onLocationChanged(Location location) {
        mLocation = location;


    }



    @Override
    public void onConnected(@Nullable Bundle bundle) {
        mLocation  = LocationServices.FusedLocationApi.getLastLocation(mGoogleApiClient);
        Log.e("onConnected: ",mLocation.getLatitude()+"" );

    }

    @Override
    public void onConnectionSuspended(int i) {

    }
    @OnClick(R.id.imgMarker)
    public void onClickMarker(){

        final LatLng center =mMaps.getCameraPosition().target;
//        mMaps.addMarker(new MarkerOptions().position(center)).setIcon(bitmapDescriptorFromVector(MapsActivity.this,R.drawable.ic_pin));
//        mMaps.moveCamera(CameraUpdateFactory.newLatLng(center));
//        imgMarker.setVisibility(View.GONE);
        setMarkerPickup(center);



    }
    private void setMarkerPickup(LatLng latLng) {
        CameraPosition myPosition = new CameraPosition.Builder()
                .target(latLng).zoom(15).
                        bearing(mMaps.getCameraPosition().bearing).build();
        mMaps.animateCamera(CameraUpdateFactory.newCameraPosition(myPosition), null);
        mMaps.addMarker(new MarkerOptions().position(latLng)).setIcon(bitmapDescriptorFromVector(MapsActivity.this,R.drawable.ic_pin));
        imgMarker.setVisibility(View.GONE);
        showSnackbar(latLng);
    }

    public void showSnackbar(final LatLng address){

        mSnackbar.make(mContainer,"Your location: "+getCompleteAddressString(address.latitude,address.longitude), Snackbar.LENGTH_INDEFINITE).setAction("OK", new View.OnClickListener() {
            @Override
            public void onClick(View view) {

               CoordinateMdl model = new CoordinateMdl(address.latitude,address.longitude,getCompleteAddressString(address.latitude,address.longitude));
                Intent intent = new Intent();

                intent.putExtra(TAG_DATA_LOCATION,model);
                setResult(TAG_RESULT_LOCATION, intent);
                finish();
            }
        }).show();
    }

    private BitmapDescriptor bitmapDescriptorFromVector(Context context, int vectorResId) {
        Drawable vectorDrawable = ContextCompat.getDrawable(context, vectorResId);
        vectorDrawable.setBounds(0, 0, vectorDrawable.getIntrinsicWidth(), vectorDrawable.getIntrinsicHeight());
        Bitmap bitmap = Bitmap.createBitmap(vectorDrawable.getIntrinsicWidth(), vectorDrawable.getIntrinsicHeight(), Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(bitmap);
        vectorDrawable.draw(canvas);

        return BitmapDescriptorFactory.fromBitmap(bitmap);
    }

    private String getCompleteAddressString(double LATITUDE, double LONGITUDE) {
        String strAdd = "";
        Geocoder geocoder = new Geocoder(this, Locale.getDefault());
        try {
            List<Address> addresses = geocoder.getFromLocation(LATITUDE, LONGITUDE, 1);
            if (addresses != null) {
                Address returnedAddress = addresses.get(0);
                StringBuilder strReturnedAddress = new StringBuilder("");

                for (int i = 0; i <= returnedAddress.getMaxAddressLineIndex(); i++) {
                    strReturnedAddress.append(returnedAddress.getAddressLine(i)).append("\n");
                }
                strAdd = strReturnedAddress.toString();
                Log.w("My Current ", strReturnedAddress.toString());
            } else {
                Log.w("My Current", "No Address returned!");
            }
        } catch (Exception e) {
            e.printStackTrace();
            Log.w("My Current ", "Canont get Address!");
        }
        return strAdd;
    }
}
