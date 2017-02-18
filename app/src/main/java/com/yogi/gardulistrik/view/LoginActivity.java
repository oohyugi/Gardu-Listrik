package com.yogi.gardulistrik.view;

import android.content.Context;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.yogi.gardulistrik.R;
import com.yogi.gardulistrik.api.ApiClient;
import com.yogi.gardulistrik.api.ApiServices;
import com.yogi.gardulistrik.api.MyObservable;
import com.yogi.gardulistrik.api.mdl.BaseMdl;
import com.yogi.gardulistrik.api.mdl.LoginMdl;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

public class LoginActivity extends AppCompatActivity {

    @BindView(R.id.ed_username)
    EditText edUsername;
    @BindView(R.id.ed_password)
    EditText edPassword;
    @BindView(R.id.btn_login)
    Button btnLogin;
    ApiClient mClient;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        ButterKnife.bind(this);
       mClient = new ApiClient();

    }
    @OnClick(R.id.btn_login)
    public void login(View v){
        mClient.getmServices().getLogin(4,edUsername.getText().toString(),edPassword.getText().toString())
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new MyObservable<BaseMdl<LoginMdl>>() {
                    @Override
                    protected void onError(String message) {
                        Toast.makeText(LoginActivity.this, message, Toast.LENGTH_SHORT).show();
                    }

                    @Override
                    protected void onSuccess(BaseMdl<LoginMdl> loginMdlBaseMdl) {
                        Toast.makeText(LoginActivity.this, loginMdlBaseMdl.message, Toast.LENGTH_SHORT).show();
                        PrefHelper.saveUser(LoginActivity.this,loginMdlBaseMdl.data);
                        MainActivity.startThisActivity(LoginActivity.this);
                        finish();

                    }
                });



    }
    public static void startThisActivity(Context context){
        Intent intent = new Intent(context, LoginActivity.class);
        context.startActivity(intent);
    }
}
