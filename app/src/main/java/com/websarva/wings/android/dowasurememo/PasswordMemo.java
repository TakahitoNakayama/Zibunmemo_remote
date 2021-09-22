package com.websarva.wings.android.dowasurememo;

import android.content.ClipData;
import android.content.ClipDescription;
import android.content.ClipboardManager;
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
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

/**
 * パスワードを入力するPasswordメモのクラス
 *
 * @author nakayama
 * @version 1.0
 */
public class PasswordMemo extends AppCompatActivity {

    private static final String _CATEGORY = "PASSWORD";

    /**
     * データベースのテーブル名
     */
    private static final String TABLE = "password";

    private Context context = PasswordMemo.this;

    private LayoutInflater inflater;
    private LinearLayout llPasswordLayout;
    private LinearLayout llPasswordInputform;
    private LinearLayout llPasswordFrame;
    private LinearLayout llPasswordTitle;
    private LinearLayout llPasswordContents;

    private EditText etPasswordTitle;
    private EditText etPasswordContents;
    private ImageButton btDelete;
    private ImageButton btClip;

    private String strPasswordTitle;
    private String strPasswordContents;

    ClipboardManager cm;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_password_memo);

        inflater = LayoutInflater.from(getApplicationContext());
        llPasswordLayout = findViewById(R.id.ll_password_layout);
        llPasswordInputform = (LinearLayout) inflater.inflate(R.layout.password_inputform, null);

        /**
         * データベースの列名の配列
         */
        String[] columnNames = {"passwordtitle", "passwordcontents"};


        /**
         * Clipbordを実装するためのClipboardManager型の変数
         */
        cm = (ClipboardManager) getSystemService(CLIPBOARD_SERVICE);

        //データベースからデータを取り出して、レイアウトを作成する処理
        DatabaseControl control = new DatabaseControl(context, TABLE, columnNames, cm);
        control.selectDatabase(llPasswordLayout);

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
                llPasswordInputform = (LinearLayout) inflater.inflate(R.layout.password_inputform, null);
                llPasswordLayout.addView(llPasswordInputform);

                llPasswordFrame = llPasswordInputform.findViewById(R.id.ll_password_frame);
                llPasswordTitle = llPasswordFrame.findViewById(R.id.ll_password_title);
                llPasswordContents = llPasswordFrame.findViewById(R.id.ll_password_contents);

                etPasswordContents = llPasswordContents.findViewById(R.id.et_password_contents);
                btDelete = llPasswordTitle.findViewById(R.id.bt_delete);
                btDelete.setOnClickListener
                        (new DeleteButton(PasswordMemo.this, llPasswordLayout, llPasswordInputform, TABLE));
                btClip = llPasswordContents.findViewById(R.id.bt_clip);
                btClip.setOnClickListener(new ClipButtonListener(context, etPasswordContents, cm));

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
        for (int i = 0; i < llPasswordLayout.getChildCount(); i++) {
            LinearLayout llPasswordLayout = findViewById(R.id.ll_password_layout);
            LinearLayout linearLayout = (LinearLayout) llPasswordLayout.getChildAt(i);
            etPasswordTitle = linearLayout.findViewById(R.id.et_password_title);
            etPasswordContents = linearLayout.findViewById(R.id.et_password_contents);

            strPasswordTitle = etPasswordTitle.getText().toString();
            strPasswordContents = etPasswordContents.getText().toString();

            DatabaseControl control2 = new DatabaseControl
                    (context, TABLE, i, _CATEGORY, strPasswordTitle, strPasswordContents);
            control2.insertDatabaseTwoColumns("passwordtitle", "passwordcontents");

        }

        finish();
    }

}

class CopyClipbord extends ClipData {

    public CopyClipbord(CharSequence label, String[] mimeTypes, Item item) {
        super(label, mimeTypes, item);
    }
}

/**
 * コピーアイコンのボタンが押されたときに、テキストを取得してクリップボードに保存するクラス
 *
 * @author nakayama
 * @version 1.0
 */
class ClipButtonListener implements View.OnClickListener {

    EditText editText;
    Context context;
    ClipboardManager cm;

    public ClipButtonListener(Context _context, EditText _edittext, ClipboardManager _cm) {
        context = _context;
        editText = _edittext;
        cm = _cm;
    }

    @Override
    public void onClick(View v) {
        ClipData.Item item = new ClipData.Item(editText.getText());
        String[] mimeType = new String[1];
        mimeType[0] = ClipDescription.MIMETYPE_TEXT_PLAIN;
        CopyClipbord copy = new CopyClipbord("password", mimeType, item);
        cm.setPrimaryClip(copy);
        Toast.makeText
                (context, "クリップボードにコピーしました", Toast.LENGTH_SHORT).show();
    }
}