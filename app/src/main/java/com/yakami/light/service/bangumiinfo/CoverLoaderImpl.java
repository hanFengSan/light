package com.yakami.light.service.bangumiinfo;

import android.support.annotation.Nullable;

import com.yakami.light.ServerAPI;
import com.yakami.light.bean.BangumiInfo;

import java.util.ArrayList;
import java.util.List;

import okhttp3.ResponseBody;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

/**
 * Created by Yakami on 2016/8/7, enjoying it!
 */

public class CoverLoaderImpl implements CoverLoader {

    private OnGetCoverListener mListener;
    private List<BangumiInfoParser> mParserList;
    private String mName;
    private String mNameForUrl;

    @Override
    public void setOnGetCoverListener(OnGetCoverListener listener) {
        mListener = listener;
    }

    @Override
    public void getByName(String bangumiName) {
        mName = bangumiName;

        mParserList = new ArrayList<>();
        mParserList.add(new BGMParser());
        mParserList.add(new DoubanParser());

        parse();
    }

    private void parse() {
        if (mParserList == null || mParserList.size() == 0) {
            mListener.error();
            return;
        }
        BangumiInfoParser parser = mParserList.get(0);
        parser.setOnGetInfoListener(new BangumiInfoParser.OnGetInfoListener() {
            @Override
            public void onGetTextInfo(BangumiInfo info) {
            }

            @Override
            public void onGetFullInfo(BangumiInfo info) {
                mListener.get(info.getCover());
            }
        });
        parser.setOnGetStepInfoListener(new BangumiInfoParser.OnGetStepInfoListener() {
            @Override
            public void print(String info) {

            }

            @Override
            public void onGetCoverError() {
                if (mParserList.size() != 0) {
                    mParserList.remove(0);
                    parse();
                }
            }

            @Override
            public void onSearchError() {
                if (mParserList.size() != 0) {
                    mParserList.remove(0);
                    parse();
                }
            }
        });
        parser.start(mName);
    }

    @Override
    public void getByUrl(String url, @Nullable String name) {
        mNameForUrl = name;
        getImageBytes(url);
    }

    private void toNameSolution() {
        if (mNameForUrl != null)
            getByName(mNameForUrl);
        else
            mListener.error();
    }

    protected void getImageBytes(String src) {
        ServerAPI.getBangumiInfoAPI()
                .getCutomUrl(src)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe((ResponseBody body) -> {
                    try {
                        mListener.get(body.bytes());
                    } catch (Exception e) {
                        e.printStackTrace();
                        toNameSolution();
                    }
                }, throwable -> {
                    throwable.printStackTrace();
                    toNameSolution();
                });
    }

}
