package com.yogi.gardulistrik.view;

import android.content.Context;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import com.yogi.gardulistrik.R;
import com.yogi.gardulistrik.api.ApiClient;
import com.yogi.gardulistrik.api.MyObservable;
import com.yogi.gardulistrik.api.mdl.AdminMdl;
import com.yogi.gardulistrik.api.mdl.BaseMdl;
import com.yogi.gardulistrik.api.mdl.LoginMdl;
import com.yogi.gardulistrik.api.mdl.TrafoMdl;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

public class AdminActivity extends AppCompatActivity {

    @BindView(R.id.ed_alamat)
    EditText edAlamat;
    String alamat;
    @BindView(R.id.ed_daya)
    EditText edDaya;
    String daya;
    @BindView(R.id.ed_latitude)
    EditText edLatitude;
    String latitude;
    @BindView(R.id.ed_longitude)
    EditText edLongitude;
    String longitude;
    @BindView(R.id.ed_singk_gardu)
    EditText edSingk;
    String singk;
    @BindView(R.id.ed_jenis_gardu)
    EditText edjenis;
    String jenis;
    @BindView(R.id.ed_nama_gardu)
    EditText edNama;
    String nama;
    @BindView(R.id.ed_idPenyulang)
    EditText edId;
    String id;@BindView(R.id.ed_feeder)
    Spinner edFeeder;
    String feeder;
    ArrayAdapter<String> spinnerAdapter;
    private List<String> itemSpinner = new ArrayList<>();
    private List<TrafoMdl> listMdl = new ArrayList<>();

    ApiClient mClient;
    TrafoMdl mdl;
    int status;
    String idPenyulang;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin);
        ButterKnife.bind(this);
        mClient = new ApiClient();
        mdl = (TrafoMdl) getIntent().getSerializableExtra("data");
        status = getIntent().getIntExtra("status",0);
     mClient.getmServices().getData(7).subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new MyObservable<BaseMdl<List<TrafoMdl>>>() {
                    @Override
                    protected void onError(String message) {
                        Toast.makeText(AdminActivity.this, message, Toast.LENGTH_SHORT).show();
                    }

                    @Override
                    protected void onSuccess(BaseMdl<List<TrafoMdl>> listBaseMdl) {
                        listMdl.addAll(listBaseMdl.data);
                        for (int i = 0; i < listBaseMdl.data.size(); i++) {
                            itemSpinner.add(listBaseMdl.data.get(i).nama_penyulang);
                        }

                        spinnerAdapter = new ArrayAdapter<String>(AdminActivity.this,android.R.layout.simple_spinner_dropdown_item,itemSpinner);

                        edFeeder.setAdapter(spinnerAdapter);

                        if (status==2){
                            edFeeder.setSelection(spinnerAdapter.getPosition(mdl.nama_penyulang));
                        }
                        edFeeder.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                            @Override
                            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                                idPenyulang =listMdl.get(i).id_penyulang;
                                Log.e("onItemSelected: ",idPenyulang );
                            }

                            @Override
                            public void onNothingSelected(AdapterView<?> adapterView) {

                            }
                        });

                    }
                });
       if (mdl!=null){

           edAlamat.setText(mdl.alamat);
           edDaya.setText(mdl.daya);
           edLatitude.setText(String.valueOf(mdl.latitude));
           edLongitude.setText(mdl.longitude+"");
           edSingk.setText(mdl.singk_gardu);
           edjenis.setText(mdl.jenis_gardu);
           edId.setText(mdl.id_penyulang);
           edNama.setText(mdl.nama_gardu);
       }

    }
    @OnClick(R.id.btn_login)
    public void login(View v){
        alamat = edAlamat.getText().toString();
        daya = edDaya.getText().toString();
        latitude = edLatitude.getText().toString();
        longitude = edLongitude.getText().toString();
        singk =edSingk.getText().toString();
        jenis  =edjenis.getText().toString();
        nama = edNama.getText().toString();
        id = edId.getText().toString();

        if (status==2){


            mClient.getmServices().postUpdateData(2,alamat,daya,latitude,longitude,singk,jenis,nama,idPenyulang,"0",mdl.id_gardu)
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(new MyObservable<String>() {
                        @Override
                        protected void onError(String message) {
                            Log.e( "onError: ",message );
                            Toast.makeText(AdminActivity.this,  message, Toast.LENGTH_SHORT).show();
                        }

                        @Override
                        protected void onSuccess(String s) {
                            Toast.makeText(AdminActivity.this,  s, Toast.LENGTH_SHORT).show();
                            finish();

                        }
                    });
        }else{
            mClient.getmServices().postData(1,alamat,daya,latitude,longitude,singk,jenis,nama,idPenyulang,"0")
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(new MyObservable<String>() {
                        @Override
                        protected void onError(String message) {
                            Log.e( "onError: ",message );
                            Toast.makeText(AdminActivity.this,  message, Toast.LENGTH_SHORT).show();
                        }

                        @Override
                        protected void onSuccess(String s) {
                            Toast.makeText(AdminActivity.this,  s, Toast.LENGTH_SHORT).show();
                            finish();

                        }
                    });
        }





    }
    public static void startThisActivity(Context context){
        Intent intent = new Intent(context, AdminActivity.class);
        context.startActivity(intent);
    }
    public static void startThisActivity(Context context, TrafoMdl mdl, int status){
        Intent intent = new Intent(context, AdminActivity.class);
        intent.putExtra("data",mdl);
        intent.putExtra("status",status);
        context.startActivity(intent);
    }
}
