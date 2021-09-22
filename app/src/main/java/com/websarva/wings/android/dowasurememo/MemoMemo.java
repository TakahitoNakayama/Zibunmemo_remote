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
 * 普通のメモを入力するMemoメモのクラス
 *
 * @author nakayama
 * @version 1.0
 */
public class MemoMemo extends AppCompatActivity {

    private static final String _CATEGORY = "MEMO";

    /**
     * データベースのテーブル名
     */
    private static final String TABLE = "memo";

    Context context = MemoMemo.this;

    LayoutInflater inflater;
    LinearLayout llMemoLayout;
    LinearLayout llMemoInputform;
    LinearLayout llMemoFrame;
    LinearLayout llMemoTitle;

    EditText etMemoTitle;
    EditText etMemoContents;
    ImageButton btDelete;

    String strMemoTitle;
    String strMemoContents;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_memo_memo);

        inflater = LayoutInflater.from(getApplicationContext());
        llMemoLayout = findViewById(R.id.ll_memo_layout);
        llMemoInputform = (LinearLayout) inflater.inflate(R.layout.memo_inputform, null);

        /**
         * データベースの列名の配列
         */
        String[] columnNames = {"memotitle", "memocontents"};

        //データベースからデータを取り出して、レイアウトを作成する処理
        DatabaseControl control = new DatabaseControl(context, TABLE, columnNames);
        control.selectDatabase(llMemoLayout);

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
                llMemoInputform = (LinearLayout) inflater.inflate(R.layout.memo_inputform, null);
                llMemoLayout.addView(llMemoInputform);

                llMemoFrame = llMemoInputform.findViewById(R.id.ll_memo_frame);
                llMemoTitle = llMemoFrame.findViewById(R.id.ll_memo_title);

                btDelete = llMemoTitle.findViewById(R.id.bt_delete);
                btDelete.setOnClickListener
                        (new DeleteButton(MemoMemo.this, llMemoLayout, llMemoInputform, TABLE));

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
        for (int i = 0; i < llMemoLayout.getChildCount(); i++) {
            LinearLayout linearLayout = (LinearLayout) llMemoLayout.getChildAt(i);
            etMemoTitle = linearLayout.findViewById(R.id.et_memo_title);
            etMemoContents = linearLayout.findViewById(R.id.et_memo_contents);

            strMemoTitle = etMemoTitle.getText().toString();
            strMemoContents = etMemoContents.getText().toString();

            DatabaseControl control2 = new DatabaseControl
                    (context, TABLE, i, _CATEGORY, strMemoTitle, strMemoContents);
            control2.insertDatabaseTwoColumns("memotitle", "memocontents");

        }

        finish();
    }
}