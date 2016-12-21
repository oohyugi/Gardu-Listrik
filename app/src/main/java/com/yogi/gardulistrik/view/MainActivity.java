package com.yogi.gardulistrik.view;

import android.app.Dialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutCompat;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.ImageButton;

import com.yogi.gardulistrik.R;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class MainActivity extends AppCompatActivity {
    @BindView(R.id.btn_maps)
    Button btnMaps;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);
    }

    @OnClick(R.id.btn_maps)
    public void onClickMaps(View v){
        PetaLokasiActivity.startThisActivity(this);
    }
    @OnClick(R.id.btn_penyulang)
    public void onClickPenyulang(View v){
       PencarianActivity.startThisActivity(this,1);
    }

    @OnClick(R.id.btn_trafo)
    public void onClickTrafo(View v){
        PencarianActivity.startThisActivity(this,2);
    }




}
