package com.yahoo.simpletodo;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.util.ArrayList;
import java.util.List;

public class TodoDbHelper extends SQLiteOpenHelper {

    private static final String DATABASE_NAME = "tododb";
    private static final int DATABASE_VERSION = 1;
    private static final String TABLE_TODO = "todo";

    private static final String KEY_TODO_ID = "id";
    private static final String KEY_TODO_ITEM = "todoItem";
    private static final String KEY_TODO_PRIORITY = "todoPriority";

    private static TodoDbHelper sInstance;

    public static synchronized TodoDbHelper getsInstance(Context context) {
        if (sInstance == null) {
            sInstance = new TodoDbHelper(context);
        }
        return sInstance;
    }

    private TodoDbHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String CREATE_TODO_TABLE = "CREATE TABLE " + TABLE_TODO +
                "(" +
                KEY_TODO_ID + " INTEGER PRIMARY KEY," +
                KEY_TODO_ITEM + " TEXT," +
                KEY_TODO_PRIORITY + " TEXT" +
                ")";

        db.execSQL(CREATE_TODO_TABLE);
    }

    @Override
    public void onConfigure(SQLiteDatabase db) {
        super.onConfigure(db);
        db.setForeignKeyConstraintsEnabled(true);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        if (oldVersion != newVersion) {
            db.execSQL("DROP TABLE IF EXISTS " + TABLE_TODO);
            onCreate(db);
        }
    }

    public long addTodoItem(Todo item) {
        SQLiteDatabase db = getWritableDatabase();
        db.beginTransaction();
        long id = -1;
        try {
            ContentValues values = new ContentValues();
            values.put(KEY_TODO_ITEM, item.getTodoItem());
            id = db.insertOrThrow(TABLE_TODO, null, values);
            db.setTransactionSuccessful();
        } catch(Exception e) {
            System.out.println("Error writing to the DB "+e.getMessage());
        } finally {
            db.endTransaction();
        }
        return id;
    }

    public int updateItem(Todo item) {
        SQLiteDatabase db = getWritableDatabase();
        db.beginTransaction();
        int rows = -1;
        try {
            ContentValues values = new ContentValues();
            values.put(KEY_TODO_ITEM, item.getTodoItem());
            rows = db.update(TABLE_TODO, values, KEY_TODO_ID + " = ?", new String[]{String.valueOf(item.getId())});
            db.setTransactionSuccessful();
        } catch(Exception e) {

        } finally {
            db.endTransaction();
        }
        return rows;
    }

    public void deleteItem(Todo item) {
        SQLiteDatabase db = getWritableDatabase();
        db.beginTransaction();
        try {
            db.delete(TABLE_TODO, KEY_TODO_ID + " = ?", new String[] {String.valueOf(item.getId())});
            db.setTransactionSuccessful();
        } catch(Exception e) {

        } finally {
            db.endTransaction();
        }
    }

    public List<Todo> getAllItems() {
        List<Todo> todoList = new ArrayList<>();
        String TODO_QUERY = String.format("SELECT * from %s", TABLE_TODO);
        SQLiteDatabase db = getReadableDatabase();
        Cursor cursor = db.rawQuery(TODO_QUERY, null);
        try {
            if (cursor.moveToFirst()) {
                do {
                    Todo item = new Todo();
                    item.setTodoItem(cursor.getString(cursor.getColumnIndex(KEY_TODO_ITEM)));
                    item.setId(cursor.getLong(cursor.getColumnIndex(KEY_TODO_ID)));
                    todoList.add(item);
                } while(cursor.moveToNext());
            }
        } catch(Exception e) {

        } finally {
            if (cursor != null && !cursor.isClosed()) {
                cursor.close();
            }
        }
        return todoList;
    }
}
