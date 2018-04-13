package loginscreen.solution.example.com.loginscreen;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

class AppDatabase extends SQLiteOpenHelper {
    private static final String DATABASE_NAME = "MetovaUsers.db";
    private static final int DATABASE_VERSION = 1;

    private static AppDatabase instance = null;

    private AppDatabase(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    static AppDatabase getInstance(Context context) {
        if (instance == null) {
            instance = new AppDatabase(context);
        }
        return instance;
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String sSQL;
        sSQL = "CREATE TABLE " + UsersContract.TABLE_NAME + " ("
                + UsersContract.Columns._ID + " INTEGER PRIMARY KEY NOT NULL, "
                + UsersContract.Columns.USERS_NAME + " TEXT NOT NULL, "
                + UsersContract.Columns.USERS_EMAIL + " TEXT, "
                + UsersContract.Columns.USERS_PHONE + " TEXT, "
                + UsersContract.Columns.USERS_PASSWORD + " TEXT);";
        db.execSQL(sSQL);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        switch (oldVersion) {
            case 1:
                break;
            default:
                throw new IllegalStateException("onUpgrade() with unknown newVersion: " + newVersion);
        }
    }
}
