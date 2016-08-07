package com.yakami.light.service;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.support.annotation.Nullable;

import com.yakami.light.bean.BangumiInfo;
import com.yakami.light.event.Event;
import com.yakami.light.event.RxBus;
import com.yakami.light.model.CacheManager;
import com.yakami.light.service.bangumiinfo.BGMParser;
import com.yakami.light.service.bangumiinfo.BangumiInfoParser;
import com.yakami.light.service.bangumiinfo.CoverLoader;
import com.yakami.light.service.bangumiinfo.CoverLoaderImpl;
import com.yakami.light.service.bangumiinfo.DoubanParser;
import com.yakami.light.service.base.BaseService;
import com.yakami.light.utils.DominantColorUtils;
import com.yakami.light.utils.Tools;

import java.util.ArrayList;
import java.util.List;

import rx.Observable;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

import static com.yakami.light.event.Event.EventType.BANGUMI_PARSE_INFO;

/**
 * Created by Yakami on 2016/7/31, enjoying it!
 * 主要用于爬去bangumi.tv的番剧介绍
 */
public class BangumiInfoService extends BaseService {

    private String mDirtyName;
    private String mTrueName;
    private BangumiInfo mInfo = new BangumiInfo();
    private List<String> mAddressList;
    private static final String CACHE_PREFIX = "BANGUMI_INFO_";
    private String mCacheName;
    private List<BangumiInfoParser> mParserList = new ArrayList<>();

    private BangumiInfoService() {
    }

    public static BangumiInfoService newInstance(String name, int id) {
        return newInstance(name, id, 1.0f);
    }

    public static BangumiInfoService newInstance(String name, int id, float version) {
        BangumiInfoService presenter = new BangumiInfoService();
        presenter.mParserList.add(new BGMParser());
        presenter.mParserList.add(new DoubanParser());
        presenter.mDirtyName = name;
        presenter.mInfo.setId(id);
        presenter.mInfo.setVersion(version);
        presenter.mTrueName = BangumiNameService.newInstance(name).getTrueName();
        return presenter;
    }

    private String getCacheName() {
        if (Tools.isAvailableStr(mCacheName))
            return mCacheName;
        mCacheName = CACHE_PREFIX + Tools.getStrHash((mTrueName));
        return mCacheName;
    }

    private boolean isExistCache() {
        return CacheManager.isExist4DataCache(getContext(), getCacheName());
    }

    private BangumiInfo readCache() {
        return CacheManager.readObject(getContext(), getCacheName());
    }

    private void saveCache() {
        CacheManager.saveObject(getContext(), mInfo, getCacheName());
    }

    public void setParserList(List<BangumiInfoParser> parserList) {
        mParserList = parserList;
    }

    private boolean isExistAvailableCache() {
        if (isExistCache()) {
            BangumiInfo tmpInfo = readCache();
            if (tmpInfo != null && tmpInfo.getVersion() >= mInfo.getVersion()) {
                return true;
            }
        }
        return false;
    }

    private void loadImageByCoverLoader(@Nullable String name, @Nullable String address, boolean isByName) {
        CoverLoader coverLoader = new CoverLoaderImpl();
        coverLoader.setOnGetCoverListener(new CoverLoader.OnGetCoverListener() {
            @Override
            public void get(byte[] cover) {
                mInfo.setCover(cover);
                setDominantColor();
                saveCache();
                RxBus.getInstance().send(Event.EventType.BANGUMI_COVER_GET, null);
            }

            @Override
            public void error() {
                RxBus.getInstance().send(Event.EventType.BANGUMI_COVER_FAIL, null);
            }
        });
        if (isByName)
            coverLoader.getByName(name);
        else
            coverLoader.getByUrl(mInfo.getCoverAddress(), mTrueName);
    }


    private void loadInfoFromCache() {
        mInfo = readCache();
        if (Tools.isAvailableStr(mInfo.getCoverAddress())) {
            RxBus.getInstance().send(Event.EventType.BANGUMI_TEXT_INFO_GET, null);
        }
        if (mInfo.getCover().length > 0) {
            RxBus.getInstance().send(Event.EventType.BANGUMI_COVER_GET, null);
        } else {
            loadImageByCoverLoader(mTrueName, mInfo.getCoverAddress(), false);
        }
        if (mInfo.getDominantColor() == 0)
            setDominantColor();
        else
            RxBus.getInstance().send(Event.EventType.BANGUMI_COLOR_GET, null);
    }

    public void start() {
        mInfo.setVersion(1.6f);
        BangumiInfoParser parser = mParserList.get(0);
        //判断缓存是否存在，图片是否已缓存
        if (isExistAvailableCache()) {
            loadInfoFromCache();
            return;
        }

        parser.setOnGetInfoListener(new BangumiInfoParser.OnGetInfoListener() {
            @Override
            public void onGetTextInfo(BangumiInfo info) {
                onGetInfo(info);
                RxBus.getInstance().send(Event.EventType.BANGUMI_TEXT_INFO_GET, null);
            }

            @Override
            public void onGetFullInfo(BangumiInfo info) {
                onGetInfo(info);
                setDominantColor();
                RxBus.getInstance().send(Event.EventType.BANGUMI_COVER_GET, null);
                saveCache();
            }
        });

        parser.setOnGetStepInfoListener(new BangumiInfoParser.OnGetStepInfoListener() {
            @Override
            public void print(String info) {
                Event event = new Event();
                event.type = BANGUMI_PARSE_INFO;
                event.message = info;
                RxBus.getInstance().send(event);
            }

            @Override
            public void onGetCoverError() {
                //启用强力封面解决方案
                loadImageByCoverLoader(mTrueName, null, true);
            }

            @Override
            public void onSearchError() {
                //更换解决方案
                mParserList.remove(0);
                if (mParserList == null || mParserList.size() == 0)
                    RxBus.getInstance().send(Event.EventType.BANGUMI_SEARCH_RESULT_FAIL, null);
                else
                    start();
            }
        });
        parser.start(mTrueName);
    }


    private void onGetInfo(BangumiInfo info) {
        info.setVersion(mInfo.getVersion());
        mInfo = info;
        saveCache();
    }

    private void setDominantColor() {
        if (mInfo.getCover().length == 0)
            return;
        Observable.just(BitmapFactory.decodeByteArray(mInfo.getCover(), 0, mInfo.getCover().length))
                .subscribeOn(Schedulers.computation())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe((Bitmap bitmap) -> {
                    if (bitmap != null) {
                        int color = DominantColorUtils.getDominantColor(bitmap);
                        if (!Tools.isGrey(color)) {
                            mInfo.setDominantColor(DominantColorUtils.getDominantColor(bitmap));
                            saveCache();
                            RxBus.getInstance().send(Event.EventType.BANGUMI_COLOR_GET, null);
                        }
                    }
                });
    }

    public String getName() {
        return mInfo.getName();
    }

    public String getcName() {
        return mInfo.getcName();
    }

    public List<String> getInfoBox() {
        return mInfo.getInfoBox();
    }

    public Bitmap getCover() {
        return BitmapFactory.decodeByteArray(mInfo.getCover(), 0, mInfo.getCover().length);
    }

    public String getIntro() {
        return mInfo.getIntro();
    }

    public int getDominantColor() {
        return mInfo.getDominantColor();
    }


}
