package com.yakami.light.view.fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.yakami.light.R;
import com.yakami.light.view.fragment.base.BaseFragment;

import butterknife.ButterKnife;

/**
 * @author Yakami, Created on 2016/4/10
 * 炮灰用，SlidingTabLayout需要Fragment，而拥有SwipeRefreshLayout的fragment才能使SlidingTabLayout正常工作
 * 原因未知
 */
public class SlidingTabFragment extends BaseFragment {

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_sliding_tab, container, false);
        ButterKnife.bind(this, view);

        return view;
    }

}
