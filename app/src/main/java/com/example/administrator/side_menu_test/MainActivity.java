package com.example.administrator.side_menu_test;

import android.animation.Animator;
import android.content.res.Configuration;
import android.graphics.Color;
import android.graphics.drawable.BitmapDrawable;
import android.os.Build;
import android.os.PersistableBundle;
import android.support.annotation.Nullable;
import android.support.annotation.RequiresApi;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.Gravity;
import android.view.View;
import android.view.animation.AccelerateInterpolator;
import android.widget.LinearLayout;

import java.util.ArrayList;
import java.util.List;

import fragment.ContentFragment;
import io.codetail.animation.ViewAnimationUtils;
import yalantis.com.sidemenu.interfaces.Resourceble;
import yalantis.com.sidemenu.interfaces.ScreenShotable;
import yalantis.com.sidemenu.model.SlideMenuItem;
import yalantis.com.sidemenu.util.ViewAnimator;

public class MainActivity extends AppCompatActivity implements ViewAnimator.ViewAnimatorListener {

    private DrawerLayout drawerlayout;
    private LinearLayout linearlayout;
    private Toolbar toolbar;
    private ActionBarDrawerToggle drawerToggle;
    private List<SlideMenuItem> list= new ArrayList<>();
    private ViewAnimator<SlideMenuItem> viewAnimator;
    private ContentFragment contentFragment;
    private int res;

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        contentFragment = ContentFragment.newInstance(R.drawable.content_music);
        getSupportFragmentManager().beginTransaction()
                .replace(R.id.fragment_container, contentFragment)
                .commit();
        drawerlayout=(DrawerLayout)findViewById(R.id.drawerlayout);
        drawerlayout.setScrimColor(Color.TRANSPARENT);
        linearlayout=(LinearLayout)findViewById(R.id.linearlayout);
        initToolbar();
        createMenuList();
        viewAnimator = new ViewAnimator<>(this, list, contentFragment, drawerlayout, this);
    }

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    private void initToolbar() {
        toolbar=(Toolbar)findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setHomeButtonEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        drawerToggle=new ActionBarDrawerToggle(this,drawerlayout,toolbar,R.string.drawer_open,R.string.drawer_close){
            @Override
            public void onDrawerSlide(View drawerView, float slideOffset) {
                super.onDrawerSlide(drawerView, slideOffset);
                if (slideOffset > 0.6 && linearlayout.getChildCount() == 0)
                    viewAnimator.showMenuContent();
            }

           public void onDrawerClosed(View drawerView) {
                super.onDrawerClosed(drawerView);
                linearlayout.removeAllViews();
                linearlayout.invalidate();
            }


        };
        drawerToggle.syncState();
        drawerlayout.setDrawerListener(drawerToggle);


    }

    @Override
    public void onPostCreate(@Nullable Bundle savedInstanceState, @Nullable PersistableBundle persistentState) {
        super.onPostCreate(savedInstanceState, persistentState);
        drawerToggle.syncState();
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        drawerToggle.onConfigurationChanged(newConfig);
    }

    private void createMenuList() {
        SlideMenuItem menuItem0 = new SlideMenuItem(ContentFragment.CLOSE, R.drawable.icn_close);
        list.add(menuItem0);
        SlideMenuItem menuItem = new SlideMenuItem(ContentFragment.BUILDING, R.drawable.icn_1);
        list.add(menuItem);
        SlideMenuItem menuItem2 = new SlideMenuItem(ContentFragment.BOOK, R.drawable.icn_2);
        list.add(menuItem2);
        SlideMenuItem menuItem3 = new SlideMenuItem(ContentFragment.PAINT, R.drawable.icn_3);
        list.add(menuItem3);
        SlideMenuItem menuItem4 = new SlideMenuItem(ContentFragment.CASE, R.drawable.icn_4);
        list.add(menuItem4);
        SlideMenuItem menuItem5 = new SlideMenuItem(ContentFragment.SHOP, R.drawable.icn_5);
        list.add(menuItem5);
        SlideMenuItem menuItem6 = new SlideMenuItem(ContentFragment.PARTY, R.drawable.icn_6);
        list.add(menuItem6);
        SlideMenuItem menuItem7 = new SlideMenuItem(ContentFragment.MOVIE, R.drawable.icn_7);
        list.add(menuItem7);
    }



    private ScreenShotable replaceFragment(ScreenShotable screenShotable, int position) {
        this.res = this.res == R.drawable.content_music ? R.drawable.content_films : R.drawable.content_music;
        View view = findViewById(R.id.fragment_container);
        int finalRadius = Math.max(view.getWidth(), view.getHeight());
        Animator animator = ViewAnimationUtils.createCircularReveal(view, 0, position, 0, finalRadius);
        animator.setInterpolator(new AccelerateInterpolator());
        animator.setDuration(ViewAnimator.CIRCULAR_REVEAL_ANIMATION_DURATION);

        findViewById(R.id.fragment_container).setBackgroundDrawable(new BitmapDrawable(getResources(), screenShotable.getBitmap()));
        animator.start();
        ContentFragment contentFragment = ContentFragment.newInstance(this.res);
        getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, contentFragment).commit();
        return contentFragment;
    }
    @Override
    public ScreenShotable onSwitch(Resourceble slideMenuItem, ScreenShotable screenShotable, int position) {
        switch (slideMenuItem.getName()) {
            case ContentFragment.CLOSE:
                return screenShotable;
            default:
                return replaceFragment(screenShotable, position);
        }
    }

    @Override
    public void disableHomeButton() {
        getSupportActionBar().setHomeButtonEnabled(false);
    }

    @Override
    public void enableHomeButton() {
        getSupportActionBar().setHomeButtonEnabled(true);
        drawerlayout.closeDrawers();
    }

    @Override
    public void addViewToContainer(View view) {
        linearlayout.addView(view);
    }
}
