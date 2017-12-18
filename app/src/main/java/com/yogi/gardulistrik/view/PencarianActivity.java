package com.yogi.gardulistrik.view;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.yogi.gardulistrik.R;
import com.yogi.gardulistrik.api.ApiClient;
import com.yogi.gardulistrik.api.MyObservable;
import com.yogi.gardulistrik.api.mdl.BaseMdl;
import com.yogi.gardulistrik.api.mdl.TrafoMdl;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

public class PencarianActivity extends BaseActivity {

    ApiClient mClient;
    @BindView(R.id.recyleView)
    RecyclerView recyclerView;
    @BindView(R.id.imageView)
    ImageView btnClear;
    @BindView(R.id.inpQuery)
    EditText inputQuery;
    TrafoAdapter adapter;
    List<TrafoMdl> mData = new ArrayList<>();
    ProgressDialog progressDialog;
    @BindView(R.id.radioGardu)
    RadioButton radioGardu;
    @BindView(R.id.radioPenyulang)
            RadioButton radioPenyulang;
    int stat;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pencarian);
        ButterKnife.bind(this);
        initRecycleView();

        mClient = new ApiClient();
        progressDialog = ProgressDialog.show(this,null,"Please Wait..");
        callAPI(1);
//        stat = getIntent().getIntExtra("STAT",0);
        radioPenyulang.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                radioGardu.setChecked(false);
                radioPenyulang.setChecked(true);
                stat=1;
                callAPI(1);
            }
        });
        radioGardu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                radioPenyulang.setChecked(false);
                radioGardu.setChecked(true);
                callAPI(3);
                stat=2;
            }
        });
//        radioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
//            @Override
//            public void onCheckedChanged(RadioGroup group, int checkedId) {
//                switch (checkedId){
//                    case R.id.radioGardu:
//                        stat=2;
//                        callAPI(3);
//                        break;
//                    case R.id.radioPenyulang:
//                        stat =1;
//                        callAPI(1);
//                        break;
//                }
//            }
//        });
//        if (stat==1){
//
//        }else if (stat==2){
//
//        }
        btnClear.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                inputQuery.setText("");
            }
        });
        inputQuery.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView textView, int i, KeyEvent keyEvent) {

                if ((i == EditorInfo.IME_ACTION_SEARCH)) {
                    if (!TextUtils.isEmpty(inputQuery.getText().toString())) {
                        InputMethodManager inputMethodManager = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                        inputMethodManager.hideSoftInputFromWindow(inputQuery.getWindowToken(), 0);

                        if (stat==1){
                            callApiSearch(inputQuery.getText().toString(), 2);
                        }else if (stat==2){
                            callApiSearch(inputQuery.getText().toString(), 4);
                        }
                        mData.clear();
                    }
                    return true;
                }
                return false;

            }
        });


    }

    private void callApiSearch(String s, int i) {
        if (i==2){
            mClient.getmServices().getCariPenyulang(i, s)
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribeOn(Schedulers.newThread())
                    .subscribe(new MyObservable<BaseMdl<List<TrafoMdl>>>() {
                        @Override
                        protected void onError(String message) {
                            Log.e( "onError: ", message);
                            progressDialog.dismiss();
                        }

                        @Override
                        protected void onSuccess(BaseMdl<List<TrafoMdl>> listBaseMdl) {
                            progressDialog.dismiss();
                            if (listBaseMdl.message.equalsIgnoreCase("not found")){
                                Toast.makeText(PencarianActivity.this, listBaseMdl.message, Toast.LENGTH_SHORT).show();
                            }else{
                                Log.e( "onSuccess: ", listBaseMdl.data.get(0).alamat);
                                adapter.notifyDataSetChanged();
                                mData.addAll(listBaseMdl.data);
                            }

                        }
                    });
        }else if (i==4){
            mClient.getmServices().getCariTrafo(i, s)
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribeOn(Schedulers.newThread())
                    .subscribe(new MyObservable<BaseMdl<List<TrafoMdl>>>() {
                        @Override
                        protected void onError(String message) {
                            Log.e( "onError: ", message);
                            progressDialog.dismiss();
                        }

                        @Override
                        protected void onSuccess(BaseMdl<List<TrafoMdl>> listBaseMdl) {
                            progressDialog.dismiss();
                            Log.e( "onSuccess: ", listBaseMdl.data.get(0).alamat);
                            adapter.notifyDataSetChanged();
                            mData.addAll(listBaseMdl.data);
                        }
                    });

        }

    }

    private void initRecycleView() {
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        adapter= new TrafoAdapter(this, mData, new TrafoAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(int position, TrafoMdl model) {
               DetailActivity.startThisActivity(PencarianActivity.this,model);
            }
        });
        recyclerView.setAdapter(adapter);
    }

    private void callAPI(int i) {
        mClient.getmServices().getData(i)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.newThread())
                .subscribe(new MyObservable<BaseMdl<List<TrafoMdl>>>() {
                    @Override
                    protected void onError(String message) {
                        Log.e( "onError: ", message);
                        progressDialog.dismiss();
                    }

                    @Override
                    protected void onSuccess(BaseMdl<List<TrafoMdl>> listBaseMdl) {
                        progressDialog.dismiss();
                        Log.e( "onSuccess: ", listBaseMdl.data.get(0).alamat);
                        adapter.notifyDataSetChanged();
                        mData.addAll(listBaseMdl.data);
                    }
                });

    }

    public static void startThisActivity(Context context, int stat){
        Intent intent = new Intent(context, PencarianActivity.class);
        intent.putExtra("STAT",stat);
        context.startActivity(intent);
    }


}
