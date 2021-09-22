package com.websarva.wings.android.dowasurememo;

import android.content.Context;
import android.os.Build;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

/**
 * サブスクサービスをメモするSubscメモのクラス
 *
 * @author nakayama
 * @version 1.0
 */
public class SubscMemo extends AppCompatActivity {

    private static final String _CATEGORY = "SUBSC";

    /**
     * データベースのテーブル名
     */
    private static final String TABLE = "subsc";

    private Context context = SubscMemo.this;

    private LayoutInflater inflater;
    private LinearLayout linearLayout;
    private LinearLayout llSubscLayout;
    private LinearLayout llSubscInputform;
    private LinearLayout llSubscTitle;
    private LinearLayout llSubscPrice;
    private LinearLayout llSubscFrame;

    private EditText etSubscTitle;
    private EditText etSubscPrice;
    private Button btSubscCulc;
    private ImageButton btDelete;
    private Spinner spPaymentInterbal;

    private String strSubscTitle;
    private String strSubscPrice;
    private int intSpinnerIndex;
    private String strSpinnerIndex;

    private int monthPaymentAmount;
    private TextView tvMonthPayment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_subsc_memo);

        inflater = LayoutInflater.from(getApplicationContext());
        llSubscLayout = findViewById(R.id.ll_subsc_layout);
        llSubscInputform = (LinearLayout) inflater.inflate(R.layout.subsc_inputform, null);

        btSubscCulc = findViewById(R.id.bt_subsc_culc);
        btSubscCulc.setOnClickListener(new CulcButtonListener());

        /**
         * データベースの列名の配列
         */
        String[] columnNames = {"subsctitle", "subscprice", "subscinterbal"};

        //データベースからデータを取り出して、レイアウトを作成する処理
        DatabaseControl control = new DatabaseControl(context, TABLE, columnNames);
        control.selectDatabase(llSubscLayout);

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
                llSubscLayout = findViewById(R.id.ll_subsc_layout);
                inflater = LayoutInflater.from(getApplicationContext());
                llSubscInputform = (LinearLayout) inflater.inflate(R.layout.subsc_inputform, null);
                llSubscLayout.addView(llSubscInputform);

                llSubscFrame = llSubscInputform.findViewById(R.id.ll_subsc_frame);
                llSubscTitle = llSubscFrame.findViewById(R.id.ll_subsc_title);
                llSubscPrice = llSubscFrame.findViewById(R.id.ll_subsc_price);

                btDelete = llSubscTitle.findViewById(R.id.bt_delete);
                btDelete.setOnClickListener
                        (new DeleteButton(SubscMemo.this, llSubscLayout, llSubscInputform, TABLE));
                spPaymentInterbal = llSubscPrice.findViewById(R.id.sp_payment_interbal);
                btSubscCulc = findViewById(R.id.bt_subsc_culc);
                btSubscCulc.setOnClickListener(new CulcButtonListener());

        }
        return super.onOptionsItemSelected(item);
    }


    /**
     * 金額を取得して、スピナーの支払い期間に応じた合計金額を出力するクラス
     *
     * @author nakayama
     * @version 1.0
     */
    class CulcButtonListener implements View.OnClickListener {
        @Override
        public void onClick(View v) {
            int price;
            String strprice;
            monthPaymentAmount = 0;

            for (int i = 0; i < llSubscLayout.getChildCount(); i++) {
                linearLayout = (LinearLayout) llSubscLayout.getChildAt(i);
                etSubscPrice = linearLayout.findViewById(R.id.et_subsc_price);
                price = 0;

                try {
                    strprice = String.valueOf(etSubscPrice.getText());
                    price = Integer.valueOf(strprice);
                } catch (NumberFormatException e) {
                    strprice = "0";
                }

                spPaymentInterbal = linearLayout.findViewById(R.id.sp_payment_interbal);
                String strInterbal = (String) spPaymentInterbal.getSelectedItem();
                switch (strInterbal) {
                    case "毎月":
                        monthPaymentAmount += price;
                        break;
                    case "2ヶ月":
                        monthPaymentAmount += price / 2;
                        break;
                    case "3ヶ月":
                        monthPaymentAmount += price / 3;
                        break;
                    case "4ヶ月":
                        monthPaymentAmount += price / 4;
                        break;
                    case "半年":
                        monthPaymentAmount += price / 6;
                        break;
                    case "1年":
                        monthPaymentAmount += price / 12;
                        break;
                    case "2年":
                        monthPaymentAmount += price / 24;
                        break;

                }
            }
            tvMonthPayment = findViewById(R.id.tv_month_payment);
            tvMonthPayment.setText(String.format("%,d", monthPaymentAmount));
        }
    }


    @Override
    protected void onPause() {
        super.onPause();

        //データベースにある全てのデータを削除
        DatabaseControl control = new DatabaseControl(context, TABLE);
        control.deleteAllDatabase();

        //メモの文字列を取得してデータベースにインサートする
        for (int i = 0; i < llSubscLayout.getChildCount(); i++) {
            LinearLayout linearLayout = (LinearLayout) llSubscLayout.getChildAt(i);
            etSubscTitle = linearLayout.findViewById(R.id.et_subsc_title);
            etSubscPrice = linearLayout.findViewById(R.id.et_subsc_price);
            spPaymentInterbal = linearLayout.findViewById(R.id.sp_payment_interbal);

            strSubscTitle = etSubscTitle.getText().toString();
            strSubscPrice = etSubscPrice.getText().toString();
            intSpinnerIndex = spPaymentInterbal.getSelectedItemPosition();
            strSpinnerIndex = String.valueOf(intSpinnerIndex);

            DatabaseControl control2 = new DatabaseControl
                    (context, TABLE, i, _CATEGORY, strSubscTitle, strSubscPrice, strSpinnerIndex);
            control2.insertDatabaseThreeColumns("subsctitle", "subscprice", "subscinterbal");

        }

        finish();
    }
}

