package com.websarva.wings.android.dowasurememo;

import android.content.Context;
import android.os.Build;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.LinearLayout;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

public class CarMemo extends AppCompatActivity {

    private static final String _CATEGORY = "CAR";

    /**
     * データベースのテーブル名
     */
    private static final String TABLE = "car";

    private Context context = CarMemo.this;

    private LayoutInflater inflater;
    private LinearLayout llCarLayout;
    private LinearLayout llCarNameInputform;

    private EditText etCarName;
    private EditText etCarMemoTitle;
    private EditText etCarMemoContents;
    private ImageButton btDelete;
    private ImageButton btCarDetailAdd;

    private String strCarName;
    private String strCarMemoTitle;
    private String strCarMemoContents;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_car_memo);

        llCarLayout = findViewById(R.id.ll_car_layout);

        inflater = LayoutInflater.from(getApplicationContext());
        llCarLayout = findViewById(R.id.ll_car_layout);

        //データベースからデータを取り出して、レイアウトを作成する処理
        DatabaseControl control = new DatabaseControl(context, TABLE);
        control.selectDatabase(llCarLayout);

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
            case R.id.option_add:
                //オプションメニューの＋ボタンを押すと、動的にビューを追加する処理
                inflater = LayoutInflater.from(getApplicationContext());
                llCarNameInputform = (LinearLayout) inflater.inflate(R.layout.car_name_inputform, null);
                llCarLayout.addView(llCarNameInputform);

                btCarDetailAdd = llCarNameInputform.findViewById(R.id.bt_cardetail_add);
                btCarDetailAdd.setOnClickListener(new AddCarDetail(CarMemo.this, llCarLayout));

                btDelete = llCarNameInputform.findViewById(R.id.bt_delete);
                btDelete.setOnClickListener
                        (new DeleteButton(CarMemo.this, llCarLayout, llCarNameInputform, TABLE));

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
        for (int i = 0; i < llCarLayout.getChildCount(); i++) {
            LinearLayout linearLayout = (LinearLayout) llCarLayout.getChildAt(i);
            switch (linearLayout.getId()) {
                case R.id.ll_car_name_inputform:
                    etCarName = linearLayout.findViewById(R.id.et_car_name);
                    strCarName = etCarName.getText().toString();

                    String inputform = "name";

                    DatabaseControl control2 = new DatabaseControl
                            (context, TABLE, i, _CATEGORY, strCarName, inputform);
                    control2.insertDatabaseTwoColumns("carname", "inputform");

                    break;


                case R.id.ll_car_detail_inputform:
                    etCarMemoTitle = linearLayout.findViewById(R.id.et_car_memo_title);
                    etCarMemoContents = linearLayout.findViewById(R.id.et_car_memo_contents);
                    strCarMemoTitle = etCarMemoTitle.getText().toString();
                    strCarMemoContents = etCarMemoContents.getText().toString();

                    String memoinputform = "detail";

                    DatabaseControl control3 = new DatabaseControl
                            (context, TABLE, i, _CATEGORY, strCarMemoTitle, strCarMemoContents, memoinputform);
                    control3.insertDatabaseThreeColumns("carmemotitle", "carmemocontents", "inputform");

                    break;
            }
        }

        finish();
    }
}

/**
 * 車の詳細を入力するviewを追加するためのクラス
 *
 * @author nakayama
 * @version 1.0
 */
class AddCarDetail extends LinearLayout implements View.OnClickListener {

    /**
     * データベースのテーブル名
     */
    private static final String TABLE = "car";

    private LayoutInflater inflater;
    private Context context;
    private LinearLayout llBaseLayout;
    private LinearLayout llCarDetailInputform;

    public AddCarDetail(Context _context, LinearLayout _llBaseLayout) {
        super(_context);
        context = _context;
        llBaseLayout = _llBaseLayout;
    }

    @Override
    public void onClick(View v) {
        /*押されたボタンの親ビューとllBaseLayoutの子ビューが等しいときに、
        そのビューのインデックス＋１した場所に車の詳細を入力するビューを追加する*/
        inflater = LayoutInflater.from(context);
        llCarDetailInputform = (LinearLayout) inflater.inflate(R.layout.car_detail_inputform, null);
        LinearLayout linearLayout = (LinearLayout) v.getParent();
        for (int i = 0; i < llBaseLayout.getChildCount(); i++) {
            LinearLayout sameLinearLayout = (LinearLayout) llBaseLayout.getChildAt(i);
            if (linearLayout == sameLinearLayout) {
                llBaseLayout.addView(llCarDetailInputform, i + 1);

                ImageButton btDelete = llCarDetailInputform.findViewById(R.id.bt_delete);
                btDelete.setOnClickListener
                        (new DeleteButton(context, llBaseLayout, llCarDetailInputform, TABLE));

            }
        }
    }
}