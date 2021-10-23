package com.websarva.wings.android.dowasurememo

import android.content.Context
import android.os.Build
import android.os.Bundle
import android.view.LayoutInflater
import android.view.Menu
import android.view.MenuItem
import android.widget.EditText
import android.widget.ImageButton
import android.widget.LinearLayout
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import com.websarva.wings.android.dowasurememo.databinding.ActivityDateMemoBinding
import com.websarva.wings.android.dowasurememo.databinding.DateInputformBinding

/**
 * 住所を入力するDateメモのクラス
 *
 * @author nakayama
 * @version 1.0
 */
class DateMemo : AppCompatActivity() {

    companion object {
        private const val _CATEGORY = "DATE1"

        /**
         * データベースのテーブル名
         */
        private const val TABLE = "date1"
    }

    private val context: Context = this@DateMemo

    private lateinit var binding: ActivityDateMemoBinding
    private lateinit var inputformBinding: DateInputformBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityDateMemoBinding.inflate(layoutInflater)
        inputformBinding = DateInputformBinding.inflate(layoutInflater)
        setContentView(binding.root)

        /**
         * データベースの列名の配列
         */
        val columnNames = listOf("datetitle", "dateyear", "datemonth", "dateday")

        /**
         * カレンダーによる日付選択用のFragmentManager型の変数
         */
        val manager = supportFragmentManager

        //データベースからデータを取り出して、レイアウトを作成する処理
        DatabaseControl(context, TABLE, columnNames, manager)
                .selectDatabase(binding.llDateLayout)
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        val inflater = menuInflater
        inflater.inflate(R.menu.optionmenu, menu)
        return super.onCreateOptionsMenu(menu)
    }

    @RequiresApi(api = Build.VERSION_CODES.Q)
    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        val manager = supportFragmentManager
        when (item.itemId) {
            R.id.option_add -> {
                //オプションメニューの＋ボタンを押すと、動的にビューを追加する処理
                val inflater = LayoutInflater.from(applicationContext)
                val llDateLayout = findViewById<LinearLayout>(R.id.ll_date_layout)
                val llDateInputform = inflater.inflate(R.layout.date_inputform, null) as LinearLayout
                llDateLayout.addView(llDateInputform)
                val llDateSelect = llDateInputform.findViewById<LinearLayout>(R.id.ll_date_select)
                val btDelete = llDateSelect.findViewById<ImageButton>(R.id.bt_delete)
                btDelete.setOnClickListener(DeleteButton
                (this@DateMemo, llDateLayout, llDateInputform, TABLE))
                val btDateSelect = llDateSelect.findViewById<ImageButton>(R.id.bt_date_select)
                btDateSelect.setOnClickListener(DatePickerListener(context, manager, TABLE))
            }
        }
        return super.onOptionsItemSelected(item)
    }

    override fun onPause() {
        super.onPause()

        //データベースにある全てのデータを削除
        DatabaseControl(context, TABLE).deleteAllDatabase()

        //メモの文字列を取得してデータベースにインサートする
        for (i in 0..binding.llDateLayout.childCount - 1) {
            val linearLayout = binding.llDateLayout.getChildAt(i) as LinearLayout
            val etDateTitle = linearLayout.findViewById<EditText>(R.id.et_date_title)
            val etDateYear = linearLayout.findViewById<EditText>(R.id.et_date_year)
            val etDateMonth = linearLayout.findViewById<EditText>(R.id.et_date_month)
            val etDateDay = linearLayout.findViewById<EditText>(R.id.et_date_day)

            val strDateTitle = etDateTitle.getText().toString()
            val strYear = etDateYear.getText().toString()
            val strMonth = etDateMonth.getText().toString()
            val strDay = etDateDay.getText().toString()
            val control2 = DatabaseControl(context, TABLE, i, _CATEGORY, strDateTitle, strYear, strMonth, strDay)
            control2.insertDatabaseFourColumns("datetitle", "dateyear", "datemonth", "dateday")
        }
        finish()
    }


}