package com.yakami.light.service.bangumiinfo;

import android.support.annotation.Nullable;

/**
 * Created by Yakami on 2016/8/7, enjoying it!
 */

public interface CoverLoader {

    interface OnGetCoverListener {

        void get(byte[] cover);

        void error();
    }

    void setOnGetCoverListener(OnGetCoverListener listener);

    void getByName(String bangumiName);

    void getByUrl(String url, @Nullable String name);

}
