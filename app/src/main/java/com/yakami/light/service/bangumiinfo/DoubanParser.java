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
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import okhttp3.ResponseBody;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

/**
 * Created by Yakami on 2016/8/6, enjoying it!
 */

public class DoubanParser extends BaseInfoParser {

    private String mDoubanHost = "https://movie.douban.com";

    @Override
    public void start(String name) {
        super.start(name);
        setOnGetBaiduResultListener(elements -> {
            mAddressList = getAddress(elements);
            if (mAddressList.size() > 0) {
                mStepListener.print(AppManager.getRes().getString(R.string.get_raw_data));
                parse(mAddressList.get(0));
            } else
                mStepListener.onSearchError();
        });
        getBaiduResult(BangumiNameService.newInstance(mName).getTrueName() + " site:douban.com");
    }

    private List<String> getAddress(Elements elements) {
        List<String> result = new ArrayList<>();
        for (Element element : elements) {
            Element tmp = element.getElementsByClass("f13").get(0);
            String text = tmp.text();
            if (text.contains("movie.douban.com/subje"))
                result.add(tmp.getElementsByTag("a").get(0).attr("href"));
        }
        return result;
    }

    private void parse(String address) {
        if (Tools.isEmpty(address)) {
            return;
        }

        ServerAPI.getBangumiInfoAPI()
                .getCutomUrl(address)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe((ResponseBody body) -> {
                    try {
                        String result = new String(body.bytes(), "utf-8");
                        if (isEpisode(result)) {
                            parse(getMainSubject(result));
                        } else
                            getData(result);
                    } catch (Throwable e) {
                        throw new RuntimeException("解析失败");
                    }
                }, throwable -> {
                    throwable.printStackTrace();
                    if (mAddressList.size() > 1) {
                        mAddressList.remove(0);
                        parse(mAddressList.get(0));
                    } else {
                        mStepListener.onSearchError();
                    }
                });
    }

    /**
     * 有可能拿到的是具体第几集的subject，需要判断
     *
     * @param html
     * @return
     */
    private boolean isEpisode(String html) {
        Document doc = Jsoup.parse(html);
        String text = doc.getElementsByTag("h1").get(0).text();
        Pattern pattern = Pattern.compile("第.{1,3}[集回话]");
        Matcher matcher = pattern.matcher(text);
        return matcher.find();
    }

    private String getMainSubject(String episodeHtml) {
        Document doc = Jsoup.parse(episodeHtml);
        String address = doc.getElementsByTag("h1").get(0).getElementsByTag("a").attr("href");
        return mDoubanHost + address;
    }

    private void getData(String html) {
        Document doc = Jsoup.parse(html);
        //名称
        mInfo.setName(doc.getElementsByTag("h1").get(0).getElementsByTag("span").text());
        //cover
        mInfo.setCoverAddress(doc.getElementsByClass("nbgnbg").get(0).getElementsByTag("img").attr("src"));
        getImageBytes(mInfo.getCoverAddress());
        //intro
        mInfo.setIntro(doc.getElementById("link-report").text().trim());
        //info box
        String infoHtml = doc.getElementById("info").html();
        infoHtml = infoHtml.replaceAll("<div id=\"info\">.*</div>", "");
        for (String item : infoHtml.split("<br>")) {
            mInfo.getInfoBox().add(item.replaceAll("<.*?>", "").replaceAll("\n", "").trim());
        }
        mInfo.getInfoBox().add("来源:豆瓣");

        mInfoListener.onGetTextInfo(mInfo);
    }
}
