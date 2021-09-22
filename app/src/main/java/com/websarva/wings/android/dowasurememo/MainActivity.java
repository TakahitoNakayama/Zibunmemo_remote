package com.websarva.wings.android.dowasurememo;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

public class MainActivity extends AppCompatActivity {

    private Databasehelper _helper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        _helper = new Databasehelper(MainActivity.this);

        //メイン画面で日時を表示するための処理
        TextView dateOutput = findViewById(R.id.date_output);
        Calendar ca = Calendar.getInstance();
        Date da = ca.getTime();
        SimpleDateFormat dateFormat = new SimpleDateFormat
                ("y年 M月 d日 (E) H時 m分");
        dateOutput.setText(dateFormat.format(da));

    }

    @Override
    public void onDestroy() {
        _helper.close();
        super.onDestroy();

    }

    //それぞれのメモへの画面遷移処理
    public void onClick(View v) {

        switch (v.getId()) {
            case R.id.bt_size:
                Intent intent = new Intent(MainActivity.this, SizeMemo.class);
                startActivity(intent);
                break;
            case R.id.bt_date:
                intent = new Intent(MainActivity.this, DateMemo.class);
                startActivity(intent);
                break;
            case R.id.bt_addres:
                intent = new Intent(MainActivity.this, AddressMemo.class);
                startActivity(intent);
                break;
            case R.id.bt_car:
                intent = new Intent(MainActivity.this, CarMemo.class);
                startActivity(intent);
                break;
            case R.id.bt_update:
                intent = new Intent(MainActivity.this, UpdateMemo.class);
                startActivity(intent);
                break;
            case R.id.bt_password:
                intent = new Intent(MainActivity.this, PasswordMemo.class);
                startActivity(intent);
                break;
            case R.id.bt_subsc:
                intent = new Intent(MainActivity.this, SubscMemo.class);
                startActivity(intent);
                break;
            case R.id.bt_wishlist:
                intent = new Intent(MainActivity.this, WishlistMemo.class);
                startActivity(intent);
                break;
            case R.id.bt_memo:
                intent = new Intent(MainActivity.this, MemoMemo.class);
                startActivity(intent);
                break;


        }
    }

}

