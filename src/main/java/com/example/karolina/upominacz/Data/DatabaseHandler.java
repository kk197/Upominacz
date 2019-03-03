package com.example.karolina.upominacz.Data;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.support.annotation.Nullable;
import android.util.Log;

import com.example.karolina.upominacz.Model.Item;
import com.example.karolina.upominacz.Util.Constants;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.concurrent.CopyOnWriteArraySet;

import static com.example.karolina.upominacz.Util.Constants.TABLE_NAME;

public class DatabaseHandler extends SQLiteOpenHelper {

    private Context ctx;
    public DatabaseHandler(@Nullable Context context) {
        super(context, Constants.DB_NAME, null, Constants.DB_VERSION);
        this.ctx = context;
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String CREATE_ITEM_TABLE = "CREATE TABLE " + TABLE_NAME + "(" + Constants.KEY_ID
                + " INTEGER PRIMARY KEY," + Constants.KEY_ITEM + " TEXT,"
                + Constants.KEY_INFO + " TEXT," + Constants.KEY_DATE_NAME + " LONG);";

        db.execSQL(CREATE_ITEM_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME);
        onCreate(db);
    }

    //Add an Item
    public void addItem(Item item){
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(Constants.KEY_ITEM, item.getName());
        values.put(Constants.KEY_INFO, item.getMoreInfo());
        values.put(Constants.KEY_DATE_NAME, java.lang.System.currentTimeMillis());

        //Insert the row
        db.insert(Constants.TABLE_NAME, null, values);

        Log.d("Saved","Saved to DB");

    }

    //Get an Item
    public Item getItem(int id){

        SQLiteDatabase db = this.getWritableDatabase();

        Cursor cursor = db.query(Constants.TABLE_NAME, new String[] {Constants.KEY_ID,
            Constants.KEY_ITEM, Constants.KEY_INFO, Constants.KEY_DATE_NAME},
                Constants.KEY_ID + "=?", new String[] {String.valueOf(id)}, null, null, null, null);

        if (cursor !=null)
            cursor.moveToFirst();

            Item item = new Item();
            item.setId(Integer.parseInt(cursor.getString(cursor.getColumnIndex(Constants.KEY_ID))));
            item.setName(cursor.getString(cursor.getColumnIndex(Constants.KEY_ITEM)));
            item.setMoreInfo(cursor.getString(cursor.getColumnIndex(Constants.KEY_INFO)));

            //converting timestamp
            java.text.DateFormat dateFormat = java.text.DateFormat.getDateInstance();
            String formatedDate = dateFormat.format(new Date(cursor.getLong(cursor.getColumnIndex(Constants.KEY_DATE_NAME))).getTime());

            item.setDateItemAdded(formatedDate);

       // cursor.close();
        return item;
    }

    //Get all Items
    public List<Item> getAllItems(){
        SQLiteDatabase db = this.getReadableDatabase();

        List<Item> itemList = new ArrayList<>();
        Cursor cursor = db.query(Constants.TABLE_NAME, new String[] {
                Constants.KEY_ID, Constants.KEY_ITEM, Constants.KEY_INFO,
                Constants.KEY_DATE_NAME}, null, null, null, null,
                Constants.KEY_DATE_NAME + " DESC");

        if (cursor.moveToFirst()){
            do {
                Item item = new Item();
                item.setId(Integer.parseInt(cursor.getString(cursor.getColumnIndex(Constants.KEY_ID))));
                item.setName(cursor.getString(cursor.getColumnIndex(Constants.KEY_ITEM)));
                item.setMoreInfo(cursor.getString(cursor.getColumnIndex(Constants.KEY_INFO)));

                //converting timestamp
                java.text.DateFormat dateFormat = java.text.DateFormat.getDateInstance();
                String formatedDate = dateFormat.format(new Date(cursor.getLong(cursor.getColumnIndex(Constants.KEY_DATE_NAME))).getTime());

                item.setDateItemAdded(formatedDate);

                //add to the itemList
                itemList.add(item);
            }while (cursor.moveToNext());
        }
        return itemList;
    }

    //Update Item
    public int updateItem(Item item){

        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(Constants.KEY_ITEM, item.getName());
        values.put(Constants.KEY_INFO, item.getMoreInfo());
        values.put(Constants.KEY_DATE_NAME, java.lang.System.currentTimeMillis()); //getting system time

        return db.update(Constants.TABLE_NAME, values, Constants.KEY_ID + "=?", new String[] {String.valueOf(item.getId())});
    }

    //Delete Item
    public void deleteItem(int id){
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete(Constants.TABLE_NAME, Constants.KEY_ID + " = ?",
                new String[] {String.valueOf(id)});

        db.close();
    }

    //Get count
    public int getItemsCount(){

        String countQuery = "SELECT * FROM " + Constants.TABLE_NAME;
        SQLiteDatabase db = this.getReadableDatabase();

        Cursor cursor = db.rawQuery(countQuery, null);

        return cursor.getCount();
    }

}
