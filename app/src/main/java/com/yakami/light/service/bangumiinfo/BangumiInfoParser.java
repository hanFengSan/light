package com.yakami.light.service.bangumiinfo;

import com.yakami.light.bean.BangumiInfo;

/**
 * Created by Yakami on 2016/8/6, enjoying it!
 */

public interface BangumiInfoParser {

    void start(String trueName);

    void setOnGetInfoListener(OnGetInfoListener listener);

    void setOnGetStepInfoListener(OnGetStepInfoListener listener);

    interface OnGetInfoListener {
        void onGetTextInfo(BangumiInfo info);

        void onGetFullInfo(BangumiInfo info);
    }

    interface OnGetStepInfoListener {

        void print(String info);

        void onGetCoverError();

        void onSearchError();
    }
}
