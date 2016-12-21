package com.yogi.gardulistrik.view;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.location.Location;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutCompat;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
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
import com.yogi.gardulistrik.api.mdl.BaseMdl;
import com.yogi.gardulistrik.api.mdl.TrafoMdl;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

public class PetaLokasiActivity extends BaseActivity implements OnMapReadyCallback, GoogleApiClient.ConnectionCallbacks, GoogleApiClient.OnConnectionFailedListener, LocationListener {
    private  GoogleMap map;
    private ApiClient mClient;
    private  List<TrafoMdl> mListData = new ArrayList<>();
    private GoogleApiClient mGoogleApiClient;
    private Location mLocation;
    LatLng arrLoc;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_peta_lokasi);

        getSupportActionBar().setTitle("Maps");
        mClient = new ApiClient();
        MapFragment mapFragment = (MapFragment) getFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
        mGoogleApiClient= new GoogleApiClient.Builder(this)
                .addApi(LocationServices.API)
                .addConnectionCallbacks(this)
                .addOnConnectionFailedListener(this)
                .build();

    }

    public static void startThisActivity(Context context){
        Intent intent = new Intent(context,PetaLokasiActivity.class);
        context.startActivity(intent);
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        this.map = googleMap;
        getDataLokasi();

    }

    private void getDataLokasi() {
        mClient.getmServices().getData(5)
                .subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new MyObservable<BaseMdl<List<TrafoMdl>>>() {
                    @Override
                    protected void onError(String message) {
                        Log.e( "onError: ",message );

                    }

                    @Override
                    protected void onSuccess(BaseMdl<List<TrafoMdl>> listBaseMdl) {
                        map.clear();
                        showMap(listBaseMdl.data);
                    }


                });
    }

    private void showMap(List<TrafoMdl> data) {
        for (int i = 0; i < data.size(); i++) {
            TrafoMdl loc =data.get(i);
             arrLoc = new LatLng(loc.latitude,loc.longitude);
            LatLng badung = new LatLng(-6.920145, 107.612255);
            map.addMarker(new MarkerOptions()
                    .position(arrLoc)
                    .icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_CYAN))
                    .title(loc.nama_gardu)
                    .snippet(loc.alamat));
            map.moveCamera(CameraUpdateFactory.newLatLngZoom(badung, 10));
            map.setMyLocationEnabled(true);
            map.getUiSettings().setMyLocationButtonEnabled(true);
            map.getUiSettings().setCompassEnabled(true);



        }
    }

    @Override
    public void onConnected(@Nullable Bundle bundle) {
        mLocation  = LocationServices.FusedLocationApi.getLastLocation(mGoogleApiClient);
        Log.e( "onConnected: ", String.valueOf(mLocation.getLatitude()));
//        if (mLocation!=null){
//            LatLng myLocation = new LatLng(mLocation.getLatitude(),mLocation.getLongitude());
//            map.addMarker(new MarkerOptions()
//                    .position(myLocation)
//                    .title("LOKASI SAYA")
//                    .icon(BitmapDescriptorFactory.fromResource(R.drawable.ic_loc)));
//        }

    }

    @Override
    public void onConnectionSuspended(int i) {

    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {

    }

    @Override
    public void onLocationChanged(Location location) {
        mLocation = location;
        Log.e( "onConnected: ", String.valueOf(location.getLatitude()));
        if (mLocation!=null){
            LatLng myLocation = new LatLng(mLocation.getLatitude(),mLocation.getLongitude());
            map.addMarker(new MarkerOptions()
                    .position(myLocation)
                    .title("LOKASI SAYA")
                    .icon(BitmapDescriptorFactory.fromResource(R.drawable.ic_loc)));
        }




    }

    @Override
    protected void onResume() {
        super.onResume();
        if (!mGoogleApiClient.isConnected()){
            mGoogleApiClient.connect();
        }

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mGoogleApiClient.disconnect();
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_location, menu);

        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == R.id.action_short){
            setupDialogArea();
        }
        return super.onOptionsItemSelected(item);
    }

    private void setupDialogArea() {
        final Dialog dialog = new Dialog(this);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.dialog_filter);
//        dialog.getWindow().setLayout(LinearLayoutCompat.LayoutParams.MATCH_PARENT, LinearLayoutCompat.LayoutParams.WRAP_CONTENT);

//        RecyclerView recyclerView = (RecyclerView)dialog.findViewById(R.id.dRecycleView);
//        recyclerView.setLayoutManager(new LinearLayoutManager(this));
//        adapter = new ProductListAdapter(DealerActivity.this, dummyModel(), new ProductListAdapter.OnItemClickListener() {
//            @Override
//            public void onItemClick(int position, ProductListMdl model) {
//
//            }
//        });
//        recyclerView.setAdapter(adapter);

        Button btnOk = (Button) dialog.findViewById(R.id.btn_ok);
        final EditText edJarak = (EditText) dialog.findViewById(R.id.ed_jarak);

        btnOk.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                map.clear();
                mClient.getmServices().getMapsTerdekat(6,mLocation.getLatitude(),mLocation.getLongitude(),edJarak.getText().toString() )
                        .subscribeOn(Schedulers.newThread())
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribe(new MyObservable<BaseMdl<List<TrafoMdl>>>() {
                            @Override
                            protected void onError(String message) {
                                Log.e( "onError: ",message );


                            }

                            @Override
                            protected void onSuccess(BaseMdl<List<TrafoMdl>> listBaseMdl) {

                                    Log.e( "onSuccess: ",listBaseMdl.data.get(0).alamat );
                                    showMap(listBaseMdl.data);


                                dialog.dismiss();
                            }


                        });
            }
        });


        dialog.show();
        dialog.setCancelable(true);
    }
}
