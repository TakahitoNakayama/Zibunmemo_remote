package com.websarva.wings.android.dowasurememo

import android.content.Context
import android.os.Build
import android.os.Bundle
import android.view.*
import android.widget.*
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import com.websarva.wings.android.dowasurememo.databinding.ActivitySubscMemoBinding
import com.websarva.wings.android.dowasurememo.databinding.SubscInputformBinding

/**
 * サブスクサービスをメモするSubscメモのクラス
 *
 * @author nakayama
 * @version 1.0
 */
class SubscMemo : AppCompatActivity() {

    companion object {
        private const val _CATEGORY = "SUBSC"

        /**
         * データベースのテーブル名
         */
        private const val TABLE = "subsc"
    }

    private val context: Context = this@SubscMemo

    private lateinit var binding: ActivitySubscMemoBinding
    private lateinit var inputformBinding: SubscInputformBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySubscMemoBinding.inflate(layoutInflater)
        inputformBinding = SubscInputformBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.btSubscCulc.setOnClickListener(CulcButtonListener())

        /**
         * データベースの列名の配列
         */
        val columnNames = listOf("subsctitle", "subscprice", "subscinterbal")

        //データベースからデータを取り出して、レイアウトを作成する処理
        DatabaseControl(context, TABLE, columnNames)
                .selectDatabase(binding.llSubscLayout)
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        val inflater = menuInflater
        inflater.inflate(R.menu.optionmenu, menu)
        return super.onCreateOptionsMenu(menu)
    }

    @RequiresApi(api = Build.VERSION_CODES.Q)
    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.option_add -> {
                //オプションメニューの＋ボタンを押すと、動的にビューを追加する処理
                val llSubscLayout = findViewById<LinearLayout>(R.id.ll_subsc_layout)
                val inflater = LayoutInflater.from(applicationContext)
                val llSubscInputform = inflater.inflate(R.layout.subsc_inputform, null) as LinearLayout
                llSubscLayout.addView(llSubscInputform)
                val llSubscFrame = llSubscInputform.findViewById<LinearLayout>(R.id.ll_subsc_frame)
                val llSubscTitle = llSubscFrame.findViewById<LinearLayout>(R.id.ll_subsc_title)
                val btDelete = llSubscTitle.findViewById<ImageButton>(R.id.bt_delete)
                btDelete.setOnClickListener(DeleteButton
                (this@SubscMemo, llSubscLayout, llSubscInputform, TABLE))
            }
        }
        return super.onOptionsItemSelected(item)
    }

    /**
     * 金額を取得して、スピナーの支払い期間に応じた合計金額を出力するクラス
     *
     * @author nakayama
     * @version 1.0
     */
    internal inner class CulcButtonListener : View.OnClickListener {
        override fun onClick(v: View) {
            var price: Int
            var strprice: String
            var monthPaymentAmount = 0
            for (i in 0..binding.llSubscLayout.childCount - 1) {
                val linearLayout = binding.llSubscLayout.getChildAt(i) as LinearLayout
                val etSubscPrice = linearLayout.findViewById<EditText>(R.id.et_subsc_price)
                price = 0
                try {
                    strprice = etSubscPrice.getText().toString()
                    price = strprice.toInt()
                } catch (e: NumberFormatException) {
                    strprice = "0"
                }
                val spPaymentInterbal = linearLayout.findViewById<Spinner>(R.id.sp_payment_interbal)
                val strInterbal = spPaymentInterbal.getSelectedItem() as String
                when (strInterbal) {
                    "毎月" -> monthPaymentAmount += price
                    "2ヶ月" -> monthPaymentAmount += price / 2
                    "3ヶ月" -> monthPaymentAmount += price / 3
                    "4ヶ月" -> monthPaymentAmount += price / 4
                    "半年" -> monthPaymentAmount += price / 6
                    "1年" -> monthPaymentAmount += price / 12
                    "2年" -> monthPaymentAmount += price / 24
                }
            }
            binding.tvMonthPayment.setText(String.format("%,d", monthPaymentAmount))
        }
    }

    override fun onPause() {
        super.onPause()

        //データベースにある全てのデータを削除
        DatabaseControl(context, TABLE).deleteAllDatabase()

        //メモの文字列を取得してデータベースにインサートする
        for (i in 0..binding.llSubscLayout.childCount - 1) {
            val linearLayout = binding.llSubscLayout.getChildAt(i) as LinearLayout
            val etSubscTitle = linearLayout.findViewById<EditText>(R.id.et_subsc_title)
            val etSubscPrice = linearLayout.findViewById<EditText>(R.id.et_subsc_price)
            val spPaymentInterbal = linearLayout.findViewById<Spinner>(R.id.sp_payment_interbal)

            val strSubscTitle = etSubscTitle.getText().toString()
            val strSubscPrice = etSubscPrice.getText().toString()
            val intSpinnerIndex = spPaymentInterbal.getSelectedItemPosition()
            val strSpinnerIndex = intSpinnerIndex.toString()
            val control2 = DatabaseControl(context, TABLE, i, _CATEGORY, strSubscTitle, strSubscPrice, strSpinnerIndex)
            control2.insertDatabaseThreeColumns("subsctitle", "subscprice", "subscinterbal")
        }
        finish()
    }
}