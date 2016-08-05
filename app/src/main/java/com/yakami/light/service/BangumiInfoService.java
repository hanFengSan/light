package com.yakami.light.service;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

import com.yakami.light.R;
import com.yakami.light.ServerAPI;
import com.yakami.light.bean.BangumiInfo;
import com.yakami.light.event.Event;
import com.yakami.light.event.RxBus;
import com.yakami.light.model.CacheManager;
import com.yakami.light.service.base.BaseService;
import com.yakami.light.utils.DominantColorUtils;
import com.yakami.light.utils.Tools;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.util.ArrayList;
import java.util.List;

import okhttp3.ResponseBody;
import rx.Observable;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

/**
 * Created by Yakami on 2016/7/31, enjoying it!
 * 主要用于爬去bangumi.tv的番剧介绍
 */

public class BangumiInfoService extends BaseService {

    private String mName;
    private List<String> mBlackList = new ArrayList<>();
    private BangumiInfo mInfo = new BangumiInfo();
    private boolean mIsCompleted; //parseBGM()用
    private List<String> mAddressList;
    private static final String CACHE_PREFIX = "BANGUMI_INFO_";
    private String mCacheName;

    private BangumiInfoService() {
    }

    public static BangumiInfoService newInstance(String name, int id) {
        return newInstance(name, id, 1.0f);
    }

    public static BangumiInfoService newInstance(String name, int id, float version) {
        BangumiInfoService presenter = new BangumiInfoService();
        presenter.mName = name;
        presenter.mInfo.setId(id);
        presenter.mInfo.setVersion(version);
        presenter.addBlackList(new String[]{"DVD", "限定版", "配信版", "Blu-ray", "REBOOT", "第.*?松",
                "特装限定", "剧场版", "BD", "BOX", "上卷", "下卷", "´", "【尼限定】", "Vol.*?001", "第.*?卷",
                "Vol.*?[1-9]", "Ⅱ", "初回盘", "初回", "第.*?集", "[1-9]", "[0-9][0-9]"});
        return presenter;
    }

    private String getCacheName() {
        if (Tools.isAvailableStr(mCacheName))
            return mCacheName;
        mCacheName = CACHE_PREFIX + Tools.getStrHash(cleanName(mName));
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

    public void addBlackStr(String str) {
        mBlackList.add(str);
    }

    public void addBlackList(String[] array) {
        for (String item : array)
            mBlackList.add(item);
    }

    /**
     * 清洁名称，过滤黑名单等
     */
    private String cleanName(String name) {
        //过滤正则表达式中的特殊字符
        String[] regBlackArray = {"*", ".", "?", "$", "^", "[", "]", "(", ")", "{", "}", "|", "\\", "/"};
        for (String arrayItem : regBlackArray)
            name = name.replace(arrayItem, " ");
        for (String item : mBlackList)
            name = name.replaceAll(item, " ");
        return name.trim();
    }

    public BangumiInfoService start() {
        mInfo.setVersion(1.3f);
        //判断缓存是否可以，图片是否已缓存
        if (isExistCache()) {
            BangumiInfo tmpInfo = readCache();
            if (tmpInfo.getVersion() >= mInfo.getVersion()) {
                mInfo = tmpInfo;
                if (Tools.isAvailableStr(mInfo.getCoverAddress()) && mInfo.getCover().length == 0) {
                    setImageBytes(mInfo.getCoverAddress());
                    RxBus.getInstance().send(Event.EventType.BANGUMI_TEXT_INFO_GET, null);
                } else if (mInfo.getCover().length > 0) {
                    RxBus.getInstance().send(Event.EventType.BANGUMI_TEXT_INFO_GET, null);
                    RxBus.getInstance().send(Event.EventType.BANGUMI_COVER_GET, null);
                }
                if (mInfo.getDominantColor() == 0)
                    setDominantColor();
                else
                    RxBus.getInstance().send(Event.EventType.BANGUMI_COLOR_GET, null);
                return this;
            }
        }

        //获取百度结果
        ServerAPI.getBangumiInfoAPI()
                .getSearchResult("utf-8", cleanName(mName) + " site:bangumi.tv")
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.newThread())
                .subscribe((ResponseBody body) -> {
                    try {
                        mAddressList = getBGMCacheAddress(body.string());
                        if (mAddressList.size() > 0) {
                            RxBus.getInstance().send(Event.EventType.BANGUMI_SEARCH_RESULT_GET, null);
                            parseBGM(mAddressList.get(0));
                        } else
                            RxBus.getInstance().send(Event.EventType.BANGUMI_SEARCH_RESULT_FAIL, null);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }, throwable -> {
                    throwable.printStackTrace();
                    RxBus.getInstance().send(Event.EventType.BANGUMI_NETWORK_OR_SERVER_ERROR, null);
                });
        return this;
    }

    /**
     * 获取到bangumi.tv对应番剧介绍的地址（利用百度快照，bgm那破服务器= =)
     */
    private List<String> getBGMCacheAddress(String html) {
        Document doc = Jsoup.parse(html);
        List<String> result = new ArrayList<>();
        Elements elements = doc.getElementsByClass("f13");
        for (Element item : elements) {
            if (item.text().contains("bangumi.tv/subject/")) {
                result.add(item.getElementsByClass("m").get(0).attr("href"));
            }
        }
        return result;
    }

    private void parseBGM(String address) {
        if (Tools.isEmpty(address)) {
            return;
        }

        ServerAPI.getBangumiInfoAPI()
                .getBGMResult(address)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.io())
                .subscribe((ResponseBody body) -> {
                    try {
                        String result = new String(body.bytes(), "GB18030");
                        //有一定几率百度快照会跳往原网页
                        if (result.substring(0, 100).contains("utf-8"))
                            getData(new String(body.bytes(), "utf-8"));
                        else
                            getData(result);
                        mIsCompleted = true;
                    } catch (Throwable e) {
                        e.printStackTrace();
                    }
                }, throwable -> {
                    throwable.printStackTrace();
                    if (mAddressList.size() > 1) {
                        mAddressList.remove(0);
                        parseBGM(mAddressList.get(0));
                    } else {
                        RxBus.getInstance().send(Event.EventType.BANGUMI_TEXT_INFO_FAIL, null);
                    }
                });

    }

