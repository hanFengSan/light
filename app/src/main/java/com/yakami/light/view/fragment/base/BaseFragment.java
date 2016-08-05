package com.yakami.light.view.fragment.base;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.Point;
import android.os.Bundle;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.widget.Toolbar;
import android.view.Display;
import android.view.LayoutInflater;
import android.widget.TextView;

import com.yakami.light.R;
import com.yakami.light.utils.Tools;

import nucleus.presenter.Presenter;
import nucleus.view.NucleusFragment;

public class BaseFragment<P extends Presenter> extends NucleusFragment<P> {

    public Context mContext;
    public LayoutInflater mInflater;
    public Resources mRes;
    public Toolbar mToolbar;
    public TextView mTitle;
    public static final String TAG = "BASE_FRAGMENT";

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
//        setRetainInstance(true);
        mContext = getActivity();
        mRes = mContext.getResources();
        mInflater = getLayoutInflater(savedInstanceState);
    }

    /*由于setRetainInstance(true)，重建时fragment的create会比activity快，所以在这绑定控件*/
    @Override
    public void onResume() {
        super.onResume();
        mToolbar = (Toolbar) getActivity().findViewById(R.id.toolbar);
        mTitle = (TextView) mToolbar.findViewById(R.id.toolbar_title);
    }

    public FragmentTransaction getChildSlideAnimTransaction() {
        FragmentTransaction transaction = getChildFragmentManager().beginTransaction();
        transaction.setCustomAnimations(R.anim.slide_from_right, R.anim.slide_out_left);
        return transaction;
    }

    public FragmentTransaction getChildTransaction() {
        return getChildFragmentManager().beginTransaction();
    }


    public void toast(String str) {
        Tools.toast(str);
    }

    //Point.x为width, Point.y为height
    public Point getScreenSize() {
        Display display = getActivity().getWindowManager().getDefaultDisplay();
        Point size = new Point();
        display.getSize(size);
        return size;
    }

}
