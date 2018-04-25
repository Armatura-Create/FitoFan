package com.example.alex.fitofan.ui.activity.main;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.net.Uri;
import android.os.Bundle;
import android.support.design.widget.NavigationView;
import android.support.v4.app.ActivityCompat;
import android.support.v4.view.GravityCompat;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.example.alex.fitofan.R;
import com.example.alex.fitofan.databinding.ActivityMainBinding;
import com.example.alex.fitofan.eventbus.MyPlansEvent;
import com.example.alex.fitofan.models.GetUserModel;
import com.example.alex.fitofan.settings.MSharedPreferences;
import com.example.alex.fitofan.ui.activity.create_plan.CreatePlanActivity;
import com.example.alex.fitofan.ui.activity.settings.SettingActivity;
import com.example.alex.fitofan.ui.activity.signin.SignInActivity;
import com.example.alex.fitofan.ui.activity.user_profile.UserProfileActivity;
import com.example.alex.fitofan.ui.fragments.my_plans.MyPlansFragment;
import com.example.alex.fitofan.ui.fragments.rainting.ParticipantFragment;
import com.example.alex.fitofan.ui.fragments.wall.WallFragment;
import com.example.alex.fitofan.utils.Connection;
import com.facebook.login.LoginManager;
import com.google.gson.Gson;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import de.hdodenhof.circleimageview.CircleImageView;

import static com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions.withCrossFade;
import static com.bumptech.glide.request.RequestOptions.centerCropTransform;

