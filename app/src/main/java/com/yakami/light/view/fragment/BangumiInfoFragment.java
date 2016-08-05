package com.yakami.light.view.fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.yakami.light.R;
import com.yakami.light.event.Event;
import com.yakami.light.event.RxBus;
import com.yakami.light.utils.Tools;
import com.yakami.light.utils.ViewUtils;
import com.yakami.light.view.fragment.base.BaseFragment;

import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * Created by Yakami on 2016/8/3, enjoying it!
 */

public class BangumiInfoFragment extends BaseFragment {

    @Bind(R.id.tv_info) TextView mTextView;
    @Bind(R.id.root) LinearLayout mRoot;

    private List<String> mInfo = new ArrayList<>();

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInStanceState) {
        View view = inflater.inflate(R.layout.fragment_bangumi_info, container, false);

        ButterKnife.bind(this, view);

        setText();

        //用于调整ViewPager高度
        mRoot.post(() -> {
            Event event = new Event();
            event.type = Event.EventType.DETAIL_FRAGMENT_HEIGHT;
            event.message = ViewUtils.getVerticalLinearLayoutHeight(mRoot);
            RxBus.getInstance().send(event);
        });
        return view;
    }

    public void setData(List<String> info) {
        mInfo = info;
        if (mTextView != null)
            setText();
    }

    private void setText() {
        mTextView.setText("");
        for (String str : mInfo) {
            if (str.contains(":")) {
                String[] infoArray = str.split(":");
                mTextView.append(Tools.getBoldSpannableStr(infoArray[0] + ":"));
                mTextView.append(" ");
                mTextView.append(infoArray[1]);
                mTextView.append("\n");
            }
        }
    }
}
