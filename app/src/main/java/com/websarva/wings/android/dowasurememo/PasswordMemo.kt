package com.websarva.wings.android.dowasurememo

import android.content.*
import android.os.Build
import android.os.Bundle
import android.view.*
import android.widget.EditText
import android.widget.ImageButton
import android.widget.LinearLayout
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import com.websarva.wings.android.dowasurememo.databinding.ActivityPasswordMemoBinding
import com.websarva.wings.android.dowasurememo.databinding.PasswordInputformBinding

/**
 * パスワードを入力するPasswordメモのクラス
 *
 * @author nakayama
 * @version 1.0
 */
class PasswordMemo : AppCompatActivity() {

    companion object {
        private const val _CATEGORY = "PASSWORD"

        /**
         * データベースのテーブル名
         */
        private const val TABLE = "password"
    }

    private val context: Context = this@PasswordMemo

    private lateinit var binding: ActivityPasswordMemoBinding
    private lateinit var inputformBinding: PasswordInputformBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityPasswordMemoBinding.inflate(layoutInflater)
        inputformBinding = PasswordInputformBinding.inflate(layoutInflater)
        setContentView(binding.root)

        /**
         * データベースの列名の配列
         */
        val columnNames = listOf("passwordtitle", "passwordcontents")

        /**
         * Clipbordを実装するためのClipboardManager型の変数
         */
        val cm = getSystemService(CLIPBOARD_SERVICE) as ClipboardManager

        //データベースからデータを取り出して、レイアウトを作成する処理
        DatabaseControl(context, TABLE, columnNames, cm)
                .selectDatabase(binding.llPasswordLayout)
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        val inflater = menuInflater
        inflater.inflate(R.menu.optionmenu, menu)
        return super.onCreateOptionsMenu(menu)
    }

    @RequiresApi(api = Build.VERSION_CODES.Q)
    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        val cm = getSystemService(CLIPBOARD_SERVICE) as ClipboardManager
        when (item.itemId) {
            R.id.option_add -> {
                //オプションメニューの＋ボタンを押すと、動的にビューを追加する処理
                val llPasswordLayout = findViewById<LinearLayout>(R.id.ll_password_layout)
                val inflater = LayoutInflater.from(applicationContext)
                val llPasswordInputform = inflater.inflate(R.layout.password_inputform, null) as LinearLayout
                llPasswordLayout.addView(llPasswordInputform)
                val llPasswordFrame = llPasswordInputform.findViewById<LinearLayout>(R.id.ll_password_frame)
                val llPasswordTitle = llPasswordFrame.findViewById<LinearLayout>(R.id.ll_password_title)
                val llPasswordContents = llPasswordFrame.findViewById<LinearLayout>(R.id.ll_password_contents)
                val etPasswordContents = llPasswordContents.findViewById<EditText>(R.id.et_password_contents)
                val btDelete = llPasswordTitle.findViewById<ImageButton>(R.id.bt_delete)
                btDelete.setOnClickListener(DeleteButton
                (this@PasswordMemo, llPasswordLayout, llPasswordInputform, TABLE))
                val btClip = llPasswordContents.findViewById<ImageButton>(R.id.bt_clip)
                btClip.setOnClickListener(ClipButtonListener
                (context, etPasswordContents, cm))
            }
        }
        return super.onOptionsItemSelected(item)
    }

    override fun onPause() {
        super.onPause()


        //データベースにある全てのデータを削除
        DatabaseControl(context, TABLE).deleteAllDatabase()

        //メモの文字列を取得してデータベースにインサートする
        for (i in 0..binding.llPasswordLayout.childCount - 1) {
//            val llPasswordLayout = findViewById<LinearLayout>(R.id.ll_password_layout)
            val linearLayout = binding.llPasswordLayout.getChildAt(i) as LinearLayout
            val etPasswordTitle = linearLayout.findViewById<EditText>(R.id.et_password_title)
            val etPasswordContents = linearLayout.findViewById<EditText>(R.id.et_password_contents)

            val strPasswordTitle = etPasswordTitle.getText().toString()
            val strPasswordContents = etPasswordContents.getText().toString()
            val control2 = DatabaseControl(context, TABLE, i, _CATEGORY, strPasswordTitle, strPasswordContents)
            control2.insertDatabaseTwoColumns("passwordtitle", "passwordcontents")
        }
        finish()
    }


}

internal class CopyClipbord(label: CharSequence, mimeTypes: Array<String?>, item: Item) : ClipData(label, mimeTypes, item)

/**
 * コピーアイコンのボタンが押されたときに、テキストを取得してクリップボードに保存するクラス
 *
 * @author nakayama
 * @version 1.0
 */
internal class ClipButtonListener(var context: Context, var editText: EditText, var cm: ClipboardManager) : View.OnClickListener {
    override fun onClick(v: View) {
        val item = ClipData.Item(editText.text)
        val mimeType = arrayOfNulls<String>(1)
        mimeType[0] = ClipDescription.MIMETYPE_TEXT_PLAIN
        val copy = CopyClipbord("password", mimeType, item)
        cm.setPrimaryClip(copy)
        Toast.makeText(context, "クリップボードにコピーしました", Toast.LENGTH_SHORT).show()
    }
}