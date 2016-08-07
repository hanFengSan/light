package com.yakami.light.view.fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.yakami.light.BuildConfig;
import com.yakami.light.R;
import com.yakami.light.view.fragment.base.BaseFragment;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * Created by Yakami on 2016/8/5, enjoying it!
 */

public class AboutFragment extends BaseFragment {

    @Bind(R.id.tv_about) TextView mAbout;

    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_about, container, false);
        ButterKnife.bind(this,view);

        mAbout.setText("版本: " + BuildConfig.VERSION_NAME + "\n" + mRes.getString(R.string.author));
        mAbout.append("\ngithub地址：https://github.com/hanFengSan/light");
        return view;
    }
}
