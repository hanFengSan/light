package com.yakami.light.view.fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;

import com.jakewharton.rxbinding.view.RxView;
import com.rey.material.widget.Button;
import com.rey.material.widget.EditText;
import com.rey.material.widget.Spinner;
import com.yakami.light.AppManager;
import com.yakami.light.R;
import com.yakami.light.service.NotificationService;
import com.yakami.light.utils.Tools;
import com.yakami.light.view.fragment.base.BaseFragment;

import java.util.concurrent.TimeUnit;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * Created by Yakami on 2016/8/7, enjoying it!
 * 爆上比例、杂鱼线等的用户自定义
 */

public class SettingsFragment extends BaseFragment {

    @Bind(R.id.fly_spinner) Spinner mFlySpinner;
    @Bind(R.id.dive_spinner) Spinner mDiveSpinner;
    @Bind(R.id.fish_line) EditText mFishLineEdit;
    @Bind(R.id.top_line) EditText mTopLineEdit;
    @Bind(R.id.button_completed) Button mButton;

    private float mFlyScale = 0.25f;
    private float mDiveScale = 0.25f;

    ArrayAdapter<String> mAdapter;

    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment__settings, container, false);
        ButterKnife.bind(this, view);

        init();

        return view;
    }

    private void init() {
        //设置项
        String[] array = new String[8];
        for (int i = 0; i < 8; i++) {
            array[i] = (i + 1) * 25 + "%";
        }
        mAdapter = new ArrayAdapter<String>(getActivity(), android.R.layout.simple_spinner_item, array);
        mAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        mFlySpinner.setAdapter(mAdapter);
        mDiveSpinner.setAdapter(mAdapter);
        //初始化
        NotificationService service = AppManager.getNotificationService();
        mFlySpinner.setSelection((int) (service.getFlyScale() * 100 / 25 - 1));
        mDiveSpinner.setSelection((int) (service.getDiveScale() * 100 / 25 - 1));
        mFishLineEdit.setText(service.getFishLine() + "");
        mTopLineEdit.setText(service.getTop() + "");
        //监听
        mFlySpinner.setOnItemSelectedListener((parent, view, position, id) -> mFlyScale = (position + 1) * 25 / 100f);
        mDiveSpinner.setOnItemSelectedListener((parent, view, position, id) -> mDiveScale = (position + 1) * 25 / 100f);
        RxView.clicks(mButton)
                .throttleFirst(500, TimeUnit.MILLISECONDS)
                .subscribe(aVoid -> {
                    service.setFlyScale(mFlyScale);
                    service.setDiveScale(mDiveScale);
                    service.setFishLine(Integer.valueOf(mFishLineEdit.getText().toString()));
                    service.setTop(Integer.valueOf(mTopLineEdit.getText().toString()));
                    service.save();
                    Tools.toast(mRes.getString(R.string.setting_ok));
                    getActivity().finish();
                });
    }

}
