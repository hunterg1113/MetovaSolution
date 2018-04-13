package loginscreen.solution.example.com.loginscreen;

import java.io.Serializable;

public class User implements Serializable {
    public static final long serialVersionUID = 20180413;

    private long _Id;
    private final String name;
    private final String email;
    private final String phone;
    private final String password;


    public User(String name, String email, String phone, String password) {
        this.name = name;
        this.email = email;
        this.phone = phone;
        this.password = password;
    }

    public String getName() {
        return name;
    }

    public String getEmail() {
        return email;
    }

    public String getPhone() {
        return phone;
    }
}
