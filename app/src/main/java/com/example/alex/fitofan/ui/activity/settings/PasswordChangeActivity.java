package com.example.alex.fitofan.ui.activity.settings;

import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.EditText;
import android.widget.Toast;

import com.example.alex.fitofan.R;
import com.example.alex.fitofan.databinding.ActivityPasswordChangeBinding;
import com.example.alex.fitofan.models.GetUserModel;
import com.example.alex.fitofan.settings.MSharedPreferences;
import com.example.alex.fitofan.utils.CheckerInputData;
import com.google.gson.Gson;

import java.util.Arrays;
import java.util.List;

public class PasswordChangeActivity extends AppCompatActivity {

    private ActivityPasswordChangeBinding mBinding;
    private Menu menu;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_password_change);

        mBinding = DataBindingUtil.setContentView(this, R.layout.activity_password_change);
        setSupportActionBar(mBinding.toolbar);
        initListeners();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        this.menu = menu;
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.setting_change, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_done) {
            boolean isEmpty = false;
            List<EditText> list = Arrays.asList(
                    mBinding.nowPass,
                    mBinding.newPass,
                    mBinding.newPassAgain
            );
            for (EditText edit : list) {
                if (TextUtils.isEmpty(edit.getText().toString().trim())) {
                    edit.setError("Обязательное поле");
                    isEmpty = true;
                }
            }
            if (!isEmpty) {
                if (!CheckerInputData.isPassword(mBinding.nowPass.getText().toString().trim())) {
                    Toast.makeText(PasswordChangeActivity.this, "Пароль должен содержать не менее 8 символов, цифры, буквы верхнего и нижнего регистра", Toast.LENGTH_SHORT).show();
                }

                if (!mBinding.newPass.getText().toString().trim().equals(mBinding.newPassAgain.getText().toString().trim())) {
                    Toast.makeText(PasswordChangeActivity.this, "Пароли не совпадают", Toast.LENGTH_SHORT).show();
                }
            }
        }

        return super.onOptionsItemSelected(item);
    }

    private void initListeners() {
        mBinding.toolbar.setNavigationOnClickListener(view -> onBackPressed());
    }
}