public class MainActivity extends AppCompatActivity
        implements MainContract.View, NavigationView.OnNavigationItemSelectedListener {

    private ActivityMainBinding mBinding;
    private MainPresenter mPresenter;
    private View navHeader;
    private static final int PERMISSION_REQUEST_CODE = 100;
    private static final int SELECT_IMAGE = 200;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mBinding = DataBindingUtil.setContentView(this, R.layout.activity_main);
        mPresenter = new MainPresenter(this);

        setSupportActionBar(mBinding.appBarMain.toolbar);

        navHeader = mBinding.navView.getHeaderView(0);

        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, mBinding.drawerLayout, mBinding.appBarMain.toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        mBinding.drawerLayout.addDrawerListener(toggle);
        toggle.syncState();

        mBinding.navView.setNavigationItemSelectedListener(this);

        initCheckConnection();
        initTabs();
        initListeners();
        loadHeader();
    }

    private void initListeners() {
        navHeader.findViewById(R.id.nav_profileImage).setOnClickListener(view -> {
//            requestMultiplePermissions();
//            if (ContextCompat.checkSelfPermission(this, Manifest.permission.READ_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED)
//                chooseAvatar();
            startActivity(new Intent(this, UserProfileActivity.class));
        });
    }

    void chooseAvatar() {
        Intent photoPickerIntent = new Intent(Intent.ACTION_PICK);
        photoPickerIntent.setType("image/*");
        startActivityForResult(photoPickerIntent, SELECT_IMAGE);
    }

    public void requestMultiplePermissions() {
        ActivityCompat.requestPermissions(this,
                new String[]{
                        Manifest.permission.READ_EXTERNAL_STORAGE,
                        Manifest.permission.WRITE_EXTERNAL_STORAGE
                },
                PERMISSION_REQUEST_CODE);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch (requestCode) {
            case SELECT_IMAGE:
                if (resultCode == RESULT_OK) {
                    if (data != null)
                        if (data.getData() != null) {
                            loadHeader(data);
                        }
                }
                break;
        }
    }

    private void initCheckConnection() {
        Connection.isNetworkAvailable(mBinding.appBarMain.contentMain.container, this);
    }

    private void loadHeader() {
        CircleImageView imageProfile = navHeader.findViewById(R.id.nav_profileImage);
        TextView firstName = navHeader.findViewById(R.id.first_name);
        TextView lastName = navHeader.findViewById(R.id.last_name);

        if (MSharedPreferences.getInstance().getUserInfo() != null &&
                new Gson().fromJson(MSharedPreferences.getInstance().getUserInfo(), GetUserModel.class) != null
                && new Gson().fromJson(MSharedPreferences.getInstance().getUserInfo(), GetUserModel.class).getUser() != null) {

            GetUserModel model = new Gson().fromJson(MSharedPreferences.getInstance().getUserInfo(), GetUserModel.class);

            Uri uri = Uri.parse(model.getUser().getImage_url());
            Glide.with(getApplicationContext()) //передаем контекст приложения
                    .load(uri)
                    .apply(centerCropTransform())
                    .transition(withCrossFade())
                    .into(imageProfile);

            firstName.setText(model.getUser().getName());
            lastName.setText(model.getUser().getSurname());

        } else {
            firstName.setText("Name");
            lastName.setText("Surname");

            Uri uri = Uri.parse("http://backbreaker.net/wp-content/uploads/2015/11/1295992106_brad_pitt.jpg");
            Glide.with(getApplicationContext()) //передаем контекст приложения
                    .load(uri)
                    .apply(centerCropTransform())
                    .transition(withCrossFade())
                    .into(imageProfile); //ссылка на ImageView
        }

        //TODO Доделать подгрузку имени
    }

    private void loadHeader(Intent data) {
        CircleImageView imageProfile = navHeader.findViewById(R.id.nav_profileImage);
        Glide.with(getApplicationContext()) //передаем контекст приложения
                .load(data.getData())
                .into(imageProfile); //ссылка на ImageView
    }


    private void initTabs() {
        ViewPagerAdapter adapter = new ViewPagerAdapter(getSupportFragmentManager());
        adapter.addFragment(new WallFragment(), getResources().getString(R.string.tab_wall));
        adapter.addFragment(new ParticipantFragment(), getResources().getString(R.string.tab_participants));
        adapter.addFragment(new MyPlansFragment(), getResources().getString(R.string.tab_my_plan));
        mBinding.appBarMain.contentMain.viewpager.setOffscreenPageLimit(3);
        mBinding.appBarMain.contentMain.viewpager.setAdapter(adapter);
        mBinding.appBarMain.tablayout.setupWithViewPager(mBinding.appBarMain.contentMain.viewpager);
    }

    @Override
    public void onBackPressed() {
        if (mBinding.drawerLayout.isDrawerOpen(GravityCompat.START)) {
            mBinding.drawerLayout.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            startActivity(new Intent(this, SettingActivity.class));
            return true;
        }
        if (id == R.id.action_exit) {
            mPresenter.alertExit();
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onStart() {
        EventBus.getDefault().register(this);
        super.onStart();
    }

    @Override
    protected void onResume() {
        EventBus.getDefault().unregister(this);
        EventBus.getDefault().register(this);
        super.onResume();
    }

    @Override
    public void onStop() {
        EventBus.getDefault().unregister(this);
        super.onStop();
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onMyPlansEvent(MyPlansEvent event) {
        mBinding.drawerLayout.closeDrawer(GravityCompat.START);
        mBinding.appBarMain.contentMain.viewpager.setCurrentItem(2);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.nav_my_profile) {
            startActivity(new Intent(this, UserProfileActivity.class));
        } else if (id == R.id.nav_wall) {
            mBinding.appBarMain.contentMain.viewpager.setCurrentItem(0);
        } else if (id == R.id.nav_participants) {
            mBinding.appBarMain.contentMain.viewpager.setCurrentItem(1);
        } else if (id == R.id.nav_my_plans) {
            mBinding.appBarMain.contentMain.viewpager.setCurrentItem(2);
        } else if (id == R.id.nav_settings) {
            startActivity(new Intent(this, SettingActivity.class));
        } else if (id == R.id.nav_share) {
            mPresenter.shareApp();
        } else if (id == R.id.nav_exit) {
            mPresenter.alertExit();
        } else if (id == R.id.nav_add_plan) {
            startActivity(new Intent(getContext(), CreatePlanActivity.class));
        }

        mBinding.drawerLayout.closeDrawer(GravityCompat.START);
        return true;
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
