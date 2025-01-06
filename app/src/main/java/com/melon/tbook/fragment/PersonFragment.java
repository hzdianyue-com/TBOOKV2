package com.melon.tbook.fragment;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.Fragment;

import com.melon.tbook.R;
//import com.melon.tbook.activity.LoginActivity;
import com.melon.tbook.model.User;
import com.melon.tbook.utils.DataProxy;
import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class PersonFragment extends Fragment {
    private DataProxy dbHelper;
    private TextView textUsername;
    private EditText editNickname;
    private EditText editEmail;
    private Button buttonResetPassword;
    private Button buttonLogout;
    private SharedPreferences sharedPreferences;
    private  User currentUser;

    public PersonFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_person, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        dbHelper = DataProxy.getInstance(getContext());
        sharedPreferences = getContext().getSharedPreferences("tbook_prefs", Context.MODE_PRIVATE);

        textUsername = view.findViewById(R.id.text_username);
        editNickname = view.findViewById(R.id.edit_nickname);
        editEmail = view.findViewById(R.id.edit_email);
        buttonResetPassword = view.findViewById(R.id.button_reset_password);
        buttonLogout = view.findViewById(R.id.button_logout);

        updateUI();
        buttonResetPassword.setOnClickListener(v -> showResetPasswordDialog());
        buttonLogout.setOnClickListener(v -> logout());
    }
    @Override
    public void onResume() {
        super.onResume();
        updateUI();
    }
    private void updateUI(){
        String username = sharedPreferences.getString("username", null);
        if(username != null){
            currentUser = dbHelper.getUserByUsername(username);
            if(currentUser!=null){
                textUsername.setText(currentUser.getUsername());
                editNickname.setText(currentUser.getNickname());
                editEmail.setText(currentUser.getEmail());
            }
        }

    }
    private void updateUserInfo(){
        if(currentUser!=null){
            String nickname = editNickname.getText().toString();
            String email = editEmail.getText().toString();
            if(!isValidEmail(email)){
                Toast.makeText(getContext(), "邮箱格式不正确", Toast.LENGTH_SHORT).show();
                return;
            }
            currentUser.setNickname(nickname);
            currentUser.setEmail(email);
            dbHelper.updateUser(currentUser);
            Toast.makeText(getContext(), "修改成功", Toast.LENGTH_SHORT).show();
        }
    }
    private void showResetPasswordDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
        builder.setTitle("重置密码");
        View view = LayoutInflater.from(getContext()).inflate(R.layout.dialog_reset_password, null);
        EditText editPassword = view.findViewById(R.id.edit_password);
        EditText editConfirmPassword = view.findViewById(R.id.edit_confirm_password);
        builder.setView(view);
        builder.setPositiveButton("确定", (dialog, which) -> {
            String password = editPassword.getText().toString();
            String confirmPassword = editConfirmPassword.getText().toString();
            if(password.isEmpty() || confirmPassword.isEmpty()){
                Toast.makeText(getContext(), "密码不能为空", Toast.LENGTH_SHORT).show();
                return;
            }
            if(!password.equals(confirmPassword)){
                Toast.makeText(getContext(), "两次密码输入不一致", Toast.LENGTH_SHORT).show();
                return;
            }
            String hashedPassword = hashPassword(password);
            currentUser.setPassword(hashedPassword);
            dbHelper.updateUser(currentUser);
            Toast.makeText(getContext(), "密码重置成功", Toast.LENGTH_SHORT).show();

        });
        builder.setNegativeButton(R.string.button_cancel, null);
        builder.show();

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
    private void logout(){
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.remove("username");
        editor.apply();
        /*
        Intent intent = new Intent(getContext(), LoginActivity.class);
        startActivity(intent);

         */
        getActivity().finish();
    }
    public boolean isValidEmail(String email) {
        String EMAIL_PATTERN = "^[_A-Za-z0-9-\\+]+(\\.[_A-Za-z0-9-]+)*@[A-Za-z0-9-]+(\\.[A-Za-z0-9]+)*(\\.[A-Za-z]{2,})$";
        Pattern pattern = Pattern.compile(EMAIL_PATTERN);
        Matcher matcher = pattern.matcher(email);
        return matcher.matches();
    }
}