package com.websarva.wings.android.dowasurememo

import android.content.ClipboardManager
import android.content.Context
import android.database.Cursor
import android.view.LayoutInflater
import android.view.View
import android.widget.*
import androidx.fragment.app.FragmentManager

/**
 * データベースに対して処理を行うクラス
 * @author nakayama
 * @version 1.0
 */
class DatabaseControl {

    private lateinit var columnNames: List<String>
    private lateinit var editTexts: List<EditText>
    private lateinit var views: List<View>

    private var context: Context
    private var table: String
    private var tagId = 0
    private var _category: String? = null
    private var str: String? = null
    private var str2: String? = null
    private var str3: String? = null
    private var str4: String? = null

    private lateinit var manager: FragmentManager
    private lateinit var cm: ClipboardManager


    /**
     * コンストラクタ
     * 特定のカラムなどを指定しないときに使用するコンストラクタ
     * @param c ９種類のメモのコンテキスト
     * @param ta データベースのテーブル名
     */
    constructor(c: Context, ta: String) {
        context = c
        table = ta
    }

    /**
     * コンストラクタ
     * データベースに格納した情報をEditTextにセットするときに使用するコンストラクタ
     * @param c ９種類のメモのコンテキスト
     * @param ta データベースのテーブル名
     * @param _columnNames データベースの列名の配列
     */
    constructor(c: Context, ta: String, _columnNames: List<String>) {
        context = c
        table = ta
        columnNames = _columnNames
    }

    /**
     * コンストラクタ
     * データベースに格納した情報をEditTextにセットするときに使用するコンストラクタ
     * DateMemo,UpdateMemoでDatePickerを実装するために使用する
     * @param c ９種類のメモのコンテキスト
     * @param ta データベースのテーブル名
     * @param _columnNames データベースの列名の配列
     * @param _manager フラグメントを操作するための変数
     */
    constructor(c: Context, ta: String, _columnNames: List<String>, _manager: FragmentManager) {
        context = c
        table = ta
        columnNames = _columnNames
        manager = _manager
    }

    /**
     * コンストラクタ
     * データベースに格納した情報をEditTextにセットするときに使用するコンストラクタ
     * PasswordMemoでClipbordを実装するために使用する
     * @param c ９種類のメモのコンテキスト
     * @param ta データベースのテーブル名
     * @param _columnNames データベースの列名の配列
     * @param _cm クリップボードを操作するための変数
     */
    constructor(c: Context, ta: String, _columnNames: List<String>, _cm: ClipboardManager) {
        context = c
        table = ta
        columnNames = _columnNames
        cm = _cm
    }

    /**
     * コンストラクタ
     * データベースにメモの内容をインサートするときに使用するコンストラクタ
     * インサートしたい文字列が１つのとき
     * @param c ９種類のメモのコンテキスト
     * @param ta データベースのテーブル名
     * @param tagid データベースのid番号
     * @param category メモの種類
     * @param st　インサートしたい文字列
     */
    constructor(c: Context, ta: String, tagid: Int, category: String, st: String) {
        context = c
        table = ta
        tagId = tagid
        _category = category
        str = st
    }

    /**
     * コンストラクタ
     * データベースにメモの内容をインサートするときに使用するコンストラクタ
     * インサートしたい文字列が２つのとき
     * @param c ９種類のメモのコンテキスト
     * @param ta データベースのテーブル名
     * @param tagid データベースのid番号
     * @param category メモの種類
     * @param st　インサートしたい文字列
     * @param st2　インサートしたい文字列
     */
    constructor(c: Context, ta: String, tagid: Int, category: String, st: String, st2: String) {
        context = c
        table = ta
        tagId = tagid
        _category = category
        str = st
        str2 = st2
    }

