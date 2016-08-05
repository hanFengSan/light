package com.yakami.light.view.fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.yakami.light.R;
import com.yakami.light.view.fragment.base.BaseDialogFragment;

import butterknife.ButterKnife;

/**
 * Created by Yakami on 2016/8/4, enjoying it!
 */

public class MsgDialogFragment extends BaseDialogFragment {

    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.dialog_message, container, false);
        ButterKnife.bind(this, view);
        return view;
    }

}
