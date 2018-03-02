package com.example.alex.fitofan.ui.activity.main;

import android.content.Context;
import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.net.Uri;
import android.os.Bundle;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

import com.bumptech.glide.Glide;
import com.bumptech.glide.Priority;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.example.alex.fitofan.R;
import com.example.alex.fitofan.databinding.ActivityMainBinding;
import com.example.alex.fitofan.ui.activity.my_profile.MyProfileActivity;
import com.example.alex.fitofan.ui.activity.settings.SettingActivity;
import com.example.alex.fitofan.ui.activity.signin.SignInActivity;
import com.example.alex.fitofan.ui.fragments.my_plans.MyPlansFragment;
import com.example.alex.fitofan.ui.fragments.participants.ParticiplantFragment;
import com.example.alex.fitofan.ui.fragments.wall.WallFragment;

import de.hdodenhof.circleimageview.CircleImageView;

public class MainActivity extends AppCompatActivity
        implements MainContract.View, NavigationView.OnNavigationItemSelectedListener {

    private ActivityMainBinding mBinding;
    private MainPresenter mPresenter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mBinding = DataBindingUtil.setContentView(this, R.layout.activity_main);
        mPresenter = new MainPresenter(this);

        setSupportActionBar(mBinding.appBarMain.toolbar);

        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, mBinding.drawerLayout, mBinding.appBarMain.toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        mBinding.drawerLayout.addDrawerListener(toggle);
        toggle.syncState();

        mBinding.navView.setNavigationItemSelectedListener(this);

        initTabs();
//        loadAvatar();
    }

    private void loadAvatar() {
        CircleImageView imageProfile = findViewById(R.id.nav_profileImage);

        Uri uri = Uri.parse("http://backbreaker.net/wp-content/uploads/2015/11/1295992106_brad_pitt.jpg");
        Glide.with(getContext()) //передаем контекст приложения
                .load(uri)
                .fitCenter()
                .thumbnail(0.5f)
                .priority(Priority.IMMEDIATE)
                .placeholder(R.mipmap.ic_launcher)
                .diskCacheStrategy(DiskCacheStrategy.SOURCE)
                .into(imageProfile); //ссылка на ImageView
    }

    private void initTabs(){
        ViewPagerAdapter adapter = new ViewPagerAdapter(getSupportFragmentManager());
        adapter.addFragment(new WallFragment(), "Wall");
        adapter.addFragment(new ParticiplantFragment(), "Participlants");
        adapter.addFragment(new MyPlansFragment(), "MyPlans");
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

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.nav_my_profile){
            startActivity(new Intent(this, MyProfileActivity.class));
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
            //sing out
            mPresenter.alertExit();
        }

        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    @Override
    public void goSingOut() {
        startActivity(new Intent(this, SignInActivity.class));
        finish();
    }

    @Override
    public Context getContext() {
        return this;
    }
}
