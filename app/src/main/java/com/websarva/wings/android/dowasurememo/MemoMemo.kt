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
import com.websarva.wings.android.dowasurememo.databinding.ActivityMemoMemoBinding
import com.websarva.wings.android.dowasurememo.databinding.MemoInputformBinding

/**
 * 普通のメモを入力するMemoメモのクラス
 *
 * @author nakayama
 * @version 1.0
 */
class MemoMemo : AppCompatActivity() {

    companion object {
        private const val _CATEGORY = "MEMO"

        /**
         * データベースのテーブル名
         */
        private const val TABLE = "memo"
    }

    private val context: Context = this@MemoMemo

    private lateinit var binding: ActivityMemoMemoBinding
    private lateinit var inputformBinding: MemoInputformBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMemoMemoBinding.inflate(layoutInflater)
        inputformBinding = MemoInputformBinding.inflate(layoutInflater)
        setContentView(binding.root)

        /**
         * データベースの列名の配列
         */
        val columnNames = listOf("memotitle", "memocontents")

        //データベースからデータを取り出して、レイアウトを作成する処理
        DatabaseControl(context, TABLE, columnNames)
                .selectDatabase(binding.llMemoLayout)
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
                val llMemoLayout = findViewById<LinearLayout>(R.id.ll_memo_layout)
                val inflater = LayoutInflater.from(applicationContext)
                val llMemoInputform = inflater.inflate(R.layout.memo_inputform, null) as LinearLayout
                llMemoLayout.addView(llMemoInputform)
                val llMemoFrame = llMemoInputform.findViewById<LinearLayout>(R.id.ll_memo_frame)
                val llMemoTitle = llMemoFrame.findViewById<LinearLayout>(R.id.ll_memo_title)
                val btDelete = llMemoTitle.findViewById<ImageButton>(R.id.bt_delete)
                btDelete.setOnClickListener(DeleteButton
                (this@MemoMemo, llMemoLayout, llMemoInputform, TABLE))
            }
        }
        return super.onOptionsItemSelected(item)
    }

    override fun onPause() {
        super.onPause()

        //データベースにある全てのデータを削除
        DatabaseControl(context, TABLE).deleteAllDatabase()

        //メモの文字列を取得してデータベースにインサートする
        for (i in 0..binding.llMemoLayout.childCount - 1) {
            val linearLayout = binding.llMemoLayout.getChildAt(i) as LinearLayout
            val etMemoTitle = linearLayout.findViewById<EditText>(R.id.et_memo_title)
            val etMemoContents = linearLayout.findViewById<EditText>(R.id.et_memo_contents)

            val strMemoTitle = etMemoTitle.getText().toString()
            val strMemoContents = etMemoContents.getText().toString()
            val control2 = DatabaseControl(context, TABLE, i, _CATEGORY, strMemoTitle, strMemoContents)
            control2.insertDatabaseTwoColumns("memotitle", "memocontents")
        }
        finish()
    }


}