    private void getData(String html) {
        Document doc = Jsoup.parse(html);
        //名称
        for (Element element : doc.getElementsByClass("nameSingle").get(0).getElementsByTag("a")) {
            if (element.attr("href").contains("/subject/")) {
                mInfo.setName(element.text());
                mInfo.setcName(element.attr("title"));
            }
        }
        //cover
        Elements tmpList = doc.getElementsByClass("cover");
        for (Element tmp : tmpList) {
            if (tmp.tagName().equals("img")) {
                mInfo.setCoverAddress(tmp.attr("src"));
                setImageBytes(tmp.attr("src"));
            }
        }
        //intro
        if (doc.getElementById("subject_summary") != null) {
            mInfo.setIntro(doc.getElementById("subject_summary").text());
        } else {
            mInfo.setIntro(getRes().getString(R.string.none));
        }
        //info box
        if (doc.getElementById("infobox") != null)
            for (Element item : doc.getElementById("infobox").getElementsByTag("li")) {
                mInfo.getInfoBox().add(item.text());
            }
        RxBus.getInstance().send(Event.EventType.BANGUMI_TEXT_INFO_GET, null);
        saveCache();
    }

    private void setImageBytes(String src) {
        ServerAPI.getBangumiInfoAPI()
                .getBGMResult(src)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.io())
                .subscribe((ResponseBody body) -> {
                    try {
                        mInfo.setCover(body.bytes());
                        RxBus.getInstance().send(Event.EventType.BANGUMI_COVER_GET, null);
                        saveCache();
                        setDominantColor();
                    } catch (Throwable e) {
                        e.printStackTrace();
                    }
                }, throwable -> {
                    throwable.printStackTrace();
                    RxBus.getInstance().send(Event.EventType.BANGUMI_COVER_FAIL, null);
                });
    }

    private void setDominantColor() {
        Observable.just(BitmapFactory.decodeByteArray(mInfo.getCover(), 0, mInfo.getCover().length))
                .subscribeOn(Schedulers.computation())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe((Bitmap bitmap) -> {
                    if (bitmap != null) {
                        mInfo.setDominantColor(DominantColorUtils.getDominantColor(bitmap));
                        saveCache();
                        RxBus.getInstance().send(Event.EventType.BANGUMI_COLOR_GET, null);
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
