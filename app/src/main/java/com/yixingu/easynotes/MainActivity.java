package com.yixingu.easynotes;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.design.widget.NavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.yixingu.easynotes.fragment.AboutFragment;
import com.yixingu.easynotes.fragment.BaseMainFragment;
import com.yixingu.easynotes.fragment.HomeFragment;

import me.yokeyword.fragmentation.SupportActivity;
import me.yokeyword.fragmentation.SupportFragment;
import me.yokeyword.fragmentation.anim.FragmentAnimator;
import me.yokeyword.fragmentation.helper.FragmentLifecycleCallbacks;

public class MainActivity extends SupportActivity implements
        NavigationView.OnNavigationItemSelectedListener,BaseMainFragment.OnFragmentOpenDrawerListener{

    private static final long WAIT_TIME = 2000L;

    private long TOUCH_TIME = 0;

    private DrawerLayout drawerLayout;
    private NavigationView navigationView;
    private ImageView navIcon;
    private TextView navName;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        if(savedInstanceState == null){
            loadRootFragment(R.id.fl_container, HomeFragment.newInstance());
        }

        initView();

        registerFragmentLifecycleCallbacks(new FragmentLifecycleCallbacks() {
            // 可以监听该Activity下的所有Fragment的18个 生命周期方法

            @Override
            public void onFragmentCreated(SupportFragment fragment, Bundle savedInstanceState) {
                Log.i("MainActivity", "onFragmentCreated--->" + fragment.getClass().getSimpleName());
            }
        });
    }

    private void initView() {
        drawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(this, drawerLayout, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
//        drawerLayout.setDrawerListener(toggle);
        toggle.syncState();

        navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);
        navigationView.setCheckedItem(R.id.nav_all);

        LinearLayout llNavHeader = (LinearLayout) navigationView.getHeaderView(0);
        navName = (TextView) llNavHeader.findViewById(R.id.tv_name);
        navIcon = (ImageView) llNavHeader.findViewById(R.id.img_nav);
        llNavHeader.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                drawerLayout.closeDrawer(GravityCompat.START);

                drawerLayout.postDelayed(new Runnable() {
                    @Override
                    public void run() {
//                        goLogin();
                    }
                }, 250);
            }
        });
    }


    @Override
    public FragmentAnimator onCreateFragmentAnimator() {
        // 设置默认Fragment动画  默认竖向(和安卓5.0以上的动画相同)
        return super.onCreateFragmentAnimator();
        // 设置横向(和安卓4.x动画相同)
//        return new DefaultHorizontalAnimator();
        // 设置自定义动画
//        return new FragmentAnimator(enter,exit,popEnter,popExit);
    }

    //侧滑栏选择
    @Override
    public boolean onNavigationItemSelected(@NonNull final MenuItem item) {
        drawerLayout.closeDrawer(GravityCompat.START);

        drawerLayout.postDelayed(new Runnable() {
            @Override
            public void run() {
                final SupportFragment topFragment = getTopFragment();
                switch (item.getItemId()){
                    case R.id.nav_about:
                        AboutFragment fragment = findFragment(AboutFragment.class);
                        if (fragment == null) {
                        popTo(HomeFragment.class, false, new Runnable() {
                            @Override
                            public void run() {
                                start(AboutFragment.newInstance());
                            }
                        });
                    } else {
                        // 如果已经在栈内,则以SingleTask模式start
                        start(fragment, SupportFragment.SINGLETASK);
                    }
                    break;

                }
//                if (id == R.id.nav_home) {
//
//                    HomeFragment fragment = findFragment(HomeFragment.class);
//                    Bundle newBundle = new Bundle();
//                    newBundle.putString("from", "主页-->来自:" + topFragment.getClass().getSimpleName());
//                    fragment.putNewBundle(newBundle);
//
//                    start(fragment, SupportFragment.SINGLETASK);
//                } else if (id == R.id.nav_discover) {
//                    DiscoverFragment fragment = findFragment(DiscoverFragment.class);
//                    if (fragment == null) {
//                        popTo(HomeFragment.class, false, new Runnable() {
//                            @Override
//                            public void run() {
//                                start(DiscoverFragment.newInstance());
//                            }
//                        });
//                    } else {
//                        // 如果已经在栈内,则以SingleTask模式start
//                        start(fragment, SupportFragment.SINGLETASK);
//                    }
//                } else if (id == R.id.nav_msg) {
//                    ShopFragment fragment = findFragment(ShopFragment.class);
//                    if (fragment == null) {
//                        popTo(HomeFragment.class, false, new Runnable() {
//                            @Override
//                            public void run() {
//                                start(ShopFragment.newInstance());
//                            }
//                        });
//                    } else {
//                        // 如果已经在栈内,则以SingleTask模式start,也可以用popTo
////                        start(fragment, SupportFragment.SINGLETASK);
//                        popTo(ShopFragment.class, false);
//                    }
//                } else if (id == R.id.nav_login) {
//                    goLogin();
//                } else if (id == R.id.nav_swipe_back) {
//                    startActivity(new Intent(MainActivity.this, SwipeBackSampleActivity.class));
//                } else if (id == R.id.nav_swipe_back_f) {
//                    start(SwipeBackSampleFragment.newInstance());
//                }
            }
        }, 250);

        return true;
    }

    @Override
    public void onBackPressedSupport() {
        if (drawerLayout.isDrawerOpen(GravityCompat.START)) {
            drawerLayout.closeDrawer(GravityCompat.START);
        } else {
            Fragment topFragment = getTopFragment();

            // 主页的Fragment
            if (topFragment instanceof BaseMainFragment) {
                navigationView.setCheckedItem(R.id.nav_all);
            }

            if (getSupportFragmentManager().getBackStackEntryCount() > 1) {
                pop();
            } else {
                if (System.currentTimeMillis() - TOUCH_TIME < WAIT_TIME) {
                    finish();
                } else {
                    TOUCH_TIME = System.currentTimeMillis();
                    Toast.makeText(this, R.string.press_again_exit, Toast.LENGTH_SHORT).show();
                }
            }
        }
    }

    @Override
    public void onOpenDrawer() {
        if (!drawerLayout.isDrawerOpen(GravityCompat.START)) {
            drawerLayout.openDrawer(GravityCompat.START);
        }
    }

}