    /**
     * コンストラクタ
     * データベースにメモの内容をインサートするときに使用するコンストラクタ
     * インサートしたい文字列が３つのとき
     * @param c ９種類のメモのコンテキスト
     * @param ta データベースのテーブル名
     * @param tagid データベースのid番号
     * @param category メモの種類
     * @param st　インサートしたい文字列
     * @param st2　インサートしたい文字列
     * @param st3　インサートしたい文字列
     */
    constructor(c: Context, ta: String, tagid: Int, category: String, st: String, st2: String, st3: String) {
        context = c
        table = ta
        tagId = tagid
        _category = category
        str = st
        str2 = st2
        str3 = st3
    }

    /**
     * コンストラクタ
     * データベースにメモの内容をインサートするときに使用するコンストラクタ
     * インサートしたい文字列が４つのとき
     * @param c ９種類のメモのコンテキスト
     * @param ta データベースのテーブル名
     * @param tagid データベースのid番号
     * @param category メモの種類
     * @param st　インサートしたい文字列
     * @param st2　インサートしたい文字列
     * @param st3　インサートしたい文字列
     * @param st4　インサートしたい文字列
     */
    constructor(c: Context, ta: String, tagid: Int, category: String, st: String, st2: String, st3: String, st4: String) {
        context = c
        table = ta
        tagId = tagid
        _category = category
        str = st
        str2 = st2
        str3 = st3
        str4 = st4
    }

    /**
     * selectDatabaseメソッド
     * データベースのデータを全て取り出して、レイアウトの作成を行うメソッド
     * @param _llBaseLayout　activity.xmlに記述している最下層のview
     */
    fun selectDatabase(_llBaseLayout: LinearLayout) {

        val llBaseLayout = _llBaseLayout

        val _helper = Databasehelper(context)
        val db = _helper.writableDatabase
        val sqlSelect = "SELECT * FROM $table"
        val cursor = db.rawQuery(sqlSelect, null)
        while (cursor.moveToNext()) {
            when (table) {
                "size" -> {
                    editTexts = setViewIdSize(context, table, llBaseLayout)
                }
                "date1" -> {
                    editTexts = setViewIdDate(context, table, llBaseLayout, manager)
                }
                "address" -> {
                    editTexts = setViewIdAddress(context, table, llBaseLayout)
                }
                "car" -> {
                    val i = cursor.getColumnIndex("inputform")
                    val st = cursor.getString(i)
                    if (st == "name") {
                        editTexts = setViewIdCarName(context, table, llBaseLayout)
                        columnNames = listOf("carname")
                    } else if (st == "detail") {
                        editTexts = setViewIdCarDetail(context, table, llBaseLayout)
                        columnNames = listOf("carmemotitle", "carmemocontents")
                    }
                }
                "update1" -> {
                    editTexts = setViewIdUpdate(context, table, llBaseLayout, manager)
                }
                "password" -> {
                    editTexts = setViewIdPassword(context, table, llBaseLayout, cm)
                }
                "subsc" -> {
                    views = setViewIdSubsc(context, table, llBaseLayout)
                }
                "wishlist" -> {
                    editTexts = setViewIdWishlist(context, table, llBaseLayout)
                }
                "memo" -> {
                    editTexts = setViewIdMemo(context, table, llBaseLayout)
                }
            }
            if (table === "subsc") {
                setSpinnerDatabaseText(cursor, columnNames, views)
            } else {
                setDatabaseText(cursor, columnNames, editTexts)
            }
        }
    }

    /**
     * setDatabaseTextメソッド
     * データベースから文字列を取り出して、EditTextの配列に順番にセットする.columnNamesとeditTextsの配列の
     * 順番は一致させる
     * @param cursor　カーソル
     * @param columnNames　データベースの列名の変数
     * @param editTexts　EditTextの配列の変数
     */
    private fun setDatabaseText(cursor: Cursor, columnNames: List<String>, editTexts: List<EditText>) {
        for (index in columnNames.indices) {
            val i = cursor.getColumnIndex("${columnNames[index]}")
            var str = cursor.getString(i)
            editTexts[index].setText(str)

        }
    }

