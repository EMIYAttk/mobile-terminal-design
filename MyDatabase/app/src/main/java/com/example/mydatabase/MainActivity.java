package com.example.mydatabase;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

public class MainActivity extends AppCompatActivity {

    private MyDBHelper helper;
    private EditText etId, etName;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        helper = new MyDBHelper(this);
        etId = findViewById(R.id.editText2);   // 编号输入框
        etName = findViewById(R.id.editText1);  // 名称输入框
    }

    // 插入按钮
    public void insert(View view) {
        String idStr = etId.getText().toString().trim();
        String name = etName.getText().toString().trim();

        if (idStr.isEmpty() || name.isEmpty()) {
            Toast.makeText(this, "编号和名称不能为空", Toast.LENGTH_SHORT).show();
            return;
        }

        int id = Integer.parseInt(idStr);
        SQLiteDatabase db = helper.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(MyDBHelper.COL_ID, id);
        values.put(MyDBHelper.COL_NAME, name);

        long result = db.insert(MyDBHelper.TABLE_NAME, null, values);
        db.close();

        if (result != -1) {
            Toast.makeText(this, "插入成功", Toast.LENGTH_SHORT).show();
            etId.setText("");
            etName.setText("");
        } else {
            Toast.makeText(this, "插入失败，可能编号已存在", Toast.LENGTH_SHORT).show();
        }
    }

    // 查询按钮
    public void query(View view) {
        String idStr = etId.getText().toString().trim();
        if (idStr.isEmpty()) {
            Toast.makeText(this, "请输入要查询的编号", Toast.LENGTH_SHORT).show();
            return;
        }

        int id = Integer.parseInt(idStr);
        SQLiteDatabase db = helper.getReadableDatabase();
        String[] columns = {MyDBHelper.COL_NAME};
        String selection = MyDBHelper.COL_ID + " = ?";
        String[] selectionArgs = {String.valueOf(id)};

        Cursor cursor = db.query(MyDBHelper.TABLE_NAME, columns, selection, selectionArgs,
                null, null, null);

        if (cursor != null && cursor.moveToFirst()) {
            String name = cursor.getString(cursor.getColumnIndexOrThrow(MyDBHelper.COL_NAME));
            Toast.makeText(this, "编号 " + id + " 对应的名称为：" + name, Toast.LENGTH_LONG).show();
            cursor.close();
        } else {
            Toast.makeText(this, "未找到编号 " + id + " 的记录", Toast.LENGTH_SHORT).show();
        }
        db.close();
    }

    // 修改按钮（根据编号更新名称）
    public void update(View view) {
        String idStr = etId.getText().toString().trim();
        String newName = etName.getText().toString().trim();

        if (idStr.isEmpty() || newName.isEmpty()) {
            Toast.makeText(this, "请填写编号和新名称", Toast.LENGTH_SHORT).show();
            return;
        }

        int id = Integer.parseInt(idStr);
        SQLiteDatabase db = helper.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(MyDBHelper.COL_NAME, newName);

        int rows = db.update(MyDBHelper.TABLE_NAME, values,
                MyDBHelper.COL_ID + " = ?", new String[]{String.valueOf(id)});
        db.close();

        if (rows > 0) {
            Toast.makeText(this, "修改成功，已将编号 " + id + " 的名称改为：" + newName, Toast.LENGTH_LONG).show();
            etName.setText("");  // 清空名称框，方便下次输入
        } else {
            Toast.makeText(this, "修改失败，未找到编号 " + id + " 的记录", Toast.LENGTH_SHORT).show();
        }
    }

    // 删除按钮（根据编号删除记录）
    public void delete(View view) {
        String idStr = etId.getText().toString().trim();
        if (idStr.isEmpty()) {
            Toast.makeText(this, "请输入要删除的编号", Toast.LENGTH_SHORT).show();
            return;
        }

        int id = Integer.parseInt(idStr);
        SQLiteDatabase db = helper.getWritableDatabase();
        int rows = db.delete(MyDBHelper.TABLE_NAME,
                MyDBHelper.COL_ID + " = ?", new String[]{String.valueOf(id)});
        db.close();

        if (rows > 0) {
            Toast.makeText(this, "删除成功，编号 " + id + " 的记录已删除", Toast.LENGTH_LONG).show();
            etId.setText("");
            etName.setText("");
        } else {
            Toast.makeText(this, "删除失败，未找到编号 " + id + " 的记录", Toast.LENGTH_SHORT).show();
        }
    }
}