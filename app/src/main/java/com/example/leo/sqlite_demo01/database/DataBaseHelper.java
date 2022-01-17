package com.example.leo.sqlite_demo01.database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.support.annotation.Nullable;
import android.util.Log;

import com.example.leo.sqlite_demo01.model.Order;

import java.util.ArrayList;
import java.util.List;

public class DataBaseHelper extends SQLiteOpenHelper {
    private static final String TAG = "DataBaseHelper";
    Context context;
    //DATABASE
    private static final int DATABASE_VER = 1;
    private static final String DATABASE_NAME = "MyData.db";
    //TABLE
    private static final String TABLE_NAME = "Product";
    //COLUMN
    private static final String KEY_ID = "Id";
    private static final String KEY_Name = "NAME";
    private static final String KEY_Quantity = "Quantity";
    private static final String KEY_Price = "Price";
    //instance
    private static DataBaseHelper instance;


    //Constructor, private防止直接實體化
    private DataBaseHelper(@Nullable Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VER);
    }

    //Singleton, avoid memory leaks and unnecessary re-allocations
    public static synchronized DataBaseHelper getInstance(Context context) {
        if (instance == null) {
            instance = new DataBaseHelper(context.getApplicationContext());
        }
        return instance;
    }

    //只有第一次建立會呼叫此方法
    @Override
    public void onCreate(SQLiteDatabase db) {
        String CREATE_TABLE =
                "CREATE TABLE " + TABLE_NAME
                        + " ("
                        + KEY_ID + " INTEGER PRIMARY KEY , "
                        + KEY_Name + " TEXT, "
                        + KEY_Quantity + " TEXT, "
                        + KEY_Price + " TEXT"
                        + ")";
        db.execSQL(CREATE_TABLE);
    }

    //更新資料庫結構時呼叫
    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        if (oldVersion != newVersion) {
            db.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME);
            onCreate(db);
        }
    }


    //CRUD
    //Create
    public void addOrder(Order order) {
        SQLiteDatabase db = getWritableDatabase();

        // It's a good idea to wrap our insert in a transaction. This helps with performance and ensures
        // consistency of the database.
        db.beginTransaction();
        try {

            ContentValues values = new ContentValues();
            values.put(KEY_ID, order.getProductId());
            values.put(KEY_Name, order.getProductName());
            values.put(KEY_Quantity, order.getQuantity());
            values.put(KEY_Price, order.getPrice());

            db.insertOrThrow(TABLE_NAME, null, values);
            db.setTransactionSuccessful();
        } catch (Exception e) {
            Log.d(TAG, "Error while trying to add to database or data exist");
        } finally {
            db.endTransaction();
        }
    }

    //Create or 如果已存在就 Update
    public long addOrUpdateOrder(Order order) {
        SQLiteDatabase db = getWritableDatabase();
        long Id = -1;

        db.beginTransaction();
        try {
            ContentValues values = new ContentValues();
            values.put(KEY_ID, order.getProductId());
            values.put(KEY_Name, order.getProductName());
            values.put(KEY_Price, order.getPrice());
            values.put(KEY_Quantity, order.getQuantity());

            // First try to update the order in case the order already exists in the database
            // This assumes KEY_ID are unique
            int rows = db.update(TABLE_NAME, values, KEY_ID + " =?", new String[]{String.valueOf(order.getProductId())});

            // Check if update succeeded
            if (rows == 1) {
                Log.d(TAG, "rows == 1");
                String orderSelectQuery = String.format("SELECT %s FROM %s WHERE %s =?", KEY_ID, TABLE_NAME, KEY_ID);
                Cursor cursor = db.rawQuery(orderSelectQuery, new String[]{String.valueOf(order.getProductId())});
                try {
                    if (cursor.moveToFirst()) {
                        Log.d(TAG, "rows == 1 cursor.moveToFirst()");
                        Id = cursor.getInt(0); //KEY_ID
                        db.setTransactionSuccessful();
                    }
                } finally {
                    if (cursor != null && !cursor.isClosed()) {
                        cursor.close();
                    }
                }
            } else {
                Id = db.insertOrThrow(TABLE_NAME, null, values);
                db.setTransactionSuccessful();
            }
        } catch (Exception e) {
            Log.d(TAG, "Error while trying to add or update ");
        } finally {
            db.endTransaction();
        }
        Log.d(TAG, "Id = " + Id);
        return Id;
    }

    //Update
    public int updateOrder(Order order) {
        SQLiteDatabase db = getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(KEY_ID, order.getProductId());
        values.put(KEY_Name, order.getProductName());
        values.put(KEY_Quantity, order.getQuantity());
        values.put(KEY_Price, order.getPrice());

        return db.update(TABLE_NAME, values, KEY_ID + " =?",
                new String[]{String.valueOf(order.getProductId())});
    }

    //Delete
    public void deleteOrder(Order order) {
        SQLiteDatabase db = getWritableDatabase();
        db.beginTransaction();
        try {
            db.delete(TABLE_NAME, KEY_ID + " =?", new String[]{String.valueOf(order.getProductId())});
            db.setTransactionSuccessful();
        } catch (Exception e) {
            Log.d(TAG, "Error while trying to delete order");
        } finally {
            db.endTransaction();
        }
    }

    // Read by position
    // 取得指定編號的資料物件
    public Order getOrder(int id) {
        SQLiteDatabase db = getReadableDatabase();
        Cursor cursor = db.query(TABLE_NAME,
                new String[]{KEY_ID, KEY_Name, KEY_Quantity, KEY_Price},
                KEY_ID + " =?",
                new String[]{String.valueOf(id)},
                null, null, null, null);
        if (cursor != null) {
            cursor.moveToFirst();
        }
        return new Order(cursor.getInt(0), cursor.getString(1), cursor.getString(2), cursor.getString(3));
    }

    //Read All
    //讀取所有記事資料
    public List<Order> getAllOrder() {
        List<Order> orderList = new ArrayList<>();
        //SQL語法
        String selectQuery = "SELECT * FROM " + TABLE_NAME;

        SQLiteDatabase db = getReadableDatabase();

        Cursor cursor = db.rawQuery(selectQuery, null);
        try {
            if (cursor.moveToFirst()) {
                do {
                    Order order = new Order();
                    order.setProductId(cursor.getInt(cursor.getColumnIndex(KEY_ID)));
                    order.setProductName(cursor.getString(cursor.getColumnIndex(KEY_Name)));
                    order.setQuantity(cursor.getString(cursor.getColumnIndex(KEY_Quantity)));
                    order.setPrice(cursor.getString(cursor.getColumnIndex(KEY_Price)));

                    orderList.add(order);
                } while (cursor.moveToNext());
            }
        } catch (Exception e) {
            Log.d(TAG, "Error while trying to getAllOrder from database");
        } finally {
            if (cursor != null && !cursor.isClosed()) {
                cursor.close();
            }
        }
        return orderList;
    }
}
