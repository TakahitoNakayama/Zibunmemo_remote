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
 * 更新期限を入力するUpdateメモのクラス
 *
 * @author nakayama
 * @version 1.0
 */
public class UpdateMemo extends AppCompatActivity {

    private static final String _CATEGORY = "UPDATE1";

    /**
     * データベースのテーブル名
     */
    private static final String TABLE = "update1";

    private Context context = UpdateMemo.this;

    private LayoutInflater inflater;
    private LinearLayout llUpdateLayout;
    private LinearLayout llUpdateInputform;
    private LinearLayout llUpdateTitle;
    private LinearLayout llUpdateDeadline;

    private EditText etUpdateTitle;
    private EditText etUpdateYear;
    private EditText etUpdateMonth;
    private EditText etUpdateDay;
    private ImageButton btDelete;
    private ImageButton btDateSelect;

    private String strUpdateTitle;
    private String strUpdateYear;
    private String strUpdateMonth;
    private String strUpdateDay;

    androidx.fragment.app.FragmentManager manager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_update_memo);


        inflater = LayoutInflater.from(getApplicationContext());
        llUpdateLayout = findViewById(R.id.ll_update_layout);
        llUpdateInputform = (LinearLayout) inflater.inflate(R.layout.update_inputform, null);


        /**
         * データベースの列名の配列
         */
        String[] columnNames = {"updatetitle", "updateyear", "updatemonth", "updateday"};


        /**
         * カレンダーによる日付選択用のFragmentManager型の変数
         */
        manager = getSupportFragmentManager();

        //データベースからデータを取り出して、レイアウトを作成する処理
        DatabaseControl control = new DatabaseControl(context, TABLE, columnNames, manager);
        control.selectDatabase(llUpdateLayout);

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
                llUpdateInputform = (LinearLayout) inflater.inflate(R.layout.update_inputform, null);
                llUpdateLayout.addView(llUpdateInputform);

                llUpdateTitle = llUpdateInputform.findViewById(R.id.ll_update_title);
                llUpdateDeadline = llUpdateTitle.findViewById(R.id.ll_update_deadline);

                btDelete = llUpdateDeadline.findViewById(R.id.bt_delete);
                btDelete.setOnClickListener
                        (new DeleteButton(UpdateMemo.this, llUpdateLayout, llUpdateInputform, TABLE));
                btDateSelect = llUpdateDeadline.findViewById(R.id.bt_date_select);
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
        for (int i = 0; i < llUpdateLayout.getChildCount(); i++) {

            LinearLayout linearLayout = (LinearLayout) llUpdateLayout.getChildAt(i);
            etUpdateTitle = linearLayout.findViewById(R.id.et_update_title);
            etUpdateYear = linearLayout.findViewById(R.id.et_update_year);
            etUpdateMonth = linearLayout.findViewById(R.id.et_update_month);
            etUpdateDay = linearLayout.findViewById(R.id.et_update_day);

            strUpdateTitle = etUpdateTitle.getText().toString();
            strUpdateYear = etUpdateYear.getText().toString();
            strUpdateMonth = etUpdateMonth.getText().toString();
            strUpdateDay = etUpdateDay.getText().toString();

            DatabaseControl control2 = new DatabaseControl
                    (context, TABLE, i, _CATEGORY, strUpdateTitle, strUpdateYear, strUpdateMonth, strUpdateDay);
            control2.insertDatabaseFourColumns("updatetitle", "updateyear", "updatemonth", "updateday");
        }

        finish();
    }
}