    /**
     * setDatabaseTextメソッド
     * データベースから文字列を取り出して、EditTextの配列に順番にセットする.
     * columnNamesとviewsの配列の順番は一致させる
     * SubscMemoでspinnerをセットするために使用する
     * @param cursor　カーソル
     * @param columnNames　データベースの列名の変数
     * @param views　Viewの配列の変数
     */
    private fun setSpinnerDatabaseText(cursor: Cursor, columnNames: List<String>, views: List<View>) {
        for (index in columnNames.indices) {
            val i = cursor.getColumnIndex("${columnNames[index]}")
            if (index == 2) {
                val intSpinnerIndex = cursor.getInt(i)
                val spinner = views[2] as Spinner
                spinner.setSelection(intSpinnerIndex)
            } else {
                var str = cursor.getString(i)
                val editText = views[index] as EditText
                editText.setText(str)
            }
        }
    }

    /**
     * setViewIdSizeメソッド
     * SizeMemoのレイアウトにviewを追加し、追加したviewの子viewをfindviewbyidして、EditTextの配列を返す
     * @param context　コンテキスト
     * @param table　データベースのテーブル名
     * @param llBaseLayout　activity.xmlに記述している最下層のview
     * @return EditTextの配列
     */
    private fun setViewIdSize(context: Context, table: String, llBaseLayout: LinearLayout): List<EditText> {
        val inflater = LayoutInflater.from(context)
        val llAddLayout = inflater.inflate(R.layout.size_inputform, null) as LinearLayout
        llBaseLayout.addView(llAddLayout)
        val etBodyPart = llAddLayout.findViewById<EditText>(R.id.et_bodypart)
        val etRecord = llAddLayout.findViewById<EditText>(R.id.et_record)
        val etUnit = llAddLayout.findViewById<EditText>(R.id.et_unit)
        val btDelete = llAddLayout.findViewById<ImageButton>(R.id.bt_delete)
        btDelete.setOnClickListener(DeleteButton(context, llBaseLayout, llAddLayout, table))
        return listOf(etBodyPart, etRecord, etUnit)
    }

    /**
     * setViewIdDateメソッド
     * DateMemoのレイアウトにviewを追加し、追加したviewの子viewをfindviewbyidして、EditTextの配列を返す
     * @param context　コンテキスト
     * @param table　データベースのテーブル名
     * @param llBaseLayout　activity.xmlに記述している最下層のview
     * @param manager　Datepicker実装用のFragmentmanager型の変数
     * @return EditTextの配列
     */
    private fun setViewIdDate(context: Context, table: String, llBaseLayout: LinearLayout, manager: FragmentManager): List<EditText> {
        val inflater = LayoutInflater.from(context)
        val llAddLayout = inflater.inflate(R.layout.date_inputform, null) as LinearLayout
        llBaseLayout.addView(llAddLayout)
        val llDateTitle = llAddLayout.findViewById<LinearLayout>(R.id.ll_date_title)
        val llDateSelect = llAddLayout.findViewById<LinearLayout>(R.id.ll_date_select)
        val etDateTitle = llDateTitle.findViewById<EditText>(R.id.et_date_title)
        val etDateYear = llDateSelect.findViewById<EditText>(R.id.et_date_year)
        val etDateMonth = llDateSelect.findViewById<EditText>(R.id.et_date_month)
        val etDateDay = llDateSelect.findViewById<EditText>(R.id.et_date_day)
        val btDelete = llDateSelect.findViewById<ImageButton>(R.id.bt_delete)
        btDelete.setOnClickListener(DeleteButton(context, llBaseLayout, llAddLayout, table))
        val btDateSelect = llDateSelect.findViewById<ImageButton>(R.id.bt_date_select)
        btDateSelect.setOnClickListener(DatePickerListener(context, manager, table))
        return listOf(etDateTitle, etDateYear, etDateMonth, etDateDay)
    }

