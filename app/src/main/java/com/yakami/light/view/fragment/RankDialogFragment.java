package com.yakami.light.view.fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.jakewharton.rxbinding.view.RxView;
import com.rey.material.widget.Switch;
import com.yakami.light.AppManager;
import com.yakami.light.DeviceManager;
import com.yakami.light.R;
import com.yakami.light.bean.BangumiRank;
import com.yakami.light.bean.NotificationItem;
import com.yakami.light.service.CopyService;
import com.yakami.light.utils.Tools;
import com.yakami.light.view.fragment.base.BaseDialogFragment;

import java.util.concurrent.TimeUnit;

import butterknife.Bind;
import butterknife.ButterKnife;

import static com.yakami.light.bean.NotificationProfile.SWITCH_DIVE;
import static com.yakami.light.bean.NotificationProfile.SWITCH_FLY;
import static com.yakami.light.bean.NotificationProfile.SWITCH_IGNORE_FISH;
import static com.yakami.light.bean.NotificationProfile.SWITCH_TOP;
import static com.yakami.light.bean.NotificationProfile.SWITCH_UPDATE;
import static com.yakami.light.bean.NotificationProfile.SWITCH_WATCH;

/**
 * Created by Yakami on 2016/6/15, enjoying it!
 */

public class RankDialogFragment extends BaseDialogFragment {

    @Bind(R.id.title) TextView mTitle;
    @Bind(R.id.switch_update) Switch mSwitchUpdate;
    @Bind(R.id.switch_fly) Switch mSwitchFly;
    @Bind(R.id.switch_dive) Switch mSwitchDive;
    @Bind(R.id.switch_ignore_fish) Switch mSwitchFish;
    @Bind(R.id.switch_top) Switch mSwitchTop;
    @Bind(R.id.switch_watch) Switch mSwitchWatch;
    @Bind(R.id.tv_copy) TextView mCopy;

    private String mTitleStr;
    private BangumiRank mInfo;
    private int[] mSwitchIdArray = {SWITCH_WATCH, SWITCH_UPDATE, SWITCH_FLY, SWITCH_DIVE, SWITCH_IGNORE_FISH, SWITCH_TOP};

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceBundle) {
        View view = inflater.inflate(R.layout.dialog_disc_rank, container, false);
        ButterKnife.bind(this, view);

        if (mTitleStr != null) {
            mTitle.setText(mTitleStr);
        }

        //初始化Switch
        for (NotificationItem tmpItem : AppManager.getNotificationService().getList()) {
            if (tmpItem.getId() == mInfo.getId()) {
                if (tmpItem.isUpdate())
                    mSwitchUpdate.setChecked(true);
                if (tmpItem.isFly())
                    mSwitchFly.setChecked(true);
                if (tmpItem.isDive())
                    mSwitchDive.setChecked(true);
                if (tmpItem.isIgnoreFish())
                    mSwitchFish.setChecked(true);
                if (tmpItem.isTop())
                    mSwitchTop.setChecked(true);
                if (tmpItem.isWatched())
                    mSwitchWatch.setChecked(true);
            }
        }

        //设置各个switch点击事件
        Switch[] switchArray = {mSwitchWatch, mSwitchUpdate, mSwitchFly, mSwitchDive, mSwitchFish, mSwitchTop};
        for (int i = 0; i < switchArray.length; i++) {
            Switch item = switchArray[i];
            final int t = i;
            item.setOnCheckedChangeListener((buttonView, isChecked) -> {
                if (isChecked)
                    AppManager.getNotificationService().setItemValue(mInfo.getId(), mSwitchIdArray[t], true);
                else
                    AppManager.getNotificationService().setItemValue(mInfo.getId(), mSwitchIdArray[t], false);
            });
        }

        RxView.clicks(mCopy)
                .throttleFirst(500, TimeUnit.MILLISECONDS)
                .subscribe(avoid -> {
                    DeviceManager.copyToClipboard(CopyService.copyRank(AppManager.getDiscRankService()
                            .toDicsRank(mInfo)));
                    Tools.toast(mRes.getString(R.string.copy_completed));
                });

        return view;
    }

    public void setBangumiRankInfo(BangumiRank info) {
        mInfo = info;
        if (mTitle != null)
            mTitle.post(() -> mTitle.setText(info.getName()));
        else
            mTitleStr = info.getName();
    }

}
