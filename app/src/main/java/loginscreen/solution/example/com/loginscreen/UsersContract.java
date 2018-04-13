package loginscreen.solution.example.com.loginscreen;

import android.content.ContentUris;
import android.net.Uri;
import android.provider.BaseColumns;

import static loginscreen.solution.example.com.loginscreen.AppProvider.CONTENT_AUTHORITY;
import static loginscreen.solution.example.com.loginscreen.AppProvider.CONTENT_AUTHORITY_URI;

public class UsersContract {
    public static final String TABLE_NAME = "Users";

    public static class Columns {
        public static final String _ID = BaseColumns._ID;
        public static final String USERS_NAME = "Name";
        public static final String USERS_EMAIL = "Email";
        public static final String USERS_PHONE = "Phone";
        public static final String USERS_PASSWORD = "Password";

        private Columns() {
        }
    }

    public static final Uri CONTENT_URI = Uri.withAppendedPath(CONTENT_AUTHORITY_URI, TABLE_NAME);

    static final String CONTENT_TYPE = "vnd.android.cursor.dir/vnd." + CONTENT_AUTHORITY + "." + TABLE_NAME;
    static final String CONTENT_ITEM_TYPE = "vnd.android.cursor.item/vnd." + CONTENT_AUTHORITY + "." + TABLE_NAME;

    static Uri buildUserUri(long userId) {
        return ContentUris.withAppendedId(CONTENT_URI, userId);
    }

    static long getUserId(Uri uri) {
        return ContentUris.parseId(uri);
    }
}
