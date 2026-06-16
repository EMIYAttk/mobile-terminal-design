package com.example.mydatabase;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import androidx.annotation.Nullable;

public class MyDBHelper extends SQLiteOpenHelper {

    // 数据库名和版本
    private static final String DB_NAME = "my.db";
    private static final int DB_VERSION = 1;

    // 表名和列名
    public static final String TABLE_NAME = "my_table";
    public static final String COL_ID = "id";
    public static final String COL_NAME = "name";

    // 构造方法
    public MyDBHelper(@Nullable Context context) {
        super(context, DB_NAME, null, DB_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        // 创建表的 SQL 语句
        String createSQL = "CREATE TABLE " + TABLE_NAME + " ("
                + COL_ID + " INTEGER PRIMARY KEY, "
                + COL_NAME + " TEXT)";
        db.execSQL(createSQL);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        // 暂时不需要实现，直接删除旧表重建（简单处理）
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME);
        onCreate(db);
    }
}