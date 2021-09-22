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
 * 目標をメモするWishlistメモのクラス
 *
 * @author nakayama
 * @version 1.0
 */
public class WishlistMemo extends AppCompatActivity {

    private static final String _CATEGORY = "WISHLIST";

    /**
     * データベースのテーブル名
     */
    private static final String TABLE = "wishlist";

    private Context context = WishlistMemo.this;

    private LayoutInflater inflater;
    private LinearLayout llWishlistLayout;
    private LinearLayout llWishlistInputform;
    private LinearLayout llWishlistTitle;

    private EditText etWishlistTitle;
    private ImageButton btDelete;

    private String strWishlistTitle;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_wishlist_memo);

        inflater = LayoutInflater.from(getApplicationContext());
        llWishlistLayout = findViewById(R.id.ll_wishlist_layout);
        llWishlistInputform = (LinearLayout) inflater.inflate(R.layout.wishlist_inputform, null);

        /**
         * データベースの列名の配列
         */
        String[] columnNames = {"wishlisttitle"};

        //データベースからデータを取り出して、レイアウトを作成する処理
        DatabaseControl control = new DatabaseControl(context, TABLE, columnNames);
        control.selectDatabase(llWishlistLayout);
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
                llWishlistInputform = (LinearLayout) inflater.inflate(R.layout.wishlist_inputform, null);
                llWishlistLayout.addView(llWishlistInputform);

                llWishlistTitle = llWishlistInputform.findViewById(R.id.ll_wishlist_title);

                etWishlistTitle = llWishlistTitle.findViewById(R.id.et_wishlist_title);
                btDelete = llWishlistTitle.findViewById(R.id.bt_delete);
                btDelete.setOnClickListener
                        (new DeleteButton(WishlistMemo.this, llWishlistLayout, llWishlistInputform, TABLE));
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
        for (int i = 0; i < llWishlistLayout.getChildCount(); i++) {
            LinearLayout linearLayout = (LinearLayout) llWishlistLayout.getChildAt(i);
            etWishlistTitle = linearLayout.findViewById(R.id.et_wishlist_title);
            strWishlistTitle = etWishlistTitle.getText().toString();

            DatabaseControl control2 = new DatabaseControl
                    (context, TABLE, i, _CATEGORY, strWishlistTitle);
            control2.insertDatabaseOneColumns("wishlisttitle");

        }

        finish();
    }
}