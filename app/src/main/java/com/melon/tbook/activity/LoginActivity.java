package com.melon.tbook.activity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.melon.tbook.R;
import com.melon.tbook.model.User;
import com.melon.tbook.utils.DataProxy;

import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

public class LoginActivity extends AppCompatActivity {
    private EditText editUsername;
    private EditText editPassword;
    private Button buttonLogin;
    private TextView textRegister;
    private DataProxy dbHelper;
    private SharedPreferences sharedPreferences;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        dbHelper = DataProxy.getInstance(this);
        sharedPreferences = getSharedPreferences("tbook_prefs", Context.MODE_PRIVATE);
        editUsername = findViewById(R.id.edit_username);
        editPassword = findViewById(R.id.edit_password);
        buttonLogin = findViewById(R.id.button_login);
        textRegister = findViewById(R.id.text_register);
        buttonLogin.setOnClickListener(v -> login());
        textRegister.setOnClickListener(v ->  {
            Intent intent = new Intent(LoginActivity.this, RegisterActivity.class);
            startActivity(intent);
        });
        // 如果已经登录，则自动跳转
        String username = sharedPreferences.getString("username",null);
        if(username!=null){
            Intent intent = new Intent(this, MainActivity.class);
            startActivity(intent);
            finish();
        }

    }

    private void login(){
        String username = editUsername.getText().toString();
        String password = editPassword.getText().toString();
        if(username.isEmpty() || password.isEmpty()){
            Toast.makeText(this, "用户名或密码不能为空", Toast.LENGTH_SHORT).show();
            return;
        }
        User user = dbHelper.getUserByUsername(username);
        if(user!=null && hashPassword(password).equals(user.getPassword())){
            SharedPreferences.Editor editor = sharedPreferences.edit();
            editor.putString("username",username);
            editor.apply();
            Intent intent = new Intent(this, MainActivity.class);
            startActivity(intent);
            finish();
        }else{
            Toast.makeText(this, "用户名或者密码不正确", Toast.LENGTH_SHORT).show();
        }
    }
    private String hashPassword(String password) {
        try {
            MessageDigest digest = MessageDigest.getInstance("MD5");
            byte[] hashBytes = digest.digest(password.getBytes(StandardCharsets.UTF_8));
            StringBuilder sb = new StringBuilder();
            for (byte b : hashBytes) {
                sb.append(String.format("%02x", b));
            }
            return sb.toString();
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
            return null;
        }
    }
}