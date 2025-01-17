package com.melon.tbook.activity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.melon.tbook.R;
import com.melon.tbook.fragment.FlowFragment;
import com.melon.tbook.fragment.HomeFragment;
import com.melon.tbook.fragment.PersonFragment;
import com.melon.tbook.fragment.ReportFragment;
import com.melon.tbook.fragment.TransactionFragment;
import com.melon.tbook.utils.DatabaseHelper;

public class MainActivity extends AppCompatActivity {

    private BottomNavigationView bottomNavigationView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        bottomNavigationView = findViewById(R.id.bottom_navigation);

        bottomNavigationView.setOnItemSelectedListener(item -> {
            if (item.getItemId() == R.id.navigation_home) {
                loadFragment(new HomeFragment());
            } else if (item.getItemId() == R.id.navigation_report) {
                loadFragment(new ReportFragment());
            } else if (item.getItemId() == R.id.navigation_transaction_list) {
                loadFragment(new TransactionFragment());
            } else if (item.getItemId() == R.id.navigation_expense) {
                loadFragment(new FlowFragment());
            } else if (item.getItemId() == R.id.navigation_person) {
                loadFragment(new PersonFragment());
            }
            return true;
        });
        // 默认加载 home
        loadFragment(new HomeFragment());
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (!checkLogin()){
            return;
        }
    }

    private boolean checkLogin() {
        SharedPreferences sharedPreferences = getSharedPreferences("tbook_prefs", Context.MODE_PRIVATE);

        String username = sharedPreferences.getString("username", null);
        if (username == null) {
            Intent intent = new Intent(MainActivity.this, LoginActivity.class);
            startActivity(intent);
            finish();
            return false;
        }

        return true;
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        SharedPreferences.Editor editor = getSharedPreferences("tbook_prefs", MODE_PRIVATE).edit();
        editor.putString("username", null);
        editor.apply();
    }

    private void loadFragment(Fragment fragment) {
        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction transaction = fragmentManager.beginTransaction();
        transaction.replace(R.id.fragment_container, fragment);
        transaction.commit();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main_options_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == R.id.action_remind) {
            // 处理提醒事件设置的点击
            // 这里可以打开一个对话框，或跳转到一个设置提醒的 Activity
            showRemindSettingDialog();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    private void showRemindSettingDialog() {
        // TODO: 实现提醒事件的设置逻辑
        // 例如 打开一个对话框， 或者跳转到一个新的 Activity
        // 这里先用一个 Toast 提示
        androidx.appcompat.app.AlertDialog.Builder builder = new androidx.appcompat.app.AlertDialog.Builder(this);
        builder.setTitle(R.string.title_remind_setting);
        builder.setMessage("这里是提醒设置");
        builder.setPositiveButton("确定", null);
        builder.show();
    }
    private void showChangeThemeDialog(){
        Toast.makeText(this, "切换主题", Toast.LENGTH_SHORT).show();
    }
}