    /**
     * setViewIdAddressメソッド
     * AddressMemoのレイアウトにviewを追加し、追加したviewの子viewをfindviewbyidして、EditTextの配列を返す
     * @param context　コンテキスト
     * @param table　データベースのテーブル名
     * @param llBaseLayout　activity.xmlに記述している最下層のview
     * @return EditTextの配列
     */
    private fun setViewIdAddress(context: Context, table: String, llBaseLayout: LinearLayout): List<EditText> {
        val inflater = LayoutInflater.from(context)
        val llAddLayout = inflater.inflate(R.layout.address_inputform, null) as LinearLayout
        llBaseLayout.addView(llAddLayout)
        val llAddressFrame = llAddLayout.findViewById<LinearLayout>(R.id.ll_address_frame)
        val llPostNumberinputform = llAddressFrame.findViewById<LinearLayout>(R.id.ll_postnumber_inputform)
        val llAddressTitle = llAddressFrame.findViewById<LinearLayout>(R.id.ll_address_title)
        val etAddressTitle = llAddressTitle.findViewById<EditText>(R.id.et_address_title)
        val etPostNumber1 = llPostNumberinputform.findViewById<EditText>(R.id.et_postnumber1)
        val etPostNumber2 = llPostNumberinputform.findViewById<EditText>(R.id.et_postnumber2)
        val etAddressDetail = llAddressFrame.findViewById<EditText>(R.id.et_address_detail)

        val btDelete = llAddressTitle.findViewById<ImageButton>(R.id.bt_delete)
        btDelete.setOnClickListener(DeleteButton(context, llBaseLayout, llAddLayout, table))
        val btPostNumberSearch = llPostNumberinputform.findViewById<Button>(R.id.bt_postnumber_search)
        btPostNumberSearch.setOnClickListener {
            PostNumberAPIClient(context).getPostNumber(etPostNumber1, etPostNumber2, etAddressDetail)
        }


        return listOf(etAddressTitle, etPostNumber1, etPostNumber2, etAddressDetail)
    }

    /**
     * setViewIdCarNameメソッド
     * CarMemoのレイアウトに車種名を入力するviewを追加し、追加したviewの子viewをfindviewbyidして、EditTextの配列を返す
     * @param context　コンテキスト
     * @param table　データベースのテーブル名
     * @param llBaseLayout　activity.xmlに記述している最下層のview
     * @return EditTextの配列
     */
    private fun setViewIdCarName(context: Context, table: String, llBaseLayout: LinearLayout): List<EditText> {
        val inflater = LayoutInflater.from(context)
        val llAddLayout = inflater.inflate(R.layout.car_name_inputform, null) as LinearLayout
        llBaseLayout.addView(llAddLayout)
        val etCarName = llAddLayout.findViewById<EditText>(R.id.et_car_name)
        val btDelete = llAddLayout.findViewById<ImageButton>(R.id.bt_delete)
        btDelete.setOnClickListener(DeleteButton(context, llBaseLayout, llAddLayout, table))

        //viewの追加ボタンにリスナーをセット
        val btCarDetailAdd = llAddLayout.findViewById<ImageButton>(R.id.bt_cardetail_add)
        btCarDetailAdd.setOnClickListener(AddCarDetail(context, llBaseLayout))
        return listOf(etCarName)
    }

    /**
     * setViewIdCarDetailメソッド
     * CarMemoのレイアウトに車の詳細を入力するviewを追加し、追加したviewの子viewをfindviewbyidして、EditTextの配列を返す
     * @param context　コンテキスト
     * @param table　データベースのテーブル名
     * @param llBaseLayout　activity.xmlに記述している最下層のview
     * @return EditTextの配列
     */
    private fun setViewIdCarDetail(context: Context, table: String, llBaseLayout: LinearLayout): List<EditText> {
        val inflater = LayoutInflater.from(context)
        val llAddLayout = inflater.inflate(R.layout.car_detail_inputform, null) as LinearLayout
        llBaseLayout.addView(llAddLayout)
        val etCarMemoTitle = llAddLayout.findViewById<EditText>(R.id.et_car_memo_title)
        val etCarMemoContents = llAddLayout.findViewById<EditText>(R.id.et_car_memo_contents)
        val btDelete = llAddLayout.findViewById<ImageButton>(R.id.bt_delete)
        btDelete.setOnClickListener(DeleteButton(context, llBaseLayout, llAddLayout, table))
        return listOf(etCarMemoTitle, etCarMemoContents)
    }

