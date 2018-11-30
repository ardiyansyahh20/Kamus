package com.ardi.kamus.db;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteStatement;

import com.ardi.kamus.entity.Kamus;

import java.util.ArrayList;

import static android.provider.BaseColumns._ID;
import static com.ardi.kamus.db.DatabaseContract.KamusColumns.DESC;
import static com.ardi.kamus.db.DatabaseContract.KamusColumns.WORD;

public class KamusHelper {
    private Context context;
    private DatabaseHelper dbHelper;
    private SQLiteDatabase database;
    private static String ENGLISH = DatabaseContract.TABLE_EN_TO_ID;
    private static String INDONESIA = DatabaseContract.TABLE_ID_TO_EN;

    public KamusHelper(Context context) {
        this.context = context;
    }

    public KamusHelper open() throws SQLException {
        dbHelper = new DatabaseHelper(context);
        database = dbHelper.getWritableDatabase();
        return this;
    }

    public void close() {
        dbHelper.close();
    }

    private Cursor cursorAllData(boolean isEnglish) {
        String DATABASE_TABLE = isEnglish ? ENGLISH : INDONESIA;
        return database.rawQuery(" SELECT * FROM " + DATABASE_TABLE + " ORDER BY " + _ID + " ASC ", null);
    }

    private Cursor searchQuery(String query, boolean isEnglish) {
        String DATABASE_TABLE = isEnglish ? ENGLISH : INDONESIA;
        return database.rawQuery(" SELECT * FROM " + DATABASE_TABLE +
                " WHERE " + WORD + " LIKE '%" + query.trim() + "%'", null);
    }

    public ArrayList<Kamus> getDataByName(String find, boolean isEnglish) {
        Kamus kamus;
        ArrayList<Kamus> arrayList = new ArrayList<>();
        Cursor cursor = searchQuery(find, isEnglish);

        cursor.moveToFirst();
        if (cursor.getCount() > 0) {
            do {
                kamus = new Kamus();
                kamus.setId((cursor.getInt(cursor.getColumnIndexOrThrow(DatabaseContract.KamusColumns._ID))));
                kamus.setWord((cursor.getString(cursor.getColumnIndexOrThrow(DatabaseContract.KamusColumns.WORD))));
                kamus.setDescription((cursor.getString(cursor.getColumnIndexOrThrow(DatabaseContract.KamusColumns.DESC))));
                arrayList.add(kamus);

                cursor.moveToNext();
            } while (!cursor.isAfterLast());
        }
        cursor.close();
        return arrayList;
    }

    public ArrayList<Kamus> getAllData(boolean isEnglish) {
        Kamus kamus;

        ArrayList<Kamus> arrayList = new ArrayList<>();
        Cursor cursor = cursorAllData(isEnglish);

        cursor.moveToFirst();
        if (cursor.getCount() > 0) {
            do {
                kamus = new Kamus();
                kamus.setId((cursor.getInt(cursor.getColumnIndexOrThrow(DatabaseContract.KamusColumns._ID))));
                kamus.setWord((cursor.getString(cursor.getColumnIndexOrThrow(DatabaseContract.KamusColumns.WORD))));
                kamus.setDescription((cursor.getString(cursor.getColumnIndexOrThrow(DatabaseContract.KamusColumns.DESC))));
                arrayList.add(kamus);

                cursor.moveToNext();
            } while (!cursor.isAfterLast());
        }
        cursor.close();
        return arrayList;
    }

    public long insert(Kamus kamus, boolean isEnglish) {
        String DATABASE_TABLE = isEnglish ? ENGLISH : INDONESIA;
        ContentValues contentValues = new ContentValues();
        contentValues.put(DatabaseContract.KamusColumns.WORD, kamus.getWord());
        contentValues.put(DatabaseContract.KamusColumns.DESC, kamus.getDescription());
        return database.insert(DATABASE_TABLE, null, contentValues);
    }

    public void insertTransaction(ArrayList<Kamus> kamuses, boolean isEnglish) {
        String DATABASE_TABLE = isEnglish ? ENGLISH : INDONESIA;
        String sql = " INSERT INTO " + DATABASE_TABLE + " (" + WORD + ", " +
                DESC + ") VALUES (?, ?)";
        database.beginTransaction();

        SQLiteStatement statement = database.compileStatement(sql);
        for (int i = 0; i < kamuses.size(); i++){
            statement.bindString(1, kamuses.get(i).getWord());
            statement.bindString(2, kamuses.get(i).getDescription());
            statement.execute();
            statement.clearBindings();
        }

        database.setTransactionSuccessful();
        database.endTransaction();
    }

    public void update(Kamus kamus, boolean isEnglish){
        String DATABASE_TABLE = isEnglish ? ENGLISH : INDONESIA;
        ContentValues contentValues = new ContentValues();
        contentValues.put(WORD, kamus.getWord());
        contentValues.put(DESC, kamus.getDescription());
        database.update(DATABASE_TABLE, contentValues, _ID + "= '" + kamus.getId() + "'", null);
    }

    public void delete(int id, boolean isEnglish){
        String DATABASE_TABLE = isEnglish ? ENGLISH : INDONESIA;
        database.delete(DATABASE_TABLE, _ID + " = '" + id + "'", null);
    }
}
