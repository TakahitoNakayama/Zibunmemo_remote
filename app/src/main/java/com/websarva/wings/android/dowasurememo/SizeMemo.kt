package com.websarva.wings.android.dowasurememo

import android.content.Context
import android.graphics.Color
import android.os.Build
import android.os.Bundle
import android.view.LayoutInflater
import android.view.Menu
import android.view.MenuItem
import android.widget.*
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import com.websarva.wings.android.dowasurememo.databinding.ActivitySizeMemoBinding
import com.websarva.wings.android.dowasurememo.databinding.SizeInputformBinding

/**
 * 体のサイズを入力するSizeメモのクラス
 *
 * @author nakayama
 * @version 1.0
 */
class SizeMemo : AppCompatActivity() {

    companion object {
        private const val _CATEGORY = "SIZE"

        /**
         * データベースのテーブル名
         */
        private const val TABLE = "size"
    }

    private val context: Context = this@SizeMemo

    private lateinit var binding: ActivitySizeMemoBinding
    private lateinit var inputformBinding: SizeInputformBinding


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySizeMemoBinding.inflate(layoutInflater)
        inputformBinding = SizeInputformBinding.inflate(layoutInflater)
        setContentView(binding.root)

        /**
         * データベースの列名の配列
         */
        val columnNames = listOf("bodypart", "records", "unit")

        //データベースからデータを取り出して、レイアウトを作成する処理
        DatabaseControl(context, TABLE, columnNames)
                .selectDatabase(binding.llSizeLayout)
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
                val llSizeLayout = findViewById<LinearLayout>(R.id.ll_size_layout)
                val inflater = LayoutInflater.from(applicationContext)
                val llSizeInputform = inflater.inflate(R.layout.size_inputform, null) as LinearLayout
                llSizeLayout.addView(llSizeInputform)
                val circle = llSizeInputform.findViewById<ImageView>(R.id.circle)
                circle.setColorFilter(Color.rgb(127, 255, 212))
                val btDelete = llSizeInputform.findViewById<ImageButton>(R.id.bt_delete)
                btDelete.setOnClickListener(DeleteButton
                (context, llSizeLayout, llSizeInputform, TABLE))
            }
        }
        return super.onOptionsItemSelected(item)
    }

    override fun onPause() {
        super.onPause()

        //データベースにある全てのデータを削除
        DatabaseControl(context, TABLE).deleteAllDatabase()

        //メモの文字列を取得してデータベースにインサートする
        for (i in 0..binding.llSizeLayout.childCount - 1) {
            val linearLayout = binding.llSizeLayout.getChildAt(i) as LinearLayout
            val etBodyPart = linearLayout.findViewById<EditText>(R.id.et_bodypart)
            val etRecord = linearLayout.findViewById<EditText>(R.id.et_record)
            val etUnit = linearLayout.findViewById<EditText>(R.id.et_unit)

            val strBodyPart = etBodyPart.getText().toString()
            val strRecord = etRecord.getText().toString()
            val strUnit = etUnit.getText().toString()
            val control2 = DatabaseControl(context, TABLE, i, _CATEGORY, strBodyPart, strRecord, strUnit)
            control2.insertDatabaseThreeColumns("bodypart", "records", "unit")
        }
        finish()
    }


}