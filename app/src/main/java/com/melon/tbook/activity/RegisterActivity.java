package com.melon.tbook.activity;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AppCompatActivity;

import com.melon.tbook.R;
import com.melon.tbook.model.User;
import com.melon.tbook.utils.DataProxy;

import java.io.FileNotFoundException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class RegisterActivity extends AppCompatActivity {
    private EditText editUsername;
    private EditText editPassword;
    private EditText editConfirmPassword;
    private EditText editNickname;
    private EditText editEmail;
    private Button buttonRegister;
    private DataProxy dbHelper;
    private Button buttonSelectAvatar;
    private ImageView imageAvatar;
    private Uri avatarUri = null;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        dbHelper = DataProxy.getInstance(this);
        editUsername = findViewById(R.id.edit_username);
        editPassword = findViewById(R.id.edit_password);
        editConfirmPassword = findViewById(R.id.edit_confirm_password);
        editNickname = findViewById(R.id.edit_nickname);
        editEmail = findViewById(R.id.edit_email);
        buttonRegister = findViewById(R.id.button_register);
        buttonSelectAvatar = findViewById(R.id.button_select_avatar);
        imageAvatar = findViewById(R.id.image_avatar);
        buttonRegister.setOnClickListener(v -> register());
        buttonSelectAvatar.setOnClickListener(v -> selectAvatar());

    }
    private void register(){
        String username = editUsername.getText().toString();
        String password = editPassword.getText().toString();
        String confirmPassword = editConfirmPassword.getText().toString();
        String nickname = editNickname.getText().toString();
        String email = editEmail.getText().toString();
        String avatar = null;
        if(avatarUri !=null){
            avatar = avatarUri.toString();
        }
        if(username.isEmpty() || password.isEmpty() || confirmPassword.isEmpty() || nickname.isEmpty() || email.isEmpty()){
            Toast.makeText(this, "请填写所有信息", Toast.LENGTH_SHORT).show();
            return;
        }
        if(!password.equals(confirmPassword)){
            Toast.makeText(this, "两次密码输入不一致", Toast.LENGTH_SHORT).show();
            return;
        }
        if(!isValidEmail(email)){
            Toast.makeText(this, "邮箱格式不正确", Toast.LENGTH_SHORT).show();
            return;
        }
        String hashedPassword = hashPassword(password);
        User user = new User(username, hashedPassword, nickname, email,avatar);
        long id = dbHelper.addUser(user);
        if(id>0){
            Toast.makeText(this, "注册成功", Toast.LENGTH_SHORT).show();
            finish();
        }else{
            Toast.makeText(this, "注册失败", Toast.LENGTH_SHORT).show();
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
    public boolean isValidEmail(String email) {
        String EMAIL_PATTERN = "^[_A-Za-z0-9-\\+]+(\\.[_A-Za-z0-9-]+)*@[A-Za-z0-9-]+(\\.[A-Za-z0-9]+)*(\\.[A-Za-z]{2,})$";
        Pattern pattern = Pattern.compile(EMAIL_PATTERN);
        Matcher matcher = pattern.matcher(email);
        return matcher.matches();
    }
    private void selectAvatar(){
        Intent intent = new Intent(Intent.ACTION_PICK);
        intent.setType("image/*");
        selectAvatarLauncher.launch(intent);
    }
    private ActivityResultLauncher<Intent> selectAvatarLauncher = registerForActivityResult(new ActivityResultContracts.StartActivityForResult(), result -> {
        if (result.getResultCode() == RESULT_OK && result.getData() != null) {
            Intent intent = result.getData();
            avatarUri = intent.getData();
            try {
                InputStream inputStream = getContentResolver().openInputStream(avatarUri);
                Bitmap bitmap = BitmapFactory.decodeStream(inputStream);
                imageAvatar.setImageBitmap(bitmap);
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            }
        }
    });
}