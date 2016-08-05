package com.yakami.light.view.activity.base;

import android.support.annotation.CallSuper;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;

import com.annimon.stream.Stream;

/**
 * @author Yakami, Created on 2016/4/21
 */
public abstract class BaseTransparentTabActivity extends BaseTransparentActivity {

    @CallSuper
    protected void initTabs(boolean isRestarted) {
        if (isRestarted) {
            //回收activity时去掉所添加的fragment，否则重建时会重复添加，而老的fragment重建会丢失回调对象。
            FragmentManager manager = getSupportFragmentManager();
            FragmentTransaction transaction = manager.beginTransaction();
            if (manager.getFragments() == null)
                return;
            Stream.of(manager.getFragments())
                    .forEach((fragment) -> {
                        if (fragment != null)
                            transaction.remove(fragment);
                    });
            transaction.commit();
            manager.executePendingTransactions();
        }
    }
}
