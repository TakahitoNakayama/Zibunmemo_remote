package com.websarva.wings.android.dowasurememo

import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import com.websarva.wings.android.dowasurememo.CarMemo
import com.websarva.wings.android.dowasurememo.databinding.ActivityMainBinding
import java.text.SimpleDateFormat
import java.util.*

class MainActivity : AppCompatActivity() {

    private lateinit var _helper: Databasehelper
    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        _helper = Databasehelper(this@MainActivity)

        //メイン画面で日時を表示するための処理
        //val dateOutput = findViewById<TextView>(R.id.date_output)
        val ca = Calendar.getInstance()
        val da = ca.time
        val dateFormat = SimpleDateFormat("y年 M月 d日 (E) H時 m分")
        binding.dateOutput.text = dateFormat.format(da)
    }

    public override fun onDestroy() {
        _helper.close()
        super.onDestroy()
    }

    //それぞれのメモへの画面遷移処理
    fun onClick(v: View) {
        when (v.id) {
            R.id.bt_size -> {
                val intent = Intent(this@MainActivity, SizeMemo::class.java)
                startActivity(intent)
            }
            R.id.bt_date -> {
                intent = Intent(this@MainActivity, DateMemo::class.java)
                startActivity(intent)
            }
            R.id.bt_addres -> {
                intent = Intent(this@MainActivity, AddressMemo::class.java)
                startActivity(intent)
            }
            R.id.bt_car -> {
                intent = Intent(this@MainActivity, CarMemo::class.java)
                startActivity(intent)
            }
            R.id.bt_update -> {
                intent = Intent(this@MainActivity, UpdateMemo::class.java)
                startActivity(intent)
            }
            R.id.bt_password -> {
                intent = Intent(this@MainActivity, PasswordMemo::class.java)
                startActivity(intent)
            }
            R.id.bt_subsc -> {
                intent = Intent(this@MainActivity, SubscMemo::class.java)
                startActivity(intent)
            }
            R.id.bt_wishlist -> {
                intent = Intent(this@MainActivity, WishlistMemo::class.java)
                startActivity(intent)
            }
            R.id.bt_memo -> {
                intent = Intent(this@MainActivity, MemoMemo::class.java)
                startActivity(intent)
            }
        }
    }


}