package org.alie.hungryloadview;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import org.alie.hungryloadview.view.HungryLoadView;

public class MainActivity extends AppCompatActivity {

    private HungryLoadView hlv;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initView();
        initListener();
        initData();
    }

    private void initView(){
        hlv = findViewById(R.id.hlv);
    }
    private void initListener(){}

    private void initData(){
        hlv.addBitmap(R.mipmap.v4);
        hlv.addBitmap(R.mipmap.v5);
        hlv.addBitmap(R.mipmap.v6);
        hlv.addBitmap(R.mipmap.v7);
        hlv.addBitmap(R.mipmap.v8);
        hlv.addBitmap(R.mipmap.v9);
        hlv.setDurationTime(700);
        hlv.startAnimation();
    }
}