    /**
     * setViewIdUpdateメソッド
     * UpdateMemoのレイアウトにviewを追加し、追加したviewの子viewをfindviewbyidして、EditTextの配列を返す
     * @param context　コンテキスト
     * @param table　データベースのテーブル名
     * @param llBaseLayout　activity.xmlに記述している最下層のview
     * @param manager　Datepicker実装用のFragmentmanager型の変数
     * @return EditTextの配列
     */
    private fun setViewIdUpdate(context: Context, table: String, llBaseLayout: LinearLayout, manager: FragmentManager): List<EditText> {
        val inflater = LayoutInflater.from(context)
        val llAddLayout = inflater.inflate(R.layout.update_inputform, null) as LinearLayout
        llBaseLayout.addView(llAddLayout)
        val llUpdateTitle = llAddLayout.findViewById<LinearLayout>(R.id.ll_update_title)
        val llUpdateDeadline = llUpdateTitle.findViewById<LinearLayout>(R.id.ll_update_deadline)
        val etUpdateTitle = llUpdateTitle.findViewById<EditText>(R.id.et_update_title)
        val etUpdateYear = llUpdateDeadline.findViewById<EditText>(R.id.et_update_year)
        val etUpdateMonth = llUpdateDeadline.findViewById<EditText>(R.id.et_update_month)
        val etUpdateDay = llUpdateDeadline.findViewById<EditText>(R.id.et_update_day)
        val btDelete = llUpdateDeadline.findViewById<ImageButton>(R.id.bt_delete)
        btDelete.setOnClickListener(DeleteButton(context, llBaseLayout, llAddLayout, table))
        val btDateSelect = llUpdateDeadline.findViewById<ImageButton>(R.id.bt_date_select)
        btDateSelect.setOnClickListener(DatePickerListener(context, manager, table))
        return listOf(etUpdateTitle, etUpdateYear, etUpdateMonth, etUpdateDay)
    }

    /**
     * setViewIdPasswordメソッド
     * PasswordMemoのレイアウトにviewを追加し、追加したviewの子viewをfindviewbyidして、EditTextの配列を返す
     * @param context　コンテキスト
     * @param table　データベースのテーブル名
     * @param llBaseLayout　activity.xmlに記述している最下層のview
     * @param cm クリップボードを実装するために使用するClipboardManager型の変数
     * @return EditTextの配列
     */
    private fun setViewIdPassword(context: Context, table: String, llBaseLayout: LinearLayout, cm: ClipboardManager): List<EditText> {
        val inflater = LayoutInflater.from(context)
        val llAddLayout = inflater.inflate(R.layout.password_inputform, null) as LinearLayout
        llBaseLayout.addView(llAddLayout)
        val llPasswordFrame = llAddLayout.findViewById<LinearLayout>(R.id.ll_password_frame)
        val llPasswordTitle = llPasswordFrame.findViewById<LinearLayout>(R.id.ll_password_title)
        val llPasswordContents = llPasswordFrame.findViewById<LinearLayout>(R.id.ll_password_contents)
        val etPasswordTitle = llPasswordTitle.findViewById<EditText>(R.id.et_password_title)
        val etPasswordContents = llPasswordContents.findViewById<EditText>(R.id.et_password_contents)
        val btDelete = llPasswordTitle.findViewById<ImageButton>(R.id.bt_delete)
        btDelete.setOnClickListener(DeleteButton(context, llBaseLayout, llAddLayout, table))
        val btClip = llPasswordContents.findViewById<ImageButton>(R.id.bt_clip)
        btClip.setOnClickListener(ClipButtonListener(context, etPasswordContents, cm))
        return listOf(etPasswordTitle, etPasswordContents)
    }

