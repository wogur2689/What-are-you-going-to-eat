package ac.dongyang.smartapp.myapp;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

interface DBContract {
    static final String TABLE_NAME="PWD_T";
    static final String COL_ID="ID";
    static final String COL_NAME="NAME";
    static final String COL_FNAME="FOODNAME";
    static final String COL_TASTE="TASTE";

    static final String SQL_CREATE_TABLE = "CREATE TABLE IF NOT EXISTS " +
            TABLE_NAME + "(" +
            COL_ID + " INTEGER NOT NULL PRIMARY KEY, " +
            COL_NAME + " TEXT NOT NULL, " +
            COL_FNAME + " TEXT NOT NULL, " +
            COL_TASTE + " TEXT NOT NULL)";
    static final String SQL_DROP_TABLE = "DROP TABLE IF EXISTS " + TABLE_NAME;
    static final String SQL_LOAD = "SELECT * FROM " + TABLE_NAME;
    static final String SQL_SELECT = "SELECT * FROM "  + TABLE_NAME + " WHERE " + COL_NAME + "=?";
    static final String SQL_SELECT_ID = "SELECT ID FROM "  + TABLE_NAME + " WHERE " + COL_NAME + "=?";
}

class DBHelper extends SQLiteOpenHelper {
    static final String DB_FILE = "pwd_t.db";
    static final int DB_VERSION = 1;

    DBHelper(Context context) {
        super(context, DB_FILE, null, DB_VERSION); // 세번째 인자 : cursor factory
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(DBContract.SQL_CREATE_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL(DBContract.SQL_DROP_TABLE);
        onCreate(db);
    }
}
