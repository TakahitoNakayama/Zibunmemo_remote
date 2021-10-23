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
import com.websarva.wings.android.dowasurememo.databinding.ActivityUpdateMemoBinding
import com.websarva.wings.android.dowasurememo.databinding.UpdateInputformBinding

/**
 * 更新期限を入力するUpdateメモのクラス
 *
 * @author nakayama
 * @version 1.0
 */
class UpdateMemo : AppCompatActivity() {

    companion object {
        private const val _CATEGORY = "UPDATE1"

        /**
         * データベースのテーブル名
         */
        private const val TABLE = "update1"
    }

    private val context: Context = this@UpdateMemo

    private lateinit var binding: ActivityUpdateMemoBinding
    private lateinit var inputformBinding: UpdateInputformBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityUpdateMemoBinding.inflate(layoutInflater)
        inputformBinding = UpdateInputformBinding.inflate(layoutInflater)
        setContentView(binding.root)

        /**
         * データベースの列名の配列
         */
        val columnNames = listOf("updatetitle", "updateyear", "updatemonth", "updateday")

        /**
         * カレンダーによる日付選択用のFragmentManager型の変数
         */
        val manager = supportFragmentManager

        //データベースからデータを取り出して、レイアウトを作成する処理
        DatabaseControl(context, TABLE, columnNames, manager)
                .selectDatabase(binding.llUpdateLayout)
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
                val llUpdateLayout = findViewById<LinearLayout>(R.id.ll_update_layout)
                val inflater = LayoutInflater.from(applicationContext)
                val llUpdateInputform = inflater.inflate(R.layout.update_inputform, null) as LinearLayout
                llUpdateLayout.addView(llUpdateInputform)
                val llUpdateTitle = llUpdateInputform.findViewById<LinearLayout>(R.id.ll_update_title)
                val llUpdateDeadline = llUpdateTitle.findViewById<LinearLayout>(R.id.ll_update_deadline)
                val btDelete = llUpdateDeadline.findViewById<ImageButton>(R.id.bt_delete)
                btDelete.setOnClickListener(DeleteButton
                (this@UpdateMemo, llUpdateLayout, llUpdateInputform, TABLE))
                val btDateSelect = llUpdateDeadline.findViewById<ImageButton>(R.id.bt_date_select)
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
        for (i in 0..binding.llUpdateLayout.childCount - 1) {
            val linearLayout = binding.llUpdateLayout.getChildAt(i) as LinearLayout
            val etUpdateTitle = linearLayout.findViewById<EditText>(R.id.et_update_title)
            val etUpdateYear = linearLayout.findViewById<EditText>(R.id.et_update_year)
            val etUpdateMonth = linearLayout.findViewById<EditText>(R.id.et_update_month)
            val etUpdateDay = linearLayout.findViewById<EditText>(R.id.et_update_day)

            val strUpdateTitle = etUpdateTitle.getText().toString()
            val strUpdateYear = etUpdateYear.getText().toString()
            val strUpdateMonth = etUpdateMonth.getText().toString()
            val strUpdateDay = etUpdateDay.getText().toString()
            val control2 = DatabaseControl(context, TABLE, i, _CATEGORY, strUpdateTitle, strUpdateYear, strUpdateMonth, strUpdateDay)
            control2.insertDatabaseFourColumns("updatetitle", "updateyear", "updatemonth", "updateday")
        }
        finish()
    }


}