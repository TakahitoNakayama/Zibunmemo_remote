package com.websarva.wings.android.dowasurememo

import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper

//データベース作成クラス
class Databasehelper(context: Context) : SQLiteOpenHelper(context, DATABASE_NAME, null, DATABASE_VERSION) {

    companion object {
        private const val DATABASE_NAME = "zibunmemo"
        private const val DATABASE_VERSION = 23
    }


    override fun onCreate(db: SQLiteDatabase) {
        val buildersize = StringBuilder()
        buildersize.append("CREATE TABLE size " +
                "(_id INTEGER,category TEXT,bodypart TEXT,records TEXT" +
                ",unit TEXT,memo TEXT)")
        val sqlcreate = buildersize.toString()
        db.execSQL(sqlcreate)

        val builderdate = StringBuilder()
        builderdate.append("CREATE TABLE date1 " +
                "(_id INTEGER,category TEXT,datetitle TEXT," +
                "dateyear TEXT,datemonth TEXT,dateday TEXT,memo TEXT)")
        val sqldatecreate = builderdate.toString()
        db.execSQL(sqldatecreate)

        val builderaddress = StringBuilder()
        builderaddress.append("CREATE TABLE address " +
                "(_id INTEGER,category TEXT,addresstitle TEXT," +
                "postnumber1 TEXT,postnumber2 TEXT,addressdetail TEXT,memo TEXT)")
        val sqladdresscreate = builderaddress.toString()
        db.execSQL(sqladdresscreate)

        val buildercar = StringBuilder()
        buildercar.append("CREATE TABLE car " +
                "(_id INTEGER,category TEXT,carname TEXT," +
                "carmemotitle TEXT,carmemocontents TEXT,inputform TEXT,memo TEXT)")
        val sqlcarcreate = buildercar.toString()
        db.execSQL(sqlcarcreate)

        val builderupdate = StringBuilder()
        builderupdate.append("CREATE TABLE update1 " +
                "(_id INTEGER,category TEXT,updatetitle TEXT," +
                "updateyear TEXT,updatemonth TEXT,updateday TEXT,memo TEXT)")
        val sqlupdatecreate = builderupdate.toString()
        db.execSQL(sqlupdatecreate)

        val builderpassword = StringBuilder()
        builderpassword.append("CREATE TABLE password " +
                "(_id INTEGER,category TEXT,passwordtitle TEXT," +
                "passwordcontents TEXT,memo TEXT)")
        val sqlpasswordcreate = builderpassword.toString()
        db.execSQL(sqlpasswordcreate)

        val buildersubsc = StringBuilder()
        buildersubsc.append("CREATE TABLE subsc " +
                "(_id INTEGER,category TEXT,subsctitle TEXT,subscprice TEXT,subscinterbal TEXT,memo TEXT)")
        val sqlsubsccreate = buildersubsc.toString()
        db.execSQL(sqlsubsccreate)

        val builderwishlist = StringBuilder()
        builderwishlist.append("CREATE TABLE wishlist " +
                "(_id INTEGER,category TEXT,wishlisttitle TEXT," +
                "memo TEXT)")
        val sqlwishlistcreate = builderwishlist.toString()
        db.execSQL(sqlwishlistcreate)

        val buildermemo = StringBuilder()
        buildermemo.append("CREATE TABLE memo " +
                "(_id INTEGER,category TEXT,memotitle TEXT," +
                "memocontents TEXT,memo TEXT)")
        val sqlmemocreate = buildermemo.toString()
        db.execSQL(sqlmemocreate)
    }

    override fun onUpgrade(db: SQLiteDatabase, oldVersion: Int, newVersion: Int) {
        if (newVersion > oldVersion) {
        }
    }


}