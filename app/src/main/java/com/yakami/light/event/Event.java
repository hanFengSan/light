package com.yakami.light.event;

/**
 * Created by Yakami on 2016/3/30.
 */
@SuppressWarnings("all")
public class Event<T> {

    public enum EventType {
        LOAD_ERROR, //用于通知LoadFragment，显示相关信息
        LOADED_SUCCESSFUL,

        INIT_TAB_LAYOUT, //初始化tablayout
        TAB_SELECT,  //tablayout的选择
        SET_UPDATE_TIME, //更新时间

        SET_ABOVE_NAVIGATION_BAR, //修复导航栏透明遮挡内容以及不满内容一页的情况

        REFRESH, //通知刷新
        REFRESH_COMPLETED,  //刷新完成,关闭刷新显示
        SHOW_LOADING, //主动通知显示刷新状态

        BANGUMI_PARSE_INFO,  //数据解析进度消息
        BANGUMI_SEARCH_RESULT_GET, //得到搜索结果
        BANGUMI_SEARCH_RESULT_FAIL,
        BANGUMI_NETWORK_OR_SERVER_ERROR,
        BANGUMI_TEXT_INFO_GET, //得到番剧文本消息
        BANGUMI_TEXT_INFO_FAIL,
        BANGUMI_COVER_GET, //得到番剧封面
        BANGUMI_COVER_FAIL,
        BANGUMI_COLOR_GET, //得到dominant color

        DETAIL_FRAGMENT_HEIGHT, //DetailActivity中的各个fragment的高度

        VERSION_DOWNLOAD, //更新
        VERSION_DIALOG,
    }

    public EventType type;
    public T message;

    public static <O> Event<O> just(O t) {
        Event<O> event = new Event<>();
        event.message = t;
        return event;
    }

    //用于转换类型，否则直接取message会是object类型
    public <T> T getMessage() {
        return (T) message;
    }

}


