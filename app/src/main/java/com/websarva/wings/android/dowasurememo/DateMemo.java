package com.websarva.wings.android.dowasurememo;

import android.content.Context;
import android.os.Build;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.LinearLayout;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

/**
 * 住所を入力するDateメモのクラス
 *
 * @author nakayama
 * @version 1.0
 */
public class DateMemo extends AppCompatActivity {

    private static final String _CATEGORY = "DATE1";

    /**
     * データベースのテーブル名
     */
    private static final String TABLE = "date1";

    private Context context = DateMemo.this;

    private EditText etDateTitle;
    private EditText etDateYear;
    private EditText etDateMonth;
    private EditText etDateDay;
    private ImageButton btDelete;
    private ImageButton btDateSelect;

    private LayoutInflater inflater;
    private LinearLayout llDateLayout;
    private LinearLayout llDateInputform;
    private LinearLayout llDateSelect;

    private String strDateTitle;
    private String strYear;
    private String strMonth;
    private String strDay;

    androidx.fragment.app.FragmentManager manager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_date_memo);

        inflater = LayoutInflater.from(getApplicationContext());
        llDateLayout = findViewById(R.id.ll_date_layout);
        llDateInputform = (LinearLayout) inflater.inflate(R.layout.date_inputform, null);

        /**
         * データベースの列名の配列
         */
        String[] columnNames = {"datetitle", "dateyear", "datemonth", "dateday"};

        /**
         * カレンダーによる日付選択用のFragmentManager型の変数
         */
        manager = getSupportFragmentManager();

        //データベースからデータを取り出して、レイアウトを作成する処理
        DatabaseControl control = new DatabaseControl(context, TABLE, columnNames, manager);
        control.selectDatabase(llDateLayout);

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
                llDateInputform = (LinearLayout) inflater.inflate(R.layout.date_inputform, null);
                llDateLayout.addView(llDateInputform);

                llDateSelect = llDateInputform.findViewById(R.id.ll_date_select);

                btDelete = llDateSelect.findViewById(R.id.bt_delete);
                btDelete.setOnClickListener
                        (new DeleteButton(DateMemo.this, llDateLayout, llDateInputform, TABLE));
                btDateSelect = llDateSelect.findViewById(R.id.bt_date_select);
                btDateSelect.setOnClickListener(new DatePickerListener(context, manager, TABLE));

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
        for (int i = 0; i < llDateLayout.getChildCount(); i++) {
            LinearLayout linearLayout = (LinearLayout) llDateLayout.getChildAt(i);
            etDateTitle = linearLayout.findViewById(R.id.et_date_title);
            etDateYear = linearLayout.findViewById(R.id.et_date_year);
            etDateMonth = linearLayout.findViewById(R.id.et_date_month);
            etDateDay = linearLayout.findViewById(R.id.et_date_day);

            strDateTitle = etDateTitle.getText().toString();
            strYear = etDateYear.getText().toString();
            strMonth = etDateMonth.getText().toString();
            strDay = etDateDay.getText().toString();

            DatabaseControl control2 = new DatabaseControl
                    (context, TABLE, i, _CATEGORY, strDateTitle, strYear, strMonth, strDay);
            control2.insertDatabaseFourColumns("datetitle", "dateyear", "datemonth", "dateday");

        }

        finish();
    }
}