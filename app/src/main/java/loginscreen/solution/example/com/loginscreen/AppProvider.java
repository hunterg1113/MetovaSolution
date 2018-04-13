package loginscreen.solution.example.com.loginscreen;

import android.content.ContentProvider;
import android.content.ContentValues;
import android.content.UriMatcher;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteQueryBuilder;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

public class AppProvider extends ContentProvider {
    private AppDatabase mOpenHelper;

    private static final UriMatcher sUriMatcher = buildUriMatcher();

    static final String CONTENT_AUTHORITY = "loginscreen.solution.example.com.loginscreen.provider";
    public static final Uri CONTENT_AUTHORITY_URI = Uri.parse("content://" + CONTENT_AUTHORITY);

    private static final int USERS = 100;
    private static final int USERS_ID = 101;

    private static UriMatcher buildUriMatcher() {
        final UriMatcher matcher = new UriMatcher(UriMatcher.NO_MATCH);

        matcher.addURI(CONTENT_AUTHORITY, UsersContract.TABLE_NAME, USERS);
        matcher.addURI(CONTENT_AUTHORITY, UsersContract.TABLE_NAME + "/#", USERS_ID);

        return matcher;
    }

    @Override
    public boolean onCreate() {
        mOpenHelper = AppDatabase.getInstance(getContext());
        return true;
    }

    @Nullable
    @Override
    public Cursor query(@NonNull Uri uri, @Nullable String[] projection, @Nullable String selection, @Nullable String[] selectionArgs, @Nullable String sortOrder) {
        final int match = sUriMatcher.match(uri);

        SQLiteQueryBuilder queryBuilder = new SQLiteQueryBuilder();
        switch (match) {
            case USERS:
                queryBuilder.setTables(UsersContract.TABLE_NAME);
                break;
            case USERS_ID:
                queryBuilder.setTables(UsersContract.TABLE_NAME);
                long taskId = UsersContract.getUserId(uri);
                queryBuilder.appendWhere(UsersContract.Columns._ID + " = " + taskId);
                break;
            default:
                throw new IllegalArgumentException("Unknown URI: " + uri);
        }

        SQLiteDatabase db = mOpenHelper.getReadableDatabase();
        Cursor cursor = queryBuilder.query(db, projection, selection, selectionArgs, null, null, sortOrder);
        cursor.setNotificationUri(getContext().getContentResolver(), uri);
        return cursor;
    }

    @Nullable
    @Override
    public String getType(@NonNull Uri uri) {
        final int match = sUriMatcher.match(uri);
        switch (match) {
            case USERS:
                return UsersContract.CONTENT_TYPE;
            case USERS_ID:
                return UsersContract.CONTENT_ITEM_TYPE;
            default:
                throw new IllegalArgumentException("Unknown URI: " + uri);
        }
    }

    @Nullable
    @Override
    public Uri insert(@NonNull Uri uri, @Nullable ContentValues values) {
        final int match = sUriMatcher.match(uri);

        final SQLiteDatabase db;

        Uri returnUri;
        long recordId;
        switch (match) {
            case USERS:
                db = mOpenHelper.getWritableDatabase();
                recordId = db.insert(UsersContract.TABLE_NAME, null, values);

                if (recordId >= 0) returnUri = UsersContract.buildUserUri(recordId);
                else throw new android.database.SQLException("Failed to insert into " + uri.toString());

                break;
            default:
                throw new IllegalArgumentException("Unknown URI: " + uri);
        }

        if (recordId >= 0) getContext().getContentResolver().notifyChange(uri, null);

        return returnUri;
    }

    @Override
    public int delete(@NonNull Uri uri, @Nullable String s, @Nullable String[] strings) {
        return 0;
    }

    @Override
    public int update(@NonNull Uri uri, @Nullable ContentValues contentValues, @Nullable String s, @Nullable String[] strings) {
        return 0;
    }
}

