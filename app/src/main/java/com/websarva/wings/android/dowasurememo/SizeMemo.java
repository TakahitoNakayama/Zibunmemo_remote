package com.websarva.wings.android.dowasurememo;

import android.content.Context;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

/**
 * 体のサイズを入力するSizeメモのクラス
 *
 * @author nakayama
 * @version 1.0
 */
public class SizeMemo extends AppCompatActivity {

    private static final String _CATEGORY = "SIZE";

    /**
     * データベースのテーブル名
     */
    private static final String TABLE = "size";

    private Context context = SizeMemo.this;

    private LayoutInflater inflater;
    private LinearLayout llSizeLayout;
    private LinearLayout llSizeInputform;

    private EditText etBodyPart;
    private EditText etRecord;
    private EditText etUnit;
    private ImageButton btDelete;

    private String strBodyPart;
    private String strRecord;
    private String strUnit;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_size_memo);


        inflater = LayoutInflater.from(getApplicationContext());
        llSizeLayout = findViewById(R.id.ll_size_layout);
        llSizeInputform = (LinearLayout) inflater.inflate(R.layout.size_inputform, null);

        /**
         * データベースの列名の配列
         */
        String[] columnNames = {"bodypart", "records", "unit"};

        //データベースからデータを取り出して、レイアウトを作成する処理
        DatabaseControl control = new DatabaseControl(context, TABLE, columnNames);
        control.selectDatabase(llSizeLayout);

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.optionmenu, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @RequiresApi(api = Build.VERSION_CODES.Q)
    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {
            //オプションメニューの＋ボタンを押すと、動的にビューを追加する処理
            case R.id.option_add:
                inflater = LayoutInflater.from(getApplicationContext());
                llSizeInputform = (LinearLayout) inflater.inflate(R.layout.size_inputform, null);
                llSizeLayout.addView(llSizeInputform);

                ImageView circle = llSizeInputform.findViewById(R.id.circle);
                circle.setColorFilter(Color.rgb(127, 255, 212));

                btDelete = llSizeInputform.findViewById(R.id.bt_delete);
                btDelete.setOnClickListener
                        (new DeleteButton(context, llSizeLayout, llSizeInputform, TABLE));
        }
        return super.onOptionsItemSelected(item);
    }


    @Override
    protected void onPause() {
        super.onPause();

        //データベースにある全てのデータを削除
        DatabaseControl control = new DatabaseControl(context, TABLE);
        control.deleteAllDatabase();

        //メモの文字列を取得してデータベースにインサートする
        for (int i = 0; i < llSizeLayout.getChildCount(); i++) {
            LinearLayout linearLayout = (LinearLayout) llSizeLayout.getChildAt(i);
            etBodyPart = linearLayout.findViewById(R.id.et_bodypart);
            etRecord = linearLayout.findViewById(R.id.et_record);
            etUnit = linearLayout.findViewById(R.id.et_unit);

            strBodyPart = etBodyPart.getText().toString();
            strRecord = etRecord.getText().toString();
            strUnit = etUnit.getText().toString();

            DatabaseControl control2 = new DatabaseControl
                    (context, TABLE, i, _CATEGORY, strBodyPart, strRecord, strUnit);
            control2.insertDatabaseThreeColumns("bodypart", "records", "unit");

        }

        finish();
    }
}

