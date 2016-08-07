package com.yakami.light.service.bangumiinfo;

import com.yakami.light.AppManager;
import com.yakami.light.R;
import com.yakami.light.ServerAPI;
import com.yakami.light.service.BangumiNameService;
import com.yakami.light.utils.Tools;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.util.ArrayList;
import java.util.List;

import okhttp3.ResponseBody;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

/**
 * Created by Yakami on 2016/8/6, enjoying it!
 * bangumi.tv解析的实现
 */

public class BGMParser extends BaseInfoParser {


    @Override
    public void start(String name) {
        super.start(name);
        setOnGetBaiduResultListener(elements -> {
            mAddressList = getBGMCacheAddress(elements);
            if (mAddressList.size() > 0) {
                print(AppManager.getRes().getString(R.string.get_raw_data));
                parseBGM(mAddressList.get(0));
            } else
                mStepListener.onSearchError();
        });
        getBaiduResult(BangumiNameService.newInstance(mName).getTrueName() + " site:bangumi.tv");
    }

    private List<String> getBGMCacheAddress(Elements elements) {
        List<String> result = new ArrayList<>();
        for (Element element : elements) {
            Element tmp =  element.getElementsByClass("f13").get(0);
            if (tmp.text().contains("bangumi.tv/subject/"))
                result.add(tmp.getElementsByClass("m").get(0).attr("href"));
        }
        return result;
    }

    private void parseBGM(String address) {
        if (Tools.isEmpty(address)) {
            return;
        }

        ServerAPI.getBangumiInfoAPI()
                .getCutomUrl(address)
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
                    } catch (Throwable e) {
                        throw new RuntimeException("解析失败");
                    }
                }, throwable -> {
                    throwable.printStackTrace();
                    if (mAddressList.size() > 1) {
                        mAddressList.remove(0);
                        parseBGM(mAddressList.get(0));
                    } else {
                        print(AppManager.getRes().getString(R.string.load_raw_data_failed));
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
                mInfo.setCoverAddress((tmp.attr("src")));
                break;
            }
        }
        getImageBytes(mInfo.getCoverAddress());
        //intro
        if (doc.getElementById("subject_summary") != null) {
            String intro = doc.getElementById("subject_summary").text();
            if (Tools.isAvailableStr(intro))
                mInfo.setIntro(doc.getElementById("subject_summary").text());
            else
                mInfo.setIntro(AppManager.getRes().getString(R.string.no_info));
        } else {
            mInfo.setIntro(AppManager.getRes().getString(R.string.no_info));
        }
        //info box
        if (doc.getElementById("infobox") != null)
            for (Element item : doc.getElementById("infobox").getElementsByTag("li")) {
                mInfoBox.add(item.text());
            }
        mInfoBox.add("来源:bangumi.tv");
        mInfo.setInfoBox(mInfoBox);
        mInfoListener.onGetTextInfo(mInfo);
    }
}
