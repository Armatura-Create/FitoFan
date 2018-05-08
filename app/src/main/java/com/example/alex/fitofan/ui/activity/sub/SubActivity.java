package com.example.alex.fitofan.ui.activity.sub;

import android.content.Context;
import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;

import com.example.alex.fitofan.R;
import com.example.alex.fitofan.databinding.ActivityMainBinding;
import com.example.alex.fitofan.databinding.ActivitySubBinding;
import com.example.alex.fitofan.models.GetUserModel;
import com.example.alex.fitofan.settings.MSharedPreferences;
import com.example.alex.fitofan.ui.activity.signin.SignInActivity;
import com.example.alex.fitofan.ui.activity.user_profile.UserProfileActivity;
import com.example.alex.fitofan.ui.fragments.my_plans.MyPlansFragment;
import com.example.alex.fitofan.ui.fragments.rainting.ParticipantFragment;
import com.example.alex.fitofan.ui.fragments.subscribers.SubscribersFragment;
import com.example.alex.fitofan.ui.fragments.subscriptions.SubscriptionsFragment;
import com.example.alex.fitofan.ui.fragments.wall.WallFragment;
import com.facebook.login.LoginManager;
import com.google.gson.Gson;

public class SubActivity extends AppCompatActivity
        implements SubContract.View {

    private ActivitySubBinding mBinding;
    private SubPresenter mPresenter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sub);

        mBinding = DataBindingUtil.setContentView(this, R.layout.activity_sub);
        mPresenter = new SubPresenter(this);

        setSupportActionBar(mBinding.toolbar);

        initTabs();
        initListeners();
    }

    private void initListeners() {
        mBinding.toolbar.setNavigationOnClickListener(view -> onBackPressed());
    }

    private void initTabs() {
        ViewPagerAdapter adapter = new ViewPagerAdapter(getSupportFragmentManager());
        adapter.addFragment(new SubscribersFragment(getIntent().getStringExtra("user_id")), getResources().getString(R.string.tab_subscribers));
        adapter.addFragment(new SubscriptionsFragment(getIntent().getStringExtra("user_id")), getResources().getString(R.string.tab_subscriptions));
        mBinding.content.viewpager.setOffscreenPageLimit(2);
        mBinding.content.viewpager.setAdapter(adapter);
        mBinding.tablayout.setupWithViewPager(mBinding.content.viewpager);
    }

    @Override
    public void goSingOut() {
        MSharedPreferences.getInstance().setUserInfo(null);
        MSharedPreferences.getInstance().setFbToken(null);
        LoginManager.getInstance().logOut();
        startActivity(new Intent(this, SignInActivity.class));
        finish();
    }

    @Override
    public Context getContext() {
        return this;
    }
}
