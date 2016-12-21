package com.yogi.gardulistrik.view;

import android.content.Context;
import android.content.Intent;
import android.location.Location;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.TextView;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationListener;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapFragment;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.yogi.gardulistrik.R;
import com.yogi.gardulistrik.api.mdl.TrafoMdl;

import butterknife.BindView;
import butterknife.ButterKnife;

public class DetailActivity extends BaseActivity implements OnMapReadyCallback, GoogleApiClient.ConnectionCallbacks, GoogleApiClient.OnConnectionFailedListener, LocationListener {
    @BindView(R.id.id_trafo)
    TextView idTrafo;
    @BindView(R.id.penyulang)
    TextView penyulang;
    @BindView(R.id.alamat)
    TextView alamat;
    @BindView(R.id.daya)
    TextView daya;
    TrafoMdl mdl;
    private GoogleApiClient mGoogleApiClient;
    private Location mLocation;
    private GoogleMap map;
    private double lat,lng;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);
        ButterKnife.bind(this);
        getSupportActionBar().setTitle("Detail");
        MapFragment mapFragment = (MapFragment) getFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
        mGoogleApiClient= new GoogleApiClient.Builder(this)
                .addApi(LocationServices.API)
                .addConnectionCallbacks(this)
                .addOnConnectionFailedListener(this)
                .build();


        mdl = (TrafoMdl) getIntent().getSerializableExtra("DATA");
        idTrafo.setText("Id Trafo "+": "+ mdl.nama_gardu);
        penyulang.setText("Feeder "+": "+ mdl.nama_penyulang);
        alamat.setText("Alamat "+": "+mdl.alamat);
        daya.setText("Daya "+": "+mdl.daya);
        lat = mdl.latitude;
        lng = mdl.longitude;


    }

    public static void startThisActivity(Context context, TrafoMdl mdl){
        Intent intent = new Intent(context,DetailActivity.class);
        intent.putExtra("DATA",mdl);
        context.startActivity(intent);
    }

    @Override
    public void onConnected(@Nullable Bundle bundle) {

    }

    @Override
    public void onConnectionSuspended(int i) {

    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {

    }

    @Override
    public void onLocationChanged(Location location) {
        mLocation  = LocationServices.FusedLocationApi.getLastLocation(mGoogleApiClient);
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        this.map = googleMap;
        getDataLokasi(googleMap);


    }

    private void getDataLokasi(GoogleMap map) {
        LatLng badung = new LatLng(lat, lng);
        map.addMarker(new MarkerOptions()
                .position(badung)
                .icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_CYAN))
                .title(mdl.nama_gardu)
                .snippet(mdl.alamat));
        map.moveCamera(CameraUpdateFactory.newLatLngZoom(badung, 10));

    }
    @Override
    protected void onResume() {
        super.onResume();
        if (!mGoogleApiClient.isConnected()){
            mGoogleApiClient.connect();
        }

    }

}