    /**
     * setViewIdSubscメソッド
     * SubscMemoのレイアウトにviewを追加し、追加したviewの子viewをfindviewbyidして、EditTextの配列を返す
     * @param context　コンテキスト
     * @param table　データベースのテーブル名
     * @param llBaseLayout　activity.xmlに記述している最下層のview
     * @return viewの配列
     */
    private fun setViewIdSubsc(context: Context, table: String, llBaseLayout: LinearLayout): List<View> {
        val inflater = LayoutInflater.from(context)
        val llAddLayout = inflater.inflate(R.layout.subsc_inputform, null) as LinearLayout
        llBaseLayout.addView(llAddLayout)
        val llSubscFrame = llAddLayout.findViewById<LinearLayout>(R.id.ll_subsc_frame)
        val llSubscTitle = llSubscFrame.findViewById<LinearLayout>(R.id.ll_subsc_title)
        val llSubscPrice = llSubscFrame.findViewById<LinearLayout>(R.id.ll_subsc_price)
        val etSubscTitle = llSubscTitle.findViewById<EditText>(R.id.et_subsc_title)
        val etSubscPrice = llSubscPrice.findViewById<EditText>(R.id.et_subsc_price)
        val btDelete = llSubscTitle.findViewById<ImageButton>(R.id.bt_delete)
        btDelete.setOnClickListener(DeleteButton(context, llBaseLayout, llAddLayout, table))
        val spPaymentInterbal = llSubscPrice.findViewById<Spinner>(R.id.sp_payment_interbal)
        return listOf(etSubscTitle, etSubscPrice, spPaymentInterbal)
    }

    /**
     * setViewIdWishlistメソッド
     * WishlistMemoのレイアウトにviewを追加し、追加したviewの子viewをfindviewbyidして、EditTextの配列を返す
     * @param context　コンテキスト
     * @param table　データベースのテーブル名
     * @param llBaseLayout　activity.xmlに記述している最下層のview
     * @return EditTextの配列
     */
    private fun setViewIdWishlist(context: Context, table: String, llBaseLayout: LinearLayout): List<EditText> {
        val inflater = LayoutInflater.from(context)
        val llAddLayout = inflater.inflate(R.layout.wishlist_inputform, null) as LinearLayout
        llBaseLayout.addView(llAddLayout)
        val llWishlistTitle = llAddLayout.findViewById<LinearLayout>(R.id.ll_wishlist_title)
        val etWishlistTitle = llWishlistTitle.findViewById<EditText>(R.id.et_wishlist_title)
        val btDelete = llWishlistTitle.findViewById<ImageButton>(R.id.bt_delete)
        btDelete.setOnClickListener(DeleteButton(context, llBaseLayout, llAddLayout, table))
        return listOf(etWishlistTitle)
    }

    /**
     * setViewIdMemoメソッド
     * MemoMemoのレイアウトにviewを追加し、追加したviewの子viewをfindviewbyidして、EditTextの配列を返す
     * @param context　コンテキスト
     * @param table　データベースのテーブル名
     * @param llBaseLayout　activity.xmlに記述している最下層のview
     * @return EditTextの配列
     */
    private fun setViewIdMemo(context: Context, table: String, llBaseLayout: LinearLayout): List<EditText> {
        val inflater = LayoutInflater.from(context)
        val llAddLayout = inflater.inflate(R.layout.memo_inputform, null) as LinearLayout
        llBaseLayout.addView(llAddLayout)
        val llMemoFrame = llAddLayout.findViewById<LinearLayout>(R.id.ll_memo_frame)
        val llMemoTitle = llMemoFrame.findViewById<LinearLayout>(R.id.ll_memo_title)
        val etMemoTitle = llMemoTitle.findViewById<EditText>(R.id.et_memo_title)
        val etMemoContents = llMemoFrame.findViewById<EditText>(R.id.et_memo_contents)
        val btDelete = llMemoTitle.findViewById<ImageButton>(R.id.bt_delete)
        btDelete.setOnClickListener(DeleteButton(context, llBaseLayout, llAddLayout, table))
        return listOf(etMemoTitle, etMemoContents)
    }

