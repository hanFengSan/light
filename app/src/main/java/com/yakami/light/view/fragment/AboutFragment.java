package com.yakami.light.view.fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.yakami.light.R;
import com.yakami.light.view.fragment.base.BaseFragment;

import butterknife.ButterKnife;

/**
 * Created by Yakami on 2016/8/5, enjoying it!
 */

public class AboutFragment extends BaseFragment {

    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_about, container, false);
        ButterKnife.bind(this,view);

        return view;
    }
}
