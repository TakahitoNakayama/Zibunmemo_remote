package com.websarva.wings.android.dowasurememo

import android.content.Context
import android.view.View
import android.widget.LinearLayout

//viewを削除するための処理
class DeleteButton(context: Context,
/*削除するビューの親ビュー */
                   var activityLinearLayout: LinearLayout,
/* 削除するビュー*/
                   var linearLayout: LinearLayout?,
                   var table: String) : LinearLayout(context), View.OnClickListener {
    var tagId = 0
    override fun onClick(v: View) {
        activityLinearLayout.removeView(linearLayout)
        tagId = activityLinearLayout.childCount + 2
        val _helper = Databasehelper(context)
        val db = _helper.writableDatabase
        val sqlDelete = "DELETE FROM $table WHERE _id = ?"
        val statement = db.compileStatement(sqlDelete)
        statement.bindLong(1, tagId.toLong())
        statement.executeUpdateDelete()
    }
}