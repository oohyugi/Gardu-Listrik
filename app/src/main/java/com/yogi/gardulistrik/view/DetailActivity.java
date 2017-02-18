package com.yogi.gardulistrik.view;

import android.content.Context;
import android.content.Intent;
import android.location.Location;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

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
import com.yogi.gardulistrik.api.ApiClient;
import com.yogi.gardulistrik.api.MyObservable;
import com.yogi.gardulistrik.api.mdl.TrafoMdl;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

public class DetailActivity extends BaseActivity implements OnMapReadyCallback, GoogleApiClient.ConnectionCallbacks, GoogleApiClient.OnConnectionFailedListener, LocationListener {
    @BindView(R.id.id_trafo)
    TextView idTrafo;
    @BindView(R.id.penyulang)
    TextView penyulang;
    @BindView(R.id.alamat)
    TextView alamat;
    @BindView(R.id.daya)
    TextView daya;
    @BindView(R.id.btn_edit)
    Button btnEdit;
    @BindView(R.id.btn_hapus)
    Button btnHapus;
    ApiClient mClient;

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
        mClient = new ApiClient();
        MapFragment mapFragment = (MapFragment) getFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
        mGoogleApiClient= new GoogleApiClient.Builder(this)
                .addApi(LocationServices.API)
                .addConnectionCallbacks(this)
                .addOnConnectionFailedListener(this)
                .build();

if (PrefHelper.getUser(this)!=null){
    btnEdit.setVisibility(View.VISIBLE);
    btnHapus.setVisibility(View.VISIBLE);
}
        mdl = (TrafoMdl) getIntent().getSerializableExtra("DATA");
        idTrafo.setText("Id Trafo "+": "+ mdl.nama_gardu);
        penyulang.setText("Penyulang "+": "+ mdl.nama_penyulang);
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

    @OnClick(R.id.btn_hapus)
    public void btnHapus(View v){
        mClient.getmServices().getHapus(3,mdl.id_gardu).subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new MyObservable<String>() {
                    @Override
                    protected void onError(String message) {
                        Toast.makeText(DetailActivity.this, message, Toast.LENGTH_SHORT).show();
                    }

                    @Override
                    protected void onSuccess(String s) {
                        Toast.makeText(DetailActivity.this, s, Toast.LENGTH_SHORT).show();
                        finish();

                    }
                });


    }
    @OnClick(R.id.btn_edit)
    public void btnEdit(View v){
        AdminActivity.startThisActivity(this,mdl,2);

    }

}
