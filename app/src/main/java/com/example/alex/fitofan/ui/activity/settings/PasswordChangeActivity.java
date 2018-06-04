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
import com.example.alex.fitofan.client.Request;
import com.example.alex.fitofan.databinding.ActivityPasswordChangeBinding;
import com.example.alex.fitofan.interfaces.ILoadingStatus;
import com.example.alex.fitofan.models.GetUserModel;
import com.example.alex.fitofan.settings.MSharedPreferences;
import com.example.alex.fitofan.utils.CheckerInputData;
import com.example.alex.fitofan.utils.Connection;
import com.google.gson.Gson;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;

public class PasswordChangeActivity extends AppCompatActivity implements ILoadingStatus {

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
            boolean isOk = true;
            List<EditText> list = Arrays.asList(
                    mBinding.newPass,
                    mBinding.newPassAgain
//                    mBinding.newPassAgain
            );
            for (EditText edit : list) {
                if (TextUtils.isEmpty(edit.getText().toString().trim())) {
                    edit.setError(getResources().getString(R.string.obligatory_field));
                    isEmpty = true;
                }
            }
            if (!isEmpty) {
                if (!CheckerInputData.isPassword(mBinding.newPass.getText().toString().trim())) {
                    Toast.makeText(PasswordChangeActivity.this, getResources().getString(R.string.exemple_pass), Toast.LENGTH_SHORT).show();
                    isOk = false;
                }

                if (!mBinding.newPass.getText().toString().trim().equals(mBinding.newPassAgain.getText().toString().trim())) {
                    Toast.makeText(PasswordChangeActivity.this, getResources().getString(R.string.pass_not_match), Toast.LENGTH_SHORT).show();
                    isOk = false;
                }
                if (isOk) {
                    HashMap<String, String> map = new HashMap<>();
                    map.put("uid", new Gson().fromJson(MSharedPreferences.getInstance().getUserInfo(), GetUserModel.class).getUser().getUid());
                    map.put("signature", new Gson().fromJson(MSharedPreferences.getInstance().getUserInfo(), GetUserModel.class).getUser().getSignature());
                    map.put("password", mBinding.newPass.getText().toString().trim());
                    if (Connection.isNetworkAvailable(this))
                        Request.getInstance().changeUserPassword(map, this);
                }
            }
        }

        return super.onOptionsItemSelected(item);
    }

    private void initListeners() {
        mBinding.toolbar.setNavigationOnClickListener(view -> onBackPressed());
    }

    @Override
    public void onSuccess(Object info) {
        Toast.makeText(this, "Done", Toast.LENGTH_SHORT).show();
        onBackPressed();
    }

    @Override
    public void onFailure(String message) {
        Toast.makeText(this, getResources().getString(R.string.connection_error), Toast.LENGTH_SHORT).show();
    }
}
