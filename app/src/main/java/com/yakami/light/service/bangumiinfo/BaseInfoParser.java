package com.yakami.light.service.bangumiinfo;

import android.support.annotation.CallSuper;

import com.yakami.light.AppManager;
import com.yakami.light.R;
import com.yakami.light.ServerAPI;
import com.yakami.light.bean.BangumiInfo;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;

import java.util.ArrayList;
import java.util.List;

import okhttp3.ResponseBody;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

/**
 * Created by Yakami on 2016/8/6, enjoying it!
 */

public abstract class BaseInfoParser implements BangumiInfoParser {

    protected String mName = "";
    protected BangumiInfo mInfo = new BangumiInfo();
    protected List<String> mAddressList = new ArrayList<>();
    protected OnGetInfoListener mInfoListener;
    protected OnGetStepInfoListener mStepListener;
    protected List<String> mInfoBox = new ArrayList<>();

    private OnGetBaiduResultListener mBaiduListener;

    interface OnGetBaiduResultListener {
        void onGetBaiduResult(Elements result);
    }

    @CallSuper
    @Override
    public void start(String name) {
        mName = name;
    }

    @Override
    public void setOnGetInfoListener(OnGetInfoListener listener) {
        mInfoListener = listener;
    }

    @Override
    public void setOnGetStepInfoListener(OnGetStepInfoListener listener) {
        mStepListener = listener;
    }

    protected void setOnGetBaiduResultListener(OnGetBaiduResultListener listener) {
        mBaiduListener = listener;
    }

    protected void print(String str) {
        if (mStepListener != null)
            mStepListener.print(str);
    }

    protected void printGettingCoverError() {
        if (mStepListener != null)
            mStepListener.onGetCoverError();
    }

    protected void getBaiduResult(String keyword) {
        ServerAPI.getBangumiInfoAPI()
                .getSearchResult("utf-8", keyword)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.io())
                .subscribe((ResponseBody body) -> {
                    try {
                        mBaiduListener.onGetBaiduResult(getSearchResultList(body.string()));
                    } catch (Exception e) {
                        e.printStackTrace();
                        throw new RuntimeException(AppManager.getRes().getString(R.string.network_or_server_error));
                    }
                }, throwable -> {
                    throwable.printStackTrace();
                    mStepListener.print(AppManager.getRes().getString(R.string.network_or_server_error));
                });
    }

    private Elements getSearchResultList(String html) {
        Document doc = Jsoup.parse(html);
        List<String> result = new ArrayList<>();
        Elements elements = doc.getElementsByClass("result");
        return elements;
    }

    protected void getImageBytes(String src) {
        ServerAPI.getBangumiInfoAPI()
                .getCutomUrl(src)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe((ResponseBody body) -> {
                    try {
                        mInfo.setCover(body.bytes());
                        mInfoListener.onGetFullInfo(mInfo);
                    } catch (Throwable e) {
                        e.printStackTrace();
                        throw new RuntimeException("切换图片加载Utils");
                    }
                }, throwable -> {
                    throwable.printStackTrace();
                    printGettingCoverError();
                });
    }


}
