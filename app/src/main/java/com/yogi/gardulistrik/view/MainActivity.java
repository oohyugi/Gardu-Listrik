package com.yogi.gardulistrik.view;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
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
    @BindView(R.id.btn_data)
    Button btnData;
    @BindView(R.id.btn_logout)
    Button btnLogout;
    @BindView(R.id.btn_admin)
    Button btnAdmin;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);
        if (PrefHelper.getUser(this)!=null){
            btnData.setVisibility(View.VISIBLE);
            btnLogout.setVisibility(View.VISIBLE);
            btnAdmin.setVisibility(View.GONE);

        }
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

    @OnClick(R.id.btn_admin)
    public void onClickAdmin(View v){
        LoginActivity.startThisActivity(this);
    }
    @OnClick(R.id.btn_data)
    public void onClickData(View v){
        AdminActivity.startThisActivity(this);
    }
    @OnClick(R.id.btn_logout)
    public void onClickLogout(View v){
       PrefHelper.saveUser(this,null);
        LoginActivity.startThisActivity(this);
        finish();

    }
    public static void startThisActivity(Context context){
        Intent intent = new Intent(context, MainActivity.class);
        context.startActivity(intent);
    }




}
