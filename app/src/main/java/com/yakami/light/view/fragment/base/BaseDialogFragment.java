package com.yakami.light.view.fragment.base;

import android.content.Context;
import android.content.res.Resources;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.view.View;
import android.view.Window;

/**
 * @author Yakami, Created on 2016/4/18
 */
public class BaseDialogFragment extends DialogFragment {

    public Context mContext;
    public Resources mRes;
    public static final String TAG = "DIALOG";

    @Override
    public void onCreate(Bundle savedBundle) {
        super.onCreate(savedBundle);
        mContext = getActivity();
        mRes = getResources();
    }

    @Override
    public void onViewCreated(View v, @Nullable Bundle savedInstanceState) {
        //在5.0以下去标题
        getDialog().requestWindowFeature(Window.FEATURE_NO_TITLE);
    }
}
