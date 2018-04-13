package loginscreen.solution.example.com.loginscreen;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.TextView;

public class LoginWelcomeActivity extends AppCompatActivity {
    @Override
    public void onBackPressed() {
        super.onBackPressed();
        Intent in = new Intent(LoginWelcomeActivity.this, MainActivity.class);
        in.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(in);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.content_welcome);
        Intent in = getIntent();
        User user = (User) in.getSerializableExtra(MainActivity.USER_CONTENTS);

        TextView tvName = (TextView) findViewById(R.id.tv_name);
        TextView tvEmail = (TextView) findViewById(R.id.tv_email);
        TextView tvPhone = (TextView) findViewById(R.id.tv_phone);

        tvName.setText(user.getName());
        tvEmail.setText(user.getEmail());
        tvPhone.setText(user.getPhone());
    }
}
