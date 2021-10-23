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
import com.websarva.wings.android.dowasurememo.databinding.ActivityWishlistMemoBinding
import com.websarva.wings.android.dowasurememo.databinding.WishlistInputformBinding

/**
 * 目標をメモするWishlistメモのクラス
 *
 * @author nakayama
 * @version 1.0
 */
class WishlistMemo : AppCompatActivity() {

    companion object {
        private const val _CATEGORY = "WISHLIST"

        /**
         * データベースのテーブル名
         */
        private const val TABLE = "wishlist"
    }

    private val context: Context = this@WishlistMemo

    private lateinit var binding: ActivityWishlistMemoBinding
    private lateinit var inputformBinding: WishlistInputformBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityWishlistMemoBinding.inflate(layoutInflater)
        inputformBinding = WishlistInputformBinding.inflate(layoutInflater)
        setContentView(binding.root)

        /**
         * データベースの列名の配列
         */
        val columnNames = listOf("wishlisttitle")

        //データベースからデータを取り出して、レイアウトを作成する処理
        DatabaseControl(context, TABLE, columnNames)
                .selectDatabase(binding.llWishlistLayout)
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
                val llWishlistLayout = findViewById<LinearLayout>(R.id.ll_wishlist_layout)
                val inflater = LayoutInflater.from(applicationContext)
                val llWishlistInputform = inflater.inflate(R.layout.wishlist_inputform, null) as LinearLayout
                llWishlistLayout.addView(llWishlistInputform)
                val llWishlistTitle = llWishlistInputform.findViewById<LinearLayout>(R.id.ll_wishlist_title)
                val etWishlistTitle = llWishlistTitle.findViewById<EditText>(R.id.et_wishlist_title)
                val btDelete = llWishlistTitle.findViewById<ImageButton>(R.id.bt_delete)
                btDelete.setOnClickListener(DeleteButton
                (this@WishlistMemo, llWishlistLayout, llWishlistInputform, TABLE))
            }
        }
        return super.onOptionsItemSelected(item)
    }

    override fun onPause() {
        super.onPause()

        //データベースにある全てのデータを削除
        DatabaseControl(context, TABLE).deleteAllDatabase()

        //メモの文字列を取得してデータベースにインサートする
        for (i in 0..binding.llWishlistLayout.childCount - 1) {
            val linearLayout = binding.llWishlistLayout.getChildAt(i) as LinearLayout
            val etWishlistTitle = linearLayout.findViewById<EditText>(R.id.et_wishlist_title)

            val strWishlistTitle = etWishlistTitle.getText().toString()
            val control2 = DatabaseControl(context, TABLE, i, _CATEGORY, strWishlistTitle)
            control2.insertDatabaseOneColumns("wishlisttitle")
        }
        finish()
    }


}