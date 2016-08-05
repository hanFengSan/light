package com.yakami.light.view.activity.base;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.res.Resources;
import android.graphics.Point;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.Display;
import android.view.Window;
import android.view.WindowManager;
import android.widget.TextView;

import com.yakami.light.R;
import com.yakami.light.utils.IntentHelper;
import com.yakami.light.utils.Tools;

import butterknife.Bind;
import butterknife.ButterKnife;
import nucleus.view.NucleusActivity;
import uk.co.chrisjenx.calligraphy.CalligraphyContextWrapper;

import static android.content.pm.ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE;
import static android.content.pm.ActivityInfo.SCREEN_ORIENTATION_PORTRAIT;

public abstract class BaseActivity extends NucleusActivity {

    @Bind(R.id.toolbar_title) protected TextView mTitle;
    @Bind(R.id.toolbar) protected Toolbar mToolbar;
    protected Resources mRes;
    protected Context mContext;
    protected Context mActivityContext;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(onBindLayout());

        ButterKnife.bind(this);

        setSupportActionBar(mToolbar);

        mRes = getResources();
        mContext = getApplicationContext();
        mActivityContext = getContext();

        setRequestedOrientation(SCREEN_ORIENTATION_PORTRAIT);
    }

    protected abstract int onBindLayout();

    public void toast(String str) {
        Tools.toast(str);
    }

    public Context getContext() {
        return BaseActivity.this;
    }

    public int getStatusBarHeight() {
        int result = 0;
        int resourceId = getResources().getIdentifier("status_bar_height", "dimen", "android");
        if (resourceId > 0) {
            result = getResources().getDimensionPixelSize(resourceId);
        }
        return result;
    }

    public int getNavigationBarHeight() {
        int resourceId = mRes.getIdentifier("navigation_bar_height", "dimen", "android");
        if (resourceId > 0) {
            return mRes.getDimensionPixelSize(resourceId);
        }
        return 0;
    }

    public IntentHelper getIntentHelper() {
        return IntentHelper.newInstance(getIntent());
    }

    @Override
    protected void attachBaseContext(Context newBase) {
        super.attachBaseContext(CalligraphyContextWrapper.wrap(newBase));
    }

    //Point.x为width, Point.y为height
    public Point getScreenSize() {
        Display display = getWindowManager().getDefaultDisplay();
        Point size = new Point();
        display.getSize(size);
        return size;
    }

    public static void setWindowFlag(Activity activity, final int bits, boolean on) {
        Window win = activity.getWindow();
        WindowManager.LayoutParams winParams = win.getAttributes();
        if (on) {
            winParams.flags |= bits;
        } else {
            winParams.flags &= ~bits;
        }
        win.setAttributes(winParams);
    }

    protected void setScreenLandScapeEnabled() {
        setRequestedOrientation(SCREEN_ORIENTATION_LANDSCAPE);
    }

    protected void share(String subject, String text) {
        Intent shareIntent = new Intent();
        shareIntent.setAction(Intent.ACTION_SEND);
        shareIntent.putExtra(Intent.EXTRA_SUBJECT, subject);
        shareIntent.putExtra(Intent.EXTRA_TEXT, text);
        shareIntent.setType("text/plain");
        shareIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_WHEN_TASK_RESET);
        //设置分享列表的标题，并且每次都显示分享列表
        startActivity(Intent.createChooser(shareIntent, "分享到"));
    }

    protected void OpenUrl(String url) {
        Intent intent = new Intent();
        intent.setAction("android.intent.action.VIEW");
        Uri content_url = Uri.parse(url);
        intent.setData(content_url);
        startActivity(intent);
    }
}
