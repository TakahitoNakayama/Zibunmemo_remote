package com.websarva.wings.android.dowasurememo;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteStatement;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.LinearLayout;

import java.util.ArrayList;

//viewを削除するための処理
public class DeleteButton extends LinearLayout implements View.OnClickListener{

    //削除するビューの親ビュー
    LinearLayout activityLinearLayout;

    //削除するビュー
    LinearLayout linearLayout;
    Databasehelper _helper;
    Context context;
    String table;
    int tagId;

    //コンストラクタ
    public DeleteButton
            (Context c,LinearLayout llactivity,LinearLayout parentview,String ta) {
        super(c);
        activityLinearLayout=llactivity;
        linearLayout=parentview;
        context=c;
        table=ta;
    }


    @Override
    public void onClick(View v) {
        activityLinearLayout.removeView(linearLayout);
        tagId=activityLinearLayout.getChildCount()+2;
        _helper=new Databasehelper(context);
        SQLiteDatabase db=_helper.getWritableDatabase();
        String sqlDelete="DELETE FROM "+table+" WHERE _id = ?";
        SQLiteStatement statement=db.compileStatement(sqlDelete);
        statement.bindLong(1,tagId);
        statement.executeUpdateDelete();

    }
}