    /**
     * deleteAllDatabaseメソッド
     * データベースにあるすべてのデータを削除する
     */
    fun deleteAllDatabase() {
        val _helper = Databasehelper(context)
        val db = _helper.writableDatabase
        val sqlDelete = "DELETE FROM $table"
        val statement = db.compileStatement(sqlDelete)
        statement.executeUpdateDelete()
    }

    /**
     * insertDatabaseOneColumnsメソッド
     * メモの入力欄が１か所のデータをデータベースにインサートする　
     * @param column1 メモの内容を格納するデータベースの列名
     */
    fun insertDatabaseOneColumns(column1: String) {
        val sqlInsert = "INSERT INTO " + table + " " +
                "(_id,category," + column1 + ") " +
                "VALUES(?,?,?)"
        val _helper = Databasehelper(context)
        val db = _helper.writableDatabase
        val statement = db.compileStatement(sqlInsert)
        statement.bindLong(1, tagId.toLong())
        statement.bindString(2, _category)
        statement.bindString(3, str)
        statement.executeInsert()
    }

    /**
     * insertDatabaseTwoColumnsメソッド
     * メモの入力欄が２か所のデータをデータベースにインサートする　
     * @param column1 メモの内容を格納するデータベースの列名
     * @param column2 メモの内容を格納するデータベースの列名
     */
    fun insertDatabaseTwoColumns(column1: String, column2: String) {
        val sqlInsert = "INSERT INTO " + table + " " +
                "(_id,category," + column1 + "," + column2 + ") " +
                "VALUES(?,?,?,?)"
        val _helper = Databasehelper(context)
        val db = _helper.writableDatabase
        val statement = db.compileStatement(sqlInsert)
        statement.bindLong(1, tagId.toLong())
        statement.bindString(2, _category)
        statement.bindString(3, str)
        statement.bindString(4, str2)
        statement.executeInsert()
    }

    /**
     * insertDatabaseThreeColumnsメソッド
     * メモの入力欄が3か所のデータをデータベースにインサートする　
     * @param column1 メモの内容を格納するデータベースの列名
     * @param column2 メモの内容を格納するデータベースの列名
     * @param column3 メモの内容を格納するデータベースの列名
     */
    fun insertDatabaseThreeColumns(column1: String, column2: String, column3: String) {
        val sqlInsert = "INSERT INTO " + table + " " +
                "(_id,category," + column1 + "," + column2 + "," + column3 + ") " +
                "VALUES(?,?,?,?,?)"
        val _helper = Databasehelper(context)
        val db = _helper.writableDatabase
        val statement = db.compileStatement(sqlInsert)
        statement.bindLong(1, tagId.toLong())
        statement.bindString(2, _category)
        statement.bindString(3, str)
        statement.bindString(4, str2)
        statement.bindString(5, str3)
        statement.executeInsert()
    }

    /**
     * insertDatabaseFourColumnsメソッド
     * メモの入力欄が4か所のデータをデータベースにインサートする　
     * @param column1 メモの内容を格納するデータベースの列名
     * @param column2 メモの内容を格納するデータベースの列名
     * @param column3 メモの内容を格納するデータベースの列名
     * @param column4 メモの内容を格納するデータベースの列名
     */
    fun insertDatabaseFourColumns(column1: String, column2: String, column3: String, column4: String) {
        val sqlInsert = "INSERT INTO " + table + " " +
                "(_id,category," + column1 + "," + column2 + "," + column3 + "," + column4 + ") " +
                "VALUES(?,?,?,?,?,?)"
        val _helper = Databasehelper(context)
        val db = _helper.writableDatabase
        val statement = db.compileStatement(sqlInsert)
        statement.bindLong(1, tagId.toLong())
        statement.bindString(2, _category)
        statement.bindString(3, str)
        statement.bindString(4, str2)
        statement.bindString(5, str3)
        statement.bindString(6, str4)
        statement.executeInsert()
    }
}