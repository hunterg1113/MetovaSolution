package loginscreen.solution.example.com.loginscreen;

import android.content.ContentResolver;
import android.content.ContentValues;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Toast;
import android.widget.ViewFlipper;

public class MainActivity extends AppCompatActivity {
    public static final int VF_ROOT_DISPLAY_CHILD = 0;
    public static final int VF_CHILD_DISPLAY_SWITCH = 1;

    public static final String USER_CONTENTS = "UserContents";

    private EditText etName;
    private EditText etPassword;
    private EditText etPhone;
    private EditText etEmail;
    private LinearLayout ltName;
    private ViewFlipper vf;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.content_main);

        etName = (EditText) findViewById(R.id.et_name);
        etPassword = (EditText) findViewById(R.id.et_password);
        etPhone = (EditText) findViewById(R.id.et_phone);
        etEmail = (EditText) findViewById(R.id.et_email);
        ltName = (LinearLayout) findViewById(R.id.lt_name);
        vf = (ViewFlipper) findViewById(R.id.view_flipper);

        Button btLogin = (Button) findViewById(R.id.bt_login);
        Button btSignIn = (Button) findViewById(R.id.bt_sign_in);
        Button btSignUp = (Button) findViewById(R.id.bt_signup);
        Button btCreate = (Button) findViewById(R.id.bt_create);

        btLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                vf.setDisplayedChild(VF_ROOT_DISPLAY_CHILD);
                ltName.setVisibility(View.INVISIBLE);
            }
        });

        btSignIn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                User user = null;
                String emailInput = etEmail.getText().toString();
                String passwordInput = etPassword.getText().toString();

                Cursor query = getContentResolver().query(UsersContract.CONTENT_URI, null, null, null, null);

                if (query.moveToFirst()) {
                    do {
                        String email = query.getString(query.getColumnIndex(UsersContract.Columns.USERS_EMAIL));
                        String password = query.getString(query.getColumnIndex(UsersContract.Columns.USERS_PASSWORD));

                        if (email.equals(emailInput) && password.equals(passwordInput)) {
                            user = new User(query.getString(query.getColumnIndex(UsersContract.Columns.USERS_NAME)),
                                    query.getString(query.getColumnIndex(UsersContract.Columns.USERS_EMAIL)),
                                    query.getString(query.getColumnIndex(UsersContract.Columns.USERS_PHONE)),
                                    query.getString(query.getColumnIndex(UsersContract.Columns.USERS_PASSWORD)));
                        }
                    } while (query.moveToNext() && null == user);
                }

                query.close();

                if (null != user) {
                    Intent intent = new Intent(MainActivity.this, LoginWelcomeActivity.class);
                    intent.putExtra(USER_CONTENTS, user);
                    startActivity(intent);
                } else {
                    Toast.makeText(getApplicationContext(), "Incorrect email/password combination. Please try again or sign up.", Toast.LENGTH_SHORT).show();
                }
            }
        });

        btSignUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                vf.setDisplayedChild(VF_CHILD_DISPLAY_SWITCH);
                ltName.setVisibility(View.VISIBLE);
            }
        });

        btCreate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String name = etName.getText().toString();
                String email = etEmail.getText().toString();
                String password = etPassword.getText().toString();
                String phone = etPhone.getText().toString();

                if (isValidName(name) && isValidEmail(email) && isValidPassword(password) && isValidPhone(phone) && !isCurrentUser(email)) {
                        ContentResolver contentResolver = getContentResolver();
                        ContentValues values = new ContentValues();
                        values.put(UsersContract.Columns.USERS_NAME, name);
                        values.put(UsersContract.Columns.USERS_EMAIL, email);
                        values.put(UsersContract.Columns.USERS_PHONE, phone);
                        values.put(UsersContract.Columns.USERS_PASSWORD, password);
                        contentResolver.insert(UsersContract.CONTENT_URI, values);

                        Intent intent = new Intent(MainActivity.this, LoginWelcomeActivity.class);
                        intent.putExtra(USER_CONTENTS, new User(name, email, phone, password));
                        startActivity(intent);
                }
            }
        });
    }

    private boolean isValidName(String name) {
        char[] array = name.toCharArray();
        for (char c : array) {
            if (!Character.isLetterOrDigit(c)) {
                Toast.makeText(getApplicationContext(), "Invalid name", Toast.LENGTH_SHORT).show();
                return false;
            }
        }
        return true;
    }

    private boolean isValidEmail(String email) {
        String emailPattern = "[a-zA-Z0-9._-]+@[a-z]+\\.+[a-z]+";
        if (email.matches(emailPattern) && email.length() > 0) {
            return true;
        } else {
            Toast.makeText(getApplicationContext(), "Invalid email address", Toast.LENGTH_SHORT).show();
            return false;
        }
    }

    private boolean isValidPassword(String pw) {
        boolean lc = !pw.equals(pw.toLowerCase());
        boolean uc = !pw.equals(pw.toUpperCase());
        boolean length = pw.length() >= 6;
        boolean special = !pw.matches("[A-Za-z0-9 ]*");
        boolean hasNumber = pw.matches(".*\\d+.*");

        if (lc && uc && length && special && hasNumber) {
            return true;
        } else {
            Toast.makeText(getApplicationContext(), "Invalid password", Toast.LENGTH_SHORT).show();
            return false;
        }
    }

    private boolean isValidPhone(String number) {
        if (number.matches("[0-9]+") && number.length() == 10) {
            return true;
        } else {
            Toast.makeText(getApplicationContext(), "Invalid phone number", Toast.LENGTH_SHORT).show();
            return false;
        }
    }

    private boolean isCurrentUser(String email) {
        Cursor query = getContentResolver().query(UsersContract.CONTENT_URI, null, null, null, null);
        if (query.moveToFirst()) {
            do {
                if (query.getString(query.getColumnIndex(UsersContract.Columns.USERS_EMAIL)).equals(email)) {
                    Toast.makeText(getApplicationContext(), "Email already has an account. Please login or create new account with different email", Toast.LENGTH_SHORT).show();
                    return true;
                }
            } while (query.moveToNext());
        }
        return false;
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
