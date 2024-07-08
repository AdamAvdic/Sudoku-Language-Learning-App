package com.example.sudokuvocabulary;

import static org.junit.Assert.*;

import android.content.Context;
import android.database.Cursor;

import androidx.test.core.app.ApplicationProvider;
import androidx.test.ext.junit.runners.AndroidJUnit4;

import com.example.sudokuvocabulary.adapters.DBAdapter;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

import java.util.ArrayList;

@RunWith(AndroidJUnit4.class)
public class DBAdapterTest {

    private DBAdapter db;
    private String tableName;

    @Before
    public void createDB() {
        Context context = ApplicationProvider.getApplicationContext();
        db = new DBAdapter(context);
        db.open();
        tableName = "Food";
        newTable();
    }

    @Test
    public void isOpen() {
        assertTrue(db.isOpen());
    }

    @Test
    public void newTable() {
        db.newTable(tableName);
        try (Cursor c = db.getSQLiteDB().rawQuery(
                "SELECT name FROM sqlite_master WHERE type='table' " +
                "AND name='" + tableName + "'",
                null
        )) {
            boolean tableNotFound = true;
            while (c.moveToNext() && tableNotFound) {
                if (c.getString(0).equals(tableName)) {
                    tableNotFound = false;
                }
            }
            assertFalse(tableNotFound);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Test
    public void insertRow() {
        assertTrue(isTableInDB(tableName));
        db.insertRow("apple", "pingguo", tableName);
        assertTrue(isInTable(tableName, "word", "apple"));
        assertTrue(isInTable(tableName, "translation", "pingguo"));
    }

    @Test
    public void deleteRow() {
    }

    @Test
    public void deleteAll() {
    }

    @Test
    public void getTableNames() {
        assertTrue(isTableInDB(tableName));
        assertTrue(isTableInDB("animals"));
    }

    @Test
    public void getAllRows() {
    }

    @Test
    public void testGetAllRows() {
    }

    @Test
    public void getAllCategories() {
        ArrayList<String> categories = db.getAllCategories();
        assertTrue(categories.contains("animal"));
        assertTrue(categories.contains("food"));
    }

    @Test
    public void getRow() {
    }

    @Test
    public void getWordsFromCategory() {
        ArrayList<ArrayList<String>> words = db.getWordsFromCategory("animal");
        assertTrue(words.get(0).contains("dog"));
        assertTrue(words.get(0).contains("cat"));
    }

    @Test
    public void updateRow() {
    }

    @After
    public void resetDB() {
        db.getSQLiteDB().execSQL(
                "DROP TABLE IF EXISTS " + tableName
        );
        db.close();
    }

    private boolean isTableInDB(String tableName) {
        Cursor c = db.getSQLiteDB().rawQuery(
                "SELECT name FROM sqlite_master WHERE type='table' " +
                        "AND name='" + tableName + "'",
                null
        );
        return c.moveToFirst();
    }

    private boolean isInTable(String table, String column, String value) {
        // Define the search parameters
        String[] columns = {column};
        String where = column + "=?";
        String[] selectionArgs = {value};

        // Query the database for the given value
        Cursor c = db.getSQLiteDB().query(
                table,
                columns,
                where,
                selectionArgs,
                null,
                null,
                null
        );
        return c.moveToFirst();
    }
}