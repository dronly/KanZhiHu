package com.dronly.kanzhihu.ui;

import android.content.res.Configuration;
import android.os.Bundle;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.widget.Toolbar;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

import com.dronly.kanzhihu.R;
import com.dronly.kanzhihu.base.BaseActivity;
import com.dronly.kanzhihu.ui.fragment.ContentFragment;
import com.dronly.kanzhihu.ui.fragment.MenuFragment;
import com.dronly.kanzhihu.utils.ThemeUtils;
import com.dronly.kanzhihu.utils.ToastUtils;
import com.orhanobut.logger.Logger;
import com.rey.material.app.DatePickerDialog;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

import butterknife.Bind;
import butterknife.ButterKnife;


public class MainActivity extends BaseActivity {
    @Bind(R.id.toolbar)
    public Toolbar toolbar;
    @Bind(R.id.drawer_layout)
    DrawerLayout mDrawerLayout;
    ActionBarDrawerToggle mDrawerToggle;
    private MenuFragment mMenuFragment;
    private long selectedTime;
    public DatePickerDialog dialog ;
    private long exitTime;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);
        toolbar.setBackgroundColor(ThemeUtils.getThemeColor());
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowTitleEnabled(false);
        mDrawerToggle = new ActionBarDrawerToggle(this, mDrawerLayout, toolbar, R.string.app_name, R.string.app_name);
        mDrawerLayout.addDrawerListener(mDrawerToggle);
        toolbar.setTitle(getResources().getString(R.string.yesterday));
        replaceFragment(R.id.frame_content, new ContentFragment());
        mMenuFragment = new MenuFragment();
        replaceFragment(R.id.frame_menu, mMenuFragment);
        selectedTime = System.currentTimeMillis();

    }

    @Override
    public void finish() {
        super.finish();
        overridePendingTransition(R.anim.anim_none, R.anim.anim_mian_out);
    }

    /**
     * 关闭菜单栏
     */
    public void closeDrawer() {
        mDrawerLayout.closeDrawers();
    }

    /**
     * 设置toolbar标题
     */
    public void setToolbarTitle(String title) {
        toolbar.setTitle(title);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.calendar) {

//            showDatePickerDialog();
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    private void showDatePickerDialog() {
        dialog = new DatePickerDialog(MainActivity.this);
        long maxTime = System.currentTimeMillis();
        Calendar cal = dialog.getCalendar();
        cal.setTimeInMillis(maxTime);
        cal.add(Calendar.MONTH, -1);
        long minTime = cal.getTimeInMillis();
        dialog.dateRange(minTime, maxTime)
                .date(selectedTime)
                .positiveAction(R.string.sure)
                .positiveActionClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        SimpleDateFormat formatter = new SimpleDateFormat("yyyyMMdd");
                        Date selectedDate = new Date(dialog.getDate());//获取当前时间
                        selectedTime = dialog.getDate();
                        String selectedTimeStr = formatter.format(selectedDate);
                        Logger.d(selectedTimeStr);
                        replaceFragment(R.id.frame_content, ContentFragment.getInstance(mMenuFragment.getSelectedItem(), selectedTimeStr));
                        dialog.dismiss();
                    }
                })
                .negativeAction(R.string.cancel)
                .negativeActionClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        dialog.dismiss();
                    }
                })
                .show();
    }


    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK
                && event.getAction() == KeyEvent.ACTION_DOWN) {
            if ((System.currentTimeMillis() - exitTime) > 2000) {
                ToastUtils.show("再按一次退出程序");
                exitTime = System.currentTimeMillis();
            } else {
                finish();
            }
            return true;
        }

        return super.onKeyDown(keyCode, event);
    }

    protected void onPostCreate(Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);
        mDrawerToggle.syncState();//该方法会自动和actionBar关联, 将开关的图片显示在了action上，如果不设置，也可以有抽屉的效果，不过是默认的图标
    }


    /**
     * 设备配置改变时
     */
    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        mDrawerToggle.onConfigurationChanged(newConfig);
    }
}
